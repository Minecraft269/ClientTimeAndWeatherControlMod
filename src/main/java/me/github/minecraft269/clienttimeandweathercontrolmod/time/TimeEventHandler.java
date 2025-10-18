package me.github.minecraft269.clienttimeandweathercontrolmod.time;

import me.github.minecraft269.clienttimeandweathercontrolmod.config.ConfigStorage;
import me.github.minecraft269.clienttimeandweathercontrolmod.moon.MoonPhaseController;
import me.github.minecraft269.clienttimeandweathercontrolmod.weather.WeatherController;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = "clienttimeandweathercontrolmod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TimeEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("ClientTimeAndWeatherControlMod/TimeEventHandler");

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (ConfigStorage.isActive()) {
                TimeStorage.getInstance().update();
            }

            if (ConfigStorage.isWeatherActive()) {
                WeatherController.getInstance().update();
            }

            // 移除月相的更新调用，因为现在通过Mixin直接修改渲染
            // if (ConfigStorage.isMoonActive()) {
            //     MoonPhaseController.getInstance().update();
            // }
        }
    }
}