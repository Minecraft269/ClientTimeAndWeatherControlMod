package me.github.minecraft269.clienttimeandweathercontrolmod.config;

import me.github.minecraft269.clienttimeandweathercontrolmod.moon.MoonPhase;
import me.github.minecraft269.clienttimeandweathercontrolmod.weather.WeatherType;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.github.minecraft269.clienttimeandweathercontrolmod.time.TimeType;

@Config(name = "clienttimeandweathercontrolmod")
public class ClientTimeAndWeatherControlModConfig implements ConfigData {

    @ConfigEntry.Category("time")
    @ConfigEntry.Gui.TransitiveObject
    public TimeSettings time = new TimeSettings();

    @ConfigEntry.Category("weather")
    @ConfigEntry.Gui.TransitiveObject
    public WeatherSettings weather = new WeatherSettings();

    @ConfigEntry.Category("moon")
    @ConfigEntry.Gui.TransitiveObject
    public MoonSettings moon = new MoonSettings();

    public static class TimeSettings {
        public boolean active = false;

        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public TimeType timeType = TimeType.STATIC;

        public int time = 18000;
        public int loopSpeed = 0;
        public int loopStart = 0;
        public int loopEnd = 24000;
    }

    public static class WeatherSettings {
        public boolean active = false;

        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public WeatherType weatherType = WeatherType.CLEAR;
    }

    public static void register() {
        AutoConfig.register(ClientTimeAndWeatherControlModConfig.class, GsonConfigSerializer::new);
    }

    public static ClientTimeAndWeatherControlModConfig get() {
        return AutoConfig.getConfigHolder(ClientTimeAndWeatherControlModConfig.class).getConfig();
    }

    public static void save() {
        AutoConfig.getConfigHolder(ClientTimeAndWeatherControlModConfig.class).save();
    }

    public static class MoonSettings {
        public boolean active = false;

        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public MoonPhase moonPhase = MoonPhase.FULL_MOON;

        public int moonLoopSpeed = 0;
    }

}