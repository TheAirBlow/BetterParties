package net.theairblow.betterparties;

import betterquesting.api.api.QuestingAPI;
import betterquesting.api.questing.IQuest;
import betterquesting.api.questing.party.IParty;
import betterquesting.api2.storage.DBEntry;
import betterquesting.commands.QuestCommandBase;
import betterquesting.network.handlers.NetQuestEdit;
import betterquesting.questing.QuestDatabase;
import betterquesting.questing.party.PartyManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.permission.DefaultPermissionLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestCommandSync extends QuestCommandBase {
    @Override
    public String getCommand() {
        return "sync";
    }

    @Override
    public void runCommand(MinecraftServer server, CommandBase command, ICommandSender sender, String[] args) {
        if(!(sender instanceof EntityPlayerMP)) return;
        EntityPlayerMP player = (EntityPlayerMP)sender;
        UUID uuid = QuestingAPI.getQuestingUUID(player);
        DBEntry<IParty> party = PartyManager.INSTANCE.getParty(uuid);
        if (party == null) {
            sender.sendMessage(new TextComponentString(
                    "You must be in a party to use this command!"));
            return;
        }
        List<Integer> toComplete = new ArrayList<>();
        List<DBEntry<IQuest>> quests = QuestDatabase.INSTANCE.getEntries();
        for (UUID memID : party.getValue().getMembers()) {
            if (memID == uuid) continue; // skip self
            for(DBEntry<IQuest> quest : quests) {
                if (!quest.getValue().isComplete(memID)) continue;
                if (quest.getValue().isComplete(uuid)) continue;
                toComplete.add(quest.getID());
            }
        }

        NetQuestEdit.setQuestStates(toComplete.stream()
                .mapToInt(Integer::intValue).toArray(), true, uuid);
        sender.sendMessage(new TextComponentString(
                "Successfully synchronized your quest progress with your party!"));
    }

    @Override
    public String getPermissionNode() {
        return "betterparties.command.user.sync";
    }

    @Override
    public DefaultPermissionLevel getPermissionLevel() {
        return DefaultPermissionLevel.ALL;
    }

    @Override
    public String getPermissionDescription() {
        return "Permission to manually sync quest completion with your team";
    }
}
