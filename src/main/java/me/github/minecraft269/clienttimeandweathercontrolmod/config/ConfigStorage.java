package me.github.minecraft269.clienttimeandweathercontrolmod.config;

import me.github.minecraft269.clienttimeandweathercontrolmod.time.TimeStorage;
import me.github.minecraft269.clienttimeandweathercontrolmod.time.TimeType;
import me.github.minecraft269.clienttimeandweathercontrolmod.weather.WeatherType;

import static com.mojang.text2speech.Narrator.LOGGER;

public class ConfigStorage {

    // 时间配置方法
    public static boolean isActive() {
        return ClientTimeAndWeatherControlModConfig.get().time.active;
    }

    public static TimeType getTimeType() {
        return ClientTimeAndWeatherControlModConfig.get().time.timeType;
    }

    public static int getTime() {
        int time = ClientTimeAndWeatherControlModConfig.get().time.time;
        LOGGER.debug("ConfigStorage.getTime() returned: " + time);
        return time;
    }

    public static int getLoopSpeed() {
        return ClientTimeAndWeatherControlModConfig.get().time.loopSpeed;
    }

    public static int getLoopStart() {
        return ClientTimeAndWeatherControlModConfig.get().time.loopStart;
    }

    public static int getLoopEnd() {
        return ClientTimeAndWeatherControlModConfig.get().time.loopEnd;
    }

    // 天气配置方法
    public static boolean isWeatherActive() {
        return ClientTimeAndWeatherControlModConfig.get().weather.active;
    }

    public static WeatherType getWeatherType() {
        return ClientTimeAndWeatherControlModConfig.get().weather.weatherType;
    }

    public static void save() {
        ClientTimeAndWeatherControlModConfig.save();
        // 确保配置更改后立即更新时间
        TimeStorage.getInstance().updateChanges();
    }
}