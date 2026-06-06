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
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/gui/player_info_detail.png");
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

    private ImageButton skillBtn;
    private ImageButton infoButton;

    private int topLeftX;
    private int topLeftY;
    private int textOffsetTopLeftX = 23;
    private int textOffsetTopLeftY = 38;
    private int textSpacingX = 112;
    private int textSpacingY = 16;

    private int detailBtnTopLeftX = 95;
    private int detailBtnTopLeftY = textOffsetTopLeftY - 5;

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
                topLeftX + (imageWidth - tabBtnW) / 2 - 80,
                topLeftY + 184
                , 50, 13,
                0, 0, 13,  // u, v, hoverOffsetV
                PLAYER_INFO_WIDGET_LOC, buttonImageHeight, buttonImageWidth,
                (btn) -> this.minecraft.setScreen(new PlayerInfoScreen())
        );
        this.addRenderableWidget(infoButton);

        skillBtn = new ImageButton(
                topLeftX + (imageWidth - tabBtnW) / 2 + 80,
                topLeftY + 184,
                50, 13,
                0, 0, 13,  // u, v, hoverOffsetV
                PLAYER_INFO_WIDGET_LOC, buttonImageHeight, buttonImageWidth,
                (btn) -> this.minecraft.setScreen(new PlayerInfoSkillScreen())
        );
        this.addRenderableWidget(skillBtn);
        // Add Detail button
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX, topLeftY + detailBtnTopLeftY,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.WEAPON_DMG;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX, topLeftY + detailBtnTopLeftY + textSpacingY,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.ATK_DMG;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX, topLeftY + detailBtnTopLeftY + textSpacingY * 2,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.RANGE_DMG;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX, topLeftY + detailBtnTopLeftY + textSpacingY * 3,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.MAGIC_DMG;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX, topLeftY + detailBtnTopLeftY + textSpacingY * 4,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.PERFECTION;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX, topLeftY + detailBtnTopLeftY + textSpacingY * 5,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.DEF;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX, topLeftY + detailBtnTopLeftY + textSpacingY * 6,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.DEF_PEN;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX, topLeftY + detailBtnTopLeftY + textSpacingY * 7,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.MAGIC_DEF;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX, topLeftY + detailBtnTopLeftY + textSpacingY * 8,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.MAGIC_PEN;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX, topLeftY + detailBtnTopLeftY,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.CRIT_CHANCE;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX, topLeftY + detailBtnTopLeftY + textSpacingY,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.ATTACK_SPEED;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX, topLeftY + detailBtnTopLeftY + textSpacingY * 2,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.SPEED;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX, topLeftY + detailBtnTopLeftY + textSpacingY * 3,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.DMG_RED;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX, topLeftY + detailBtnTopLeftY + textSpacingY * 4,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.MAGIC_RESIST;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX, topLeftY + detailBtnTopLeftY + textSpacingY * 5,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.EVASION;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX, topLeftY + detailBtnTopLeftY + textSpacingY * 6,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.ACCURACY;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX, topLeftY + detailBtnTopLeftY + textSpacingY * 7,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.COUNTER_CHANCE;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX, topLeftY + detailBtnTopLeftY + textSpacingY * 8,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.RESISTANCE;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX * 2, topLeftY + detailBtnTopLeftY,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.LIFE_STEAL;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX * 2, topLeftY + detailBtnTopLeftY + textSpacingY,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.MANA_STEAL;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX * 2, topLeftY + detailBtnTopLeftY + textSpacingY * 2,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.HEAL_REGEN;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX * 2, topLeftY + detailBtnTopLeftY + textSpacingY * 3,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.MANA_REGEN;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX * 2, topLeftY + detailBtnTopLeftY + textSpacingY * 4,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.DMG_RED_PEN;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX * 2, topLeftY + detailBtnTopLeftY + textSpacingY * 5,
                detailBtnW, detailBtnH, 0, 48, detailBtnH,
                PLAYER_INFO_WIDGET_LOC, buttonImageWidth, buttonImageHeight,
                0.3f, false,
                () -> {
                    this.selectedDetail = DetailType.MAGIC_RESIST_PEN;
                    spawnSparklesAt(lastMouseX, lastMouseY);
                }
        ));
        this.addRenderableWidget(addBtn(
                topLeftX + detailBtnTopLeftX + textSpacingX * 2, topLeftY + detailBtnTopLeftY + textSpacingY * 6,
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
        float scale = 0.7f;
        guiGraphics.pose().scale(scale, scale, 1.0f);
        guiGraphics.drawString(this.font, Component.literal("Player Info").withStyle(ChatFormatting.DARK_GRAY),
                (int) (((topLeftX + (float) (imageWidth - tabBtnW) / 2) - 76) / scale),
                (int) ((topLeftY + 188) / scale), 0xFFAA00, false);
        guiGraphics.drawString(this.font, Component.literal("Detail Info").withStyle(ChatFormatting.WHITE),
                (int) (((topLeftX + (float) (imageWidth - tabBtnW) / 2) + 7) / scale),
                (int) ((topLeftY + 188) / scale), 0xFFAA00, false);
        guiGraphics.drawString(this.font, Component.literal("Skill Info").withStyle(ChatFormatting.DARK_GRAY),
                (int) (((topLeftX + (float) (imageWidth - tabBtnW) / 2) + 90) / scale),
                (int) ((topLeftY + 188) / scale),
                0xFFAA00, false);
        guiGraphics.pose().popPose();
    }

    private void drawPlayerStats(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        float scaleT = 0.55f;
        guiGraphics.pose().scale(scaleT, scaleT, 1.0f);

        drawStat(guiGraphics, "Weapon Damage: " + String.format("%.2f", (PlayerClientData.WeaponDmg * (1 + (PlayerClientData.WeaponDmgBonus / 100.0)))) + " (" + String.format("%.2f", PlayerClientData.WeaponDmg) + ")", 0, 0);
        drawStat(guiGraphics, "Attack Damage: " + String.format("%.2f", (PlayerClientData.AtkDmg + PlayerClientData.bonusDmg)), 0, 1);
        drawStat(guiGraphics, "Range Damage: " + String.format("%.2f", PlayerClientData.RangeDmg), 0, 2);
        drawStat(guiGraphics, "Magic Damage: " + String.format("%.2f", PlayerClientData.MagicDmg), 0, 3);
        drawStat(guiGraphics, "Perfection: " + String.format("%.2f", PlayerClientData.Perfection) + "%", 0, 4);
        drawStat(guiGraphics, "Def: " + String.format("%.2f", PlayerClientData.Def), 0, 5);
        drawStat(guiGraphics, "Def Penetration: " + String.format("%.2f", PlayerClientData.DefPen), 0, 6);
        drawStat(guiGraphics, "Magic Def: " + String.format("%.2f", PlayerClientData.MagicDef), 0, 7);
        drawStat(guiGraphics, "Magic Penetration: " + String.format("%.2f", PlayerClientData.MagicPen), 0, 8);
        drawStat(guiGraphics, "Critical Chance: " + String.format("%.2f", PlayerClientData.CritChance) + "%", 1, 0);
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
        drawStat(guiGraphics, "Health Regen: " + String.format("%.2f", PlayerClientData.HealRegen) + "/s", 2, 2);
        drawStat(guiGraphics, "Mana Regen: " + String.format("%.2f", PlayerClientData.ManaRegen) + "/s", 2, 3);
        drawStat(guiGraphics, "Damage Red Pen: " + String.format("%.2f", PlayerClientData.DmgRedPen) + "%", 2, 4);
        drawStat(guiGraphics, "Magic Res Pen: " + String.format("%.2f", PlayerClientData.MagicResistPen) + "%", 2, 5);
        drawStat(guiGraphics, "Resistance Pen: " + String.format("%.2f", PlayerClientData.ResistancePen) + "%", 2, 6);


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
        int dialogWidth = 580;
//        int dialogHeight = 100;

//        guiGraphics.fill((int) ((i + 224) / scaleT), (int) (j / scaleT),
//                (int) ((i + 200 + dialogWidth) / scaleT), j + 65 + dialogHeight, 0xCC000000);
        Component text;
        switch (type) {
            case WEAPON_DMG -> text = Component.translatable("sentence.detail.weapon_dmg");
            case ATK_DMG -> text = Component.translatable("sentence.detail.atk_dmg");
            case RANGE_DMG -> text = Component.translatable("sentence.detail.range_dmg");
            case MAGIC_DMG -> text = Component.translatable("sentence.detail.magic_dmg");
            case DEF -> text = Component.translatable("sentence.detail.def");
            case DEF_PEN -> text = Component.translatable("sentence.detail.def_pen");
            case MAGIC_DEF -> text = Component.translatable("sentence.detail.magic_def");
            case MAGIC_PEN -> text = Component.translatable("sentence.detail.magic_pen");
            case CRIT_CHANCE -> text = Component.translatable("sentence.detail.crit_chance");
            case PERFECTION -> text = Component.translatable("sentence.detail.perfection");
            case ATTACK_SPEED -> text = Component.translatable("sentence.detail.attack_speed");
            case SPEED -> text = Component.translatable("sentence.detail.speed");
            case DMG_RED -> text = Component.translatable("sentence.detail.dmg_red");
            case MAGIC_RESIST -> text = Component.translatable("sentence.detail.magic_resist");
            case EVASION -> text = Component.translatable("sentence.detail.evasion");
            case ACCURACY -> text = Component.translatable("sentence.detail.accuracy");
            case COUNTER_CHANCE -> text = Component.translatable("sentence.detail.counter_chance");
            case RESISTANCE -> text = Component.translatable("sentence.detail.resistance");
            case LIFE_STEAL -> text = Component.translatable("sentence.detail.life_steal");
            case MANA_STEAL -> text = Component.translatable("sentence.detail.mana_steal");
            case HEAL_REGEN -> text = Component.translatable("sentence.detail.heal_regen");
            case MANA_REGEN -> text = Component.translatable("sentence.detail.mana_regen");
            case DMG_RED_PEN -> text = Component.translatable("sentence.detail.dmg_red_pen");
            case MAGIC_RESIST_PEN -> text = Component.translatable("sentence.detail.magic_resist_pen");
            case RESISTANCE_PEN -> text = Component.translatable("sentence.detail.resistance_pen");
            default -> {
                return;
            }
        }


            guiGraphics.drawWordWrap(this.font, text,
                    (int) ((topLeftX + 23) / scaleT),
                    (int) ((topLeftY + 12) / scaleT),
                    dialogWidth, // max width in pixels
                    0x000000
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
        PERFECTION,
        DEF,
        DEF_PEN,
        MAGIC_DEF,
        MAGIC_PEN,
        CRIT_CHANCE,
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