package bklmc.ocelotsign.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;

/**
 * 布局与渲染辅助工具类 (1.19.2 兼容版)
 *
 * @see PatternAndFontOverlay
 */
public final class LayoutHelper {
    private LayoutHelper() {
    }

    public static int getSidebarTopItemHeight(Text text, TextRenderer textRenderer) {
        int maxWidth = UIConstants.SIDEBAR_WIDTH - 24;
        if (maxWidth < 20) maxWidth = 20;
        List<OrderedText> lines = textRenderer.wrapLines(text, maxWidth);
        return Math.max(UIConstants.DOC_LIST_ITEM_HEIGHT, lines.size() * 10 + 6);
    }

    public static int getCategoryHeight(Text title, String prefix, int indent, TextRenderer textRenderer) {
        int maxWidth = UIConstants.SIDEBAR_WIDTH - indent - 8;
        if (maxWidth < 20) maxWidth = 20;
        String fullText = prefix + title.getString();
        List<OrderedText> lines = textRenderer.wrapLines(Text.literal(fullText), maxWidth);
        return Math.max(24, lines.size() * 10 + 10);
    }

    public static int calculateH3Height(PatternAndFontOverlay.H3Category h3, int indent, TextRenderer textRenderer) {
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

    public static int calculateH4Height(PatternAndFontOverlay.H4Section section, int indent, TextRenderer textRenderer) {
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
     */
    public static int renderH4SectionDynamic(MatrixStack matrices, TextRenderer textRenderer,
                                             double mouseX, double mouseY,
                                             PatternAndFontOverlay.H4Section section,
                                             int indent, int y,
                                             PatternAndFontOverlay.H3Category parentH3) {
        String prefix = section.useStyles ? (section.isExpanded ? "[-] " : "[+] ") : "";
        int itemHeight = getCategoryHeight(section.title, prefix, indent, textRenderer);

        boolean isSelected = (PatternAndFontOverlay.selectedH3 == parentH3);
        boolean isHover = isMouseInRect(mouseX, mouseY, 0, y, UIConstants.SIDEBAR_WIDTH, itemHeight);

        if (isSelected) {
            DrawableHelper.fill(matrices, 0, y, UIConstants.SIDEBAR_WIDTH, y + itemHeight, UIConstants.COLOR_H3_BG_SELECTED);
        } else if (isHover) {
            DrawableHelper.fill(matrices, 0, y, UIConstants.SIDEBAR_WIDTH, y + itemHeight, 0x20FFFFFF);
        }

        int maxWidth = UIConstants.SIDEBAR_WIDTH - indent - 8;
        List<OrderedText> lines = textRenderer.wrapLines(Text.literal(prefix + section.title.getString()), maxWidth);
        int textY = y + (itemHeight - lines.size() * 10) / 2 + 1;

        for (int i = 0; i < lines.size(); i++) {
            int color = isSelected ? UIConstants.COLOR_H3_TEXT_SELECTED : (isHover ? 0xFFFFFFFF : UIConstants.COLOR_H3_TEXT);
            textRenderer.draw(matrices, lines.get(i), indent, textY + i * 10, color);
        }

        int currentY = y + itemHeight;
        if (section.isExpanded && section.useStyles && !section.subSections.isEmpty()) {
            for (PatternAndFontOverlay.H4Section child : section.subSections) {
                currentY = renderH4SectionDynamic(matrices, textRenderer, mouseX, mouseY, child, indent + 12, currentY, parentH3);
            }
        }
        return currentY;
    }

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
     */
    public static int renderH3CategoryDynamic(MatrixStack matrices, TextRenderer textRenderer,
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
            DrawableHelper.fill(matrices, 0, y, UIConstants.SIDEBAR_WIDTH, y + itemHeight, UIConstants.COLOR_H3_BG_SELECTED);
        } else if (isHover) {
            int hoverBg = 0x20FFFFFF;
            DrawableHelper.fill(matrices, 0, y, UIConstants.SIDEBAR_WIDTH, y + itemHeight, hoverBg);
        }

        int maxWidth = UIConstants.SIDEBAR_WIDTH - indent - 8;
        List<OrderedText> lines = textRenderer.wrapLines(Text.literal(prefix + h3.title.getString()), maxWidth);
        int textY = y + (itemHeight - lines.size() * 10) / 2 + 1;

        for (int i = 0; i < lines.size(); i++) {
            int color = isSelected ? UIConstants.COLOR_H3_TEXT_SELECTED : (isHover ? 0xFFFFFFFF : UIConstants.COLOR_H3_TEXT);
            textRenderer.draw(matrices, lines.get(i), indent, textY + i * 10, color);
        }

        int currentY = y + itemHeight;
        if (h3.isExpanded && !h3.subCategories.isEmpty()) {
            for (PatternAndFontOverlay.H3Category child : h3.subCategories) {
                currentY = renderH3CategoryDynamic(matrices, textRenderer, mouseX, mouseY, child, indent + 12, currentY);
            }
        }
        if (h3.isExpanded) {
            for (PatternAndFontOverlay.H4Section sec : h3.sections) {
                if (sec.useStyles) {
                    currentY = renderH4SectionDynamic(matrices, textRenderer, mouseX, mouseY, sec, indent, currentY, h3);
                }
            }
        }
        return currentY;
    }

    public static Integer handleH3CategoryClick(double mouseX, double mouseY,
                                                PatternAndFontOverlay.H2Category h2,
                                                PatternAndFontOverlay.H3Category h3,
                                                int indent, int y, TextRenderer textRenderer) {
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

    public static Integer handleH4SectionClick(double mouseX, double mouseY,
                                               PatternAndFontOverlay.H2Category h2,
                                               PatternAndFontOverlay.H3Category parentH3,
                                               PatternAndFontOverlay.H4Section section,
                                               int indent, int y, TextRenderer textRenderer) {
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

    private static boolean isH3Descendant(PatternAndFontOverlay.H3Category parent, PatternAndFontOverlay.H3Category child) {
        if (parent.subCategories.contains(child)) return true;
        for (PatternAndFontOverlay.H3Category sub : parent.subCategories) {
            if (isH3Descendant(sub, child)) return true;
        }
        return false;
    }

    /**
     * 渲染滚动条。
     */
    public static void renderScrollbar(MatrixStack matrices, int x, int y, int width, int height,
                                       double scrollY, double maxScrollY, int scrollWindowHeight,
                                       double mouseX, double mouseY) {
        if (maxScrollY <= 20) return;

        int scrollbarX = x + width - UIConstants.SCROLLBAR_WIDTH - 2;
        int scrollbarHeight = height;

        DrawableHelper.fill(matrices, scrollbarX, y, scrollbarX + UIConstants.SCROLLBAR_WIDTH, y + scrollbarHeight, UIConstants.COLOR_SCROLLBAR_TRACK);

        float trackRatio = (float) scrollWindowHeight / (float) (scrollWindowHeight + maxScrollY);
        int thumbHeight = Math.max(UIConstants.SCROLLBAR_MIN_HEIGHT, (int) (scrollbarHeight * trackRatio));
        float scrollRatio = (float) scrollY / (float) maxScrollY;
        int thumbY = y + (int) ((scrollbarHeight - thumbHeight) * scrollRatio);

        boolean isHover = isMouseInRect(mouseX, mouseY, scrollbarX, thumbY, UIConstants.SCROLLBAR_WIDTH, thumbHeight);
        int thumbColor = isHover ? UIConstants.COLOR_SCROLLBAR_THUMB_HOVER : UIConstants.COLOR_SCROLLBAR_THUMB;
        DrawableHelper.fill(matrices, scrollbarX, thumbY, scrollbarX + UIConstants.SCROLLBAR_WIDTH, thumbY + thumbHeight, thumbColor);
        drawBorder(matrices, scrollbarX, thumbY, UIConstants.SCROLLBAR_WIDTH, thumbHeight, 0xFF999999);
    }

    public static int getTotalMainContentHeight(int mainWidth, TextRenderer textRenderer) {
        if (PatternAndFontOverlay.isDocumentListSelected) {
            return getDocListContentHeight(mainWidth, textRenderer);
        } else if (PatternAndFontOverlay.isAcknowledgmentSelected) {
            return PatternAndFontOverlay.getAcknowledgmentContentHeight(mainWidth, textRenderer);
        } else if (PatternAndFontOverlay.isColorPaletteSelected) {
            return PatternAndFontOverlay.getColorPaletteContentHeight(mainWidth, textRenderer);
        } else if (PatternAndFontOverlay.isColorPickerSelected) {
            return PatternAndFontOverlay.getColorPickerContentHeight(mainWidth, textRenderer);
        } else if (PatternAndFontOverlay.selectedH3 != null) {
            String mishangKey = Text.translatable("ocelotsignmod.gui.categories.mishang_builtin").getString();
            if (PatternAndFontOverlay.selectedH3.title.getString().equals(mishangKey)) {
                return getMishangContentHeight(mainWidth, textRenderer);
            } else {
                return getSectionContentHeight(mainWidth, textRenderer);
            }
        }
        return 0;
    }

    private static int getDocListContentHeight(int mainWidth, TextRenderer textRenderer) {
        int headerImageHeight = (int) (mainWidth * 0.3);
        int height = headerImageHeight + 10 + 26 + 20;
        height += 28;

        Text hintText = Text.translatable("ocelotsignmod.gui.homepage.hint");
        List<OrderedText> hintLines = textRenderer.wrapLines(hintText, mainWidth - 64);
        int hintBgHeight = hintLines.size() * 12 + 10;
        height += hintBgHeight + 12;
        height += 10 + 22;

        int introLineHeight = 15;
        int introParaGap = 20;
        for (String key : new String[]{"ocelotsignmod.gui.homepage.intro.p1",
                "ocelotsignmod.gui.homepage.intro.p2", "ocelotsignmod.gui.homepage.intro.p3"}) {
            Text t = Text.translatable(key);
            List<OrderedText> lines = textRenderer.wrapLines(t, mainWidth - 40);
            height += lines.size() * introLineHeight;
            height += introParaGap;
        }

        height += 1 + 20;
        height += 5 + 22;

        int columnWidth = (mainWidth - 60) / 2;
        int leftCardHeight = getCardHeightFromUrls(textRenderer, columnWidth,
                "https://github.com/SolidBlock-cn/mishanguc",
                "https://www.mcmod.cn/class/5743.html");
        int rightCardHeight = getCardHeightFromUrls(textRenderer, columnWidth,
                "https://github.com/Creeper-Cola123/ocelotsignmod-minecraft",
                "https://creeper-cola123.github.io/OcelotSignMod_Docs/");

        height += Math.max(leftCardHeight, rightCardHeight);
        height += 25;
        height += 10 + 22;

        int disclaimerLineHeight = 15;
        int disclaimerParaGap = 20;
        for (String key : new String[]{"ocelotsignmod.gui.homepage.disclaimer.p1",
                "ocelotsignmod.gui.homepage.disclaimer.p2", "ocelotsignmod.gui.homepage.disclaimer.p3"}) {
            Text t = Text.translatable(key);
            List<OrderedText> lines = textRenderer.wrapLines(t, mainWidth - 40);
            height += lines.size() * disclaimerLineHeight;
            height += disclaimerParaGap;
        }
        height += 45;

        return height;
    }

    private static int getCardHeightFromUrls(TextRenderer textRenderer, int columnWidth,
                                             String repoUrl, String docUrl) {
        int padding = 12;
        int titleHeight = 14;
        int btnWidth = columnWidth - padding * 2;
        String repoName = HomepageRenderer.getShortLinkText(repoUrl).getString();
        String docName = HomepageRenderer.getShortLinkText(docUrl).getString();
        int repoLines = Math.max(1, textRenderer.wrapLines(Text.literal(repoName), btnWidth - 16).size());
        int docLines = Math.max(1, textRenderer.wrapLines(Text.literal(docName), btnWidth - 16).size());
        int repoBtnHeight = repoLines * 10 + 8;
        int docBtnHeight = docLines * 10 + 8;
        int linkAreaHeight = repoBtnHeight + docBtnHeight + 12;
        return padding + titleHeight + 12 + linkAreaHeight + padding;
    }

    private static int getMishangContentHeight(int mainWidth, TextRenderer textRenderer) {
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
            Text displayText = Text.translatable(displayKeys[i]);
            Text descText = Text.translatable(descKeys[i]);
            int displayWidth = textRenderer.getWidth(displayText);
            int descWidth = Math.max(0, availableWidth - displayWidth - 10);
            List<OrderedText> lines = textRenderer.wrapLines(descText, descWidth);
            int rowHeight = Math.max(24, lines.size() * 10 + 14);
            height += rowHeight + 8;
        }
        height += 15 + 48;

        int cols = Math.max(1, (mainWidth - 48) / (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_X));
        int totalRows = (int) Math.ceil((double) PatternAndFontOverlay.MISHANG_PATTERNS.size() / cols);
        height += totalRows * (UIConstants.ITEM_SIZE + UIConstants.ITEM_PADDING_Y + 12);
        return height;
    }

    private static boolean hasAnyFontSection(PatternAndFontOverlay.H3Category h3) {
        String defaultFontsKey = Text.translatable("ocelotsignmod.gui.sections.default_fonts").getString();
        String customFontsKey = Text.translatable("ocelotsignmod.gui.sections.custom_fonts").getString();
        for (PatternAndFontOverlay.H4Section section : h3.sections) {
            PatternAndFontOverlay.H4Section effectiveSection = section;
            if (section.useStyles && !section.subSections.isEmpty()
                    && section.activeStyleIndex >= 0 && section.activeStyleIndex < section.subSections.size()) {
                effectiveSection = section.subSections.get(section.activeStyleIndex);
            }
            Text titleToRender = effectiveSection.title.getString().isEmpty() ? section.title : effectiveSection.title;
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

    private static int getSectionContentHeight(int mainWidth, TextRenderer textRenderer) {
        int height = 0;
        PatternAndFontOverlay.H3Category selectedH3 = PatternAndFontOverlay.selectedH3;

        if (selectedH3.headerText != null) {
            int lines = textRenderer.wrapLines(selectedH3.headerText, mainWidth - 48).size();
            height += (lines * 12 + 16) + 15;
        }

        if (hasAnyFontSection(selectedH3)) {
            int boxWidth = mainWidth - 40;
            int paddingY = 8;
            int titleHeight = 14;
            int gap = 4;
            int lineHeight = 12;
            Text warningText = Text.translatable("ocelotsignmod.gui.sections.font_rendering_warning");
            int warningLines = textRenderer.wrapLines(warningText, boxWidth - 16).size();
            height += 8;
            height += paddingY + titleHeight + gap + warningLines * lineHeight + paddingY;
            height += 8;
        }

        String defaultFontsKey = Text.translatable("ocelotsignmod.gui.sections.default_fonts").getString();
        String customFontsKey = Text.translatable("ocelotsignmod.gui.sections.custom_fonts").getString();

        for (PatternAndFontOverlay.H4Section section : selectedH3.sections) {
            PatternAndFontOverlay.H4Section effectiveSection = section;
            if (section.useStyles && !section.subSections.isEmpty()
                    && section.activeStyleIndex >= 0 && section.activeStyleIndex < section.subSections.size()) {
                effectiveSection = section.subSections.get(section.activeStyleIndex);
            }
            Text sectionTitle = effectiveSection.title.getString().isEmpty() ? section.title : effectiveSection.title;
            boolean isDefaultFonts = sectionTitle.getString().equals(defaultFontsKey);
            boolean isCustomFonts = sectionTitle.getString().equals(customFontsKey);
            if (isDefaultFonts) {
                int lines = textRenderer.wrapLines(section.description, mainWidth - 48).size();
                height += 8 + lines * 12 + 10;
                height += 8;
                height += 12;
                if (!effectiveSection.fontItems.isEmpty()) {
                    height += effectiveSection.fontItems.size() * UIConstants.FONT_ITEM_HEIGHT;
                } else {
                    height += 30;
                }
            } else if (isCustomFonts) {
                height += 12;
                if (!effectiveSection.fontItems.isEmpty()) {
                    height += effectiveSection.fontItems.size() * UIConstants.FONT_ITEM_HEIGHT;
                } else {
                    height += 30;
                }
            } else {
                height += 12;
                int lines = textRenderer.wrapLines(section.description, mainWidth - 48).size();
                height += lines * 12 + 10;
            }

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

    public static boolean isMouseInRect(double mouseX, double mouseY, int x, int y, int w, int h) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    public static boolean isMouseInRectStatic(int mouseX, int mouseY, int x, int y, int w, int h) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    public static int getScreenWidth() {
        return MinecraftClient.getInstance().getWindow().getScaledWidth();
    }

    public static int getScreenHeight() {
        return MinecraftClient.getInstance().getWindow().getScaledHeight();
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