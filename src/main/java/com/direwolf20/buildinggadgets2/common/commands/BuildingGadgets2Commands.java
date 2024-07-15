package com.direwolf20.buildinggadgets2.common.commands;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.CommandEvent;

public class BuildingGadgets2Commands {
    @SubscribeEvent
    public static void registerCommands(CommandEvent event) {

        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("buildinggadgets2");

        // sub commands
        registerCommand(builder, "redprints", RedprintCommand::register);

        // register final command
        event.getDispatcher().register(builder);
    }

    /** Registers a sub command for the root BG2 command */
    private static void registerCommand(LiteralArgumentBuilder<CommandSourceStack> root, String name, Consumer<LiteralArgumentBuilder<CommandSourceStack>> consumer) {
        LiteralArgumentBuilder<CommandSourceStack> subCommand = Commands.literal(name);
        consumer.accept(subCommand);
        root.then(subCommand);
    }
}
