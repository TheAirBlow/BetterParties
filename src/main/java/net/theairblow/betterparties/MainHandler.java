package net.theairblow.betterparties;
import betterquesting.api.api.QuestingAPI;
import betterquesting.api.questing.IQuest;
import betterquesting.api.questing.party.IParty;
import betterquesting.api2.storage.DBEntry;
import betterquesting.network.handlers.NetQuestEdit;
import betterquesting.questing.QuestDatabase;
import betterquesting.questing.party.PartyManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class MainHandler {
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntityLiving().world.isRemote) return;
        if(!(event.getEntityLiving() instanceof EntityPlayerMP)) return;
        EntityPlayerMP player = (EntityPlayerMP)event.getEntityLiving();
        UUID uuid = QuestingAPI.getQuestingUUID(player);
        if (player.ticksExisted%200 != 0) return; // Do it every 10 seconds
        DBEntry<IParty> partyEntry = PartyManager.INSTANCE.getParty(uuid);
        if (partyEntry == null) return; // Player doesn't have a party
        List<DBEntry<IQuest>> quests = QuestDatabase.INSTANCE.getEntries();
        List<Integer> toComplete = new ArrayList<>();
        for (UUID memID : partyEntry.getValue().getMembers()) {
            for(DBEntry<IQuest> quest : quests) {
                if (!quest.getValue().isComplete(memID)) continue;
                if (quest.getValue().isComplete(uuid)) continue;
                toComplete.add(quest.getID());
            }
        }

        NetQuestEdit.setQuestStates(toComplete.stream()
                .mapToInt(Integer::intValue).toArray(), true, uuid);
    }
}
