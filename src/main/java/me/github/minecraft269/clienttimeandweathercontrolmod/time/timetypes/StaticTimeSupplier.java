package me.github.minecraft269.clienttimeandweathercontrolmod.time.timetypes;

import me.github.minecraft269.clienttimeandweathercontrolmod.config.ConfigStorage;
import me.github.minecraft269.clienttimeandweathercontrolmod.time.ITimeSupplier;
import me.github.minecraft269.clienttimeandweathercontrolmod.config.SaveableConfig;

import java.util.Arrays;
import java.util.List;

/**
 * 静态时间供应器，只返回一个时间。
 */
public class StaticTimeSupplier implements ITimeSupplier {

    @Override
    public int getTime(long ms) {
        return ConfigStorage.getTime();
    }

    @Override
    public List<SaveableConfig<?>> getOptions() {
        return Arrays.asList(
                new SaveableConfig<>("time", ConfigStorage.getTime())
        );
    }
}