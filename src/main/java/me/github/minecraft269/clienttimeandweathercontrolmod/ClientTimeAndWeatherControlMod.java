package me.github.minecraft269.clienttimeandweathercontrolmod;

import me.github.minecraft269.clienttimeandweathercontrolmod.config.ClientTimeAndWeatherControlModConfig;
import me.github.minecraft269.clienttimeandweathercontrolmod.time.TimeStorage;
import me.github.minecraft269.clienttimeandweathercontrolmod.config.ConfigScreen;
import me.github.minecraft269.clienttimeandweathercontrolmod.weather.WeatherController;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.github.minecraft269.clienttimeandweathercontrolmod.config.KeyBindings;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.IEventBus;

@Mod(ClientTimeAndWeatherControlMod.MOD_ID)
public class ClientTimeAndWeatherControlMod {
    private static final Logger LOGGER = LogManager.getLogger("ClientTimeAndWeatherControlMod");

    public static final String MOD_ID = "clienttimeandweathercontrolmod";

    public ClientTimeAndWeatherControlMod() {
        LOGGER.info("ClientTimeAndWeatherControlMod mod constructor called");

        // 获取Mod事件总线
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 注册客户端设置事件
        modEventBus.addListener(this::onClientSetup);

        // 注册按键映射事件
        modEventBus.addListener(this::onRegisterKeyMappings);

        ClientTimeAndWeatherControlModConfig.register();
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.info("Client setup event received");

        // 注册配置屏幕
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> {
                    return ConfigScreen.create(parent);
                })
        );

        // 确保在客户端设置完成后立即更新一次时间和天气
        event.enqueueWork(() -> {
            LOGGER.info("Enqueuing initial time and weather update");
            // 初始化时间存储
            TimeStorage.getInstance().updateChanges();
            // 初始化天气控制器
            WeatherController.getInstance().updateChanges();
        });
    }

    // 添加按键映射注册方法
    private void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.OPEN_CONFIG);
        LOGGER.info("Registered key binding for config menu");
    }
}