package bklmc.ocelotsign.client;

import bklmc.ocelotsign.mixin_interfaces.ISignEditorExtension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * MishangUC 告示牌编辑器功能的集成层
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
    /** 4 种插入方式的描述国际化 key。 */
    private static final String[] DESC_KEYS = {
            "ocelotsignmod.mishang.json.desc",
            "ocelotsignmod.mishang.nbt.desc",
            "ocelotsignmod.mishang.rect.desc",
            "ocelotsignmod.mishang.texture.desc"
    };
    /** 4 种插入方式的命令前缀。 */
    private static final String[] INSERT_PREFIXES = {"-json ", "-nbt ", "-rect ", "-texture "};

    /** 当前渲染帧的悬停插入文本。 */
    public static String hoveredInsertText = null;

    private MishangIntegration() {
    }

    /**
     * 清除上一帧的悬停插入文本状态。
     */
    public static void clearHovered() {
        hoveredInsertText = null;
    }

    /**
     * 渲染浮层的 MishangUC 集成部分。
     *
     * @param context 绘制上下文
     * @param textRenderer 文本渲染器
     * @param width 屏幕宽度
     * @param height 屏幕高度
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param sidebarWidth 侧边栏宽度
     */
    public static void render(DrawContext context, TextRenderer textRenderer, int width, int height,
                             int mouseX, int mouseY, int sidebarWidth) {
        double scrollY = PatternAndFontOverlay.scrollY;
        int scrollWindowStartY = UIConstants.HEADER_HEIGHT + 1;
        int scrollWindowEndY = height - UIConstants.FOOTER_HEIGHT;
        int contentStartY = scrollWindowStartY + 15 - (int) scrollY;
        int mainWidth = width - sidebarWidth;
        int currentY = contentStartY;

        context.drawText(textRenderer, Text.translatable("ocelotsignmod.mishang.title"),
                sidebarWidth + 24, currentY, UIConstants.COLOR_SECTION_TITLE, false);
        currentY += 20;

        currentY = renderMethodButtons(context, textRenderer, mouseX, mouseY, width, mainWidth, currentY, scrollWindowStartY, scrollWindowEndY);

        currentY += 15;

        context.drawText(textRenderer, Text.translatable("ocelotsignmod.gui.sections.mishang_patterns"),
                sidebarWidth + 24, currentY, UIConstants.COLOR_SECTION_TITLE, false);
        context.drawText(textRenderer, Text.translatable("ocelotsignmod.gui.sections.mishang_patterns.desc"),
                sidebarWidth + 24, currentY + 16, UIConstants.COLOR_DESC_TEXT, false);
        context.drawText(textRenderer, Text.translatable("ocelotsignmod.mishang.old_version_note"),
                sidebarWidth + 24, currentY + 30, UIConstants.COLOR_H3_TEXT, false);

        currentY += 48;

        renderPatternGrid(context, textRenderer, mouseX, mouseY, width, mainWidth, sidebarWidth, currentY, scrollWindowStartY, scrollWindowEndY);

        int scrollWindowHeight = scrollWindowEndY - scrollWindowStartY;
        LayoutHelper.renderScrollbar(context, sidebarWidth, scrollWindowStartY, width - sidebarWidth,
                scrollWindowHeight, PatternAndFontOverlay.scrollY, PatternAndFontOverlay.maxScrollY,
                scrollWindowHeight, mouseX, mouseY);
    }

    /**
     * 处理 MishangUC 集成元素的点击事件。
     *
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param width 屏幕宽度
     * @param height 屏幕高度
     * @param sidebarWidth 侧边栏宽度
     * @return 若点击被消费返回 {@code true}
     */
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
    private static int renderMethodButtons(DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY,
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

                context.drawText(textRenderer, displayText, UIConstants.SIDEBAR_WIDTH + 30, textStartY,
                        UIConstants.COLOR_BTN_TEXT, false);

                int descStartX = UIConstants.SIDEBAR_WIDTH + 30 + displayWidth + 10;
                for (int j = 0; j < wrappedLines.size(); j++) {
                    context.drawText(textRenderer, wrappedLines.get(j), descStartX, textStartY + j * 10,
                            UIConstants.COLOR_DESC_TEXT, false);
                }

                int btnX = width - 24 - btnW;
                int btnY = currentY + (rowHeight - btnH) / 2;
                boolean hov = LayoutHelper.isMouseInRectStatic(mouseX, mouseY, btnX, btnY, btnW, btnH)
                        && mouseY >= scrollWindowStartY && mouseY <= scrollWindowEndY;

                if (hov) {
                    hoveredInsertText = INSERT_PREFIXES[i];
                }

                context.fill(btnX, btnY, btnX + btnW, btnY + btnH,
                        hov ? UIConstants.COLOR_INSERT_BTN_BG_HOVER : UIConstants.COLOR_INSERT_BTN_BG);
                context.drawBorder(btnX, btnY, btnW, btnH, UIConstants.COLOR_INSERT_BTN_BORDER);
                context.drawText(textRenderer, insertBtnText, btnX + (btnW - textRenderer.getWidth(insertBtnText)) / 2,
                        btnY + 3, UIConstants.COLOR_BTN_TEXT, false);
            }

            currentY += rowHeight + 8;
        }
        return currentY;
    }

    /**
     * 渲染 MishangUC 图案网格。
     */
    private static void renderPatternGrid(DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY,
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
            context.fill(px, py, px + UIConstants.ITEM_SIZE, py + UIConstants.ITEM_SIZE, UIConstants.COLOR_ITEM_BG);
            context.drawBorder(px, py, UIConstants.ITEM_SIZE, UIConstants.ITEM_SIZE, UIConstants.COLOR_ITEM_BORDER);
            context.drawTexture(RenderLayer::getGuiTextured, item.textureId, px + 2, py + 2, 0.0F, 0.0F,
                    UIConstants.ITEM_SIZE - 4, UIConstants.ITEM_SIZE - 4,
                    UIConstants.ITEM_SIZE - 4, UIConstants.ITEM_SIZE - 4);

            int pInsertBtnY = py + UIConstants.ITEM_SIZE + 1;
            boolean pIsHover = LayoutHelper.isMouseInRectStatic(mouseX, mouseY, px, pInsertBtnY,
                    UIConstants.ITEM_SIZE, 12)
                    && mouseY >= scrollWindowStartY && mouseY <= scrollWindowEndY;

            if (pIsHover) {
                hoveredInsertText = item.insertCode;
            }

            context.fill(px, pInsertBtnY, px + UIConstants.ITEM_SIZE, pInsertBtnY + 12,
                    pIsHover ? UIConstants.COLOR_INSERT_BTN_BG_HOVER : UIConstants.COLOR_INSERT_BTN_BG);
            context.drawBorder(px, pInsertBtnY, UIConstants.ITEM_SIZE, 12, UIConstants.COLOR_INSERT_BTN_BORDER);
            context.drawText(textRenderer, insertBtnText,
                    px + (UIConstants.ITEM_SIZE - textRenderer.getWidth(insertBtnText)) / 2,
                    pInsertBtnY + 2, UIConstants.COLOR_BTN_TEXT, false);
        }
    }

    /**
     * 向当前告示牌编辑界面插入纹理。
     */
    private static void insertTextureToScreen(Identifier identifier) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen instanceof ISignEditorExtension extension) {
            extension.ocelotsign$insertTexture(identifier);
            PatternAndFontOverlay.isVisible = false;
        }
    }

    /**
     * 向当前告示牌编辑界面插入文本。
     */
    private static void insertTextToScreen(String text) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen instanceof ISignEditorExtension extension) {
            extension.ocelotsign$insertText(text);
            PatternAndFontOverlay.isVisible = false;
        }
    }
}
