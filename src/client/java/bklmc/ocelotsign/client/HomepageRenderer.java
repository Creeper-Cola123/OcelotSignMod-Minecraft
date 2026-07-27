package bklmc.ocelotsign.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.List;

/**
 * 文档列表（主页）渲染逻辑 (1.19.2 兼容版)
 *
 * @see PatternAndFontOverlay
 */
public final class HomepageRenderer {

    private HomepageRenderer() {
    }

    /**
     * 渲染主页/文档列表页完整内容。
     */
    public static int render(MatrixStack matrices, TextRenderer textRenderer, int mouseX, int mouseY,
                             int mainWidth, int contentStartY, int scrollWindowStartY, int scrollWindowEndY) {
        int currentY = contentStartY;

        // 绘制头图
        int headerImageX = UIConstants.SIDEBAR_WIDTH;
        int headerImageWidth = mainWidth;
        int headerImageHeight = (int) (headerImageWidth * 0.3);
        int headerImageY = currentY;

        if (headerImageY + headerImageHeight >= scrollWindowStartY && headerImageY <= scrollWindowEndY) {
            Identifier headerImage = new Identifier("ocelotsignmod", "textures/image/bg1.png");
            RenderSystem.setShaderTexture(0, headerImage);
            DrawableHelper.drawTexture(matrices, headerImageX, headerImageY, 0, 0, headerImageWidth, headerImageHeight, headerImageWidth, headerImageHeight);
        }
        currentY += headerImageHeight + 10;

        // 主标题
        Text title = Text.translatable("ocelotsignmod.gui.homepage.title");
        int titleWidth = textRenderer.getWidth(title);
        textRenderer.draw(matrices, title, UIConstants.SIDEBAR_WIDTH + (mainWidth - titleWidth) / 2, currentY, 0x0066CC);
        currentY += 26;

        // 标题下划线
        int dividerY = currentY;
        DrawableHelper.fill(matrices, UIConstants.SIDEBAR_WIDTH + 40, dividerY, LayoutHelper.getScreenWidth() - 40, dividerY + 2, UIConstants.COLOR_HOMEPAGE_DIVIDER);
        currentY += 20;

        // 副标题
        Text welcome = Text.translatable("ocelotsignmod.gui.homepage.welcome");
        int welcomeWidth = textRenderer.getWidth(welcome);
        textRenderer.draw(matrices, welcome, UIConstants.SIDEBAR_WIDTH + (mainWidth - welcomeWidth) / 2, currentY, UIConstants.COLOR_HOMEPAGE_SUBTITLE);
        currentY += 28;

        // 提示文字
        currentY = renderHintText(matrices, textRenderer, mainWidth, currentY, scrollWindowStartY, scrollWindowEndY);

        // Introduction 章节
        currentY += 10;
        currentY = renderIntroSection(matrices, textRenderer, mainWidth, currentY, scrollWindowStartY, scrollWindowEndY);

        // Project Links 章节
        currentY = renderLinksSection(matrices, textRenderer, mouseX, mouseY, mainWidth, currentY, scrollWindowStartY, scrollWindowEndY);

        // 免责声明
        currentY = renderDisclaimerSection(matrices, textRenderer, mainWidth, currentY, scrollWindowStartY, scrollWindowEndY);

        currentY += 45;
        return currentY;
    }

    /**
     * 渲染提示文本区域。
     */
    private static int renderHintText(MatrixStack matrices, TextRenderer textRenderer,
                                      int mainWidth, int currentY, int scrollWindowStartY, int scrollWindowEndY) {
        Text hintText = Text.translatable("ocelotsignmod.gui.homepage.hint");
        int hintPaddingY = 5;
        int hintX = UIConstants.SIDEBAR_WIDTH + 20;
        int hintMaxWidth = mainWidth - 40;
        List<OrderedText> hintLines = textRenderer.wrapLines(hintText, hintMaxWidth);
        int hintBgHeight = hintLines.size() * 12 + hintPaddingY * 2;
        if (currentY + hintBgHeight >= scrollWindowStartY && currentY <= scrollWindowEndY) {
            DrawableHelper.fill(matrices, hintX, currentY, hintX + hintMaxWidth, currentY + hintBgHeight, UIConstants.COLOR_HEADER_BG_HELP);
            int textY = currentY + hintPaddingY + (hintBgHeight - hintLines.size() * 12) / 2;
            for (int i = 0; i < hintLines.size(); i++) {
                int lineW = textRenderer.getWidth(hintLines.get(i));
                textRenderer.draw(matrices, hintLines.get(i), hintX + (hintMaxWidth - lineW) / 2, textY + i * 12, UIConstants.COLOR_HEADER_TEXT);
            }
        }
        return currentY + hintBgHeight + 12;
    }

    /**
     * 渲染介绍章节。
     */
    private static int renderIntroSection(MatrixStack matrices, TextRenderer textRenderer,
                                          int mainWidth, int currentY, int scrollWindowStartY, int scrollWindowEndY) {
        Text introTitle = Text.translatable("ocelotsignmod.gui.homepage.section.intro");
        textRenderer.draw(matrices, introTitle, UIConstants.SIDEBAR_WIDTH + 20, currentY, UIConstants.COLOR_HOMEPAGE_SECTION_TITLE);
        currentY += 22;

        int introX = UIConstants.SIDEBAR_WIDTH + 20;
        int introMaxWidth = mainWidth - 40;
        int introLineHeight = 15;
        int introParaGap = 20;

        Text introP1 = Text.translatable("ocelotsignmod.gui.homepage.intro.p1");
        currentY = renderTextBlock(matrices, textRenderer, introP1, introX, introMaxWidth, currentY, scrollWindowStartY, scrollWindowEndY, UIConstants.COLOR_HOMEPAGE_BODY, introLineHeight);
        currentY += introParaGap;

        Text introP2 = Text.translatable("ocelotsignmod.gui.homepage.intro.p2");
        currentY = renderTextBlock(matrices, textRenderer, introP2, introX, introMaxWidth, currentY, scrollWindowStartY, scrollWindowEndY, UIConstants.COLOR_HOMEPAGE_BODY, introLineHeight);
        currentY += introParaGap;

        Text introP3 = Text.translatable("ocelotsignmod.gui.homepage.intro.p3");
        currentY = renderTextBlock(matrices, textRenderer, introP3, introX, introMaxWidth, currentY, scrollWindowStartY, scrollWindowEndY, UIConstants.COLOR_HOMEPAGE_BODY, introLineHeight);

        currentY += 45;
        return currentY;
    }

    /**
     * 渲染项目链接章节（双列卡片布局）。
     */
    private static int renderLinksSection(MatrixStack matrices, TextRenderer textRenderer, int mouseX, int mouseY,
                                          int mainWidth, int currentY, int scrollWindowStartY, int scrollWindowEndY) {
        // 分割线
        int sectionDividerY = currentY;
        DrawableHelper.fill(matrices, UIConstants.SIDEBAR_WIDTH + 40, sectionDividerY, LayoutHelper.getScreenWidth() - 40, sectionDividerY + 1, UIConstants.COLOR_HOMEPAGE_DIVIDER);
        currentY += 20;

        // 章节标题
        currentY += 5;
        Text linksTitle = Text.translatable("ocelotsignmod.gui.homepage.section.links");
        textRenderer.draw(matrices, linksTitle, UIConstants.SIDEBAR_WIDTH + 20, currentY, UIConstants.COLOR_HOMEPAGE_SECTION_TITLE);
        currentY += 22;

        // 两列卡片布局
        int columnWidth = (mainWidth - 60) / 2;
        int leftColumnX = UIConstants.SIDEBAR_WIDTH + 20;
        int rightColumnX = leftColumnX + columnWidth + 20;
        int cardStartY = currentY;

        int padding = 12;
        int fixedCardHeight = calculateCardHeight(textRenderer, columnWidth, padding,
                Text.translatable("ocelotsignmod.gui.homepage.mishang.title"),
                "https://github.com/SolidBlock-cn/mishanguc",
                "https://www.mcmod.cn/class/5743.html");

        int leftCardEndY = renderCard(matrices, textRenderer, mouseX, mouseY,
                leftColumnX, cardStartY, columnWidth,
                Text.translatable("ocelotsignmod.gui.homepage.mishang.title"),
                "https://github.com/SolidBlock-cn/mishanguc",
                "https://www.mcmod.cn/class/5743.html",
                scrollWindowStartY, scrollWindowEndY, fixedCardHeight);

        int rightCardEndY = renderCard(matrices, textRenderer, mouseX, mouseY,
                rightColumnX, cardStartY, columnWidth,
                Text.translatable("ocelotsignmod.gui.homepage.ocelot.title"),
                "https://github.com/Creeper-Cola123/ocelotsignmod-minecraft",
                "https://creeper-cola123.github.io/OcelotSignMod_Docs/",
                scrollWindowStartY, scrollWindowEndY, fixedCardHeight);

        return Math.max(leftCardEndY, rightCardEndY) + 25;
    }

    /**
     * 渲染免责声明章节。
     */
    private static int renderDisclaimerSection(MatrixStack matrices, TextRenderer textRenderer,
                                               int mainWidth, int currentY, int scrollWindowStartY, int scrollWindowEndY) {
        currentY += 10;
        Text disclaimerTitle = Text.translatable("ocelotsignmod.gui.homepage.disclaimer.title");
        textRenderer.draw(matrices, disclaimerTitle, UIConstants.SIDEBAR_WIDTH + 20, currentY, UIConstants.COLOR_HOMEPAGE_SECTION_TITLE);
        currentY += 22;

        int disclaimerX = UIConstants.SIDEBAR_WIDTH + 20;
        int disclaimerMaxWidth = mainWidth - 40;
        int disclaimerLineHeight = 15;
        int disclaimerParaGap = 20;

        Text disclaimerP1 = Text.translatable("ocelotsignmod.gui.homepage.disclaimer.p1");
        currentY = renderTextBlock(matrices, textRenderer, disclaimerP1, disclaimerX, disclaimerMaxWidth, currentY, scrollWindowStartY, scrollWindowEndY, UIConstants.COLOR_HOMEPAGE_BODY, disclaimerLineHeight);
        currentY += disclaimerParaGap;

        Text disclaimerP2 = Text.translatable("ocelotsignmod.gui.homepage.disclaimer.p2");
        currentY = renderTextBlock(matrices, textRenderer, disclaimerP2, disclaimerX, disclaimerMaxWidth, currentY, scrollWindowStartY, scrollWindowEndY, UIConstants.COLOR_HOMEPAGE_BODY, disclaimerLineHeight);
        currentY += disclaimerParaGap;

        Text disclaimerP3 = Text.translatable("ocelotsignmod.gui.homepage.disclaimer.p3");
        currentY = renderTextBlock(matrices, textRenderer, disclaimerP3, disclaimerX, disclaimerMaxWidth, currentY, scrollWindowStartY, scrollWindowEndY, UIConstants.COLOR_HOMEPAGE_BODY, disclaimerLineHeight);

        return currentY + 45;
    }

    /**
     * 渲染文本块（支持多段落换行）。
     */
    private static int renderTextBlock(MatrixStack matrices, TextRenderer textRenderer, Text text,
                                       int x, int maxWidth, int currentY,
                                       int scrollWindowStartY, int scrollWindowEndY, int textColor, int lineHeight) {
        String rawText = text.getString();
        String[] paragraphs = rawText.split("\n\n");
        for (int p = 0; p < paragraphs.length; p++) {
            String paraText = paragraphs[p];
            Text para = Text.literal(paraText);
            List<OrderedText> lines = textRenderer.wrapLines(para, maxWidth);
            for (int i = 0; i < lines.size(); i++) {
                if (currentY + lineHeight >= scrollWindowStartY && currentY <= scrollWindowEndY) {
                    textRenderer.draw(matrices, lines.get(i), x, currentY, textColor);
                }
                currentY += lineHeight;
            }
            if (p < paragraphs.length - 1) {
                currentY += lineHeight;
                if (currentY >= scrollWindowStartY && currentY - lineHeight <= scrollWindowEndY) {
                    textRenderer.draw(matrices, Text.literal(" "), x, currentY - lineHeight, textColor);
                }
            }
        }
        return currentY;
    }

    /**
     * 计算链接卡片的高度。
     */
    private static int calculateCardHeight(TextRenderer textRenderer, int width, int padding,
                                           Text teamTitle, String repoUrl, String docUrl) {
        int titleHeight = 14;
        Text repoName = getShortLinkText(repoUrl);
        Text docName = getShortLinkText(docUrl);
        int repoLines = Math.max(1, textRenderer.wrapLines(repoName, width - padding * 2 - 16).size());
        int docLines = Math.max(1, textRenderer.wrapLines(docName, width - padding * 2 - 16).size());
        int repoBtnHeight = repoLines * 10 + 8;
        int docBtnHeight = docLines * 10 + 8;
        int linkAreaHeight = repoBtnHeight + docBtnHeight + 12;
        return padding + titleHeight + 12 + linkAreaHeight + padding;
    }

    /**
     * 渲染单个链接卡片。
     */
    private static int renderCard(MatrixStack matrices, TextRenderer textRenderer, int mouseX, int mouseY,
                                  int x, int currentY, int width,
                                  Text teamTitle, String repoUrl, String docUrl,
                                  int scrollWindowStartY, int scrollWindowEndY, int fixedCardHeight) {
        int padding = 12;
        int cardHeight = fixedCardHeight;

        if (currentY + cardHeight >= scrollWindowStartY && currentY <= scrollWindowEndY) {
            DrawableHelper.fill(matrices, x, currentY, x + width, currentY + cardHeight, UIConstants.COLOR_HOMEPAGE_CARD_BG);
            DrawableHelper.fill(matrices, x, currentY, x + 3, currentY + cardHeight, UIConstants.COLOR_HOMEPAGE_CARD_TITLE);
            DrawableHelper.fill(matrices, x, currentY + cardHeight - 1, x + width, currentY + cardHeight, UIConstants.COLOR_HOMEPAGE_CARD_BORDER);
            DrawableHelper.fill(matrices, x, currentY, x + width, currentY + 1, UIConstants.COLOR_HOMEPAGE_CARD_BORDER);
            DrawableHelper.fill(matrices, x + width - 1, currentY, x + width, currentY + cardHeight, UIConstants.COLOR_HOMEPAGE_CARD_BORDER);
        }

        int cardY = currentY + padding;

        if (cardY + 14 >= scrollWindowStartY && cardY <= scrollWindowEndY) {
            textRenderer.draw(matrices, teamTitle, x + padding, cardY, UIConstants.COLOR_HOMEPAGE_CARD_TITLE);
        }
        cardY += 14;
        cardY += 12;

        int btnMaxWidth = width - padding * 2;

        cardY = renderLinkButton(matrices, textRenderer, mouseX, mouseY, x + padding, cardY, btnMaxWidth, repoUrl, scrollWindowStartY, scrollWindowEndY);
        cardY = renderLinkButton(matrices, textRenderer, mouseX, mouseY, x + padding, cardY, btnMaxWidth, docUrl, scrollWindowStartY, scrollWindowEndY);

        return currentY + cardHeight;
    }

    /**
     * 渲染链接按钮。
     */
    private static int renderLinkButton(MatrixStack matrices, TextRenderer textRenderer, int mouseX, int mouseY,
                                        int cardX, int currentY, int cardWidth, String url,
                                        int scrollWindowStartY, int scrollWindowEndY) {
        Text linkText = getShortLinkText(url);
        int btnWidth = cardWidth;
        int lineHeight = 10;

        List<OrderedText> wrappedLines = textRenderer.wrapLines(linkText, btnWidth - 16);
        int actualBtnHeight = wrappedLines.size() * lineHeight + 8;

        int textWidth = textRenderer.getWidth(wrappedLines.get(0));
        int textX = cardX + (btnWidth - textWidth) / 2;
        int textY = currentY + (actualBtnHeight - wrappedLines.size() * lineHeight) / 2;

        boolean isVisible = currentY + actualBtnHeight >= scrollWindowStartY && currentY <= scrollWindowEndY;
        boolean isHover = isVisible && LayoutHelper.isMouseInRect(mouseX, mouseY, cardX, currentY, btnWidth, actualBtnHeight);

        if (isVisible) {
            int bgColor = isHover ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG;
            DrawableHelper.fill(matrices, cardX, currentY, cardX + btnWidth, currentY + actualBtnHeight, bgColor);
            drawBorder(matrices, cardX, currentY, btnWidth, actualBtnHeight, UIConstants.COLOR_BTN_BORDER);

            int textColor = isHover ? 0xFF004499 : UIConstants.COLOR_LINK_NORMAL;
            int yOffset = 0;
            for (OrderedText line : wrappedLines) {
                int lineW = textRenderer.getWidth(line);
                textRenderer.draw(matrices, line, cardX + (btnWidth - lineW) / 2, textY + yOffset, textColor);
                yOffset += lineHeight;
            }
        }

        if (isHover) {
            PatternAndFontOverlay.setLastHoveredUrl(url);
        }

        return currentY + actualBtnHeight + 6;
    }

    /**
     * 获取 URL 的短显示文本。
     */
    public static Text getShortLinkText(String url) {
        if (url == null || url.isEmpty()) {
            return Text.empty();
        }
        if (url.contains("github.com")) {
            if (url.contains("SolidBlock-cn/mishanguc") || url.contains("Creeper-Cola123/ocelotsignmod")) {
                return Text.translatable("ocelotsignmod.gui.homepage.repo.label");
            }
        }
        if (url.contains("mcmod.cn")) {
            return Text.translatable("ocelotsignmod.gui.homepage.doc.label");
        }
        if (url.contains("yuque.com")) {
            return Text.translatable("ocelotsignmod.gui.homepage.doc.label");
        }
        if (url.contains("github.io")) {
            return Text.translatable("ocelotsignmod.gui.homepage.doc.label");
        }
        try {
            String path = url.substring(url.indexOf("/", 8));
            if (path.length() > 25) {
                String[] parts = path.split("/");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < Math.min(3, parts.length); i++) {
                    if (parts[i].length() > 0) {
                        if (sb.length() > 0) sb.append("/");
                        sb.append(parts[i]);
                    }
                }
                if (sb.length() > 20) {
                    return Text.literal(sb.substring(0, 17) + "...");
                }
                return Text.literal(sb.toString());
            }
            return Text.literal(path);
        } catch (Exception e) {
            return Text.translatable("ocelotsignmod.gui.homepage.doc.label");
        }
    }

    /**
     * 使用系统默认浏览器打开 URL。
     */
    public static boolean openUrl(String url) {
        if (url != null && !url.isEmpty()) {
            Util.getOperatingSystem().open(url);
            return true;
        }
        return false;
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