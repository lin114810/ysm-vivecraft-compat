package com.example.ysmvivecraft;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * YSM Vivecraft Compat - Compatibility mod between Yes Steve Model (YSM) and Vivecraft
 *
 * <p>This mod bridges the gap between YSM's custom player model system and Vivecraft's
 * VR tracking system, enabling proper VR body tracking to drive YSM model animations.</p>
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Maps Vivecraft VR controller/headset data to YSM animation variables</li>
 *   <li>Prevents rendering conflicts between YSM's model layers and Vivecraft's VR hands</li>
 *   <li>Provides proper arm/body rotation when in VR mode</li>
 *   <li>Supports VR hand tracking for YSM model controllers</li>
 * </ul>
 */
@Mod(YSMVivecraftCompat.MOD_ID)
public class YSMVivecraftCompat {

    public static final String MOD_ID = "ysm_vivecraft_compat";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public YSMVivecraftCompat(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("[YSM-Vivecraft] Initializing YSM Vivecraft Compat mod...");

        modEventBus.addListener(this::commonSetup);

        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(this::clientSetup);
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("[YSM-Vivecraft] Common setup complete.");
    }

    private void clientSetup(FMLClientSetupEvent event) {
        LOGGER.info("[YSM-Vivecraft] Client setup complete.");
        LOGGER.info("[YSM-Vivecraft] YSM + Vivecraft compatibility active.");
    }
}
