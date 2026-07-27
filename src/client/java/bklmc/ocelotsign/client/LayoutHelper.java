package bklmc.ocelotsign.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

/**
 * 布局与渲染辅助工具类
 *
 * @see PatternAndFontOverlay
 */
public final class LayoutHelper {
    private LayoutHelper() {
    }

    /**
     * 计算侧边栏顶部固定项换行后的高度。
     *
     * @param text 文本内容
     * @param textRenderer 文本渲染器
     * @return 项高度（像素）
     */
    public static int getSidebarTopItemHeight(Component text, Font textRenderer) {
        int maxWidth = UIConstants.SIDEBAR_WIDTH - 24;
        if (maxWidth < 20) maxWidth = 20;
        List<FormattedCharSequence> lines = textRenderer.split(text, maxWidth);
        return Math.max(UIConstants.DOC_LIST_ITEM_HEIGHT, lines.size() * 10 + 6);
    }

    /**
     * 计算分类项的高度。
     *
     * @param title 标题文本
     * @param prefix 前缀文本
     * @param indent 缩进量
     * @param textRenderer 文本渲染器
     * @return 分类项高度（像素）
     */
    public static int getCategoryHeight(Component title, String prefix, int indent, Font textRenderer) {
        int maxWidth = UIConstants.SIDEBAR_WIDTH - indent - 8;
        if (maxWidth < 20) maxWidth = 20;
        String fullText = prefix + title.getString();
        List<FormattedCharSequence> lines = textRenderer.split(Component.literal(fullText), maxWidth);
        return Math.max(24, lines.size() * 10 + 10);
    }

    /**
     * 计算 H3 分类的总高度（递归）。
     *
     * @param h3 H3 分类
     * @param indent 缩进量
     * @param textRenderer 文本渲染器
     * @return 总高度（像素）
     */
    public static int calculateH3Height(PatternAndFontOverlay.H3Category h3, int indent, Font textRenderer) {
        String prefix = h3.subCategories.isEmpty() ? "" : (h3.isExpanded ? "[-] " : "[+] ");
        int height = getCategoryHeight(h3.title, prefix, indent, textRenderer);
        if (h3.isExpanded && !h3.subCategories.isEmpty()) {
            for (PatternAndFontOverlay.H3Category child : h3.subCategories) {
                height += calculateH3Height(child, indent + 12, textRenderer);
            }
        }
        if (h3.isExpanded) {
            for (PatternAndFontOverlay.H4Section sec : h3.sections) {
                if (sec.useStyles) {
                    height += calculateH4Height(sec, indent, textRenderer);
                }
            }
        }
        return height;
    }

    /**
     * 计算 H4Section 的总高度（含子样式，递归）。
     *
     * @param section H4 分区
     * @param indent 缩进量
     * @param textRenderer 文本渲染器
     * @return 总高度（像素）
     */
    public static int calculateH4Height(PatternAndFontOverlay.H4Section section, int indent, Font textRenderer) {
        String prefix = section.useStyles ? (section.isExpanded ? "[-] " : "[+] ") : "";
        int height = getCategoryHeight(section.title, prefix, indent, textRenderer);
        if (section.isExpanded && section.useStyles && !section.subSections.isEmpty()) {
            for (PatternAndFontOverlay.H4Section child : section.subSections) {
                height += calculateH4Height(child, indent + 12, textRenderer);
            }
        }
        return height;
    }

    /**
     * 渲染 H4Section（递归，含子样式）。
     *
     * @param context 绘制上下文
     * @param textRenderer 文本渲染器
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param section H4 分区
     * @param indent 缩进量
     * @param y 起始 Y 坐标
     * @param parentH3 父级 H3 分类
     * @return 渲染后的 Y 坐标
     */
    public static int renderH4SectionDynamic(GuiGraphicsExtractor context, Font textRenderer,
                                             double mouseX, double mouseY,
                                             PatternAndFontOverlay.H4Section section,
                                             int indent, int y,
                                             PatternAndFontOverlay.H3Category parentH3) {
        String prefix = section.useStyles ? (section.isExpanded ? "[-] " : "[+] ") : "";
        int itemHeight = getCategoryHeight(section.title, prefix, indent, textRenderer);

        boolean isSelected = (PatternAndFontOverlay.selectedH3 == parentH3);
        boolean isHover = isMouseInRect(mouseX, mouseY, 0, y, UIConstants.SIDEBAR_WIDTH, itemHeight);

        if (isSelected) {
            context.fill(0, y, UIConstants.SIDEBAR_WIDTH, y + itemHeight, UIConstants.COLOR_H3_BG_SELECTED);
        } else if (isHover) {
            context.fill(0, y, UIConstants.SIDEBAR_WIDTH, y + itemHeight, 0x20FFFFFF);
        }

        int maxWidth = UIConstants.SIDEBAR_WIDTH - indent - 8;
        List<FormattedCharSequence> lines = textRenderer.split(Component.literal(prefix + section.title.getString()), maxWidth);
        int textY = y + (itemHeight - lines.size() * 10) / 2 + 1;

        for (int i = 0; i < lines.size(); i++) {
            int color = isSelected ? UIConstants.COLOR_H3_TEXT_SELECTED : (isHover ? 0xFFFFFFFF : UIConstants.COLOR_H3_TEXT);
            context.text(textRenderer, lines.get(i), indent, textY + i * 10, color, false);
        }

        int currentY = y + itemHeight;
        if (section.isExpanded && section.useStyles && !section.subSections.isEmpty()) {
            for (PatternAndFontOverlay.H4Section child : section.subSections) {
                currentY = renderH4SectionDynamic(context, textRenderer, mouseX, mouseY, child, indent + 12, currentY, parentH3);
            }
        }
        return currentY;
    }

    /**
     * 递归更新 H3 分类的 Y 坐标。
     *
     * @param currentY 当前 Y 坐标
     * @param h3 H3 分类
     * @return 更新后的 Y 坐标
     */
    public static int updateH3CategoryY(int currentY, PatternAndFontOverlay.H3Category h3) {
        currentY += 24;
        if (h3.isExpanded && !h3.subCategories.isEmpty()) {
            for (PatternAndFontOverlay.H3Category child : h3.subCategories) {
                currentY = updateH3CategoryY(currentY, child);
            }
        }
        return currentY;
    }

    /**
     * 渲染 H3 分类（递归）。
     *
     * @param context 绘制上下文
     * @param textRenderer 文本渲染器
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param h3 H3 分类
     * @param indent 缩进量
     * @param y 起始 Y 坐标
     * @return 渲染后的 Y 坐标
     */
    public static int renderH3CategoryDynamic(GuiGraphicsExtractor context, Font textRenderer,
                                               double mouseX, double mouseY,
                                               PatternAndFontOverlay.H3Category h3,
                                               int indent, int y) {
        String prefix = h3.subCategories.isEmpty() ? "" : (h3.isExpanded ? "[-] " : "[+] ");
        int itemHeight = getCategoryHeight(h3.title, prefix, indent, textRenderer);

        boolean isSelected = (PatternAndFontOverlay.selectedH3 == h3
                && !PatternAndFontOverlay.isDocumentListSelected
                && !PatternAndFontOverlay.isColorPaletteSelected
                && !PatternAndFontOverlay.isColorPickerSelected
                && !PatternAndFontOverlay.isAcknowledgmentSelected);
        boolean isHover = isMouseInRect(mouseX, mouseY, 0, y, UIConstants.SIDEBAR_WIDTH, itemHeight);

        if (isSelected) {
            context.fill(0, y, UIConstants.SIDEBAR_WIDTH, y + itemHeight, UIConstants.COLOR_H3_BG_SELECTED);
        } else if (isHover) {
            // 悬停半透明背景
            int hoverBg = 0x20FFFFFF;
            context.fill(0, y, UIConstants.SIDEBAR_WIDTH, y + itemHeight, hoverBg);
        }

        int maxWidth = UIConstants.SIDEBAR_WIDTH - indent - 8;
        List<FormattedCharSequence> lines = textRenderer.split(Component.literal(prefix + h3.title.getString()), maxWidth);
        int textY = y + (itemHeight - lines.size() * 10) / 2 + 1;

        for (int i = 0; i < lines.size(); i++) {
            int color = isSelected ? UIConstants.COLOR_H3_TEXT_SELECTED : (isHover ? 0xFFFFFFFF : UIConstants.COLOR_H3_TEXT);
            context.text(textRenderer, lines.get(i), indent, textY + i * 10, color, false);
        }

        int currentY = y + itemHeight;
        if (h3.isExpanded && !h3.subCategories.isEmpty()) {
            for (PatternAndFontOverlay.H3Category child : h3.subCategories) {
                currentY = renderH3CategoryDynamic(context, textRenderer, mouseX, mouseY, child, indent + 12, currentY);
            }
        }
        if (h3.isExpanded) {
            for (PatternAndFontOverlay.H4Section sec : h3.sections) {
                if (sec.useStyles) {
                    currentY = renderH4SectionDynamic(context, textRenderer, mouseX, mouseY, sec, indent, currentY, h3);
                }
            }
        }
        return currentY;
    }

    /**
     * 处理 H3 分类点击事件（递归）。
     *
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param h2 父级 H2 分类
     * @param h3 H3 分类
     * @param indent 缩进量
     * @param y 起始 Y 坐标
     * @param textRenderer 文本渲染器
     * @return 点击项的 Y 坐标，未命中返回 null
     */
    public static Integer handleH3CategoryClick(double mouseX, double mouseY,
                                                  PatternAndFontOverlay.H2Category h2,
                                                  PatternAndFontOverlay.H3Category h3,
                                                  int indent, int y, Font textRenderer) {
        String prefix = h3.subCategories.isEmpty() ? "" : (h3.isExpanded ? "[-] " : "[+] ");
        int itemHeight = getCategoryHeight(h3.title, prefix, indent, textRenderer);

        if (isMouseInRect(mouseX, mouseY, 0, y, UIConstants.SIDEBAR_WIDTH, itemHeight)) {
            if (!h3.subCategories.isEmpty()) {
                h3.isExpanded = !h3.isExpanded;
                if (!h3.isExpanded && PatternAndFontOverlay.selectedH3 != null && isH3Descendant(h3, PatternAndFontOverlay.selectedH3)) {
                    PatternAndFontOverlay.selectedH3 = null;
                }
            } else {
                PatternAndFontOverlay.clearSidebarTop();
                PatternAndFontOverlay.selectedH2 = h2;
                PatternAndFontOverlay.selectedH3 = h3;
                PatternAndFontOverlay.scrollY = 0;
            }
            return y;
        }

        int currentY = y + itemHeight;
        if (h3.isExpanded && !h3.subCategories.isEmpty()) {
            for (PatternAndFontOverlay.H3Category child : h3.subCategories) {
                Integer result = handleH3CategoryClick(mouseX, mouseY, h2, child, indent + 12, currentY, textRenderer);
                if (result != null) {
                    return result;
                }
                currentY += calculateH3Height(child, indent + 12, textRenderer);
            }
        }
        return null;
    }

    /**
     * 处理 H4Section 点击事件（含样式子项）。
     *
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param h2 父级 H2 分类
     * @param parentH3 父级 H3 分类
     * @param section H4 分区
     * @param indent 缩进量
     * @param y 起始 Y 坐标
     * @param textRenderer 文本渲染器
     * @return 点击项的 Y 坐标，未命中返回 null
     */
    public static Integer handleH4SectionClick(double mouseX, double mouseY,
                                               PatternAndFontOverlay.H2Category h2,
                                               PatternAndFontOverlay.H3Category parentH3,
                                               PatternAndFontOverlay.H4Section section,
                                               int indent, int y, Font textRenderer) {
        String prefix = section.useStyles ? (section.isExpanded ? "[-] " : "[+] ") : "";
        int itemHeight = getCategoryHeight(section.title, prefix, indent, textRenderer);

        if (isMouseInRect(mouseX, mouseY, 0, y, UIConstants.SIDEBAR_WIDTH, itemHeight)) {
            if (section.useStyles && !section.subSections.isEmpty()) {
                section.isExpanded = !section.isExpanded;
            } else {
                PatternAndFontOverlay.clearSidebarTop();
                PatternAndFontOverlay.selectedH2 = h2;
                PatternAndFontOverlay.selectedH3 = parentH3;
                PatternAndFontOverlay.scrollY = 0;
            }
            return y;
        }

        int currentY = y + itemHeight;
        if (section.isExpanded && section.useStyles && !section.subSections.isEmpty()) {
            for (int i = 0; i < section.subSections.size(); i++) {
                PatternAndFontOverlay.H4Section child = section.subSections.get(i);
                Integer result = handleH4SectionClick(mouseX, mouseY, h2, parentH3, child, indent + 12, currentY, textRenderer);
                if (result != null) {
                    // 点击子样式时设置活动样式并选中父分类
                    if (child.subSections.isEmpty() && section.useStyles) {
                        section.activeStyleIndex = i;
                        PatternAndFontOverlay.clearSidebarTop();
                        PatternAndFontOverlay.selectedH2 = h2;
                        PatternAndFontOverlay.selectedH3 = parentH3;
                        PatternAndFontOverlay.scrollY = 0;
                    }
                    return result;
                }
                currentY += calculateH4Height(child, indent + 12, textRenderer);
            }
        }
        return null;
    }

    // 判断 child 是否为 parent 的后代
    private static boolean isH3Descendant(PatternAndFontOverlay.H3Category parent, PatternAndFontOverlay.H3Category child) {
        if (parent.subCategories.contains(child)) return true;
        for (PatternAndFontOverlay.H3Category sub : parent.subCategories) {
            if (isH3Descendant(sub, child)) return true;
        }
        return false;
    }

    /**
     * 渲染滚动条。
     *
     * @param context 绘制上下文
     * @param x 滚动条 X 坐标
     * @param y 滚动条 Y 坐标
     * @param width 滚动区域宽度
     * @param height 滚动区域高度
     * @param scrollY 当前滚动量
     * @param maxScrollY 最大滚动量
     * @param scrollWindowHeight 滚动窗口高度
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     */
    public static void renderScrollbar(GuiGraphicsExtractor context, int x, int y, int width, int height,
                                       double scrollY, double maxScrollY, int scrollWindowHeight,
                                       double mouseX, double mouseY) {
        if (maxScrollY <= 20) return;

        int scrollbarX = x + width - UIConstants.SCROLLBAR_WIDTH - 2;
        int scrollbarHeight = height;

        context.fill(scrollbarX, y, scrollbarX + UIConstants.SCROLLBAR_WIDTH, y + scrollbarHeight, UIConstants.COLOR_SCROLLBAR_TRACK);

        float trackRatio = (float) scrollWindowHeight / (float) (scrollWindowHeight + maxScrollY);
        int thumbHeight = Math.max(UIConstants.SCROLLBAR_MIN_HEIGHT, (int) (scrollbarHeight * trackRatio));
        float scrollRatio = (float) scrollY / (float) maxScrollY;
        int thumbY = y + (int) ((scrollbarHeight - thumbHeight) * scrollRatio);

        boolean isHover = isMouseInRect(mouseX, mouseY, scrollbarX, thumbY, UIConstants.SCROLLBAR_WIDTH, thumbHeight);
        int thumbColor = isHover ? UIConstants.COLOR_SCROLLBAR_THUMB_HOVER : UIConstants.COLOR_SCROLLBAR_THUMB;
        context.fill(scrollbarX, thumbY, scrollbarX + UIConstants.SCROLLBAR_WIDTH, thumbY + thumbHeight, thumbColor);
        context.outline(scrollbarX, thumbY, UIConstants.SCROLLBAR_WIDTH, thumbHeight, 0xFF999999);
    }

    /**
     * 计算主内容区域的总高度。
     *
     * @param mainWidth 主区域宽度
     * @param textRenderer 文本渲染器
     * @return 总内容高度（像素）
     */
    public static int getTotalMainContentHeight(int mainWidth, Font textRenderer) {
        if (PatternAndFontOverlay.isDocumentListSelected) {
            return getDocListContentHeight(mainWidth, textRenderer);
        } else if (PatternAndFontOverlay.isAcknowledgmentSelected) {
            return PatternAndFontOverlay.getAcknowledgmentContentHeight(mainWidth, textRenderer);
        } else if (PatternAndFontOverlay.isColorPaletteSelected) {
            return PatternAndFontOverlay.getColorPaletteContentHeight(mainWidth, textRenderer);
        } else if (PatternAndFontOverlay.isColorPickerSelected) {
            return PatternAndFontOverlay.getColorPickerContentHeight(mainWidth, textRenderer);
        } else if (PatternAndFontOverlay.selectedH3 != null) {
            String mishangKey = Component.translatable("ocelotsignmod.gui.categories.mishang_builtin").getString();
            if (PatternAndFontOverlay.selectedH3.title.getString().equals(mishangKey)) {
                return getMishangContentHeight(mainWidth, textRenderer);
            } else {
                return getSectionContentHeight(mainWidth, textRenderer);
            }
        }
        return 0;
    }

    // 计算文档列表内容高度
    private static int getDocListContentHeight(int mainWidth, Font textRenderer) {
        // 头图高度
        int headerImageHeight = (int) (mainWidth * 0.3);

        // 主标题 + 下划线 + 间距
        int height = headerImageHeight + 10 + 26 + 20;

        // 副标题 + 间距
        height += 28;

        // 提示文字块
        Component hintText = Component.translatable("ocelotsignmod.gui.homepage.hint");
        List<FormattedCharSequence> hintLines = textRenderer.split(hintText, mainWidth - 64);
        int hintBgHeight = hintLines.size() * 12 + 10;
        height += hintBgHeight + 12;

        // Introduction 章节
        height += 10 + 22;

        // 简介文本高度：3段
        int introLineHeight = 15;
        int introParaGap = 20;
        for (String key : new String[]{"ocelotsignmod.gui.homepage.intro.p1",
                "ocelotsignmod.gui.homepage.intro.p2", "ocelotsignmod.gui.homepage.intro.p3"}) {
            Component t = Component.translatable(key);
            List<FormattedCharSequence> lines = textRenderer.split(t, mainWidth - 40);
            height += lines.size() * introLineHeight;
            height += introParaGap;
        }

        // 分割线 + 间距
        height += 1 + 20;

        // Project Links 章节
        height += 5 + 22;

        // 两列卡片高度
        int columnWidth = (mainWidth - 60) / 2;
        int leftCardHeight = getCardHeightFromUrls(textRenderer, columnWidth,
                "https://github.com/SolidBlock-cn/mishanguc",
                "https://www.mcmod.cn/class/5743.html");
        int rightCardHeight = getCardHeightFromUrls(textRenderer, columnWidth,
                "https://github.com/Creeper-Cola123/ocelotsignmod-minecraft",
                "https://creeper-cola123.github.io/OcelotSignMod_Docs/");

        height += Math.max(leftCardHeight, rightCardHeight);

        // 卡片底部间距
        height += 25;

        // 免责声明部分
        height += 10 + 22;
        int disclaimerLineHeight = 15;
        int disclaimerParaGap = 20;
        for (String key : new String[]{"ocelotsignmod.gui.homepage.disclaimer.p1",
                "ocelotsignmod.gui.homepage.disclaimer.p2", "ocelotsignmod.gui.homepage.disclaimer.p3"}) {
            Component t = Component.translatable(key);
            List<FormattedCharSequence> lines = textRenderer.split(t, mainWidth - 40);
            height += lines.size() * disclaimerLineHeight;
            height += disclaimerParaGap;
        }
        height += 45;

        return height;
    }

    /**
     * 计算卡片固定高度。
     *
     * @param textRenderer 文本渲染器
     * @param columnWidth 列宽
     * @param repoUrl 仓库链接
     * @param docUrl 文档链接
     * @return 卡片高度（像素）
     */
    private static int getCardHeightFromUrls(Font textRenderer, int columnWidth,
                                            String repoUrl, String docUrl) {
        int padding = 12;
        int titleHeight = 14;
        int btnWidth = columnWidth - padding * 2;
        String repoName = HomepageRenderer.getShortLinkText(repoUrl).getString();
        String docName = HomepageRenderer.getShortLinkText(docUrl).getString();
        int repoLines = Math.max(1, textRenderer.split(Component.literal(repoName), btnWidth - 16).size());
        int docLines = Math.max(1, textRenderer.split(Component.literal(docName), btnWidth - 16).size());
        int repoBtnHeight = repoLines * 10 + 8;
        int docBtnHeight = docLines * 10 + 8;
        int linkAreaHeight = repoBtnHeight + docBtnHeight + 12;
        return padding + titleHeight + 12 + linkAreaHeight + padding;
    }

    // 计算 Mishang 内置图案内容高度
    private static int getMishangContentHeight(int mainWidth, Font textRenderer) {
        int height = 20;
        String[] descKeys = {
            "ocelotsignmod.mishang.json.desc", "ocelotsignmod.mishang.nbt.desc",
            "ocelotsignmod.mishang.rect.desc", "ocelotsignmod.mishang.texture.desc"
        };
        String[] displayKeys = {
            "ocelotsignmod.mishang.json.display", "ocelotsignmod.mishang.nbt.display",
            "ocelotsignmod.mishang.rect.display", "ocelotsignmod.mishang.texture.display"
        };
        int rightMargin = 24 + 50 + 15;
        int availableWidth = mainWidth - 30 - rightMargin;

        for (int i = 0; i < displayKeys.length; i++) {
            Component displayText = Component.translatable(displayKeys[i]);
            Component descText = Component.translatable(descKeys[i]);
            int displayWidth = textRenderer.width(displayText);
            int descWidth = Math.max(0, availableWidth - displayWidth - 10);
            List<FormattedCharSequence> lines = textRenderer.split(descText, descWidth);
            int rowHeight = Math.max(24, lines.size() * 10 + 14);
            height += rowHeight + 8;
        }
        height += 15 + 48;

        int cols = Math.max(1, (mainWidth - 48) / (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_X));
        int totalRows = (int) Math.ceil((double) PatternAndFontOverlay.MISHANG_PATTERNS.size() / cols);
        // 每个 item 的高度 = 图案 + Insert 按钮 + 间距
        height += totalRows * (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_Y + 12);
        return height;
    }

    // 判断 H3 下是否存在任意处于字体模式的有效 section
    private static boolean hasAnyFontSection(PatternAndFontOverlay.H3Category h3) {
        String defaultFontsKey = Component.translatable("ocelotsignmod.gui.sections.default_fonts").getString();
        String customFontsKey = Component.translatable("ocelotsignmod.gui.sections.custom_fonts").getString();
        for (PatternAndFontOverlay.H4Section section : h3.sections) {
            PatternAndFontOverlay.H4Section effectiveSection = section;
            if (section.useStyles && !section.subSections.isEmpty()
                    && section.activeStyleIndex >= 0 && section.activeStyleIndex < section.subSections.size()) {
                effectiveSection = section.subSections.get(section.activeStyleIndex);
            }
            Component titleToRender = effectiveSection.title.getString().isEmpty() ? section.title : effectiveSection.title;
            String titleStr = titleToRender.getString();
            if (titleStr.equals(defaultFontsKey) || titleStr.equals(customFontsKey)) {
                return true;
            }
            if (effectiveSection.isFontMode) {
                return true;
            }
        }
        return false;
    }

    // 计算分区内容高度
    private static int getSectionContentHeight(int mainWidth, Font textRenderer) {
        int height = 0;
        PatternAndFontOverlay.H3Category selectedH3 = PatternAndFontOverlay.selectedH3;

        if (selectedH3.headerText != null) {
            int lines = textRenderer.split(selectedH3.headerText, mainWidth - 48).size();
            height += (lines * 12 + 16) + 15;
        }

        // 字体渲染警告框
        if (hasAnyFontSection(selectedH3)) {
            int boxWidth = mainWidth - 40;
            int paddingY = 8;
            int titleHeight = 14;
            int gap = 4;
            int lineHeight = 12;
            Component warningText = Component.translatable("ocelotsignmod.gui.sections.font_rendering_warning");
            int warningLines = textRenderer.split(warningText, boxWidth - 16).size();
            height += 8;                                                                             // 间距（警告框前）
            height += paddingY + titleHeight + gap + warningLines * lineHeight + paddingY;          // 警告框
            height += 8;                                                                             // 警告框后间距（renderWarningBox 返回的尾部 8px）
        }

        String defaultFontsKey = Component.translatable("ocelotsignmod.gui.sections.default_fonts").getString();
        String customFontsKey = Component.translatable("ocelotsignmod.gui.sections.custom_fonts").getString();

        for (PatternAndFontOverlay.H4Section section : selectedH3.sections) {
            PatternAndFontOverlay.H4Section effectiveSection = section;
            if (section.useStyles && !section.subSections.isEmpty()
                    && section.activeStyleIndex >= 0 && section.activeStyleIndex < section.subSections.size()) {
                effectiveSection = section.subSections.get(section.activeStyleIndex);
            }
            Component sectionTitle = effectiveSection.title.getString().isEmpty() ? section.title : effectiveSection.title;
            boolean isDefaultFonts = sectionTitle.getString().equals(defaultFontsKey);
            boolean isCustomFonts = sectionTitle.getString().equals(customFontsKey);
            if (isDefaultFonts) {
                int lines = textRenderer.split(section.description, mainWidth - 48).size();
                height += 8 + lines * 12 + 10;              // 描述框
                height += 8;                                // 间距（描述框与标题之间）
                height += 12;                                // 标题
                // 字体列表
                if (!effectiveSection.fontItems.isEmpty()) {
                    height += effectiveSection.fontItems.size() * UIConstants.FONT_ITEM_HEIGHT;
                } else {
                    height += 30;
                }
            } else if (isCustomFonts) {
                // 自定义字体特殊布局
                height += 12;                                // 标题
                // 字体列表
                if (!effectiveSection.fontItems.isEmpty()) {
                    height += effectiveSection.fontItems.size() * UIConstants.FONT_ITEM_HEIGHT;
                } else {
                    height += 30;
                }
            } else {
                // 普通界面：标题 -> 描述
                height += 12;                                // 标题
                int lines = textRenderer.split(section.description, mainWidth - 48).size();
                height += lines * 12 + 10;                  // 描述（与渲染代码一致）
            }

            // 按钮区（与渲染代码一致：3 个独立的 if 块）
            if (section.useSubfolders && !section.subFolders.isEmpty()) {
                height += 20;
            }
            if (section.useStyles && !section.subSections.isEmpty()) {
                height += 20;
            }
            if (!section.useSubfolders && !section.useStyles) {
                height += 20;
            }

            if (effectiveSection.isFontMode && !isDefaultFonts && !isCustomFonts) {
                // 字体列表（非 default_fonts）
                if (!effectiveSection.fontItems.isEmpty()) {
                    height += effectiveSection.fontItems.size() * UIConstants.FONT_ITEM_HEIGHT;
                } else {
                    height += 30;
                }
            } else if (effectiveSection.isWhitelistMode) {
                if (!effectiveSection.whitelistItems.isEmpty()) {
                    int cols = Math.max(1, (mainWidth - 48) / (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_X));
                    int rows = (int) Math.ceil((double) effectiveSection.whitelistItems.size() / cols);
                    height += rows * (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_Y) + 20;
                } else {
                    height += 30;
                }
            } else if (!isCustomFonts && !isDefaultFonts) {
                // 纹理网格（非字体模式）
                String tabKey = effectiveSection.useSubfolders && !effectiveSection.subFolders.isEmpty()
                    ? effectiveSection.subFolders.get(effectiveSection.activeTabIndex).dirName : "root";
                var textures = effectiveSection.cachedTextures.get(tabKey);
                if (textures != null && !textures.isEmpty()) {
                    int cols = Math.max(1, (mainWidth - 48) / (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_X));
                    int rows = (int) Math.ceil((double) textures.size() / cols);
                    height += rows * (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_Y);
                } else {
                    height += 30;
                }
            }
            height += 12;
        }
        return height + 12;
    }

    /**
     * 判断鼠标是否在矩形区域内。
     *
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param x 矩形 X 坐标
     * @param y 矩形 Y 坐标
     * @param w 矩形宽度
     * @param h 矩形高度
     * @return 是否在区域内
     */
    public static boolean isMouseInRect(double mouseX, double mouseY, int x, int y, int w, int h) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    /**
     * 判断鼠标是否在矩形区域内（整数版本）。
     *
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param x 矩形 X 坐标
     * @param y 矩形 Y 坐标
     * @param w 矩形宽度
     * @param h 矩形高度
     * @return 是否在区域内
     */
    public static boolean isMouseInRectStatic(int mouseX, int mouseY, int x, int y, int w, int h) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    /**
     * 获取屏幕缩放宽度。
     *
     * @return 屏幕宽度（像素）
     */
    public static int getScreenWidth() {
        return net.minecraft.client.Minecraft.getInstance().getWindow().getGuiScaledWidth();
    }

    /**
     * 获取屏幕缩放高度。
     *
     * @return 屏幕高度（像素）
     */
    public static int getScreenHeight() {
        return net.minecraft.client.Minecraft.getInstance().getWindow().getGuiScaledHeight();
    }
}
