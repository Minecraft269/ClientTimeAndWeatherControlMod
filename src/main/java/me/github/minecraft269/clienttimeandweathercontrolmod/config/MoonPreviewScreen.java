package me.github.minecraft269.clienttimeandweathercontrolmod.config;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.Util;
import java.util.ArrayList;
import java.util.List;

public class MoonPreviewScreen extends Screen {
    private static final ResourceLocation MOON_PHASES = new ResourceLocation("textures/environment/moon_phases.png");
    private final Screen parent;
    private List<MoonPhaseText> moonPhaseTexts = new ArrayList<>();
    private int cachedWidth = 0;
    private int cachedHeight = 0;
    private boolean firstRender = true;
    private boolean backgroundRendered = false;

    // 调整月相之间的间距
    private static final int HORIZONTAL_SPACING = 60; // 水平间距
    private static final int VERTICAL_SPACING = 90;   // 垂直间距
    private static final int MOON_SIZE = 32;          // 月相图像大小
    private static final int TEXT_Y_OFFSET = 38;      // 文本垂直偏移量，从45减少到38，使文字更靠近图像

    public MoonPreviewScreen(Screen parent) {
        super(Component.translatable("clienttimeandweathercontrolmod.screen.moon_preview"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        // 添加返回按钮
        this.addRenderableWidget(Button.builder(Component.translatable("gui.back"), button -> {
            this.minecraft.setScreen(parent);
        }).bounds(this.width / 2 - 100, this.height - 40, 200, 20).build());

        // 预先计算月相文本位置和分割
        precalculateMoonPhaseTexts();
    }

    /**
     * 预先计算月相文本位置和分割
     */
    private void precalculateMoonPhaseTexts() {
        moonPhaseTexts.clear();

        // 使用翻译键获取月相名称
        String[] phaseKeys = {
                "clienttimeandweathercontrolmod.config.moonphase.full_moon",
                "clienttimeandweathercontrolmod.config.moonphase.waning_gibbous",
                "clienttimeandweathercontrolmod.config.moonphase.last_quarter",
                "clienttimeandweathercontrolmod.config.moonphase.waning_crescent",
                "clienttimeandweathercontrolmod.config.moonphase.new_moon",
                "clienttimeandweathercontrolmod.config.moonphase.waxing_crescent",
                "clienttimeandweathercontrolmod.config.moonphase.first_quarter",
                "clienttimeandweathercontrolmod.config.moonphase.waxing_gibbous"
        };

        // 计算月相位置 - 整体上移
        int startX = this.width / 2 - (HORIZONTAL_SPACING * 2);
        int startY = this.height / 2 - (VERTICAL_SPACING / 2) - 20; // 整体上移20像素

        for (int i = 0; i < 8; i++) {
            int phaseX = startX + (i % 4) * HORIZONTAL_SPACING;
            int phaseY = startY + (i / 4) * VERTICAL_SPACING;

            int nameX = phaseX + (HORIZONTAL_SPACING / 2);
            int nameY = phaseY + MOON_SIZE + 5; // 减少文本与月相图像的间距

            // 使用翻译文本
            Component phaseName = Component.translatable(phaseKeys[i]);
            String nameStr = phaseName.getString();

            // 创建月相文本对象
            MoonPhaseText text = new MoonPhaseText();
            text.phaseX = phaseX;
            text.phaseY = phaseY;
            text.nameX = nameX;
            text.nameY = nameY;

            // 处理长名称 - 分割成两行
            if (nameStr.length() > 10) {
                // 尝试在空格处分割
                int spaceIndex = nameStr.indexOf(' ');
                if (spaceIndex > 0) {
                    text.line1 = nameStr.substring(0, spaceIndex);
                    text.line2 = nameStr.substring(spaceIndex + 1);
                } else {
                    // 如果没有空格，尝试在中间分割
                    int mid = nameStr.length() / 2;
                    // 寻找最佳分割点
                    int bestSplit = mid;
                    for (int j = mid - 2; j <= mid + 2; j++) {
                        if (j > 0 && j < nameStr.length()) {
                            if (nameStr.charAt(j) == ' ') {
                                bestSplit = j;
                                break;
                            }
                        }
                    }
                    text.line1 = nameStr.substring(0, bestSplit);
                    text.line2 = nameStr.substring(bestSplit);
                }
            } else {
                text.line1 = nameStr;
                text.line2 = null;
            }

            moonPhaseTexts.add(text);
        }

        // 缓存当前尺寸
        cachedWidth = this.width;
        cachedHeight = this.height;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // 首次渲染或屏幕尺寸改变时重新计算
        if (firstRender || cachedWidth != this.width || cachedHeight != this.height) {
            precalculateMoonPhaseTexts();
            firstRender = false;
            backgroundRendered = false;
        }

        // 只在需要时渲染背景
        if (!backgroundRendered) {
            this.renderBackground(guiGraphics);
            backgroundRendered = true;
        }

        // 渲染静态内容（月相、文本、标题）
        renderStaticContent(guiGraphics);

        // 渲染交互元素（按钮）
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    /**
     * 渲染静态内容（月相、文本、标题）
     */
    private void renderStaticContent(GuiGraphics guiGraphics) {
        // 绘制标题
        Component titleComponent = Component.translatable("clienttimeandweathercontrolmod.screen.moon_preview");
        int titleWidth = this.font.width(titleComponent);
        guiGraphics.drawString(this.font, titleComponent, (this.width - titleWidth) / 2, 20, 0xFFFFFF, false);

        // 绘制月相预览和名称
        for (MoonPhaseText text : moonPhaseTexts) {
            // 绘制月相纹理
            int textureX = ((text.phaseX - (this.width / 2 - (HORIZONTAL_SPACING * 2))) / HORIZONTAL_SPACING % 4) * 32;
            int textureY = ((text.phaseY - (this.height / 2 - (VERTICAL_SPACING / 2) - 20)) / VERTICAL_SPACING) * 32;

            guiGraphics.blit(
                    MOON_PHASES,
                    text.phaseX, text.phaseY,
                    0,
                    textureX, textureY,
                    MOON_SIZE, MOON_SIZE,
                    128, 64
            );

            // 绘制月相名称
            if (text.line2 != null) {
                int line1Width = this.font.width(text.line1);
                int line2Width = this.font.width(text.line2);

                // 确保文本不会超出月相边界
                int maxWidth = HORIZONTAL_SPACING - 10;
                if (line1Width > maxWidth) {
                    text.line1 = shortenText(text.line1, maxWidth);
                    line1Width = this.font.width(text.line1);
                }
                if (line2Width > maxWidth) {
                    text.line2 = shortenText(text.line2, maxWidth);
                    line2Width = this.font.width(text.line2);
                }

                guiGraphics.drawString(this.font, text.line1, text.nameX - line1Width / 2, text.nameY, 0xFFFFFF, false);
                guiGraphics.drawString(this.font, text.line2, text.nameX - line2Width / 2, text.nameY + 10, 0xFFFFFF, false);
            } else {
                int lineWidth = this.font.width(text.line1);

                // 确保文本不会超出月相边界
                int maxWidth = HORIZONTAL_SPACING - 10;
                if (lineWidth > maxWidth) {
                    text.line1 = shortenText(text.line1, maxWidth);
                    lineWidth = this.font.width(text.line1);
                }

                guiGraphics.drawString(this.font, text.line1, text.nameX - lineWidth / 2, text.nameY, 0xFFFFFF, false);
            }
        }
    }

    /**
     * 缩短文本以适应可用空间
     */
    private String shortenText(String text, int maxWidth) {
        if (this.font.width(text) <= maxWidth) {
            return text;
        }

        // 尝试添加省略号
        String shortened = text;
        while (this.font.width(shortened + "...") > maxWidth && shortened.length() > 3) {
            shortened = shortened.substring(0, shortened.length() - 1);
        }

        return shortened + "...";
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        // 每tick检查是否需要重新渲染背景
        if (cachedWidth != this.width || cachedHeight != this.height) {
            backgroundRendered = false;
        }
    }

    /**
     * 月相文本信息类
     */
    private static class MoonPhaseText {
        public int phaseX;     // 月相图像的X坐标
        public int phaseY;     // 月相图像的Y坐标
        public int nameX;      // 名称的X坐标
        public int nameY;      // 名称的Y坐标
        public String line1;   // 第一行文本
        public String line2;   // 第二行文本（如果有）
    }
}