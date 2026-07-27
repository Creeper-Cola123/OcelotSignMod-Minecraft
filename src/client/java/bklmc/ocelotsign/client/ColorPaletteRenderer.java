package bklmc.ocelotsign.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;

/**
 * 道路标志专用调色板渲染器 (1.19.2 兼容版)
 *
 * @see ColorPickerState
 * @see PatternAndFontOverlay
 */
public final class ColorPaletteRenderer {

    private static final int BASE_HEADER_HEIGHT = 26;
    private static final int SWATCH_SIZE = 18;

    private static final int[] PALETTE_RGB = {
            0x008000, 0x1F4EA0, 0xFFCC00, 0xA36927, 0xF26121
    };

    private ColorPaletteRenderer() {
    }

    /**
     * 计算引导文本区域高度。
     */
    private static int calculateIntroHeight(TextRenderer textRenderer, int mainWidth) {
        int introMaxWidth = mainWidth - 60;
        if (introMaxWidth < 60) introMaxWidth = 60;

        Text intro1 = Text.translatable("ocelotsignmod.gui.color_palette.intro");
        Text intro2 = Text.translatable("ocelotsignmod.gui.color_palette.intro_hint");

        List<OrderedText> lines1 = textRenderer.wrapLines(intro1, introMaxWidth);
        List<OrderedText> lines2 = textRenderer.wrapLines(intro2, introMaxWidth);

        return lines1.size() * textRenderer.fontHeight + 4
                + lines2.size() * textRenderer.fontHeight + 8;
    }

    /**
     * 计算表头实际高度（考虑文本换行）。
     */
    private static int calculateHeaderHeight(TextRenderer textRenderer, int tableWidth) {
        int col1W = (int) (tableWidth * 0.34f);
        int col2W = (int) (tableWidth * 0.20f);
        int col3W = (int) (tableWidth * 0.26f);
        int col4W = tableWidth - col1W - col2W - col3W;

        int swatchHWidth = textRenderer.getWidth(Text.translatable("ocelotsignmod.gui.color_palette.col.swatch"));
        int copyHWidth = textRenderer.getWidth(Text.translatable("ocelotsignmod.gui.color_palette.col.copy"));

        int col1Lines = textRenderer.wrapLines(Text.translatable("ocelotsignmod.gui.color_palette.col.name"), col1W - 16).size();
        int col2Lines = textRenderer.wrapLines(Text.translatable("ocelotsignmod.gui.color_palette.col.swatch"), col2W - swatchHWidth - SWATCH_SIZE - 6).size();
        int col3Lines = textRenderer.wrapLines(Text.translatable("ocelotsignmod.gui.color_palette.col.hex"), col3W - 16).size();
        int col4Lines = textRenderer.wrapLines(Text.translatable("ocelotsignmod.gui.color_palette.col.copy"), col4W - 16).size();

        int maxLines = Math.max(Math.max(col1Lines, col2Lines), Math.max(col3Lines, col4Lines));
        return Math.max(BASE_HEADER_HEIGHT, maxLines * textRenderer.fontHeight + 8);
    }

    /**
     * 获取内容区总高度，用于滚动计算。
     */
    public static int getContentHeight(int mainWidth, TextRenderer textRenderer) {
        int introH = calculateIntroHeight(textRenderer, mainWidth);
        int tableWidth = mainWidth - 60;
        int headerH = calculateHeaderHeight(textRenderer, tableWidth);
        int rows = 5;
        int rowH = 26;
        return introH + headerH + rows * rowH + 10 + 48;
    }

    /**
     * 绘制调色板引导文本区域，返回内容结束 Y 坐标。
     */
    private static int renderPaletteIntro(MatrixStack matrices, TextRenderer textRenderer,
                                          int mainWidth, int contentStartY, int centerX) {
        int introMaxWidth = mainWidth - 60;
        if (introMaxWidth < 60) introMaxWidth = 60;

        Text intro1 = Text.translatable("ocelotsignmod.gui.color_palette.intro");
        Text intro2 = Text.translatable("ocelotsignmod.gui.color_palette.intro_hint");

        List<OrderedText> lines1 = textRenderer.wrapLines(intro1, introMaxWidth);
        List<OrderedText> lines2 = textRenderer.wrapLines(intro2, introMaxWidth);

        int lineH = textRenderer.fontHeight;
        int y = contentStartY;

        for (int i = 0; i < lines1.size(); i++) {
            int lw = textRenderer.getWidth(lines1.get(i));
            textRenderer.draw(matrices, lines1.get(i), centerX - lw / 2, y + i * lineH, UIConstants.COLOR_DESC_TEXT);
        }
        y += lines1.size() * lineH + 4;

        for (int i = 0; i < lines2.size(); i++) {
            int lw = textRenderer.getWidth(lines2.get(i));
            textRenderer.draw(matrices, lines2.get(i), centerX - lw / 2, y + i * lineH, UIConstants.COLOR_DESC_TEXT);
        }
        y += lines2.size() * lineH + 8;

        return y;
    }

    /**
     * 绘制颜色表格表头行，返回表头高度。
     */
    private static int renderTableHeader(MatrixStack matrices, TextRenderer textRenderer,
                                         int tableLeft, int tableTop, int tableWidth) {
        int col1W = (int) (tableWidth * 0.34f);
        int col2W = (int) (tableWidth * 0.20f);
        int col3W = (int) (tableWidth * 0.26f);
        int col4W = tableWidth - col1W - col2W - col3W;

        int swatchHWidth = textRenderer.getWidth(Text.translatable("ocelotsignmod.gui.color_palette.col.swatch"));
        int copyHWidth = textRenderer.getWidth(Text.translatable("ocelotsignmod.gui.color_palette.col.copy"));

        List<OrderedText> hNameLines = textRenderer.wrapLines(Text.translatable("ocelotsignmod.gui.color_palette.col.name"), col1W - 16);
        List<OrderedText> hSwatchLines = textRenderer.wrapLines(Text.translatable("ocelotsignmod.gui.color_palette.col.swatch"), col2W - swatchHWidth - SWATCH_SIZE - 6);
        List<OrderedText> hHexLines = textRenderer.wrapLines(Text.translatable("ocelotsignmod.gui.color_palette.col.hex"), col3W - 16);
        List<OrderedText> hCopyLines = textRenderer.wrapLines(Text.translatable("ocelotsignmod.gui.color_palette.col.copy"), col4W - 16);

        int maxLines = Math.max(Math.max(hNameLines.size(), hSwatchLines.size()),
                Math.max(hHexLines.size(), hCopyLines.size()));
        int headerHeight = Math.max(BASE_HEADER_HEIGHT, maxLines * textRenderer.fontHeight + 8);

        // 绘制表头背景
        DrawableHelper.fill(matrices, tableLeft, tableTop, tableLeft + tableWidth, tableTop + headerHeight, 0xFFEDEDED);
        DrawableHelper.fill(matrices, tableLeft, tableTop + headerHeight - 1, tableLeft + tableWidth, tableTop + headerHeight, UIConstants.COLOR_ITEM_BORDER);

        int lineH = textRenderer.fontHeight;
        int textStartY = tableTop + (headerHeight - maxLines * lineH) / 2;

        // 列 1: 名称
        for (int i = 0; i < hNameLines.size(); i++) {
            textRenderer.draw(matrices, hNameLines.get(i), tableLeft + 8, textStartY + i * lineH, 0xFF333333);
        }

        // 列 2: 色块（居中显示）
        int swatchX = tableLeft + col1W + (col2W - SWATCH_SIZE) / 2;
        int swatchTextLines = Math.max(1, hSwatchLines.size());
        int swatchTextStartY = textStartY + (maxLines - swatchTextLines) / 2 * lineH;
        for (int i = 0; i < hSwatchLines.size(); i++) {
            int lw = textRenderer.getWidth(hSwatchLines.get(i));
            textRenderer.draw(matrices, hSwatchLines.get(i),
                    swatchX + SWATCH_SIZE + 6,
                    swatchTextStartY + i * lineH, 0xFF333333);
        }

        // 列 3: HEX
        for (int i = 0; i < hHexLines.size(); i++) {
            textRenderer.draw(matrices, hHexLines.get(i), tableLeft + col1W + col2W + 8, textStartY + i * lineH, 0xFF333333);
        }

        // 列 4: 复制
        for (int i = 0; i < hCopyLines.size(); i++) {
            int lw = textRenderer.getWidth(hCopyLines.get(i));
            textRenderer.draw(matrices, hCopyLines.get(i), tableLeft + col1W + col2W + col3W + (col4W - lw) / 2, textStartY + i * lineH, 0xFF333333);
        }

        return headerHeight;
    }

    /**
     * 渲染调色板完整表格。
     */
    public static void render(MatrixStack matrices, TextRenderer textRenderer, int mouseX, int mouseY,
                              int mainWidth, int contentStartY,
                              int scrollWindowStartY, int scrollWindowEndY) {
        int centerX = UIConstants.SIDEBAR_WIDTH + mainWidth / 2;

        // 绘制引导文本
        int tableTop = renderPaletteIntro(matrices, textRenderer, mainWidth, contentStartY, centerX);

        int tableLeft = UIConstants.SIDEBAR_WIDTH + 30;
        int tableWidth = mainWidth - 60;
        int rowH = 26;
        int col1W = (int) (tableWidth * 0.34f);
        int col2W = (int) (tableWidth * 0.20f);
        int col3W = (int) (tableWidth * 0.26f);
        int col4W = tableWidth - col1W - col2W - col3W;
        int copyBtnW = Math.max(48, col4W - 12);
        int copyBtnH = 18;

        // 绘制表头
        int headerH = renderTableHeader(matrices, textRenderer, tableLeft, tableTop, tableWidth);

        // 颜色名称 i18n key
        String[] nameKeys = {
                "ocelotsignmod.gui.color_palette.color.green",
                "ocelotsignmod.gui.color_palette.color.blue",
                "ocelotsignmod.gui.color_palette.color.yellow",
                "ocelotsignmod.gui.color_palette.color.brown",
                "ocelotsignmod.gui.color_palette.color.orange_construction"
        };

        int y = tableTop + headerH;
        for (int i = 0; i < PALETTE_RGB.length; i++) {
            int rgb = PALETTE_RGB[i];
            // 斑马纹背景
            if ((i & 1) == 0) {
                DrawableHelper.fill(matrices, tableLeft, y, tableLeft + tableWidth, y + rowH, 0xFFFAFAFA);
            }
            // 行分隔线
            DrawableHelper.fill(matrices, tableLeft, y + rowH - 1, tableLeft + tableWidth, y + rowH, 0xFFE0E0E0);

            // 列 1: 颜色名称
            Text nameText = Text.translatable(nameKeys[i]);
            List<OrderedText> nameLines = textRenderer.wrapLines(nameText, col1W - 16);
            int nameStartY = y + (rowH - nameLines.size() * textRenderer.fontHeight) / 2;
            for (int li = 0; li < nameLines.size(); li++) {
                textRenderer.draw(matrices, nameLines.get(li), tableLeft + 8, nameStartY + li * textRenderer.fontHeight, 0xFF222222);
            }

            // 列 2: 色块预览
            int swatchY = y + (rowH - SWATCH_SIZE) / 2;
            int swatchX = tableLeft + col1W + (col2W - SWATCH_SIZE) / 2;
            DrawableHelper.fill(matrices, swatchX, swatchY, swatchX + SWATCH_SIZE, swatchY + SWATCH_SIZE, 0xFF000000 | rgb);
            drawBorder(matrices, swatchX, swatchY, SWATCH_SIZE, SWATCH_SIZE, UIConstants.COLOR_ITEM_BORDER);

            // 列 3: HEX 值
            String hex = String.format("#%06X", rgb & 0xFFFFFF);
            List<OrderedText> hexLines = textRenderer.wrapLines(Text.literal(hex), col3W - 16);
            int hexStartY = y + (rowH - hexLines.size() * textRenderer.fontHeight) / 2;
            for (int li = 0; li < hexLines.size(); li++) {
                textRenderer.draw(matrices, hexLines.get(li), tableLeft + col1W + col2W + 8, hexStartY + li * textRenderer.fontHeight, 0xFF222222);
            }

            // 列 4: 复制按钮
            int btnX = tableLeft + col1W + col2W + col3W + (col4W - copyBtnW) / 2;
            int btnY = y + (rowH - copyBtnH) / 2;
            boolean btnHover = mouseX >= btnX && mouseX <= btnX + copyBtnW
                    && mouseY >= btnY && mouseY <= btnY + copyBtnH
                    && mouseY >= scrollWindowStartY && mouseY <= scrollWindowEndY;
            DrawableHelper.fill(matrices, btnX, btnY, btnX + copyBtnW, btnY + copyBtnH,
                    btnHover ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG);
            drawBorder(matrices, btnX, btnY, copyBtnW, copyBtnH, UIConstants.COLOR_BTN_BORDER);
            Text btnText = Text.translatable("ocelotsignmod.gui.color_palette.copy");
            List<OrderedText> btnLines = textRenderer.wrapLines(btnText, copyBtnW - 4);
            int btnTextStartY = btnY + (copyBtnH - btnLines.size() * textRenderer.fontHeight) / 2;
            for (int li = 0; li < btnLines.size(); li++) {
                int lw = textRenderer.getWidth(btnLines.get(li));
                textRenderer.draw(matrices, btnLines.get(li),
                        btnX + (copyBtnW - lw) / 2,
                        btnTextStartY + li * textRenderer.fontHeight,
                        UIConstants.COLOR_BTN_TEXT);
            }

            y += rowH;
        }

        // 表格边框
        int totalH = headerH + PALETTE_RGB.length * rowH;
        drawBorder(matrices, tableLeft, tableTop, tableWidth, totalH, UIConstants.COLOR_ITEM_BORDER);
        // 列分隔线
        DrawableHelper.fill(matrices, tableLeft + col1W - 1, tableTop, tableLeft + col1W, tableTop + totalH, 0xFFE0E0E0);
        DrawableHelper.fill(matrices, tableLeft + col1W + col2W - 1, tableTop, tableLeft + col1W + col2W, tableTop + totalH, 0xFFE0E0E0);
        DrawableHelper.fill(matrices, tableLeft + col1W + col2W + col3W - 1, tableTop, tableLeft + col1W + col2W + col3W, tableTop + totalH, 0xFFE0E0E0);
    }

    /**
     * 处理调色板复制按钮点击事件。
     */
    public static boolean handleClick(double mouseX, double mouseY, int mainWidth, int contentStartY,
                                      int scrollWindowStartY, int scrollWindowEndY) {
        if (mouseY < scrollWindowStartY || mouseY > scrollWindowEndY) return false;

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int centerX = UIConstants.SIDEBAR_WIDTH + mainWidth / 2;

        int introH = calculateIntroHeight(textRenderer, mainWidth);
        int tableTop = contentStartY + introH;

        int tableLeft = UIConstants.SIDEBAR_WIDTH + 30;
        int tableWidth = mainWidth - 60;
        int rowH = 26;
        int col1W = (int) (tableWidth * 0.34f);
        int col2W = (int) (tableWidth * 0.20f);
        int col3W = (int) (tableWidth * 0.26f);
        int col4W = tableWidth - col1W - col2W - col3W;
        int copyBtnW = Math.max(48, col4W - 12);
        int copyBtnH = 18;

        int headerH = calculateHeaderHeight(textRenderer, tableWidth);
        int dataStartY = tableTop + headerH;

        for (int i = 0; i < PALETTE_RGB.length; i++) {
            int rgb = PALETTE_RGB[i];
            int rowY = dataStartY + rowH * i;
            int btnX = tableLeft + col1W + col2W + col3W + (col4W - copyBtnW) / 2;
            int btnY = rowY + (rowH - copyBtnH) / 2;
            if (mouseX >= btnX && mouseX <= btnX + copyBtnW
                    && mouseY >= btnY && mouseY <= btnY + copyBtnH) {
                ColorPickerState.copyHexToClipboard(String.format("#%06X", rgb & 0xFFFFFF));
                return true;
            }
        }
        return false;
    }

    /**
     * 绘制简单边框（替代 1.20+ 的 drawBorder）。
     */
    private static void drawBorder(MatrixStack matrices, int x, int y, int width, int height, int color) {
        // 上边框
        DrawableHelper.fill(matrices, x, y, x + width, y + 1, color);
        // 下边框
        DrawableHelper.fill(matrices, x, y + height - 1, x + width, y + height, color);
        // 左边框
        DrawableHelper.fill(matrices, x, y, x + 1, y + height, color);
        // 右边框
        DrawableHelper.fill(matrices, x + width - 1, y, x + width, y + height, color);
    }
}