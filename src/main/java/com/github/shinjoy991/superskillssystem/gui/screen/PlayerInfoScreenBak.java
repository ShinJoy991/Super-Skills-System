//package com.github.shinjoy991.superskillssystem.gui.screen;
//
//import com.github.shinjoy991.superskillssystem.SSS;
//import com.github.shinjoy991.superskillssystem.gui.ScaledStatImageButton;
//import com.github.shinjoy991.superskillssystem.helpers.Calculation;
//import com.github.shinjoy991.superskillssystem.network.ModNetworking;
//import com.github.shinjoy991.superskillssystem.network.server.AttPointChangeRequestC2S;
//import net.minecraft.ChatFormatting;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.client.gui.components.ImageButton;
//import net.minecraft.client.gui.screens.Screen;
//import net.minecraft.client.gui.screens.inventory.InventoryScreen;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Player;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//@OnlyIn(Dist.CLIENT)
//public class PlayerInfoScreenBak extends Screen {
//    private static final ResourceLocation PLAYER_INFO_LOC =
//            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/gui/player_info.png");
//    private static final ResourceLocation PLAYER_INFO_WIDGET_LOC =
//            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/gui/player_info_widget.png");
//
//
//    private final Player player;
//
//    // Dimensions of gui NEED TO SHOW
//    private final int imageWidth = 222;
//    private final int imageHeight = 208;
//
//    private final int tabBtnW = 50;
//    private final int tabBtnH = 13;
//    private final int statBtnW = 11;
//    private final int statBtnH = 18;
//    private final int detailBtnW = 16 * 6;
//    private final int detailBtnH = 16 * 3;
//
//    private ImageButton detailBtn;
//
//    private boolean showDetailStr = false; // Show detail strength
//    private boolean showDetailVit = false; // Show detail vitality
//    private boolean showDetailAgi = false; // Show detail agility
//    private boolean showDetailInt = false; // Show detail intelligence
//    private boolean showDetailPer = false; // Show detail perception
//    private boolean showDetailLevel = false; // Show detail level
//
//    public PlayerInfoScreenBak() {
//        super(Component.literal("Player Info"));
//        this.screenData = data;
//        this.player = Minecraft.getInstance().player;
//        this.StrPoint = screenData.StrPoint;
//        this.VitPoint = screenData.VitPoint;
//        this.AgiPoint = screenData.AgiPoint;
//        this.IntPoint = screenData.IntPoint;
//        this.PerPoint = screenData.PerPoint;
//        this.totalStr = screenData.TotalStr;
//        this.totalVit = screenData.TotalVit;
//        this.totalAgi = screenData.TotalAgi;
//        this.totalInt = screenData.TotalInt;
//        this.totalPer = screenData.TotalPer;
//        this.availableAttPoint = screenData.availableAttPoint;
//
////        this.primeExp = primeExp;
////        this.level = Calculation.calLevelByExp(this.primeExp);
////        this.expForNextLevel =  Calculation.calExpForLevel(this.level + 1)
////                - Calculation.calExpForLevel(level);
////        this.currentExp = Calculation.calCurrentLevelExp(this.primeExp);
//    }
//
//    @Override
//    protected void init() {
//        super.init();
//
//        // GUI center screen, so i,j is the top left corner of the draw gui (not full gui image)
//        int i = (this.width - imageWidth) / 2;
//        int j = (this.height - imageHeight) / 2;
//
//        int buttonImageHeight = 256;
//        int buttonImageWidth = 256;
//
//        detailBtn = new ImageButton(
//                i + (imageWidth - tabBtnH) / 2,
//                j + 185,
//                tabBtnW, tabBtnH,
//                0, 0, tabBtnH,  // u, v, hover state OffsetV
//                PLAYER_INFO_WIDGET_LOC, buttonImageHeight, buttonImageWidth,
//                (btn) -> this.minecraft.setScreen(new PlayerInfoDetailScreen(screenData))
//        );
//        this.addRenderableWidget(detailBtn);
//        // Add Detail button
//        addBtn(
//                i + 195, j + 65, detailBtnW, detailBtnH,
//                0, 48, detailBtnH,
//                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
//                0.3f, false,
//                () -> {
//                    this.showDetailStr = true;
//                    this.showDetailVit = false;
//                    this.showDetailAgi = false;
//                    this.showDetailInt = false;
//                    this.showDetailPer = false;
//                }
//        );
//        addBtn(
//                i + 196, j + 83, detailBtnW, detailBtnH,
//                0, 48, detailBtnH,
//                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
//                0.3f, false,
//                () -> {
//                    this.showDetailStr = false;
//                    this.showDetailVit = true;
//                    this.showDetailAgi = false;
//                    this.showDetailInt = false;
//                    this.showDetailPer = false;
//                }
//        );
//        addBtn(
//                i + 196, j + 98, detailBtnW, detailBtnH,
//                0, 48, detailBtnH,
//                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
//                0.3f, false,
//                () -> {
//                    this.showDetailStr = false;
//                    this.showDetailVit = false;
//                    this.showDetailAgi = true;
//                    this.showDetailInt = false;
//                    this.showDetailPer = false;
//                }
//        );
//        addBtn(
//                i + 196, j + 113, detailBtnW, detailBtnH,
//                0, 48, detailBtnH,
//                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
//                0.3f, false,
//                () -> {
//                    this.showDetailStr = false;
//                    this.showDetailVit = false;
//                    this.showDetailAgi = false;
//                    this.showDetailInt = true;
//                    this.showDetailPer = false;
//                }
//        );
//        addBtn(
//                i + 196, j + 128, detailBtnW, detailBtnH,
//                0, 48, detailBtnH,
//                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
//                0.3f, false,
//                () -> {
//                    this.showDetailStr = false;
//                    this.showDetailVit = false;
//                    this.showDetailAgi = false;
//                    this.showDetailInt = false;
//                    this.showDetailPer = true;
//                }
//        );
//
//        // Add Strength button
//        addBtn(
//                i + 190, j + 66, statBtnW, statBtnH,
//                64, 0, statBtnH,
//                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
//                0.5f, true,
//                () -> {
//                    System.out.println("Add Strength clicked");
//                    AttButtonClicked(0, 2); // Add Strength
//
//                }
//        );
//        addBtn(
//                i + 180, j + 66, statBtnW, statBtnH,
//                78, 0, statBtnH,
//                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
//                0.5f, true,
//                () -> {
//                    System.out.println("Subtract Strength clicked");
//                    AttButtonClicked(0, -2); // Subtract Strength
//                }
//        );
//        // Add Vitality button
//        addBtn(
//                i + 190, j + 84, statBtnW, statBtnH,
//                64, 0, statBtnH,
//                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
//                0.5f, true,
//                () -> {
//                    System.out.println("Add Vitality clicked");
//                    AttButtonClicked(1, 2); // Add Vitality
//                }
//        );
//        addBtn(
//                i + 180, j + 84, statBtnW, statBtnH,
//                78, 0, statBtnH,
//                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
//                0.5f, true,
//                () -> {
//                    System.out.println("Subtract Vitality clicked");
//                    AttButtonClicked(1, -2); // Subtract Vitality
//                }
//        );
//        // Add Agility button
//        addBtn(
//                i + 190, j + 99, statBtnW, statBtnH,
//                64, 0, statBtnH,
//                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
//                0.5f, true,
//                () -> {
//                    System.out.println("Add Agility clicked");
//                    AttButtonClicked(2, 2); // Add Agility
//                }
//        );
//        addBtn(
//                i + 180, j + 99, statBtnW, statBtnH,
//                78, 0, statBtnH,
//                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
//                0.5f, true,
//                () -> {
//                    System.out.println("Subtract Agility clicked");
//                    AttButtonClicked(2, -2); // Subtract Agility
//                }
//        );
//        // Add Intelligence button
//        addBtn(
//                i + 190, j + 114, statBtnW, statBtnH,
//                64, 0, statBtnH,
//                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
//                0.5f, true,
//                () -> {
//                    System.out.println("Add Intelligence clicked");
//                    AttButtonClicked(3, 2); // Add Intelligence
//                }
//        );
//        addBtn(
//                i + 180, j + 114, statBtnW, statBtnH,
//                78, 0, statBtnH,
//                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
//                0.5f, true,
//                () -> {
//                    System.out.println("Subtract Intelligence clicked");
//                    AttButtonClicked(3, -2); // Subtract Intelligence
//                }
//        );
//        // Add Perception button
//        addBtn(
//                i + 190, j + 129, statBtnW, statBtnH,
//                64, 0, statBtnH,
//                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
//                0.5f, true,
//                () -> {
//                    System.out.println("Add Perception clicked");
//                    AttButtonClicked(4, 2); // Add Perception
//                }
//        );
//        addBtn(
//                i + 180, j + 129, statBtnW, statBtnH,
//                78, 0, statBtnH,
//                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
//                0.5f, true,
//                () -> {
//                    System.out.println("Subtract Perception clicked");
//                    AttButtonClicked(4, -2); // Subtract Perception
//                }
//        );
//    }
//    private void AttButtonClicked(int type, int amount) {
//        switch (type) {
//            case 0: // Strength
//            {
//                if (amount > 0) {
//                    int verifiedAmount = VerifyAmountClient(true, amount, this.StrPoint);
////                    System.out.println("Add Strength: " + verifiedAmount);
//                    this.StrPoint += verifiedAmount;
//                    this.availableAttPoint -= verifiedAmount;
//                    this.totalStr = Calculation.calTotalStr(this.StrPoint);
//                    screenData.usedAttPoint += verifiedAmount;
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(0, verifiedAmount));
//                }
//                else if (amount < 0) {
//                    int verifiedAmount = VerifyAmountClient(false, amount, this.StrPoint);
////                    System.out.println("Subtract Strength: " + verifiedAmount);
//                    this.StrPoint -= verifiedAmount; // amount is negative, so we subtract
//                    this.availableAttPoint += verifiedAmount; // Add back to available points
//                    this.totalStr = Calculation.calTotalStr(this.StrPoint);
//                    screenData.usedAttPoint -= verifiedAmount;
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(0, -verifiedAmount));
//                }
//                screenData.StrPoint = this.StrPoint;
//                screenData.TotalStr = this.totalStr;
//                screenData.availableAttPoint = this.availableAttPoint;
//            }
//            break;
//            case 1: // Vitality
//            {
//                if (amount > 0) {
//                    int verifiedAmount = VerifyAmountClient(true, amount, this.VitPoint);
////                    System.out.println("Add Vitality: " + verifiedAmount);
//                    this.VitPoint += verifiedAmount;
//                    this.availableAttPoint -= verifiedAmount;
//                    this.totalVit = Calculation.calTotalVit(this.VitPoint);
//                    screenData.usedAttPoint += verifiedAmount;
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(1, verifiedAmount));
//                }
//                else if (amount < 0) {
//                    int verifiedAmount = VerifyAmountClient(false, amount, this.VitPoint);
////                    System.out.println("Subtract Vitality: " + verifiedAmount);
//                    this.VitPoint -= verifiedAmount; // amount is negative, so we subtract
//                    this.availableAttPoint += verifiedAmount; // Add back to available points
//                    this.totalVit = Calculation.calTotalVit(this.VitPoint);
//                    screenData.usedAttPoint -= verifiedAmount;
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(1, -verifiedAmount));
//                }
//                screenData.VitPoint = this.VitPoint;
//                screenData.TotalVit = this.totalVit;
//                screenData.availableAttPoint = this.availableAttPoint;
//            }
//            break;
//            case 2: // Agility
//            {
//                if (amount > 0) {
//                    int verifiedAmount = VerifyAmountClient(true, amount, this.AgiPoint);
//                    this.AgiPoint += verifiedAmount;
//                    this.availableAttPoint -= verifiedAmount;
//                    this.totalAgi = Calculation.calTotalAgi(this.AgiPoint);
//                    screenData.usedAttPoint += verifiedAmount;
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(2, verifiedAmount));
//                }
//                else if (amount < 0) {
//                    int verifiedAmount = VerifyAmountClient(false, amount, this.AgiPoint);
//                    this.AgiPoint -= verifiedAmount; // amount is negative, so we subtract
//                    this.availableAttPoint += verifiedAmount; // Add back to available points
//                    this.totalAgi = Calculation.calTotalAgi(this.AgiPoint);
//                    screenData.usedAttPoint -= verifiedAmount;
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(2, -verifiedAmount));
//                }
//                screenData.AgiPoint = this.AgiPoint;
//                screenData.TotalAgi = this.totalAgi;
//                screenData.availableAttPoint = this.availableAttPoint;
//            }
//            break;
//            case 3: // Intelligence
//            {
//                if (amount > 0) {
//                    int verifiedAmount = VerifyAmountClient(true, amount, this.IntPoint);
//                    this.IntPoint += verifiedAmount;
//                    this.availableAttPoint -= verifiedAmount;
//                    this.totalInt = Calculation.calTotalInt(this.IntPoint);
//                    screenData.usedAttPoint += verifiedAmount;
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(3, verifiedAmount));
//                }
//                else if (amount < 0) {
//                    int verifiedAmount = VerifyAmountClient(false, amount, this.IntPoint);
//                    this.IntPoint -= verifiedAmount; // amount is negative, so we subtract
//                    this.availableAttPoint += verifiedAmount; // Add back to available points
//                    this.totalInt = Calculation.calTotalInt(this.IntPoint);
//                    screenData.usedAttPoint -= verifiedAmount;
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(3, -verifiedAmount));
//                }
//                screenData.IntPoint = this.IntPoint;
//                screenData.TotalInt = this.totalInt;
//                screenData.availableAttPoint = this.availableAttPoint;
//            }
//            break;
//            case 4: // Perception
//            {
//                if (amount > 0) {
//                    int verifiedAmount = VerifyAmountClient(true, amount, this.PerPoint);
//                    this.PerPoint += verifiedAmount;
//                    this.availableAttPoint -= verifiedAmount;
//                    this.totalPer = Calculation.calTotalPer(this.PerPoint);
//                    screenData.usedAttPoint += verifiedAmount;
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(4, verifiedAmount));
//                }
//                else if (amount < 0) {
//                    int verifiedAmount = VerifyAmountClient(false, amount, this.PerPoint);
//                    this.PerPoint -= verifiedAmount; // amount is negative, so we subtract
//                    this.availableAttPoint += verifiedAmount; // Add back to available points
//                    this.totalPer = Calculation.calTotalPer(this.PerPoint);
//                    screenData.usedAttPoint -= verifiedAmount;
//                    ModNetworking.INSTANCE.sendToServer(new AttPointChangeRequestC2S(4, -verifiedAmount));
//                }
//                screenData.PerPoint = this.PerPoint;
//                screenData.TotalPer = this.totalPer;
//                screenData.availableAttPoint = this.availableAttPoint;
//            }
//        }
//    }
//
//    private int VerifyAmountClient(boolean isPositive, int amount, int currentAttPoint) {
//        int returnAmount = amount;
//        if (isPositive) {
//            if (amount > this.availableAttPoint) {
//                returnAmount = this.availableAttPoint; // Limit to available points
//            }
//        } else {
//            if (-amount > currentAttPoint) {
//                returnAmount = -currentAttPoint; // Limit to current points
//            }
//            returnAmount = -returnAmount; // Convert to positive for subtraction logic
//        }
//        return returnAmount;
//    }
//
//    @Override
//    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
//        this.renderBackground(guiGraphics);
//        int i = (this.width - imageWidth) / 2;
//        int j = (this.height - imageHeight) / 2;
//        float scaleT = 0.8f;
//        guiGraphics.blit(PLAYER_INFO_LOC, i, j, 0, 0, imageWidth, imageHeight);
//
//        guiGraphics.pose().pushPose();
//        float scale = 0.8f; // scale to 80%
//        guiGraphics.pose().scale(scale, scale, 1.0f);
//
//        // Draw icons
//        guiGraphics.blit(
//                PLAYER_INFO_WIDGET_LOC,
//                (int) ((i + 105) / scale),
//                (int) ((j + 65) / scale),
//                96, 0, 16, 16
//        );
//
//        guiGraphics.blit(
//                PLAYER_INFO_WIDGET_LOC,    // Texture chứa ảnh
//                (int) ((i + 105) / scale),
//                (int) ((j + 83) / scale),
//                112, 0, // Vị trí ảnh trong texture
//                16, 16
//        );
//
//        guiGraphics.blit(
//                PLAYER_INFO_WIDGET_LOC,
//                (int) ((i + 105) / scale),
//                (int) ((j + 98) / scale),
//                128, 0,
//                16, 16
//        );
//        guiGraphics.blit(
//                PLAYER_INFO_WIDGET_LOC,
//                (int) ((i + 105) / scale),
//                (int) ((j + 113) / scale),
//                144, 0,
//                16, 16
//        );
//        guiGraphics.blit(
//                PLAYER_INFO_WIDGET_LOC,
//                (int) ((i + 105) / scale),
//                (int) ((j + 128) / scale),
//                160, 0,
//                16, 16
//        );
//        // Restore pose
//        guiGraphics.pose().popPose();
//
//        // Some add after render
//        super.render(guiGraphics, mouseX, mouseY, partialTicks);
//
//        // Draw the EXP bar
//        float expProgress = (float) screenData.expInCurrentLevel / screenData.expToNextLevel; // Giá trị từ 0.0 đến 1.0
//        int barWidth = 90;
//        int barHeight = 8;
//
//        int x = i + 10; // Vị trí X của thanh
//        int y = j + 175;  // Vị trí Y của thanh
//
//        int filledWidth = (int) (barWidth * expProgress);
//
//        // Viền hoặc nền thanh EXP (màu tối)
//        guiGraphics.fill(x - 1, y - 1, x + barWidth + 1, y + barHeight + 1, 0xFF000000);
//
//        // Nền EXP (màu xám)
//        guiGraphics.fill(x, y, x + barWidth, y + barHeight, 0xFF555555);
//
//        // Phần đã đầy EXP
//        guiGraphics.fill(x, y, x + filledWidth, y + barHeight, 0xFFAA00AA);
//
//        float hpProgress = player.getHealth() / player.getMaxHealth(); // Giá trị từ 0.0 đến 1.0
//        int hpBarWidth = 60;
//        int hpBarHeight = 5;
//        int hpX = i + 17; // Vị trí X của thanh HP
//        int hpY = j + 150;  // Vị trí Y của thanh HP
//        int filledHpWidth = (int) (hpBarWidth * hpProgress);
//        // Viền hoặc nền thanh HP (màu tối)
//        guiGraphics.fill(hpX - 1, hpY - 1, hpX + hpBarWidth + 1, hpY + hpBarHeight + 1, 0xFF000000);
//        // Nền HP (màu xám)
//        guiGraphics.fill(hpX, hpY, hpX + hpBarWidth, hpY + hpBarHeight, 0xFF555555);
//        // Phần đã đầy HP
//        guiGraphics.fill(hpX, hpY, hpX + filledHpWidth, hpY + hpBarHeight, 0xFFFF0000);
//
//        float manaProgress = screenData.mana / screenData.maxMana; // Giá trị từ 0.0 đến 1.0
//        int manaBarWidth = 60;
//        int manaBarHeight = 5;
//        int manaX = i + 20; // Vị trí X của thanh Mana
//        int manaY = j + 160;  // Vị trí Y của thanh Mana
//        int filledManaWidth = (int) (manaBarWidth * manaProgress);
//        // Viền hoặc nền thanh Mana (màu tối)
//        guiGraphics.fill(manaX - 1, manaY - 1, manaX + manaBarWidth + 1, manaY + manaBarHeight + 1, 0xFF000000);
//        // Nền Mana (màu xám)
//        guiGraphics.fill(manaX, manaY, manaX + manaBarWidth, manaY + manaBarHeight, 0xFF555555);
//        // Phần đã đầy Mana
//        guiGraphics.fill(manaX, manaY, manaX + filledManaWidth, manaY + manaBarHeight, 0xFF0099FF);
//
//        guiGraphics.pose().pushPose();
//        //        if (this.detailBtn.isHovered()) {
////            guiGraphics.renderTooltip(this.font,
////                    Component.literal("View Player Details"),
////                    (int) (mouseX / scaleT), (int) (mouseY / scaleT) + 80
////            );
////        }
//        // Text
//        guiGraphics.pose().scale(scaleT, scaleT, 1.0f);  // scale to 80%
//
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("Player Info").withStyle(ChatFormatting.DARK_GRAY),
//                (int) ((i + (float) (imageWidth - tabBtnH) / 2) - 38/ scaleT),
//                (int) ((j + 187) / scaleT),
//                0xFFAA00,
//                false
//        );
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("Detail Info").withStyle(ChatFormatting.DARK_GRAY),
//                (int) ((i + (float) (imageWidth - tabBtnH) / 2) / scaleT),
//                (int) ((j + 187) / scaleT),
//                0xFFAA00,
//                false
//        );
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("Skill Info").withStyle(ChatFormatting.DARK_GRAY),
//                (int) ((i + (float) (imageWidth - tabBtnH) / 2) + 120/ scaleT),
//                (int) ((j + 187) / scaleT),
//                0xFFAA00,
//                false
//        );
//
//        if (this.showDetailStr) {
//            drawInfoText(guiGraphics, i, j, scaleT,
//                    Component.literal("Strength affects physical damage, some defense")
//                            .withStyle(ChatFormatting.WHITE));
//
//        }
//        else if (this.showDetailVit) {
//            drawInfoText(guiGraphics, i, j, scaleT,
//                    Component.literal("Vitality affects health and some defense")
//                            .withStyle(ChatFormatting.WHITE));
//        }
//        else if (this.showDetailAgi) {
//            drawInfoText(guiGraphics, i, j, scaleT,
//                    Component.literal("Agility affects attack speed and dodge chance")
//                            .withStyle(ChatFormatting.WHITE));
//        }
//        else if (this.showDetailInt) {
//            drawInfoText(guiGraphics, i, j, scaleT,
//                    Component.literal("Intelligence affects magic damage and mana")
//                            .withStyle(ChatFormatting.DARK_GRAY));
//        }
//        else if (this.showDetailPer) {
//            drawInfoText(guiGraphics, i, j, scaleT,
//                    Component.literal("Perception affects critical hit chance and accuracy")
//                            .withStyle(ChatFormatting.DARK_GRAY));
//        }
//        else if (this.showDetailLevel) {
//            drawInfoText(guiGraphics, i, j, scaleT,
//                    Component.literal("Level determines overall power and unlocks new skills")
//                            .withStyle(ChatFormatting.DARK_GRAY));
//        }
//
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("STR: " + this.StrPoint).withStyle(ChatFormatting.DARK_GRAY),
//                (int) ((i + 121) / scaleT),
//                (int) ((j + 69) / scaleT),
//                0xFFAA00, // Color for text
//                false
//        );
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("VIT: " + this.VitPoint).withStyle(ChatFormatting.DARK_GRAY),
//                (int) ((i + 121) / scaleT),
//                (int) ((j + 87) / scaleT),
//                0xFFAA00,
//                false
//        );
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("AGI: " + this.AgiPoint).withStyle(ChatFormatting.DARK_GRAY),
//                (int) ((i + 121) / scaleT),
//                (int) ((j + 102) / scaleT),
//                0xFFAA00,
//                false
//        );
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("INT: " + this.IntPoint).withStyle(ChatFormatting.DARK_GRAY),
//                (int) ((i + 121) / scaleT),
//                (int) ((j + 117) / scaleT),
//                0xFFAA00,
//                false
//        );
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("PER: " + this.PerPoint).withStyle(ChatFormatting.DARK_GRAY),
//                (int) ((i + 121) / scaleT),
//                (int) ((j + 132) / scaleT),
//                0xFFAA00,
//                false
//        );
//
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("Name: " + screenData.name).withStyle(ChatFormatting.DARK_GRAY),
//                (int) ((i + 121) / scaleT),
//                (int) ((j + 10) / scaleT),
//                0xFFAA00,
//                false
//        );
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("UUID: View detail").withStyle(ChatFormatting.DARK_GRAY),
//                (int) ((i + 121) / scaleT),
//                (int) ((j + 25) / scaleT),
//                0xFFAA00,
//                false
//        );
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("Faction:").withStyle(ChatFormatting.DARK_GRAY),
//                (int) ((i + 121) / scaleT),
//                (int) ((j + 38) / scaleT),
//                0xFFAA00,
//                false
//        );
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("Title:").withStyle(ChatFormatting.DARK_GRAY),
//                (int) ((i + 121) / scaleT),
//                (int) ((j + 53) / scaleT),
//                0xFFAA00,
//                false
//        );
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("Stats point: " + (screenData.availableAttPoint) +
//                        "Used: "+ screenData.usedAttPoint).withStyle(ChatFormatting.DARK_GRAY),
//                (int) ((i + 121) / scaleT),
//                (int) ((j + 150) / scaleT),
//                0xFFAA00,
//                false
//        );
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("Level: " + screenData.primeLevel).withStyle(ChatFormatting.DARK_GRAY),
//                (int) ((i + 121) / scaleT),
//                (int) ((j + 168) / scaleT),
//                0xFFAA00,
//                false
//        );
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("Prime Exp: " + screenData.primeExp).withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD),
//                (int) ((i + 121) / scaleT),
//                (int) ((j + 183) / scaleT),
//                0xFFAA00,
//                false
//        );
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("HP: " + player.getHealth() + " / " + player.getMaxHealth())
//                        .withStyle(ChatFormatting.GOLD),
//                (int) ((i + 30) / scaleT),
//                (int) ((j + 150) / scaleT),
//                0xFFAA00,
//                false
//        );
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("Mana: " + screenData.mana + " / " + screenData.maxMana)
//                        .withStyle(ChatFormatting.GOLD),
//                (int) ((i + 30) / scaleT),
//                (int) ((j + 160) / scaleT),
//                0xFFAA00,
//                false
//        );
//        guiGraphics.drawString(
//                this.font,
//                Component.literal("Exp: " + screenData.expInCurrentLevel + " / " + screenData.expToNextLevel)
//                        .withStyle(ChatFormatting.LIGHT_PURPLE),
//                (int) ((i + 35) / scaleT),
//                (int) ((j + 175) / scaleT),
//                0xFFAA00,
//                false
//        );
//
//        guiGraphics.pose().popPose();
//
//
//
//        int entityX = i + 50; // X position on screen
//        int entityY = j + 130;   // Y position on screen
//        int scaleM = 50;
//
//        float xRot = (float)(entityX - mouseX);
//        float yRot = (float)(entityY - mouseY);
//
//        InventoryScreen.renderEntityInInventoryFollowsMouse(
//                guiGraphics,
//                entityX,
//                entityY,
//                scaleM,
//                xRot,
//                yRot,
//                this.player
//        );
//
//    }
//
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
//
//    private void drawInfoText(GuiGraphics guiGraphics, int i, int j, float scaleT, Component text) {
//        int dialogWidth = 130;
//        int dialogHeight = 100;
//
//        // Vẽ nền hộp thoại
//        guiGraphics.fill((int) ((i + 224) / scaleT), (int) (j / scaleT),
//                (int) ((i + 200 + dialogWidth) / scaleT), j + 65 + dialogHeight, 0xCC000000);
//        guiGraphics.drawWordWrap(this.font, text,
//                (int)((i + 230) / scaleT),
//                (int)((j + 5) / scaleT),
//                dialogWidth - 10, // max width in pixels
//                0xFFFFFF
//        );
//    }
//}
