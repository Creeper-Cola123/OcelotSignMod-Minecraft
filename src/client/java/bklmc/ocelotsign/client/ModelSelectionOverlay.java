package bklmc.ocelotsign.client;

import bklmc.ocelotsign.client.UIConstants;
import bklmc.ocelotsign.client.model.ModelRegistryManager;
import bklmc.ocelotsign.item.CustomModelBlockItem;
import bklmc.ocelotsign.platform.ServerNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 模型选择叠加层。
 *
 * <p>作为 {@link ModelSelectionBlankScreen} 渲染在游戏世界之上（屏幕不暂停）：
 * <ul>
 *     <li>不会暂停游戏，世界始终渲染（vanilla 自动处理）</li>
 *     <li>无默认背景（vanilla Screen 在 shouldPause=false 时背景由我们自行控制）</li>
 *     <li>左 1/3 区域绘制本叠加层的 UI 面板</li>
 *     <li>右 2/3 区域完全不绘制，世界原样透出</li>
 * </ul>
 *
 * <p>输入通过 {@link net.fabricmc.api.client.screen.v1.ScreenEvents}
 * 在 {@link bklmc.ocelotsign.OcelotSignModClient} 中统一派发。</p>
 *
 * <p>本类与 1.21.11 的 Overlay 不同：1.21.1 中 {@code Overlay} 类不存在，
 * 这里采用静态类 + {@code ModelSelectionBlankScreen}（一个 {@code shouldPause()=false} 的 Screen）
 * 的组合来达到相同的效果。</p>
 */
public final class ModelSelectionOverlay {

    private ModelSelectionOverlay() {}

    /**
     * 选择目标类型。
     */
    public enum TargetType {
        ITEM, BLOCK
    }

    /** 布局常量 */
    private static final int HEADER_HEIGHT = UIConstants.HEADER_HEIGHT;
    private static final int ROW_HEIGHT = 30;
    private static final int ROW_GAP = 8;
    private static final int SCROLL_AMOUNT = UIConstants.SCROLL_AMOUNT;
    private static final int CONTENT_TOP_PAD = 16;
    private static final int CONTENT_H_PADDING = 20;
    /** 面板左侧、上方、右侧、下方边距 */
    private static final int PANEL_MARGIN = 12;
    /** 面板固定宽度（像素）。当屏幕更窄时不超过屏幕宽度的 1/3。 */
    private static final int PANEL_PREFERRED_WIDTH = 380;

    // ==================== 状态字段 ====================

    public static boolean isVisible = false;
    private static Hand hand = Hand.MAIN_HAND;
    private static TargetType targetType = TargetType.ITEM;
    private static BlockPos blockPos = null;
    private static List<ModelEntry> modelEntries = null;
    private static boolean hasNoModels = false;

    private static double scrollY = 0;
    private static double maxScrollY = 0;
    private static double contentHeight = 0;
    private static boolean isDraggingScrollbar = false;
    private static double dragStartMouseY = 0;
    private static double dragStartScrollY = 0;

    private static int cachedWidth = 0;
    private static int cachedHeight = 0;

    private static int panelWidth = 0;
    private static int panelLeft = 0, panelRight = 0;
    private static int backBtnX = 0, backBtnY = 0;
    private static int contentLeft = 0, contentRight = 0;
    private static boolean backBtnHovered = false;
    private static int headerHeight = 0;
    private static int footerHeight = 0;

    /** 上一次记录的鼠标按下状态（用于 click 边沿检测）。 */
    private static boolean lastMouseDown = false;

    /**
     * 模型列表条目。
     *
     * <p>每行显示：模型名称（左侧） + 选择按钮（右侧）。</p>
     */
    public static final class ModelEntry {
        private static final int BTN_W = 60;
        private static final int BTN_H = 22;
        /** 按钮距离面板右边缘的内边距。 */
        private static final int BTN_RIGHT_MARGIN = 6;

        public final String modelId;
        public final String localizedName;

        public ModelEntry(String modelId, String localizedName) {
            this.modelId = modelId;
            this.localizedName = localizedName;
        }

        public void render(DrawContext ctx, int x, int y, int rowWidth, int mx, int my, int panelLeft, int panelRight) {
            boolean rowHover = inBounds(mx, my, panelLeft, y, panelRight - panelLeft, ROW_HEIGHT);

            ctx.fill(panelLeft, y, panelRight, y + ROW_HEIGHT,
                    rowHover ? UIConstants.COLOR_ITEM_BG : UIConstants.COLOR_MAIN_BG);
            if (rowHover) {
                ctx.drawBorder(panelLeft, y, panelRight - panelLeft, ROW_HEIGHT, UIConstants.COLOR_ITEM_BORDER);
            }

            int btnX = panelRight - BTN_W - BTN_RIGHT_MARGIN;
            int btnY = y + (ROW_HEIGHT - BTN_H) / 2;
            boolean btnHover = inBounds(mx, my, btnX, btnY, BTN_W, BTN_H);
            int btnBg = btnHover ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG;
            ctx.fill(btnX, btnY, btnX + BTN_W, btnY + BTN_H, btnBg);
            ctx.drawBorder(btnX, btnY, BTN_W, BTN_H, UIConstants.COLOR_BTN_BORDER);

            TextRenderer tr = MinecraftClient.getInstance().textRenderer;
            Text selLabel = Text.translatable("screen.ocelotsignmod.model_selection.select");
            ctx.drawText(tr, selLabel,
                    btnX + (BTN_W - tr.getWidth(selLabel)) / 2,
                    btnY + (BTN_H - tr.fontHeight) / 2,
                    UIConstants.COLOR_BTN_TEXT, false);

            int nameMaxW = btnX - (panelLeft + 6);

            // 在宽度允许的范围内逐字符换行;不依赖 wrapLines,避免 OrderedText 反求字符串。
            // 行高 = fontHeight + 1;最多容纳的行数由 ROW_HEIGHT 决定,
            // 超过行数时仅保留前面行,最后一行做 "..." 截断,
            // 确保总高度恒等于 ROW_HEIGHT,不会撑高整行。
            List<String> wrappedLines = wrapByCharWidth(localizedName, tr, nameMaxW);

            int maxTextLines = Math.max(1, (ROW_HEIGHT - 4) / (tr.fontHeight + 1));
            if (wrappedLines.size() > maxTextLines) {
                List<String> truncated = new ArrayList<>(wrappedLines.subList(0, maxTextLines));
                String last = truncated.get(maxTextLines - 1);
                while (!last.isEmpty() && tr.getWidth(last + "...") > nameMaxW) {
                    last = last.substring(0, last.length() - 1);
                }
                truncated.set(maxTextLines - 1, last + "...");
                wrappedLines = truncated;
            }

            int textBlockH = wrappedLines.size() * (tr.fontHeight + 1) - 1;
            int textY = y + Math.max(0, (ROW_HEIGHT - textBlockH) / 2);
            int textX = panelLeft + 6;
            for (int li = 0; li < wrappedLines.size(); li++) {
                ctx.drawText(tr, Text.literal(wrappedLines.get(li)),
                        textX,
                        textY + li * (tr.fontHeight + 1),
                        0xFF000000, false);
            }
        }

        public boolean mouseClicked(double mx, double my, int x, int y, int rowWidth) {
            int btnX = panelRight - BTN_W - BTN_RIGHT_MARGIN;
            int btnY = y + (ROW_HEIGHT - BTN_H) / 2;
            if (inBounds(mx, my, btnX, btnY, BTN_W, BTN_H)) {
                selectModel(this.modelId);
                return true;
            }
            return false;
        }
    }

    // ==================== 公共 API（由 ScreenEvents 调度） ====================

    /**
     * 打开物品模式叠加层。
     */
    public static void open(Hand hand) {
        ModelSelectionOverlay.hand = hand;
        ModelSelectionOverlay.targetType = TargetType.ITEM;
        ModelSelectionOverlay.blockPos = null;
        ModelSelectionOverlay.isVisible = true;
        ModelSelectionOverlay.lastMouseDown = false;
        ModelSelectionOverlay.isDraggingScrollbar = false;
        ModelSelectionOverlay.reloadEntries();
        ModelSelectionOverlay.recalcLayout();
        ModelSelectionOverlay.resetScroll();
    }

    /**
     * 打开方块模式叠加层。
     */
    public static void open(TargetType targetType, BlockPos blockPos) {
        ModelSelectionOverlay.hand = Hand.MAIN_HAND;
        ModelSelectionOverlay.targetType = targetType;
        ModelSelectionOverlay.blockPos = blockPos;
        ModelSelectionOverlay.isVisible = true;
        ModelSelectionOverlay.lastMouseDown = false;
        ModelSelectionOverlay.isDraggingScrollbar = false;
        ModelSelectionOverlay.reloadEntries();
        ModelSelectionOverlay.recalcLayout();
        ModelSelectionOverlay.resetScroll();
    }

    /** 关闭叠加层。 */
    public static void close() {
        isVisible = false;
        MinecraftClient mc = MinecraftClient.getInstance();
        // 重新锁定鼠标,恢复第三人称视角输入
        if (mc != null && mc.mouse != null && !mc.mouse.isCursorLocked()) {
            mc.mouse.lockCursor();
        }
        // 释放 useKey (右键) 状态,避免玩家按住右键打开叠加层、关闭后 useKey 仍然为 pressed,
        // 导致 doItemUse() 在下一个 tick 又触发 UseBlockCallback,把叠加层再次打开。
        if (mc != null && mc.options != null && mc.options.useKey != null) {
            mc.options.useKey.setPressed(false);
        }
    }

    /** 关闭叠加层并放置方块（物品模式）。 */
    public static void closeAndPlaceBlock() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.player == null || mc.world == null) {
            close();
            return;
        }

        ItemStack stack = mc.player.getStackInHand(hand);
        if (!CustomModelBlockItem.isCustomModelItem(stack)) {
            close();
            return;
        }

        close();

        net.minecraft.util.math.Direction face = net.minecraft.util.math.Direction.NORTH;
        net.minecraft.util.hit.HitResult hit = mc.crosshairTarget;
        if (hit != null && hit.getType() == net.minecraft.util.hit.HitResult.Type.BLOCK) {
            net.minecraft.util.hit.BlockHitResult blockHit = (net.minecraft.util.hit.BlockHitResult) hit;
            face = blockHit.getSide();
            net.minecraft.item.ItemPlacementContext ctx = new net.minecraft.item.ItemPlacementContext(
                    mc.player, hand, stack, blockHit);
            stack.useOnBlock(ctx);
        }
        // 抑制 unused 警告
        if (face == null) {
            // 不可能
        }
    }

    // ==================== 渲染 ====================

    /**
     * 由 ScreenEvents.afterRender 调用。
     */
    public static void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        if (!isVisible) return;
        updateCachedSize();
        recalcLayout();
        calculateLayout();
        renderUIPanel(ctx, mouseX, mouseY, delta);
        renderBottomRightHints(ctx);
    }

    /**
     * 在游戏画面右下角渲染两行提示文字,距屏幕边缘保留较宽的间距。
     */
    private static void renderBottomRightHints(DrawContext ctx) {
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        Text line1 = Text.translatable("screen.ocelotsignmod.model_selection.hint_switch_model");
        Text line2 = Text.translatable("screen.ocelotsignmod.model_selection.hint_import_resource_pack");

        int w1 = tr.getWidth(line1);
        int w2 = tr.getWidth(line2);
        int maxW = Math.max(w1, w2);
        int lineH = tr.fontHeight + 2;
        int padding = 4;
        // 距离屏幕右下角的内边距(像素),留出更大空隙避免贴边
        int margin = 24;

        int bx = cachedWidth - maxW - padding * 2 - margin;
        int by = cachedHeight - lineH * 2 - padding * 2 - margin;

        ctx.fill(bx, by, cachedWidth - margin, cachedHeight - margin, 0xAA000000);

        ctx.drawText(tr, line1, bx + padding, by + padding, 0xFFFFFFFF, false);
        ctx.drawText(tr, line2, bx + padding, by + padding + lineH, 0xFFFFFFFF, false);
    }

    /**
     * 渲染左侧 UI 面板（左侧 1/3）。
     */
    private static void renderUIPanel(DrawContext ctx, int mx, int my, float delta) {
        // 内容区域背景
        ctx.fill(PANEL_MARGIN, PANEL_MARGIN,
                panelWidth - PANEL_MARGIN, cachedHeight - PANEL_MARGIN,
                UIConstants.COLOR_MAIN_BG);

        renderHeader(ctx);
        renderFooter(ctx, mx, my);

        if (hasNoModels) {
            renderEmptyState(ctx);
        } else {
            handleScrollbarDragging(mx, my);
            renderContent(ctx, mx, my);
            renderScrollbar(ctx, mx, my);
        }
    }

    /** 渲染标题栏。 */
    private static void renderHeader(DrawContext ctx) {
        int headerTop = PANEL_MARGIN;
        int headerBottom = headerTop + headerHeight;
        ctx.fill(PANEL_MARGIN, headerTop, panelWidth - PANEL_MARGIN, headerBottom, UIConstants.COLOR_MAIN_HEADER);
        // 用 fill 绘制 1px 分隔线代替 drawStrokedRectangle
        ctx.fill(PANEL_MARGIN, headerBottom - 1, panelWidth - PANEL_MARGIN, headerBottom, UIConstants.COLOR_MAIN_BORDER);

        Text titleText = Text.translatable("screen.ocelotsignmod.model_selection.title");
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        int titleW = tr.getWidth(titleText);
        ctx.drawText(tr, titleText,
                PANEL_MARGIN + (panelWidth - PANEL_MARGIN * 2 - titleW) / 2,
                headerTop + (headerHeight - tr.fontHeight) / 2,
                0xFFFFFFFF, false);
    }

    /** 渲染底部栏。 */
    private static void renderFooter(DrawContext ctx, int mx, int my) {
        int footerTop = cachedHeight - PANEL_MARGIN - footerHeight;
        backBtnX = PANEL_MARGIN + (panelWidth - PANEL_MARGIN * 2 - UIConstants.RETURN_BUTTON_WIDTH) / 2;
        backBtnY = footerTop;
        backBtnHovered = inBounds(mx, my, backBtnX, footerTop,
                UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT);

        int btnBg = backBtnHovered ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG;
        ctx.fill(backBtnX, footerTop,
                backBtnX + UIConstants.RETURN_BUTTON_WIDTH,
                footerTop + UIConstants.RETURN_BUTTON_HEIGHT,
                btnBg);
        ctx.drawBorder(backBtnX, footerTop,
                UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT,
                UIConstants.COLOR_BTN_BORDER);

        Text backTxt = Text.translatable("screen.ocelotsignmod.model_selection.back");
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        ctx.drawText(tr, backTxt,
                backBtnX + (UIConstants.RETURN_BUTTON_WIDTH - tr.getWidth(backTxt)) / 2,
                footerTop + (UIConstants.RETURN_BUTTON_HEIGHT - tr.fontHeight) / 2,
                UIConstants.COLOR_BTN_TEXT, false);
    }

    /** 渲染空状态。 */
    private static void renderEmptyState(DrawContext ctx) {
        int cx = panelWidth / 2;
        int cy = cachedHeight / 2;

        drawEmptyIcon(ctx, cx, cy - 64);

        Text head = Text.translatable("screen.ocelotsignmod.model_selection.no_models");
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        ctx.drawText(tr, head,
                cx - tr.getWidth(head) / 2, cy,
                UIConstants.COLOR_SECTION_TITLE, false);

        Text sub = Text.translatable("screen.ocelotsignmod.model_selection.no_models_hint");
        ctx.drawText(tr, sub,
                cx - tr.getWidth(sub) / 2, cy + 22,
                UIConstants.COLOR_DESC_TEXT, false);
    }

    /** 绘制空状态图标。 */
    private static void drawEmptyIcon(DrawContext ctx, int cx, int cy) {
        int size = 34;
        // 三层错位方块
        ctx.fill(cx - size, cy + 4, cx, cy + size + 4, UIConstants.COLOR_ITEM_BG);
        ctx.drawBorder(cx - size, cy + 4, size, size, UIConstants.COLOR_ITEM_BORDER);

        ctx.fill(cx - size + 7, cy - 3, cx + 7, cy + size + 3, UIConstants.COLOR_ITEM_BG);
        ctx.drawBorder(cx - size + 7, cy - 3, size, size, UIConstants.COLOR_ITEM_BORDER);

        ctx.fill(cx - size + 14, cy - 10, cx + 14, cy + size - 10, UIConstants.COLOR_ITEM_BG);
        ctx.drawBorder(cx - size + 14, cy - 10, size, size, UIConstants.COLOR_ITEM_BORDER);

        Text q = Text.literal("?");
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        int qw = tr.getWidth(q);
        ctx.drawText(tr, q,
                cx - qw / 2, cy - tr.fontHeight / 2,
                UIConstants.COLOR_H3_TEXT, false);
    }

    /** 渲染模型列表。 */
    private static void renderContent(DrawContext ctx, int mx, int my) {
        int contentTop = PANEL_MARGIN + headerHeight + CONTENT_TOP_PAD;
        int contentBottom = cachedHeight - PANEL_MARGIN - footerHeight - 10;
        boolean hasScrollbar = maxScrollY > 20;
        int rightEdge = hasScrollbar ? panelWidth - PANEL_MARGIN - UIConstants.SCROLLBAR_WIDTH : panelWidth - PANEL_MARGIN;
        ctx.enableScissor(PANEL_MARGIN, contentTop, rightEdge, contentBottom);

        int startY = contentTop - (int) scrollY;

        if (modelEntries != null) {
            for (int i = 0; i < modelEntries.size(); i++) {
                int y = startY + i * (ROW_HEIGHT + ROW_GAP);
                if (y + ROW_HEIGHT < contentTop || y > contentBottom) continue;
                modelEntries.get(i).render(ctx, contentLeft, y, contentRight - contentLeft, mx, my, panelLeft, panelRight);
            }
        }

        ctx.disableScissor();
    }

    /** 渲染滚动条。 */
    private static void renderScrollbar(DrawContext ctx, int mx, int my) {
        int contentTop = PANEL_MARGIN + headerHeight + CONTENT_TOP_PAD;
        int contentBottom = cachedHeight - PANEL_MARGIN - footerHeight - 10;
        int winH = contentBottom - contentTop;
        if (maxScrollY <= 20) return;

        int sx = panelWidth - PANEL_MARGIN - UIConstants.SCROLLBAR_WIDTH - 3;
        int trackTop = contentTop + 8;
        int trackH = winH - 16;

        ctx.fill(sx, trackTop, sx + UIConstants.SCROLLBAR_WIDTH, trackTop + trackH, UIConstants.COLOR_SCROLLBAR_TRACK);

        float ratio = (float) winH / (float) (winH + maxScrollY);
        int thumbH = Math.max(UIConstants.SCROLLBAR_MIN_HEIGHT, (int) (trackH * ratio));
        float scrollPct = maxScrollY > 0 ? (float) scrollY / (float) maxScrollY : 0;
        int thumbY = trackTop + (int) ((trackH - thumbH) * scrollPct);

        boolean hover = mx >= sx && mx <= sx + UIConstants.SCROLLBAR_WIDTH
                && my >= thumbY && my <= thumbY + thumbH;
        int thumbC = hover ? UIConstants.COLOR_SCROLLBAR_THUMB_HOVER : UIConstants.COLOR_SCROLLBAR_THUMB;
        ctx.fill(sx, thumbY, sx + UIConstants.SCROLLBAR_WIDTH, thumbY + thumbH, thumbC);
        ctx.drawBorder(sx, thumbY, UIConstants.SCROLLBAR_WIDTH, thumbH, UIConstants.COLOR_ITEM_BORDER);
    }

    /** 处理滚动条拖拽。 */
    private static void handleScrollbarDragging(int mx, int my) {
        if (!isDraggingScrollbar) return;
        int contentTop = PANEL_MARGIN + headerHeight + CONTENT_TOP_PAD;
        int contentBottom = cachedHeight - PANEL_MARGIN - footerHeight - 10;
        int winH = contentBottom - contentTop;
        float ratio = (float) winH / (float) (winH + maxScrollY);
        int thumbH = Math.max(UIConstants.SCROLLBAR_MIN_HEIGHT, (int) (winH * ratio));
        int range = winH - thumbH;
        if (range > 0) {
            double d = ((my - dragStartMouseY) / range) * maxScrollY;
            scrollY = MathHelper.clamp(dragStartScrollY + d, 0, maxScrollY);
        }
    }

    // ==================== 输入派发（由 ScreenEvents 调用） ====================

    /**
     * 由 ScreenMouseEvents.allowMouseClick 调用。
     *
     * @return true 表示事件已处理,不再向下传递
     */
    public static boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isVisible) return false;
        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) return false;
        updateCachedSize();
        recalcLayout();

        // 只响应左侧 UI 面板区域的鼠标事件
        if (mouseX >= panelWidth) return false;

        int footerTop = cachedHeight - PANEL_MARGIN - footerHeight;
        if (inBounds(mouseX, mouseY, backBtnX, footerTop,
                UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT)) {
            closeAndPlaceBlock();
            return true;
        }

        int sx = panelWidth - PANEL_MARGIN - UIConstants.SCROLLBAR_WIDTH - 3;
        int contentTop = PANEL_MARGIN + headerHeight + CONTENT_TOP_PAD;
        int contentBottom = cachedHeight - PANEL_MARGIN - footerHeight - 10;
        int winH = contentBottom - contentTop;

        if (maxScrollY > 20 && mouseX >= sx - 2 && mouseX <= sx + UIConstants.SCROLLBAR_WIDTH + 2) {
            int trackTop = contentTop + 8;
            int trackH = winH - 16;
            float ratio = (float) trackH / (float) (trackH + maxScrollY);
            int thumbH = Math.max(UIConstants.SCROLLBAR_MIN_HEIGHT, (int) (trackH * ratio));
            float scrollPct = maxScrollY > 0 ? (float) scrollY / (float) maxScrollY : 0;
            int thumbY = trackTop + (int) ((trackH - thumbH) * scrollPct);

            if (mouseY >= thumbY && mouseY <= thumbY + thumbH) {
                isDraggingScrollbar = true;
                dragStartMouseY = mouseY;
                dragStartScrollY = scrollY;
                return true;
            } else if (mouseY >= trackTop && mouseY <= trackTop + trackH) {
                scrollY = MathHelper.clamp(((mouseY - trackTop) / (double) trackH) * maxScrollY, 0, maxScrollY);
                return true;
            }
        }

        if (modelEntries != null) {
            int startY = contentTop - (int) scrollY;
            for (int i = 0; i < modelEntries.size(); i++) {
                int y = startY + i * (ROW_HEIGHT + ROW_GAP);
                if (modelEntries.get(i).mouseClicked(mouseX, mouseY, contentLeft, y, contentRight - contentLeft)) {
                    return true;
                }
            }
        }
        return true; // 在面板区域消费事件,阻止穿透到下层 Screen
    }

    /**
     * 由 ScreenMouseEvents.allowMouseRelease 调用。
     */
    public static boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!isVisible) return false;
        if (mouseX >= panelWidth) return false;
        if (isDraggingScrollbar) {
            isDraggingScrollbar = false;
        }
        return false;
    }

    /**
     * 由 ScreenMouseEvents.allowMouseScroll 调用。
     */
    public static boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (!isVisible) return false;
        // 只响应左侧面板区域的滚轮事件
        if (mouseX >= panelWidth) return false;

        int contentTop = PANEL_MARGIN + headerHeight + CONTENT_TOP_PAD;
        int contentBottom = cachedHeight - PANEL_MARGIN - footerHeight - 10;
        if (mouseY < contentTop || mouseY > contentBottom) return false;

        if (maxScrollY > 0 && verticalAmount != 0) {
            scrollY = MathHelper.clamp(scrollY - verticalAmount * SCROLL_AMOUNT, 0, maxScrollY);
        }
        // 抑制 unused 警告
        if (horizontalAmount != 0) {
            // 不处理水平滚动
        }
        return true;
    }

    /**
     * 由 ScreenKeyboardEvents.allowKeyPress 调用。
     *
     * @return true 表示事件已处理,不再向下传递
     */
    public static boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!isVisible) return false;
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            closeAndPlaceBlock();
            return true;
        }
        return false;
    }

    /**
     * 由 ScreenKeyboardEvents.allowKeyRelease 调用。
     */
    public static boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        // 不消费按键释放事件,放行给下层
        return false;
    }

    // ==================== 内部方法 ====================

    /** 重新加载条目（资源包重载或被打开时调用）。 */
    private static void reloadEntries() {
        Map<String, ModelRegistryManager.ModelDefinition> models = ModelRegistryManager.getAvailableModels();
        hasNoModels = models.isEmpty();

        if (!hasNoModels) {
            modelEntries = new ArrayList<>();
            for (Map.Entry<String, ModelRegistryManager.ModelDefinition> e : models.entrySet()) {
                ModelRegistryManager.ModelDefinition def = e.getValue();
                modelEntries.add(new ModelEntry(e.getKey(), def.localizedName()));
            }
            modelEntries.sort((a, b) -> a.localizedName.compareToIgnoreCase(b.localizedName));
        } else {
            modelEntries = null;
        }
    }

    /** 刷新窗口尺寸缓存。 */
    private static void updateCachedSize() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc != null && mc.getWindow() != null) {
            cachedWidth = mc.getWindow().getScaledWidth();
            cachedHeight = mc.getWindow().getScaledHeight();
        }
    }

    /** 重新计算布局参数。 */
    private static void recalcLayout() {
        int preferred = Math.min(PANEL_PREFERRED_WIDTH, Math.max(0, cachedWidth) / 3);
        panelWidth = preferred;

        headerHeight = HEADER_HEIGHT;
        footerHeight = UIConstants.RETURN_BUTTON_HEIGHT;

        backBtnX = PANEL_MARGIN + (panelWidth - PANEL_MARGIN * 2 - UIConstants.RETURN_BUTTON_WIDTH) / 2;
        backBtnY = cachedHeight - PANEL_MARGIN - footerHeight;

        contentLeft = PANEL_MARGIN + CONTENT_H_PADDING;
        contentRight = panelWidth - PANEL_MARGIN - CONTENT_H_PADDING;
        panelLeft = PANEL_MARGIN;
        panelRight = panelWidth - PANEL_MARGIN;
    }

    /** 重置滚动位置。 */
    private static void resetScroll() {
        scrollY = 0;
        calculateLayout();
    }

    /** 计算布局参数。 */
    private static void calculateLayout() {
        int contentTop = PANEL_MARGIN + headerHeight + CONTENT_TOP_PAD;
        int contentBottom = cachedHeight - PANEL_MARGIN - footerHeight - 10;
        int contentAreaH = contentBottom - contentTop;

        if (modelEntries != null && !modelEntries.isEmpty()) {
            contentHeight = modelEntries.size() * (ROW_HEIGHT + ROW_GAP);
        } else {
            contentHeight = 200;
        }
        maxScrollY = Math.max(0, contentHeight - contentAreaH);
        scrollY = MathHelper.clamp(scrollY, 0, maxScrollY);
    }

    /** 选择模型并同步（不关闭叠加层）。 */
    private static void selectModel(String modelId) {
        applySelectionLocally(modelId);
        sendSelectionToServer(modelId);
    }

    /** 本地应用模型选择。 */
    private static void applySelectionLocally(String modelId) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;
        if (targetType == TargetType.ITEM) {
            ItemStack stack = client.player.getStackInHand(hand);
            if (CustomModelBlockItem.isCustomModelItem(stack)) {
                setModelIdToStack(stack, modelId);
            }
        }
    }

    /** 将模型 ID 写入物品的自定义数据组件。 */
    private static void setModelIdToStack(ItemStack stack, String modelId) {
        NbtCompound existing = getCustomNbt(stack);
        NbtCompound nbt = existing != null ? existing.copy() : new NbtCompound();
        nbt.putString(CustomModelBlockItem.SELECTED_MODEL_ID_KEY, modelId);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }

    /** 获取物品的自定义 NBT 数据。 */
    private static NbtCompound getCustomNbt(ItemStack stack) {
        NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (customData == null) return null;
        return customData.copyNbt();
    }

    /** 发送模型选择到服务器。 */
    private static void sendSelectionToServer(String modelId) {
        ServerNetworking.SelectModelPayload payload = new ServerNetworking.SelectModelPayload(
                modelId,
                targetType == TargetType.BLOCK && blockPos != null,
                blockPos,
                hand == Hand.MAIN_HAND
        );
        ClientPlayNetworking.send(payload);
    }

    private static boolean inBounds(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    /**
     * 按像素宽度切分字符串为多行(在尽量完整地切在一个\n / 空格 / 字符边界上)。
     */
    private static List<String> wrapByCharWidth(String s, TextRenderer tr, int maxWidth) {
        List<String> out = new ArrayList<>();
        if (s == null || s.isEmpty()) {
            out.add("");
            return out;
        }
        // 先按换行符切段,每段内部再按宽度切。
        for (String segment : s.split("\n", -1)) {
            StringBuilder line = new StringBuilder();
            for (int i = 0; i < segment.length(); ) {
                int cp = segment.codePointAt(i);
                String ch = new String(Character.toChars(cp));
                i += Character.charCount(cp);
                if (tr.getWidth(line + ch) > maxWidth) {
                    if (line.length() == 0) {
                        // 单字符就超长——硬塞进这一行,避免死循环。
                        out.add(ch);
                        line.setLength(0);
                    } else {
                        out.add(line.toString());
                        line.setLength(0);
                        line.append(ch);
                    }
                } else {
                    line.append(ch);
                }
            }
            out.add(line.toString());
        }
        return out;
    }
}
