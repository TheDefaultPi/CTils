package com.ctils.mod.stats;

import com.ctils.mod.CTilsClient;
import com.ctils.mod.config.CTilsConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DungeonStatsTracker {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File STATS_FILE = FabricLoader.getInstance().getConfigDir().resolve("ctils_stats.json").toFile();
    
    private boolean inDungeon = false;
    private long dungeonStartTime = 0;
    private long dungeonEndTime = 0;
    private int completedRuns = 0;
    private long bestTime = Long.MAX_VALUE;
    private long sessionStartTime = 0;
    private int sessionRuns = 0;
    private List<Long> completionTimes = new ArrayList<>();
    private List<ItemDrop> recentDrops = new ArrayList<>();
    
    public DungeonStatsTracker() {
        sessionStartTime = System.currentTimeMillis();
        loadStats();
    }
    
    public void onTick() {
        if (inDungeon) {
            // Update timer logic here if needed
        }
    }
    
    public void startDungeon() {
        if (!inDungeon) {
            inDungeon = true;
            dungeonStartTime = System.currentTimeMillis();
            CTilsClient.LOGGER.info("Dungeon run started");
        }
    }
    
    public void endDungeon() {
        if (inDungeon) {
            inDungeon = false;
            dungeonEndTime = System.currentTimeMillis();
            long runTime = dungeonEndTime - dungeonStartTime;
            
            completedRuns++;
            sessionRuns++;
            completionTimes.add(runTime);
            
            if (runTime < bestTime) {
                bestTime = runTime;
            }
            
            CTilsClient.LOGGER.info("Dungeon run completed in " + formatTime(runTime));
            saveStats();
        }
    }
    
    public void registerItemDrop(String itemName, long timestamp) {
        ItemDrop drop = new ItemDrop(itemName, timestamp);
        recentDrops.add(0, drop); // Add to beginning of list
        
        // Keep only the 5 most recent drops
        while (recentDrops.size() > 5) {
            recentDrops.remove(recentDrops.size() - 1);
        }
        
        saveStats();
    }
    
    public long getCurrentRunTime() {
        if (!inDungeon) {
            return 0;
        }
        return System.currentTimeMillis() - dungeonStartTime;
    }
    
    public long getAverageTime() {
        if (completionTimes.isEmpty()) {
            return 0;
        }
        
        long sum = 0;
        for (Long time : completionTimes) {
            sum += time;
        }
        return sum / completionTimes.size();
    }
    
    public long getSessionTime() {
        return System.currentTimeMillis() - sessionStartTime;
    }
    
    private void loadStats() {
        if (STATS_FILE.exists()) {
            try (FileReader reader = new FileReader(STATS_FILE)) {
                StatsData data = GSON.fromJson(reader, StatsData.class);
                this.completedRuns = data.completedRuns;
                this.bestTime = data.bestTime;
                this.completionTimes = data.completionTimes;
                this.recentDrops = data.recentDrops;
                
                CTilsClient.LOGGER.info("Loaded CTils dungeon stats");
            } catch (IOException e) {
                CTilsClient.LOGGER.error("Failed to load CTils dungeon stats", e);
            }
        }
    }
    
    private void saveStats() {
        try {
            if (!STATS_FILE.exists()) {
                STATS_FILE.getParentFile().mkdirs();
                STATS_FILE.createNewFile();
            }
            
            StatsData data = new StatsData();
            data.completedRuns = this.completedRuns;
            data.bestTime = this.bestTime;
            data.completionTimes = this.completionTimes;
            data.recentDrops = this.recentDrops;
            
            try (FileWriter writer = new FileWriter(STATS_FILE)) {
                GSON.toJson(data, writer);
                CTilsClient.LOGGER.info("Saved CTils dungeon stats");
            }
        } catch (IOException e) {
            CTilsClient.LOGGER.error("Failed to save CTils dungeon stats", e);
        }
    }
    
    public static String formatTime(long millis) {
        return String.format("%02d:%02d.%03d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1),
                millis % 1000);
    }
    
    // Getters
    public boolean isInDungeon() {
        return inDungeon;
    }
    
    public int getCompletedRuns() {
        return completedRuns;
    }
    
    public long getBestTime() {
        return bestTime;
    }
    
    public int getSessionRuns() {
        return sessionRuns;
    }
    
    public List<ItemDrop> getRecentDrops() {
        return recentDrops;
    }
    
    // Data classes for serialization
    private static class StatsData {
        int completedRuns;
        long bestTime;
        List<Long> completionTimes;
        List<ItemDrop> recentDrops;
    }
    
    public static class ItemDrop {
        private final String itemName;
        private final long timestamp;
        
        public ItemDrop(String itemName, long timestamp) {
            this.itemName = itemName;
            this.timestamp = timestamp;
        }
        
        public String getItemName() {
            return itemName;
        }
        
        public long getTimestamp() {
            return timestamp;
        }
        
        public String getFormattedTimestamp() {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            return format.format(new Date(timestamp));
        }
    }
}
