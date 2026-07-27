package bklmc.ocelotsign.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.gui.DrawableHelper;

/**
 * 鼠标事件处理器
 *
 * @see PatternAndFontOverlay
 * @see GridRenderer
 */
public final class MouseEventHandler {
    private MouseEventHandler() {
    }

    /**
     * 处理鼠标点击事件。
     *
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param button 鼠标按钮编号
     * @return 若点击被消费返回 {@code true}
     */
    public static boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!PatternAndFontOverlay.isVisible || button != 0) return false;

        int width = LayoutHelper.getScreenWidth();
        int height = LayoutHelper.getScreenHeight();
        int headerHeight = UIConstants.HEADER_HEIGHT;
        int scrollWindowStartY = headerHeight + 1;
        int scrollWindowEndY = height - UIConstants.FOOTER_HEIGHT;
        int scrollWindowHeight = scrollWindowEndY - scrollWindowStartY;
        int mainWidth = width - UIConstants.SIDEBAR_WIDTH;

        // 处理主区域滚动条拖动
        if (mouseX > UIConstants.SIDEBAR_WIDTH && PatternAndFontOverlay.maxScrollY > 20) {
            if (handleMainScrollbarClick(mouseX, mouseY, width, scrollWindowStartY, scrollWindowHeight)) {
                return true;
            }
        }

        // 处理侧边栏滚动条拖动
        if (mouseX <= UIConstants.SIDEBAR_WIDTH && PatternAndFontOverlay.maxSidebarScrollY > 20) {
            if (handleSidebarScrollbarClick(mouseX, mouseY, height)) {
                return true;
            }
        }

        // 处理侧边栏点击
        if (mouseX <= UIConstants.SIDEBAR_WIDTH) {
            return handleSidebarClick(mouseX, mouseY);
        }

        // 处理主区域内容点击
        if (mouseY >= scrollWindowStartY && mouseY <= scrollWindowEndY) {
            return handleMainContentClick(mouseX, mouseY, width, height, mainWidth, scrollWindowStartY, scrollWindowEndY);
        }

        // 处理返回按钮
        int returnBtnX = UIConstants.SIDEBAR_WIDTH + (mainWidth - UIConstants.RETURN_BUTTON_WIDTH) / 2;
        if (LayoutHelper.isMouseInRect(mouseX, mouseY, returnBtnX, height - 35, UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT)) {
            PatternAndFontOverlay.isVisible = false;
            return true;
        }

        return false;
    }

    /**
     * 处理主区域滚动条点击。
     *
     * @return 若点击被消费返回 {@code true}
     */
    private static boolean handleMainScrollbarClick(double mouseX, double mouseY, int width, int scrollWindowStartY, int scrollWindowHeight) {
        int scrollbarX = width - UIConstants.SCROLLBAR_WIDTH - 2;
        int scrollbarY = scrollWindowStartY;

        float trackRatio = (float) scrollWindowHeight / (float) (scrollWindowHeight + PatternAndFontOverlay.maxScrollY);
        int thumbHeight = Math.max(UIConstants.SCROLLBAR_MIN_HEIGHT, (int) (scrollWindowHeight * trackRatio));
        float scrollRatio = (float) PatternAndFontOverlay.scrollY / (float) PatternAndFontOverlay.maxScrollY;
        int thumbY = scrollbarY + (int) ((scrollWindowHeight - thumbHeight) * scrollRatio);

        if (LayoutHelper.isMouseInRect(mouseX, mouseY, scrollbarX, scrollbarY, UIConstants.SCROLLBAR_WIDTH, scrollWindowHeight)) {
            PatternAndFontOverlay.isDraggingMainScrollbar = true;
            PatternAndFontOverlay.dragStartMouseY = mouseY;
            PatternAndFontOverlay.dragStartScrollY = PatternAndFontOverlay.scrollY;

            if (!LayoutHelper.isMouseInRect(mouseX, mouseY, scrollbarX, thumbY, UIConstants.SCROLLBAR_WIDTH, thumbHeight)) {
                float newScrollRatio = (float) (mouseY - scrollbarY - thumbHeight / 2.0f) / (float) (scrollWindowHeight - thumbHeight);
                PatternAndFontOverlay.scrollY = MathHelper.clamp(newScrollRatio * PatternAndFontOverlay.maxScrollY, 0, PatternAndFontOverlay.maxScrollY);
                PatternAndFontOverlay.dragStartScrollY = PatternAndFontOverlay.scrollY;
            }
            return true;
        }
        return false;
    }

    /**
     * 处理侧边栏滚动条点击。
     *
     * @return 若点击被消费返回 {@code true}
     */
    private static boolean handleSidebarScrollbarClick(double mouseX, double mouseY, int height) {
        int sidebarHeaderHeight = UIConstants.HEADER_HEIGHT;
        int sidebarScrollWindowHeight = height - sidebarHeaderHeight - UIConstants.FOOTER_HEIGHT;
        int scrollbarX = UIConstants.SIDEBAR_WIDTH - UIConstants.SCROLLBAR_WIDTH - 2;
        int scrollbarY = sidebarHeaderHeight;
        int scrollbarHeight = sidebarScrollWindowHeight;

        float trackRatio = (float) sidebarScrollWindowHeight / (float) (sidebarScrollWindowHeight + PatternAndFontOverlay.maxSidebarScrollY);
        int thumbHeight = Math.max(UIConstants.SCROLLBAR_MIN_HEIGHT, (int) (scrollbarHeight * trackRatio));
        float scrollRatio = (float) PatternAndFontOverlay.sidebarScrollY / (float) PatternAndFontOverlay.maxSidebarScrollY;
        int thumbY = scrollbarY + (int) ((scrollbarHeight - thumbHeight) * scrollRatio);

        if (LayoutHelper.isMouseInRect(mouseX, mouseY, scrollbarX, scrollbarY, UIConstants.SCROLLBAR_WIDTH, scrollbarHeight)) {
            PatternAndFontOverlay.isDraggingSidebarScrollbar = true;
            PatternAndFontOverlay.dragStartMouseY = mouseY;
            PatternAndFontOverlay.dragStartSidebarScrollY = PatternAndFontOverlay.sidebarScrollY;

            if (!LayoutHelper.isMouseInRect(mouseX, mouseY, scrollbarX, thumbY, UIConstants.SCROLLBAR_WIDTH, thumbHeight)) {
                float newScrollRatio = (float) (mouseY - scrollbarY - thumbHeight / 2.0f) / (float) (scrollbarHeight - thumbHeight);
                PatternAndFontOverlay.sidebarScrollY = MathHelper.clamp(newScrollRatio * PatternAndFontOverlay.maxSidebarScrollY, 0, PatternAndFontOverlay.maxSidebarScrollY);
                PatternAndFontOverlay.dragStartSidebarScrollY = PatternAndFontOverlay.sidebarScrollY;
            }
            return true;
        }
        return false;
    }

    /**
     * 处理侧边栏点击事件。
     *
     * @return 若点击被消费返回 {@code true}
     */
    private static boolean handleSidebarClick(double mouseX, double mouseY) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int sidebarHeaderHeight = UIConstants.HEADER_HEIGHT;
        int currentY = sidebarHeaderHeight + 12 - (int) PatternAndFontOverlay.sidebarScrollY;

        // 文档列表
        int docH = LayoutHelper.getSidebarTopItemHeight(Text.translatable("ocelotsignmod.gui.sidebar.docs"), textRenderer);
        if (LayoutHelper.isMouseInRect(mouseX, mouseY, 0, currentY, UIConstants.SIDEBAR_WIDTH, docH)) {
            PatternAndFontOverlay.selectSidebarTop(PatternAndFontOverlay.SIDEBAR_TOP_DOCS);
            return true;
        }
        currentY += docH;

        // 鸣谢与模组声明
        int ackH = LayoutHelper.getSidebarTopItemHeight(Text.translatable("ocelotsignmod.gui.sidebar.ack"), textRenderer);
        if (LayoutHelper.isMouseInRect(mouseX, mouseY, 0, currentY, UIConstants.SIDEBAR_WIDTH, ackH)) {
            PatternAndFontOverlay.selectSidebarTop(PatternAndFontOverlay.SIDEBAR_TOP_ACK);
            return true;
        }
        currentY += ackH;

        // 道路交通颜色色表
        int paletteH = LayoutHelper.getSidebarTopItemHeight(Text.translatable("ocelotsignmod.gui.sidebar.color_palette"), textRenderer);
        if (LayoutHelper.isMouseInRect(mouseX, mouseY, 0, currentY, UIConstants.SIDEBAR_WIDTH, paletteH)) {
            PatternAndFontOverlay.selectSidebarTop(PatternAndFontOverlay.SIDEBAR_TOP_PALETTE);
            return true;
        }
        currentY += paletteH;

        // 颜色选择器
        int pickerH = LayoutHelper.getSidebarTopItemHeight(Text.translatable("ocelotsignmod.gui.sidebar.color_picker"), textRenderer);
        if (LayoutHelper.isMouseInRect(mouseX, mouseY, 0, currentY, UIConstants.SIDEBAR_WIDTH, pickerH)) {
            PatternAndFontOverlay.selectSidebarTop(PatternAndFontOverlay.SIDEBAR_TOP_PICKER);
            return true;
        }
        currentY += pickerH;

        // H2 分类
        for (PatternAndFontOverlay.H2Category h2 : PatternAndFontOverlay.REGISTRY) {
            String prefix = h2.isExpanded ? "[-] " : "[+] ";
            int itemHeight = LayoutHelper.getCategoryHeight(h2.title, prefix, 12, textRenderer);

            if (LayoutHelper.isMouseInRect(mouseX, mouseY, 0, currentY, UIConstants.SIDEBAR_WIDTH, itemHeight)) {
                PatternAndFontOverlay.clearSidebarTop();
                h2.isExpanded = !h2.isExpanded;
                return true;
            }
            currentY += itemHeight;

            if (h2.isExpanded) {
                for (PatternAndFontOverlay.H3Category h3 : h2.subCategories) {
                    Integer result = LayoutHelper.handleH3CategoryClick(mouseX, mouseY, h2, h3, 24, currentY, textRenderer);
                    if (result != null) {
                        return true;
                    }
                    currentY += LayoutHelper.calculateH3Height(h3, 24, textRenderer);
                }
                // 处理 H3 下的 H4Section 样式项
                for (PatternAndFontOverlay.H3Category h3 : h2.subCategories) {
                    for (PatternAndFontOverlay.H4Section sec : h3.sections) {
                        if (sec.useStyles) {
                            Integer result = LayoutHelper.handleH4SectionClick(mouseX, mouseY, h2, h3, sec, 24, currentY, textRenderer);
                            if (result != null) {
                                return true;
                            }
                            currentY += LayoutHelper.calculateH4Height(sec, 24, textRenderer);
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 处理主区域内容点击事件。
     *
     * @return 若点击被消费返回 {@code true}
     */
    private static boolean handleMainContentClick(double mouseX, double mouseY, int width, int height,
                                                  int mainWidth, int scrollWindowStartY, int scrollWindowEndY) {
        double scrollY = PatternAndFontOverlay.scrollY;
        int contentStartY = scrollWindowStartY + 15 - (int) scrollY;

        if (PatternAndFontOverlay.isDocumentListSelected) {
            return handleDocListClick(mouseX, mouseY, mainWidth, contentStartY, scrollWindowStartY, scrollWindowEndY);
        } else if (PatternAndFontOverlay.isAcknowledgmentSelected) {
            return handleDocListClick(mouseX, mouseY, mainWidth, contentStartY, scrollWindowStartY, scrollWindowEndY);
        } else if (PatternAndFontOverlay.isColorPaletteSelected) {
            return PatternAndFontOverlay.handleColorPaletteClick(mouseX, mouseY, mainWidth, contentStartY, scrollWindowStartY, scrollWindowEndY);
        } else if (PatternAndFontOverlay.isColorPickerSelected) {
            return PatternAndFontOverlay.handleColorPickerClick(mouseX, mouseY, mainWidth, contentStartY, scrollWindowStartY, scrollWindowEndY);
        } else if (PatternAndFontOverlay.selectedH3 != null) {
            String mishangKey = Text.translatable("ocelotsignmod.gui.categories.mishang_builtin").getString();
            if (PatternAndFontOverlay.selectedH3.title.getString().equals(mishangKey)) {
                if (PatternAndFontOverlay.handleMishangClick((int) mouseX, (int) mouseY, width, height, (int) scrollY, UIConstants.SIDEBAR_WIDTH)) {
                    return true;
                }
            }
            return handleSectionClick(mouseX, mouseY, width, height, mainWidth, contentStartY, scrollWindowStartY, scrollWindowEndY, scrollY);
        }
        return false;
    }

    /**
     * 处理文档列表链接点击。
     *
     * @return 若点击被消费返回 {@code true}
     */
    private static boolean handleDocListClick(double mouseX, double mouseY, int mainWidth,
                                              int contentStartY, int scrollWindowStartY, int scrollWindowEndY) {
        // 直接读取上一次渲染时记录的精确悬停 URL，点击判定与视觉像素永远完美对齐
        String hoveredUrl = PatternAndFontOverlay.getLastHoveredUrl();
        if (hoveredUrl != null) {
            Util.getOperatingSystem().open(hoveredUrl);
            return true;
        }
        // 没有悬停在任何链接上，不消费点击事件，让点击穿透到其他元素
        return false;
    }

    /**
     * 处理分区内容点击事件。
     *
     * @return 若点击被消费返回 {@code true}
     */
    private static boolean handleSectionClick(double mouseX, double mouseY, int width, int height,
                                              int mainWidth, int contentStartY, int scrollWindowStartY,
                                              int scrollWindowEndY, double scrollY) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        PatternAndFontOverlay.H3Category selectedH3 = PatternAndFontOverlay.selectedH3;

        int currentContentY = contentStartY;

        if (selectedH3.headerText != null) {
            int lines = textRenderer.wrapLines(selectedH3.headerText, mainWidth - 48).size();
            currentContentY += lines * 12 + 16 + 15;
        }

        // 字体渲染警告框（与渲染逻辑一致：仅在最顶部出现一次，与 headerText 同级）
        if (PatternAndFontOverlay.hasAnyFontSection(selectedH3)) {
            int boxWidth = mainWidth - 40;
            int paddingY = 8;
            int titleHeight = 14;
            int gap = 4;
            int lineHeight = 12;
            List<OrderedText> warningLines = textRenderer.wrapLines(
                    Text.translatable("ocelotsignmod.gui.sections.font_rendering_warning"), boxWidth - 16);
            // 警告框前 8px 间距 + 警告框高度 + renderWarningBox 返回的尾部 8px 间距
            currentContentY += 8 + paddingY + titleHeight + gap + warningLines.size() * lineHeight + paddingY + 8;
        }

        String defaultFontsKey = Text.translatable("ocelotsignmod.gui.sections.default_fonts").getString();
        String customFontsKey = Text.translatable("ocelotsignmod.gui.sections.custom_fonts").getString();

        for (PatternAndFontOverlay.H4Section section : selectedH3.sections) {
            // 获取当前活动的样式 section
            PatternAndFontOverlay.H4Section effectiveSection = section;
            if (section.useStyles && !section.subSections.isEmpty()
                    && section.activeStyleIndex >= 0 && section.activeStyleIndex < section.subSections.size()) {
                effectiveSection = section.subSections.get(section.activeStyleIndex);
            }

            // 判断是否为默认字体/自定义字体特殊界面（使用 effectiveSection 的标题）
            Text titleToCheck = effectiveSection.title.getString().isEmpty() ? section.title : effectiveSection.title;
            boolean isDefaultFonts = titleToCheck.getString().equals(defaultFontsKey);
            boolean isCustomFonts = titleToCheck.getString().equals(customFontsKey);

            if (isDefaultFonts) {
                // 默认字体：描述框 -> 间距8 -> 标题 -> 间距20 -> 字体列表（警告框已在循环前统一处理）
                List<OrderedText> descLines = textRenderer.wrapLines(section.description, mainWidth - 48);
                currentContentY += 8 + descLines.size() * 12 + 10; // 描述框
                currentContentY += 8;                               // 描述框与标题之间的间距

                currentContentY += 12; // 标题
                currentContentY += 20; // 标题与字体列表之间的间距

                // 字体列表
                if (!effectiveSection.fontItems.isEmpty()) {
                    int fontItemWidth = mainWidth - UIConstants.FONT_ITEM_WIDTH_OFFSET;
                    int fontStartX = UIConstants.SIDEBAR_WIDTH + 30;
                    int insertBtnX = fontStartX + fontItemWidth - UIConstants.INSERT_BUTTON_WIDTH - 10;

                    for (int i = 0; i < effectiveSection.fontItems.size(); i++) {
                        int fy = currentContentY + i * UIConstants.FONT_ITEM_HEIGHT;
                        if (LayoutHelper.isMouseInRect(mouseX, mouseY, insertBtnX, fy + 2, UIConstants.INSERT_BUTTON_WIDTH, 16)) {
                            PatternAndFontOverlay.FontItem fontItem = effectiveSection.fontItems.get(i);
                            insertTextToScreen(String.format(effectiveSection.fontInsertTemplate, fontItem.fontId));
                            return true;
                        }
                    }
                    currentContentY += effectiveSection.fontItems.size() * UIConstants.FONT_ITEM_HEIGHT;
                }
                currentContentY += 12;
                continue;
            } else if (isCustomFonts) {
                // 自定义字体：标题 -> 间距20 -> 字体列表（警告框已在循环前统一处理）
                currentContentY += 12; // 标题
                currentContentY += 20; // 标题与字体列表之间的间距

                // 字体列表
                if (!effectiveSection.fontItems.isEmpty()) {
                    int fontItemWidth = mainWidth - UIConstants.FONT_ITEM_WIDTH_OFFSET;
                    int fontStartX = UIConstants.SIDEBAR_WIDTH + 30;
                    int insertBtnX = fontStartX + fontItemWidth - UIConstants.INSERT_BUTTON_WIDTH - 10;

                    for (int i = 0; i < effectiveSection.fontItems.size(); i++) {
                        int fy = currentContentY + i * UIConstants.FONT_ITEM_HEIGHT;
                        if (LayoutHelper.isMouseInRect(mouseX, mouseY, insertBtnX, fy + 2, UIConstants.INSERT_BUTTON_WIDTH, 16)) {
                            PatternAndFontOverlay.FontItem fontItem = effectiveSection.fontItems.get(i);
                            insertTextToScreen(String.format(effectiveSection.fontInsertTemplate, fontItem.fontId));
                            return true;
                        }
                    }
                    currentContentY += effectiveSection.fontItems.size() * UIConstants.FONT_ITEM_HEIGHT;
                }
                currentContentY += 12;
                continue;
            } else {
                currentContentY += 12; // 标题
                // 注意：字体警告框已在循环之前统一处理（与 renderSectionContent 中的渲染顺序一致），
                // 此处无需再为单个 section 重复累加警告框高度。

                List<OrderedText> descLines = textRenderer.wrapLines(section.description, mainWidth - 48);
                currentContentY += descLines.size() * 12 + 10; // 描述
            }

            // 子文件夹筛选按钮
            if (section.useSubfolders && !section.subFolders.isEmpty()) {
                int filterBtnY = currentContentY;
                int filterAreaWidth = section.subFolders.size() * UIConstants.FILTER_BUTTON_WIDTH
                    + (section.subFolders.size() - 1) * UIConstants.FILTER_BUTTON_GAP;
                int filterStartX = width - 24 - filterAreaWidth;

                for (int i = 0; i < section.subFolders.size(); i++) {
                    int bx = filterStartX + i * (UIConstants.FILTER_BUTTON_WIDTH + UIConstants.FILTER_BUTTON_GAP);
                    if (LayoutHelper.isMouseInRect(mouseX, mouseY, bx, filterBtnY, UIConstants.FILTER_BUTTON_WIDTH, UIConstants.FILTER_BUTTON_HEIGHT)) {
                        section.activeTabIndex = i;
                        return true;
                    }
                }
                currentContentY += 20;
            }

            // 样式切换按钮
            if (section.useStyles && !section.subSections.isEmpty()
                    && section.activeStyleIndex >= 0 && section.activeStyleIndex < section.subSections.size()) {
                effectiveSection = section.subSections.get(section.activeStyleIndex);
                int styleBtnY = currentContentY;
                int styleCount = section.subSections.size();
                int styleAreaWidth = styleCount * UIConstants.FILTER_BUTTON_WIDTH
                    + (styleCount - 1) * UIConstants.FILTER_BUTTON_GAP;
                int styleStartX = width - 24 - styleAreaWidth;

                for (int i = 0; i < styleCount; i++) {
                    int bx = styleStartX + i * (UIConstants.FILTER_BUTTON_WIDTH + UIConstants.FILTER_BUTTON_GAP);
                    if (LayoutHelper.isMouseInRect(mouseX, mouseY, bx, styleBtnY, UIConstants.FILTER_BUTTON_WIDTH, UIConstants.FILTER_BUTTON_HEIGHT)) {
                        section.activeStyleIndex = i;
                        return true;
                    }
                }
                currentContentY += 20;
            }

            if (!section.useSubfolders && !section.useStyles) {
                currentContentY += 20;
            }

            if (effectiveSection.isFontMode) {
                int fontItemWidth = mainWidth - UIConstants.FONT_ITEM_WIDTH_OFFSET;
                int fontStartX = UIConstants.SIDEBAR_WIDTH + 30;
                int insertBtnX = fontStartX + fontItemWidth - UIConstants.INSERT_BUTTON_WIDTH - 10;

                for (int i = 0; i < effectiveSection.fontItems.size(); i++) {
                    int fy = currentContentY + i * UIConstants.FONT_ITEM_HEIGHT;
                    if (LayoutHelper.isMouseInRect(mouseX, mouseY, insertBtnX, fy + 2, UIConstants.INSERT_BUTTON_WIDTH, 16)) {
                        PatternAndFontOverlay.FontItem fontItem = effectiveSection.fontItems.get(i);
                        insertTextToScreen(String.format(effectiveSection.fontInsertTemplate, fontItem.fontId));
                        return true;
                    }
                }
                currentContentY += effectiveSection.fontItems.size() * UIConstants.FONT_ITEM_HEIGHT;
            } else if (effectiveSection.isWhitelistMode) {
                if (!effectiveSection.whitelistItems.isEmpty()) {
                    int index = GridRenderer.getGridItemIndex(mouseX, mouseY, 0, mainWidth, currentContentY, scrollWindowStartY, scrollWindowEndY, effectiveSection.whitelistItems.size());
                    if (index >= 0) {
                        PatternAndFontOverlay.WhitelistPatternItem item = effectiveSection.whitelistItems.get(index);
                        if (!item.insertContent.isEmpty()) {
                            insertTextToScreen(item.insertContent);
                        } else {
                            insertTextureToScreen(item.textureId);
                        }
                        return true;
                    }
                    int cols = Math.max(1, (mainWidth - 48) / (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_X));
                    currentContentY += ((int) Math.ceil((double) effectiveSection.whitelistItems.size() / cols)) * (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_Y) + 20;
                } else {
                    currentContentY += 30;
                }
            } else {
                String tabKey = effectiveSection.useSubfolders && !effectiveSection.subFolders.isEmpty()
                    ? effectiveSection.subFolders.get(effectiveSection.activeTabIndex).dirName : "root";
                var textures = effectiveSection.cachedTextures.get(tabKey);

                if (textures != null && !textures.isEmpty()) {
                    int index = GridRenderer.getGridItemIndex(mouseX, mouseY, 0, mainWidth, currentContentY, scrollWindowStartY, scrollWindowEndY, textures.size());
                    if (index >= 0) {
                        insertTextureToScreen(textures.get(index));
                        return true;
                    }
                    int cols = Math.max(1, (mainWidth - 48) / (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_X));
                    currentContentY += ((int) Math.ceil((double) textures.size() / cols)) * (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_Y);
                } else {
                    currentContentY += 30;
                }
            }
            currentContentY += 12;
        }
        return false;
    }

    /**
     * 处理鼠标滚轮滚动事件。
     *
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param amount 滚动量
     */
    public static void mouseScrolled(double mouseX, double mouseY, double amount) {
        if (!PatternAndFontOverlay.isVisible) return;

        if (mouseX > UIConstants.SIDEBAR_WIDTH && PatternAndFontOverlay.maxScrollY > 20) {
            PatternAndFontOverlay.scrollY -= amount * UIConstants.SCROLL_AMOUNT;
            PatternAndFontOverlay.scrollY = MathHelper.clamp(PatternAndFontOverlay.scrollY, 0, PatternAndFontOverlay.maxScrollY);
        } else if (mouseX <= UIConstants.SIDEBAR_WIDTH && PatternAndFontOverlay.maxSidebarScrollY > 20) {
            PatternAndFontOverlay.sidebarScrollY -= amount * UIConstants.SCROLL_AMOUNT;
            PatternAndFontOverlay.sidebarScrollY = MathHelper.clamp(PatternAndFontOverlay.sidebarScrollY, 0, PatternAndFontOverlay.maxSidebarScrollY);
        }
    }

    /**
     * 处理鼠标释放事件，重置所有拖动状态。
     *
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param button 鼠标按钮编号
     * @return 若事件被消费返回 {@code true}
     */
    public static boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!PatternAndFontOverlay.isVisible || button != 0) return false;
        PatternAndFontOverlay.isDraggingMainScrollbar = false;
        PatternAndFontOverlay.isDraggingSidebarScrollbar = false;
        PatternAndFontOverlay.isDraggingSv = false;
        PatternAndFontOverlay.isDraggingHue = false;
        return true;
    }

    /**
     * 向当前告示牌编辑界面插入纹理。
     */
    private static void insertTextureToScreen(Identifier identifier) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen instanceof bklmc.ocelotsign.mixin_interfaces.ISignEditorExtension extension) {
            extension.ocelotsign$insertTexture(identifier);
            PatternAndFontOverlay.isVisible = false;
        }
    }

    /**
     * 向当前告示牌编辑界面插入文本。
     */
    private static void insertTextToScreen(String text) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen instanceof bklmc.ocelotsign.mixin_interfaces.ISignEditorExtension extension) {
            extension.ocelotsign$insertText(text);
            PatternAndFontOverlay.isVisible = false;
        }
    }
}
