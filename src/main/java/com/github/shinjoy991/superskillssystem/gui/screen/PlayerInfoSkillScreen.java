package com.github.shinjoy991.superskillssystem.gui.screen;

import com.github.shinjoy991.superskillssystem.SSS;
import com.github.shinjoy991.superskillssystem.gui.ScaledStatImageButton;
import com.github.shinjoy991.superskillssystem.helpers.PlayerClientData;
import com.github.shinjoy991.superskillssystem.helpers.skill.ActiveSkill;
import com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkillInstance;
import com.github.shinjoy991.superskillssystem.network.ModNetworking;
import com.github.shinjoy991.superskillssystem.network.server.DeleteSkillC2S;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;

import static com.github.shinjoy991.superskillssystem.SSS.MODID;
import static com.github.shinjoy991.superskillssystem.gui.screen.ScreenHelper.addBtn;

@OnlyIn(Dist.CLIENT)
public class PlayerInfoSkillScreen extends Screen {
    private static final ResourceLocation DETAIL_SCREEN_LOC =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/gui/player_info_skill.png");
    private static final ResourceLocation PLAYER_INFO_WIDGET_LOC =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/gui/player_info_widget.png");
    private static final ResourceLocation PIE_LOC =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/skill/skill_pie_button.png");

    private final Player player;

    private final int imageWidth = 361;
    private final int imageHeight = 208;

    private final int tabBtnW = 50;
    private final int tabBtnH = 13;
    private final int statBtnW = 11;
    private final int statBtnH = 18;
    private final int detailBtnW = 16 * 6;
    private final int detailBtnH = 16 * 3;
    private final int buttonImageWH = 256;

    private ImageButton detailBtn;
    private ImageButton infoButton;

    private int topLeftX;
    private int topLeftY;
    private int textOffsetTopLeftX = 23;
    private int textOffsetTopLeftY = 38;
    private int textSpacingX = 112;
    private int textSpacingY = 16;

    private int detailBtnTopLeftX = 95;
    private int detailBtnTopLeftY = textOffsetTopLeftY - 5;

    private static final int MAX_ROWS = 9;

    private static final int PASSIVE_COLS = 2;
    private static final int ACTIVE_COLS = 1;

    private static final int TOTAL_PASSIVE_BTNS = MAX_ROWS * PASSIVE_COLS;      // 18
    private static final int TOTAL_ACTIVE_BTNS = MAX_ROWS * ACTIVE_COLS;       // 9

    private int currentPassivePage = 1, maxPassivePage;
    private int currentActivePage = 1,  maxActivePage;

    // Detail flags
    private int selectedDetail = -1;

    private final Map<Integer, ScaledStatImageButton> detailBtnMap = new HashMap<>();

    private final List<GuiSparkleParticle> sparkleParticles = new ArrayList<>();
    private int lastMouseX, lastMouseY;

    // Delete skill button
    private ImageButton deleteSkillButton;

    // Pie button
    private ImageButton pieBtn1;
    private ImageButton pieBtn2;
    private ImageButton pieBtn3;
    private ImageButton pieBtn4;

    private final int pieImgSize = 256;
    private int pieSize = 126;
    private int halfPieSize = pieSize / 2;
    private float pieScale = 1/7f;
    private int pieOffset = 11;


    public PlayerInfoSkillScreen() {
        super(Component.literal("Player Info Skills"));
        this.player = Minecraft.getInstance().player;
    }

    @Override
    protected void init() {
        super.init();

        topLeftX = (this.width - imageWidth) / 2;
        topLeftY = (this.height - imageHeight) / 2;

        // Add info button
        this.addRenderableWidget(infoButton = new ImageButton(
                topLeftX + (imageWidth - tabBtnW) / 2 - 80,
                topLeftY + 184
                , tabBtnW, tabBtnH,
                0, 0, tabBtnH,  // u, v, hoverOffsetV
                PLAYER_INFO_WIDGET_LOC, buttonImageWH, buttonImageWH,
                (btn) -> this.minecraft.setScreen(new PlayerInfoScreen())
        ));

        // Add detail button
        this.addRenderableWidget(new ImageButton(
                topLeftX + (imageWidth - tabBtnW) / 2,
                topLeftY + 184,
                tabBtnW, tabBtnH,
                0, 0, tabBtnH,  // u, v, hover state OffsetV
                PLAYER_INFO_WIDGET_LOC, buttonImageWH, buttonImageWH,
                (btn) -> this.minecraft.setScreen(new PlayerInfoDetailScreen())
        ));

        PlayerClientData.passiveSkills.sort(Comparator.comparing(skill -> skill.getName().toLowerCase()));
        maxPassivePage = (PlayerClientData.passiveSkills.size() + TOTAL_PASSIVE_BTNS - 1) / TOTAL_PASSIVE_BTNS;
        maxPassivePage = Math.max(1, maxPassivePage);

        maxActivePage = (PlayerClientData.activeSkills.size() + MAX_ROWS - 1) / MAX_ROWS;
        if (maxActivePage <= 0) maxActivePage = 1;

        // Add page buttons for skills
        this.addRenderableWidget(addBtn(
                topLeftX + 330, topLeftY + 160, statBtnW, statBtnH,
                64, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWH, buttonImageWH,
                0.5f, true,
                () -> {
                    this.currentPassivePage = Math.min(maxPassivePage, this.currentPassivePage + 1);
                    this.currentActivePage = Math.min(maxActivePage, this.currentActivePage + 1);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + 320, topLeftY + 160, statBtnW, statBtnH,
                78, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWH, buttonImageWH,
                0.5f, true,
                () -> {
                    this.currentPassivePage = Math.max(1, this.currentPassivePage - 1);
                    this.currentActivePage = Math.max(1, this.currentActivePage - 1);
                }
        ));

        // Add detail buttons for both
        int number = 0;

        for (int col = 0; col < 3; col++) {
            for (int row = 0; row < MAX_ROWS; row++) {
                int x = topLeftX + detailBtnTopLeftX + textSpacingX * col;
                int y = topLeftY + detailBtnTopLeftY + textSpacingY * row;

                int finalNumber = number;
                ScaledStatImageButton btn =
                        (ScaledStatImageButton) addBtn(
                                x, y,
                                detailBtnW, detailBtnH,
                                0, 48,
                                detailBtnH, PLAYER_INFO_WIDGET_LOC,
                                buttonImageWH, buttonImageWH,
                                0.3f, false, false,
                                () -> {
                                    if (finalNumber < 18) {
                                        pieBtn1.visible = false;
                                        pieBtn2.visible = false;
                                        pieBtn3.visible = false;
                                        pieBtn4.visible = false;
                                        selectedDetail = (currentPassivePage - 1) * TOTAL_PASSIVE_BTNS + finalNumber;
                                    } else {
                                        pieBtn1.visible = true;
                                        pieBtn2.visible = true;
                                        pieBtn3.visible = true;
                                        pieBtn4.visible = true;
                                        selectedDetail = 100000 + (currentActivePage - 1) * TOTAL_ACTIVE_BTNS + (finalNumber - 18);
                                    }
                                    spawnSparklesAt(lastMouseX, lastMouseY
                                    );
                                });
                this.addRenderableWidget(btn);
                detailBtnMap.put(number, btn);
                number++;
            }
        }

        // add delete skill button (hiding)
        deleteSkillButton = new ImageButton(
                topLeftX + 320,
                topLeftY + 14,
                10, 10,
                179, 0,
                10,
                PLAYER_INFO_WIDGET_LOC,
                buttonImageWH, buttonImageWH,
                btn -> {
                    if (selectedDetail < 0) return;

                    Minecraft.getInstance().setScreen(
                            new net.minecraft.client.gui.screens.ConfirmScreen(
                                    this::onDeleteConfirm,
                                    Component.translatable("title.popup.delete_skill"),
                                    Component.translatable("sentence.popup.delete_skill")
                            )
                    );
                }
        );

        deleteSkillButton.visible = false;
        this.addRenderableWidget(deleteSkillButton);


        // Add pie buttons
        int x = topLeftX + 320 - 40;
        int y = topLeftY + 14 - 5;
//
//        int imgSize = 256;
//        int size = 126;
//        int half = size / 2;
//        float scale = 1/7f;
//        int offset = 11;

        // TL
        this.pieBtn1 = (ImageButton) addBtn(
                x, y,
                halfPieSize, halfPieSize, 0, 0, 126,
                PIE_LOC,
                pieImgSize, pieImgSize,
                pieScale, false, false,
                () -> {
//                    System.out.println("Pie Btn 1 clicked");
                    if (selectedDetail < 100000) return; // chỉ nhận active skill
                    int idx = selectedDetail - 100000;
                    if (idx >= PlayerClientData.activeSkills.size()) return;
                    PlayerClientData.setWheelSlot(1, PlayerClientData.activeSkills.get(idx).getId());
                }
        );
        this.addRenderableWidget(this.pieBtn1);

        // TR
        this.pieBtn2 = (ImageButton) addBtn(
                x + pieOffset, y,
                halfPieSize, halfPieSize, halfPieSize, 0, 126,
                PIE_LOC,
                pieImgSize, pieImgSize,
                pieScale, false, false,
                () -> {
//                    System.out.println("Pie Btn 2 clicked");
                    if (selectedDetail < 100000) return; // chỉ nhận active skill
                    int idx = selectedDetail - 100000;
                    if (idx >= PlayerClientData.activeSkills.size()) return;
                    PlayerClientData.setWheelSlot(2, PlayerClientData.activeSkills.get(idx).getId());
                }
        );
        this.addRenderableWidget(this.pieBtn2);

        // BL
        this.pieBtn4 = (ImageButton) addBtn(
                x, y + pieOffset,
                halfPieSize, halfPieSize, 0, halfPieSize, 126,
                PIE_LOC,
                pieImgSize, pieImgSize,
                pieScale, false, false,
                () -> {
//                    System.out.println("Pie Btn 4 clicked");
                    if (selectedDetail < 100000) return; // chỉ nhận active skill
                    int idx = selectedDetail - 100000;
                    if (idx >= PlayerClientData.activeSkills.size()) return;
                    PlayerClientData.setWheelSlot(4, PlayerClientData.activeSkills.get(idx).getId());
                }
        );
        this.addRenderableWidget(this.pieBtn4);

        // BR
        this.pieBtn3 = (ImageButton) addBtn(
                x + pieOffset, y + pieOffset,
                halfPieSize, halfPieSize, halfPieSize, halfPieSize, 126,
                PIE_LOC,
                pieImgSize, pieImgSize,
                pieScale, false, false,
                () -> {
//                    System.out.println("Pie Btn 3 clicked");
                    if (selectedDetail < 100000) return; // chỉ nhận active skill
                    int idx = selectedDetail - 100000;
                    if (idx >= PlayerClientData.activeSkills.size()) return;
                    PlayerClientData.setWheelSlot(3, PlayerClientData.activeSkills.get(idx).getId());
                }
        );
        this.addRenderableWidget(this.pieBtn3);

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        guiGraphics.blit(DETAIL_SCREEN_LOC, topLeftX, topLeftY, 0, 0, imageWidth, imageHeight, 512, 256);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        lastMouseX = mouseX;
        lastMouseY = mouseY;

        drawSparkles(guiGraphics);
        drawTitles(guiGraphics);
        drawSkillNames(guiGraphics);
        drawActiveSkillNames(guiGraphics);
        if (selectedDetail > -1) {
            deleteSkillButton.visible = true;
            drawInfoText(guiGraphics, selectedDetail, 0.55f);
        } else {
            deleteSkillButton.visible = false;
        }
    }


    private void drawSparkles(GuiGraphics guiGraphics) {
        sparkleParticles.removeIf(p -> !p.isAlive());
        for (GuiSparkleParticle p : sparkleParticles) {
            p.tick();
            float alpha = p.getAlpha();
            int alphaByte = (int) (alpha * 255.0f);
            int sparkleColor = (alphaByte << 24) | 0x00FFFFFF;
            guiGraphics.fill((int) p.x, (int) p.y, (int) p.x + 2, (int) p.y + 2, sparkleColor);
        }
    }

    private void drawTitles(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        float scale = 0.7f;
        guiGraphics.pose().scale(scale, scale, 1.0f);
        guiGraphics.drawString(this.font, Component.literal("Player Info").withStyle(ChatFormatting.DARK_GRAY),
                (int) (((topLeftX + (float) (imageWidth - tabBtnW) / 2) - 76) / scale),
                (int) ((topLeftY + 188) / scale), 0xFFAA00, false);
        guiGraphics.drawString(this.font, Component.literal("Detail Info").withStyle(ChatFormatting.DARK_GRAY),
                (int) (((topLeftX + (float) (imageWidth - tabBtnW) / 2) + 7) / scale),
                (int) ((topLeftY + 188) / scale), 0xFFAA00, false);
        guiGraphics.drawString(this.font, Component.literal("Skill Info").withStyle(ChatFormatting.WHITE),
                (int) (((topLeftX + (float) (imageWidth - tabBtnW) / 2) + 90) / scale),
                (int) ((topLeftY + 188) / scale),
                0xFFAA00, false);
        guiGraphics.pose().popPose();
    }

    private void drawSkillNames(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.55f, 0.55f, 1f);

        int pageOffset = (currentPassivePage - 1) * TOTAL_PASSIVE_BTNS;

        for (int col = 0; col < 2; col++) {
            for (int row = 0; row < MAX_ROWS; row++) {

                int globalIdx = pageOffset + col * MAX_ROWS + row;

                int btnKey = col * MAX_ROWS + row;
                ScaledStatImageButton btn = detailBtnMap.get(btnKey);

                boolean hasSkill =
                        globalIdx < PlayerClientData.passiveSkills.size();

                if (btn != null) btn.visible = hasSkill;

                if (!hasSkill) continue;

                PassiveSkillInstance skill =
                        PlayerClientData.passiveSkills.get(globalIdx);

                int lvl = skill.getLevel();

                int x = (int)((topLeftX + textOffsetTopLeftX
                        + col * textSpacingX) / 0.55f);

                int y = (int)((topLeftY + textOffsetTopLeftY
                        + row * textSpacingY) / 0.55f);

                MutableComponent display =
                        Component.translatable("skill.name." + skill.getName())
                                .append(Component.literal(" Lv" + lvl))
                                .withStyle(ChatFormatting.DARK_GRAY);

                drawWordWrapWithSpacing(
                        guiGraphics,
                        this.font,
                        display,
                        x,
                        y,
                        130,
                        0xFFAA00,
                        3
                );
            }
        }

        guiGraphics.pose().popPose();
    }
    public void drawWordWrapWithSpacing(GuiGraphics guiGraphics, Font font,
                                        FormattedText text, int x, int y, int maxWidth, int color, int extraSpacing) {
        for (FormattedCharSequence line : font.split(text, maxWidth)) {
            guiGraphics.drawString(font, line, x, y, color, false);
            y += 9 + extraSpacing;
        }
    }
    private void drawActiveSkillNames(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.55f, 0.55f, 1f);

        int pageOffset = (currentActivePage - 1) * TOTAL_ACTIVE_BTNS;

        for (int row = 0; row < MAX_ROWS; row++) {
            int globalIdx = pageOffset + row;
            int btnKey = 18 + row;
            ScaledStatImageButton btn = detailBtnMap.get(btnKey);
            boolean hasSkill = globalIdx < PlayerClientData.activeSkills.size();

            if (btn != null) btn.visible = hasSkill;
            if (!hasSkill) continue;

            ActiveSkill skill = PlayerClientData.activeSkills.get(globalIdx);

            int x = (int)((topLeftX + textOffsetTopLeftX + textSpacingX * 2) / 0.55f);
            int y = (int)((topLeftY + textOffsetTopLeftY + row * textSpacingY) / 0.55f);

            MutableComponent display = Component.translatable("skill.name."+ skill.getName())
                    .append(" Lv" + skill.getLevel())
                            .withStyle(ChatFormatting.DARK_GRAY);
            
            guiGraphics.drawString(this.font,
                    display,
                    x, y, 0xFFAA00, false
            );
        }
        guiGraphics.pose().popPose();
    }

    private int getSkillLevel(int globalIndex) {
        if (globalIndex < 0 || globalIndex >= PlayerClientData.passiveSkills.size()) {
            return 0;
        }
        PassiveSkillInstance skill = PlayerClientData.passiveSkills.get(globalIndex);
        return skill.getLevel();
    }

    private String getSkillName(int globalIndex) {
        if (globalIndex < 0 || globalIndex >= PlayerClientData.passiveSkills.size()) {
            return "Unknown Skill";
        }
        PassiveSkillInstance skill = PlayerClientData.passiveSkills.get(globalIndex);
        return skill.getName();
    }

    private Component getSkillInfo(int selectedDetail) {
//        System.out.println("Getting info for selectedDetail: " + selectedDetail);
        if (selectedDetail >= 100000) {
            int idx = selectedDetail - 100000;
            if (idx >= PlayerClientData.activeSkills.size()) {
                return Component.literal("No skill info available.");
            }
            ActiveSkill activeSkill = PlayerClientData.activeSkills.get(idx);
            return activeSkill.getInfo(activeSkill.getSectType() == PlayerClientData.sect, activeSkill.getDisplayPowerType());
        }
        if (selectedDetail < 0 || selectedDetail >= PlayerClientData.passiveSkills.size()) {
            return Component.literal("No skill info available.");
        }
        PassiveSkillInstance skill = PlayerClientData.passiveSkills.get(selectedDetail);
        return skill.getInfo(PlayerClientData.sect == skill.getSectType());
    }
    private void drawInfoText(GuiGraphics guiGraphics, int selectedDetail, float scaleT) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scaleT, scaleT, 1.0f);
        int dialogWidth = 450;
        Component text = getSkillInfo(selectedDetail);

        drawWordWrapWithSpacing(guiGraphics, this.font, text,
                (int) ((topLeftX + 25) / scaleT),
                (int) ((topLeftY + 10) / scaleT),
                dialogWidth, // max width in pixels
                0xFF0000, 3);
        guiGraphics.pose().popPose();

    }

    private void spawnSparklesAt(int x, int y) {
        for (int i = 0; i < 20; i++) {
            sparkleParticles.add(new GuiSparkleParticle(
                    x + (float) (Math.random() * 6 - 3),
                    y + (float) (Math.random() * 6 - 3)
            ));
        }
    }

    private void onDeleteConfirm(boolean confirmed) {
        if (confirmed) {
            if (selectedDetail >= 100000) {
                int idx = selectedDetail - 100000;
                if (idx < 0 || idx >= PlayerClientData.activeSkills.size()) return;
                ActiveSkill skill = PlayerClientData.activeSkills.get(idx);
                // remove wheel slot assignment if any
                for (int slot = 1; slot <= 4; slot++) {
                    if (PlayerClientData.getWheelSkillId(slot) != null
                            && PlayerClientData.getWheelSkillId(slot).equals(skill.getId())) {
                        PlayerClientData.setWheelSlot(slot, null);
                    }
                }
                ModNetworking.INSTANCE.sendToServer(new DeleteSkillC2S(skill.getId().toString(), 1));
                selectedDetail = -1;
                Minecraft.getInstance().setScreen(new PlayerInfoSkillScreen());
                return;
            }
            else if (selectedDetail < 0 || selectedDetail >= PlayerClientData.passiveSkills.size()) {
                return;
            }
            String skillName =
                    PlayerClientData.passiveSkills.get(selectedDetail).getName();
//            System.out.println("Requesting deletion of skill: " + skillName);
            // send packet to server here
            ModNetworking.INSTANCE.sendToServer(new DeleteSkillC2S(skillName, 0));
            selectedDetail = -1;
            Minecraft.getInstance().setScreen(new PlayerInfoSkillScreen());
        } else {
            Minecraft.getInstance().setScreen(this);
        }
    }
}