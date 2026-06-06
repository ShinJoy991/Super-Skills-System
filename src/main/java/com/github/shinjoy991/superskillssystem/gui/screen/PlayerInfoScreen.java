package com.github.shinjoy991.superskillssystem.gui.screen;

import com.github.shinjoy991.superskillssystem.gui.ScaledStatImageButton;
import com.github.shinjoy991.superskillssystem.helpers.PlayerClientData;
import com.github.shinjoy991.superskillssystem.network.ModNetworking;
import com.github.shinjoy991.superskillssystem.network.server.AttPointChangeRequestC2S;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.github.shinjoy991.superskillssystem.SSS;

import static com.github.shinjoy991.superskillssystem.gui.screen.ScreenHelper.addBtn;

@OnlyIn(Dist.CLIENT)
public class PlayerInfoScreen extends Screen {
    private static final ResourceLocation PLAYER_INFO_LOC =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/gui/player_info.png");
    private static final ResourceLocation PLAYER_INFO_WIDGET_LOC =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/gui/player_info_widget.png");

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
    private final int attBtnW = 32;
    private final int attBtnH = 10;

    private int tempStr;
    private int tempVit;
    private int tempAgi;
    private int tempInt;
    private int tempPer;
    private int tempAvailable;


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

        syncTempFromPlayerData();
        // u, v, hover state OffsetV
        ImageButton detailBtn = new ImageButton(
                topLeftX + (imageWidth - tabBtnW) / 2,
                topLeftY + 186,
                tabBtnW, tabBtnH,
                0, 0, tabBtnH,  // u, v, hover state OffsetV
                PLAYER_INFO_WIDGET_LOC, buttonImageHeight, buttonImageWidth,
                (btn) -> {
//                    System.out.println("Client atk data: " + PlayerClientData.AtkDmg);
                    this.minecraft.setScreen(new PlayerInfoDetailScreen());
                }
        );
        this.addRenderableWidget(detailBtn);
        ImageButton skillBtn = new ImageButton(
                topLeftX + (imageWidth - tabBtnW) / 2 + 65,
                topLeftY + 186,
                tabBtnW, tabBtnH,
                0, 0, tabBtnH,  // u, v, hoverOffsetV
                PLAYER_INFO_WIDGET_LOC, buttonImageHeight, buttonImageWidth,
                (btn) -> this.minecraft.setScreen(new PlayerInfoSkillScreen())
        );
        this.addRenderableWidget(skillBtn);
        // Add Detail button
        this.addRenderableWidget(addBtn(
                topLeftX + 195, topLeftY + 33, detailBtnW, detailBtnH,
                0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.UUID;
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + 195, topLeftY + 72, detailBtnW, detailBtnH,
                0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.STRENGTH;
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + 196, topLeftY + 85, detailBtnW, detailBtnH,
                0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    selectedDetail = DetailType.VITALITY;
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + 196, topLeftY + 98, detailBtnW, detailBtnH,
                0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    selectedDetail = DetailType.AGILITY;
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + 196, topLeftY + 111, detailBtnW, detailBtnH,
                0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    selectedDetail = DetailType.INTELLIGENCE;
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + 196, topLeftY + 124, detailBtnW, detailBtnH,
                0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    selectedDetail = DetailType.PERCEPTION;
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + 196, topLeftY + 163, detailBtnW, detailBtnH,
                0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    selectedDetail = DetailType.LEVEL;
                }
        ));

        // Add Strength button
        this.addRenderableWidget(addBtn(
                topLeftX + 190, topLeftY + 75, statBtnW, statBtnH,
                64, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(0, 1));
                    if(tempAvailable > 0){
                        tempStr++;
                        tempAvailable--;
                    }
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + 180, topLeftY + 75, statBtnW, statBtnH,
                78, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(0, -1));
                    if(tempStr > PlayerClientData.StrPoint){
                        tempStr--;
                        tempAvailable++;
                    }
                }
        ));
        // Add Vitality button
        this.addRenderableWidget(addBtn(
                topLeftX + 190, topLeftY + 88, statBtnW, statBtnH,
                64, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(1, 1)); // Add Vitality
                    if(tempAvailable > 0){
                        tempVit++;
                        tempAvailable--;
                    }
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + 180, topLeftY + 88, statBtnW, statBtnH,
                78, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(1, -1)); // Subtract Vitality
                    if(tempVit > PlayerClientData.VitPoint){
                        tempVit--;
                        tempAvailable++;
                    }
                }
        ));
        // Add Agility button
        this.addRenderableWidget(addBtn(
                topLeftX + 190, topLeftY + 101, statBtnW, statBtnH,
                64, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(2, 2)); // Add Agility
                    if(tempAvailable > 0){
                        tempAgi++;
                        tempAvailable--;
                    }
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + 180, topLeftY + 101, statBtnW, statBtnH,
                78, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(2, -1)); // Subtract Agility
                    if(tempAgi > PlayerClientData.AgiPoint){
                        tempAgi--;
                        tempAvailable++;
                    }
                }
        ));
        // Add Intelligence button
        this.addRenderableWidget(addBtn(
                topLeftX + 190, topLeftY + 114, statBtnW, statBtnH,
                64, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(3, 1)); // Add Intelligence
                    if(tempAvailable > 0){
                        tempInt++;
                        tempAvailable--;
                    }
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + 180, topLeftY + 114, statBtnW, statBtnH,
                78, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(3, -1)); // Subtract Intelligence
                    if(tempInt > PlayerClientData.IntPoint){
                        tempInt--;
                        tempAvailable++;
                    }
                }
        ));
        // Add Perception button
        this.addRenderableWidget(addBtn(
                topLeftX + 190, topLeftY + 127, statBtnW, statBtnH,
                64, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(4, 1)); // Add Perception
                    if(tempAvailable > 0){
                        tempPer++;
                        tempAvailable--;
                    }
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + 180, topLeftY + 127, statBtnW, statBtnH,
                78, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.5f, true,
                () -> {
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(4, -1)); // Subtract Perception
                    if(tempPer > PlayerClientData.PerPoint){
                        tempPer--;
                        tempAvailable++;
                    }
                }
        ));

        // add attribute buttons
        // reset attribute points
        this.addRenderableWidget(addBtn(
                topLeftX + 115, topLeftY + 139, attBtnW, attBtnH,
                194, 0, attBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                1.0f, true,
                () -> {
                        Minecraft.getInstance().setScreen(
                                new net.minecraft.client.gui.screens.ConfirmScreen(
                                        this::onResetConfirm,
                                        Component.translatable("title.popup.reset_attributes"),
                                        Component.translatable("sentence.popup.reset_attributes")
                                )
                        );
                }
        ));
        // confirm attribute points
        this.addRenderableWidget(addBtn(
                topLeftX + 150, topLeftY + 139, attBtnW, attBtnH,
                194, 0, attBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                1.0f, true,
                () -> {
                    int strDelta = tempStr - PlayerClientData.StrPoint;
                    int vitDelta = tempVit - PlayerClientData.VitPoint;
                    int agiDelta = tempAgi - PlayerClientData.AgiPoint;
                    int intDelta = tempInt - PlayerClientData.IntPoint;
                    int perDelta = tempPer - PlayerClientData.PerPoint;

                    if (strDelta != 0)
                        ModNetworking.INSTANCE.sendToServer(
                                new AttPointChangeRequestC2S(0, strDelta));

                    if (vitDelta != 0)
                        ModNetworking.INSTANCE.sendToServer(
                                new AttPointChangeRequestC2S(1, vitDelta));

                    if (agiDelta != 0)
                        ModNetworking.INSTANCE.sendToServer(
                                new AttPointChangeRequestC2S(2, agiDelta));

                    if (intDelta != 0)
                        ModNetworking.INSTANCE.sendToServer(
                                new AttPointChangeRequestC2S(3, intDelta));

                    if (perDelta != 0)
                        ModNetworking.INSTANCE.sendToServer(
                                new AttPointChangeRequestC2S(4, perDelta));
                }
        ));
        // cancel change attribute points
        this.addRenderableWidget(addBtn(
                topLeftX + 185, topLeftY + 139, attBtnW, attBtnH,
                194, 0, attBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                1.0f, true,
                () -> {
                    tempStr = PlayerClientData.StrPoint;
                    tempVit = PlayerClientData.VitPoint;
                    tempAgi = PlayerClientData.AgiPoint;
                    tempInt = PlayerClientData.IntPoint;
                    tempPer = PlayerClientData.PerPoint;

                    tempAvailable = PlayerClientData.availableAttPoint;
                }
        ));
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

//    private void addBtn(int xLocOnGui, int yLocOnGui, int btnW, int btnH, int u, int v, int hoverStateOffsetV,
//                        ResourceLocation widgetLoc, int buttonImageWidth, int buttonImageHeight, float scale, boolean delayPopUp, Runnable onClick) {
//        ScaledStatImageButton btn = new ScaledStatImageButton(
//                xLocOnGui, yLocOnGui,
//                btnW, btnH,
//                u, v, hoverStateOffsetV,
//                widgetLoc, buttonImageWidth, buttonImageHeight,
//                scale, delayPopUp,
//                (button) -> onClick.run()
//
//        );
//        this.addRenderableWidget(btn);
//    }

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
        // sword
        guiGraphics.blit(
                PLAYER_INFO_WIDGET_LOC,
                (int) ((topLeftX + 115) / scale),
                (int) ((topLeftY + 73) / scale),
                96, 0, 16, 16
        );
        // shield
        guiGraphics.blit(
                PLAYER_INFO_WIDGET_LOC,    // Texture chứa ảnh
                (int) ((topLeftX + 115) / scale),
                (int) ((topLeftY + 86) / scale),
                112, 0, // Vị trí ảnh trong texture
                16, 16
        );
        // boots
        guiGraphics.blit(
                PLAYER_INFO_WIDGET_LOC,
                (int) ((topLeftX + 115) / scale),
                (int) ((topLeftY + 99) / scale),
                128, 0,
                16, 16
        );
        // fire
        guiGraphics.blit(
                PLAYER_INFO_WIDGET_LOC,
                (int) ((topLeftX + 115) / scale),
                (int) ((topLeftY + 112) / scale),
                144, 0,
                16, 16
        );
        // ring
        guiGraphics.blit(
                PLAYER_INFO_WIDGET_LOC,
                (int) ((topLeftX + 115) / scale),
                (int) ((topLeftY + 125) / scale),
                160, 0,
                16, 16
        );
        // Restore pose
        guiGraphics.pose().popPose();
    }

    private void drawBars(GuiGraphics guiGraphics) {
        // Draw the EXP bar
        float expProgress = (float) PlayerClientData.expInCurrentLevel / PlayerClientData.expToNextLevel; // Giá trị từ 0.0 đến 1.0
        int barWidth = 75;
        int barHeight = 7;

        int x = topLeftX + 15; // Vị trí X của thanh
        int y = topLeftY + 175;  // Vị trí Y của thanh

        int filledWidth = (int) (barWidth * expProgress);

        // Viền hoặc nền thanh EXP (màu tối)
        guiGraphics.fill(x - 1, y - 1, x + barWidth + 1, y + barHeight + 1, 0xFF000000);

        // Nền EXP (màu xám)
        guiGraphics.fill(x, y, x + barWidth, y + barHeight, 0xFF555555);

        // Phần đã đầy EXP
        guiGraphics.fill(x, y, x + filledWidth, y + barHeight, 0xFFAA00AA);

        float hpProgress = player.getHealth() / player.getMaxHealth(); // Giá trị từ 0.0 đến 1.0
        int hpBarWidth = 75;
        int hpBarHeight = 7;
        int hpX = topLeftX + 15; // Vị trí X của thanh HP
        int hpY = topLeftY + 151;  // Vị trí Y của thanh HP
        int filledHpWidth = (int) (hpBarWidth * hpProgress);
        // Viền hoặc nền thanh HP (màu tối)
        guiGraphics.fill(hpX - 1, hpY - 1, hpX + hpBarWidth + 1, hpY + hpBarHeight + 1, 0xFF000000);
        // Nền HP (màu xám)
        guiGraphics.fill(hpX, hpY, hpX + hpBarWidth, hpY + hpBarHeight, 0xFF555555);
        // Phần đã đầy HP
        guiGraphics.fill(hpX, hpY, hpX + filledHpWidth, hpY + hpBarHeight, 0xFFFF0000);

        float manaProgress = PlayerClientData.mana / PlayerClientData.maxMana; // Giá trị từ 0.0 đến 1.0
        int manaBarWidth = 75;
        int manaBarHeight = 7;
        int manaX = topLeftX + 15; // Vị trí X của thanh Mana
        int manaY = topLeftY + 163;  // Vị trí Y của thanh Mana
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
        float scaleT = 0.7f; // scale to 70%
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scaleT, scaleT, 1.0f);

        guiGraphics.drawString(
                this.font,
                Component.translatable("title.playerinfo").withStyle(ChatFormatting.WHITE),
                (int) (
                        (((topLeftX + (float) (imageWidth - tabBtnW) / 2) - 58) / scaleT)
                ),
                (int) ((topLeftY + 190) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.detailinfo").withStyle(ChatFormatting.DARK_GRAY),
                (int) (
                        (((topLeftX + (float) (imageWidth - tabBtnW) / 2) + 7) / scaleT)
                ),
                (int) ((topLeftY + 190) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.skillinfo").withStyle(ChatFormatting.DARK_GRAY),
                (int) (
                        (((topLeftX + (float) (imageWidth - tabBtnW) / 2) + 75) / scaleT)
                ),
                (int) ((topLeftY + 190) / scaleT),
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
                            .withStyle(ChatFormatting.WHITE));
        }
        else if (this.selectedDetail == DetailType.PERCEPTION) {
            drawInfoText(guiGraphics, topLeftX, topLeftY, scaleT,
                    Component.translatable("sentence.detail.perception")
                            .withStyle(ChatFormatting.WHITE));
        }
        else if (this.selectedDetail == DetailType.LEVEL) {
            drawInfoText(guiGraphics, topLeftX, topLeftY, scaleT,
                    Component.translatable("sentence.detail.level")
                            .withStyle(ChatFormatting.WHITE));
        }

        // make this location more dynamic later
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.name").withStyle(ChatFormatting.WHITE)
                        .append(Component.literal(player.getName().getString())),
                (int) ((topLeftX + 152) / scaleT),
                (int) ((topLeftY + 16) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.literal("UUID: ").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.translatable("sentence.playerinfo.uuid")),
                (int) ((topLeftX + 118) / scaleT),
                (int) ((topLeftY + 38) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.sect").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(": ").append(PlayerClientData.sect.translatableName())),
                (int) ((topLeftX + 118) / scaleT),
                (int) ((topLeftY + 51) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.title").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(": Not yet")),
                (int) ((topLeftX + 118) / scaleT),
                (int) ((topLeftY + 64) / scaleT),
                0xFFAA00,
                false
        );

        guiGraphics.drawString(
                this.font,
                Component.translatable("title.str").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(": " + tempStr)),
                (int) ((topLeftX + 130) / scaleT),
                (int) ((topLeftY + 77) / scaleT),
                0xFFAA00, // Color for text
                false
        );
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("" + PlayerClientData.TotalStr).withStyle(ChatFormatting.DARK_GRAY),
//                (int) ((topLeftX + 167) / scaleT),
//                (int) ((topLeftY + 77) / scaleT),
//                0xFFAA00,
//                false
//        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.vit").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(": " + tempVit)),
                (int) ((topLeftX + 130) / scaleT),
                (int) ((topLeftY + 90) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.agi").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(": " + tempAgi)),
                (int) ((topLeftX + 130) / scaleT),
                (int) ((topLeftY + 103) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.int").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(": " + tempInt)),
                (int) ((topLeftX + 130) / scaleT),
                (int) ((topLeftY + 116) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.per").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(": " + tempPer)),
                (int) ((topLeftX + 130) / scaleT),
                (int) ((topLeftY + 129) / scaleT),
                0xFFAA00,
                false
        );

        // 3 buttons for reset, confirm, cancel attribute points
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.reset").withStyle(ChatFormatting.DARK_GRAY),
                (int) ((topLeftX + 122) / scaleT),
                (int) ((topLeftY + 141) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.confirm").withStyle(ChatFormatting.DARK_GRAY),
                (int) ((topLeftX + 153) / scaleT),
                (int) ((topLeftY + 141) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.cancel").withStyle(ChatFormatting.DARK_GRAY),
                (int) ((topLeftX + 190) / scaleT),
                (int) ((topLeftY + 141) / scaleT),
                0xFFAA00,
                false
        );

        guiGraphics.drawString(
                this.font,
                Component.translatable("title.statpoints").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(": " + tempAvailable + "    "))
                        .append(Component.translatable("title.used"))
                        .append(Component.literal(": " + PlayerClientData.usedAttPoint)),
                (int) ((topLeftX + 118) / scaleT),
                (int) ((topLeftY + 155) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.level").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(": " + PlayerClientData.primeLevel)),
                (int) ((topLeftX + 118) / scaleT),
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
                Component.translatable("title.hp").withStyle(ChatFormatting.GREEN)
                        .append(Component.literal(": " + String.format("%.1f", player.getHealth()) + " / " + String.format("%.1f", player.getMaxHealth()))),
                (int) ((topLeftX + 23) / scaleT),
                (int) ((topLeftY + 152) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.mp").withStyle(ChatFormatting.GREEN)
                        .append(Component.literal(": " + String.format("%.2f", PlayerClientData.mana) + " / " + String.format("%.2f", PlayerClientData.maxMana))),
                (int) ((topLeftX + 25) / scaleT),
                (int) ((topLeftY + 165) / scaleT),
                0xFFAA00,
                false
        );
        guiGraphics.drawString(
                this.font,
                Component.translatable("title.exp").withStyle(ChatFormatting.LIGHT_PURPLE)
                        .append(Component.literal(": " + PlayerClientData.expInCurrentLevel + " / " + PlayerClientData.expToNextLevel)),
                (int) ((topLeftX + 27) / scaleT),
                (int) ((topLeftY + 176) / scaleT),
                0xFFAA00,
                false
        );

        guiGraphics.pose().popPose();

    }

    private void syncTempFromPlayerData() {
        tempStr = PlayerClientData.StrPoint;
        tempVit = PlayerClientData.VitPoint;
        tempAgi = PlayerClientData.AgiPoint;
        tempInt = PlayerClientData.IntPoint;
        tempPer = PlayerClientData.PerPoint;
        tempAvailable = PlayerClientData.availableAttPoint;
    }
    private void onResetConfirm(boolean confirmed) {
        if (confirmed) {
            ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(999, 0));
            Minecraft.getInstance().setScreen(null);
        } else {
            Minecraft.getInstance().setScreen(this);
        }
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
