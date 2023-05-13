package net.theairblow.betterparties;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Logger;

@Mod(modid = BetterParties.MOD_ID, name = BetterParties.MOD_NAME, version = BetterParties.VERSION, serverSideOnly = true)
public class BetterParties {
    public static final String MOD_ID = "betterparties";
    public static final String MOD_NAME = "BetterParties";
    public static final String VERSION = "1.0.0";
    public static Logger LOGGER;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(MainHandler.class);
        LOGGER = event.getModLog();
    }
}
