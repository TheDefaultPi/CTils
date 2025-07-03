package com.ctils.mod.mixin;

import com.ctils.mod.CTilsClient;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    
    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        // This is just to log when the title screen is loaded
        // Can be used for initialization if needed
        CTilsClient.LOGGER.info("CTils loaded successfully!");
    }
}
