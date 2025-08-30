package me.github.minecraft269.clienttimeandweathercontrolmod.time;

import me.github.minecraft269.clienttimeandweathercontrolmod.config.SaveableConfig;

import java.util.List;

/**
 * 提供时间和修改时间的配置值的接口
 */
public interface ITimeSupplier {

    /**
     * 获取时间（以Tick为单位）。此值应在0-24000之间，但模组操作会将其带入该范围。
     * 每tick调用多次，因此依赖毫秒来计算持续时间可以实现平滑动画。
     * @param ms 自上次重置以来的毫秒数
     * @return 时间（以滴答为单位）
     */
    int getTime(long ms);

    /**
     * 获取配置供应器所需的选项。用于配置菜单。
     * 此列表可以为空且不会有任何问题。
     * @return IConfigBase列表
     */
    default List<SaveableConfig<?>> getOptions() {
        return java.util.Collections.emptyList();
    }
}