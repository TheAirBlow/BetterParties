package net.theairblow.betterparties;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Arrays;
import java.util.List;

@Mod(modid = BetterParties.MOD_ID, name = BetterParties.MOD_NAME, version = BetterParties.VERSION,
        acceptableRemoteVersions = "*" /* Do not require on client, but allow it for singleplayer */)
public class BetterParties implements ILateMixinLoader {
    public static final String MOD_ID = "betterparties";
    public static final String MOD_NAME = "BetterParties";
    public static final String VERSION = "1.3.1";
    public static Logger LOGGER;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(MainHandler.class);
        LOGGER = event.getModLog();
    }

    @Mod.EventHandler
    public static void registerCommands(FMLServerStartingEvent event) {
        if (MainConfig.syncCommand) event.registerServerCommand(new SyncCommand());
    }

    @Override
    public List<String> getMixinConfigs() {
        return Arrays.asList("mixins.json");
    }
}
