package me.github.minecraft269.clienttimeandweathercontrolmod.config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MoonPreviewEntry extends AbstractConfigListEntry<Void> {
    private final Button button;

    public MoonPreviewEntry() {
        super(Component.translatable("clienttimeandweathercontrolmod.config.moon.show_preview"), false);

        this.button = Button.builder(Component.translatable("clienttimeandweathercontrolmod.config.moon.show_preview"), btn -> {
            Minecraft.getInstance().setScreen(new MoonPreviewScreen(Minecraft.getInstance().screen));
        }).build();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
        this.button.setX(x);
        this.button.setY(y);
        this.button.setWidth(entryWidth);
        this.button.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return Collections.singletonList(button);
    }

    @Override
    public @NotNull List<? extends NarratableEntry> narratables() {
        return Collections.singletonList(button);
    }

    @Override
    public Void getValue() {
        return null;
    }

    @Override
    public Optional<Void> getDefaultValue() {
        return Optional.empty();
    }

    @Override
    public void save() {
        // 不需要保存任何内容
    }
}