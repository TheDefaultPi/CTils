package com.ctils.mod.gui;

import com.ctils.mod.CTilsClient;
import com.ctils.mod.config.CTilsConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class CTilsConfigScreen extends Screen {
    private final CTilsConfig config;
    private ButtonWidget toggleOverlayButton;
    private ButtonWidget toggleTimerButton;
    private ButtonWidget toggleRunCountButton;
    private ButtonWidget toggleDropsButton;
    private ButtonWidget togglePersonalBestButton;
    private ButtonWidget toggleAverageTimeButton;
    private TextFieldWidget dungeonStartTriggerField;
    private TextFieldWidget dungeonEndTriggerField;
    private boolean configChanged = false;
    
    public CTilsConfigScreen() {
        super(Text.of("CTils Configuration"));
        this.config = CTilsClient.getInstance().getConfig();
    }
    
    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = 50;
        int buttonWidth = 200;
        
        // Toggle Overlay
        toggleOverlayButton = this.addDrawableChild(ButtonWidget.builder(
            Text.of((config.isOverlayEnabled() ? "✓ " : "✗ ") + "Overlay Enabled"), 
            button -> {
                config.setOverlayEnabled(!config.isOverlayEnabled());
                button.setMessage(Text.of((config.isOverlayEnabled() ? "✓ " : "✗ ") + "Overlay Enabled"));
                configChanged = true;
            })
            .position(centerX - buttonWidth / 2, startY)
            .size(buttonWidth, 20)
            .build()
        );
        
        // Toggle Timer
        toggleTimerButton = this.addDrawableChild(ButtonWidget.builder(
            Text.of((config.isShowTimer() ? "✓ " : "✗ ") + "Show Timer"), 
            button -> {
                config.setShowTimer(!config.isShowTimer());
                button.setMessage(Text.of((config.isShowTimer() ? "✓ " : "✗ ") + "Show Timer"));
                configChanged = true;
            })
            .position(centerX - buttonWidth / 2, startY + 25)
            .size(buttonWidth, 20)
            .build()
        );
        
        // Toggle Run Count
        toggleRunCountButton = this.addDrawableChild(ButtonWidget.builder(
            Text.of((config.isShowRunCount() ? "✓ " : "✗ ") + "Show Run Count"), 
            button -> {
                config.setShowRunCount(!config.isShowRunCount());
                button.setMessage(Text.of((config.isShowRunCount() ? "✓ " : "✗ ") + "Show Run Count"));
                configChanged = true;
            })
            .position(centerX - buttonWidth / 2, startY + 50)
            .size(buttonWidth, 20)
            .build()
        );
        
        // Toggle Drops
        toggleDropsButton = this.addDrawableChild(ButtonWidget.builder(
            Text.of((config.isShowDrops() ? "✓ " : "✗ ") + "Show Item Drops"), 
            button -> {
                config.setShowDrops(!config.isShowDrops());
                button.setMessage(Text.of((config.isShowDrops() ? "✓ " : "✗ ") + "Show Item Drops"));
                configChanged = true;
            })
            .position(centerX - buttonWidth / 2, startY + 75)
            .size(buttonWidth, 20)
            .build()
        );
        
        // Toggle Personal Best
        togglePersonalBestButton = this.addDrawableChild(ButtonWidget.builder(
            Text.of((config.isShowPersonalBest() ? "✓ " : "✗ ") + "Show Personal Best"), 
            button -> {
                config.setShowPersonalBest(!config.isShowPersonalBest());
                button.setMessage(Text.of((config.isShowPersonalBest() ? "✓ " : "✗ ") + "Show Personal Best"));
                configChanged = true;
            })
            .position(centerX - buttonWidth / 2, startY + 100)
            .size(buttonWidth, 20)
            .build()
        );
        
        // Toggle Average Time
        toggleAverageTimeButton = this.addDrawableChild(ButtonWidget.builder(
            Text.of((config.isShowAverageTime() ? "✓ " : "✗ ") + "Show Average Time"), 
            button -> {
                config.setShowAverageTime(!config.isShowAverageTime());
                button.setMessage(Text.of((config.isShowAverageTime() ? "✓ " : "✗ ") + "Show Average Time"));
                configChanged = true;
            })
            .position(centerX - buttonWidth / 2, startY + 125)
            .size(buttonWidth, 20)
            .build()
        );
        
        // Dungeon Start Trigger
        this.addDrawableChild(ButtonWidget.builder(
            Text.of("Save"), 
            button -> {
                if (configChanged) {
                    config.save();
                    configChanged = false;
                }
                this.close();
            })
            .position(centerX - buttonWidth / 2, this.height - 50)
            .size(buttonWidth, 20)
            .build()
        );
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawCenteredTextWithShadow(this.textRenderer, "§9§lCTils Configuration", this.width / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public void close() {
        if (configChanged) {
            config.save();
        }
        super.close();
    }
}
