package com.github.shinjoy991.superskillssystem.gui.screen;

import com.github.shinjoy991.superskillssystem.activeskills.ModSkills;
import com.github.shinjoy991.superskillssystem.activeskills.meleephysical.ActSkillThrust;
import com.github.shinjoy991.superskillssystem.helpers.PlayerClientData;
import com.github.shinjoy991.superskillssystem.network.ModNetworking;
import com.github.shinjoy991.superskillssystem.network.server.CastSkillC2S;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static com.github.shinjoy991.superskillssystem.SSS.MODID;

public class SkillWheelScreen extends Screen {

    private static final ResourceLocation BASE =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/skill/skill_wheel_base.png");
    private static final ResourceLocation PIE =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/skill/skill_pie.png");

    // atlas size
    private static final int TEX_SIZE = 256;
    private float scale = 1.0f;
    private static final int WHEEL_SIZE = 195;
    private static final int HALF = WHEEL_SIZE / 2;
    private final int size = (int) (WHEEL_SIZE * scale);
    private final int half = size / 2;

    public SkillWheelScreen() {
        super(Component.literal("Skill Wheel"));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // =========================
    // HOVER (QUADRANT LOGIC)
    // =========================
    private int getHoveredSector(int mouseX, int mouseY) {

        int cx = this.width / 2;
        int cy = this.height / 2;

        double dx = mouseX - cx;
        double dy = mouseY - cy;

        double dist = Math.sqrt(dx * dx + dy * dy);

        // inner dead zone
        if (dist < 20 * scale) return 0;

        boolean right = dx > 0;
        boolean bottom = dy > 0;

        if (!right && !bottom) return 1; // TL
        if ( right && !bottom) return 2; // TR
        if (!right &&  bottom) return 4; // BL

        return 3; // BR
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {

        // scroll to adjust scale live
        scale += delta * 0.1f;

        if (scale < 0.5f) scale = 0.5f;
        if (scale > 3.0f) scale = 3.0f;

        return true;
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if (button == 0) {
            int hovered = getHoveredSector((int) mouseX, (int) mouseY);
            if (hovered != 0) {
                ResourceLocation skillId = PlayerClientData.getWheelSkillId(hovered);
                if (skillId != null) {
                    ModNetworking.INSTANCE.sendToServer(new CastSkillC2S(skillId));
                }
                this.onClose();
            }
        }
        return true;
    }

    @Override
    public void render(GuiGraphics g,
                       int mouseX,
                       int mouseY,
                       float partialTick) {
        this.renderBackground(g);

        int cx = this.width / 2;
        int cy = this.height / 2;

        int hovered = getHoveredSector(mouseX, mouseY);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1f, 1f, 1f, 0.6f);
        // =========================
        // FINAL RENDER SIZE
        // =========================

        int x = cx - size / 2;
        int y = cy - size / 2;

        // =========================
        // BASE WHEEL (192x192 region)
        // =========================

        g.blit(BASE,
                x - 2, y - 3,
                0, 0,
                WHEEL_SIZE, WHEEL_SIZE,
                TEX_SIZE, TEX_SIZE);

        // =========================
        // HOVER PIE OVERLAY
        // (same atlas, same region logic)
        // =========================
        RenderSystem.setShaderColor(1f, 1f, 1f, 0.3f);
        if (hovered == 1) { // TL
            g.blit(PIE,
                    x, y,
                    0, 0,
                    half, half,
                    TEX_SIZE, TEX_SIZE);
        }

        if (hovered == 2) { // TR
            g.blit(PIE,
                    x + half, y,
                    HALF, 0,
                    half, half,
                    TEX_SIZE, TEX_SIZE);
        }

        if (hovered == 4) { // BL
            g.blit(PIE,
                    x, y + half,
                    0, HALF,
                    half, half,
                    TEX_SIZE, TEX_SIZE);
        }

        if (hovered == 3) { // BR
            g.blit(PIE,
                    x + half, y + half,
                    HALF, HALF,
                    half, half,
                    TEX_SIZE, TEX_SIZE);
        }
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
        // =========================
        // CENTER DOT
        // =========================
        g.fill(cx - 2, cy - 2, cx + 2, cy + 2, 0xFFFFFFFF);

        // =========================
        // DEBUG TEXT
        // =========================
        g.drawCenteredString(
                this.font,
                hovered == 0 ? "Skill Wheel" : "Selected: " + hovered,
                cx,
                cy - 90,
                0xFFFFFF
        );
    }


}