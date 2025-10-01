package me.github.minecraft269.clienttimeandweathercontrolmod.time.timetypes;

import me.github.minecraft269.clienttimeandweathercontrolmod.config.ConfigStorage;
import me.github.minecraft269.clienttimeandweathercontrolmod.time.ITimeSupplier;
import me.github.minecraft269.clienttimeandweathercontrolmod.config.SaveableConfig;

import java.util.Arrays;
import java.util.List;

/**
 * 时间供应器，将从起点循环到终点，然后立即跳回起点。
 */
public class LoopSkipTimeSupplier implements ITimeSupplier {

    @Override
    public int getTime(long ms) {
        int loopSpeed = ConfigStorage.getLoopSpeed();
        int loopStart = ConfigStorage.getLoopStart();

        // 如果循环速度为0，直接返回起始时间
        if (loopSpeed == 0) {
            return loopStart;
        }

        int loopEnd = ConfigStorage.getLoopEnd();

        // 如果起始和结束时间相同，直接返回该时间
        if (loopStart == loopEnd) {
            return loopStart;
        }

        // 确保结束时间大于起始时间
        if (loopEnd < loopStart) {
            int temp = loopStart;
            loopStart = loopEnd;
            loopEnd = temp;
        }

        int loopDif = loopEnd - loopStart;

        // 计算循环周期（毫秒）
        // 循环速度：每秒多少tick
        // 整个循环需要的时间（毫秒）= (loopDif / |loopSpeed|) * 1000
        double cycleTimeMs = (double) loopDif / Math.abs(loopSpeed) * 1000.0;

        // 避免除以零错误
        if (cycleTimeMs <= 0) {
            return loopStart;
        }

        // 计算当前在循环中的位置
        double progress = (ms % cycleTimeMs) / cycleTimeMs; // 0.0 到 1.0
        int positionInCycle = (int) (progress * loopDif);

        return loopStart + positionInCycle;
    }

    @Override
    public List<SaveableConfig<?>> getOptions() {
        return Arrays.asList(
                new SaveableConfig<>("loopSpeed", null),
                new SaveableConfig<>("loopStart", null),
                new SaveableConfig<>("loopEnd", null)
        );
    }
}