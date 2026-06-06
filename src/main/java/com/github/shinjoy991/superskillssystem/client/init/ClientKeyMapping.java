package com.github.shinjoy991.superskillssystem.client.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ClientKeyMapping {
    public static final String KEY_CATEGORY_RELOAD = "key.category.sss";

    public static final String KEY_DESCRIPTION = "key.category.sss.reload";
    public static final String KEY_DESCRIPTION_SKILL_WHEEL = "key.category.sss.skill_wheel";

    public static final KeyMapping PLAYER_INFO_KEY =
            new KeyMapping(
                    KEY_DESCRIPTION,
                    KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_0,
                    KEY_CATEGORY_RELOAD);

    public static final KeyMapping SKILL_WHEEL_KEY =
            new KeyMapping(
                    KEY_DESCRIPTION_SKILL_WHEEL,
                    KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_V,
                    KEY_CATEGORY_RELOAD);
}