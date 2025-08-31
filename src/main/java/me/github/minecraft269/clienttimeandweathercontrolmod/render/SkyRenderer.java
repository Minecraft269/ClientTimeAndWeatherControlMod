package me.github.minecraft269.clienttimeandweathercontrolmod.render;

import me.github.minecraft269.clienttimeandweathercontrolmod.config.ConfigStorage;
import me.github.minecraft269.clienttimeandweathercontrolmod.time.TimeStorage;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "clienttimeandweathercontrolmod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SkyRenderer {

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        // 只处理天空渲染阶段
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) {
            return;
        }

        // 如果模组激活且是SkyOnly模式，尝试处理天空渲染
        if (ConfigStorage.isActive() &&
                ConfigStorage.getTimeType().getSerializedName().equals("sky_only")) {
            // 获取自定义的天空时间
            int customTime = TimeStorage.getInstance().getSkyRenderTime();

            // 如果返回有效时间，修改天空渲染
            if (customTime != -1) {
                // 这里可以添加自定义的天空渲染逻辑
                // 由于Minecraft的渲染系统复杂，这里可能需要更深入的修改
                // 暂时保留这个框架以备将来扩展
            }
        }
    }
}