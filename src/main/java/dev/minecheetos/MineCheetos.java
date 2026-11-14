package dev.minecheetos;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Abilities;

public final class MineCheetos implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                registerFlightCommand(dispatcher)
        );
    }

    private static void registerFlightCommand(
            CommandDispatcher<CommandSourceStack> dispatcher
    ) {
        dispatcher.register(Commands.literal("minecheetos")
                .then(Commands.literal("flight")
                        .requires(source -> source.getEntity() instanceof ServerPlayer)
                        .executes(context -> toggleFlight(context.getSource()))
                )
        );
    }

    private static int toggleFlight(CommandSourceStack source) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Only players can toggle flight."));
            return 0;
        }

        MinecraftServer server = source.getServer();
        if (server == null || !server.isSingleplayer() || server.isPublished()) {
            source.sendFailure(Component.literal("Flight toggle is only available in a local single-player world."));
            return 0;
        }

        if (player.isCreative() || player.isSpectator()) {
            source.sendFailure(Component.literal("Flight toggle is for survival and adventure modes."));
            return 0;
        }

        Abilities abilities = player.getAbilities();
        boolean enableFlight = !abilities.mayfly;
        abilities.mayfly = enableFlight;
        abilities.flying = enableFlight;
        player.onUpdateAbilities();

        String message = enableFlight ? "Flight enabled." : "Flight disabled.";
        source.sendSuccess(() -> Component.literal(message), false);
        return 1;
    }
}
