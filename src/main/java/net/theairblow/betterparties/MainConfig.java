package net.theairblow.betterparties;

import net.minecraftforge.common.config.Config;

@Config(modid = BetterParties.MOD_ID)
public class MainConfig {
    @Config.Comment({
            "Adds `/party_sync` to forcefully sync quests with all members",
            "WARNING: If your intent is to disallow syncing to new team members,",
            "set `Sync On Join` to false too, or else this change would be useless!"
    })
    @Config.Name("Sync Command")
    public static boolean syncCommand = true;

    @Config.Comment({
            "Quest completion is shared between team members"
    })
    @Config.Name("Shared Completion")
    public static boolean sharedCompletion = true;

    @Config.Comment({
            "Once any team member claimed a reward, it is claimed for others"
    })
    @Config.Name("Shared Rewards")
    public static boolean sharedRewards = false;

    @Config.Comment({
            "Synchronizes quests with team members on player joining to a party",
            "WARNING: If your intent is to disallow syncing to new team members,",
            "set `Sync Command` to false too, or else this change would be useless!"
    })
    @Config.Name("Sync On Join")
    public static boolean syncOnJoin = true;
}
