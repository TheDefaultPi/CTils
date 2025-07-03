package com.ctils.mod.command;

import com.ctils.mod.CTilsClient;
import com.ctils.mod.gui.CTilsConfigScreen;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class CTilsCommands {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            registerCommands(dispatcher);
        });
    }

    private static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("ctils")
            .executes(context -> {
                // Open config screen
                MinecraftClient.getInstance().setScreen(new CTilsConfigScreen());
                return 1;
            })
            .then(ClientCommandManager.literal("toggle")
                .executes(context -> {
                    boolean current = CTilsClient.getInstance().getConfig().isOverlayEnabled();
                    CTilsClient.getInstance().getConfig().setOverlayEnabled(!current);
                    sendFeedback(context, "Overlay " + (!current ? "enabled" : "disabled"));
                    return 1;
                }))
            .then(ClientCommandManager.literal("reset")
                .executes(context -> {
                    // Reset session stats
                    sendFeedback(context, "Reset session stats");
                    return 1;
                }))
            .then(ClientCommandManager.literal("help")
                .executes(context -> {
                    sendFeedback(context, "§9§lCTils Commands:");
                    sendFeedback(context, "§e/ctils §7- Open config screen");
                    sendFeedback(context, "§e/ctils toggle §7- Toggle overlay");
                    sendFeedback(context, "§e/ctils reset §7- Reset session stats");
                    return 1;
                }))
        );
    }

    private static void sendFeedback(CommandContext<FabricClientCommandSource> context, String message) {
        context.getSource().sendFeedback(Text.of("§9[CTils] §r" + message));
    }
}
