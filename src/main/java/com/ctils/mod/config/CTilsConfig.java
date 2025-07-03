package com.ctils.mod.config;

import com.ctils.mod.CTilsClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CTilsConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("ctils.json");
    
    // HUD settings
    private boolean overlayEnabled = true;
    private int overlayX = 5;
    private int overlayY = 5;
    private boolean showTimer = true;
    private boolean showRunCount = true;
    private boolean showDrops = true;
    private boolean showPersonalBest = true;
    private boolean showAverageTime = true;
    
    // Dungeon detection settings
    private String dungeonStartTrigger = "Dungeon Started";
    private String dungeonEndTrigger = "Dungeon Complete";
    
    // Item drop detection
    private String[] rareItemKeywords = {"Legendary", "Epic", "Rare", "Unique"};
    
    public void load() {
        File configFile = CONFIG_PATH.toFile();
        
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                CTilsConfig loaded = GSON.fromJson(reader, CTilsConfig.class);
                
                // Copy values from loaded config
                this.overlayEnabled = loaded.overlayEnabled;
                this.overlayX = loaded.overlayX;
                this.overlayY = loaded.overlayY;
                this.showTimer = loaded.showTimer;
                this.showRunCount = loaded.showRunCount;
                this.showDrops = loaded.showDrops;
                this.showPersonalBest = loaded.showPersonalBest;
                this.showAverageTime = loaded.showAverageTime;
                this.dungeonStartTrigger = loaded.dungeonStartTrigger;
                this.dungeonEndTrigger = loaded.dungeonEndTrigger;
                this.rareItemKeywords = loaded.rareItemKeywords;
                
                CTilsClient.LOGGER.info("Loaded CTils configuration");
            } catch (IOException e) {
                CTilsClient.LOGGER.error("Failed to load CTils configuration", e);
                save(); // Create default config
            }
        } else {
            save(); // Create default config
        }
    }
    
    public void save() {
        File configFile = CONFIG_PATH.toFile();
        
        try {
            if (!configFile.exists()) {
                if (!configFile.getParentFile().exists()) {
                    Files.createDirectories(configFile.getParentFile().toPath());
                }
                configFile.createNewFile();
            }
            
            try (FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(this, writer);
                CTilsClient.LOGGER.info("Saved CTils configuration");
            }
        } catch (IOException e) {
            CTilsClient.LOGGER.error("Failed to save CTils configuration", e);
        }
    }
    
    // Getters and Setters
    public boolean isOverlayEnabled() {
        return overlayEnabled;
    }
    
    public void setOverlayEnabled(boolean overlayEnabled) {
        this.overlayEnabled = overlayEnabled;
        save();
    }
    
    public int getOverlayX() {
        return overlayX;
    }
    
    public void setOverlayX(int overlayX) {
        this.overlayX = overlayX;
        save();
    }
    
    public int getOverlayY() {
        return overlayY;
    }
    
    public void setOverlayY(int overlayY) {
        this.overlayY = overlayY;
        save();
    }
    
    public boolean isShowTimer() {
        return showTimer;
    }
    
    public void setShowTimer(boolean showTimer) {
        this.showTimer = showTimer;
        save();
    }
    
    public boolean isShowRunCount() {
        return showRunCount;
    }
    
    public void setShowRunCount(boolean showRunCount) {
        this.showRunCount = showRunCount;
        save();
    }
    
    public boolean isShowDrops() {
        return showDrops;
    }
    
    public void setShowDrops(boolean showDrops) {
        this.showDrops = showDrops;
        save();
    }
    
    public boolean isShowPersonalBest() {
        return showPersonalBest;
    }
    
    public void setShowPersonalBest(boolean showPersonalBest) {
        this.showPersonalBest = showPersonalBest;
        save();
    }
    
    public boolean isShowAverageTime() {
        return showAverageTime;
    }
    
    public void setShowAverageTime(boolean showAverageTime) {
        this.showAverageTime = showAverageTime;
        save();
    }
    
    public String getDungeonStartTrigger() {
        return dungeonStartTrigger;
    }
    
    public void setDungeonStartTrigger(String dungeonStartTrigger) {
        this.dungeonStartTrigger = dungeonStartTrigger;
        save();
    }
    
    public String getDungeonEndTrigger() {
        return dungeonEndTrigger;
    }
    
    public void setDungeonEndTrigger(String dungeonEndTrigger) {
        this.dungeonEndTrigger = dungeonEndTrigger;
        save();
    }
    
    public String[] getRareItemKeywords() {
        return rareItemKeywords;
    }
    
    public void setRareItemKeywords(String[] rareItemKeywords) {
        this.rareItemKeywords = rareItemKeywords;
        save();
    }
}
