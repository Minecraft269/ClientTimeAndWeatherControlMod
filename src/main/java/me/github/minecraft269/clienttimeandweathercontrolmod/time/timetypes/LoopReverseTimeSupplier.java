package me.github.minecraft269.clienttimeandweathercontrolmod.time.timetypes;

import me.github.minecraft269.clienttimeandweathercontrolmod.config.ConfigStorage;
import me.github.minecraft269.clienttimeandweathercontrolmod.time.ITimeSupplier;
import me.github.minecraft269.clienttimeandweathercontrolmod.config.SaveableConfig;

import java.util.Arrays;
import java.util.List;

/**
 * 时间供应器，将从起点到终点，然后向后返回到起点。
 */
public class LoopReverseTimeSupplier implements ITimeSupplier {

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

        // 计算完整的循环周期（往返，毫秒）
        // 循环速度：每秒多少tick
        // 整个往返循环需要的时间（毫秒）= (loopDif * 2 / |loopSpeed|) * 1000
        double cycleTimeMs = (double) (loopDif * 2) / Math.abs(loopSpeed) * 1000.0;

        // 避免除以零错误
        if (cycleTimeMs <= 0) {
            return loopStart;
        }

        // 计算当前在循环中的位置
        double progress = (ms % cycleTimeMs) / cycleTimeMs; // 0.0 到 1.0
        double positionInFullCycle = progress * loopDif * 2;

        // 计算实际时间
        int actualTime;
        if (positionInFullCycle < loopDif) {
            // 正向阶段：从开始到结束
            actualTime = loopStart + (int) positionInFullCycle;
        } else {
            // 反向阶段：从结束返回到开始
            actualTime = loopEnd - (int) (positionInFullCycle - loopDif);
        }

        // 确保时间在有效范围内
        if (actualTime < 0) {
            actualTime = 0;
        } else if (actualTime > 24000) {
            actualTime = 24000;
        }

        return actualTime;
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