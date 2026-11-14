package dev.minecheetos;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;

public final class MineCheetosClient implements ClientModInitializer {
    private static final KeyMapping FLIGHT_KEY = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.minecheetos.flight",
                    InputConstants.KEY_G,
                    KeyMapping.Category.MISC
            )
    );
    private static final KeyMapping MENU_KEY = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.minecheetos.menu",
                    InputConstants.KEY_RSHIFT,
                    KeyMapping.Category.MISC
            )
    );

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (MENU_KEY.consumeClick()) {
                if (client.player != null) {
                    client.setScreenAndShow(new MineCheetosScreen());
                }
            }

            handleFlightKey(client);
        });
    }

    private static void handleFlightKey(Minecraft client) {
        while (FLIGHT_KEY.consumeClick()) {
            requestFlightToggle(client);
        }
    }

    static boolean canToggleFlight(Minecraft client) {
        IntegratedServer server = client.getSingleplayerServer();
        return client.player != null
                && server != null
                && !server.isPublished()
                && !client.player.isCreative()
                && !client.player.isSpectator();
    }

    static void requestFlightToggle(Minecraft client) {
        if (canToggleFlight(client)) {
            client.player.connection.sendCommand("minecheetos flight");
        }
    }
}
