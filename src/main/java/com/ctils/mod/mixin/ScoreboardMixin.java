package com.ctils.mod.mixin;

import com.ctils.mod.CTilsClient;
import com.ctils.mod.stats.DungeonStatsTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ScoreboardMixin {
    
    @Inject(method = "onScoreboardObjectiveUpdate", at = @At("TAIL"))
    private void onScoreboardObjectiveUpdate(ScoreboardObjectiveUpdateS2CPacket packet, CallbackInfo ci) {
        checkForDungeonScoreboard();
    }
    
    @Inject(method = "onScoreboardDisplay", at = @At("TAIL"))
    private void onScoreboardDisplay(ScoreboardDisplayS2CPacket packet, CallbackInfo ci) {
        checkForDungeonScoreboard();
    }
    
    private void checkForDungeonScoreboard() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null) {
            Scoreboard scoreboard = client.world.getScoreboard();
            ScoreboardObjective objective = scoreboard.getObjectiveForSlot(1); // Side display
            
            if (objective != null) {
                String displayName = objective.getDisplayName().getString();
                DungeonStatsTracker statsTracker = CTilsClient.getInstance().getStatsTracker();
                
                // Check for dungeon indicators in scoreboard
                if (displayName.contains("Dungeon") && !statsTracker.isInDungeon()) {
                    statsTracker.startDungeon();
                } else if (displayName.contains("Results") && statsTracker.isInDungeon()) {
                    statsTracker.endDungeon();
                }
            }
        }
    }
}
