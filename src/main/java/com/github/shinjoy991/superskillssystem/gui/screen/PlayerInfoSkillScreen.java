package com.github.shinjoy991.superskillssystem.gui.screen;

import com.github.shinjoy991.superskillssystem.SSS;
import com.github.shinjoy991.superskillssystem.gui.ScaledStatImageButton;
import com.github.shinjoy991.superskillssystem.helpers.PlayerClientData;
import com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkillInstance;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;

import static com.github.shinjoy991.superskillssystem.gui.screen.ScreenHelper.addBtn;

@OnlyIn(Dist.CLIENT)
public class PlayerInfoSkillScreen extends Screen {
    private static final ResourceLocation DETAIL_SCREEN_LOC =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/gui/player_info_skill.png");
    private static final ResourceLocation PLAYER_INFO_WIDGET_LOC =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/gui/player_info_widget.png");

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
    private int textOffsetTopLeftX = 10;
    private int textOffsetTopLeftY = 50;
    private int textSpacingX = 120;
    private int textSpacingY = 17;

    private int detailBtnTopLeftX = 90;

    private static final int MAX_ROWS = 8;
    private static final int SKILLS_PER_COL = MAX_ROWS;   // 8 items per column
    private static final int TOTAL_DETAIL_BTNS = SKILLS_PER_COL * 2; // 16 buttons
    private int currentPassivePage = 1, maxPassivePage;
    private int currentActivePage = 1,  maxActivePage;

    // Detail flags
    private int selectedDetail = -1;
    private final Map<Integer, ScaledStatImageButton> detailBtnMap = new HashMap<>();

    private final List<GuiSparkleParticle> sparkleParticles = new ArrayList<>();
    private int lastMouseX, lastMouseY;

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
                topLeftX + (imageWidth - 50) / 2 - 80,
                topLeftY + 185
                , tabBtnW, tabBtnH,
                0, 0, tabBtnH,  // u, v, hoverOffsetV
                PLAYER_INFO_WIDGET_LOC, buttonImageWH, buttonImageWH,
                (btn) -> this.minecraft.setScreen(new PlayerInfoScreen())
        ));

        // Add detail button
        this.addRenderableWidget(new ImageButton(
                topLeftX + (imageWidth - tabBtnH) / 2,
                topLeftY + 185,
                tabBtnW, tabBtnH,
                0, 0, tabBtnH,  // u, v, hover state OffsetV
                PLAYER_INFO_WIDGET_LOC, buttonImageWH, buttonImageWH,
                (btn) -> this.minecraft.setScreen(new PlayerInfoDetailScreen())
        ));

        PlayerClientData.passiveSkills.sort(Comparator.comparing(skill -> skill.getName().toLowerCase()));
        maxPassivePage = (PlayerClientData.passiveSkills.size()  + SKILLS_PER_COL - 1) / SKILLS_PER_COL;
        maxActivePage  = (PlayerClientData.activeSkills.size()  + SKILLS_PER_COL - 1) / SKILLS_PER_COL;

//        this.maxActivePage = screenData.activeSkills.size() / 8 + 1;

        // Add page buttons for active skills
        addBtn(
                topLeftX + 360, topLeftY + 66, statBtnW, statBtnH,
                64, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWH, buttonImageWH,
                0.5f, true,
                () -> {
                    this.currentActivePage = Math.min(maxActivePage, this.currentActivePage + 1);
                }
        );
        addBtn(
                topLeftX + 350, topLeftY + 66, statBtnW, statBtnH,
                78, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWH, buttonImageWH,
                0.5f, true,
                () -> {
                    this.currentActivePage = Math.max(1, this.currentActivePage - 1);
                }
        );
        // Add page buttons for passive skills
        addBtn(
                topLeftX + 190, topLeftY + 66, statBtnW, statBtnH,
                64, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWH, buttonImageWH,
                0.5f, true,
                () -> {
                    this.currentPassivePage = Math.min(maxPassivePage, this.currentPassivePage + 1);
                }
        );
        addBtn(
                topLeftX + 180, topLeftY + 66, statBtnW, statBtnH,
                78, 0, statBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWH, buttonImageWH,
                0.5f, true,
                () -> {
                    this.currentPassivePage = Math.max(1, this.currentPassivePage - 1);
                }
        );

        // Add detail buttons for both
        int number = 0;
        for (int col = 0; col < 2; col++) {
            for (int row = 0; row < MAX_ROWS; row++) {
                int x = topLeftX + detailBtnTopLeftX + textSpacingX * col;
                int y = topLeftY + textOffsetTopLeftY + textSpacingY * row;

                int finalNumber = number;
                ScaledStatImageButton btn = (ScaledStatImageButton) addBtn(
                        x, y,
                        detailBtnW, detailBtnH, 0, 48, detailBtnH,
                        PLAYER_INFO_WIDGET_LOC, buttonImageWH, buttonImageWH,
                        0.3f, false, false,
                        () -> {
                            this.selectedDetail = finalNumber;
                            spawnSparklesAt(lastMouseX, lastMouseY);
                        }
                );
                this.addRenderableWidget(btn);
                detailBtnMap.put(number, btn);
                number++;
            }
        }

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
//        drawActiveSkillNames(guiGraphics);
        if (selectedDetail > -1) {
            drawInfoText(guiGraphics, selectedDetail, 0.55f);
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
        guiGraphics.pose().scale(0.8f, 0.8f, 1.0f);
        guiGraphics.drawString(this.font, Component.literal("Player Info").withStyle(ChatFormatting.DARK_GRAY),
                (int) ((topLeftX + (float) (imageWidth - tabBtnH) / 2) - 31 / 0.8f),
                (int) ((topLeftY + 189) / 0.8f), 0xFFAA00, false);
        guiGraphics.drawString(this.font, Component.literal("Detail Info").withStyle(ChatFormatting.DARK_GRAY),
                (int) ((topLeftX + (float) (imageWidth - tabBtnH) / 2) / 0.8f),
                (int) ((topLeftY + 189) / 0.8f), 0xFFAA00, false);
        guiGraphics.drawString(this.font, Component.literal("Skill Info").withStyle(ChatFormatting.DARK_GRAY),
                (int) ((topLeftX + (float) (imageWidth - tabBtnH) / 2) + 120 / 0.8f),
                (int) ((topLeftY + 190) / 0.8f),
                0xFFAA00, false);
        guiGraphics.pose().popPose();
    }

    private void drawSkillNames(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.55f, 0.55f, 1f);

        int pageOffset = (currentPassivePage - 1) * SKILLS_PER_COL;
        for (int row = 0; row < SKILLS_PER_COL; row++) {
            int globalIdx = pageOffset + row;
            ScaledStatImageButton btn = detailBtnMap.get(row);
            boolean hasSkill = globalIdx < PlayerClientData.passiveSkills.size();

            // Ẩn/hiện nút
            if (btn != null) btn.visible = hasSkill;

            if (!hasSkill) continue;
            PassiveSkillInstance skill = PlayerClientData.passiveSkills.get(globalIdx);
            String name = skill.getName();
            int lvl = skill.getLevel();

            int x = (int)((topLeftX + textOffsetTopLeftX) / 0.55f);
            int y = (int)((topLeftY + textOffsetTopLeftY + row * textSpacingY) / 0.55f);

            guiGraphics.drawString(this.font,
                    Component.literal(name + " " + lvl).withStyle(ChatFormatting.DARK_GRAY),
                    x, y, 0xFFAA00, false);
        }

        guiGraphics.pose().popPose();
    }
    private void drawActiveSkillNames(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.55f, 0.55f, 1f);

        int pageOffset = (currentActivePage - 1) * SKILLS_PER_COL;
        for (int row = 0; row < SKILLS_PER_COL; row++) {
            int globalIdx = pageOffset + row;
            int btnKey = SKILLS_PER_COL + row; // nút 8–15
            ScaledStatImageButton btn = detailBtnMap.get(btnKey);
            boolean hasSkill = globalIdx < PlayerClientData.activeSkills.size();

            // Ẩn/hiện nút
            if (btn != null) btn.visible = hasSkill;

            if (!hasSkill) continue;
            PassiveSkillInstance skill = PlayerClientData.activeSkills.get(globalIdx);
            String name = skill.getName();
            int lvl = skill.getLevel();

            // Dịch x sang cột 1
            int x = (int)((topLeftX + textOffsetTopLeftX + textSpacingX) / 0.55f);
            int y = (int)((topLeftY + textOffsetTopLeftY + row * textSpacingY) / 0.55f);

            guiGraphics.drawString(this.font,
                    Component.literal(name + " " + lvl).withStyle(ChatFormatting.DARK_GRAY),
                    x, y, 0xFFAA00, false);
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

    private Component getSkillInfo(int type) {
        if (type > PlayerClientData.passiveSkills.size()) {
            return Component.literal("No skill info available.");
        }
        PassiveSkillInstance skill = PlayerClientData.passiveSkills.get(type);
        if (PlayerClientData.sect == skill.getSectType()) {
            return skill.getInfo(true);
        }
        return skill.getInfo(false);
    }

    private void drawInfoText(GuiGraphics guiGraphics, int type, float scaleT) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scaleT, scaleT, 1.0f);
        int dialogWidth = 600;

        Component text = getSkillInfo(type);

        guiGraphics.drawWordWrap(this.font, text,
                (int) ((topLeftX + 20) / scaleT),
                (int) ((topLeftY + 10) / scaleT),
                dialogWidth - 10, // max width in pixels
                0xFF0000
        );
        guiGraphics.pose().popPose();

    }

    private void addDetailButton(int col, int row, int number) {
        int x = topLeftX + detailBtnTopLeftX + textSpacingX * col;
        int y = topLeftY + textOffsetTopLeftY + textSpacingY * row;

        ScaledStatImageButton button = (ScaledStatImageButton) addBtn(
                x, y,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWH, buttonImageWH,
                0.3f, false, false,
                () -> {
                    this.selectedDetail = number;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        );
        this.addRenderableWidget(button);
        detailBtnMap.put(number, button);
    }


    private void spawnSparklesAt(int x, int y) {
        for (int i = 0; i < 20; i++) {
            sparkleParticles.add(new GuiSparkleParticle(
                    x + (float) (Math.random() * 6 - 3),
                    y + (float) (Math.random() * 6 - 3)
            ));
        }
    }

//    private int extractSkillNumber(String name) {
//        try {
//            // Tìm số đầu tiên trong chuỗi (ví dụ "Level 12" -> 12)
//            String numberStr = name.replaceAll("[^0-9]", " ").trim().split("\\s+")[0];
//            return Integer.parseInt(numberStr);
//        } catch (Exception e) {
//            return Integer.MAX_VALUE; // skill nào không có số thì đẩy ra sau
//        }
//    }

}