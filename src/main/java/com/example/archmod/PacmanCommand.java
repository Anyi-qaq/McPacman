package com.example.archmod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class PacmanCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("pacman")
                .then(CommandManager.argument("action", StringArgumentType.word())
                    .then(CommandManager.argument("modid", StringArgumentType.word())
                        .executes(context -> {
                            String action = StringArgumentType.getString(context, "action");
                            String modid = StringArgumentType.getString(context, "modid");
                            return execute(context.getSource(), action, modid);
                        }))));
        });
    }

    private static int execute(ServerCommandSource source, String action, String modid) {
        if (!source.hasPermissionLevel(0)) { // 允许所有玩家
            source.sendFeedback(Text.literal("你没有权限使用此命令"), false);
            return 0;
        }
        if ("install".equals(action)) {
            source.sendFeedback(Text.literal("正在查询Modrinth上的mod版本..."), false);
            ModrinthDownloader.showVersionSelection(source, modid);
        } else {
            source.sendFeedback(Text.literal("未知操作: " + action), false);
        }
        return 1;
    }
}