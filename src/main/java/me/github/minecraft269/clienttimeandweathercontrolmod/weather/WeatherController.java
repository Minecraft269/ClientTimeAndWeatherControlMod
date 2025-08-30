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
        if (!ConfigStorage.isWeatherActive()) {
            // 如果天气控制被禁用，恢复原版天气行为
            if (wasWeatherActive) {
                LOGGER.info("Weather control disabled, restoring vanilla weather behavior");
                wasWeatherActive = false;
            }
            return;
        }

        wasWeatherActive = true;

        if (this.client.level != null) {
            Level level = this.client.level;
            WeatherType weatherType = ConfigStorage.getWeatherType();

            switch (weatherType) {
                case CLEAR:
                    setClearWeather(level);
                    break;
                case RAIN:
                    setRainWeather(level);
                    break;
                case THUNDER:
                    setThunderWeather(level);
                    break;
            }
        }
    }

    /**
     * 设置晴天
     */
    private void setClearWeather(Level level) {
        level.setRainLevel(0.0F);
        level.setThunderLevel(0.0F);

        // 使用ServerLevelData的方法设置天气状态
        if (level.getLevelData() instanceof ServerLevelData) {
            ServerLevelData levelData = (ServerLevelData) level.getLevelData();
            levelData.setRaining(false);
            levelData.setThundering(false);

            // 设置非常大的持续时间，使天气一直持续
            levelData.setClearWeatherTime(Integer.MAX_VALUE);
            levelData.setRainTime(0);
            levelData.setThunderTime(0);
        }
    }

    /**
     * 设置雨天/雪天
     */
    private void setRainWeather(Level level) {
        level.setRainLevel(1.0F);
        level.setThunderLevel(0.0F);

        // 使用ServerLevelData的方法设置天气状态
        if (level.getLevelData() instanceof ServerLevelData) {
            ServerLevelData levelData = (ServerLevelData) level.getLevelData();
            levelData.setRaining(true);
            levelData.setThundering(false);

            // 设置非常大的持续时间，使天气一直持续
            levelData.setRainTime(Integer.MAX_VALUE);
            levelData.setClearWeatherTime(0);
            levelData.setThunderTime(0);
        }
    }

    /**
     * 设置雷暴/暴雪
     */
    private void setThunderWeather(Level level) {
        level.setRainLevel(1.0F);
        level.setThunderLevel(1.0F);

        // 使用ServerLevelData的方法设置天气状态
        if (level.getLevelData() instanceof ServerLevelData) {
            ServerLevelData levelData = (ServerLevelData) level.getLevelData();
            levelData.setRaining(true);
            levelData.setThundering(true);

            // 设置非常大的持续时间，使天气一直持续
            levelData.setThunderTime(Integer.MAX_VALUE);
            levelData.setClearWeatherTime(0);
            levelData.setRainTime(0);
        }
    }

    /**
     * 应用配置更改
     */
    public void updateChanges() {
        LOGGER.info("WeatherController updated changes, weather type: " +
                ConfigStorage.getWeatherType().getSerializedName());
        LOGGER.info("Weather Active: " + ConfigStorage.isWeatherActive());

        // 立即更新天气
        update();
    }
}