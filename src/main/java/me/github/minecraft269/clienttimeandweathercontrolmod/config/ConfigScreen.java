package me.github.minecraft269.clienttimeandweathercontrolmod.config;

import me.github.minecraft269.clienttimeandweathercontrolmod.time.TimeStorage;
import me.github.minecraft269.clienttimeandweathercontrolmod.weather.WeatherType;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import me.github.minecraft269.clienttimeandweathercontrolmod.time.TimeType;
import java.util.function.Supplier;

public class ConfigScreen {

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("clienttimeandweathercontrolmod.screen.main"))
                .setSavingRunnable(ConfigStorage::save);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // 时间配置分类
        ConfigCategory timeCategory = builder.getOrCreateCategory(Component.translatable("category.clienttimeandweathercontrolmod.time"));
        addTimeOptions(entryBuilder, timeCategory);

        // 天气配置分类
        ConfigCategory weatherCategory = builder.getOrCreateCategory(Component.translatable("category.clienttimeandweathercontrolmod.weather"));
        addWeatherOptions(entryBuilder, weatherCategory);

        return builder.build();
    }

    /**
     * 添加时间配置选项
     */
    private static void addTimeOptions(ConfigEntryBuilder entryBuilder, ConfigCategory category) {
        // 创建条件判断Supplier
        Supplier<Boolean> isStaticOrSkyOnly = () ->
                ClientTimeAndWeatherControlModConfig.get().time.timeType == TimeType.STATIC ||
                        ClientTimeAndWeatherControlModConfig.get().time.timeType == TimeType.SKY_ONLY;

        Supplier<Boolean> isLoopMode = () ->
                ClientTimeAndWeatherControlModConfig.get().time.timeType == TimeType.LOOP_SKIP ||
                        ClientTimeAndWeatherControlModConfig.get().time.timeType == TimeType.LOOP_REVERSE;

        // 时间激活选项
        category.addEntry(entryBuilder.startBooleanToggle(Component.translatable("clienttimeandweathercontrolmod.config.time.active"), ClientTimeAndWeatherControlModConfig.get().time.active)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("clienttimeandweathercontrolmod.config.time.info.active"))
                .setSaveConsumer(newValue -> ClientTimeAndWeatherControlModConfig.get().time.active = newValue)
                .build());

        // 时间类型下拉菜单
        category.addEntry(entryBuilder.startEnumSelector(Component.translatable("clienttimeandweathercontrolmod.config.time.timetype"), TimeType.class, ClientTimeAndWeatherControlModConfig.get().time.timeType)
                .setDefaultValue(TimeType.STATIC)
                .setTooltip(Component.translatable("clienttimeandweathercontrolmod.config.time.info.timetype"))
                .setEnumNameProvider(value -> ((TimeType) value).getDisplayName())
                .setSaveConsumer(newValue -> {
                    ClientTimeAndWeatherControlModConfig.get().time.timeType = (TimeType) newValue;
                    // 保存配置
                    ClientTimeAndWeatherControlModConfig.save();
                    // 更新时间存储
                    TimeStorage.getInstance().updateChanges();
                })
                .build());

        // 添加说明文本
        category.addEntry(entryBuilder.startTextDescription(Component.translatable("clienttimeandweathercontrolmod.config.info.options.alwaysvisible"))
                .build());

        // 时间选项 - 始终显示，但在循环模式下不保存
        category.addEntry(entryBuilder.startIntField(Component.translatable("clienttimeandweathercontrolmod.config.time.time"), ClientTimeAndWeatherControlModConfig.get().time.time)
                .setDefaultValue(18000)
                .setMin(0)
                .setMax(24000)
                .setTooltip(Component.translatable("clienttimeandweathercontrolmod.config.time.info.time"))
                .setSaveConsumer(newValue -> {
                    if (isStaticOrSkyOnly.get()) {
                        ClientTimeAndWeatherControlModConfig.get().time.time = newValue;
                    }
                })
                .build());

        // 循环速度选项 - 始终显示，但在静态/天空模式下不保存
        category.addEntry(entryBuilder.startIntField(Component.translatable("clienttimeandweathercontrolmod.config.time.loopspeed"), ClientTimeAndWeatherControlModConfig.get().time.loopSpeed)
                .setDefaultValue(0)
                .setMin(-1000)
                .setMax(1000)
                .setTooltip(Component.translatable("clienttimeandweathercontrolmod.config.time.info.loopspeed"))
                .setSaveConsumer(newValue -> {
                    if (isLoopMode.get()) {
                        ClientTimeAndWeatherControlModConfig.get().time.loopSpeed = newValue;
                    }
                })
                .build());

        // 循环开始时间选项 - 始终显示，但在静态/天空模式下不保存
        category.addEntry(entryBuilder.startIntField(Component.translatable("clienttimeandweathercontrolmod.config.time.loopstart"), ClientTimeAndWeatherControlModConfig.get().time.loopStart)
                .setDefaultValue(0)
                .setMin(0)
                .setMax(24000)
                .setTooltip(Component.translatable("clienttimeandweathercontrolmod.config.time.info.loopstart"))
                .setSaveConsumer(newValue -> {
                    if (isLoopMode.get()) {
                        ClientTimeAndWeatherControlModConfig.get().time.loopStart = newValue;
                    }
                })
                .build());

        // 循环结束时间选项 - 始终显示，但在静态/天空模式下不保存
        category.addEntry(entryBuilder.startIntField(Component.translatable("clienttimeandweathercontrolmod.config.time.loopend"), ClientTimeAndWeatherControlModConfig.get().time.loopEnd)
                .setDefaultValue(24000)
                .setMin(0)
                .setMax(24000)
                .setTooltip(Component.translatable("clienttimeandweathercontrolmod.config.time.info.loopend"))
                .setSaveConsumer(newValue -> {
                    if (isLoopMode.get()) {
                        ClientTimeAndWeatherControlModConfig.get().time.loopEnd = newValue;
                    }
                })
                .build());
    }

    /**
     * 添加天气配置选项
     */
    private static void addWeatherOptions(ConfigEntryBuilder entryBuilder, ConfigCategory category) {
        // 天气激活选项
        category.addEntry(entryBuilder.startBooleanToggle(Component.translatable("clienttimeandweathercontrolmod.config.weather.active"), ClientTimeAndWeatherControlModConfig.get().weather.active)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("clienttimeandweathercontrolmod.config.weather.info.active"))
                .setSaveConsumer(newValue -> ClientTimeAndWeatherControlModConfig.get().weather.active = newValue)
                .build());

        // 天气类型下拉菜单
        category.addEntry(entryBuilder.startEnumSelector(Component.translatable("clienttimeandweathercontrolmod.config.weather.weathertype"), WeatherType.class, ClientTimeAndWeatherControlModConfig.get().weather.weatherType)
                .setDefaultValue(WeatherType.CLEAR)
                .setTooltip(Component.translatable("clienttimeandweathercontrolmod.config.weather.info.weathertype"))
                .setEnumNameProvider(value -> ((WeatherType) value).getDisplayName())
                .setSaveConsumer(newValue -> {
                    ClientTimeAndWeatherControlModConfig.get().weather.weatherType = (WeatherType) newValue;
                    ClientTimeAndWeatherControlModConfig.save();
                })
                .build());
    }
}