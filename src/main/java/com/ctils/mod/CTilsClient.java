package com.ctils.mod;

import com.ctils.mod.config.CTilsConfig;
import com.ctils.mod.event.KeyInputHandler;
import com.ctils.mod.gui.HudOverlay;
import com.ctils.mod.stats.DungeonStatsTracker;
import com.ctils.mod.command.CTilsCommands;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CTilsClient implements ClientModInitializer {
    public static final String MOD_ID = "ctils";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    private static CTilsClient INSTANCE;
    private DungeonStatsTracker statsTracker;
    private HudOverlay hudOverlay;
    private CTilsConfig config;
    
    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        LOGGER.info("Initializing CTils dungeon utility mod");
        
        // Load configuration
        this.config = new CTilsConfig();
        config.load();
        
        // Initialize components
        this.statsTracker = new DungeonStatsTracker();
        this.hudOverlay = new HudOverlay(statsTracker);
        
        // Register event handlers
        KeyInputHandler.register();
        CTilsCommands.register();
        
        // Register HUD renderer
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            if (MinecraftClient.getInstance().player != null && config.isOverlayEnabled()) {
                hudOverlay.render(matrixStack);
            }
        });
        
        // Register tick event for timer updates
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                statsTracker.onTick();
            }
        });
        
        LOGGER.info("CTils initialization complete!");
    }
    
    public static CTilsClient getInstance() {
        return INSTANCE;
    }
    
    public DungeonStatsTracker getStatsTracker() {
        return statsTracker;
    }
    
    public CTilsConfig getConfig() {
        return config;
    }
    
    public HudOverlay getHudOverlay() {
        return hudOverlay;
    }
}
