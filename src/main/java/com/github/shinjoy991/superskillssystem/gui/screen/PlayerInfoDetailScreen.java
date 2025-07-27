package com.github.shinjoy991.superskillssystem.gui.screen;

import com.github.shinjoy991.superskillssystem.SSS;
import com.github.shinjoy991.superskillssystem.helpers.PlayerDataScreen;
import com.github.shinjoy991.superskillssystem.helpers.PlayerInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class PlayerInfoDetailScreen extends Screen {
    private static final ResourceLocation DETAIL_SCREEN_LOC =
            new ResourceLocation(SSS.MODID, "textures/gui/player_info_detail.png");
    private static final ResourceLocation PLAYER_INFO_WIDGET_LOC =
            new ResourceLocation(SSS.MODID, "textures/gui/player_info_widget.png");

    private final PlayerDataScreen screenData;

    private final Player player;

    private final int imageWidth = 222;
    private final int imageHeight = 208;

    private ImageButton infoButton;

    public PlayerInfoDetailScreen(PlayerDataScreen data) {
        super(Component.literal("Player Info Details"));
        this.screenData = data;
        this.player = Minecraft.getInstance().player;
    }

    @Override
    protected void init() {
        super.init();

        int i = (this.width - imageWidth) / 2;
        int j = (this.height - imageHeight) / 2;

        int buttonImageHeight = 256;
        int buttonImageWidth = 256;
        infoButton = new ImageButton(
                i + (imageWidth - 50) / 2 - 66,
                j + 185
                , 50, 13,
                0, 0, 13,  // u, v, hoverOffsetV
                PLAYER_INFO_WIDGET_LOC, buttonImageHeight, buttonImageWidth,
                (btn) -> this.minecraft.setScreen(new PlayerInfoScreen(this.screenData))
        );
        this.addRenderableWidget(infoButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        int i = (this.width - imageWidth) / 2;
        int j = (this.height - imageHeight) / 2;
        guiGraphics.blit(DETAIL_SCREEN_LOC, i, j, 0, 0, imageWidth, imageHeight);

        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        if (this.infoButton.isHovered()) {
            guiGraphics.renderTooltip(this.font,
                    Component.literal("View Player Info"),
                    mouseX, mouseY
            );
        }
    }
}