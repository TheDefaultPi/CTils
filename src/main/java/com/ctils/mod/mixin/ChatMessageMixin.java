package com.ctils.mod.mixin;

import com.ctils.mod.CTilsClient;
import com.ctils.mod.config.CTilsConfig;
import com.ctils.mod.stats.DungeonStatsTracker;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.message.MessageType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ClientPlayNetworkHandler.class)
public class ChatMessageMixin {
    
    @Inject(method = "handleSystemMessage", at = @At("HEAD"))
    private void onChatMessage(Text message, boolean overlay, CallbackInfo ci) {
        String msgString = message.getString();
        DungeonStatsTracker statsTracker = CTilsClient.getInstance().getStatsTracker();
        CTilsConfig config = CTilsClient.getInstance().getConfig();
        
        // Check for dungeon start/end triggers
        if (msgString.contains(config.getDungeonStartTrigger())) {
            statsTracker.startDungeon();
        } else if (msgString.contains(config.getDungeonEndTrigger())) {
            statsTracker.endDungeon();
        }
        
        // Check for rare item drops
        for (String keyword : config.getRareItemKeywords()) {
            if (msgString.contains(keyword)) {
                // Try to extract item name
                statsTracker.registerItemDrop(extractItemName(msgString, keyword), System.currentTimeMillis());
                break;
            }
        }
    }
    
    private String extractItemName(String message, String keyword) {
        // Very basic item name extraction - this could be improved
        int keywordIndex = message.indexOf(keyword);
        if (keywordIndex >= 0) {
            int start = Math.max(0, keywordIndex - 20);
            int end = Math.min(message.length(), keywordIndex + 30);
            return message.substring(start, end).trim();
        }
        return keyword + " Item";
    }
}
