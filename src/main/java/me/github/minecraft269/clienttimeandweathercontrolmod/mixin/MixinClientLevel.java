package me.github.minecraft269.clienttimeandweathercontrolmod.mixin;

import me.github.minecraft269.clienttimeandweathercontrolmod.config.ConfigStorage;
import me.github.minecraft269.clienttimeandweathercontrolmod.time.TimeStorage;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mixin(ClientLevel.class)
public class MixinClientLevel {
    private static final Logger LOGGER = LogManager.getLogger("ClientTimeAndWeatherControlMod/MixinClientLevel");

    @Inject(method = "setDayTime", at = @At("HEAD"), cancellable = true)
    private void onSetDayTime(long time, CallbackInfo ci) {
        // 如果模组激活，取消所有服务器发送的时间更新
        if (ConfigStorage.isActive()) {
            // 获取模组设置的目标时间
            int targetTime = TimeStorage.getInstance().getTime();

            // 只有当服务器时间与模组设置的时间不同时才取消
            if (time != targetTime) {
                LOGGER.debug("Cancelling server time update: " + time + " (target: " + targetTime + ")");
                ci.cancel();
            }
        }
    }
}