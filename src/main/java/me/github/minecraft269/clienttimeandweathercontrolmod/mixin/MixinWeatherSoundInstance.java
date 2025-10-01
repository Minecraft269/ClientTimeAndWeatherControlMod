package me.github.minecraft269.clienttimeandweathercontrolmod.mixin;

import me.github.minecraft269.clienttimeandweathercontrolmod.config.ConfigStorage;
import me.github.minecraft269.clienttimeandweathercontrolmod.weather.WeatherController;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.AmbientSoundHandler;
import net.minecraft.client.resources.sounds.BiomeAmbientSoundsHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BiomeAmbientSoundsHandler.class)
public class MixinWeatherSoundInstance {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onWeatherSoundTick(CallbackInfo ci) {
        if (ConfigStorage.isWeatherActive()) {
            WeatherController weatherController = WeatherController.getInstance();

            // 检查是否应该播放雨声
            if (!weatherController.shouldPlayRainSound() && !weatherController.shouldPlayThunderSound()) {
                ci.cancel();
            }
        }
    }
}