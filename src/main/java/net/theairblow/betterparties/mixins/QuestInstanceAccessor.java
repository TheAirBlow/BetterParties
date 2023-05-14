package net.theairblow.betterparties.mixins;

import betterquesting.questing.QuestInstance;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.HashMap;
import java.util.UUID;

@Mixin(QuestInstance.class)
public interface QuestInstanceAccessor {
    @Accessor("completeUsers")
    HashMap<UUID, NBTTagCompound> getCompleteUsers();
}
