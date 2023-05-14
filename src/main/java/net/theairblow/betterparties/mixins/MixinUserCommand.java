package net.theairblow.betterparties.mixins;

import betterquesting.commands.BQ_CommandUser;
import betterquesting.commands.QuestCommandBase;
import net.theairblow.betterparties.MainConfig;
import net.theairblow.betterparties.QuestCommandSync;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = BQ_CommandUser.class, remap = false)
public class MixinUserCommand {
    @Shadow
    private List<QuestCommandBase> coms;

    @Inject(method = "<init>", at = @At("HEAD"))
    public void init(CallbackInfo ci) {
        if (MainConfig.syncCommand)
            coms.add(new QuestCommandSync());
    }
}
