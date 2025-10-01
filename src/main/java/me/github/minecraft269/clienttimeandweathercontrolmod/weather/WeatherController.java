package me.github.minecraft269.clienttimeandweathercontrolmod.weather;

import me.github.minecraft269.clienttimeandweathercontrolmod.config.ConfigStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 天气控制器，用于控制游戏天气
 */
public class WeatherController {
    private static final Logger LOGGER = LogManager.getLogger("ClientTimeAndWeatherControlMod/WeatherController");
    private static final WeatherController INSTANCE = new WeatherController();

    private final Minecraft client;
    private boolean wasWeatherActive = false;
    private WeatherType lastWeatherType = WeatherType.CLEAR;

    // 跟踪原版天气状态，用于恢复
    private boolean wasRaining = false;
    private boolean wasThundering = false;
    private float originalRainLevel = 0.0f;
    private float originalThunderLevel = 0.0f;

    public static WeatherController getInstance() {
        return INSTANCE;
    }

    private WeatherController() {
        this.client = Minecraft.getInstance();
    }

    /**
     * 更新天气
     */
    public void update() {
        boolean isWeatherActive = ConfigStorage.isWeatherActive();
        WeatherType currentWeatherType = ConfigStorage.getWeatherType();

        if (!isWeatherActive) {
            // 如果天气控制被禁用，恢复原版天气行为
            if (wasWeatherActive) {
                restoreVanillaWeather();
                wasWeatherActive = false;
                LOGGER.debug("Weather control disabled, restoring vanilla weather");
            }
            return;
        }

        // 如果天气控制刚刚启用，保存原版天气状态
        if (!wasWeatherActive) {
            saveVanillaWeatherState();
            wasWeatherActive = true;
            LOGGER.debug("Weather control enabled, saving vanilla weather state");
        }

        // 如果天气类型发生变化，更新天气
        if (lastWeatherType != currentWeatherType) {
            LOGGER.debug("Weather type changed from {} to {}", lastWeatherType, currentWeatherType);
            lastWeatherType = currentWeatherType;
        }

        // 强制设置天气
        setCustomWeather(currentWeatherType);
    }

    /**
     * 保存原版天气状态
     */
    private void saveVanillaWeatherState() {
        if (this.client.level != null) {
            Level level = this.client.level;
            this.wasRaining = level.isRaining();
            this.wasThundering = level.isThundering();
            this.originalRainLevel = level.getRainLevel(1.0f);
            this.originalThunderLevel = level.getThunderLevel(1.0f);

            LOGGER.debug("Saved vanilla weather state - Raining: {}, Thundering: {}, RainLevel: {}, ThunderLevel: {}",
                    wasRaining, wasThundering, originalRainLevel, originalThunderLevel);
        }
    }

    /**
     * 恢复原版天气
     */
    private void restoreVanillaWeather() {
        if (this.client.level != null) {
            Level level = this.client.level;

            // 恢复原版天气状态
            if (level.getLevelData() instanceof ServerLevelData) {
                ServerLevelData levelData = (ServerLevelData) level.getLevelData();
                levelData.setRaining(wasRaining);
                levelData.setThundering(wasThundering);
            }

            LOGGER.debug("Restored vanilla weather - Raining: {}, Thundering: {}", wasRaining, wasThundering);
        }
    }

    /**
     * 设置自定义天气
     */
    private void setCustomWeather(WeatherType weatherType) {
        if (this.client.level != null) {
            Level level = this.client.level;

            // 获取细粒度设置
            boolean overrideRainSound = ConfigStorage.isOverrideRainSound();
            boolean overrideThunderSound = ConfigStorage.isOverrideThunderSound();
            boolean overrideLightning = ConfigStorage.isOverrideLightning();
            boolean overrideRainParticles = ConfigStorage.isOverrideRainParticles();

            // 设置天气等级
            float targetRainLevel = overrideRainParticles ? weatherType.getRainLevel() : originalRainLevel;
            float targetThunderLevel = overrideLightning ? weatherType.getThunderLevel() : originalThunderLevel;

            // 立即设置天气等级，避免渐变效果
            level.setRainLevel(targetRainLevel);
            level.setThunderLevel(targetThunderLevel);

            // 使用ServerLevelData的方法设置天气状态
            if (level.getLevelData() instanceof ServerLevelData) {
                ServerLevelData levelData = (ServerLevelData) level.getLevelData();

                // 根据天气类型设置状态
                switch (weatherType) {
                    case CLEAR:
                        levelData.setRaining(false);
                        levelData.setThundering(false);
                        break;
                    case RAIN:
                        levelData.setRaining(true);
                        levelData.setThundering(false);
                        break;
                    case THUNDER:
                        levelData.setRaining(true);
                        levelData.setThundering(true);
                        break;
                }

                // 设置非常大的持续时间，防止原版天气更新
                if (weatherType != WeatherType.CLEAR) {
                    levelData.setRainTime(Integer.MAX_VALUE);
                    levelData.setThunderTime(Integer.MAX_VALUE);
                    levelData.setClearWeatherTime(0);
                } else {
                    levelData.setClearWeatherTime(Integer.MAX_VALUE);
                    levelData.setRainTime(0);
                    levelData.setThunderTime(0);
                }
            }

            // 调试信息
            if (System.currentTimeMillis() % 5000 < 50) { // 每5秒输出一次
                LOGGER.debug("Custom weather set - Type: {}, RainLevel: {}, ThunderLevel: {}",
                        weatherType, targetRainLevel, targetThunderLevel);
            }
        }
    }

    /**
     * 获取当前雨声是否应该播放
     */
    public boolean shouldPlayRainSound() {
        if (!ConfigStorage.isWeatherActive() || !ConfigStorage.isOverrideRainSound()) {
            return wasRaining; // 返回原版状态
        }
        return ConfigStorage.getWeatherType() != WeatherType.CLEAR;
    }

    /**
     * 获取当前雷声是否应该播放
     */
    public boolean shouldPlayThunderSound() {
        if (!ConfigStorage.isWeatherActive() || !ConfigStorage.isOverrideThunderSound()) {
            return wasThundering; // 返回原版状态
        }
        return ConfigStorage.getWeatherType() == WeatherType.THUNDER;
    }

    /**
     * 获取当前是否应该显示闪电
     */
    public boolean shouldShowLightning() {
        if (!ConfigStorage.isWeatherActive() || !ConfigStorage.isOverrideLightning()) {
            return wasThundering; // 返回原版状态
        }
        return ConfigStorage.getWeatherType() == WeatherType.THUNDER;
    }

    /**
     * 获取当前是否应该显示雨雪粒子
     */
    public boolean shouldShowRainParticles() {
        if (!ConfigStorage.isWeatherActive() || !ConfigStorage.isOverrideRainParticles()) {
            return wasRaining; // 返回原版状态
        }
        return ConfigStorage.getWeatherType() != WeatherType.CLEAR;
    }

    /**
     * 应用配置更改
     */
    public void updateChanges() {
        LOGGER.info("WeatherController updated changes, weather type: " +
                ConfigStorage.getWeatherType().getSerializedName());
        LOGGER.info("Weather Active: " + ConfigStorage.isWeatherActive());
        LOGGER.info("Rain Sound Override: " + ConfigStorage.isOverrideRainSound());
        LOGGER.info("Thunder Sound Override: " + ConfigStorage.isOverrideThunderSound());
        LOGGER.info("Lightning Override: " + ConfigStorage.isOverrideLightning());
        LOGGER.info("Rain Particles Override: " + ConfigStorage.isOverrideRainParticles());

        // 重置状态跟踪
        wasWeatherActive = false;
        saveVanillaWeatherState();

        // 立即更新天气
        update();
    }
}