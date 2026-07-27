package bklmc.ocelotsign.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;

/**
 * 颜色选择器面板的 UI 渲染与交互处理 (1.19.2 兼容版)
 *
 * @see ColorPickerState
 * @see PatternAndFontOverlay
 */
public final class ColorPickerRenderer {

    private ColorPickerRenderer() {
    }

    /**
     * 渲染颜色选择器面板。
     */
    public static void render(MatrixStack matrices, TextRenderer textRenderer,
                              int mainWidth, int contentStartY, int scrollWindowStartY, int scrollWindowEndY) {
        int centerX = UIConstants.SIDEBAR_WIDTH + mainWidth / 2;
        int y = contentStartY;

        // 顶部说明
        int introMaxWidth = mainWidth - 60;
        if (introMaxWidth < 60) introMaxWidth = 60;
        Text intro1 = Text.translatable("ocelotsignmod.gui.color_picker.intro1");
        Text intro2 = Text.translatable("ocelotsignmod.gui.color_picker.intro2");
        int intro1H = drawCenteredWrappedText(matrices, textRenderer, intro1, centerX, y, introMaxWidth);
        y += intro1H + 4;
        int intro2H = drawCenteredWrappedText(matrices, textRenderer, intro2, centerX, y, introMaxWidth);
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
        renderSvPanel(matrices, svX, svY, svSize);

        // Hue 滑块
        renderHueBar(matrices, hueX, hueY, hueW, svSize);

        // 颜色预览 + HEX
        renderColorPreview(matrices, textRenderer, previewX, previewY, previewW);

        // 快速选色
        renderPresetColors(matrices, textRenderer, previewX, previewY, previewW);

        // 复制按钮
        int hsvTotalW = svSize + ColorPickerState.HUE_BAR_VISUAL_GAP + ColorPickerState.HUE_BAR_VISUAL_W;
        int btnW = 200;
        int btnH = 26;
        int btnX = svX + (hsvTotalW - btnW) / 2;
        int btnY = y + svSize + 6;
        renderCopyButton(matrices, textRenderer, btnX, btnY, btnW, btnH);
    }

    /**
     * 渲染 SV 饱和度-亮度面板。
     */
    private static void renderSvPanel(MatrixStack matrices, int svX, int svY, int svSize) {
        ColorPickerState.ensureSvTexture(ColorPickerState.getH());
        if (ColorPickerState.getSvTextureId() != null) {
            RenderSystem.setShaderTexture(0, ColorPickerState.getSvTextureId());
            DrawableHelper.drawTexture(matrices, svX, svY, 0, 0, svSize, svSize, svSize, svSize);
        } else {
            for (int py = 0; py < svSize; py += 4) {
                for (int px = 0; px < svSize; px += 4) {
                    float s = px / (float) (svSize - 1);
                    float v = 1f - py / (float) (svSize - 1);
                    int rgb = ColorPickerState.hsvToRawRgb(ColorPickerState.getH(), s, v);
                    DrawableHelper.fill(matrices, svX + px, svY + py, svX + px + 4, svY + py + 4, 0xFF000000 | (rgb & 0xFFFFFF));
                }
            }
        }
        drawBorder(matrices, svX, svY, svSize, svSize, 0xFF222222);

        // SV 圆点指示器
        int indicatorPad = 4;
        int ix = svX + Math.round(ColorPickerState.getS() * (svSize - 1));
        int iy = svY + Math.round((1f - ColorPickerState.getV()) * (svSize - 1));
        DrawableHelper.fill(matrices, ix - indicatorPad, iy - 1, ix + indicatorPad, iy + 1, 0xFFFFFFFF);
        DrawableHelper.fill(matrices, ix - 1, iy - indicatorPad, ix + 1, iy + indicatorPad, 0xFFFFFFFF);
        drawBorder(matrices, ix - indicatorPad, iy - 1, indicatorPad * 2, 2, 0xFF222222);
        drawBorder(matrices, ix - 1, iy - indicatorPad, 2, indicatorPad * 2, 0xFF222222);
    }

    /**
     * 渲染色相滑块条。
     */
    private static void renderHueBar(MatrixStack matrices, int hueX, int hueY, int hueW, int svSize) {
        int hueStepPx = 2;
        for (int py = 0; py < svSize; py += hueStepPx) {
            float h = py / (float) (svSize - 1);
            int rgb = ColorPickerState.hsvToRawRgb(h, 1f, 1f);
            DrawableHelper.fill(matrices, hueX, hueY + py, hueX + hueW, hueY + Math.min(py + hueStepPx, svSize),
                    0xFF000000 | (rgb & 0xFFFFFF));
        }
        drawBorder(matrices, hueX, hueY, hueW, svSize, 0xFF222222);

        int hueIndicatorY = hueY + Math.round(ColorPickerState.getH() * (svSize - 1));
        int hueIndicatorPad = 3;
        DrawableHelper.fill(matrices, hueX - hueIndicatorPad, hueIndicatorY - 1, hueX + hueW + hueIndicatorPad, hueIndicatorY + 1, 0xFFFFFFFF);
        drawBorder(matrices, hueX - hueIndicatorPad, hueIndicatorY - 1, hueW + hueIndicatorPad * 2, 2, 0xFF222222);
    }

    /**
     * 渲染颜色预览区与 HEX 值。
     */
    private static void renderColorPreview(MatrixStack matrices, TextRenderer textRenderer, int previewX, int previewY, int previewW) {
        int previewH = 60;
        int argb = 0xFF000000 | ColorPickerState.getCurrentRgb();
        DrawableHelper.fill(matrices, previewX, previewY, previewX + previewW, previewY + previewH, argb);
        drawBorder(matrices, previewX, previewY, previewW, previewH, 0xFF222222);
        drawBorder(matrices, previewX - 1, previewY - 1, previewW + 2, previewH + 2, 0xFF888888);

        int stripH = 20;
        DrawableHelper.fill(matrices, previewX, previewY + previewH - stripH, previewX + previewW, previewY + previewH, 0x99000000);
        String hexText = String.format("#%06X", ColorPickerState.getCurrentRgb());
        Text hexLine = Text.literal(hexText);
        int hexW = textRenderer.getWidth(hexLine);
        textRenderer.draw(matrices, hexLine,
                previewX + (previewW - hexW) / 2,
                previewY + previewH - stripH + (stripH - textRenderer.fontHeight) / 2,
                0xFFFFFFFF);

        Text labelCur = Text.translatable("ocelotsignmod.gui.color_picker.current_label");
        int labelCurW = textRenderer.getWidth(labelCur);
        textRenderer.draw(matrices, labelCur,
                previewX + (previewW - labelCurW) / 2, previewY + previewH + 2, UIConstants.COLOR_DESC_TEXT);
    }

    /**
     * 渲染预设颜色选择面板。
     */
    private static void renderPresetColors(MatrixStack matrices, TextRenderer textRenderer, int previewX, int previewY, int previewW) {
        int presetSize = ColorPickerState.COLOR_PICKER_PRESET_SIZE;
        int presetGap = ColorPickerState.COLOR_PICKER_PRESET_GAP;
        int presetCols = ColorPickerState.COLOR_PICKER_PRESET_COLS;
        int presetRows = ColorPickerState.COLOR_PICKER_PRESET_ROWS;
        int presetW = presetCols * presetSize + (presetCols - 1) * presetGap;
        int presetGridStartX = previewX + (previewW - presetW) / 2;
        int presetGridStartY = previewY + 60 + 16;
        Text quickLabel = Text.translatable("ocelotsignmod.gui.color_picker.presets_label");
        textRenderer.draw(matrices, quickLabel, presetGridStartX, presetGridStartY, UIConstants.COLOR_DESC_TEXT);
        int presetAreaY = presetGridStartY + 12;

        int currentRgb = ColorPickerState.getCurrentRgb();
        for (int i = 0; i < ColorPickerState.COLOR_PICKER_PRESETS.length; i++) {
            int col = i % presetCols;
            int row = i / presetCols;
            int sx = presetGridStartX + col * (presetSize + presetGap);
            int sy = presetAreaY + row * (presetSize + presetGap);
            int rgb = ColorPickerState.COLOR_PICKER_PRESETS[i];
            DrawableHelper.fill(matrices, sx, sy, sx + presetSize, sy + presetSize, 0xFF000000 | (rgb & 0xFFFFFF));
            drawBorder(matrices, sx, sy, presetSize, presetSize, 0xFF555555);
            if ((rgb & 0xFFFFFF) == (currentRgb & 0xFFFFFF)) {
                drawBorder(matrices, sx - 1, sy - 1, presetSize + 2, presetSize + 2, 0xFFE088);
            }
        }
    }

    /**
     * 渲染 HEX 复制按钮。
     */
    private static void renderCopyButton(MatrixStack matrices, TextRenderer textRenderer, int btnX, int btnY, int btnW, int btnH) {
        DrawableHelper.fill(matrices, btnX, btnY, btnX + btnW, btnY + btnH, UIConstants.COLOR_BTN_BG);
        drawBorder(matrices, btnX, btnY, btnW, btnH, UIConstants.COLOR_BTN_BORDER);
        Text useBtn = Text.translatable("ocelotsignmod.gui.color_picker.copy_value");
        int useBtnTextW = textRenderer.getWidth(useBtn);
        if (useBtnTextW <= btnW - 8) {
            textRenderer.draw(matrices, useBtn,
                    btnX + (btnW - useBtnTextW) / 2,
                    btnY + (btnH - textRenderer.fontHeight) / 2,
                    UIConstants.COLOR_BTN_TEXT);
        } else {
            List<OrderedText> btnLines = textRenderer.wrapLines(useBtn, btnW - 8);
            int lineH = textRenderer.fontHeight;
            int totalH = btnLines.size() * lineH;
            int lineY = btnY + Math.max(0, (btnH - totalH) / 2);
            for (int i = 0; i < btnLines.size(); i++) {
                int lw = textRenderer.getWidth(btnLines.get(i));
                textRenderer.draw(matrices, btnLines.get(i),
                        btnX + (btnW - lw) / 2,
                        lineY + i * lineH,
                        UIConstants.COLOR_BTN_TEXT);
            }
        }
    }

    /**
     * 绘制居中换行文本，返回总高度。
     */
    private static int drawCenteredWrappedText(MatrixStack matrices, TextRenderer textRenderer,
                                               Text text, int centerX, int y, int maxWidth) {
        List<OrderedText> lines = textRenderer.wrapLines(text, maxWidth);
        int lineH = textRenderer.fontHeight;
        for (int i = 0; i < lines.size(); i++) {
            int lw = textRenderer.getWidth(lines.get(i));
            textRenderer.draw(matrices, lines.get(i), centerX - lw / 2, y + i * lineH, UIConstants.COLOR_DESC_TEXT);
        }
        return lines.size() * lineH;
    }

    /**
     * 处理颜色选择器面板的点击事件。
     */
    public static boolean handleClick(double mouseX, double mouseY, int mainWidth, int contentStartY,
                                      int scrollWindowStartY, int scrollWindowEndY) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        if (mouseY < scrollWindowStartY || mouseY > scrollWindowEndY) return false;

        int centerX = UIConstants.SIDEBAR_WIDTH + mainWidth / 2;
        int y = contentStartY;

        int introMaxWidth = mainWidth - 60;
        if (introMaxWidth < 60) introMaxWidth = 60;
        y += textRenderer.wrapLines(Text.translatable("ocelotsignmod.gui.color_picker.intro1"), introMaxWidth).size() * textRenderer.fontHeight + 4;
        y += textRenderer.wrapLines(Text.translatable("ocelotsignmod.gui.color_picker.intro2"), introMaxWidth).size() * textRenderer.fontHeight + 4;

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

    /**
     * 绘制简单边框（1.19.2 兼容）。
     */
    private static void drawBorder(MatrixStack matrices, int x, int y, int width, int height, int color) {
        DrawableHelper.fill(matrices, x, y, x + width, y + 1, color);
        DrawableHelper.fill(matrices, x, y + height - 1, x + width, y + height, color);
        DrawableHelper.fill(matrices, x, y, x + 1, y + height, color);
        DrawableHelper.fill(matrices, x + width - 1, y, x + width, y + height, color);
    }
}