package me.github.minecraft269.clienttimeandweathercontrolmod.config;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final KeyMapping OPEN_CONFIG = new KeyMapping(
            "key.clienttimeandweathercontrolmod.open_config",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_C),
            "category.clienttimeandweathercontrolmod"
    );
}