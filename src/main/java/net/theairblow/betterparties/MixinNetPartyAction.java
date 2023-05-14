package net.theairblow.betterparties;

import betterquesting.api.api.QuestingAPI;
import betterquesting.api.questing.IQuest;
import betterquesting.api.questing.party.IParty;
import betterquesting.api2.storage.DBEntry;
import betterquesting.network.handlers.NetPartyAction;
import betterquesting.network.handlers.NetQuestEdit;
import betterquesting.questing.QuestDatabase;
import betterquesting.questing.party.PartyManager;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(value = NetPartyAction.class, remap = false)
public class MixinNetPartyAction {
    @Inject(method = "acceptInvite", at = @At(value = "INVOKE", target = "Lbetterquesting/network/handlers/NetNameSync;quickSync"))
    public void acceptInvite(int partyID, EntityPlayerMP sender, CallbackInfo ci) {
        if (MainConfig.syncOnJoin) {
            UUID uuid = QuestingAPI.getQuestingUUID(sender);
            DBEntry<IParty> party = PartyManager.INSTANCE.getParty(uuid);
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
        }
    }
}
