package bklmc.ocelotsign.client;

import bklmc.ocelotsign.mixin_interfaces.ISignEditorExtension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 图案与字体选择界面的 UI 渲染与交互组合入口 (1.19.2 兼容版)
 */
public final class PatternAndFontOverlay {

    public enum FilterMode { WHITELIST, BLACKLIST, NONE }

    public static class SubFolderDef {
        public final String dirName;
        public final Text displayName;
        public SubFolderDef(String dirName, Text displayName) { this.dirName = dirName; this.displayName = displayName; }
    }

    public static class WhitelistPatternItem {
        public final Identifier textureId;
        public final String insertContent;
        public final Text displayName;
        public WhitelistPatternItem(Identifier textureId, String insertContent, Text displayName) { this.textureId = textureId; this.insertContent = insertContent; this.displayName = displayName; }
    }

    public static class FontItem {
        public final String fontId;
        public final Text displayName;
        public FontItem(String fontId, Text displayName) { this.fontId = fontId; this.displayName = displayName; }
    }

    public static class H4Section {
        public final Text title;
        public final Text description;
        public final Identifier basePath;
        public boolean useSubfolders = false;
        public final List<SubFolderDef> subFolders = new ArrayList<>();
        public boolean useStyles = false;
        public final List<H4Section> subSections = new ArrayList<>();
        public int activeStyleIndex = 0;
        public boolean isExpanded = true;
        public FilterMode extFilterMode = FilterMode.NONE;
        public final List<String> extFilterList = new ArrayList<>();
        public int activeTabIndex = 0;
        public final java.util.Map<String, List<Identifier>> cachedTextures = new java.util.HashMap<>();
        public boolean isWhitelistMode = false;
        public final List<WhitelistPatternItem> whitelistItems = new ArrayList<>();
        public boolean isFontMode = false;
        public final List<FontItem> fontItems = new ArrayList<>();
        public String fontInsertTemplate = "-json {\"font\":\"%s\",\"text\":\"XXX\"}";
        public String customJsonPath = "";

        public H4Section(Text title, Text description, Identifier basePath) { this.title = title; this.description = description; this.basePath = basePath; }
        public H4Section enableSubfolders() { this.useSubfolders = true; return this; }
        public H4Section addSubFolder(String dirName, Text displayName) { this.subFolders.add(new SubFolderDef(dirName, displayName)); return this; }
        public H4Section enableStyles() { this.useStyles = true; return this; }
        public H4Section addStyle(H4Section styleSection) { this.subSections.add(styleSection); return this; }
        public H4Section setExtensionFilter(FilterMode mode, String... exts) { this.extFilterMode = mode; this.extFilterList.clear(); for (String ext : exts) this.extFilterList.add(ext); return this; }
        public H4Section setWhitelistMode() { this.isWhitelistMode = true; return this; }
        public H4Section addWhitelistItem(Identifier textureId, String insertContent, Text displayName) { this.whitelistItems.add(new WhitelistPatternItem(textureId, insertContent, displayName)); return this; }
        public H4Section setFontMode() { this.isFontMode = true; return this; }
        public H4Section addFontItem(String fontId, Text displayName) { this.fontItems.add(new FontItem(fontId, displayName)); return this; }
        public H4Section setFontInsertTemplate(String template) { this.fontInsertTemplate = template; return this; }
        public H4Section setCustomJsonPath(String path) { this.customJsonPath = path; return this; }
    }

    public static class H3Category {
        public final Text title;
        public Text headerText = null;
        public final List<H3Category> subCategories = new ArrayList<>();
        public final List<H4Section> sections = new ArrayList<>();
        public boolean isExpanded = true;
        public H3Category(Text title) { this.title = title; }
        public H3Category addSubCategory(H3Category sub) { this.subCategories.add(sub); return this; }
        public H3Category addSection(H4Section section) { this.sections.add(section); return this; }
    }

    public static class H2Category {
        public final Text title;
        public final List<H3Category> subCategories = new ArrayList<>();
        public boolean isExpanded = true;
        public H2Category(Text title) { this.title = title; }
        public H2Category addSubCategory(H3Category sub) { this.subCategories.add(sub); return this; }
    }

    public static class MishangPatternItem {
        public final String name;
        public final String insertCode;
        public final Identifier textureId;
        public MishangPatternItem(String name, String insertCode, Identifier textureId) { this.name = name; this.insertCode = insertCode; this.textureId = textureId; }
    }

    // ==================== 公开状态字段 ====================
    public static final List<H2Category> REGISTRY = new ArrayList<>();
    public static H2Category selectedH2 = null;
    public static H3Category selectedH3 = null;
    public static boolean isVisible = false;
    public static double scrollY = 0;
    public static double maxScrollY = 0;
    public static double sidebarScrollY = 0;
    public static double maxSidebarScrollY = 0;
    public static boolean isDraggingMainScrollbar = false;
    public static boolean isDraggingSidebarScrollbar = false;
    public static double dragStartMouseY = 0;
    public static double dragStartScrollY = 0;
    public static double dragStartSidebarScrollY = 0;
    public static boolean isDocumentListSelected = false;
    public static boolean isColorPaletteSelected = false;
    public static boolean isColorPickerSelected = false;
    public static boolean isAcknowledgmentSelected = false;
    public static int sidebarSelection = 0;
    public static final int SIDEBAR_TOP_NONE = SidebarState.SIDEBAR_NONE;
    public static final int SIDEBAR_TOP_DOCS = SidebarState.SIDEBAR_DOCS;
    public static final int SIDEBAR_TOP_PALETTE = SidebarState.SIDEBAR_PALETTE;
    public static final int SIDEBAR_TOP_PICKER = SidebarState.SIDEBAR_PICKER;
    public static final int SIDEBAR_TOP_ACK = SidebarState.SIDEBAR_ACK;
    public static int colorPickerR = 0x00;
    public static int colorPickerG = 0x80;
    public static int colorPickerB = 0x00;
    public static boolean isDraggingSv = false;
    public static boolean isDraggingHue = false;
    public static final List<MishangPatternItem> MISHANG_PATTERNS = new ArrayList<>();
    public static boolean isDataLoaded = false;

    public static void initMishangPatterns() {
        if (!MISHANG_PATTERNS.isEmpty()) return;
        String[][] patterns = {
                {"向左箭头","arrow-left"},{"向右箭头","arrow-right"},{"向上箭头","arrow-up"},{"向下箭头","arrow-down"},
                {"向左箭头(细)","arrow-left-thin"},{"向右箭头(细)","arrow-right-thin"},{"向上箭头(细)","arrow-up-thin"},{"向下箭头(细)","arrow-down-thin"},
                {"向左上方箭头","arrow-left-up"},{"向右上方箭头","arrow-right-up"},{"向左下方箭头","arrow-left-down"},{"向右下方箭头","arrow-right-down"},
                {"左转向上箭头","arrow-left-turn-up"},{"右转向上箭头","arrow-right-turn-up"},{"左转向下箭头","arrow-left-turn-down"},{"右转向下箭头","arrow-right-turn-down"},
                {"左右双向箭头","arrow-left-right"},{"上下双向箭头","arrow-up-down"},
                {"小圆圈","circle-small"},{"中圆圈","circle-medium"},{"禁止符号","ban"},
                {"左转掉头(向下)","u-turn-left-down"},{"右转掉头(向下)","u-turn-right-down"},{"左转掉头(向上)","u-turn-left-up"},{"右转掉头(向上)","u-turn-right-up"},
                {"小十字","cross-small"},{"中十字","cross-medium"},{"大十字","cross-large"},
                {"小正方形","square-small"},{"中正方形","square-medium"},{"大正方形","square-large"},
                {"小斜方形","square-slant-small"},{"中斜方形","square-slant-medium"},{"大斜方形","square-slant-large"}
        };
        for (String[] p : patterns) addMishangPattern(p[0], p[1]);
    }

    private static void addMishangPattern(String name, String patternName) {
        MISHANG_PATTERNS.add(new MishangPatternItem(name, "-pattern " + patternName,
                new Identifier("ocelotsignmod", "textures/mishanguc_patterns/" + patternName + ".png")));
    }

    public static void resetForReload() { REGISTRY.clear(); MISHANG_PATTERNS.clear(); isDataLoaded = false; selectedH2 = null; selectedH3 = null; }
    public static void selectSidebarTop(int which) { SidebarState.selectSidebarTop(which); }
    public static void clearSidebarTop() { SidebarState.clearSidebarTop(); }

    // ==================== 主渲染入口 ====================

    public static void render(MatrixStack matrices, int mouseX, int mouseY) {
        if (!isVisible) return;
        MishangIntegration.clearHovered();
        clearLastHoveredUrl();

        int width = LayoutHelper.getScreenWidth();
        int height = LayoutHelper.getScreenHeight();
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int mainWidth = width - UIConstants.SIDEBAR_WIDTH;
        int scrollWindowStartY = UIConstants.HEADER_HEIGHT + 1;
        int scrollWindowEndY = height - UIConstants.FOOTER_HEIGHT;
        int scrollWindowHeight = scrollWindowEndY - scrollWindowStartY;
        int sidebarScrollWindowHeight = height - UIConstants.HEADER_HEIGHT - UIConstants.FOOTER_HEIGHT;

        PatternRegistry.registerBuiltInPatterns();
        SidebarState.enforceSidebarMutualExclusion();

        if (isColorPickerSelected && (isDraggingSv || isDraggingHue)) handleColorPickerDrag(mouseX, mouseY);

        updateScrollValues(textRenderer, mainWidth, sidebarScrollWindowHeight, scrollWindowHeight);
        handleScrollbarDragging(mouseX, mouseY, scrollWindowHeight, sidebarScrollWindowHeight);

        matrices.push();
        matrices.translate(0.0f, 0.0f, UIConstants.LAYER_Z_OFFSET);

        renderSidebar(matrices, textRenderer, mouseX, mouseY, sidebarScrollWindowHeight);
        renderMainArea(matrices, textRenderer, mouseX, mouseY, width, height, mainWidth, scrollWindowStartY, scrollWindowEndY);
        renderReturnButton(matrices, textRenderer, mouseX, mouseY, width, mainWidth, height);

        matrices.pop();
    }

    private static void updateScrollValues(TextRenderer textRenderer, int mainWidth, int sidebarScrollWindowHeight, int scrollWindowHeight) {
        int totalSidebarHeight = UIConstants.HEADER_HEIGHT + 12
                + LayoutHelper.getSidebarTopItemHeight(Text.translatable("ocelotsignmod.gui.sidebar.docs"), textRenderer)
                + LayoutHelper.getSidebarTopItemHeight(Text.translatable("ocelotsignmod.gui.sidebar.ack"), textRenderer)
                + LayoutHelper.getSidebarTopItemHeight(Text.translatable("ocelotsignmod.gui.sidebar.color_palette"), textRenderer)
                + LayoutHelper.getSidebarTopItemHeight(Text.translatable("ocelotsignmod.gui.sidebar.color_picker"), textRenderer);
        for (H2Category h2 : REGISTRY) {
            totalSidebarHeight += LayoutHelper.getCategoryHeight(h2.title, h2.isExpanded ? "[-] " : "[+] ", 12, textRenderer);
            if (h2.isExpanded) for (H3Category h3 : h2.subCategories) totalSidebarHeight += LayoutHelper.calculateH3Height(h3, 24, textRenderer);
        }
        maxSidebarScrollY = Math.max(0, totalSidebarHeight - sidebarScrollWindowHeight);
        sidebarScrollY = MathHelper.clamp(sidebarScrollY, 0, maxSidebarScrollY);
        maxScrollY = Math.max(0, LayoutHelper.getTotalMainContentHeight(mainWidth, textRenderer) - scrollWindowHeight);
        scrollY = MathHelper.clamp(scrollY, 0, maxScrollY);
    }

    private static void handleScrollbarDragging(double mouseX, double mouseY, int scrollWindowHeight, int sidebarScrollWindowHeight) {
        if (isDraggingMainScrollbar && maxScrollY > 0) {
            float trackRatio = (float) scrollWindowHeight / (float) (scrollWindowHeight + maxScrollY);
            int thumbHeight = Math.max(UIConstants.SCROLLBAR_MIN_HEIGHT, (int) (scrollWindowHeight * trackRatio));
            int trackRange = scrollWindowHeight - thumbHeight;
            if (trackRange > 0) scrollY = MathHelper.clamp(dragStartScrollY + ((mouseY - dragStartMouseY) / trackRange) * maxScrollY, 0, maxScrollY);
        }
        if (isDraggingSidebarScrollbar && maxSidebarScrollY > 0) {
            float trackRatio = (float) sidebarScrollWindowHeight / (float) (sidebarScrollWindowHeight + maxSidebarScrollY);
            int thumbHeight = Math.max(UIConstants.SCROLLBAR_MIN_HEIGHT, (int) (sidebarScrollWindowHeight * trackRatio));
            int trackRange = sidebarScrollWindowHeight - thumbHeight;
            if (trackRange > 0) sidebarScrollY = MathHelper.clamp(dragStartSidebarScrollY + ((mouseY - dragStartMouseY) / trackRange) * maxSidebarScrollY, 0, maxSidebarScrollY);
        }
    }

    private static void renderSidebar(MatrixStack matrices, TextRenderer textRenderer, int mouseX, int mouseY, int sidebarScrollWindowHeight) {
        DrawableHelper.fill(matrices, 0, 0, UIConstants.SIDEBAR_WIDTH, LayoutHelper.getScreenHeight(), UIConstants.COLOR_SIDEBAR_BG);
        DrawableHelper.fill(matrices, 0, 0, UIConstants.SIDEBAR_WIDTH, UIConstants.HEADER_HEIGHT, UIConstants.COLOR_SIDEBAR_HEADER);
        DrawableHelper.fill(matrices, 0, UIConstants.HEADER_HEIGHT - 1, UIConstants.SIDEBAR_WIDTH, UIConstants.HEADER_HEIGHT, UIConstants.COLOR_SIDEBAR_BORDER);

        Text sidebarTitle = Text.translatable("ocelotsignmod.gui.sidebar.title");
        textRenderer.draw(matrices, sidebarTitle, (UIConstants.SIDEBAR_WIDTH - textRenderer.getWidth(sidebarTitle)) / 2, (UIConstants.HEADER_HEIGHT - 8) / 2, 0xFFFFFFFF);

        enableScissor(0, UIConstants.HEADER_HEIGHT, UIConstants.SIDEBAR_WIDTH, LayoutHelper.getScreenHeight() - UIConstants.HEADER_HEIGHT);
        int currentY = UIConstants.HEADER_HEIGHT + 12 - (int) sidebarScrollY;

        currentY = renderSidebarTopItem(matrices, textRenderer, mouseX, mouseY, Text.translatable("ocelotsignmod.gui.sidebar.docs"), currentY, isDocumentListSelected && !isColorPaletteSelected && !isColorPickerSelected && !isAcknowledgmentSelected);
        currentY = renderSidebarTopItem(matrices, textRenderer, mouseX, mouseY, Text.translatable("ocelotsignmod.gui.sidebar.ack"), currentY, isAcknowledgmentSelected && !isDocumentListSelected && !isColorPaletteSelected && !isColorPickerSelected);
        currentY = renderSidebarTopItem(matrices, textRenderer, mouseX, mouseY, Text.translatable("ocelotsignmod.gui.sidebar.color_palette"), currentY, isColorPaletteSelected && !isDocumentListSelected && !isColorPickerSelected && !isAcknowledgmentSelected);
        currentY = renderSidebarTopItem(matrices, textRenderer, mouseX, mouseY, Text.translatable("ocelotsignmod.gui.sidebar.color_picker"), currentY, isColorPickerSelected && !isDocumentListSelected && !isColorPaletteSelected && !isAcknowledgmentSelected);

        for (H2Category h2 : REGISTRY) {
            String prefix = h2.isExpanded ? "[-] " : "[+] ";
            int itemHeight = LayoutHelper.getCategoryHeight(h2.title, prefix, 12, textRenderer);
            boolean hoverH2 = LayoutHelper.isMouseInRect(mouseX, mouseY, 0, currentY, UIConstants.SIDEBAR_WIDTH, itemHeight);
            List<OrderedText> lines = textRenderer.wrapLines(Text.literal(prefix + h2.title.getString()), UIConstants.SIDEBAR_WIDTH - 20);
            int textY = currentY + (itemHeight - lines.size() * 10) / 2 + 1;
            for (int i = 0; i < lines.size(); i++) textRenderer.draw(matrices, lines.get(i), 12, textY + i * 10, hoverH2 ? 0xFFFFFFFF : UIConstants.COLOR_H2_TEXT);
            currentY += itemHeight;
            if (h2.isExpanded) for (H3Category h3 : h2.subCategories) currentY = LayoutHelper.renderH3CategoryDynamic(matrices, textRenderer, mouseX, mouseY, h3, 24, currentY);
        }

        disableScissor();
        LayoutHelper.renderScrollbar(matrices, 0, UIConstants.HEADER_HEIGHT, UIConstants.SIDEBAR_WIDTH, sidebarScrollWindowHeight, sidebarScrollY, maxSidebarScrollY, sidebarScrollWindowHeight, mouseX, mouseY);
    }

    private static int renderSidebarTopItem(MatrixStack matrices, TextRenderer textRenderer, double mouseX, double mouseY, Text text, int currentY, boolean isSelected) {
        int maxWidth = Math.max(20, UIConstants.SIDEBAR_WIDTH - 24);
        List<OrderedText> lines = textRenderer.wrapLines(text, maxWidth);
        int itemHeight = Math.max(UIConstants.DOC_LIST_ITEM_HEIGHT, lines.size() * 10 + 6);
        if (isSelected) DrawableHelper.fill(matrices, 0, currentY, UIConstants.SIDEBAR_WIDTH, currentY + itemHeight, UIConstants.COLOR_H3_BG_SELECTED);
        boolean isHover = LayoutHelper.isMouseInRect(mouseX, mouseY, 0, currentY, UIConstants.SIDEBAR_WIDTH, itemHeight);
        int textColor = isSelected ? 0xFFFFFFFF : (isHover ? 0xFFFFFFFF : UIConstants.COLOR_H3_TEXT);
        int textY = currentY + (itemHeight - lines.size() * 10) / 2 + 1;
        for (int i = 0; i < lines.size(); i++) textRenderer.draw(matrices, lines.get(i), 12, textY + i * 10, textColor);
        return currentY + itemHeight;
    }

    private static void renderMainArea(MatrixStack matrices, TextRenderer textRenderer, int mouseX, int mouseY,
                                       int width, int height, int mainWidth, int scrollWindowStartY, int scrollWindowEndY) {
        DrawableHelper.fill(matrices, UIConstants.SIDEBAR_WIDTH, 0, width, height, UIConstants.COLOR_MAIN_BG);
        DrawableHelper.fill(matrices, UIConstants.SIDEBAR_WIDTH, 0, width, UIConstants.HEADER_HEIGHT, UIConstants.COLOR_MAIN_HEADER);
        DrawableHelper.fill(matrices, UIConstants.SIDEBAR_WIDTH, UIConstants.HEADER_HEIGHT, width, UIConstants.HEADER_HEIGHT + 1, UIConstants.COLOR_MAIN_BORDER);

        Text currentH2Title = isDocumentListSelected ? Text.translatable("ocelotsignmod.gui.sidebar.docs")
                : isColorPaletteSelected ? Text.translatable("ocelotsignmod.gui.sidebar.color_palette")
                : isColorPickerSelected ? Text.translatable("ocelotsignmod.gui.sidebar.color_picker")
                : isAcknowledgmentSelected ? Text.translatable("ocelotsignmod.gui.sidebar.ack")
                : (selectedH3 != null ? selectedH3.title : Text.literal(""));
        int h2Width = textRenderer.getWidth(currentH2Title);
        textRenderer.drawWithShadow(matrices, currentH2Title, UIConstants.SIDEBAR_WIDTH + (mainWidth - h2Width) / 2, (UIConstants.HEADER_HEIGHT - 8) / 2, 0xFFFFFFFF);

        enableScissor(UIConstants.SIDEBAR_WIDTH, scrollWindowStartY, width, scrollWindowEndY);
        int contentStartY = scrollWindowStartY + 15 - (int) scrollY;

        if (isDocumentListSelected) HomepageRenderer.render(matrices, textRenderer, mouseX, mouseY, mainWidth, contentStartY, scrollWindowStartY, scrollWindowEndY);
        else if (isColorPaletteSelected) ColorPaletteRenderer.render(matrices, textRenderer, mouseX, mouseY, mainWidth, contentStartY, scrollWindowStartY, scrollWindowEndY);
        else if (isColorPickerSelected) ColorPickerRenderer.render(matrices, textRenderer, mainWidth, contentStartY, scrollWindowStartY, scrollWindowEndY);
        else if (isAcknowledgmentSelected) AcknowledgmentRenderer.render(matrices, textRenderer, mouseX, mouseY, mainWidth, contentStartY, scrollWindowStartY, scrollWindowEndY);
        else if (selectedH3 != null) {
            if (selectedH3.title.getString().equals(Text.translatable("ocelotsignmod.gui.categories.mishang_builtin").getString())) {
                MishangIntegration.render(matrices, textRenderer, width, height, mouseX, mouseY, UIConstants.SIDEBAR_WIDTH);
                disableScissor();
                return;
            }
            renderSectionContent(matrices, textRenderer, mouseX, mouseY, width, mainWidth, contentStartY, scrollWindowStartY, scrollWindowEndY);
        }

        disableScissor();
        LayoutHelper.renderScrollbar(matrices, UIConstants.SIDEBAR_WIDTH, scrollWindowStartY, width - UIConstants.SIDEBAR_WIDTH,
                scrollWindowEndY - scrollWindowStartY, scrollY, maxScrollY, scrollWindowEndY - scrollWindowStartY, mouseX, mouseY);
    }

    public static boolean hasAnyFontSection(H3Category h3) {
        String defaultFontsKey = Text.translatable("ocelotsignmod.gui.sections.default_fonts").getString();
        String customFontsKey = Text.translatable("ocelotsignmod.gui.sections.custom_fonts").getString();
        for (H4Section section : h3.sections) {
            H4Section eff = section;
            if (section.useStyles && !section.subSections.isEmpty() && section.activeStyleIndex >= 0 && section.activeStyleIndex < section.subSections.size())
                eff = section.subSections.get(section.activeStyleIndex);
            String titleStr = (eff.title.getString().isEmpty() ? section.title : eff.title).getString();
            if (titleStr.equals(defaultFontsKey) || titleStr.equals(customFontsKey) || eff.isFontMode) return true;
        }
        return false;
    }

    private static void renderSectionContent(MatrixStack matrices, TextRenderer textRenderer, int mouseX, int mouseY,
                                             int width, int mainWidth, int contentStartY, int scrollWindowStartY, int scrollWindowEndY) {
        int currentContentY = contentStartY;
        H3Category h3 = selectedH3;
        String defaultFontsKey = Text.translatable("ocelotsignmod.gui.sections.default_fonts").getString();
        String customFontsKey = Text.translatable("ocelotsignmod.gui.sections.custom_fonts").getString();

        if (h3.headerText != null) {
            renderHeaderText(matrices, textRenderer, h3.headerText, mainWidth, currentContentY);
            currentContentY += textRenderer.wrapLines(h3.headerText, mainWidth - 48).size() * 12 + 16 + 15;
        }
        if (hasAnyFontSection(h3)) currentContentY = (currentContentY + 8) + renderWarningBox(matrices, textRenderer, mainWidth, currentContentY + 8, scrollWindowStartY, scrollWindowEndY);

        for (H4Section section : h3.sections) {
            H4Section eff = section;
            if (section.useStyles && !section.subSections.isEmpty() && section.activeStyleIndex >= 0 && section.activeStyleIndex < section.subSections.size())
                eff = section.subSections.get(section.activeStyleIndex);
            Text titleToRender = eff.title.getString().isEmpty() ? section.title : eff.title;
            boolean isDefaultFonts = titleToRender.getString().equals(defaultFontsKey);
            boolean isCustomFonts = titleToRender.getString().equals(customFontsKey);

            if (isDefaultFonts) {
                currentContentY = renderDefaultFontsDescription(matrices, textRenderer, eff, mainWidth, currentContentY) + 8;
                textRenderer.draw(matrices, titleToRender, UIConstants.SIDEBAR_WIDTH + 24, currentContentY, UIConstants.COLOR_SECTION_TITLE);
                currentContentY += 12;
            } else if (isCustomFonts) {
                textRenderer.draw(matrices, titleToRender, UIConstants.SIDEBAR_WIDTH + 24, currentContentY, UIConstants.COLOR_SECTION_TITLE);
                currentContentY += 12;
            } else {
                textRenderer.draw(matrices, titleToRender, UIConstants.SIDEBAR_WIDTH + 24, currentContentY, UIConstants.COLOR_SECTION_TITLE);
                currentContentY += 12;
                textRenderer.draw(matrices, eff.description, UIConstants.SIDEBAR_WIDTH + 24, currentContentY, UIConstants.COLOR_DESC_TEXT);
                currentContentY += textRenderer.wrapLines(eff.description, mainWidth - 48).size() * 12 + 10;
            }

            if (section.useSubfolders && !section.subFolders.isEmpty()) { renderFilterButtons(matrices, textRenderer, mouseX, mouseY, width, section.subFolders, section.activeTabIndex, currentContentY); currentContentY += 20; }
            if (section.useStyles && !section.subSections.isEmpty()) {
                List<SubFolderDef> styleTabs = new ArrayList<>();
                for (H4Section child : section.subSections) styleTabs.add(new SubFolderDef(child.title.getString(), child.title));
                renderFilterButtons(matrices, textRenderer, mouseX, mouseY, width, styleTabs, section.activeStyleIndex, currentContentY);
                currentContentY += 20;
            }
            if (!section.useSubfolders && !section.useStyles) currentContentY += 20;
            currentContentY = renderSectionItems(matrices, textRenderer, mouseX, mouseY, mainWidth, scrollWindowStartY, scrollWindowEndY, eff, currentContentY) + 12;
        }
    }

    private static void renderHeaderText(MatrixStack matrices, TextRenderer textRenderer, Text headerText, int mainWidth, int currentY) {
        int descWidth = mainWidth - 48;
        List<OrderedText> lines = textRenderer.wrapLines(headerText, descWidth);
        DrawableHelper.fill(matrices, UIConstants.SIDEBAR_WIDTH + 20, currentY + 2, UIConstants.SIDEBAR_WIDTH + 28 + descWidth, currentY + 2 + lines.size() * 12 + 16, UIConstants.COLOR_HEADER_BG_HELP);
        for (int i = 0; i < lines.size(); i++) textRenderer.draw(matrices, lines.get(i), UIConstants.SIDEBAR_WIDTH + 24, currentY + 10 + i * 12, UIConstants.COLOR_HEADER_TEXT);
    }

    private static int renderDefaultFontsDescription(MatrixStack matrices, TextRenderer textRenderer, H4Section section, int mainWidth, int currentY) {
        int descWidth = mainWidth - 48;
        List<OrderedText> lines = textRenderer.wrapLines(section.description, descWidth);
        int totalDescHeight = lines.size() * 12 + 10;
        DrawableHelper.fill(matrices, UIConstants.SIDEBAR_WIDTH + 20, currentY + 8, UIConstants.SIDEBAR_WIDTH + 32 + descWidth, currentY + 8 + totalDescHeight, UIConstants.COLOR_HEADER_BG_FONT);
        DrawableHelper.fill(matrices, UIConstants.SIDEBAR_WIDTH + 20, currentY + 8, UIConstants.SIDEBAR_WIDTH + 23, currentY + 8 + totalDescHeight, UIConstants.COLOR_WARNING_BAR);
        for (int i = 0; i < lines.size(); i++) textRenderer.draw(matrices, lines.get(i), UIConstants.SIDEBAR_WIDTH + 24, currentY + 12 + i * 12, UIConstants.COLOR_HEADER_TEXT);
        return currentY + 8 + totalDescHeight;
    }

    private static int renderWarningBox(MatrixStack matrices, TextRenderer textRenderer, int mainWidth, int currentY,
                                        int scrollWindowStartY, int scrollWindowEndY) {
        Text warningTitle = Text.translatable("ocelotsignmod.gui.sections.font_rendering_warning.title");
        Text warningText = Text.translatable("ocelotsignmod.gui.sections.font_rendering_warning");
        int boxX = UIConstants.SIDEBAR_WIDTH + 20, boxWidth = mainWidth - 40, paddingX = 8, paddingY = 8, titleHeight = 14, lineHeight = 12, gap = 4;
        List<OrderedText> textLines = textRenderer.wrapLines(warningText, boxWidth - paddingX * 2);
        int totalBoxHeight = paddingY + titleHeight + gap + textLines.size() * lineHeight + paddingY;
        if (currentY + totalBoxHeight >= scrollWindowStartY && currentY <= scrollWindowEndY) {
            DrawableHelper.fill(matrices, boxX, currentY, boxX + boxWidth, currentY + totalBoxHeight, UIConstants.COLOR_HEADER_BG_WARNING);
            DrawableHelper.fill(matrices, boxX, currentY, boxX + boxWidth, currentY + 1, UIConstants.COLOR_WARNING_BAR);
            DrawableHelper.fill(matrices, boxX, currentY + totalBoxHeight - 1, boxX + boxWidth, currentY + totalBoxHeight, UIConstants.COLOR_WARNING_BAR);
            DrawableHelper.fill(matrices, boxX + boxWidth - 1, currentY, boxX + boxWidth, currentY + totalBoxHeight, UIConstants.COLOR_WARNING_BAR);
            DrawableHelper.fill(matrices, boxX, currentY, boxX + 3, currentY + totalBoxHeight, UIConstants.COLOR_WARNING_BAR);
            textRenderer.draw(matrices, warningTitle, boxX + paddingX + 8, currentY + paddingY, UIConstants.COLOR_WARNING_TEXT);
            int textY = currentY + paddingY + titleHeight + gap;
            for (int i = 0; i < textLines.size(); i++) textRenderer.draw(matrices, textLines.get(i), boxX + paddingX + 8, textY + i * lineHeight, UIConstants.COLOR_HEADER_TEXT);
        }
        return totalBoxHeight + 8;
    }

    private static void renderFilterButtons(MatrixStack matrices, TextRenderer textRenderer, int mouseX, int mouseY,
                                            int width, List<SubFolderDef> items, int activeIndex, int currentY) {
        int filterStartX = width - 24 - (items.size() * UIConstants.FILTER_BUTTON_WIDTH + (items.size() - 1) * UIConstants.FILTER_BUTTON_GAP);
        for (int i = 0; i < items.size(); i++) {
            int bx = filterStartX + i * (UIConstants.FILTER_BUTTON_WIDTH + UIConstants.FILTER_BUTTON_GAP);
            if (currentY + UIConstants.FILTER_BUTTON_HEIGHT < UIConstants.HEADER_HEIGHT + 1 || currentY > LayoutHelper.getScreenHeight() - UIConstants.FOOTER_HEIGHT) continue;
            boolean isHover = LayoutHelper.isMouseInRect(mouseX, mouseY, bx, currentY, UIConstants.FILTER_BUTTON_WIDTH, UIConstants.FILTER_BUTTON_HEIGHT);
            boolean isActive = activeIndex == i;
            DrawableHelper.fill(matrices, bx, currentY, bx + UIConstants.FILTER_BUTTON_WIDTH, currentY + UIConstants.FILTER_BUTTON_HEIGHT, isActive ? 0xFFAAAAAA : (isHover ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG));
            drawBorder(matrices, bx, currentY, UIConstants.FILTER_BUTTON_WIDTH, UIConstants.FILTER_BUTTON_HEIGHT, isActive ? 0xFF000000 : UIConstants.COLOR_BTN_BORDER);
            if (isActive) DrawableHelper.fill(matrices, bx, currentY + UIConstants.FILTER_BUTTON_HEIGHT - 2, bx + UIConstants.FILTER_BUTTON_WIDTH, currentY + UIConstants.FILTER_BUTTON_HEIGHT, 0xFF00AAFF);
            String tabName = items.get(i).displayName.getString();
            List<OrderedText> lines = textRenderer.wrapLines(Text.literal(tabName), UIConstants.FILTER_BUTTON_WIDTH - 4);
            int textStartY = currentY + (UIConstants.FILTER_BUTTON_HEIGHT - lines.size() * textRenderer.fontHeight) / 2;
            for (int li = 0; li < lines.size(); li++) {
                int lw = textRenderer.getWidth(lines.get(li));
                textRenderer.draw(matrices, lines.get(li), bx + (UIConstants.FILTER_BUTTON_WIDTH - lw) / 2, textStartY + li * textRenderer.fontHeight, isActive ? 0xFF000000 : UIConstants.COLOR_BTN_TEXT);
            }
        }
    }

    private static int renderSectionItems(MatrixStack matrices, TextRenderer textRenderer, int mouseX, int mouseY,
                                          int mainWidth, int scrollWindowStartY, int scrollWindowEndY, H4Section section, int currentY) {
        H4Section eff = section;
        if (section.useStyles && !section.subSections.isEmpty() && section.activeStyleIndex >= 0 && section.activeStyleIndex < section.subSections.size())
            eff = section.subSections.get(section.activeStyleIndex);
        if (eff.isFontMode) return currentY + GridRenderer.renderFontList(matrices, textRenderer, mouseX, mouseY, mainWidth, scrollWindowStartY, scrollWindowEndY, eff.fontItems, eff, UIConstants.SIDEBAR_WIDTH + 30, currentY);
        if (eff.isWhitelistMode) return currentY + GridRenderer.renderWhitelistGrid(matrices, textRenderer, mouseX, mouseY, mainWidth, scrollWindowStartY, scrollWindowEndY, eff.whitelistItems, UIConstants.SIDEBAR_WIDTH + 24, currentY);
        String tabKey = eff.useSubfolders && !eff.subFolders.isEmpty() ? eff.subFolders.get(eff.activeTabIndex).dirName : "root";
        var textures = eff.cachedTextures.get(tabKey);
        return currentY + GridRenderer.renderCachedTextureGrid(matrices, textRenderer, mouseX, mouseY, mainWidth, scrollWindowStartY, scrollWindowEndY, textures != null ? textures : List.of(), UIConstants.SIDEBAR_WIDTH + 24, currentY);
    }

    private static void renderReturnButton(MatrixStack matrices, TextRenderer textRenderer, int mouseX, int mouseY,
                                           int width, int mainWidth, int height) {
        int returnBtnX = UIConstants.SIDEBAR_WIDTH + (mainWidth - UIConstants.RETURN_BUTTON_WIDTH) / 2;
        int returnBtnY = height - 35;
        boolean hoverRet = LayoutHelper.isMouseInRect(mouseX, mouseY, returnBtnX, returnBtnY, UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT);
        DrawableHelper.fill(matrices, returnBtnX, returnBtnY, returnBtnX + UIConstants.RETURN_BUTTON_WIDTH, returnBtnY + UIConstants.RETURN_BUTTON_HEIGHT, hoverRet ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG);
        drawBorder(matrices, returnBtnX, returnBtnY, UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT, UIConstants.COLOR_BTN_BORDER);
        Text returnText = Text.translatable("ocelotsignmod.gui.button.back");
        int rtw = textRenderer.getWidth(returnText);
        textRenderer.draw(matrices, returnText, returnBtnX + (UIConstants.RETURN_BUTTON_WIDTH - rtw) / 2, returnBtnY + 7, UIConstants.COLOR_BTN_TEXT);
    }

    // ==================== 1.19.2 兼容 ====================

    private static void enableScissor(int x, int y, int width, int height) {
        MinecraftClient client = MinecraftClient.getInstance();
        double scale = client.getWindow().getScaleFactor();
        int windowHeight = client.getWindow().getFramebufferHeight();
        org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);
        org.lwjgl.opengl.GL11.glScissor((int)(x*scale), (int)(windowHeight-(y+height)*scale), (int)(width*scale), (int)(height*scale));
    }

    private static void disableScissor() { org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST); }

    private static void drawBorder(MatrixStack matrices, int x, int y, int width, int height, int color) {
        DrawableHelper.fill(matrices, x, y, x + width, y + 1, color);
        DrawableHelper.fill(matrices, x, y + height - 1, x + width, y + height, color);
        DrawableHelper.fill(matrices, x, y, x + 1, y + height, color);
        DrawableHelper.fill(matrices, x + width - 1, y, x + width, y + height, color);
    }

    // ==================== 代理方法 ====================

    public static boolean handleColorPickerClick(double mouseX, double mouseY, int mainWidth, int contentStartY, int scrollWindowStartY, int scrollWindowEndY) { return ColorPickerRenderer.handleClick(mouseX, mouseY, mainWidth, contentStartY, scrollWindowStartY, scrollWindowEndY); }
    public static void handleColorPickerDrag(double mouseX, double mouseY) {
        if (isDraggingSv) ColorPickerState.applySvFromMouse(mouseX, mouseY, ColorPickerState.getSvPanelX(), ColorPickerState.getSvPanelY(), ColorPickerState.getSvPanelW());
        else if (isDraggingHue) ColorPickerState.applyHueFromMouse(mouseY, ColorPickerState.getHueBarY(), ColorPickerState.getHueBarH());
    }
    public static int getColorPickerContentHeight(int mainWidth, TextRenderer textRenderer) { return ColorPickerState.getColorPickerContentHeight(mainWidth, textRenderer); }
    public static int getColorPaletteContentHeight(int mainWidth, TextRenderer textRenderer) { return ColorPaletteRenderer.getContentHeight(mainWidth, textRenderer); }
    public static boolean handleColorPaletteClick(double mouseX, double mouseY, int mainWidth, int contentStartY, int scrollWindowStartY, int scrollWindowEndY) { return ColorPaletteRenderer.handleClick(mouseX, mouseY, mainWidth, contentStartY, scrollWindowStartY, scrollWindowEndY); }
    public static int getAcknowledgmentContentHeight(int mainWidth, TextRenderer textRenderer) { return AcknowledgmentRenderer.getContentHeight(mainWidth, textRenderer); }
    public static boolean handleMishangClick(int mouseX, int mouseY, int width, int height, int scrollY, int sidebarWidth) { return MishangIntegration.handleClick(mouseX, mouseY, width, height, sidebarWidth); }
    public static boolean mouseClicked(double mouseX, double mouseY, int button) { return MouseEventHandler.mouseClicked(mouseX, mouseY, button); }
    public static void mouseScrolled(double mouseX, double mouseY, double amount) { MouseEventHandler.mouseScrolled(mouseX, mouseY, amount); }
    public static boolean mouseReleased(double mouseX, double mouseY, int button) { return MouseEventHandler.mouseReleased(mouseX, mouseY, button); }

    private static String lastHoveredUrl = null;
    public static String getLastHoveredUrl() { return lastHoveredUrl; }
    public static void setLastHoveredUrl(String url) { lastHoveredUrl = url; }
    public static void clearLastHoveredUrl() { lastHoveredUrl = null; }
    public static boolean openHomepageLink(String url) { if (url != null && !url.isEmpty()) { Util.getOperatingSystem().open(url); return true; } return false; }
}