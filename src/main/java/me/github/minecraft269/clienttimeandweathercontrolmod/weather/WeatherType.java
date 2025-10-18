package me.github.minecraft269.clienttimeandweathercontrolmod.weather;

import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

/**
 * 天气类型枚举
 */
public enum WeatherType implements StringRepresentable {
    CLEAR("clear"),
    RAIN("rain"),
    THUNDER("thunder");

    private final String configValue;

    WeatherType(String configValue) {
        this.configValue = configValue;
    }

    @Override
    public String getSerializedName() {
        return configValue;
    }

    public Component getDisplayName() {
        return Component.translatable("clienttimeandweathercontrolmod.config.weathertype." + configValue);
    }

    public static WeatherType fromString(String value) {
        for (WeatherType type : values()) {
            if (type.getSerializedName().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return CLEAR;
    }

    // 获取对应的雨雪等级
    public float getRainLevel() {
        return this == CLEAR ? 0.0f : 1.0f;
    }

    // 获取对应的雷暴等级
    public float getThunderLevel() {
        return this == THUNDER ? 1.0f : 0.0f;
    }
}