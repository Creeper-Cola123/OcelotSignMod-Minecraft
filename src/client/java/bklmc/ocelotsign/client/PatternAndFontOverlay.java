package bklmc.ocelotsign.client;

import bklmc.ocelotsign.mixin_interfaces.ISignEditorExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 图案与字体选择界面的 UI 渲染与交互组合入口
 *
 * @see PatternRegistry
 * @see SidebarState
 * @see ColorPickerState
 * @see ColorPaletteRenderer
 * @see MishangIntegration
 * @see HomepageRenderer
 * @see AcknowledgmentRenderer
 */
public final class PatternAndFontOverlay {

    // ==================== 数据类 ====================

    public enum FilterMode {
        WHITELIST, BLACKLIST, NONE
    }

    public static class SubFolderDef {
        public final String dirName;
        public final Component displayName;

        public SubFolderDef(String dirName, Component displayName) {
            this.dirName = dirName;
            this.displayName = displayName;
        }
    }

    public static class WhitelistPatternItem {
        public final Identifier textureId;
        public final String insertContent;
        public final Component displayName;

        public WhitelistPatternItem(Identifier textureId, String insertContent, Component displayName) {
            this.textureId = textureId;
            this.insertContent = insertContent;
            this.displayName = displayName;
        }
    }

    /**
     * 字体条目。
     */
    public static class FontItem {
        public final String fontId;
        public final Component displayName;

        public FontItem(String fontId, Component displayName) {
            this.fontId = fontId;
            this.displayName = displayName;
        }
    }

    /**
     * H4 级分区，承载一组图案/字体的展示配置。
     */
    public static class H4Section {
        public final Component title;
        public final Component description;
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

        public H4Section(Component title, Component description, Identifier basePath) {
            this.title = title;
            this.description = description;
            this.basePath = basePath;
        }

        public H4Section enableSubfolders() {
            this.useSubfolders = true;
            return this;
        }

        public H4Section addSubFolder(String dirName, Component displayName) {
            this.subFolders.add(new SubFolderDef(dirName, displayName));
            return this;
        }

        public H4Section enableStyles() {
            this.useStyles = true;
            return this;
        }

        public H4Section addStyle(H4Section styleSection) {
            this.subSections.add(styleSection);
            return this;
        }

        public H4Section setExtensionFilter(FilterMode mode, String... exts) {
            this.extFilterMode = mode;
            this.extFilterList.clear();
            this.extFilterList.addAll(Arrays.asList(exts));
            return this;
        }

        public H4Section setWhitelistMode() {
            this.isWhitelistMode = true;
            return this;
        }

        public H4Section addWhitelistItem(Identifier textureId, String insertContent, Component displayName) {
            this.whitelistItems.add(new WhitelistPatternItem(textureId, insertContent, displayName));
            return this;
        }

        public H4Section setFontMode() {
            this.isFontMode = true;
            return this;
        }

        public H4Section addFontItem(String fontId, Component displayName) {
            this.fontItems.add(new FontItem(fontId, displayName));
            return this;
        }

        public H4Section setFontInsertTemplate(String template) {
            this.fontInsertTemplate = template;
            return this;
        }

        public H4Section setCustomJsonPath(String path) {
            this.customJsonPath = path;
            return this;
        }
    }

    /**
     * H3 级分类。
     */
    public static class H3Category {
        public final Component title;
        public Component headerText = null;
        public final List<H3Category> subCategories = new ArrayList<>();
        public final List<H4Section> sections = new ArrayList<>();
        public boolean isExpanded = true;

        public H3Category(Component title) {
            this.title = title;
        }

        public H3Category addSubCategory(H3Category sub) {
            this.subCategories.add(sub);
            return this;
        }

        public H3Category addSection(H4Section section) {
            this.sections.add(section);
            return this;
        }
    }

    /**
     * H2 级分类。
     */
    public static class H2Category {
        public final Component title;
        public final List<H3Category> subCategories = new ArrayList<>();
        public boolean isExpanded = true;

        public H2Category(Component title) {
            this.title = title;
        }

        public H2Category addSubCategory(H3Category sub) {
            this.subCategories.add(sub);
            return this;
        }
    }

    /**
     * Mishang 图案条目。
     */
    public static class MishangPatternItem {
        public final String name;
        public final String insertCode;
        public final Identifier textureId;

        public MishangPatternItem(String name, String insertCode, Identifier textureId) {
            this.name = name;
            this.insertCode = insertCode;
            this.textureId = textureId;
        }
    }

    // ==================== 公开状态字段 ====================

    /** 分类注册列表。 */
    public static final List<H2Category> REGISTRY = new ArrayList<>();

    /** 当前选中的 H2 分类。 */
    public static H2Category selectedH2 = null;
    /** 当前选中的 H3 分类。 */
    public static H3Category selectedH3 = null;

    /** 浮层是否可见。 */
    public static boolean isVisible = false;
    /** 主区域滚动位置。 */
    public static double scrollY = 0;
    /** 主区域最大滚动位置。 */
    public static double maxScrollY = 0;

    /** 侧边栏滚动位置。 */
    public static double sidebarScrollY = 0;
    /** 侧边栏最大滚动位置。 */
    public static double maxSidebarScrollY = 0;

    /** 主滚动条是否正在拖动。 */
    public static boolean isDraggingMainScrollbar = false;
    /** 侧边栏滚动条是否正在拖动。 */
    public static boolean isDraggingSidebarScrollbar = false;

    /** 拖动起始鼠标 Y 坐标。 */
    public static double dragStartMouseY = 0;
    /** 拖动起始主区域滚动位置。 */
    public static double dragStartScrollY = 0;
    /** 拖动起始侧边栏滚动位置。 */
    public static double dragStartSidebarScrollY = 0;

    /** 文档列表是否选中。 */
    public static boolean isDocumentListSelected = false;
    /** 颜色调色板是否选中。 */
    public static boolean isColorPaletteSelected = false;
    /** 颜色选择器是否选中。 */
    public static boolean isColorPickerSelected = false;
    /** 鸣谢页是否选中。 */
    public static boolean isAcknowledgmentSelected = false;

    /** 侧边栏选中索引。 */
    public static int sidebarSelection = 0;
    /** 侧边栏顶层项：无。 */
    public static final int SIDEBAR_TOP_NONE = SidebarState.SIDEBAR_NONE;
    /** 侧边栏顶层项：文档。 */
    public static final int SIDEBAR_TOP_DOCS = SidebarState.SIDEBAR_DOCS;
    /** 侧边栏顶层项：调色板。 */
    public static final int SIDEBAR_TOP_PALETTE = SidebarState.SIDEBAR_PALETTE;
    /** 侧边栏顶层项：颜色选择器。 */
    public static final int SIDEBAR_TOP_PICKER = SidebarState.SIDEBAR_PICKER;
    /** 侧边栏顶层项：鸣谢。 */
    public static final int SIDEBAR_TOP_ACK = SidebarState.SIDEBAR_ACK;

    /** 颜色选择器红色分量（0-255）。 */
    public static int colorPickerR = 0x00;
    /** 颜色选择器绿色分量（0-255）。 */
    public static int colorPickerG = 0x80;
    /** 颜色选择器蓝色分量（0-255）。 */
    public static int colorPickerB = 0x00;

    /** 是否正在拖动 SV 面板。 */
    public static boolean isDraggingSv = false;
    /** 是否正在拖动色相条。 */
    public static boolean isDraggingHue = false;

    /** Mishang 图案列表。 */
    public static final List<MishangPatternItem> MISHANG_PATTERNS = new ArrayList<>();

    /** 注册中心数据是否已加载。 */
    public static boolean isDataLoaded = false;

    /** 初始化 Mishang 内置图案（幂等）。 */
    public static void initMishangPatterns() {
        if (!MISHANG_PATTERNS.isEmpty()) return;
        addMishangPattern("向左箭头", "arrow-left");
        addMishangPattern("向右箭头", "arrow-right");
        addMishangPattern("向上箭头", "arrow-up");
        addMishangPattern("向下箭头", "arrow-down");
        addMishangPattern("向左箭头(细)", "arrow-left-thin");
        addMishangPattern("向右箭头(细)", "arrow-right-thin");
        addMishangPattern("向上箭头(细)", "arrow-up-thin");
        addMishangPattern("向下箭头(细)", "arrow-down-thin");
        addMishangPattern("向左上方箭头", "arrow-left-up");
        addMishangPattern("向右上方箭头", "arrow-right-up");
        addMishangPattern("向左下方箭头", "arrow-left-down");
        addMishangPattern("向右下方箭头", "arrow-right-down");
        addMishangPattern("左转向上箭头", "arrow-left-turn-up");
        addMishangPattern("右转向上箭头", "arrow-right-turn-up");
        addMishangPattern("左转向下箭头", "arrow-left-turn-down");
        addMishangPattern("右转向下箭头", "arrow-right-turn-down");
        addMishangPattern("左右双向箭头", "arrow-left-right");
        addMishangPattern("上下双向箭头", "arrow-up-down");
        addMishangPattern("小圆圈", "circle-small");
        addMishangPattern("中圆圈", "circle-medium");
        addMishangPattern("禁止符号", "ban");
        addMishangPattern("左转掉头(向下)", "u-turn-left-down");
        addMishangPattern("右转掉头(向下)", "u-turn-right-down");
        addMishangPattern("左转掉头(向上)", "u-turn-left-up");
        addMishangPattern("右转掉头(向上)", "u-turn-right-up");
        addMishangPattern("小十字", "cross-small");
        addMishangPattern("中十字", "cross-medium");
        addMishangPattern("大十字", "cross-large");
        addMishangPattern("小正方形", "square-small");
        addMishangPattern("中正方形", "square-medium");
        addMishangPattern("大正方形", "square-large");
        addMishangPattern("小斜方形", "square-slant-small");
        addMishangPattern("中斜方形", "square-slant-medium");
        addMishangPattern("大斜方形", "square-slant-large");
    }

    // 添加 Mishang 图案到列表
    private static void addMishangPattern(String name, String patternName) {
        String insertCode = "-pattern " + patternName;
        Identifier textureId = Identifier.fromNamespaceAndPath("ocelotsignmod", "textures/mishanguc_patterns/" + patternName + ".png");
        MISHANG_PATTERNS.add(new MishangPatternItem(name, insertCode, textureId));
    }

    // ==================== 互斥逻辑转发 ====================

    /**
     * 清除注册表数据，准备重新加载。
     * <p>在资源包刷新时调用，清空所有缓存数据以便重新构建。
     */
    public static void resetForReload() {
        REGISTRY.clear();
        MISHANG_PATTERNS.clear();
        isDataLoaded = false;
        selectedH2 = null;
        selectedH3 = null;
    }

    /**
     * 选中侧边栏顶层项。
     *
     * @param which 顶层项索引
     */
    public static void selectSidebarTop(int which) {
        SidebarState.selectSidebarTop(which);
    }

    /**
     * 清除侧边栏顶层选中状态。
     */
    public static void clearSidebarTop() {
        SidebarState.clearSidebarTop();
    }

    // ==================== 主渲染入口 ====================

    /**
     * 渲染图案与字体选择浮层。
     *
     * @param context 绘制上下文
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     */
    public static void render(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        if (!isVisible) return;

        MishangIntegration.clearHovered();
        // 每帧清空悬停 URL，防止残留
        clearLastHoveredUrl();

        int width = LayoutHelper.getScreenWidth();
        int height = LayoutHelper.getScreenHeight();
        Font textRenderer = Minecraft.getInstance().font;
        int mainWidth = width - UIConstants.SIDEBAR_WIDTH;

        int scrollWindowStartY = UIConstants.HEADER_HEIGHT + 1;
        int scrollWindowEndY = height - UIConstants.FOOTER_HEIGHT;
        int scrollWindowHeight = scrollWindowEndY - scrollWindowStartY;
        int sidebarScrollWindowHeight = height - UIConstants.HEADER_HEIGHT - UIConstants.FOOTER_HEIGHT;

        PatternRegistry.registerBuiltInPatterns();

        // 防御性自愈
        SidebarState.enforceSidebarMutualExclusion();

        // 颜色选择器拖动持续更新
        if (isColorPickerSelected && (isDraggingSv || isDraggingHue)) {
            handleColorPickerDrag(mouseX, mouseY);
        }

        updateScrollValues(textRenderer, mainWidth, sidebarScrollWindowHeight, scrollWindowHeight);
        handleScrollbarDragging(mouseX, mouseY, scrollWindowHeight, sidebarScrollWindowHeight);

        context.pose().pushMatrix();
        context.pose().translate(0.0f, 0.0f);

        renderSidebar(context, textRenderer, mouseX, mouseY, sidebarScrollWindowHeight);
        renderMainArea(context, textRenderer, mouseX, mouseY, width, height, mainWidth, scrollWindowStartY, scrollWindowEndY);

        renderReturnButton(context, textRenderer, mouseX, mouseY, width, mainWidth, height);

        context.pose().popMatrix();
    }

    // 更新滚动位置最大值与钳位
    private static void updateScrollValues(Font textRenderer, int mainWidth, int sidebarScrollWindowHeight, int scrollWindowHeight) {
        // 侧边栏顶层固定 4 项
        int totalSidebarHeight = UIConstants.HEADER_HEIGHT + 12
                + LayoutHelper.getSidebarTopItemHeight(Component.translatable("ocelotsignmod.gui.sidebar.docs"), textRenderer)
                + LayoutHelper.getSidebarTopItemHeight(Component.translatable("ocelotsignmod.gui.sidebar.ack"), textRenderer)
                + LayoutHelper.getSidebarTopItemHeight(Component.translatable("ocelotsignmod.gui.sidebar.color_palette"), textRenderer)
                + LayoutHelper.getSidebarTopItemHeight(Component.translatable("ocelotsignmod.gui.sidebar.color_picker"), textRenderer);
        for (H2Category h2 : REGISTRY) {
            String h2Prefix = h2.isExpanded ? "[-] " : "[+] ";
            totalSidebarHeight += LayoutHelper.getCategoryHeight(h2.title, h2Prefix, 12, textRenderer);
            if (h2.isExpanded) {
                for (H3Category h3 : h2.subCategories) {
                    totalSidebarHeight += LayoutHelper.calculateH3Height(h3, 24, textRenderer);
                }
            }
        }
        maxSidebarScrollY = Math.max(0, totalSidebarHeight - sidebarScrollWindowHeight);
        sidebarScrollY = Mth.clamp(sidebarScrollY, 0, maxSidebarScrollY);

        int totalMainHeight = LayoutHelper.getTotalMainContentHeight(mainWidth, textRenderer);
        maxScrollY = Math.max(0, totalMainHeight - scrollWindowHeight);
        scrollY = Mth.clamp(scrollY, 0, maxScrollY);
    }

    // 处理滚动条拖动
    private static void handleScrollbarDragging(double mouseX, double mouseY, int scrollWindowHeight, int sidebarScrollWindowHeight) {
        if (isDraggingMainScrollbar && maxScrollY > 0) {
            float trackRatio = (float) scrollWindowHeight / (float) (scrollWindowHeight + maxScrollY);
            int thumbHeight = Math.max(UIConstants.SCROLLBAR_MIN_HEIGHT, (int) (scrollWindowHeight * trackRatio));
            int trackRange = scrollWindowHeight - thumbHeight;
            if (trackRange > 0) {
                double scrollDelta = ((mouseY - dragStartMouseY) / trackRange) * maxScrollY;
                scrollY = Mth.clamp(dragStartScrollY + scrollDelta, 0, maxScrollY);
            }
        }

        if (isDraggingSidebarScrollbar && maxSidebarScrollY > 0) {
            float trackRatio = (float) sidebarScrollWindowHeight / (float) (sidebarScrollWindowHeight + maxSidebarScrollY);
            int thumbHeight = Math.max(UIConstants.SCROLLBAR_MIN_HEIGHT, (int) (sidebarScrollWindowHeight * trackRatio));
            int trackRange = sidebarScrollWindowHeight - thumbHeight;
            if (trackRange > 0) {
                double scrollDelta = ((mouseY - dragStartMouseY) / trackRange) * maxSidebarScrollY;
                sidebarScrollY = Mth.clamp(dragStartSidebarScrollY + scrollDelta, 0, maxSidebarScrollY);
            }
        }
    }

    // 渲染侧边栏
    private static void renderSidebar(GuiGraphicsExtractor context, Font textRenderer, int mouseX, int mouseY, int sidebarScrollWindowHeight) {
        context.fill(0, 0, UIConstants.SIDEBAR_WIDTH, LayoutHelper.getScreenHeight(), UIConstants.COLOR_SIDEBAR_BG);
        context.fill(0, 0, UIConstants.SIDEBAR_WIDTH, UIConstants.HEADER_HEIGHT, UIConstants.COLOR_SIDEBAR_HEADER);
        context.fill(0, UIConstants.HEADER_HEIGHT - 1, UIConstants.SIDEBAR_WIDTH, UIConstants.HEADER_HEIGHT, UIConstants.COLOR_SIDEBAR_BORDER);

        Component sidebarTitle = Component.translatable("ocelotsignmod.gui.sidebar.title");
        int titleWidth = textRenderer.width(sidebarTitle);
        context.text(textRenderer, sidebarTitle, (UIConstants.SIDEBAR_WIDTH - titleWidth) / 2, (UIConstants.HEADER_HEIGHT - 8) / 2, 0xFFFFFFFF, false);

        context.enableScissor(0, UIConstants.HEADER_HEIGHT, UIConstants.SIDEBAR_WIDTH, LayoutHelper.getScreenHeight() - UIConstants.HEADER_HEIGHT);

        int currentY = UIConstants.HEADER_HEIGHT + 12 - (int) sidebarScrollY;

        // 文档列表
        Component docListText = Component.translatable("ocelotsignmod.gui.sidebar.docs");
        currentY = renderSidebarTopItem(context, textRenderer, mouseX, mouseY, docListText, currentY,
                isDocumentListSelected && !isColorPaletteSelected && !isColorPickerSelected && !isAcknowledgmentSelected);
        // 鸣谢与模组声明
        Component ackText = Component.translatable("ocelotsignmod.gui.sidebar.ack");
        currentY = renderSidebarTopItem(context, textRenderer, mouseX, mouseY, ackText, currentY,
                isAcknowledgmentSelected && !isDocumentListSelected && !isColorPaletteSelected && !isColorPickerSelected);
        // 道路交通颜色色表
        Component colorPaletteText = Component.translatable("ocelotsignmod.gui.sidebar.color_palette");
        currentY = renderSidebarTopItem(context, textRenderer, mouseX, mouseY, colorPaletteText, currentY,
                isColorPaletteSelected && !isDocumentListSelected && !isColorPickerSelected && !isAcknowledgmentSelected);
        // 颜色选择器
        Component colorPickerText = Component.translatable("ocelotsignmod.gui.sidebar.color_picker");
        currentY = renderSidebarTopItem(context, textRenderer, mouseX, mouseY, colorPickerText, currentY,
                isColorPickerSelected && !isDocumentListSelected && !isColorPaletteSelected && !isAcknowledgmentSelected);

        // H2 分类
        for (H2Category h2 : REGISTRY) {
            String prefix = h2.isExpanded ? "[-] " : "[+] ";
            int itemHeight = LayoutHelper.getCategoryHeight(h2.title, prefix, 12, textRenderer);
            boolean hoverH2 = LayoutHelper.isMouseInRect(mouseX, mouseY, 0, currentY, UIConstants.SIDEBAR_WIDTH, itemHeight);

            List<FormattedCharSequence> lines = textRenderer.split(Component.literal(prefix + h2.title.getString()), UIConstants.SIDEBAR_WIDTH - 20);
            int textY = currentY + (itemHeight - lines.size() * 10) / 2 + 1;

            for (int i = 0; i < lines.size(); i++) {
                context.text(textRenderer, lines.get(i), 12, textY + i * 10, hoverH2 ? 0xFFFFFFFF : UIConstants.COLOR_H2_TEXT, true);
            }
            currentY += itemHeight;

            if (h2.isExpanded) {
                for (H3Category h3 : h2.subCategories) {
                    currentY = LayoutHelper.renderH3CategoryDynamic(context, textRenderer, mouseX, mouseY, h3, 24, currentY);
                }
            }
        }

        context.disableScissor();

        LayoutHelper.renderScrollbar(context, 0, UIConstants.HEADER_HEIGHT, UIConstants.SIDEBAR_WIDTH,
                sidebarScrollWindowHeight, sidebarScrollY, maxSidebarScrollY, sidebarScrollWindowHeight, mouseX, mouseY);
    }

    // 渲染侧边栏顶层项
    private static int renderSidebarTopItem(GuiGraphicsExtractor context, Font textRenderer,
                                           double mouseX, double mouseY,
                                           Component text, int currentY, boolean isSelected) {
        int maxWidth = UIConstants.SIDEBAR_WIDTH - 24;
        if (maxWidth < 20) maxWidth = 20;
        List<FormattedCharSequence> lines = textRenderer.split(text, maxWidth);
        int itemHeight = Math.max(UIConstants.DOC_LIST_ITEM_HEIGHT, lines.size() * 10 + 6);

        boolean isHover = LayoutHelper.isMouseInRect(mouseX, mouseY, 0, currentY, UIConstants.SIDEBAR_WIDTH, itemHeight);
        if (isSelected) {
            context.fill(0, currentY, UIConstants.SIDEBAR_WIDTH, currentY + itemHeight, UIConstants.COLOR_H3_BG_SELECTED);
        }
        int textColor = isSelected ? 0xFFFFFFFF : (isHover ? 0xFFFFFFFF : UIConstants.COLOR_H3_TEXT);
        int textY = currentY + (itemHeight - lines.size() * 10) / 2 + 1;
        for (int i = 0; i < lines.size(); i++) {
            context.text(textRenderer, lines.get(i), 12, textY + i * 10, textColor, false);
        }
        return currentY + itemHeight;
    }

    // 渲染主区域
    private static void renderMainArea(GuiGraphicsExtractor context, Font textRenderer, int mouseX, int mouseY,
                                       int width, int height, int mainWidth, int scrollWindowStartY, int scrollWindowEndY) {
        context.fill(UIConstants.SIDEBAR_WIDTH, 0, width, height, UIConstants.COLOR_MAIN_BG);
        context.fill(UIConstants.SIDEBAR_WIDTH, 0, width, UIConstants.HEADER_HEIGHT, UIConstants.COLOR_MAIN_HEADER);
        context.fill(UIConstants.SIDEBAR_WIDTH, UIConstants.HEADER_HEIGHT, width, UIConstants.HEADER_HEIGHT + 1, UIConstants.COLOR_MAIN_BORDER);

        Component currentH2Title = isDocumentListSelected
                ? Component.translatable("ocelotsignmod.gui.sidebar.docs")
                : isColorPaletteSelected
                ? Component.translatable("ocelotsignmod.gui.sidebar.color_palette")
                : isColorPickerSelected
                ? Component.translatable("ocelotsignmod.gui.sidebar.color_picker")
                : isAcknowledgmentSelected
                ? Component.translatable("ocelotsignmod.gui.sidebar.ack")
                : (selectedH3 != null ? selectedH3.title : Component.literal(""));
        int h2Width = textRenderer.width(currentH2Title);
        context.text(textRenderer, currentH2Title, UIConstants.SIDEBAR_WIDTH + (mainWidth - h2Width) / 2, (UIConstants.HEADER_HEIGHT - 8) / 2, 0xFFFFFFFF);

        context.enableScissor(UIConstants.SIDEBAR_WIDTH, scrollWindowStartY, width, scrollWindowEndY);

        int contentStartY = scrollWindowStartY + 15 - (int) scrollY;

        if (isDocumentListSelected) {
            HomepageRenderer.render(context, textRenderer, mouseX, mouseY, mainWidth, contentStartY, scrollWindowStartY, scrollWindowEndY);
        } else if (isColorPaletteSelected) {
            ColorPaletteRenderer.render(context, textRenderer, mouseX, mouseY, mainWidth, contentStartY, scrollWindowStartY, scrollWindowEndY);
        } else if (isColorPickerSelected) {
            ColorPickerRenderer.render(context, textRenderer, mainWidth, contentStartY, scrollWindowStartY, scrollWindowEndY);
        } else if (isAcknowledgmentSelected) {
            AcknowledgmentRenderer.render(context, textRenderer, mouseX, mouseY, mainWidth, contentStartY, scrollWindowStartY, scrollWindowEndY);
        } else if (selectedH3 != null) {
            String mishangKey = Component.translatable("ocelotsignmod.gui.categories.mishang_builtin").getString();
            if (selectedH3.title.getString().equals(mishangKey)) {
                MishangIntegration.render(context, textRenderer, width, height, mouseX, mouseY, UIConstants.SIDEBAR_WIDTH);
                context.disableScissor();
                return;
            }
            renderSectionContent(context, textRenderer, mouseX, mouseY, width, mainWidth, contentStartY, scrollWindowStartY, scrollWindowEndY);
        }

        context.disableScissor();

        LayoutHelper.renderScrollbar(context, UIConstants.SIDEBAR_WIDTH, scrollWindowStartY, width - UIConstants.SIDEBAR_WIDTH,
                scrollWindowEndY - scrollWindowStartY, scrollY, maxScrollY, scrollWindowEndY - scrollWindowStartY, mouseX, mouseY);
    }

    /**
     * 判断 H3 下是否存在任意处于字体模式的有效 section。
     * <p>供其他渲染/事件分发模块复用，避免在多处重复实现判定逻辑。
     *
     * @param h3 目标 H3 分类
     * @return 是否存在字体相关 section
     */
    public static boolean hasAnyFontSection(H3Category h3) {
        String defaultFontsKey = Component.translatable("ocelotsignmod.gui.sections.default_fonts").getString();
        String customFontsKey = Component.translatable("ocelotsignmod.gui.sections.custom_fonts").getString();
        for (H4Section section : h3.sections) {
            H4Section effectiveSection = section;
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

    // 渲染分区内容
    private static void renderSectionContent(GuiGraphicsExtractor context, Font textRenderer, int mouseX, int mouseY,
                                             int width, int mainWidth, int contentStartY, int scrollWindowStartY, int scrollWindowEndY) {
        int currentContentY = contentStartY;
        H3Category h3 = selectedH3;

        if (h3.headerText != null) {
            renderHeaderText(context, textRenderer, h3.headerText, mainWidth, currentContentY);
            int lines = textRenderer.split(h3.headerText, mainWidth - 48).size();
            currentContentY += lines * 12 + 16 + 15;
        }

        // 字体渲染警告框（与 headerText 性质相同，只在最顶部出现一次）
        if (hasAnyFontSection(h3)) {
            int warningBoxTopY = currentContentY + 8;
            currentContentY = warningBoxTopY + renderWarningBox(context, textRenderer, mainWidth, warningBoxTopY,
                    scrollWindowStartY, scrollWindowEndY);
        }

        String defaultFontsKey = Component.translatable("ocelotsignmod.gui.sections.default_fonts").getString();
        String customFontsKey = Component.translatable("ocelotsignmod.gui.sections.custom_fonts").getString();

        for (H4Section section : h3.sections) {
            H4Section effectiveSection = section;
            if (section.useStyles && !section.subSections.isEmpty()
                    && section.activeStyleIndex >= 0 && section.activeStyleIndex < section.subSections.size()) {
                effectiveSection = section.subSections.get(section.activeStyleIndex);
            }

            Component titleToRender = effectiveSection.title.getString().isEmpty() ? section.title : effectiveSection.title;
            boolean isDefaultFonts = titleToRender.getString().equals(defaultFontsKey);
            boolean isCustomFonts = titleToRender.getString().equals(customFontsKey);

            // 默认字体特殊布局：描述框 -> 标题 -> 字体列表
            if (isDefaultFonts) {
                // 1. 渲染描述框
                int descBottomY = renderDefaultFontsDescription(context, textRenderer, effectiveSection, mainWidth, currentContentY);

                // 2. 小间距
                currentContentY = descBottomY + 8;

                // 3. 渲染标题（在描述框下方）
                context.text(textRenderer, titleToRender, UIConstants.SIDEBAR_WIDTH + 24, currentContentY, UIConstants.COLOR_SECTION_TITLE, false);
                currentContentY += 12;
            } else if (isCustomFonts) {
                // 自定义字体特殊布局：标题 -> 字体列表
                context.text(textRenderer, titleToRender, UIConstants.SIDEBAR_WIDTH + 24, currentContentY, UIConstants.COLOR_SECTION_TITLE, false);
                currentContentY += 12;
            } else {
                context.text(textRenderer, titleToRender, UIConstants.SIDEBAR_WIDTH + 24, currentContentY, UIConstants.COLOR_SECTION_TITLE, false);
                currentContentY += 12;

                context.text(textRenderer, effectiveSection.description, UIConstants.SIDEBAR_WIDTH + 24, currentContentY, UIConstants.COLOR_DESC_TEXT, false);
                List<FormattedCharSequence> descLines = textRenderer.split(effectiveSection.description, mainWidth - 48);
                currentContentY += descLines.size() * 12 + 10;
            }

            // 颜色子文件夹按钮
            if (section.useSubfolders && !section.subFolders.isEmpty()) {
                renderFilterButtons(context, textRenderer, mouseX, mouseY, width, section.subFolders, section.activeTabIndex, currentContentY);
                currentContentY += 20;
            }

            // 样式切换按钮
            if (section.useStyles && !section.subSections.isEmpty()) {
                List<SubFolderDef> styleTabs = new ArrayList<>();
                for (H4Section child : section.subSections) {
                    styleTabs.add(new SubFolderDef(child.title.getString(), child.title));
                }
                renderFilterButtons(context, textRenderer, mouseX, mouseY, width, styleTabs, section.activeStyleIndex, currentContentY);
                currentContentY += 20;
            }

            if (!section.useSubfolders && !section.useStyles) {
                currentContentY += 20;
            }

            currentContentY = renderSectionItems(context, textRenderer, mouseX, mouseY, mainWidth, scrollWindowStartY, scrollWindowEndY, effectiveSection, currentContentY);
            currentContentY += 12;
        }
    }

    // 渲染头部说明文本
    private static void renderHeaderText(GuiGraphicsExtractor context, Font textRenderer, Component headerText, int mainWidth, int currentY) {
        int descWidth = mainWidth - 48;
        List<FormattedCharSequence> wrappedLines = textRenderer.split(headerText, descWidth);
        int totalDescHeight = wrappedLines.size() * 12 + 16;

        context.fill(UIConstants.SIDEBAR_WIDTH + 20, currentY + 2, UIConstants.SIDEBAR_WIDTH + 28 + descWidth, currentY + 2 + totalDescHeight, UIConstants.COLOR_HEADER_BG_HELP);

        for (int i = 0; i < wrappedLines.size(); i++) {
            context.text(textRenderer, wrappedLines.get(i), UIConstants.SIDEBAR_WIDTH + 24, currentY + 10 + i * 12, UIConstants.COLOR_HEADER_TEXT, false);
        }
    }

    // 渲染默认字体描述
    private static int renderDefaultFontsDescription(GuiGraphicsExtractor context, Font textRenderer, H4Section section, int mainWidth, int currentY) {
        int descWidth = mainWidth - 48;
        List<FormattedCharSequence> wrappedLines = textRenderer.split(section.description, descWidth);
        int totalDescHeight = wrappedLines.size() * 12 + 10;

        context.fill(UIConstants.SIDEBAR_WIDTH + 20, currentY + 8, UIConstants.SIDEBAR_WIDTH + 32 + descWidth, currentY + 8 + totalDescHeight, UIConstants.COLOR_HEADER_BG_FONT);
        context.fill(UIConstants.SIDEBAR_WIDTH + 20, currentY + 8, UIConstants.SIDEBAR_WIDTH + 23, currentY + 8 + totalDescHeight, UIConstants.COLOR_WARNING_BAR);

        for (int i = 0; i < wrappedLines.size(); i++) {
            context.text(textRenderer, wrappedLines.get(i), UIConstants.SIDEBAR_WIDTH + 24, currentY + 12 + i * 12, UIConstants.COLOR_HEADER_TEXT, false);
        }
        return currentY + 8 + totalDescHeight;
    }

    /**
     * 渲染字体渲染兼容性警告框。
     *
     * @param context 绘制上下文
     * @param textRenderer 文本渲染器
     * @param mainWidth 主区域宽度
     * @param currentY 当前 Y 坐标
     * @param scrollWindowStartY 滚动窗口起始 Y
     * @param scrollWindowEndY 滚动窗口结束 Y
     * @return 警告框总高度
     */
    private static int renderWarningBox(GuiGraphicsExtractor context, Font textRenderer, int mainWidth, int currentY,
                                       int scrollWindowStartY, int scrollWindowEndY) {
        Component warningTitle = Component.translatable("ocelotsignmod.gui.sections.font_rendering_warning.title");
        Component warningText = Component.translatable("ocelotsignmod.gui.sections.font_rendering_warning");

        int boxX = UIConstants.SIDEBAR_WIDTH + 20;
        int boxWidth = mainWidth - 40;
        int paddingX = 8;
        int paddingY = 8;
        int titleHeight = 14;
        int lineHeight = 12;
        int gap = 4;

        List<FormattedCharSequence> textLines = textRenderer.split(warningText, boxWidth - paddingX * 2);
        int textHeight = textLines.size() * lineHeight;
        int totalBoxHeight = paddingY + titleHeight + gap + textHeight + paddingY;

        if (currentY + totalBoxHeight >= scrollWindowStartY && currentY <= scrollWindowEndY) {
            // 背景
            context.fill(boxX, currentY, boxX + boxWidth, currentY + totalBoxHeight, UIConstants.COLOR_HEADER_BG_WARNING);
            // 顶部边框
            context.fill(boxX, currentY, boxX + boxWidth, currentY + 1, UIConstants.COLOR_WARNING_BAR);
            // 底部边框
            context.fill(boxX, currentY + totalBoxHeight - 1, boxX + boxWidth, currentY + totalBoxHeight, UIConstants.COLOR_WARNING_BAR);
            // 右侧边框
            context.fill(boxX + boxWidth - 1, currentY, boxX + boxWidth, currentY + totalBoxHeight, UIConstants.COLOR_WARNING_BAR);
            // 左侧橙色竖线
            context.fill(boxX, currentY, boxX + 3, currentY + totalBoxHeight, UIConstants.COLOR_WARNING_BAR);

            // 标题
            context.text(textRenderer, warningTitle, boxX + paddingX + 8, currentY + paddingY, UIConstants.COLOR_WARNING_TEXT, false);

            // 内容
            int textY = currentY + paddingY + titleHeight + gap;
            for (int i = 0; i < textLines.size(); i++) {
                context.text(textRenderer, textLines.get(i), boxX + paddingX + 8, textY + i * lineHeight, UIConstants.COLOR_HEADER_TEXT, false);
            }
        }

        return totalBoxHeight + 8;
    }

    // 渲染筛选按钮
    private static void renderFilterButtons(GuiGraphicsExtractor context, Font textRenderer, int mouseX, int mouseY,
                                           int width, List<SubFolderDef> items, int activeIndex, int currentY) {
        int filterAreaWidth = items.size() * UIConstants.FILTER_BUTTON_WIDTH
                + (items.size() - 1) * UIConstants.FILTER_BUTTON_GAP;
        int filterStartX = width - 24 - filterAreaWidth;

        for (int i = 0; i < items.size(); i++) {
            SubFolderDef subDef = items.get(i);
            int bx = filterStartX + i * (UIConstants.FILTER_BUTTON_WIDTH + UIConstants.FILTER_BUTTON_GAP);

            if (currentY + UIConstants.FILTER_BUTTON_HEIGHT < UIConstants.HEADER_HEIGHT + 1 || currentY > LayoutHelper.getScreenHeight() - UIConstants.FOOTER_HEIGHT) continue;

            boolean isFltHover = LayoutHelper.isMouseInRect(mouseX, mouseY, bx, currentY, UIConstants.FILTER_BUTTON_WIDTH, UIConstants.FILTER_BUTTON_HEIGHT);
            boolean isActive = (activeIndex == i);

            int bgColor = isActive ? 0xFFAAAAAA : (isFltHover ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG);
            int borderColor = isActive ? 0xFF000000 : UIConstants.COLOR_BTN_BORDER;

            context.fill(bx, currentY, bx + UIConstants.FILTER_BUTTON_WIDTH, currentY + UIConstants.FILTER_BUTTON_HEIGHT, bgColor);
            context.outline(bx, currentY, UIConstants.FILTER_BUTTON_WIDTH, UIConstants.FILTER_BUTTON_HEIGHT, borderColor);

            if (isActive) {
                context.fill(bx, currentY + UIConstants.FILTER_BUTTON_HEIGHT - 2,
                        bx + UIConstants.FILTER_BUTTON_WIDTH, currentY + UIConstants.FILTER_BUTTON_HEIGHT,
                        0xFF00AAFF);
            }

            String tabName = subDef.displayName.getString();
            int textColor = isActive ? 0xFF000000 : UIConstants.COLOR_BTN_TEXT;
            List<FormattedCharSequence> lines = textRenderer.split(Component.literal(tabName), UIConstants.FILTER_BUTTON_WIDTH - 4);
            int totalTextHeight = lines.size() * textRenderer.lineHeight;
            int textStartY = currentY + (UIConstants.FILTER_BUTTON_HEIGHT - totalTextHeight) / 2;

            for (int lineIdx = 0; lineIdx < lines.size(); lineIdx++) {
                FormattedCharSequence line = lines.get(lineIdx);
                int lineWidth = textRenderer.width(line);
                int textX = bx + (UIConstants.FILTER_BUTTON_WIDTH - lineWidth) / 2;
                context.text(textRenderer, line, textX, textStartY + lineIdx * textRenderer.lineHeight, textColor, false);
            }
        }
    }

    // 渲染分区条目
    private static int renderSectionItems(GuiGraphicsExtractor context, Font textRenderer, int mouseX, int mouseY,
                                         int mainWidth, int scrollWindowStartY, int scrollWindowEndY,
                                         H4Section section, int currentY) {
        H4Section effectiveSection = section;
        if (section.useStyles && !section.subSections.isEmpty()
                && section.activeStyleIndex >= 0 && section.activeStyleIndex < section.subSections.size()) {
            effectiveSection = section.subSections.get(section.activeStyleIndex);
        }

        if (effectiveSection.isFontMode) {
            return currentY + GridRenderer.renderFontList(context, textRenderer, mouseX, mouseY, mainWidth, scrollWindowStartY, scrollWindowEndY,
                    effectiveSection.fontItems, effectiveSection, UIConstants.SIDEBAR_WIDTH + 30, currentY);
        } else if (effectiveSection.isWhitelistMode) {
            return currentY + GridRenderer.renderWhitelistGrid(context, textRenderer, mouseX, mouseY, mainWidth, scrollWindowStartY, scrollWindowEndY,
                    effectiveSection.whitelistItems, UIConstants.SIDEBAR_WIDTH + 24, currentY);
        } else {
            String tabKey = effectiveSection.useSubfolders && !effectiveSection.subFolders.isEmpty()
                    ? effectiveSection.subFolders.get(effectiveSection.activeTabIndex).dirName : "root";
            var textures = effectiveSection.cachedTextures.get(tabKey);
            return currentY + GridRenderer.renderCachedTextureGrid(context, textRenderer, mouseX, mouseY, mainWidth, scrollWindowStartY, scrollWindowEndY,
                    textures != null ? textures : List.of(), UIConstants.SIDEBAR_WIDTH + 24, currentY);
        }
    }

    // 渲染返回按钮
    private static void renderReturnButton(GuiGraphicsExtractor context, Font textRenderer, int mouseX, int mouseY,
                                          int width, int mainWidth, int height) {
        int returnBtnX = UIConstants.SIDEBAR_WIDTH + (mainWidth - UIConstants.RETURN_BUTTON_WIDTH) / 2;
        int returnBtnY = height - 35;
        boolean hoverRet = LayoutHelper.isMouseInRect(mouseX, mouseY, returnBtnX, returnBtnY, UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT);

        int bgColor = hoverRet ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG;
        context.fill(returnBtnX, returnBtnY, returnBtnX + UIConstants.RETURN_BUTTON_WIDTH, returnBtnY + UIConstants.RETURN_BUTTON_HEIGHT, bgColor);
        context.outline(returnBtnX, returnBtnY, UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT, UIConstants.COLOR_BTN_BORDER);

        Component returnText = Component.translatable("ocelotsignmod.gui.button.back");
        int rtw = textRenderer.width(returnText);
        context.text(textRenderer, returnText, returnBtnX + (UIConstants.RETURN_BUTTON_WIDTH - rtw) / 2, returnBtnY + 7, UIConstants.COLOR_BTN_TEXT, false);
    }

    // ==================== 颜色选择器代理 ====================

    /**
     * 颜色选择器点击处理。
     *
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param mainWidth 主区域宽度
     * @param contentStartY 内容起始 Y
     * @param scrollWindowStartY 滚动窗口起始 Y
     * @param scrollWindowEndY 滚动窗口结束 Y
     * @return 是否处理了点击
     */
    public static boolean handleColorPickerClick(double mouseX, double mouseY, int mainWidth, int contentStartY,
                                                  int scrollWindowStartY, int scrollWindowEndY) {
        return ColorPickerRenderer.handleClick(mouseX, mouseY, mainWidth, contentStartY, scrollWindowStartY, scrollWindowEndY);
    }

    /**
     * 颜色选择器拖动更新。
     *
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     */
    public static void handleColorPickerDrag(double mouseX, double mouseY) {
        if (isDraggingSv) {
            ColorPickerState.applySvFromMouse(mouseX, mouseY, ColorPickerState.getSvPanelX(), ColorPickerState.getSvPanelY(), ColorPickerState.getSvPanelW());
        } else if (isDraggingHue) {
            ColorPickerState.applyHueFromMouse(mouseY, ColorPickerState.getHueBarY(), ColorPickerState.getHueBarH());
        }
    }

    /**
     * 获取颜色选择器内容高度。
     *
     * @param mainWidth 主区域宽度
     * @param textRenderer 文本渲染器
     * @return 内容高度
     */
    public static int getColorPickerContentHeight(int mainWidth, Font textRenderer) {
        return ColorPickerState.getColorPickerContentHeight(mainWidth, textRenderer);
    }

    /**
     * 获取颜色调色板内容高度。
     *
     * @param mainWidth 主区域宽度
     * @param textRenderer 文本渲染器
     * @return 内容高度
     */
    public static int getColorPaletteContentHeight(int mainWidth, Font textRenderer) {
        return ColorPaletteRenderer.getContentHeight(mainWidth, textRenderer);
    }

    /**
     * 颜色调色板点击处理。
     *
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param mainWidth 主区域宽度
     * @param contentStartY 内容起始 Y
     * @param scrollWindowStartY 滚动窗口起始 Y
     * @param scrollWindowEndY 滚动窗口结束 Y
     * @return 是否处理了点击
     */
    public static boolean handleColorPaletteClick(double mouseX, double mouseY, int mainWidth, int contentStartY,
                                                  int scrollWindowStartY, int scrollWindowEndY) {
        return ColorPaletteRenderer.handleClick(mouseX, mouseY, mainWidth, contentStartY, scrollWindowStartY, scrollWindowEndY);
    }

    /**
     * 获取鸣谢页内容高度。
     *
     * @param mainWidth 主区域宽度
     * @param textRenderer 文本渲染器
     * @return 内容高度
     */
    public static int getAcknowledgmentContentHeight(int mainWidth, Font textRenderer) {
        return AcknowledgmentRenderer.getContentHeight(mainWidth, textRenderer);
    }

    /**
     * Mishang 图案点击处理。
     *
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param width 屏幕宽度
     * @param height 屏幕高度
     * @param scrollY 滚动位置
     * @param sidebarWidth 侧边栏宽度
     * @return 是否处理了点击
     */
    public static boolean handleMishangClick(int mouseX, int mouseY, int width, int height, int scrollY, int sidebarWidth) {
        return MishangIntegration.handleClick(mouseX, mouseY, width, height, sidebarWidth);
    }

    // ==================== 插入辅助 ====================

    // 插入纹理到告示牌
    private static void insertTextureToScreen(Identifier identifier) {
        Minecraft client = Minecraft.getInstance();
        if (client.screen instanceof ISignEditorExtension extension) {
            extension.ocelotsign$insertTexture(identifier);
            isVisible = false;
        }
    }

    // 插入文本到告示牌
    private static void insertTextToScreen(String text) {
        Minecraft client = Minecraft.getInstance();
        if (client.screen instanceof ISignEditorExtension extension) {
            extension.ocelotsign$insertText(text);
            isVisible = false;
        }
    }

    // ==================== 事件代理 ====================

    /**
     * 鼠标点击事件处理。
     *
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param button 鼠标按钮
     * @return 是否处理了事件
     */
    public static boolean mouseClicked(double mouseX, double mouseY, int button) {
        return MouseEventHandler.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * 鼠标滚轮事件处理。
     *
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param amount 滚动量
     */
    public static void mouseScrolled(double mouseX, double mouseY, double amount) {
        MouseEventHandler.mouseScrolled(mouseX, mouseY, amount);
    }

    /**
     * 鼠标释放事件处理。
     *
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param button 鼠标按钮
     * @return 是否处理了事件
     */
    public static boolean mouseReleased(double mouseX, double mouseY, int button) {
        return MouseEventHandler.mouseReleased(mouseX, mouseY, button);
    }

    // ==================== URL 悬停状态 ====================

    /** 上一次悬停的 URL。 */
    private static String lastHoveredUrl = null;

    /**
     * 获取上一次悬停的 URL。
     *
     * @return 悬停的 URL
     */
    public static String getLastHoveredUrl() {
        return lastHoveredUrl;
    }

    /**
     * 设置上一次悬停的 URL。
     *
     * @param url 悬停的 URL
     */
    public static void setLastHoveredUrl(String url) {
        lastHoveredUrl = url;
    }

    /**
     * 清除上一次悬停的 URL。
     */
    public static void clearLastHoveredUrl() {
        lastHoveredUrl = null;
    }

    /**
     * 打开主页链接。
     *
     * @param url 链接地址
     * @return 是否成功打开
     */
    public static boolean openHomepageLink(String url) {
        if (url != null && !url.isEmpty()) {
            Util.getPlatform().openUri(url);
            return true;
        }
        return false;
    }
}
