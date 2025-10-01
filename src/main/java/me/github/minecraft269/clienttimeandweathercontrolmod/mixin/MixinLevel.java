package me.github.minecraft269.clienttimeandweathercontrolmod.mixin;

import me.github.minecraft269.clienttimeandweathercontrolmod.config.ConfigStorage;
import me.github.minecraft269.clienttimeandweathercontrolmod.weather.WeatherController;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mixin(Level.class)
public class MixinLevel {
    private static final Logger LOGGER = LogManager.getLogger("ClientTimeAndWeatherControlMod/MixinLevel");

    @Inject(method = "getRainLevel", at = @At("HEAD"), cancellable = true)
    private void onGetRainLevel(float delta, CallbackInfoReturnable<Float> cir) {
        if (ConfigStorage.isWeatherActive() && ConfigStorage.isOverrideRainParticles()) {
            WeatherController weatherController = WeatherController.getInstance();
            cir.setReturnValue(weatherController.shouldPlayRainSound() ? 1.0f : 0.0f);
        }
    }

    @Inject(method = "getThunderLevel", at = @At("HEAD"), cancellable = true)
    private void onGetThunderLevel(float delta, CallbackInfoReturnable<Float> cir) {
        if (ConfigStorage.isWeatherActive() && ConfigStorage.isOverrideLightning()) {
            WeatherController weatherController = WeatherController.getInstance();
            cir.setReturnValue(weatherController.shouldShowLightning() ? 1.0f : 0.0f);
        }
    }

    @Inject(method = "isRaining", at = @At("HEAD"), cancellable = true)
    private void onIsRaining(CallbackInfoReturnable<Boolean> cir) {
        if (ConfigStorage.isWeatherActive() && ConfigStorage.isOverrideRainParticles()) {
            WeatherController weatherController = WeatherController.getInstance();
            cir.setReturnValue(weatherController.shouldPlayRainSound());
        }
    }

    @Inject(method = "isThundering", at = @At("HEAD"), cancellable = true)
    private void onIsThundering(CallbackInfoReturnable<Boolean> cir) {
        if (ConfigStorage.isWeatherActive() && ConfigStorage.isOverrideLightning()) {
            WeatherController weatherController = WeatherController.getInstance();
            cir.setReturnValue(weatherController.shouldShowLightning());
        }
    }
}