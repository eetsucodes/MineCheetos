package dev.minecheetos;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class MineCheetosScreen extends Screen {
    public MineCheetosScreen() {
        super(Component.translatable("screen.minecheetos.title"));
    }

    @Override
    protected void init() {
        Minecraft client = Minecraft.getInstance();
        Button flightButton = this.addRenderableWidget(
                Button.builder(
                        Component.translatable("screen.minecheetos.flight"),
                        button -> MineCheetosClient.requestFlightToggle(client)
                ).bounds(this.width / 2 - 100, this.height / 2 - 10, 200, 20).build()
        );
        flightButton.active = MineCheetosClient.canToggleFlight(client);

        this.addRenderableWidget(
                Button.builder(
                        Component.translatable("screen.minecheetos.close"),
                        button -> this.onClose()
                ).bounds(this.width / 2 - 100, this.height / 2 + 20, 200, 20).build()
        );
    }

    @Override
    public void extractRenderState(
            GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.centeredText(this.font, this.title, this.width / 2, this.height / 2 - 55, 0xFFFFFF);
        graphics.centeredText(
                this.font,
                Component.translatable("screen.minecheetos.features"),
                this.width / 2,
                this.height / 2 - 35,
                0xAAAAAA
        );
    }
}
