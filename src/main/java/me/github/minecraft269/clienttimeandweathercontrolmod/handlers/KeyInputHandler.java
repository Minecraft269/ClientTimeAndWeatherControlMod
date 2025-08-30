package me.github.minecraft269.clienttimeandweathercontrolmod.handlers;

import me.github.minecraft269.clienttimeandweathercontrolmod.config.ConfigScreen;
import me.github.minecraft269.clienttimeandweathercontrolmod.config.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "clienttimeandweathercontrolmod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KeyInputHandler {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (KeyBindings.OPEN_CONFIG.consumeClick()) {
            Minecraft.getInstance().setScreen(ConfigScreen.create(Minecraft.getInstance().screen));
        }
    }
}