package net.theairblow.betterparties.mixins;

import betterquesting.api.api.QuestingAPI;
import betterquesting.api.questing.party.IParty;
import betterquesting.api2.cache.CapabilityProviderQuestCache;
import betterquesting.api2.cache.QuestCache;
import betterquesting.api2.storage.DBEntry;
import betterquesting.questing.QuestDatabase;
import betterquesting.questing.QuestInstance;
import betterquesting.questing.party.PartyManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.theairblow.betterparties.MainConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.UUID;

@Mixin(value = QuestInstance.class, remap = false)
public class MixinQuestInstance {
    @Inject(method = "setComplete", at = @At("TAIL"))
    public void setComplete(UUID uuid, long timestamp, CallbackInfo ci) {
        if (!MainConfig.sharedCompletion) return;
        QuestInstance instance = (QuestInstance)(Object)this;
        int questID = QuestDatabase.INSTANCE.getID(instance);
        QuestInstanceAccessor accessor = (QuestInstanceAccessor)instance;
        HashMap<UUID, NBTTagCompound> completeUsers = accessor.getCompleteUsers();
        synchronized(completeUsers) {
            DBEntry<IParty> partyEntry = PartyManager.INSTANCE.getParty(uuid);
            if (partyEntry != null) { // Player has a party
                for (UUID memID : partyEntry.getValue().getMembers()) {
                    NBTTagCompound entry = instance.getCompletionInfo(memID);
                    if(entry != null) continue; // Already complete
                    entry = new NBTTagCompound();
                    entry.setBoolean("claimed", false);
                    entry.setLong("timestamp", timestamp);
                    completeUsers.put(memID, entry);
                    EntityPlayerMP player2 = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(memID);
                    QuestCache qc2 = player2.getCapability(CapabilityProviderQuestCache.CAP_QUEST_CACHE, null);
                    if(qc2 != null) qc2.markQuestDirty(questID);
                }
            }
        }
    }

    @Inject(method = "claimReward", at = @At("TAIL"))
    public void claimReward(EntityPlayer player, CallbackInfo ci) {
        if (!MainConfig.sharedRewards) return;
        QuestInstance instance = (QuestInstance)(Object)this;
        int questID = QuestDatabase.INSTANCE.getID(instance);
        UUID uuid = QuestingAPI.getQuestingUUID(player);
        QuestInstanceAccessor accessor = (QuestInstanceAccessor)instance;
        HashMap<UUID, NBTTagCompound> completeUsers = accessor.getCompleteUsers();
        synchronized(completeUsers) {
            DBEntry<IParty> partyEntry = PartyManager.INSTANCE.getParty(uuid);
            if (partyEntry != null) { // Player has a party
                for (UUID memID : partyEntry.getValue().getMembers()) {
                    NBTTagCompound entry = instance.getCompletionInfo(memID);
                    if(entry != null) continue; // Already complete
                    entry = new NBTTagCompound();
                    entry.setBoolean("claimed", true);
                    entry.setLong("timestamp", System.currentTimeMillis());
                    completeUsers.put(memID, entry);
                    EntityPlayerMP player2 = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(memID);
                    QuestCache qc2 = player2.getCapability(CapabilityProviderQuestCache.CAP_QUEST_CACHE, null);
                    if(qc2 != null) qc2.markQuestDirty(questID);
                }
            }
        }
    }
}
