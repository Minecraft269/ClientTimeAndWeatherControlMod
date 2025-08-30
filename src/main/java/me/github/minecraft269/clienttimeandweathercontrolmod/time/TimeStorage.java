package me.github.minecraft269.clienttimeandweathercontrolmod.time;

import me.github.minecraft269.clienttimeandweathercontrolmod.config.ConfigStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 用于跟踪客户端应设置的时间的实用程序类
 */
public class TimeStorage {

    private final static TimeStorage INSTANCE = new TimeStorage();
    private final Minecraft client;
    private ITimeSupplier supplier;
    private static final Logger LOGGER = LogManager.getLogger("ClientTimeAndWeatherControlMod/TimeStorage");

    // 跟踪最后一次设置的客户端时间
    private int lastSetTime = -1;
    private long lastUpdateTime = 0;
    private static final long UPDATE_INTERVAL = 100; // 每100ms更新一次

    // 添加标志位来防止递归调用
    private boolean isSyncing = false;

    // 添加计数器减少日志输出
    private int logCounter = 0;
    private static final int LOG_INTERVAL = 1000; // 每1000次更新记录一次日志

    public static TimeStorage getInstance() {
        return INSTANCE;
    }

    /**
     * 偏移毫秒数。用于在应用更改时重新启动循环。
     */
    private long startMs = 0;

    private TimeStorage() {
        this.client = Minecraft.getInstance();
    }

    /**
     * 应用配置中的更改并重置TimeType
     */
    public void updateChanges() {
        startMs = getMs();
        supplier = ConfigStorage.getTimeType().getSupplier().get();
        LOGGER.info("TimeStorage updated changes, supplier: " + supplier.getClass().getSimpleName());
        LOGGER.info("Active: " + ConfigStorage.isActive() + ", Time: " + ConfigStorage.getTime());

        // 重置跟踪变量
        lastSetTime = -1;
        lastUpdateTime = 0;

        // 强制立即同步时间
        forceTimeSync();
    }

    /**
     * 获取用于测量的当前毫秒数
     */
    public static long getMs() {
        return Util.getMillis();
    }

    /**
     * 更新时间。
     */
    public void update() {
        if (!ConfigStorage.isActive()) {
            return; // 如果模组未激活，不执行任何操作
        }

        // 对于SkyOnly模式，不修改实际游戏时间
        if (ConfigStorage.getTimeType().getSerializedName().equals("sky_only")) {
            return;
        }

        long currentTimeMs = getMs();
        if (currentTimeMs - lastUpdateTime < UPDATE_INTERVAL) {
            return; // 避免过于频繁的更新
        }
        lastUpdateTime = currentTimeMs;

        if (this.client.level != null) {
            int targetTime = getTime();
            long currentTime = this.client.level.getDayTime();

            // 只有当当前时间与目标时间不同时才设置
            if (currentTime != targetTime) {
                // 减少日志输出频率
                logCounter++;
                if (logCounter % LOG_INTERVAL == 0) {
                    LOGGER.info("Setting time from " + currentTime + " to: " + targetTime);
                }
                setTimeSafely(targetTime);
                lastSetTime = targetTime;
            }
        }
    }

    /**
     * 安全设置时间，避免递归调用
     */
    private void setTimeSafely(int time) {
        if (isSyncing) {
            return;
        }

        isSyncing = true;
        try {
            this.client.level.setDayTime(time);
        } finally {
            isSyncing = false;
        }
    }

    public int getTime() {
        if (supplier == null) {
            LOGGER.info("Supplier is null, initializing...");
            updateChanges();
        }
        int time = supplier.getTime(getMs() - startMs);
        // 确保时间在有效范围内 (0-24000)
        int result = time % 24000;
        if (result < 0) {
            result += 24000; // 确保结果为非负数
        }
        return result;
    }

    /**
     * 获取用于天空渲染的时间（SkyOnly模式专用）
     * 这个方法可以被其他类调用以获取自定义的天空时间
     */
    public int getSkyRenderTime() {
        if (ConfigStorage.isActive() &&
                ConfigStorage.getTimeType().getSerializedName().equals("sky_only")) {
            return getTime();
        }
        // 返回一个无效值，让调用者知道应该使用原版时间
        return -1;
    }

    /**
     * 强制同步时间，确保客户端时间与设置的时间一致
     */
    public void forceTimeSync() {
        if (this.client.level != null && ConfigStorage.isActive() && !isSyncing) {
            isSyncing = true;
            try {
                int targetTime = getTime();
                long currentTime = this.client.level.getDayTime();

                if (currentTime != targetTime) {
                    LOGGER.info("Force syncing time from " + currentTime + " to: " + targetTime);
                    setTimeSafely(targetTime);
                    lastSetTime = targetTime;
                }
            } finally {
                isSyncing = false;
            }
        }
    }
}