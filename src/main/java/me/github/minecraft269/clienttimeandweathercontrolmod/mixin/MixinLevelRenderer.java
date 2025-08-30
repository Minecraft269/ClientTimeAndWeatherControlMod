package me.github.minecraft269.clienttimeandweathercontrolmod.mixin;

import me.github.minecraft269.clienttimeandweathercontrolmod.config.ConfigStorage;
import me.github.minecraft269.clienttimeandweathercontrolmod.time.TimeStorage;
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
            method = "renderLevel",
            at = @At(value = "HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private float modifySkyRenderTime(float partialTick) {
        // 如果是SkyOnly模式且模组激活，修改天空渲染时间
        if (ConfigStorage.isActive() &&
                ConfigStorage.getTimeType().getSerializedName().equals("sky_only")) {
            int customTime = TimeStorage.getInstance().getSkyRenderTime();
            if (customTime != -1) {
                LOGGER.debug("Modifying sky render time to: " + customTime);
                return customTime;
            }
        }
        return partialTick;
    }
}