package me.github.minecraft269.clienttimeandweathercontrolmod.moon;

import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

/**
 * 月相枚举
 */
public enum MoonPhase implements StringRepresentable {
    FULL_MOON("full_moon", 0),
    WANING_GIBBOUS("waning_gibbous", 1),
    LAST_QUARTER("last_quarter", 2),
    WANING_CRESCENT("waning_crescent", 3),
    NEW_MOON("new_moon", 4),
    WAXING_CRESCENT("waxing_crescent", 5),
    FIRST_QUARTER("first_quarter", 6),
    WAXING_GIBBOUS("waxing_gibbous", 7);

    private final String configValue;
    private final int phaseId;

    MoonPhase(String configValue, int phaseId) {
        this.configValue = configValue;
        this.phaseId = phaseId;
    }

    public int getPhaseId() {
        return phaseId;
    }

    @Override
    public String getSerializedName() {
        return configValue;
    }

    public Component getDisplayName() {
        return Component.translatable("clienttimeandweathercontrolmod.config.moonphase." + configValue);
    }

    public static MoonPhase fromId(int id) {
        for (MoonPhase phase : values()) {
            if (phase.getPhaseId() == id) {
                return phase;
            }
        }
        return FULL_MOON;
    }

    public static MoonPhase fromString(String value) {
        for (MoonPhase phase : values()) {
            if (phase.getSerializedName().equalsIgnoreCase(value)) {
                return phase;
            }
        }
        return FULL_MOON;
    }
}