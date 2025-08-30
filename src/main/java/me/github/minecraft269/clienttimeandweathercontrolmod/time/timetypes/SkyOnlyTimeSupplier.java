package me.github.minecraft269.clienttimeandweathercontrolmod.time.timetypes;

import me.github.minecraft269.clienttimeandweathercontrolmod.config.ConfigStorage;
import me.github.minecraft269.clienttimeandweathercontrolmod.time.ITimeSupplier;
import me.github.minecraft269.clienttimeandweathercontrolmod.config.SaveableConfig;

import java.util.Arrays;
import java.util.List;

import static com.mojang.text2speech.Narrator.LOGGER;

/**
 * 混合时间供应器，只影响天空渲染，不影响实际游戏时间
 */
public class SkyOnlyTimeSupplier implements ITimeSupplier {

    @Override
    public int getTime(long ms) {
        int time = ConfigStorage.getTime();
        LOGGER.debug("SkyOnlyTimeSupplier.getTime() returned: " + time);
        return time;
    }

    @Override
    public List<SaveableConfig<?>> getOptions() {
        return Arrays.asList(
                new SaveableConfig<>("time", ConfigStorage.getTime())
        );
    }
}