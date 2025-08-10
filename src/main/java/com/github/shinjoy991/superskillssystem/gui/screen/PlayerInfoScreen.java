package com.github.shinjoy991.superskillssystem.gui.screen;

import com.github.shinjoy991.superskillssystem.gui.ScaledStatImageButton;
import com.github.shinjoy991.superskillssystem.helpers.PlayerClientData;
import com.github.shinjoy991.superskillssystem.network.ModNetworking;
import com.github.shinjoy991.superskillssystem.network.server.AttPointChangeRequestC2S;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.github.shinjoy991.superskillssystem.SSS;

@OnlyIn(Dist.CLIENT)
public class PlayerInfoScreen extends Screen {
    private static final ResourceLocation PLAYER_INFO_LOC =
            new ResourceLocation(SSS.MODID, "textures/gui/player_info.png");
    private static final ResourceLocation PLAYER_INFO_WIDGET_LOC =
            new ResourceLocation(SSS.MODID, "textures/gui/player_info_widget.png");

    private final Player player;

    // Dimensions of gui NEED TO SHOW
    private int topLeftX;
    private int topLeftY;
    private final int imageWidth = 222;
    private final int imageHeight = 208;

    private final int tabBtnW = 50;
    private final int tabBtnH = 13;
    private final int statBtnW = 11;
    private final int statBtnH = 18;
    private final int detailBtnW = 16 * 6;
    private final int detailBtnH = 16 * 3;

    private DetailType selectedDetail = null;

    public PlayerInfoScreen() {
        super(Component.literal("Player Info"));
        this.player = Minecraft.getInstance().player;
    }

    @Override
    protected void init() {
        super.init();

        // GUI center screen, so i,j is the top left corner of the draw gui (not full gui image)
        topLeftX = (this.width - imageWidth) / 2;
        topLeftY = (this.height - imageHeight) / 2;

        int buttonImageHeight = 256;
        int buttonImageWidth = 256;

        // u, v, hover state OffsetV
        ImageButton detailBtn = new ImageButton(
                topLeftX + (imageWidth - tabBtnH) / 2,
                topLeftY + 185,
                tabBtnW, tabBtnH,
                0, 0, tabBtnH,  // u, v, hover state OffsetV
                PLAYER_INFO_WIDGET_LOC, buttonImageHeight, buttonImageWidth,
                (btn) -> this.minecraft.setScreen(new PlayerInfoDetailScreen())
        );
        this.addRenderableWidget(detailBtn);
        ImageButton skillBtn = new ImageButton(
                topLeftX + (imageWidth - tabBtnH) / 2 + 80,
                topLeftY + 185,
                tabBtnW, tabBtnH,
                0, 0, tabBtnH,  // u, v, hoverOffsetV
                PLAYER_INFO_WIDGET_LOC, buttonImageHeight, buttonImageWidth,
                (btn) -> this.minecraft.setScreen(new PlayerInfoSkillScreen())
        );
        this.addRenderableWidget(skillBtn);
        // Add Detail button
        addBtn(
                topLeftX + 195, topLeftY + 30, detailBtnW, detailBtnH,
                0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.UUID;
                }
        );
        addBtn(
                topLeftX + 195, topLeftY + 65, detailBtnW, detailBtnH,
                0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.STRENGTH;
                }
        );
        addBtn(
                topLeftX + 196, topLeftY + 83, detailBtnW, detailBtnH,
                0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    selectedDetail = DetailType.VITALITY;
                }
        );
        addBtn(
                topLeftX + 196, topLeftY + 98, detailBtnW, detailBtnH,
                0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    selectedDetail = DetailType.AGILITY;
                }
        );
        addBtn(
                topLeftX + 196, topLeftY + 113, detailBtnW, detailBtnH,
                0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    selectedDetail = DetailType.INTELLIGENCE;
                }
        );
        addBtn(
                topLeftX + 196, topLeftY + 128, detailBtnW, detailBtnH,
                0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    selectedDetail = DetailType.PERCEPTION;
                }
        );
        addBtn(
                topLeftX + 196, topLeftY + 163, detailBtnW, detailBtnH,
                0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    selectedDetail = DetailType.LEVEL;
                }
        );

        // Add Strength button
        addBtn(
                topLeftX + 190, topLeftY + 66, statBtnW, statBtnH,
                64, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(0, 2));
                }
        );
        addBtn(
                topLeftX + 180, topLeftY + 66, statBtnW, statBtnH,
                78, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(0, -2));
                }
        );
        // Add Vitality button
        addBtn(
                topLeftX + 190, topLeftY + 84, statBtnW, statBtnH,
                64, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(1, 2)); // Add Vitality
                }
        );
        addBtn(
                topLeftX + 180, topLeftY + 84, statBtnW, statBtnH,
                78, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(1, -2)); // Subtract Vitality
                }
        );
        // Add Agility button
        addBtn(
                topLeftX + 190, topLeftY + 99, statBtnW, statBtnH,
                64, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(2, 2)); // Add Agility
                }
        );
        addBtn(
                topLeftX + 180, topLeftY + 99, statBtnW, statBtnH,
                78, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(2, -2)); // Subtract Agility
                }
        );
        // Add Intelligence button
        addBtn(
                topLeftX + 190, topLeftY + 114, statBtnW, statBtnH,
                64, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(3, 2)); // Add Intelligence
                }
        );
        addBtn(
                topLeftX + 180, topLeftY + 114, statBtnW, statBtnH,
                78, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(3, -2)); // Subtract Intelligence
                }
        );
        // Add Perception button
        addBtn(
                topLeftX + 190, topLeftY + 129, statBtnW, statBtnH,
                64, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(4, 2)); // Add Perception
                }
        );
        addBtn(
                topLeftX + 180, topLeftY + 129, statBtnW, statBtnH,
                78, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(4, -2)); // Subtract Perception
                }
        );
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);

        guiGraphics.blit(PLAYER_INFO_LOC, topLeftX, topLeftY, 0, 0, imageWidth, imageHeight);

        drawIcon(guiGraphics, topLeftX, topLeftY);

        // Some add after render
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        drawBars(guiGraphics);

        drawText(guiGraphics);

        int entityX = topLeftX + 50;
        int entityY = topLeftY + 130;

        float xRot = (float)(entityX - mouseX);
        float yRot = (float)(entityY - mouseY);

        InventoryScreen.renderEntityInInventoryFollowsMouse(
                guiGraphics,
                entityX,
                entityY,
                50, // scale
                xRot,
                yRot,
                this.player
        );

    }

    private void addBtn(int xLocOnGui, int yLocOnGui, int btnW, int btnH, int u, int v, int hoverStateOffsetV,
                        ResourceLocation widgetLoc, int buttonImageWidth, int buttonImageHeight, float scale, boolean delayPopUp, Runnable onClick) {
        ScaledStatImageButton btn = new ScaledStatImageButton(
                xLocOnGui, yLocOnGui,
                btnW, btnH,
                u, v, hoverStateOffsetV,
                widgetLoc, buttonImageWidth, buttonImageHeight,
                scale, delayPopUp,
                (button) -> onClick.run()

        );
        this.addRenderableWidget(btn);
    }

    private void drawInfoText(GuiGraphics guiGraphics, int i, int j, float scaleT, Component text) {
        int dialogWidth = 130;
        int dialogHeight = 100;

        // Vẽ nền hộp thoại
        guiGraphics.fill((int) ((topLeftX + 224) / scaleT), (int) (j / scaleT),
                (int) ((topLeftX + 200 + dialogWidth) / scaleT), topLeftY + 65 + dialogHeight, 0xCC000000);
        guiGraphics.drawWordWrap(this.font, text,
                (int)((topLeftX + 230) / scaleT),
                (int)((topLeftY + 5) / scaleT),
                dialogWidth - 10, // max width in pixels
                0xFFFFFF
        );
    }
    
    private void drawIcon(GuiGraphics guiGraphics, int topLeftX, int topLeftY) {
        guiGraphics.pose().pushPose();
        float scale = 0.8f; // scale to 80%
        guiGraphics.pose().scale(scale, scale, 1.0f);

        // Draw icons
        guiGraphics.blit(
                PLAYER_INFO_WIDGET_LOC,
                (int) ((topLeftX + 105) / scale),
                (int) ((topLeftY + 65) / scale),
                96, 0, 16, 16
        );

        guiGraphics.blit(
                PLAYER_INFO_WIDGET_LOC,    // Texture chứa ảnh
                (int) ((topLeftX + 105) / scale),
                (int) ((topLeftY + 83) / scale),
                112, 0, // Vị trí ảnh trong texture
                16, 16
        );

        guiGraphics.blit(
                PLAYER_INFO_WIDGET_LOC,
                (int) ((topLeftX + 105) / scale),
                (int) ((topLeftY + 98) / scale),
                128, 0,
                16, 16
        );
        guiGraphics.blit(
                PLAYER_INFO_WIDGET_LOC,
                (int) ((topLeftX + 105) / scale),
                (int) ((topLeftY + 113) / scale),
                144, 0,
                16, 16
        );
        guiGraphics.blit(
                PLAYER_INFO_WIDGET_LOC,
                (int) ((topLeftX + 105) / scale),
                (int) ((topLeftY + 128) / scale),
                160, 0,
                16, 16
        );
        // Restore pose
        guiGraphics.pose().popPose();
    }

    private void drawBars(GuiGraphics guiGraphics) {
        // Draw the EXP bar
        float expProgress = (float) PlayerClientData.expInCurrentLevel / PlayerClientData.expToNextLevel; // Giá trị từ 0.0 đến 1.0
        int barWidth = 90;
        int barHeight = 8;

        int x = topLeftX + 10; // Vị trí X của thanh
        int y = topLeftY + 175;  // Vị trí Y của thanh

        int filledWidth = (int) (barWidth * expProgress);

        // Viền hoặc nền thanh EXP (màu tối)
        guiGraphics.fill(x - 1, y - 1, x + barWidth + 1, y + barHeight + 1, 0xFF000000);

        // Nền EXP (màu xám)
        guiGraphics.fill(x, y, x + barWidth, y + barHeight, 0xFF555555);

        // Phần đã đầy EXP
        guiGraphics.fill(x, y, x + filledWidth, y + barHeight, 0xFFAA00AA);

        float hpProgress = player.getHealth() / player.getMaxHealth(); // Giá trị từ 0.0 đến 1.0
        int hpBarWidth = 65;
        int hpBarHeight = 5;
        int hpX = topLeftX + 17; // Vị trí X của thanh HP
        int hpY = topLeftY + 150;  // Vị trí Y của thanh HP
        int filledHpWidth = (int) (hpBarWidth * hpProgress);
        // Viền hoặc nền thanh HP (màu tối)
        guiGraphics.fill(hpX - 1, hpY - 1, hpX + hpBarWidth + 1, hpY + hpBarHeight + 1, 0xFF000000);
        // Nền HP (màu xám)
        guiGraphics.fill(hpX, hpY, hpX + hpBarWidth, hpY + hpBarHeight, 0xFF555555);
        // Phần đã đầy HP
        guiGraphics.fill(hpX, hpY, hpX + filledHpWidth, hpY + hpBarHeight, 0xFFFF0000);

        float manaProgress = PlayerClientData.mana / PlayerClientData.maxMana; // Giá trị từ 0.0 đến 1.0
        int manaBarWidth = 65;
        int manaBarHeight = 5;
        int manaX = topLeftX + 20; // Vị trí X của thanh Mana
        int manaY = topLeftY + 160;  // Vị trí Y của thanh Mana
        int filledManaWidth = (int) (manaBarWidth * manaProgress);
        // Viền hoặc nền thanh Mana (màu tối)
        guiGraphics.fill(manaX - 1, manaY - 1, manaX + manaBarWidth + 1, manaY + manaBarHeight + 1, 0xFF000000);
        // Nền Mana (màu xám)
        guiGraphics.fill(manaX, manaY, manaX + manaBarWidth, manaY + manaBarHeight, 0xFF555555);
        // Phần đã đầy Mana
        guiGraphics.fill(manaX, manaY, manaX + filledManaWidth, manaY + manaBarHeight, 0xFF0099FF);

    }

    private void drawText(GuiGraphics guiGraphics) {
        // Text
        float scaleT = 0.8f;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scaleT, scaleT, 1.0f);  // scale to 80%

        guiGraphics.drawString(
                this.font,
                Component.translatable("title.playerinfo").withStyle(ChatFormatting.DARK_GRAY),
                (int) ((topLeftX + (float) (imageWidth - tabBtnH) / 2) - 38/ scaleT),
                (int) ((topLeftY + 187) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.detailinfo").withStyle(ChatFormatting.DARK_GRAY),
                (int) ((topLeftX + (float) (imageWidth - tabBtnH) / 2) / scaleT),
                (int) ((topLeftY + 187) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.skillinfo").withStyle(ChatFormatting.DARK_GRAY),
                (int) ((topLeftX + (float) (imageWidth - tabBtnH) / 2) + 120/ scaleT),
                (int) ((topLeftY + 187) / scaleT),
                0xFFAA00,
                false
        );

        if (this.selectedDetail == DetailType.UUID) {
            drawInfoText(guiGraphics, topLeftX, topLeftY, scaleT,
                    Component.literal(player.getStringUUID())
                            .withStyle(ChatFormatting.WHITE));

        }
        if (this.selectedDetail == DetailType.STRENGTH) {
            drawInfoText(guiGraphics, topLeftX, topLeftY, scaleT,
                    Component.translatable("sentence.detail.strength")
                            .withStyle(ChatFormatting.WHITE));

        }
        else if (this.selectedDetail == DetailType.VITALITY) {
            drawInfoText(guiGraphics, topLeftX, topLeftY, scaleT,
                    Component.translatable("sentence.detail.vitality")
                            .withStyle(ChatFormatting.WHITE));
        }
        else if (this.selectedDetail == DetailType.AGILITY) {
            drawInfoText(guiGraphics, topLeftX, topLeftY, scaleT,
                    Component.translatable("sentence.detail.agility")
                            .withStyle(ChatFormatting.WHITE));
        }
        else if (this.selectedDetail == DetailType.INTELLIGENCE) {
            drawInfoText(guiGraphics, topLeftX, topLeftY, scaleT,
                    Component.translatable("sentence.detail.intelligence")
                            .withStyle(ChatFormatting.DARK_GRAY));
        }
        else if (this.selectedDetail == DetailType.PERCEPTION) {
            drawInfoText(guiGraphics, topLeftX, topLeftY, scaleT,
                    Component.translatable("sentence.detail.perception")
                            .withStyle(ChatFormatting.DARK_GRAY));
        }
        else if (this.selectedDetail == DetailType.LEVEL) {
            drawInfoText(guiGraphics, topLeftX, topLeftY, scaleT,
                    Component.translatable("sentence.detail.level")
                            .withStyle(ChatFormatting.DARK_GRAY));
        }

        guiGraphics.drawString(
                this.font,
                Component.translatable("title.str").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(": " + PlayerClientData.StrPoint)),
                (int) ((topLeftX + 121) / scaleT),
                (int) ((topLeftY + 69) / scaleT),
                0xFFAA00, // Color for text
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.literal("" + PlayerClientData.TotalStr).withStyle(ChatFormatting.DARK_GRAY),
                (int) ((topLeftX + 160) / scaleT),
                (int) ((topLeftY + 69) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.vit").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(": " + PlayerClientData.VitPoint)),
                (int) ((topLeftX + 121) / scaleT),
                (int) ((topLeftY + 87) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.agi").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(": " + PlayerClientData.AgiPoint)),
                (int) ((topLeftX + 121) / scaleT),
                (int) ((topLeftY + 102) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.int").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(": " + PlayerClientData.IntPoint)),
                (int) ((topLeftX + 121) / scaleT),
                (int) ((topLeftY + 117) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.per").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(": " + PlayerClientData.PerPoint)),
                (int) ((topLeftX + 121) / scaleT),
                (int) ((topLeftY + 132) / scaleT),
                0xFFAA00,
                false
        );

        guiGraphics.drawString(
                this.font,
                Component.translatable("title.name").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(player.getName().getString())),
                (int) ((topLeftX + 121) / scaleT),
                (int) ((topLeftY + 10) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.literal("UUID: ").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.translatable("sentence.playerinfo.uuid")),
                (int) ((topLeftX + 121) / scaleT),
                (int) ((topLeftY + 25) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.sect").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(": ").append(PlayerClientData.sect.translatableName())),
                (int) ((topLeftX + 121) / scaleT),
                (int) ((topLeftY + 38) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.title").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(": Not yet")),
                (int) ((topLeftX + 121) / scaleT),
                (int) ((topLeftY + 53) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.statpoints").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(": " + PlayerClientData.availableAttPoint + "    "))
                        .append(Component.translatable("title.used"))
                        .append(Component.literal(": " + PlayerClientData.usedAttPoint)),
                (int) ((topLeftX + 121) / scaleT),
                (int) ((topLeftY + 150) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.level").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(": " + PlayerClientData.primeLevel)),
                (int) ((topLeftX + 121) / scaleT),
                (int) ((topLeftY + 168) / scaleT),
                0xFFAA00,
                false
        );
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("Prime Exp: " + PlayerDataScreen.primeExp).withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD),
//                (int) ((topLeftX + 121) / scaleT),
//                (int) ((topLeftY + 183) / scaleT),
//                0xFFAA00,
//                false
//        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.hp").withStyle(ChatFormatting.GOLD)
                        .append(Component.literal(": " + player.getHealth() + " / " + player.getMaxHealth())),
                (int) ((topLeftX + 30) / scaleT),
                (int) ((topLeftY + 150) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.mp").withStyle(ChatFormatting.GOLD)
                        .append(Component.literal(": " + PlayerClientData.mana + " / " + PlayerClientData.maxMana)),
                (int) ((topLeftX + 30) / scaleT),
                (int) ((topLeftY + 160) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.exp").withStyle(ChatFormatting.LIGHT_PURPLE)
                        .append(Component.literal(": " + PlayerClientData.expInCurrentLevel + " / " + PlayerClientData.expToNextLevel)),
                (int) ((topLeftX + 35) / scaleT),
                (int) ((topLeftY + 175) / scaleT),
                0xFFAA00,
                false
        );

        guiGraphics.pose().popPose();

    }

    private enum DetailType {
        STRENGTH,
        VITALITY,
        AGILITY,
        INTELLIGENCE,
        PERCEPTION,
        UUID, LEVEL
    }
}
