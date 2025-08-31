package me.github.minecraft269.clienttimeandweathercontrolmod.mixin;

import me.github.minecraft269.clienttimeandweathercontrolmod.config.ConfigStorage;
import me.github.minecraft269.clienttimeandweathercontrolmod.moon.MoonPhaseController;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {
    private static final Logger LOGGER = LogManager.getLogger("ClientTimeAndWeatherControlMod/MixinLevelRenderer");

    @ModifyVariable(
            method = "renderSky",
            at = @At(value = "STORE"),
            ordinal = 0
    )
    private int modifyMoonPhase(int originalPhase) {
        if (ConfigStorage.isMoonActive()) {
            int customPhase = MoonPhaseController.getInstance().getMoonPhase();
            LOGGER.debug("Overriding moon phase from " + originalPhase + " to " + customPhase);
            return customPhase;
        }
        return originalPhase;
    }
}