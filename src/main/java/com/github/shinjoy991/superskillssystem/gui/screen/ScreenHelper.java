package com.github.shinjoy991.superskillssystem.gui.screen;

import com.github.shinjoy991.superskillssystem.gui.ScaledStatImageButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

public class ScreenHelper {

    public static AbstractWidget addBtn(
            int xLocOnGui, int yLocOnGui,
            int btnW, int btnH,
            int u, int v, int hoverStateOffsetV,
            ResourceLocation widgetLoc,
            int buttonImageWidth, int buttonImageHeight,
            float scale, boolean delayPopUp,
            Runnable onClick
    ) {
        ScaledStatImageButton btn = new ScaledStatImageButton(
                xLocOnGui, yLocOnGui,
                btnW, btnH,
                u, v, hoverStateOffsetV,
                widgetLoc, buttonImageWidth, buttonImageHeight,
                scale, delayPopUp,
                (button) -> onClick.run()
        );
        btn.visible = true;
        return btn;
    }

    public static AbstractWidget addBtn(
            int xLocOnGui, int yLocOnGui,
            int btnW, int btnH,
            int u, int v, int hoverStateOffsetV,
            ResourceLocation widgetLoc,
            int buttonImageWidth, int buttonImageHeight,
            float scale, boolean delayPopUp, boolean visible,
            Runnable onClick
    ) {
        ScaledStatImageButton btn = new ScaledStatImageButton(
                xLocOnGui, yLocOnGui,
                btnW, btnH,
                u, v, hoverStateOffsetV,
                widgetLoc, buttonImageWidth, buttonImageHeight,
                scale, delayPopUp,
                (button) -> onClick.run()
        );
        btn.visible = visible;
        return btn;
    }
}
