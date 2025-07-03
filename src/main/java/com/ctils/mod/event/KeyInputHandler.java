package com.ctils.mod.event;

import com.ctils.mod.CTilsClient;
import com.ctils.mod.gui.CTilsConfigScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    private static final String CATEGORY = "key.category.ctils";
    private static final KeyBinding TOGGLE_OVERLAY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.ctils.toggle_overlay", 
            InputUtil.Type.KEYSYM, 
            GLFW.GLFW_KEY_RIGHT_BRACKET, 
            CATEGORY
    ));
    private static final KeyBinding OPEN_CONFIG = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.ctils.open_config", 
            InputUtil.Type.KEYSYM, 
            GLFW.GLFW_KEY_BACKSLASH, 
            CATEGORY
    ));
    
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (TOGGLE_OVERLAY.wasPressed()) {
                boolean current = CTilsClient.getInstance().getConfig().isOverlayEnabled();
                CTilsClient.getInstance().getConfig().setOverlayEnabled(!current);
                CTilsClient.LOGGER.info("Toggled CTils overlay: " + !current);
            }
            
            if (OPEN_CONFIG.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new CTilsConfigScreen());
            }
        });
    }
}
