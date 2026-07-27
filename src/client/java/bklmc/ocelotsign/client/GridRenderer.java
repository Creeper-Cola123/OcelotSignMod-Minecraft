package bklmc.ocelotsign.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

/**
 * 网格渲染器，处理图案和字体的网格展示 (1.19.2 兼容版)
 *
 * @see PatternAndFontOverlay
 */
public final class GridRenderer {
    private GridRenderer() {
    }

    /**
     * 渲染白名单模式的图案网格。
     */
    public static int renderWhitelistGrid(MatrixStack matrices, TextRenderer textRenderer,
                                          double mouseX, double mouseY,
                                          int mainWidth, int scrollWindowStartY, int scrollWindowEndY,
                                          List<PatternAndFontOverlay.WhitelistPatternItem> items,
                                          int startX, int startY) {
        if (items.isEmpty()) {
            textRenderer.draw(matrices, Text.translatable("ocelotsignmod.gui.no_images"),
                    startX, startY, 0xFFAAAAAA);
            return 30;
        }

        int cols = Math.max(1, (mainWidth - 48) / (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_X));
        int rows = (int) Math.ceil((double) items.size() / cols);
        int gridWidth = cols * UIConstants.ITEM_SIZE + (cols - 1) * UIConstants.ITEM_PADDING_X;
        int gridStartX = UIConstants.SIDEBAR_WIDTH + (mainWidth - gridWidth) / 2;

        Text insertBtnText = Text.translatable("ocelotsignmod.gui.button.insert");

        for (int i = 0; i < items.size(); i++) {
            int row = i / cols;
            int col = i % cols;
            int x = gridStartX + col * (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_X);
            int y = startY + row * (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_Y);

            if (y + UIConstants.ITEM_SIZE + 20 < scrollWindowStartY || y > scrollWindowEndY) continue;

            PatternAndFontOverlay.WhitelistPatternItem item = items.get(i);
            renderTextureItem(matrices, x, y, item.textureId);

            int pInsertBtnY = y + UIConstants.ITEM_SIZE + 1;
            boolean isHover = LayoutHelper.isMouseInRect(mouseX, mouseY, x, pInsertBtnY, UIConstants.ITEM_SIZE, 12);
            int bgColor = isHover ? UIConstants.COLOR_INSERT_BTN_BG_HOVER : UIConstants.COLOR_INSERT_BTN_BG;
            int borderColor = isHover ? UIConstants.COLOR_INSERT_BTN_BORDER : 0xFFB0B0B0;
            DrawableHelper.fill(matrices, x, pInsertBtnY, x + UIConstants.ITEM_SIZE, pInsertBtnY + 12, bgColor);
            drawBorder(matrices, x, pInsertBtnY, UIConstants.ITEM_SIZE, 12, borderColor);
            textRenderer.draw(matrices, insertBtnText, x + (UIConstants.ITEM_SIZE - textRenderer.getWidth(insertBtnText)) / 2, pInsertBtnY + 2, UIConstants.COLOR_BTN_TEXT);
        }
        return rows * (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_Y) + 20;
    }

    /**
     * 渲染缓存纹理模式的图案网格。
     */
    public static int renderCachedTextureGrid(MatrixStack matrices, TextRenderer textRenderer,
                                              double mouseX, double mouseY,
                                              int mainWidth, int scrollWindowStartY, int scrollWindowEndY,
                                              List<Identifier> textures,
                                              int startX, int startY) {
        if (textures == null || textures.isEmpty()) {
            textRenderer.draw(matrices, Text.translatable("ocelotsignmod.gui.no_images"),
                    startX, startY, 0xFFAAAAAA);
            return 30;
        }

        int cols = Math.max(1, (mainWidth - 48) / (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_X));
        int rows = (int) Math.ceil((double) textures.size() / cols);
        int gridWidth = cols * UIConstants.ITEM_SIZE + (cols - 1) * UIConstants.ITEM_PADDING_X;
        int gridStartX = UIConstants.SIDEBAR_WIDTH + (mainWidth - gridWidth) / 2;

        Text insertBtnText = Text.translatable("ocelotsignmod.gui.button.insert");

        for (int i = 0; i < textures.size(); i++) {
            int row = i / cols;
            int col = i % cols;
            int x = gridStartX + col * (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_X);
            int y = startY + row * (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_Y);

            if (y + UIConstants.ITEM_SIZE + 20 < scrollWindowStartY || y > scrollWindowEndY) continue;

            renderTextureItem(matrices, x, y, textures.get(i));

            int pInsertBtnY = y + UIConstants.ITEM_SIZE + 1;
            boolean isHover = LayoutHelper.isMouseInRect(mouseX, mouseY, x, pInsertBtnY, UIConstants.ITEM_SIZE, 12);
            int bgColor = isHover ? UIConstants.COLOR_INSERT_BTN_BG_HOVER : UIConstants.COLOR_INSERT_BTN_BG;
            int borderColor = isHover ? UIConstants.COLOR_INSERT_BTN_BORDER : 0xFFB0B0B0;
            DrawableHelper.fill(matrices, x, pInsertBtnY, x + UIConstants.ITEM_SIZE, pInsertBtnY + 12, bgColor);
            drawBorder(matrices, x, pInsertBtnY, UIConstants.ITEM_SIZE, 12, borderColor);
            textRenderer.draw(matrices, insertBtnText, x + (UIConstants.ITEM_SIZE - textRenderer.getWidth(insertBtnText)) / 2, pInsertBtnY + 2, UIConstants.COLOR_BTN_TEXT);
        }
        return rows * (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_Y);
    }

    /**
     * 渲染单个纹理条目（含背景与边框）。
     */
    private static void renderTextureItem(MatrixStack matrices, int x, int y, Identifier textureId) {
        int borderSize = 2;
        DrawableHelper.fill(matrices, x, y, x + UIConstants.ITEM_SIZE, y + UIConstants.ITEM_SIZE, UIConstants.COLOR_ITEM_BG);
        drawBorder(matrices, x, y, UIConstants.ITEM_SIZE, UIConstants.ITEM_SIZE, UIConstants.COLOR_ITEM_BORDER);
        int innerSize = UIConstants.ITEM_SIZE - borderSize * 2;

        // 1.19.2: 先绑定纹理
        RenderSystem.setShaderTexture(0, textureId);
        DrawableHelper.drawTexture(matrices, x + borderSize, y + borderSize, 0, 0, innerSize, innerSize, innerSize, innerSize);
    }

    /**
     * 渲染字体列表。
     */
    public static int renderFontList(MatrixStack matrices, TextRenderer textRenderer,
                                     double mouseX, double mouseY,
                                     int mainWidth, int scrollWindowStartY, int scrollWindowEndY,
                                     List<PatternAndFontOverlay.FontItem> fontItems,
                                     PatternAndFontOverlay.H4Section section,
                                     int startX, int startY) {
        if (fontItems.isEmpty()) {
            textRenderer.draw(matrices, Text.translatable("ocelotsignmod.gui.no_fonts"),
                    startX, startY, 0xFFAAAAAA);
            return 30;
        }

        int fontItemWidth = mainWidth - UIConstants.FONT_ITEM_WIDTH_OFFSET;
        int fontStartX = UIConstants.SIDEBAR_WIDTH + 30;

        for (int i = 0; i < fontItems.size(); i++) {
            PatternAndFontOverlay.FontItem fontItem = fontItems.get(i);
            int y = startY + i * UIConstants.FONT_ITEM_HEIGHT;

            if (y + UIConstants.FONT_ITEM_HEIGHT < scrollWindowStartY || y > scrollWindowEndY) continue;

            boolean isHover = LayoutHelper.isMouseInRect(mouseX, mouseY, fontStartX, y, fontItemWidth, UIConstants.FONT_ITEM_HEIGHT);
            int fontItemVisualHeight = UIConstants.FONT_ITEM_HEIGHT - 4;

            DrawableHelper.fill(matrices, fontStartX, y, fontStartX + fontItemWidth, y + fontItemVisualHeight,
                    isHover ? 0xFFE0E0E0 : 0xFFF0F0F0);
            drawBorder(matrices, fontStartX, y, fontItemWidth, fontItemVisualHeight,
                    isHover ? 0xFFAAAAAA : 0xFFD0D0D0);
            textRenderer.draw(matrices, fontItem.displayName.getString(), fontStartX + 10, y + 6,
                    UIConstants.COLOR_BTN_TEXT);

            int insertBtnX = fontStartX + fontItemWidth - UIConstants.INSERT_BUTTON_WIDTH - 10;
            boolean isBtnHover = LayoutHelper.isMouseInRect(mouseX, mouseY, insertBtnX, y + 2, UIConstants.INSERT_BUTTON_WIDTH, 16);
            int btnBgColor = isBtnHover ? UIConstants.COLOR_INSERT_BTN_BG_HOVER : UIConstants.COLOR_INSERT_BTN_BG;
            int btnBorderColor = isBtnHover ? UIConstants.COLOR_INSERT_BTN_BORDER : 0xFFB0B0B0;
            DrawableHelper.fill(matrices, insertBtnX, y + 2, insertBtnX + UIConstants.INSERT_BUTTON_WIDTH, y + 18, btnBgColor);
            drawBorder(matrices, insertBtnX, y + 2, UIConstants.INSERT_BUTTON_WIDTH, 16, btnBorderColor);

            Text insertText = Text.translatable("ocelotsignmod.gui.button.insert");
            int itw = textRenderer.getWidth(insertText);
            textRenderer.draw(matrices, insertText, insertBtnX + (UIConstants.INSERT_BUTTON_WIDTH - itw) / 2, y + 5,
                    UIConstants.COLOR_BTN_TEXT);
        }
        return fontItems.size() * UIConstants.FONT_ITEM_HEIGHT;
    }

    /**
     * 获取图案网格点击位置的索引。
     */
    public static int getGridItemIndex(double mouseX, double mouseY, double mouseYScrollStart,
                                       int mainWidth, int startY, int scrollWindowStartY, int scrollWindowEndY,
                                       int itemCount) {
        if (mouseY < scrollWindowStartY || mouseY > scrollWindowEndY) return -1;

        int cols = Math.max(1, (mainWidth - 48) / (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_X));
        int gridWidth = cols * UIConstants.ITEM_SIZE + (cols - 1) * UIConstants.ITEM_PADDING_X;
        int gridStartX = UIConstants.SIDEBAR_WIDTH + (mainWidth - gridWidth) / 2;

        int relY = (int) mouseY - startY;
        int relX = (int) mouseX - gridStartX;

        if (relX < 0 || relY < 0) return -1;

        int col = relX / (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_X);
        int row = relY / (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_Y);

        if (col >= cols) return -1;

        int index = row * cols + col;
        if (index >= itemCount) return -1;

        int itemX = gridStartX + col * (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_X);
        int itemY = startY + row * (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_Y);
        int insertBtnY = itemY + UIConstants.ITEM_SIZE + 1;

        if (LayoutHelper.isMouseInRect(mouseX, mouseY, itemX, insertBtnY, UIConstants.ITEM_SIZE, 12)) {
            return index;
        }
        return -1;
    }

    /**
     * 获取字体列表点击位置的索引。
     */
    public static int getFontItemIndex(double mouseX, double mouseY, int mainWidth,
                                       int startY, int scrollWindowStartY, int scrollWindowEndY,
                                       int itemCount) {
        if (mouseY < scrollWindowStartY || mouseY > scrollWindowEndY) return -1;

        int fontItemWidth = mainWidth - UIConstants.FONT_ITEM_WIDTH_OFFSET;
        int fontStartX = UIConstants.SIDEBAR_WIDTH + 30;
        int insertBtnX = fontStartX + fontItemWidth - UIConstants.INSERT_BUTTON_WIDTH - 10;

        int relY = (int) mouseY - startY;
        int index = relY / UIConstants.FONT_ITEM_HEIGHT;

        if (index < 0 || index >= itemCount) return -1;

        int itemY = startY + index * UIConstants.FONT_ITEM_HEIGHT;
        if (LayoutHelper.isMouseInRect(mouseX, mouseY, insertBtnX, itemY + 2, UIConstants.INSERT_BUTTON_WIDTH, 16)) {
            return index;
        }
        return -1;
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