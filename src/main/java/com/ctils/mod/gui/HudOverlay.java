package com.ctils.mod.gui;

import com.ctils.mod.CTilsClient;
import com.ctils.mod.config.CTilsConfig;
import com.ctils.mod.stats.DungeonStatsTracker;
import com.ctils.mod.stats.DungeonStatsTracker.ItemDrop;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class HudOverlay {
    private final DungeonStatsTracker statsTracker;
    private final MinecraftClient client;
    private final CTilsConfig config;
    
    public HudOverlay(DungeonStatsTracker statsTracker) {
        this.statsTracker = statsTracker;
        this.client = MinecraftClient.getInstance();
        this.config = CTilsClient.getInstance().getConfig();
    }
    
    public void render(MatrixStack matrices) {
        if (!config.isOverlayEnabled() || client.player == null) {
            return;
        }
        
        DrawContext context = new DrawContext(client, client.getBufferBuilders().getEntityVertexConsumers());
        TextRenderer textRenderer = client.textRenderer;
        
        int x = config.getOverlayX();
        int y = config.getOverlayY();
        
        // Background
        List<String> lines = new ArrayList<>();
        
        // Title
        lines.add("§9§lCTils Dungeon Stats");
        
        // Timer
        if (config.isShowTimer()) {
            if (statsTracker.isInDungeon()) {
                lines.add("§e§lTime: §r§f" + DungeonStatsTracker.formatTime(statsTracker.getCurrentRunTime()));
            } else {
                lines.add("§e§lTime: §r§7Not in dungeon");
            }
        }
        
        // Run count
        if (config.isShowRunCount()) {
            lines.add("§a§lRuns: §r§f" + statsTracker.getCompletedRuns());
        }
        
        // Personal best
        if (config.isShowPersonalBest() && statsTracker.getBestTime() < Long.MAX_VALUE) {
            lines.add("§b§lPB: §r§f" + DungeonStatsTracker.formatTime(statsTracker.getBestTime()));
        }
        
        // Average time
        if (config.isShowAverageTime() && statsTracker.getAverageTime() > 0) {
            lines.add("§d§lAvg: §r§f" + DungeonStatsTracker.formatTime(statsTracker.getAverageTime()));
        }
        
        // Session stats
        lines.add("§6§lSession: §r§f" + statsTracker.getSessionRuns() + " runs, " + 
                DungeonStatsTracker.formatTime(statsTracker.getSessionTime()));
        
        // Recent drops
        if (config.isShowDrops() && !statsTracker.getRecentDrops().isEmpty()) {
            lines.add("§c§lRecent Drops:");
            for (ItemDrop drop : statsTracker.getRecentDrops()) {
                lines.add("§8- §f" + drop.getItemName() + " §7(" + drop.getFormattedTimestamp() + ")");
            }
        }
        
        // Draw background
        int maxWidth = 0;
        for (String line : lines) {
            int width = textRenderer.getWidth(line);
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        
        context.fill(x - 3, y - 3, x + maxWidth + 3, y + (lines.size() * 10) + 3, 0x80000000);
        
        // Draw text
        for (int i = 0; i < lines.size(); i++) {
            context.drawText(textRenderer, Text.of(lines.get(i)), x, y + (i * 10), 0xFFFFFF, false);
        }
    }
    
    public void setPosition(int x, int y) {
        config.setOverlayX(x);
        config.setOverlayY(y);
    }
}
