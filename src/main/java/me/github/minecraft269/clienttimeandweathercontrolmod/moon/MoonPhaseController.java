package me.github.minecraft269.clienttimeandweathercontrolmod.moon;

import me.github.minecraft269.clienttimeandweathercontrolmod.config.ConfigStorage;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 月相控制器，用于控制游戏月相
 */
public class MoonPhaseController {
    private static final Logger LOGGER = LogManager.getLogger("ClientTimeAndWeatherControlMod/MoonPhaseController");
    private static final MoonPhaseController INSTANCE = new MoonPhaseController();

    private final Minecraft client;
    private boolean wasMoonActive = false;
    private long startMs = 0;

    public static MoonPhaseController getInstance() {
        return INSTANCE;
    }

    private MoonPhaseController() {
        this.client = Minecraft.getInstance();
    }

    /**
     * 获取当前月相
     */
    public int getMoonPhase() {
        int moonLoopSpeed = ConfigStorage.getMoonLoopSpeed();

        // 如果循环速度为0，使用固定月相
        if (moonLoopSpeed == 0) {
            return ConfigStorage.getMoonPhase().getPhaseId();
        }

        // 计算循环月相
        long elapsedMs = System.currentTimeMillis() - startMs;
        // 将循环速度转换为每相所需的毫秒数
        // 例如：循环速度1表示每秒变化1相，则每相需要1000毫秒
        double phaseTimeMs = 1000.0 / Math.abs(moonLoopSpeed);
        // 完整循环8相所需的时间
        double cycleTimeMs = phaseTimeMs * 8;

        if (cycleTimeMs <= 0) {
            return ConfigStorage.getMoonPhase().getPhaseId();
        }

        // 计算当前进度
        double progress = (elapsedMs % cycleTimeMs) / cycleTimeMs;
        // 计算当前月相 (0-7)
        int phase = (int) (progress * 8);

        // 确保月相在0-7范围内
        return phase % 8;
    }

    /**
     * 应用配置更改
     */
    public void updateChanges() {
        startMs = System.currentTimeMillis();
        LOGGER.info("MoonPhaseController updated changes, moon phase: " +
                ConfigStorage.getMoonPhase().getSerializedName());
        LOGGER.info("Moon Active: " + ConfigStorage.isMoonActive() +
                ", Moon Loop Speed: " + ConfigStorage.getMoonLoopSpeed());
    }
}