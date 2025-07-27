package com.github.shinjoy991.superskillssystem.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.resources.ResourceLocation;

public class ScaledStatImageButton extends ImageButton {
    private final float scale;
    private long clickedTime = -1;
    private boolean textureHover = false;
    private boolean delayPopUp = false;

    public ScaledStatImageButton(int x, int y, int originalWidth, int originalHeight,
                                 int u, int v, int hoverOffsetV,
                                 ResourceLocation texture, int texWidth, int texHeight,
                                 float scale, boolean delayPopUp,
                                 OnPress onPress) {
        super(x, y, (int) (originalWidth * scale), (int) (originalHeight * scale),
                u, v, hoverOffsetV, texture, texWidth, texHeight, onPress);
        this.scale = scale;
        this.delayPopUp = delayPopUp;
    }

    @Override
    public void onPress() {
        super.onPress();
        if (delayPopUp) {
            clickedTime = Util.getMillis();
            textureHover = !textureHover;
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();

        guiGraphics.pose().translate(this.getX() * (1.0F - scale), this.getY() * (1.0F - scale), 0);
        guiGraphics.pose().scale(scale, scale, 1.0F);

        // Kiểm tra thời gian giữ nhấn
        if (this.textureHover && Util.getMillis() - this.clickedTime > 1000L) {
            this.textureHover = false;
            this.isHovered = this.isMouseOver(mouseX, mouseY);
            this.setFocused(false);
        }

        // This below is modified method in super class
        int i = this.yTexStart;
        if (!this.isActive()) {
            i = this.yTexStart + this.yDiffTex * 2;
        } else if (this.isHoveredOrFocused()) {
            i = this.yTexStart + this.yDiffTex;
        }

        RenderSystem.enableDepthTest();
        guiGraphics.blit(this.resourceLocation, this.getX(), this.getY(),
                (float) this.xTexStart, (float) i,
                (int) (this.width / scale),(int) (this.height / scale),
                this.textureWidth, this.textureHeight);

        guiGraphics.pose().popPose();
    }
}