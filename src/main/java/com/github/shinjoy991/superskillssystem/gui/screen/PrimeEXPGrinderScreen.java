package com.github.shinjoy991.superskillssystem.gui.screen;

import com.github.shinjoy991.superskillssystem.SSS;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.github.shinjoy991.superskillssystem.gui.menu.PrimeEXPGrinderMenu;

@OnlyIn(Dist.CLIENT)
public class PrimeEXPGrinderScreen extends AbstractContainerScreen<PrimeEXPGrinderMenu> {
    private static final ResourceLocation PRIME_EXP_GRINDER_LOC = new ResourceLocation(SSS.MODID, "textures/gui/container/prime_exp_grinder.png");

    public PrimeEXPGrinderScreen(PrimeEXPGrinderMenu p_98782_, Inventory p_98783_, Component p_98784_) {
        super(p_98782_, p_98783_, p_98784_);
    }

    public void render(GuiGraphics p_283326_, int p_281847_, int p_283310_, float p_283486_) {
        this.renderBackground(p_283326_);
        this.renderBg(p_283326_, p_283486_, p_281847_, p_283310_);
        super.render(p_283326_, p_281847_, p_283310_, p_283486_);
        this.renderTooltip(p_283326_, p_281847_, p_283310_);
    }

    protected void renderBg(GuiGraphics p_281991_, float p_282138_, int p_282937_, int p_281956_) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        p_281991_.blit(PRIME_EXP_GRINDER_LOC, i, j, 0, 0, this.imageWidth, this.imageHeight);
        if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(2).hasItem()) {
            p_281991_.blit(PRIME_EXP_GRINDER_LOC, i + 92, j + 31, this.imageWidth, 0, 28, 21);
        }

    }
}