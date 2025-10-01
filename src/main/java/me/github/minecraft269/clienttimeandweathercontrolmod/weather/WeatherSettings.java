package me.github.minecraft269.clienttimeandweathercontrolmod.weather;

/**
 * 天气设置类
 */
public class WeatherSettings {
    private boolean active;
    private WeatherType weatherType;
    private boolean overrideRainSound;
    private boolean overrideThunderSound;
    private boolean overrideLightning;
    private boolean overrideRainParticles;

    public WeatherSettings() {
        this.active = false;
        this.weatherType = WeatherType.CLEAR;
        this.overrideRainSound = true;
        this.overrideThunderSound = true;
        this.overrideLightning = true;
        this.overrideRainParticles = true;
    }

    // Getters and Setters
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public WeatherType getWeatherType() { return weatherType; }
    public void setWeatherType(WeatherType weatherType) { this.weatherType = weatherType; }

    public boolean isOverrideRainSound() { return overrideRainSound; }
    public void setOverrideRainSound(boolean overrideRainSound) { this.overrideRainSound = overrideRainSound; }

    public boolean isOverrideThunderSound() { return overrideThunderSound; }
    public void setOverrideThunderSound(boolean overrideThunderSound) { this.overrideThunderSound = overrideThunderSound; }

    public boolean isOverrideLightning() { return overrideLightning; }
    public void setOverrideLightning(boolean overrideLightning) { this.overrideLightning = overrideLightning; }

    public boolean isOverrideRainParticles() { return overrideRainParticles; }
    public void setOverrideRainParticles(boolean overrideRainParticles) { this.overrideRainParticles = overrideRainParticles; }
}