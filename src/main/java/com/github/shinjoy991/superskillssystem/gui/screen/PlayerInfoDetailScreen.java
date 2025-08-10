package com.github.shinjoy991.superskillssystem.gui.screen;

import com.github.shinjoy991.superskillssystem.SSS;
import com.github.shinjoy991.superskillssystem.helpers.PlayerClientData;
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

import java.util.ArrayList;
import java.util.List;

import static com.github.shinjoy991.superskillssystem.gui.screen.ScreenHelper.addBtn;

@OnlyIn(Dist.CLIENT)
public class PlayerInfoDetailScreen extends Screen {
    private static final ResourceLocation DETAIL_SCREEN_LOC =
            new ResourceLocation(SSS.MODID, "textures/gui/player_info_detail.png");
    private static final ResourceLocation PLAYER_INFO_WIDGET_LOC =
            new ResourceLocation(SSS.MODID, "textures/gui/player_info_widget.png");

    private final Player player;

    private final int imageWidth = 361;
    private final int imageHeight = 208;

    private final int tabBtnW = 50;
    private final int tabBtnH = 13;
    private final int statBtnW = 11;
    private final int statBtnH = 18;
    private final int detailBtnW = 16 * 6;
    private final int detailBtnH = 16 * 3;

    private ImageButton skillBtn;
    private ImageButton infoButton;

    private int topLeftX;
    private int topLeftY;
    private int textOffsetTopLeftX = 10;
    private int textOffsetTopLeftY = 30;
    private int textSpacingX = 120;
    private int textSpacingY = 17;

    private int detailBtnTopLeftX = 90;

    // Detail flags
    private DetailType selectedDetail = null;

    private final List<GuiSparkleParticle> sparkleParticles = new ArrayList<>();
    private int lastMouseX, lastMouseY;

    public PlayerInfoDetailScreen() {
        super(Component.literal("Player Info Details"));
        this.player = Minecraft.getInstance().player;
    }

    @Override
    protected void init() {
        super.init();

        topLeftX = (this.width - imageWidth) / 2;
        topLeftY = (this.height - imageHeight) / 2;

        int buttonImageHeight = 256;
        int buttonImageWidth = 256;
        infoButton = new ImageButton(
                topLeftX + (imageWidth - 50) / 2 - 80,
                topLeftY + 185
                , 50, 13,
                0, 0, 13,  // u, v, hoverOffsetV
                PLAYER_INFO_WIDGET_LOC, buttonImageHeight, buttonImageWidth,
                (btn) -> this.minecraft.setScreen(new PlayerInfoScreen())
        );
        this.addRenderableWidget(infoButton);

        skillBtn = new ImageButton(
                topLeftX + (imageWidth - 50) / 2 + 80,
                topLeftY + 185,
                50, 13,
                0, 0, 13,  // u, v, hoverOffsetV
                PLAYER_INFO_WIDGET_LOC, buttonImageHeight, buttonImageWidth,
                (btn) -> this.minecraft.setScreen(new PlayerInfoSkillScreen())
        );
        this.addRenderableWidget(skillBtn);
        // Add Detail button
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX, topLeftY + textOffsetTopLeftY,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.WEAPON_DMG;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX, topLeftY + textOffsetTopLeftY + textSpacingY,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.ATK_DMG;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX, topLeftY + textOffsetTopLeftY + textSpacingY * 2,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.RANGE_DMG;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX, topLeftY + textOffsetTopLeftY + textSpacingY * 3,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.MAGIC_DMG;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX, topLeftY + textOffsetTopLeftY + textSpacingY * 4,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.DEF;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX, topLeftY + textOffsetTopLeftY + textSpacingY * 5,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.DEF_PEN;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX, topLeftY + textOffsetTopLeftY + textSpacingY * 6,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.MAGIC_DEF;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX, topLeftY + textOffsetTopLeftY + textSpacingY * 7,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.MAGIC_PEN;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX, topLeftY + textOffsetTopLeftY + textSpacingY * 8,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.CRIT_CHANCE;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX, topLeftY + textOffsetTopLeftY,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.CRIT_DMG;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX, topLeftY + textOffsetTopLeftY + textSpacingY,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.ATTACK_SPEED;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX, topLeftY + textOffsetTopLeftY + textSpacingY * 2,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.SPEED;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX, topLeftY + textOffsetTopLeftY + textSpacingY * 3,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.DMG_RED;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX, topLeftY + textOffsetTopLeftY + textSpacingY * 4,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.MAGIC_RESIST;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX, topLeftY + textOffsetTopLeftY + textSpacingY * 5,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.EVASION;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX, topLeftY + textOffsetTopLeftY + textSpacingY * 6,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.ACCURACY;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX, topLeftY + textOffsetTopLeftY + textSpacingY * 7,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.COUNTER_CHANCE;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX, topLeftY + textOffsetTopLeftY + textSpacingY * 8,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.RESISTANCE;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX * 2, topLeftY + textOffsetTopLeftY,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.LIFE_STEAL;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX * 2, topLeftY + textOffsetTopLeftY + textSpacingY,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.MANA_STEAL;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX * 2, topLeftY + textOffsetTopLeftY + textSpacingY * 2,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.HEAL_REGEN;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX * 2, topLeftY + textOffsetTopLeftY + textSpacingY * 3,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.MANA_REGEN;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX * 2, topLeftY + textOffsetTopLeftY + textSpacingY * 4,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.DMG_RED_PEN;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX * 2, topLeftY + textOffsetTopLeftY + textSpacingY * 5,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.MAGIC_RESIST_PEN;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX * 2, topLeftY + textOffsetTopLeftY + textSpacingY * 6,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.RESISTANCE_PEN;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));

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
        drawPlayerStats(guiGraphics);
        if (selectedDetail != null) {
            drawInfoText(guiGraphics, selectedDetail, 0.55f);
        }


    }


    private void drawSparkles(GuiGraphics guiGraphics) {
        sparkleParticles.removeIf(p -> !p.isAlive());
        for (GuiSparkleParticle p : sparkleParticles) {
            p.tick();
            float alpha = p.getAlpha();
            int alphaByte = (int)(alpha * 255.0f);
            int sparkleColor = (alphaByte << 24) | 0x00FFFFFF;
            guiGraphics.fill((int)p.x, (int)p.y, (int)p.x + 2, (int)p.y + 2, sparkleColor);
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
                (int) ((topLeftX + (float) (imageWidth - tabBtnH) / 2) + 120/ 0.8f),
                (int) ((topLeftY + 190) / 0.8f),
                0xFFAA00, false);
        guiGraphics.pose().popPose();
    }

    private void drawPlayerStats(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        float scaleT = 0.55f;
        guiGraphics.pose().scale(scaleT, scaleT, 1.0f);

        drawStat(guiGraphics, "Weapon Damage: " + String.format("%.2f", PlayerClientData.WeaponDmg) + " (" + String.format("%.2f", PlayerClientData.WeaponDmgBonus / 100.0) + ")", 0, 0);
        drawStat(guiGraphics, "Attack Damage: " + String.format("%.2f", PlayerClientData.AtkDmg) + " (" + String.format("%.2f", (PlayerClientData.bonusDmg)) + ")", 0, 1);
        drawStat(guiGraphics, "Range Damage: " + String.format("%.2f", PlayerClientData.RangeDmg), 0, 2);
        drawStat(guiGraphics, "Magic Damage: " + String.format("%.2f", PlayerClientData.MagicDmg), 0, 3);
        drawStat(guiGraphics, "Def: " + String.format("%.2f", PlayerClientData.Def), 0, 4);
        drawStat(guiGraphics, "Def Penetration: " + String.format("%.2f", PlayerClientData.DefPen), 0, 5);
        drawStat(guiGraphics, "Magic Def: " + String.format("%.2f", PlayerClientData.MagicDef), 0, 6);
        drawStat(guiGraphics, "Magic Penetration: " + String.format("%.2f", PlayerClientData.MagicPen), 0, 7);
        drawStat(guiGraphics, "Critical Chance: " + String.format("%.2f", PlayerClientData.CritChance) + "%", 0, 8);
        drawStat(guiGraphics, "Perfection: " + String.format("%.2f", PlayerClientData.Perfection) + "%", 1, 0);
        drawStat(guiGraphics, "Attack Speed: " + String.format("%.2f", PlayerClientData.AttackSpeed), 1, 1);
        drawStat(guiGraphics, "Speed: " + String.format("%.2f", PlayerClientData.Speed), 1, 2);
        drawStat(guiGraphics, "Damage Reduction: " + String.format("%.2f", PlayerClientData.DmgRed) + "%", 1, 3);
        drawStat(guiGraphics, "Magic Resistance: " + String.format("%.2f", PlayerClientData.MagicResist) + "%", 1, 4);
        drawStat(guiGraphics, "Evasion: " + String.format("%.2f", PlayerClientData.Evasion) + "%", 1, 5);
        drawStat(guiGraphics, "Accuracy: " + String.format("%.2f", PlayerClientData.Accuracy) + "%", 1, 6);
        drawStat(guiGraphics, "Counter Chance: " + String.format("%.2f", PlayerClientData.CounterChance) + "%", 1, 7);
        drawStat(guiGraphics, "Resistance: " + String.format("%.2f", PlayerClientData.Resistance) + "%", 1, 8);
        drawStat(guiGraphics, "Life Steal: " + String.format("%.2f", PlayerClientData.LifeSteal) + "%", 2, 0);
        drawStat(guiGraphics, "Mana Steal: " + String.format("%.2f", PlayerClientData.ManaSteal) + "%", 2, 1);
        drawStat(guiGraphics, "Health Regeneration: " + String.format("%.2f", PlayerClientData.HealRegen) + "/s", 2, 2);
        drawStat(guiGraphics, "Mana Regeneration: " + String.format("%.2f", PlayerClientData.ManaRegen) + "/s", 2, 3);
        drawStat(guiGraphics, "Damage Reduction Penetration: " + String.format("%.2f", PlayerClientData.DmgRedPen) + "%", 2, 4);
        drawStat(guiGraphics, "Magic Resistance Penetration: " + String.format("%.2f", PlayerClientData.MagicResistPen) + "%", 2, 5);
        drawStat(guiGraphics, "Resistance Penetration: " + String.format("%.2f", PlayerClientData.ResistancePen) + "%", 2, 6);


        guiGraphics.pose().popPose();
    }

    private void drawStat(GuiGraphics g, String text, int col, int row) {
        float scaleT = 0.55f;
        int x = (int)((topLeftX + textOffsetTopLeftX + col * textSpacingX) / scaleT);
        int y = (int)((topLeftY + textOffsetTopLeftY + row * textSpacingY) / scaleT);
        g.drawString(this.font, Component.literal(text).withStyle(ChatFormatting.DARK_GRAY), x, y, 0xFFAA00, false);
    }

    private void drawInfoText(GuiGraphics guiGraphics, DetailType type, float scaleT) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scaleT, scaleT, 1.0f);
        int dialogWidth = 600;
//        int dialogHeight = 100;

//        guiGraphics.fill((int) ((i + 224) / scaleT), (int) (j / scaleT),
//                (int) ((i + 200 + dialogWidth) / scaleT), j + 65 + dialogHeight, 0xCC000000);
        Component text;
        switch (type) {
            case WEAPON_DMG -> text = Component.literal("Strength affects physical damage, some defense");
            case ATK_DMG -> text = Component.literal("Vitality affects health and some defense");
            case RANGE_DMG -> text = Component.literal("Dexterity affects ranged damage, accuracy, and evasion");
            case MAGIC_DMG -> text = Component.literal("Intelligence affects magic damage, magic Def, and magic resistance");
            case DEF -> text = Component.literal("Def reduces physical damage taken");
            case DEF_PEN -> text = Component.literal("Def Penetration ignores a percentage of the target's Def");
            case MAGIC_DEF -> text = Component.literal("Magic Def reduces magic damage taken");
            case MAGIC_PEN -> text = Component.literal("Magic Penetration ignores a percentage of the target's magic Def");
            case CRIT_CHANCE -> text = Component.literal("Critical Chance increases the chance to deal critical damage");
            case CRIT_DMG -> text = Component.literal("Critical Damage increases the damage dealt by critical hits");
            case ATTACK_SPEED -> text = Component.literal("Attack Speed increases the speed of basic attacks");
            case SPEED -> text = Component.literal("Speed increases movement speed");
            case DMG_RED -> text = Component.literal("Damage Reduction reduces incoming damage");
            case MAGIC_RESIST -> text = Component.literal("Magic Resistance reduces incoming magic damage");
            case EVASION -> text = Component.literal("Evasion increases the chance to avoid attacks");
            case ACCURACY -> text = Component.literal("Accuracy increases the chance to hit with attacks");
            case COUNTER_CHANCE -> text = Component.literal("Counter Chance increases the chance to counter an attack");
            case RESISTANCE -> text = Component.literal("Resistance reduces the duration of negative effects");
            case LIFE_STEAL -> text = Component.literal("Life Steal converts a percentage of damage dealt into health");
            case MANA_STEAL -> text = Component.literal("Mana Steal converts a percentage of damage dealt into mana");
            case HEAL_REGEN -> text = Component.literal("Health Regeneration restores health over time");
            case MANA_REGEN -> text = Component.literal("Mana Regeneration restores mana over time");
            case DMG_RED_PEN -> text = Component.literal("Damage Reduction Penetration ignores a percentage of the target's damage reduction");
            case MAGIC_RESIST_PEN -> text = Component.literal("Magic Resistance Penetration ignores a percentage of the target's magic resistance");
            case RESISTANCE_PEN -> text = Component.literal("Resistance jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjPenetration ignores a percentage of the target's resistance");
            default -> {
                return;
            }
        }


            guiGraphics.drawWordWrap(this.font, text,
                    (int) ((topLeftX + 12) / scaleT),
                    (int) ((topLeftY + 12) / scaleT),
                    dialogWidth - 10, // max width in pixels
                    0xFF0000
            );
        guiGraphics.pose().popPose();

    }

    private void spawnSparklesAt(int x, int y) {
        for (int i = 0; i < 20; i++) {
            sparkleParticles.add(new GuiSparkleParticle(
                    x + (float)(Math.random() * 6 - 3),
                    y + (float)(Math.random() * 6 - 3)
            ));
        }
    }

    private enum DetailType {
        WEAPON_DMG,
        ATK_DMG,
        RANGE_DMG,
        MAGIC_DMG,
        DEF,
        DEF_PEN,
        MAGIC_DEF,
        MAGIC_PEN,
        CRIT_CHANCE,
        CRIT_DMG,
        ATTACK_SPEED,
        SPEED,
        DMG_RED,
        MAGIC_RESIST,
        EVASION,
        ACCURACY,
        COUNTER_CHANCE,
        RESISTANCE,
        LIFE_STEAL,
        MANA_STEAL,
        HEAL_REGEN,
        MANA_REGEN,
        DMG_RED_PEN,
        MAGIC_RESIST_PEN,
        RESISTANCE_PEN
    }
}