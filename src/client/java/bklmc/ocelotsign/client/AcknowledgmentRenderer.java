package bklmc.ocelotsign.client;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * 鸣谢与模组声明页渲染器
 *
 * @see PatternAndFontOverlay
 */
public final class AcknowledgmentRenderer {

    private AcknowledgmentRenderer() {
    }

    /**
     * 鸣谢条目数据模型
     */
    public static final class Entry {
        public final Text label;
        public final String url;
        public final Text buttonText;

        public Entry(Text label, String url, Text buttonText) {
            this.label = label;
            this.url = url;
            this.buttonText = buttonText;
        }

        public Entry(Text label, String url) {
            this(label, url, null);
        }

        public Entry(Text label) {
            this(label, null, null);
        }
    }

    /**
     * 鸣谢内容区块数据模型
     */
    public static final class Block {
        public final Text title;
        public final int titleColor;
        public final List<Text> introLines = new ArrayList<>();
        public final List<Entry> entries = new ArrayList<>();
        public Text quoteTitle;
        public final List<Text> quoteLines = new ArrayList<>();
        public boolean twoColumn = false;
        public Text col1Header;
        public Text col2Header;

        public Block(Text title, int titleColor) {
            this.title = title;
            this.titleColor = titleColor;
        }
    }

    /**
     * 渲染整个鸣谢页面内容，返回占用总高度。
     *
     * @param context 绘制上下文
     * @param textRenderer 文本渲染器
     * @param mouseX 鼠标 X 坐标
     * @param mouseY 鼠标 Y 坐标
     * @param mainWidth 主区域宽度
     * @param contentStartY 内容起始 Y 坐标
     * @param scrollWindowStartY 滚动窗口起始 Y 坐标
     * @param scrollWindowEndY 滚动窗口结束 Y 坐标
     * @return 内容总高度（像素）
     */
    public static int render(DrawContext context, TextRenderer textRenderer,
                             int mouseX, int mouseY, int mainWidth, int contentStartY,
                             int scrollWindowStartY, int scrollWindowEndY) {
        PatternAndFontOverlay.clearLastHoveredUrl();

        int currentY = contentStartY;

        // 页面顶部大标题
        currentY = renderBigTitle(context, textRenderer, mainWidth, currentY,
                scrollWindowStartY, scrollWindowEndY);

        // 鸣谢区
        Block ack = buildAcknowledgmentBlock();
        currentY = renderBlock(context, textRenderer, mouseX, mouseY,
                mainWidth, currentY, scrollWindowStartY, scrollWindowEndY, ack);

        // 模组声明区
        List<Block> declBlocks = buildDeclarationBlocks();
        for (Block decl : declBlocks) {
            currentY = renderBlock(context, textRenderer, mouseX, mouseY,
                    mainWidth, currentY, scrollWindowStartY, scrollWindowEndY, decl);
        }

        currentY += 30;
        return currentY;
    }

    /**
     * 获取内容区总高度，用于滚动计算（与 render 一一对应）。
     *
     * @param mainWidth 主区域宽度
     * @param textRenderer 文本渲染器
     * @return 内容总高度（像素）
     */
    public static int getContentHeight(int mainWidth, TextRenderer textRenderer) {
        int height = estimateBigTitleHeight(textRenderer, mainWidth);

        Block ack = buildAcknowledgmentBlock();
        height += estimateBlockHeight(textRenderer, mainWidth, ack);

        List<Block> declBlocks = buildDeclarationBlocks();
        for (Block decl : declBlocks) {
            height += estimateBlockHeight(textRenderer, mainWidth, decl);
        }

        height += 30;
        return height;
    }

    /**
     * 构建鸣谢区块数据。
     */
    private static Block buildAcknowledgmentBlock() {
        Block b = new Block(
                Text.translatable("ocelotsignmod.gui.ack.section.ack_title"),
                UIConstants.COLOR_HOMEPAGE_SECTION_TITLE);
        b.introLines.add(Text.translatable("ocelotsignmod.gui.ack.section.ack_p1"));

        // Column headers (rendered inside the card)
        b.col1Header = Text.translatable("ocelotsignmod.gui.ack.column.ocelot_dev");
        b.col2Header = Text.translatable("ocelotsignmod.gui.ack.column.mishang_dev");

        // Column 1: Ocelot Sign Mod Developers
        b.entries.add(new Entry(Text.translatable("ocelotsignmod.gui.ack.section.ack_contributors_label")));

        // Column 2: MishangUC Development Team
        b.entries.add(new Entry(Text.translatable("ocelotsignmod.gui.ack.section.ack_mishang_dev_label")));

        b.twoColumn = true;
        return b;
    }

    /**
     * 构建所有声明区块数据。
     */
    private static List<Block> buildDeclarationBlocks() {
        List<Block> blocks = new ArrayList<>();

        Block b1 = new Block(
                Text.translatable("ocelotsignmod.gui.ack.mod.mishang.h2"),
                0xFF2A6F2A);
        b1.entries.add(new Entry(Text.translatable("ocelotsignmod.gui.ack.mod.mishang.label.name")));
        b1.entries.add(new Entry(
                Text.translatable("ocelotsignmod.gui.ack.mod.mishang.label.license"),
                "https://www.gnu.org/licenses/lgpl-3.0.html",
                Text.translatable("ocelotsignmod.gui.ack.link.gpl")));
        b1.entries.add(new Entry(
                Text.translatable("ocelotsignmod.gui.ack.mod.mishang.label.repo"),
                "https://github.com/SolidBlock-cn/mishanguc",
                Text.translatable("ocelotsignmod.gui.ack.link.github_repo")));
        b1.entries.add(new Entry(Text.translatable("ocelotsignmod.gui.ack.mod.mishang.label.note")));
        // 引用块（MISHANGUC）
        b1.quoteTitle = Text.translatable("ocelotsignmod.gui.ack.quote_excerpt_title");
        b1.quoteLines.add(Text.translatable("ocelotsignmod.gui.ack.mod.mishang.quote"));
        blocks.add(b1);

        /**
         * 字体使用声明。
         */
        Block b2 = new Block(
                Text.translatable("ocelotsignmod.gui.ack.fonts.h2"),
                0xFF2A6F2A);
        b2.introLines.add(Text.translatable("ocelotsignmod.gui.ack.fonts.p1"));
        blocks.add(b2);

        // MiSans
        Block b3 = new Block(
                Text.translatable("ocelotsignmod.gui.ack.font.misans.h3"),
                0xFF2A6F2A);
        b3.entries.add(new Entry(Text.translatable("ocelotsignmod.gui.ack.font.misans.label.owner")));
        b3.entries.add(new Entry(Text.translatable("ocelotsignmod.gui.ack.font.misans.label.license")));
        b3.entries.add(new Entry(
                Text.translatable("ocelotsignmod.gui.ack.font.misans.label.link"),
                "https://hyperos.mi.com/font/zh/",
                Text.translatable("ocelotsignmod.gui.ack.link.xiaomi")));
        b3.entries.add(new Entry(
                Text.translatable("ocelotsignmod.gui.ack.font.misans.label.license_link"),
                "https://hyperos.mi.com/font/zh/faq/",
                Text.translatable("ocelotsignmod.gui.ack.link.misans_license")));
        blocks.add(b3);

        // Roadgeek 2014
        Block b4 = new Block(
                Text.translatable("ocelotsignmod.gui.ack.font.roadgeek.h3"),
                0xFF2A6F2A);
        b4.entries.add(new Entry(Text.translatable("ocelotsignmod.gui.ack.font.roadgeek.label.owner")));
        b4.entries.add(new Entry(Text.translatable("ocelotsignmod.gui.ack.font.roadgeek.label.license")));
        b4.entries.add(new Entry(
                Text.translatable("ocelotsignmod.gui.ack.font.roadgeek.label.link"),
                "https://github.com/sammdot/roadgeek-fonts",
                Text.translatable("ocelotsignmod.gui.ack.link.github_repo")));
        blocks.add(b4);

        // Source Han Sans
        Block b5 = new Block(
                Text.translatable("ocelotsignmod.gui.ack.font.sans.h3"),
                0xFF2A6F2A);
        b5.entries.add(new Entry(Text.translatable("ocelotsignmod.gui.ack.font.sans.label.license")));
        b5.entries.add(new Entry(
                Text.translatable("ocelotsignmod.gui.ack.font.sans.label.link"),
                "https://github.com/adobe-fonts/source-han-sans",
                Text.translatable("ocelotsignmod.gui.ack.link.github_repo")));
        blocks.add(b5);

        // Source Han Serif
        Block b6 = new Block(
                Text.translatable("ocelotsignmod.gui.ack.font.serif.h3"),
                0xFF2A6F2A);
        b6.entries.add(new Entry(Text.translatable("ocelotsignmod.gui.ack.font.serif.label.license")));
        b6.entries.add(new Entry(
                Text.translatable("ocelotsignmod.gui.ack.font.serif.label.link"),
                "https://github.com/adobe-fonts/source-han-serif",
                Text.translatable("ocelotsignmod.gui.ack.link.github_repo")));
        blocks.add(b6);

        return blocks;
    }

    /**
     * 添加一种字体声明的多行条目，并附带仓库链接。
     */
    private static void addFontEntries(Block b, String prefix, String repoUrl, Text repoBtnText) {
        b.entries.add(new Entry(Text.translatable("ocelotsignmod.gui.ack.font." + prefix + ".h3")));
        b.entries.add(new Entry(Text.translatable("ocelotsignmod.gui.ack.font." + prefix + ".label.owner")));
        b.entries.add(new Entry(Text.translatable("ocelotsignmod.gui.ack.font." + prefix + ".label.license")));
        b.entries.add(new Entry(
                Text.translatable("ocelotsignmod.gui.ack.font." + prefix + ".label.link"),
                repoUrl,
                repoBtnText));
    }

    /**
     * 添加字体声明条目，license 行额外附带 OFL 协议链接。
     */
    private static void addFontEntriesWithOflLicense(Block b, String prefix, String repoUrl) {
        b.entries.add(new Entry(Text.translatable("ocelotsignmod.gui.ack.font." + prefix + ".h3")));
        b.entries.add(new Entry(Text.translatable("ocelotsignmod.gui.ack.font." + prefix + ".label.license")));
        b.entries.add(new Entry(
                Text.translatable("ocelotsignmod.gui.ack.font." + prefix + ".label.license_link"),
                "https://openfontlicense.org",
                Text.translatable("ocelotsignmod.gui.ack.link.ofl")));
        b.entries.add(new Entry(
                Text.translatable("ocelotsignmod.gui.ack.font." + prefix + ".label.link"),
                repoUrl,
                Text.translatable("ocelotsignmod.gui.ack.link.github_repo")));
    }

    /**
     * 渲染页面大标题。
     */
    private static int renderBigTitle(DrawContext context, TextRenderer textRenderer, int mainWidth,
                                      int currentY, int scrollWindowStartY, int scrollWindowEndY) {
        Text title = Text.translatable("ocelotsignmod.gui.ack.title");
        int titleWidth = textRenderer.getWidth(title);
        int titleX = UIConstants.SIDEBAR_WIDTH + (mainWidth - titleWidth) / 2;
        if (currentY + 22 >= scrollWindowStartY && currentY <= scrollWindowEndY) {
            context.drawText(textRenderer, title, titleX, currentY, 0xFF1A1A1A, false);
        }
        currentY += 24;

        if (currentY + 4 >= scrollWindowStartY && currentY <= scrollWindowEndY) {
            context.fill(UIConstants.SIDEBAR_WIDTH + 60, currentY,
                    UIConstants.SIDEBAR_WIDTH + mainWidth - 60, currentY + 2,
                    UIConstants.COLOR_HOMEPAGE_DIVIDER);
        }
        currentY += 14;
        return currentY;
    }

    /**
     * 估算页面大标题高度。
     */
    private static int estimateBigTitleHeight(TextRenderer textRenderer, int mainWidth) {
        Text title = Text.translatable("ocelotsignmod.gui.ack.title");
        List<OrderedText> lines = textRenderer.wrapLines(title, mainWidth - 60);
        return lines.size() * 22 + 2 + 14;
    }

    /**
     * 渲染一个内容区块（含标题、介绍、条目、引用）。
     */
    private static int renderBlock(DrawContext context, TextRenderer textRenderer,
                                   int mouseX, int mouseY, int mainWidth, int currentY,
                                   int scrollWindowStartY, int scrollWindowEndY, Block block) {
        // 章节大标题
        if (currentY + 22 >= scrollWindowStartY && currentY <= scrollWindowEndY) {
            context.drawText(textRenderer, block.title,
                    UIConstants.SIDEBAR_WIDTH + 24, currentY, block.titleColor, false);
        }
        currentY += 24;

        // intro 行
        for (Text t : block.introLines) {
            currentY = renderWrappedParagraph(context, textRenderer, t,
                    UIConstants.SIDEBAR_WIDTH + 24, mainWidth - 48,
                    currentY, scrollWindowStartY, scrollWindowEndY,
                    UIConstants.COLOR_HOMEPAGE_BODY, 14, 6);
            currentY += 10;
        }

        // 条目卡片
        if (!block.entries.isEmpty()) {
            currentY += 4;
            if (block.twoColumn) {
                currentY = renderTwoColumnEntries(context, textRenderer, mouseX, mouseY,
                        mainWidth, currentY, scrollWindowStartY, scrollWindowEndY,
                        block.entries, block.col1Header, block.col2Header);
            } else {
                currentY = renderEntries(context, textRenderer, mouseX, mouseY,
                        mainWidth, currentY, scrollWindowStartY, scrollWindowEndY,
                        block.entries);
            }
        }

        // 引用块
        if (block.quoteTitle != null || !block.quoteLines.isEmpty()) {
            currentY += 6;
            currentY = renderQuote(context, textRenderer, mainWidth, currentY,
                    scrollWindowStartY, scrollWindowEndY, block);
        }

        currentY += 28;
        return currentY;
    }

    /**
     * 估算内容区块总高度。
     */
    private static int estimateBlockHeight(TextRenderer textRenderer, int mainWidth, Block block) {
        int height = 24;
        for (Text t : block.introLines) {
            List<OrderedText> lines = textRenderer.wrapLines(t, mainWidth - 48);
            height += lines.size() * 14 + 6 + 10;
        }
        if (!block.entries.isEmpty()) {
            height += 4;
            if (block.twoColumn) {
                height += estimateTwoColumnEntriesHeight(textRenderer, mainWidth, block.entries);
            } else {
                height += estimateEntriesHeight(textRenderer, mainWidth, block.entries);
            }
        }
        if (block.quoteTitle != null || !block.quoteLines.isEmpty()) {
            height += 6;
            height += estimateQuoteHeight(textRenderer, mainWidth, block);
        }
        height += 28;
        return height;
    }

    /**
     * 渲染单列条目卡片列表。
     */
    private static int renderEntries(DrawContext context, TextRenderer textRenderer,
                                     int mouseX, int mouseY, int mainWidth, int currentY,
                                     int scrollWindowStartY, int scrollWindowEndY,
                                     List<Entry> entries) {
        int blockX = UIConstants.SIDEBAR_WIDTH + 24;
        int blockW = mainWidth - 48;

        int totalH = estimateEntriesHeight(textRenderer, mainWidth, entries);
        if (currentY + totalH >= scrollWindowStartY && currentY <= scrollWindowEndY) {
            // 卡片背景
            context.fill(blockX, currentY, blockX + blockW, currentY + totalH, 0xFFFAFAFA);
            context.fill(blockX + blockW - 1, currentY, blockX + blockW, currentY + totalH, 0xFFE0E0E0);
            context.fill(blockX, currentY + totalH - 1, blockX + blockW, currentY + totalH, 0xFFE0E0E0);
            context.fill(blockX, currentY, blockX + 1, currentY + totalH, 0xFFE0E0E0);
            context.fill(blockX, currentY, blockX + blockW, currentY + 1, 0xFFE0E0E0);
            // 左侧色条
            context.fill(blockX, currentY, blockX + 3, currentY + totalH, 0xFF888888);
        }

        int y = currentY + 6;
        for (Entry e : entries) {
            y = renderEntry(context, textRenderer, mouseX, mouseY,
                    blockX, blockW, y, scrollWindowStartY, scrollWindowEndY, e);
            y += 4;
        }
        return currentY + totalH;
    }

    /**
     * 渲染双列条目卡片（鸣谢页面专用）。
     *
     * <p>假设 entries 顺序为：列1内容, 列2内容。
     */
    private static int renderTwoColumnEntries(DrawContext context, TextRenderer textRenderer,
                                              int mouseX, int mouseY, int mainWidth, int currentY,
                                              int scrollWindowStartY, int scrollWindowEndY,
                                              List<Entry> entries, Text col1Header, Text col2Header) {
        int blockX = UIConstants.SIDEBAR_WIDTH + 24;
        int blockW = mainWidth - 48;
        int gap = 16;
        int colW = (blockW - gap) / 2;
        int leftX = blockX;
        int rightX = blockX + colW + gap;

        int headerH = 18;
        int leftH = estimateEntryHeight(textRenderer, colW, entries.get(0));
        int rightH = entries.size() > 1 ? estimateEntryHeight(textRenderer, colW, entries.get(1)) : 0;
        int contentH = Math.max(leftH, rightH);
        int totalH = headerH + contentH;

        if (currentY + totalH >= scrollWindowStartY && currentY <= scrollWindowEndY) {
            // 内容区域背景
            context.fill(blockX, currentY + headerH, blockX + blockW, currentY + totalH, 0xFFFAFAFA);
            // 顶部标题栏背景（蓝色）
            context.fill(blockX, currentY, blockX + blockW, currentY + headerH, 0xFF1A6EB5);
            // 左侧色条（蓝色）
            context.fill(blockX, currentY, blockX + 3, currentY + totalH, 0xFF1A6EB5);
            // 底部边框
            context.fill(blockX, currentY + totalH - 1, blockX + blockW, currentY + totalH, UIConstants.COLOR_HOMEPAGE_CARD_BORDER);
            // 右侧边框
            context.fill(blockX + blockW - 1, currentY, blockX + blockW, currentY + totalH, UIConstants.COLOR_HOMEPAGE_CARD_BORDER);
            // 中间分隔线（从标题栏底部到内容区底部）
            context.fill(leftX + colW + gap / 2 - 1, currentY + headerH, leftX + colW + gap / 2, currentY + totalH, 0xFFE0E0E0);

            // 列标题（白色文字在蓝色背景上）
            int headerY = currentY + 5;
            int headerColor = 0xFFFFFFFF;
            context.drawText(textRenderer, col1Header, leftX + 8, headerY, headerColor, false);
            if (col2Header != null) {
                context.drawText(textRenderer, col2Header, rightX + 8, headerY, headerColor, false);
            }
        }

        int contentY = currentY + headerH + 6;

        // 渲染左列
        renderEntry(context, textRenderer, mouseX, mouseY,
                leftX, colW, contentY, scrollWindowStartY, scrollWindowEndY, entries.get(0));

        // 渲染右列
        if (entries.size() > 1) {
            renderEntry(context, textRenderer, mouseX, mouseY,
                    rightX, colW, contentY, scrollWindowStartY, scrollWindowEndY, entries.get(1));
        }

        return currentY + totalH;
    }

    /**
     * 估算双列条目卡片总高度。
     */
    private static int estimateTwoColumnEntriesHeight(TextRenderer textRenderer, int mainWidth, List<Entry> entries) {
        int blockW = mainWidth - 48;
        int gap = 16;
        int colW = (blockW - gap) / 2;
        int headerH = 18;
        int leftH = entries.get(0) != null ? estimateEntryHeight(textRenderer, colW, entries.get(0)) : 0;
        int rightH = entries.size() > 1 && entries.get(1) != null ? estimateEntryHeight(textRenderer, colW, entries.get(1)) : 0;
        int contentH = Math.max(leftH, rightH);
        return headerH + contentH + 6; // header + content + padding
    }

    /**
     * 估算单个条目高度。
     */
    private static int estimateEntryHeight(TextRenderer textRenderer, int colW, Entry entry) {
        int padding = 6;
        int innerW = colW - padding * 2;
        List<OrderedText> labelLines = textRenderer.wrapLines(entry.label, innerW - 8);
        int labelHeight = labelLines.size() * 12;
        int entryHeight = labelHeight + padding * 2;
        if (entry.url != null && !entry.url.isEmpty()) {
            entryHeight += 24 + 4;
        }
        return entryHeight;
    }

    /**
     * 渲染单个条目。
     */
    private static int renderEntry(DrawContext context, TextRenderer textRenderer,
                                   int mouseX, int mouseY,
                                   int blockX, int blockW, int currentY,
                                   int scrollWindowStartY, int scrollWindowEndY,
                                   Entry entry) {
        int padding = 6;
        int innerX = blockX + padding;
        int innerW = blockW - padding * 2;

        List<OrderedText> labelLines = textRenderer.wrapLines(entry.label, innerW - 8);
        int labelHeight = labelLines.size() * 12;

        int entryHeight = labelHeight + padding * 2;
        if (entry.url != null && !entry.url.isEmpty()) {
            entryHeight += 24 + 4;
        }

        if (currentY + entryHeight >= scrollWindowStartY && currentY <= scrollWindowEndY) {
            // 文本
            for (int i = 0; i < labelLines.size(); i++) {
                context.drawText(textRenderer, labelLines.get(i),
                        innerX + 4, currentY + padding + i * 12,
                        0xFF333333, false);
            }
            // 按钮
            if (entry.url != null && !entry.url.isEmpty()) {
                int btnY = currentY + padding + labelHeight + 4;
                renderUrlButton(context, textRenderer, mouseX, mouseY,
                        innerX + 4, btnY, innerW - 16, entry.url, entry.buttonText,
                        scrollWindowStartY, scrollWindowEndY);
            }
        }
        return currentY + entryHeight;
    }

    /**
     * 估算单列条目列表总高度。
     */
    private static int estimateEntriesHeight(TextRenderer textRenderer, int mainWidth, List<Entry> entries) {
        int blockW = mainWidth - 48;
        int padding = 6;
        int innerW = blockW - padding * 2;
        int total = 12;
        for (Entry e : entries) {
            List<OrderedText> labelLines = textRenderer.wrapLines(e.label, innerW - 8);
            int labelHeight = labelLines.size() * 12;
            int entryHeight = labelHeight + padding * 2;
            if (e.url != null && !e.url.isEmpty()) {
                entryHeight += 24 + 4;
            }
            total += entryHeight + 4;
        }
        return total;
    }

    /**
     * 渲染 URL 链接按钮。
     */
    private static void renderUrlButton(DrawContext context, TextRenderer textRenderer,
                                        int mouseX, int mouseY,
                                        int x, int y, int maxWidth,
                                        String url, Text customText,
                                        int scrollWindowStartY, int scrollWindowEndY) {
        Text display = customText != null ? customText : simplifyUrl(url);
        int btnW = Math.max(40, maxWidth);
        int minH = 22;

        List<OrderedText> wrapped = textRenderer.wrapLines(display, btnW - 16);
        int actualH = Math.max(minH, wrapped.size() * 10 + 6);

        boolean isVisible = y + actualH >= scrollWindowStartY && y <= scrollWindowEndY;
        boolean isHover = isVisible && LayoutHelper.isMouseInRect(mouseX, mouseY, x, y, btnW, actualH);

        if (isVisible) {
            int bgColor = isHover ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG;
            context.fill(x, y, x + btnW, y + actualH, bgColor);
            context.drawBorder(x, y, btnW, actualH, UIConstants.COLOR_BTN_BORDER);

            int color = isHover ? UIConstants.COLOR_LINK_HOVER : UIConstants.COLOR_LINK_NORMAL;
            int textY = y + (actualH - wrapped.size() * 10) / 2;
            for (int i = 0; i < wrapped.size(); i++) {
                int lw = textRenderer.getWidth(wrapped.get(i));
                context.drawText(textRenderer, wrapped.get(i),
                        x + (btnW - lw) / 2, textY + i * 10, color, false);
            }
        }
        if (isHover) {
            PatternAndFontOverlay.setLastHoveredUrl(url);
        }
    }

    /**
     * 简化 URL 为短显示文本。
     */
    private static Text simplifyUrl(String url) {
        if (url == null || url.isEmpty()) return Text.empty();
        String trimmed = url;
        if (trimmed.startsWith("https://")) trimmed = trimmed.substring(8);
        else if (trimmed.startsWith("http://")) trimmed = trimmed.substring(7);
        if (trimmed.length() > 32) {
            return Text.literal(trimmed.substring(0, 29) + "...");
        }
        return Text.literal(trimmed);
    }

    /**
     * 渲染引用块。
     */
    private static int renderQuote(DrawContext context, TextRenderer textRenderer,
                                   int mainWidth, int currentY,
                                   int scrollWindowStartY, int scrollWindowEndY,
                                   Block block) {
        int blockX = UIConstants.SIDEBAR_WIDTH + 24;
        int blockW = mainWidth - 48;
        int padding = 10;
        int innerX = blockX + padding;
        int innerW = blockW - padding * 2;

        int totalH = estimateQuoteHeight(textRenderer, mainWidth, block);

        if (currentY + totalH >= scrollWindowStartY && currentY <= scrollWindowEndY) {
            context.fill(blockX, currentY, blockX + blockW, currentY + totalH, UIConstants.COLOR_HEADER_BG_FONT);
            context.fill(blockX, currentY, blockX + 4, currentY + totalH, UIConstants.COLOR_SECTION_TITLE);

            int y = currentY + padding;
            if (block.quoteTitle != null) {
                context.drawText(textRenderer, block.quoteTitle,
                        innerX, y, UIConstants.COLOR_SECTION_TITLE, false);
                y += 16 + 6;
            }
            for (Text t : block.quoteLines) {
                List<OrderedText> lines = textRenderer.wrapLines(t, innerW - 8);
                for (OrderedText line : lines) {
                    context.drawText(textRenderer, line, innerX, y, UIConstants.COLOR_DESC_TEXT, false);
                    y += 13;
                }
                y += 4;
            }
        }
        return currentY + totalH;
    }

    /**
     * 估算引用块高度。
     */
    private static int estimateQuoteHeight(TextRenderer textRenderer, int mainWidth, Block block) {
        int blockW = mainWidth - 48;
        int padding = 10;
        int innerW = blockW - padding * 2;
        int height = padding;
        if (block.quoteTitle != null) height += 16 + 6;
        for (Text t : block.quoteLines) {
            List<OrderedText> lines = textRenderer.wrapLines(t, innerW - 8);
            height += lines.size() * 13 + 4;
        }
        return height;
    }

    /**
     * 渲染换行文本段落。
     */
    private static int renderWrappedParagraph(DrawContext context, TextRenderer textRenderer,
                                              Text text, int x, int maxWidth, int currentY,
                                              int scrollWindowStartY, int scrollWindowEndY,
                                              int color, int lineHeight, int paraGap) {
        String raw = text.getString();
        String[] paragraphs = raw.split("\n");
        for (int p = 0; p < paragraphs.length; p++) {
            List<OrderedText> lines = textRenderer.wrapLines(Text.literal(paragraphs[p]), maxWidth);
            for (int i = 0; i < lines.size(); i++) {
                if (currentY + lineHeight >= scrollWindowStartY && currentY <= scrollWindowEndY) {
                    context.drawText(textRenderer, lines.get(i), x, currentY, color, false);
                }
                currentY += lineHeight;
            }
            if (p < paragraphs.length - 1) currentY += paraGap;
        }
        return currentY;
    }
}
