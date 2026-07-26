package bklmc.ocelotsign.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.List;

/**
 * 颜色选择器面板的 UI 渲染与交互处理
 *
 * @see ColorPickerState
 * @see PatternAndFontOverlay
 */
public final class ColorPickerRenderer {

    private ColorPickerRenderer() {
    }

    /**
     * 渲染颜色选择器面板。
     *
     * @param context 绘制上下文
     * @param textRenderer 文本渲染器
     * @param mainWidth 主区域宽度
     * @param contentStartY 内容起始 Y 坐标
     * @param scrollWindowStartY 滚动窗口起始 Y 坐标
     * @param scrollWindowEndY 滚动窗口结束 Y 坐标
     */
    public static void render(GuiGraphicsExtractor context, Font textRenderer,
                            int mainWidth, int contentStartY, int scrollWindowStartY, int scrollWindowEndY) {
        int centerX = UIConstants.SIDEBAR_WIDTH + mainWidth / 2;
        int y = contentStartY;

        // 顶部说明
        int introMaxWidth = mainWidth - 60;
        if (introMaxWidth < 60) introMaxWidth = 60;
        Component intro1 = Component.translatable("ocelotsignmod.gui.color_picker.intro1");
        Component intro2 = Component.translatable("ocelotsignmod.gui.color_picker.intro2");
        int intro1H = drawCenteredWrappedText(context, textRenderer, intro1, centerX, y, introMaxWidth);
        y += intro1H + 4;
        int intro2H = drawCenteredWrappedText(context, textRenderer, intro2, centerX, y, introMaxWidth);
        y += intro2H + 4;

        // HSV 主色板区
        int svSize = ColorPickerState.SV_VISUAL_SIZE;
        int hueW = ColorPickerState.HUE_BAR_VISUAL_W;
        int previewW = 180;
        int totalW = svSize + ColorPickerState.HUE_BAR_VISUAL_GAP + hueW + ColorPickerState.HUE_BAR_VISUAL_GAP + previewW;
        int startX = centerX - totalW / 2;
        int svX = startX;
        int svY = y;
        int hueX = svX + svSize + ColorPickerState.HUE_BAR_VISUAL_GAP;
        int hueY = svY;
        int previewX = hueX + hueW + ColorPickerState.HUE_BAR_VISUAL_GAP;
        int previewY = svY;

        ColorPickerState.recordSvPanel(svX, svY, svSize, svSize);
        ColorPickerState.recordHueBar(hueX, hueY, hueW, svSize);

        // SV 方板
        renderSvPanel(context, svX, svY, svSize);

        // Hue 滑块
        renderHueBar(context, hueX, hueY, hueW, svSize);

        // 颜色预览 + HEX
        renderColorPreview(context, textRenderer, previewX, previewY, previewW);

        // 快速选色
        renderPresetColors(context, textRenderer, previewX, previewY, previewW);

        // 复制按钮
        int hsvTotalW = svSize + ColorPickerState.HUE_BAR_VISUAL_GAP + ColorPickerState.HUE_BAR_VISUAL_W;
        int btnW = 200;
        int btnH = 26;
        int btnX = svX + (hsvTotalW - btnW) / 2;
        int btnY = y + svSize + 6;
        renderCopyButton(context, textRenderer, btnX, btnY, btnW, btnH);
    }

    /**
     * 渲染 SV 饱和度-亮度面板。
     */
    private static void renderSvPanel(GuiGraphicsExtractor context, int svX, int svY, int svSize) {
        ColorPickerState.ensureSvTexture(ColorPickerState.getH());
        if (ColorPickerState.getSvTextureId() != null) {
            context.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, ColorPickerState.getSvTextureId(),
                    svX, svY, 0, 0, svSize, svSize, svSize, svSize);
        } else {
            for (int py = 0; py < svSize; py += 4) {
                for (int px = 0; px < svSize; px += 4) {
                    float s = px / (float) (svSize - 1);
                    float v = 1f - py / (float) (svSize - 1);
                    int rgb = ColorPickerState.hsvToRawRgb(ColorPickerState.getH(), s, v);
                    context.fill(svX + px, svY + py, svX + px + 4, svY + py + 4, 0xFF000000 | (rgb & 0xFFFFFF));
                }
            }
        }
        context.outline(svX, svY, svSize, svSize, 0xFF222222);

        // SV 圆点指示器
        int indicatorPad = 4;
        int ix = svX + Math.round(ColorPickerState.getS() * (svSize - 1));
        int iy = svY + Math.round((1f - ColorPickerState.getV()) * (svSize - 1));
        context.fill(ix - indicatorPad, iy - 1, ix + indicatorPad, iy + 1, 0xFFFFFFFF);
        context.fill(ix - 1, iy - indicatorPad, ix + 1, iy + indicatorPad, 0xFFFFFFFF);
        context.outline(ix - indicatorPad, iy - 1, indicatorPad * 2, 2, 0xFF222222);
        context.outline(ix - 1, iy - indicatorPad, 2, indicatorPad * 2, 0xFF222222);
    }

    /**
     * 渲染色相滑块条。
     */
    private static void renderHueBar(GuiGraphicsExtractor context, int hueX, int hueY, int hueW, int svSize) {
        int hueStepPx = 2;
        for (int py = 0; py < svSize; py += hueStepPx) {
            float h = py / (float) (svSize - 1);
            int rgb = ColorPickerState.hsvToRawRgb(h, 1f, 1f);
            context.fill(hueX, hueY + py, hueX + hueW, hueY + Math.min(py + hueStepPx, svSize),
                    0xFF000000 | (rgb & 0xFFFFFF));
        }
        context.outline(hueX, hueY, hueW, svSize, 0xFF222222);

        int hueIndicatorY = hueY + Math.round(ColorPickerState.getH() * (svSize - 1));
        int hueIndicatorPad = 3;
        context.fill(hueX - hueIndicatorPad, hueIndicatorY - 1, hueX + hueW + hueIndicatorPad, hueIndicatorY + 1, 0xFFFFFFFF);
        context.outline(hueX - hueIndicatorPad, hueIndicatorY - 1, hueW + hueIndicatorPad * 2, 2, 0xFF222222);
    }

    /**
     * 渲染颜色预览区与 HEX 值。
     */
    private static void renderColorPreview(GuiGraphicsExtractor context, Font textRenderer, int previewX, int previewY, int previewW) {
        int previewH = 60;
        int argb = 0xFF000000 | ColorPickerState.getCurrentRgb();
        context.fill(previewX, previewY, previewX + previewW, previewY + previewH, argb);
        context.outline(previewX, previewY, previewW, previewH, 0xFF222222);
        context.outline(previewX - 1, previewY - 1, previewW + 2, previewH + 2, 0xFF888888);

        int stripH = 20;
        context.fill(previewX, previewY + previewH - stripH, previewX + previewW, previewY + previewH, 0x99000000);
        String hexText = String.format("#%06X", ColorPickerState.getCurrentRgb());
        Component hexLine = Component.literal(hexText);
        int hexW = textRenderer.width(hexLine);
        context.text(textRenderer, hexLine,
                previewX + (previewW - hexW) / 2,
                previewY + previewH - stripH + (stripH - textRenderer.lineHeight) / 2,
                0xFFFFFFFF, false);

        Component labelCur = Component.translatable("ocelotsignmod.gui.color_picker.current_label");
        int labelCurW = textRenderer.width(labelCur);
        context.text(textRenderer, labelCur,
                previewX + (previewW - labelCurW) / 2, previewY + previewH + 2, UIConstants.COLOR_DESC_TEXT, false);
    }

    /**
     * 渲染预设颜色选择面板。
     */
    private static void renderPresetColors(GuiGraphicsExtractor context, Font textRenderer, int previewX, int previewY, int previewW) {
        int presetSize = ColorPickerState.COLOR_PICKER_PRESET_SIZE;
        int presetGap = ColorPickerState.COLOR_PICKER_PRESET_GAP;
        int presetCols = ColorPickerState.COLOR_PICKER_PRESET_COLS;
        int presetRows = ColorPickerState.COLOR_PICKER_PRESET_ROWS;
        int presetW = presetCols * presetSize + (presetCols - 1) * presetGap;
        int presetGridStartX = previewX + (previewW - presetW) / 2;
        int presetGridStartY = previewY + 60 + 16;
        Component quickLabel = Component.translatable("ocelotsignmod.gui.color_picker.presets_label");
        context.text(textRenderer, quickLabel, presetGridStartX, presetGridStartY, UIConstants.COLOR_DESC_TEXT, false);
        int presetAreaY = presetGridStartY + 12;

        int currentRgb = ColorPickerState.getCurrentRgb();
        for (int i = 0; i < ColorPickerState.COLOR_PICKER_PRESETS.length; i++) {
            int col = i % presetCols;
            int row = i / presetCols;
            int sx = presetGridStartX + col * (presetSize + presetGap);
            int sy = presetAreaY + row * (presetSize + presetGap);
            int rgb = ColorPickerState.COLOR_PICKER_PRESETS[i];
            context.fill(sx, sy, sx + presetSize, sy + presetSize, 0xFF000000 | (rgb & 0xFFFFFF));
            context.outline(sx, sy, presetSize, presetSize, 0xFF555555);
            if ((rgb & 0xFFFFFF) == (currentRgb & 0xFFFFFF)) {
                context.outline(sx - 1, sy - 1, presetSize + 2, presetSize + 2, 0xFFE088);
            }
        }
    }

    /**
     * 渲染 HEX 复制按钮。
     */
    private static void renderCopyButton(GuiGraphicsExtractor context, Font textRenderer, int btnX, int btnY, int btnW, int btnH) {
        context.fill(btnX, btnY, btnX + btnW, btnY + btnH, UIConstants.COLOR_BTN_BG);
        context.outline(btnX, btnY, btnW, btnH, UIConstants.COLOR_BTN_BORDER);
        Component useBtn = Component.translatable("ocelotsignmod.gui.color_picker.copy_value");
        int useBtnTextW = textRenderer.width(useBtn);
        if (useBtnTextW <= btnW - 8) {
            context.text(textRenderer, useBtn,
                    btnX + (btnW - useBtnTextW) / 2,
                    btnY + (btnH - textRenderer.lineHeight) / 2,
                    UIConstants.COLOR_BTN_TEXT, false);
        } else {
            List<FormattedCharSequence> btnLines = textRenderer.split(useBtn, btnW - 8);
            int lineH = textRenderer.lineHeight;
            int totalH = btnLines.size() * lineH;
            int lineY = btnY + Math.max(0, (btnH - totalH) / 2);
            for (int i = 0; i < btnLines.size(); i++) {
                int lw = textRenderer.width(btnLines.get(i));
                context.text(textRenderer, btnLines.get(i),
                        btnX + (btnW - lw) / 2,
                        lineY + i * lineH,
                        UIConstants.COLOR_BTN_TEXT, false);
            }
        }
    }

    /**
     * 绘制居中换行文本，返回总高度。
     */
    private static int drawCenteredWrappedText(GuiGraphicsExtractor context, Font textRenderer,
                                              Component text, int centerX, int y, int maxWidth) {
        List<FormattedCharSequence> lines = textRenderer.split(text, maxWidth);
        int lineH = textRenderer.lineHeight;
        for (int i = 0; i < lines.size(); i++) {
            int lw = textRenderer.width(lines.get(i));
            context.text(textRenderer, lines.get(i), centerX - lw / 2, y + i * lineH, UIConstants.COLOR_DESC_TEXT, false);
        }
        return lines.size() * lineH;
    }

    /**
     * 处理颜色选择器面板的点击事件。
     *
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param mainWidth 主区域宽度
     * @param contentStartY 内容起始 Y 坐标
     * @param scrollWindowStartY 滚动窗口起始 Y 坐标
     * @param scrollWindowEndY 滚动窗口结束 Y 坐标
     * @return 若点击被消费返回 {@code true}
     */
    public static boolean handleClick(double mouseX, double mouseY, int mainWidth, int contentStartY,
                                      int scrollWindowStartY, int scrollWindowEndY) {
        Font textRenderer = Minecraft.getInstance().font;
        if (mouseY < scrollWindowStartY || mouseY > scrollWindowEndY) return false;

        int centerX = UIConstants.SIDEBAR_WIDTH + mainWidth / 2;
        int y = contentStartY;

        int introMaxWidth = mainWidth - 60;
        if (introMaxWidth < 60) introMaxWidth = 60;
        y += textRenderer.split(Component.translatable("ocelotsignmod.gui.color_picker.intro1"), introMaxWidth).size() * textRenderer.lineHeight + 4;
        y += textRenderer.split(Component.translatable("ocelotsignmod.gui.color_picker.intro2"), introMaxWidth).size() * textRenderer.lineHeight + 4;

        int svSize = ColorPickerState.SV_VISUAL_SIZE;
        int hueW = ColorPickerState.HUE_BAR_VISUAL_W;
        int previewW = 180;
        int totalW = svSize + ColorPickerState.HUE_BAR_VISUAL_GAP + hueW + ColorPickerState.HUE_BAR_VISUAL_GAP + previewW;
        int startX = centerX - totalW / 2;
        int svX = startX;
        int hueX = svX + svSize + ColorPickerState.HUE_BAR_VISUAL_GAP;
        int previewX = hueX + hueW + ColorPickerState.HUE_BAR_VISUAL_GAP;

        // SV 方板
        if (LayoutHelper.isMouseInRect(mouseX, mouseY, svX, y, svSize, svSize)) {
            ColorPickerState.applySvFromMouse(mouseX, mouseY, svX, y, svSize);
            PatternAndFontOverlay.isDraggingSv = true;
            PatternAndFontOverlay.isDraggingHue = false;
            return true;
        }

        // Hue 滑块
        if (LayoutHelper.isMouseInRect(mouseX, mouseY, hueX, y, hueW, svSize)) {
            ColorPickerState.applyHueFromMouse(mouseY, y, svSize);
            PatternAndFontOverlay.isDraggingHue = true;
            PatternAndFontOverlay.isDraggingSv = false;
            return true;
        }

        // 快速选色
        if (handlePresetClick(mouseX, mouseY, previewX, y, previewW)) {
            return true;
        }

        // 复制按钮
        int hsvTotalW = svSize + ColorPickerState.HUE_BAR_VISUAL_GAP + ColorPickerState.HUE_BAR_VISUAL_W;
        int btnW = 200;
        int btnH = 26;
        int btnX = svX + (hsvTotalW - btnW) / 2;
        int btnY = y + svSize + 6;
        if (LayoutHelper.isMouseInRect(mouseX, mouseY, btnX, btnY, btnW, btnH)) {
            ColorPickerState.copyHexToClipboard(String.format("#%06X", ColorPickerState.getCurrentRgb()));
            PatternAndFontOverlay.isDraggingSv = false;
            PatternAndFontOverlay.isDraggingHue = false;
            return true;
        }

        return false;
    }

    /**
     * 处理预设颜色的点击事件。
     *
     * @return 若点击被消费返回 {@code true}
     */
    private static boolean handlePresetClick(double mouseX, double mouseY, int previewX, int y, int previewW) {
        int presetSize = ColorPickerState.COLOR_PICKER_PRESET_SIZE;
        int presetGap = ColorPickerState.COLOR_PICKER_PRESET_GAP;
        int presetCols = ColorPickerState.COLOR_PICKER_PRESET_COLS;
        int presetW = presetCols * presetSize + (presetCols - 1) * presetGap;
        int presetGridStartX = previewX + (previewW - presetW) / 2;
        int presetGridStartY = y + 60 + 16 + 12;

        for (int i = 0; i < ColorPickerState.COLOR_PICKER_PRESETS.length; i++) {
            int col = i % presetCols;
            int row = i / presetCols;
            int sx = presetGridStartX + col * (presetSize + presetGap);
            int sy = presetGridStartY + row * (presetSize + presetGap);
            if (LayoutHelper.isMouseInRect(mouseX, mouseY, sx, sy, presetSize, presetSize)) {
                int rgb = ColorPickerState.COLOR_PICKER_PRESETS[i];
                ColorPickerState.rgbToHsv((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
                PatternAndFontOverlay.isDraggingSv = false;
                PatternAndFontOverlay.isDraggingHue = false;
                return true;
            }
        }
        return false;
    }
}
