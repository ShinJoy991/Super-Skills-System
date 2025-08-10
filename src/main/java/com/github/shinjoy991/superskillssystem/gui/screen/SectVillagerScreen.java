package com.github.shinjoy991.superskillssystem.gui.screen;

import com.github.shinjoy991.superskillssystem.SSS;
import com.github.shinjoy991.superskillssystem.gui.menu.SectVillagerMenu;
import com.github.shinjoy991.superskillssystem.helpers.PlayerClientData;
import com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkill;
import com.github.shinjoy991.superskillssystem.network.ModNetworking;
import com.github.shinjoy991.superskillssystem.network.server.SectSelectTradePacketC2S;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SectVillagerScreen extends AbstractContainerScreen<SectVillagerMenu> {

    private static final ResourceLocation SECT_VILLAGER_LOC =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/gui/container/sect_villager.png");

    private static final int TEXTURE_WIDTH = 512;
    private static final int TEXTURE_HEIGHT = 256;
    private static final int MERCHANT_MENU_PART_X = 99;
    private static final int PROGRESS_BAR_X = 136;
    private static final int PROGRESS_BAR_Y = 16;
    private static final int SELL_ITEM_1_X = 5;
    private static final int SELL_ITEM_2_X = 35;
    private static final int BUY_ITEM_X = 68;
    private static final int LABEL_Y = 6;
    private static final int NUMBER_OF_OFFER_BUTTONS = 7;
    private static final int TRADE_BUTTON_X = 5;
    private static final int TRADE_BUTTON_HEIGHT = 20;
    private static final int TRADE_BUTTON_WIDTH = 88;
    private static final int SCROLLER_HEIGHT = 27;
    private static final int SCROLLER_WIDTH = 6;
    private static final int SCROLL_BAR_HEIGHT = 139;
    private static final int SCROLL_BAR_TOP_POS_Y = 18;
    private static final int SCROLL_BAR_START_X = 94;
    private static final Component SKILLS_LABEL = Component.translatable("label.sect_villager.skills");
    private static final Component SKILL_MASTER_LABEL = Component.translatable("label.sect_villager.skill_master");
    private static final Component LEVEL_SEPARATOR = Component.literal(" - ");
    private static final Component DEPRECATED_TOOLTIP = Component.translatable("merchant.deprecated");
    private int shopItem;
    private final SectVillagerScreen.TradeOfferButton[] tradeOfferButtons = new SectVillagerScreen.TradeOfferButton[7];
    int scrollOff;
    private boolean isDragging;
    private int selectedTradeIndex = 0;

    public SectVillagerScreen(SectVillagerMenu p_99123_, Inventory p_99124_, Component title) {
        super(p_99123_, p_99124_, title);
        this.imageWidth = 276;
        this.inventoryLabelX = 107;
    }

    private void postButtonClick() {
        this.menu.setSelectionHint(this.shopItem);
        this.menu.tryMoveItems(this.shopItem);
        ModNetworking.INSTANCE.sendToServer(new SectSelectTradePacketC2S(this.shopItem));

    }

    protected void init() {
        super.init();
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        int k = j + 16 + 2;

        for(int l = 0; l < 7; ++l) {
            this.tradeOfferButtons[l] = this.addRenderableWidget(new SectVillagerScreen.TradeOfferButton(i + 5, k, l, (p_99174_) -> {
                if (p_99174_ instanceof SectVillagerScreen.TradeOfferButton) {
                    this.shopItem = ((SectVillagerScreen.TradeOfferButton)p_99174_).getIndex() + this.scrollOff;
                    this.postButtonClick();
                }

            }));
            k += 20;
        }

    }

    protected void renderLabels(GuiGraphics graphics, int p_282009_, int p_283691_) {
        Component component = SKILL_MASTER_LABEL.copy().append(LEVEL_SEPARATOR).append(this.menu.getSectType().translatableName())
                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD);
        int j = this.font.width(component);
        int k = 49 + this.imageWidth / 2 - j / 2;
        graphics.drawString(this.font, component, k, 6, 4210752, false);


        graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
        int l = this.font.width(SKILLS_LABEL);
        graphics.drawString(this.font, SKILLS_LABEL, 5 - l / 2 + 48, 6, 4210752, false);
    }

    protected void renderBg(GuiGraphics p_283072_, float p_281275_, int p_282312_, int p_282984_) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        p_283072_.blit(SECT_VILLAGER_LOC, i, j, 0, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 512, 256);
//        MerchantOffers merchantoffers = this.menu.getOffers();
//        if (!merchantoffers.isEmpty()) {
//            int k = this.shopItem;
//            if (k < 0 || k >= merchantoffers.size()) {
//                return;
//            }
//
//            MerchantOffer merchantoffer = merchantoffers.get(k);
//            if (merchantoffer.isOutOfStock()) {
//                p_283072_.blit(SECT_VILLAGER_LOC, this.leftPos + 83 + 99, this.topPos + 35, 0, 311.0F, 0.0F, 28, 21, 512, 256);
//            }
//        }

    }

    private void renderScroller(GuiGraphics p_283030_, int p_283154_, int p_281664_, MerchantOffers p_282877_) {
        int i = p_282877_.size() + 1 - 7;
        if (i > 1) {
            int j = 139 - (27 + (i - 1) * 139 / i);
            int k = 1 + j / i + 139 / i;
            int l = 113;
            int i1 = Math.min(113, this.scrollOff * k);
            if (this.scrollOff == i - 1) {
                i1 = 113;
            }

            p_283030_.blit(SECT_VILLAGER_LOC, p_283154_ + 94, p_281664_ + 18 + i1, 0, 0.0F, 199.0F, 6, 27, 512, 256);
        } else {
            p_283030_.blit(SECT_VILLAGER_LOC, p_283154_ + 94, p_281664_ + 18, 0, 6.0F, 199.0F, 6, 27, 512, 256);
        }

    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        MerchantOffers merchantoffers = this.menu.getOffers();
        if (!merchantoffers.isEmpty()) {
            int i = (this.width - this.imageWidth) / 2;
            int j = (this.height - this.imageHeight) / 2;
            int k = j + 16 + 1;
            int l = i + 5 + 5;
            this.renderScroller(guiGraphics, i, j, merchantoffers);

            if (this.selectedTradeIndex >= 0 && this.selectedTradeIndex < merchantoffers.size()) {

                MerchantOffer merchantoffer = merchantoffers.get(selectedTradeIndex);

                ItemStack itemstack = merchantoffer.getBaseCostA();
                ItemStack itemstack1 = merchantoffer.getCostA();
                ItemStack itemstack2 = merchantoffer.getCostB();
                ItemStack itemstack3 = merchantoffer.getResult();

                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);

                int j1 = k + 2 + 5;
                int newOffsetA = 126;

                // render cost A
                this.renderAndDecorateCostA(guiGraphics, itemstack1, itemstack, l + newOffsetA, j1);

                // render cost B nếu có
                if (!itemstack2.isEmpty()) {
                    int newOffsetB = 122;
                    guiGraphics.renderFakeItem(itemstack2, i + 5 + 35 + newOffsetB, j1);
                    guiGraphics.renderItemDecorations(this.font, itemstack2, i + 5 + 35 + newOffsetB, j1);
                }

                // render result
                int newOffsetResult = 147;
                guiGraphics.renderFakeItem(itemstack3, i + 5 + 68 + newOffsetResult, j1);
                guiGraphics.renderItemDecorations(this.font, itemstack3, i + 5 + 68 + newOffsetResult, j1);

                guiGraphics.pose().popPose();
            }

            // Kiểm tra vùng hover cho price1
            if (isMouseOverPrice1(mouseX, mouseY)) {
                MerchantOffer offer = merchantoffers.get(selectedTradeIndex);
                ItemStack price1 = offer.getBaseCostA();
                guiGraphics.renderTooltip(this.font, price1, mouseX, mouseY);
            }

            // Kiểm tra vùng hover cho price2
            if (isMouseOverPrice2(mouseX, mouseY)) {
                MerchantOffer offer = merchantoffers.get(selectedTradeIndex);
                ItemStack price2 = offer.getCostB();
                guiGraphics.renderTooltip(this.font, price2, mouseX, mouseY);
            }

            // Kiểm tra vùng hover cho result
            if (isMouseOverResult(mouseX, mouseY)) {
                MerchantOffer offer = merchantoffers.get(selectedTradeIndex);
                ItemStack result = offer.getResult();
                guiGraphics.renderTooltip(this.font, result, mouseX, mouseY);
            }
            RenderSystem.enableDepthTest();
        }

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.7f, 0.7f, 1.0f);
        for(SectVillagerScreen.TradeOfferButton btn : this.tradeOfferButtons) {
            btn.visible = btn.index < merchantoffers.size();
            // draw text on the button
            if (btn.visible) {
                btn.setMessage(PlayerClientData.warriorGlobalPassiveSkills.get(btn.index).getTranslatableName());
            }
        }
        guiGraphics.pose().popPose();

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private boolean isMouseOverPrice1(double mouseX, double mouseY) {
        int regionX = this.leftPos + 136; // vị trí X trong GUI
            int regionY = this.topPos + 25;   // vị trí Y trong GUI
            int regionWidth = 16;             // chiều rộng vùng
            int regionHeight = 16;            // chiều cao vùng
        return mouseX >= regionX && mouseX < regionX + regionWidth &&
                mouseY >= regionY && mouseY < regionY + regionHeight;
    }

    private boolean isMouseOverPrice2(double mouseX, double mouseY) {
        int price2X = this.leftPos + 162; // leftPos là offset của GUI
        int price2Y = this.topPos + 25;
        int width = 16;
        int height = 16;

        return mouseX >= price2X && mouseX < price2X + width &&
                mouseY >= price2Y && mouseY < price2Y + height;
    }
    private boolean isMouseOverResult(double mouseX, double mouseY) {
        int resultX = this.leftPos + 220; // leftPos là offset của GUI
        int resultY = this.topPos + 25;
        int width = 16;
        int height = 16;

        return mouseX >= resultX && mouseX < resultX + width &&
                mouseY >= resultY && mouseY < resultY + height;
    }

    private void renderButtonArrows(GuiGraphics p_283020_, MerchantOffer p_281926_, int p_282752_, int p_282179_) {
        RenderSystem.enableBlend();
        if (p_281926_.isOutOfStock()) {
            p_283020_.blit(SECT_VILLAGER_LOC, p_282752_ + 5 + 35 + 20, p_282179_ + 3, 0, 25.0F, 171.0F, 10, 9, 512, 256);
        } else {
            p_283020_.blit(SECT_VILLAGER_LOC, p_282752_ + 5 + 35 + 20, p_282179_ + 3, 0, 15.0F, 171.0F, 10, 9, 512, 256);
        }

    }

    private void renderAndDecorateCostA(GuiGraphics p_281357_, ItemStack p_283466_, ItemStack p_282046_, int p_282403_, int p_283601_) {
        p_281357_.renderFakeItem(p_283466_, p_282403_, p_283601_);
        if (p_282046_.getCount() == p_283466_.getCount()) {
            p_281357_.renderItemDecorations(this.font, p_283466_, p_282403_, p_283601_);
        } else {
            p_281357_.renderItemDecorations(this.font, p_282046_, p_282403_, p_283601_, p_282046_.getCount() == 1 ? "1" : null);
            // Forge: fixes Forge-8806, code for count rendering taken from GuiGraphics#renderGuiItemDecorations
            p_281357_.pose().pushPose();
            p_281357_.pose().translate(0.0F, 0.0F, 200.0F);
            String count = p_283466_.getCount() == 1 ? "1" : String.valueOf(p_283466_.getCount());
            font.drawInBatch(count, (float) (p_282403_ + 14) + 19 - 2 - font.width(count), (float)p_283601_ + 6 + 3, 0xFFFFFF, true, p_281357_.pose().last().pose(), p_281357_.bufferSource(), net.minecraft.client.gui.Font.DisplayMode.NORMAL, 0, 15728880, false);
            p_281357_.pose().popPose();
            p_281357_.pose().pushPose();
            p_281357_.pose().translate(0.0F, 0.0F, 300.0F);
            p_281357_.blit(SECT_VILLAGER_LOC, p_282403_ + 7, p_283601_ + 12, 0, 0.0F, 176.0F, 9, 2, 512, 256);
            p_281357_.pose().popPose();
        }

    }

    private boolean canScroll(int p_99141_) {
        return p_99141_ > 7;
    }

    public boolean mouseScrolled(double p_99127_, double p_99128_, double p_99129_) {
        int i = this.menu.getOffers().size();
        if (this.canScroll(i)) {
            int j = i - 7;
            this.scrollOff = Mth.clamp((int)((double)this.scrollOff - p_99129_), 0, j);
        }

        return true;
    }

    public boolean mouseDragged(double p_99135_, double p_99136_, int p_99137_, double p_99138_, double p_99139_) {
        int i = this.menu.getOffers().size();
        if (this.isDragging) {
            int j = this.topPos + 18;
            int k = j + 139;
            int l = i - 7;
            float f = ((float)p_99136_ - (float)j - 13.5F) / ((float)(k - j) - 27.0F);
            f = f * (float)l + 0.5F;
            this.scrollOff = Mth.clamp((int)f, 0, l);
            return true;
        } else {
            return super.mouseDragged(p_99135_, p_99136_, p_99137_, p_99138_, p_99139_);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int btn) {
        this.isDragging = false;
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        if (this.canScroll(this.menu.getOffers().size()) && mouseX > (double)(i + 94) && mouseX < (double)(i + 94 + 6) && mouseY > (double)(j + 18) && mouseY <= (double)(j + 18 + 139 + 1)) {
            this.isDragging = true;
        }
        // Kiểm tra click vào list trade
        for (int k = 0; k < this.menu.getOffers().size(); ++k) {
            int rowY = j + 18 + k * 20; // mỗi dòng cao 20px
            if (mouseX > i + 10 && mouseX < i + 90 // vùng X của trade
                    && mouseY > rowY && mouseY < rowY + 20) {
                this.selectedTradeIndex = k;
                break;
            }
        }
        return super.mouseClicked(mouseX, mouseY, btn);
    }

    @OnlyIn(Dist.CLIENT)
    class TradeOfferButton extends Button {
        final int index;

        public TradeOfferButton(int p_99205_, int p_99206_, int p_99207_, Button.OnPress p_99208_) {
            super(p_99205_, p_99206_, 88, 20, CommonComponents.EMPTY, p_99208_, DEFAULT_NARRATION);
            this.index = p_99207_;
            this.visible = false;
        }

        public int getIndex() {
            return this.index;
        }

        public void renderToolTip(GuiGraphics graphics, int mouseX, int mouseY) {
            if (this.isHovered && SectVillagerScreen.this.menu.getOffers().size() > this.index + SectVillagerScreen.this.scrollOff) {
                if (mouseX < this.getX() + 20) {
                    ItemStack itemstack = SectVillagerScreen.this.menu.getOffers().get(this.index + SectVillagerScreen.this.scrollOff).getCostA();
                    graphics.renderTooltip(SectVillagerScreen.this.font, itemstack, mouseX, mouseY);
                } else if (mouseX < this.getX() + 50 && mouseX > this.getX() + 30) {
                    ItemStack itemstack2 = SectVillagerScreen.this.menu.getOffers().get(this.index + SectVillagerScreen.this.scrollOff).getCostB();
                    if (!itemstack2.isEmpty()) {
                        graphics.renderTooltip(SectVillagerScreen.this.font, itemstack2, mouseX, mouseY);
                    }
                } else if (mouseX > this.getX() + 65) {
                    ItemStack itemstack1 = SectVillagerScreen.this.menu.getOffers().get(this.index + SectVillagerScreen.this.scrollOff).getResult();
                    graphics.renderTooltip(SectVillagerScreen.this.font, itemstack1, mouseX, mouseY);
                }
            }

        }
    }
}
