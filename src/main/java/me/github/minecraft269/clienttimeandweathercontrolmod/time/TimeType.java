package me.github.minecraft269.clienttimeandweathercontrolmod.time;

import me.github.minecraft269.clienttimeandweathercontrolmod.time.timetypes.LoopReverseTimeSupplier;
import me.github.minecraft269.clienttimeandweathercontrolmod.time.timetypes.LoopSkipTimeSupplier;
import me.github.minecraft269.clienttimeandweathercontrolmod.time.timetypes.SkyOnlyTimeSupplier;
import me.github.minecraft269.clienttimeandweathercontrolmod.time.timetypes.StaticTimeSupplier;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * 不同类型时间供应器的持有者类。
 */
public enum TimeType implements StringRepresentable {
    STATIC("static", StaticTimeSupplier::new),
    LOOP_SKIP("loop_skip", LoopSkipTimeSupplier::new),
    LOOP_REVERSE("loop_reverse", LoopReverseTimeSupplier::new),
    SKY_ONLY("sky_only", SkyOnlyTimeSupplier::new); // 新增混合模式

    private final String configValue;
    @Getter
    private final Supplier<ITimeSupplier> supplier;

    TimeType(String configValue, Supplier<ITimeSupplier> supplier) {
        this.configValue = configValue;
        this.supplier = supplier;
    }

    public List<String> getOptions() {
        return Arrays.asList("time", "loopSpeed", "loopStart", "loopEnd");
    }

    @Override
    public String getSerializedName() {
        return configValue;
    }

    public Component getDisplayName() {
        return Component.translatable("clienttimeandweathercontrolmod.config.timetype." + configValue);
    }

    public static TimeType fromString(String value) {
        for (TimeType type : values()) {
            if (type.getSerializedName().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return STATIC;
    }
}