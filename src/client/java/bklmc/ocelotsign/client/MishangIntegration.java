package bklmc.ocelotsign.client;

import bklmc.ocelotsign.mixin_interfaces.ISignEditorExtension;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * MishangUC 告示牌编辑器功能的集成层 (1.19.2 兼容版)
 *
 * @see PatternAndFontOverlay
 */
public final class MishangIntegration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MishangIntegration.class);

    private static final String[] DISPLAY_KEYS = {
            "ocelotsignmod.mishang.json.display",
            "ocelotsignmod.mishang.nbt.display",
            "ocelotsignmod.mishang.rect.display",
            "ocelotsignmod.mishang.texture.display"
    };
    private static final String[] DESC_KEYS = {
            "ocelotsignmod.mishang.json.desc",
            "ocelotsignmod.mishang.nbt.desc",
            "ocelotsignmod.mishang.rect.desc",
            "ocelotsignmod.mishang.texture.desc"
    };
    private static final String[] INSERT_PREFIXES = {"-json ", "-nbt ", "-rect ", "-texture "};

    public static String hoveredInsertText = null;

    private MishangIntegration() {
    }

    public static void clearHovered() {
        hoveredInsertText = null;
    }

    /**
     * 渲染浮层的 MishangUC 集成部分。
     */
    public static void render(MatrixStack matrices, TextRenderer textRenderer, int width, int height,
                              int mouseX, int mouseY, int sidebarWidth) {
        double scrollY = PatternAndFontOverlay.scrollY;
        int scrollWindowStartY = UIConstants.HEADER_HEIGHT + 1;
        int scrollWindowEndY = height - UIConstants.FOOTER_HEIGHT;
        int contentStartY = scrollWindowStartY + 15 - (int) scrollY;
        int mainWidth = width - sidebarWidth;
        int currentY = contentStartY;

        textRenderer.draw(matrices, Text.translatable("ocelotsignmod.mishang.title"),
                sidebarWidth + 24, currentY, UIConstants.COLOR_SECTION_TITLE);
        currentY += 20;

        currentY = renderMethodButtons(matrices, textRenderer, mouseX, mouseY, width, mainWidth, currentY, scrollWindowStartY, scrollWindowEndY);

        currentY += 15;

        textRenderer.draw(matrices, Text.translatable("ocelotsignmod.gui.sections.mishang_patterns"),
                sidebarWidth + 24, currentY, UIConstants.COLOR_SECTION_TITLE);
        textRenderer.draw(matrices, Text.translatable("ocelotsignmod.gui.sections.mishang_patterns.desc"),
                sidebarWidth + 24, currentY + 16, UIConstants.COLOR_DESC_TEXT);
        textRenderer.draw(matrices, Text.translatable("ocelotsignmod.mishang.old_version_note"),
                sidebarWidth + 24, currentY + 30, UIConstants.COLOR_H3_TEXT);

        currentY += 48;

        renderPatternGrid(matrices, textRenderer, mouseX, mouseY, width, mainWidth, sidebarWidth, currentY, scrollWindowStartY, scrollWindowEndY);

        int scrollWindowHeight = scrollWindowEndY - scrollWindowStartY;
        LayoutHelper.renderScrollbar(matrices, sidebarWidth, scrollWindowStartY, width - sidebarWidth,
                scrollWindowHeight, PatternAndFontOverlay.scrollY, PatternAndFontOverlay.maxScrollY,
                scrollWindowHeight, mouseX, mouseY);
    }

    public static boolean handleClick(int mouseX, int mouseY, int width, int height, int sidebarWidth) {
        if (hoveredInsertText != null) {
            insertTextToScreen(hoveredInsertText);
            hoveredInsertText = null;
            return true;
        }
        return false;
    }

    /**
     * 渲染 4 种插入方式的按钮列表。
     */
    private static int renderMethodButtons(MatrixStack matrices, TextRenderer textRenderer, int mouseX, int mouseY,
                                           int width, int mainWidth, int currentY,
                                           int scrollWindowStartY, int scrollWindowEndY) {
        int btnW = 50;
        int btnH = 16;
        int rightMargin = 24 + btnW + 15;
        int availableWidth = mainWidth - 30 - rightMargin;
        Text insertBtnText = Text.translatable("ocelotsignmod.gui.button.insert");

        for (int i = 0; i < DISPLAY_KEYS.length; i++) {
            Text displayText = Text.translatable(DISPLAY_KEYS[i]);
            Text descText = Text.translatable(DESC_KEYS[i]);

            int displayWidth = textRenderer.getWidth(displayText);
            int descWidth = Math.max(0, availableWidth - displayWidth - 10);

            List<OrderedText> wrappedLines = textRenderer.wrapLines(descText, descWidth);
            int rowHeight = Math.max(24, wrappedLines.size() * 10 + 14);

            if (currentY + rowHeight >= scrollWindowStartY && currentY <= scrollWindowEndY) {
                int textStartY = currentY + (rowHeight - wrappedLines.size() * 10) / 2;

                textRenderer.draw(matrices, displayText, UIConstants.SIDEBAR_WIDTH + 30, textStartY,
                        UIConstants.COLOR_BTN_TEXT);

                int descStartX = UIConstants.SIDEBAR_WIDTH + 30 + displayWidth + 10;
                for (int j = 0; j < wrappedLines.size(); j++) {
                    textRenderer.draw(matrices, wrappedLines.get(j), descStartX, textStartY + j * 10,
                            UIConstants.COLOR_DESC_TEXT);
                }

                int btnX = width - 24 - btnW;
                int btnY = currentY + (rowHeight - btnH) / 2;
                boolean hov = LayoutHelper.isMouseInRectStatic(mouseX, mouseY, btnX, btnY, btnW, btnH)
                        && mouseY >= scrollWindowStartY && mouseY <= scrollWindowEndY;

                if (hov) {
                    hoveredInsertText = INSERT_PREFIXES[i];
                }

                DrawableHelper.fill(matrices, btnX, btnY, btnX + btnW, btnY + btnH,
                        hov ? UIConstants.COLOR_INSERT_BTN_BG_HOVER : UIConstants.COLOR_INSERT_BTN_BG);
                drawBorder(matrices, btnX, btnY, btnW, btnH, UIConstants.COLOR_INSERT_BTN_BORDER);
                textRenderer.draw(matrices, insertBtnText, btnX + (btnW - textRenderer.getWidth(insertBtnText)) / 2,
                        btnY + 3, UIConstants.COLOR_BTN_TEXT);
            }

            currentY += rowHeight + 8;
        }
        return currentY;
    }

    /**
     * 渲染 MishangUC 图案网格。
     */
    private static void renderPatternGrid(MatrixStack matrices, TextRenderer textRenderer, int mouseX, int mouseY,
                                          int width, int mainWidth, int sidebarWidth, int startY,
                                          int scrollWindowStartY, int scrollWindowEndY) {
        int cols = Math.max(1, (mainWidth - 48) / (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_X));
        int gridWidth = cols * UIConstants.ITEM_SIZE + (cols - 1) * UIConstants.ITEM_PADDING_X;
        int gridStartX = sidebarWidth + (mainWidth - gridWidth) / 2;

        Text insertBtnText = Text.translatable("ocelotsignmod.gui.button.insert");

        for (int i = 0; i < PatternAndFontOverlay.MISHANG_PATTERNS.size(); i++) {
            int row = i / cols;
            int col = i % cols;
            int px = gridStartX + col * (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_X);
            int py = startY + row * (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_Y);

            if (py + UIConstants.ITEM_SIZE + 20 < scrollWindowStartY || py > scrollWindowEndY) continue;

            PatternAndFontOverlay.MishangPatternItem item = PatternAndFontOverlay.MISHANG_PATTERNS.get(i);
            DrawableHelper.fill(matrices, px, py, px + UIConstants.ITEM_SIZE, py + UIConstants.ITEM_SIZE, UIConstants.COLOR_ITEM_BG);
            drawBorder(matrices, px, py, UIConstants.ITEM_SIZE, UIConstants.ITEM_SIZE, UIConstants.COLOR_ITEM_BORDER);
            RenderSystem.setShaderTexture(0, item.textureId);
            DrawableHelper.drawTexture(matrices, px + 2, py + 2, 0.0F, 0.0F,
                    UIConstants.ITEM_SIZE - 4, UIConstants.ITEM_SIZE - 4,
                    UIConstants.ITEM_SIZE - 4, UIConstants.ITEM_SIZE - 4);

            int pInsertBtnY = py + UIConstants.ITEM_SIZE + 1;
            boolean pIsHover = LayoutHelper.isMouseInRectStatic(mouseX, mouseY, px, pInsertBtnY,
                    UIConstants.ITEM_SIZE, 12)
                    && mouseY >= scrollWindowStartY && mouseY <= scrollWindowEndY;

            if (pIsHover) {
                hoveredInsertText = item.insertCode;
            }

            DrawableHelper.fill(matrices, px, pInsertBtnY, px + UIConstants.ITEM_SIZE, pInsertBtnY + 12,
                    pIsHover ? UIConstants.COLOR_INSERT_BTN_BG_HOVER : UIConstants.COLOR_INSERT_BTN_BG);
            drawBorder(matrices, px, pInsertBtnY, UIConstants.ITEM_SIZE, 12, UIConstants.COLOR_INSERT_BTN_BORDER);
            textRenderer.draw(matrices, insertBtnText,
                    px + (UIConstants.ITEM_SIZE - textRenderer.getWidth(insertBtnText)) / 2,
                    pInsertBtnY + 2, UIConstants.COLOR_BTN_TEXT);
        }
    }

    private static void insertTextureToScreen(Identifier identifier) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen instanceof ISignEditorExtension extension) {
            extension.ocelotsign$insertTexture(identifier);
            PatternAndFontOverlay.isVisible = false;
        }
    }

    private static void insertTextToScreen(String text) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen instanceof ISignEditorExtension extension) {
            extension.ocelotsign$insertText(text);
            PatternAndFontOverlay.isVisible = false;
        }
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