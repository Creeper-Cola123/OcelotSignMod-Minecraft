package bklmc.ocelotsign.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import java.util.List;

/**
 * 颜色拾取器状态管理
 *
 * @see PatternAndFontOverlay
 */
public final class ColorPickerState {

    // 离屏纹理与视觉尺寸
    public static final int SV_PANEL_PIXEL = 128;
    /** 色相条像素高度 */
    public static final int HUE_BAR_PIXEL_H = 18;
    /** 色相条宽度 */
    public static final int HUE_BAR_WIDTH = SV_PANEL_PIXEL;

    // 屏幕显示尺寸
    /** SV 面板显示尺寸 */
    public static final int SV_VISUAL_SIZE = 180;
    /** 色相条显示宽度 */
    public static final int HUE_BAR_VISUAL_W = 18;
    /** 色相条间距 */
    public static final int HUE_BAR_VISUAL_GAP = 10;

    // 48 色预设：8 色相 × 6 阶明度
    /** 预设颜色数组 */
    public static final int[] COLOR_PICKER_PRESETS = {
            0xEF9A9A, 0xFFCC80, 0xFFF59D, 0xA5D6A7, 0x80DEEA, 0x90CAF9, 0xCE93D8, 0xFFFFFF,
            0xE57373, 0xFFB74D, 0xFFF176, 0x81C784, 0x4DD0E1, 0x64B5F6, 0xBA68C8, 0xE0E0E0,
            0xEF5350, 0xFFA726, 0xFFEE58, 0x66BB6A, 0x26C6DA, 0x42A5F5, 0xAB47BC, 0xBDBDBD,
            0xE53935, 0xFB8C00, 0xFDD835, 0x43A047, 0x00ACC1, 0x1E88E5, 0x8E24AA, 0x9E9E9E,
            0xC62828, 0xEF6C00, 0xF9A825, 0x2E7D32, 0x00838F, 0x1565C0, 0x6A1B9A, 0x616161,
            0xB71C1C, 0xE65100, 0xF57F17, 0x1B5E20, 0x006064, 0x0D47A1, 0x4A148C, 0x000000
    };
    /** 预设颜色列数 */
    public static final int COLOR_PICKER_PRESET_COLS = 8;
    /** 预设颜色行数 */
    public static final int COLOR_PICKER_PRESET_ROWS = 6;
    /** 预设颜色块尺寸 */
    public static final int COLOR_PICKER_PRESET_SIZE = 18;
    /** 预设颜色块间距 */
    public static final int COLOR_PICKER_PRESET_GAP = 4;
    /** 步进按钮宽度 */
    public static final int COLOR_PICKER_STEP_BTN_W = 26;
    /** 步进按钮高度 */
    public static final int COLOR_PICKER_STEP_BTN_H = 22;
    /** 数值显示宽度 */
    public static final int COLOR_PICKER_VALUE_W = 56;

    // HSV 内部表示（0..1）
    private static float h = 0.3333f;
    private static float s = 1.0f;
    private static float v = 0.5f;

    // HSV 面板屏幕位置（每帧由渲染写入）
    private static int svPanelX = 0, svPanelY = 0, svPanelW = 0, svPanelH = 0;
    private static int hueBarX = 0, hueBarY = 0, hueBarW = 0, hueBarH = 0;

    // 动态纹理缓存
    private static Identifier svTextureId = null;
    private static NativeImage svTextureImage = null;
    private static float svTextureHueCached = -1f;

    private ColorPickerState() {
    }

    // ==================== HSV 字段访问器 ====================

    /**
     * 获取色相值。
     *
     * @return 色相（0..1）
     */
    public static float getH() { return h; }

    /**
     * 获取饱和度。
     *
     * @return 饱和度（0..1）
     */
    public static float getS() { return s; }

    /**
     * 获取明度。
     *
     * @return 明度（0..1）
     */
    public static float getV() { return v; }

    /**
     * 设置 HSV 值。
     *
     * @param nh 色相（0..1）
     * @param ns 饱和度（0..1）
     * @param nv 明度（0..1）
     */
    public static void setHsv(float nh, float ns, float nv) { h = nh; s = ns; v = nv; }

    // ==================== 面板位置（每帧写入） ====================

    /**
     * 记录 SV 面板屏幕位置。
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @param w 宽度
     * @param h_ 高度
     */
    public static void recordSvPanel(int x, int y, int w, int h_) {
        svPanelX = x; svPanelY = y; svPanelW = w; svPanelH = h_;
    }

    /**
     * 记录色相条屏幕位置。
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @param w 宽度
     * @param h_ 高度
     */
    public static void recordHueBar(int x, int y, int w, int h_) {
        hueBarX = x; hueBarY = y; hueBarW = w; hueBarH = h_;
    }

    /**
     * 获取 SV 面板 X 坐标。
     *
     * @return X 坐标
     */
    public static int getSvPanelX() { return svPanelX; }

    /**
     * 获取 SV 面板 Y 坐标。
     *
     * @return Y 坐标
     */
    public static int getSvPanelY() { return svPanelY; }

    /**
     * 获取 SV 面板宽度。
     *
     * @return 宽度
     */
    public static int getSvPanelW() { return svPanelW; }

    /**
     * 获取色相条 Y 坐标。
     *
     * @return Y 坐标
     */
    public static int getHueBarY() { return hueBarY; }

    /**
     * 获取色相条高度。
     *
     * @return 高度
     */
    public static int getHueBarH() { return hueBarH; }

    // ==================== 离屏纹理 ====================

    /**
     * 获取 SV 纹理标识符。
     *
     * @return 纹理标识符
     */
    public static Identifier getSvTextureId() { return svTextureId; }

    // ==================== 公开方法 ====================

    /**
     * 获取当前 RGB 整数值。
     *
     * @return RGB 整数（0xRRGGBB 格式）
     */
    public static int getCurrentRgb() {
        return ((PatternAndFontOverlay.colorPickerR & 0xFF) << 16)
                | ((PatternAndFontOverlay.colorPickerG & 0xFF) << 8)
                | (PatternAndFontOverlay.colorPickerB & 0xFF);
    }

    /**
     * 计算颜色选择器面板的内容总高度。
     *
     * @param mainWidth 主内容区域宽度
     * @param textRenderer 文本渲染器
     * @return 总内容高度（像素）
     */
    public static int getColorPickerContentHeight(int mainWidth, Font textRenderer) {
        int introMaxWidth = mainWidth - 60;
        if (introMaxWidth < 60) introMaxWidth = 60;

        Component intro1 = Component.translatable("ocelotsignmod.gui.color_picker.intro1");
        Component intro2 = Component.translatable("ocelotsignmod.gui.color_picker.intro2");

        List<FormattedCharSequence> lines1 = textRenderer.split(intro1, introMaxWidth);
        List<FormattedCharSequence> lines2 = textRenderer.split(intro2, introMaxWidth);

        int introH = lines1.size() * textRenderer.lineHeight + 4
                + lines2.size() * textRenderer.lineHeight + 4;

        int rightColumnH = 60 + 16 + 12
                + (COLOR_PICKER_PRESET_ROWS * (COLOR_PICKER_PRESET_SIZE + COLOR_PICKER_PRESET_GAP)
                - COLOR_PICKER_PRESET_GAP);
        int svAreaH = Math.max(SV_VISUAL_SIZE, rightColumnH);

        Component useBtnText = Component.translatable("ocelotsignmod.gui.color_picker.copy_value");
        List<FormattedCharSequence> btnLines = textRenderer.split(useBtnText, 200 - 8);
        int btnH = Math.max(26, btnLines.size() * textRenderer.lineHeight + 4);

        return introH + svAreaH + 6 + btnH;
    }

    /**
     * 确保 SV 色板离屏纹理存在（hue 变化时重新生成）。
     *
     * @param hue 色相值（0..1）
     */
    public static void ensureSvTexture(float hue) {
        if (svTextureId != null && Math.abs(hue - svTextureHueCached) < 0.0001f) return;
        if (svTextureImage != null) {
            svTextureImage.close();
            svTextureImage = null;
        }
        int size = SV_PANEL_PIXEL;
        NativeImage image = new NativeImage(size, size, false);
        for (int y = 0; y < size; y++) {
            float vVal = 1f - (y / (float) (size - 1));
            for (int x = 0; x < size; x++) {
                float sVal = x / (float) (size - 1);
                int rgb = hsvToRawRgb(hue, sVal, vVal);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                image.setPixelABGR(x, y, (0xFF << 24) | (b << 16) | (g << 8) | r);
            }
        }
        TextureManager tm = Minecraft.getInstance().getTextureManager();
        if (svTextureId != null) {
            tm.release(svTextureId);
        }
        svTextureId = Identifier.fromNamespaceAndPath("ocelotsignmod", "dynamic/color_picker_sv");
        tm.register(svTextureId, new net.minecraft.client.renderer.texture.DynamicTexture(() -> "ocelotsignmod/sv_picker", image));
        svTextureImage = image;
        svTextureHueCached = hue;
    }

    /**
     * HSV 转 RGB 整数（不写回字段）。
     *
     * @param hue 色相（0..1）
     * @param sat 饱和度（0..1）
     * @param val 明度（0..1）
     * @return RGB 整数（0xRRGGBB 格式）
     */
    public static int hsvToRawRgb(float hue, float sat, float val) {
        hue = Mth.clamp(hue, 0f, 1f);
        sat = Mth.clamp(sat, 0f, 1f);
        val = Mth.clamp(val, 0f, 1f);
        int r, g, b;
        if (sat <= 0.0001f) {
            int gray = Math.round(val * 255f);
            r = g = b = gray;
        } else {
            float h6 = hue * 6f;
            if (h6 >= 6f) h6 = 0f;
            int i = (int) Math.floor(h6);
            float f = h6 - i;
            float p = val * (1f - sat);
            float q = val * (1f - sat * f);
            float t = val * (1f - sat * (1f - f));
            switch (i) {
                case 0 -> { r = Math.round(val * 255f); g = Math.round(t * 255f); b = Math.round(p * 255f); }
                case 1 -> { r = Math.round(q * 255f); g = Math.round(val * 255f); b = Math.round(p * 255f); }
                case 2 -> { r = Math.round(p * 255f); g = Math.round(val * 255f); b = Math.round(t * 255f); }
                case 3 -> { r = Math.round(p * 255f); g = Math.round(q * 255f); b = Math.round(val * 255f); }
                case 4 -> { r = Math.round(t * 255f); g = Math.round(p * 255f); b = Math.round(val * 255f); }
                default -> { r = Math.round(val * 255f); g = Math.round(p * 255f); b = Math.round(q * 255f); }
            }
        }
        return (r << 16) | (g << 8) | b;
    }

    /**
     * HSV 转 RGB 并写回 colorPickerR/G/B。
     *
     * @param hue 色相（0..1）
     * @param sat 饱和度（0..1）
     * @param val 明度（0..1）
     */
    public static void hsvToRgb(float hue, float sat, float val) {
        hue = Mth.clamp(hue, 0f, 1f);
        sat = Mth.clamp(sat, 0f, 1f);
        val = Mth.clamp(val, 0f, 1f);
        h = hue; s = sat; v = val;
        int r, g, b;
        if (sat <= 0.0001f) {
            int gray = Math.round(val * 255f);
            r = g = b = gray;
        } else {
            float h6 = hue * 6f;
            if (h6 >= 6f) h6 = 0f;
            int i = (int) Math.floor(h6);
            float f = h6 - i;
            float p = val * (1f - sat);
            float q = val * (1f - sat * f);
            float t = val * (1f - sat * (1f - f));
            switch (i) {
                case 0 -> { r = Math.round(val * 255f); g = Math.round(t * 255f); b = Math.round(p * 255f); }
                case 1 -> { r = Math.round(q * 255f); g = Math.round(val * 255f); b = Math.round(p * 255f); }
                case 2 -> { r = Math.round(p * 255f); g = Math.round(val * 255f); b = Math.round(t * 255f); }
                case 3 -> { r = Math.round(p * 255f); g = Math.round(q * 255f); b = Math.round(val * 255f); }
                case 4 -> { r = Math.round(t * 255f); g = Math.round(p * 255f); b = Math.round(val * 255f); }
                default -> { r = Math.round(val * 255f); g = Math.round(p * 255f); b = Math.round(q * 255f); }
            }
        }
        PatternAndFontOverlay.colorPickerR = Mth.clamp(r, 0, 255);
        PatternAndFontOverlay.colorPickerG = Mth.clamp(g, 0, 255);
        PatternAndFontOverlay.colorPickerB = Mth.clamp(b, 0, 255);
    }

    /**
     * RGB 转 HSV 并写回 h/s/v。
     *
     * @param r 红色分量（0..255）
     * @param g 绿色分量（0..255）
     * @param b 蓝色分量（0..255）
     */
    public static void rgbToHsv(int r, int g, int b) {
        r = Mth.clamp(r, 0, 255);
        g = Mth.clamp(g, 0, 255);
        b = Mth.clamp(b, 0, 255);
        PatternAndFontOverlay.colorPickerR = r;
        PatternAndFontOverlay.colorPickerG = g;
        PatternAndFontOverlay.colorPickerB = b;
        float rf = r / 255f, gf = g / 255f, bf = b / 255f;
        float max = Math.max(rf, Math.max(gf, bf));
        float min = Math.min(rf, Math.min(gf, bf));
        float d = max - min;
        float hue;
        if (d < 0.00001f) {
            hue = 0f;
        } else if (max == rf) {
            hue = ((gf - bf) / d) % 6f;
            if (hue < 0f) hue += 6f;
        } else if (max == gf) {
            hue = (bf - rf) / d + 2f;
        } else {
            hue = (rf - gf) / d + 4f;
        }
        hue /= 6f;
        float sat = max <= 0.00001f ? 0f : d / max;
        h = hue; s = sat; v = max;
    }

    /**
     * 根据 SV 面板鼠标位置更新 HSV 状态。
     *
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param px 面板 X 位置
     * @param py 面板 Y 位置
     * @param size 面板尺寸
     */
    public static void applySvFromMouse(double mouseX, double mouseY, int px, int py, int size) {
        float sat = (float) ((mouseX - px) / (double) Math.max(1, size - 1));
        float val = 1f - (float) ((mouseY - py) / (double) Math.max(1, size - 1));
        sat = Mth.clamp(sat, 0f, 1f);
        val = Mth.clamp(val, 0f, 1f);
        hsvToRgb(h, sat, val);
    }

    /**
     * 根据色相条鼠标位置更新 HSV 状态。
     *
     * @param mouseY 鼠标 Y 坐标
     * @param py 色相条 Y 位置
     * @param size 色相条高度
     */
    public static void applyHueFromMouse(double mouseY, int py, int size) {
        float hue = (float) ((mouseY - py) / (double) Math.max(1, size - 1));
        hue = Mth.clamp(hue, 0f, 1f);
        hsvToRgb(hue, s, v);
    }

    /**
     * 将 HEX 颜色字符串写入系统剪贴板并显示提示。
     *
     * @param hexWithHash 带 # 的 HEX 颜色字符串
     */
    public static void copyHexToClipboard(String hexWithHash) {
        Minecraft client = Minecraft.getInstance();
        if (client == null) return;
        try {
            long handle = client.getWindow().handle();
            if (handle != 0L) {
                org.lwjgl.glfw.GLFW.glfwSetClipboardString(handle, hexWithHash);
            }
        } catch (Throwable t) {
            // 静默忽略
        }
        if (client.keyboardHandler != null) {
            client.keyboardHandler.setClipboard(hexWithHash);
        }
        net.minecraft.client.gui.components.toasts.SystemToast.add(
                client.getToastManager(),
                net.minecraft.client.gui.components.toasts.SystemToast.SystemToastId.PERIODIC_NOTIFICATION,
                Component.translatable("ocelotsignmod.gui.color_palette.copy_toast_title"),
                Component.translatable("ocelotsignmod.gui.color_palette.copy_toast_body", hexWithHash)
        );
    }

    /**
     * 绘制加减按钮（仅外观）。
     *
     * @param context 绘制上下文
     * @param textRenderer 文本渲染器
     * @param x 按钮 X 坐标
     * @param y 按钮 Y 坐标
     * @param w 按钮宽度
     * @param h_ 按钮高度
     * @param label 按钮标签
     * @param hover 是否悬停
     */
    public static void drawStepButton(GuiGraphicsExtractor context, Font textRenderer,
                                      int x, int y, int w, int h_, String label, boolean hover) {
        context.fill(x, y, x + w, y + h_, hover ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG);
        context.outline(x, y, w, h_, UIConstants.COLOR_BTN_BORDER);
        int tw = textRenderer.width(label);
        context.text(textRenderer, label,
                x + (w - tw) / 2, y + (h_ - textRenderer.lineHeight) / 2,
                UIConstants.COLOR_BTN_TEXT, false);
    }
}
