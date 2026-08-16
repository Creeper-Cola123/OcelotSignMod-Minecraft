package bklmc.ocelotsign.client.gui;

import bklmc.ocelotsign.client.UIConstants;
import bklmc.ocelotsign.client.model.ModelRegistryManager;
import bklmc.ocelotsign.item.CustomModelBlockItem;
import bklmc.ocelotsign.platform.ServerNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Overlay;
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
 * <p>作为 {@link Overlay} 渲染在游戏世界之上而非独立的 Screen：
 * <ul>
 *     <li>不会暂停游戏，世界始终渲染（vanilla 自动处理）</li>
 *     <li>无默认背景（vanilla Overlay 不绘制背景）</li>
 *     <li>左 1/3 区域绘制本叠加层的 UI 面板</li>
 *     <li>右 2/3 区域完全不绘制，世界原样透出</li>
 * </ul>
 *
 * <p>输入通过 {@link net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents#END_CLIENT_TICK}
 * 在 {@link bklmc.ocelotsign.OcelotSignModClient} 中统一轮询派发。
 */
public class ModelSelectionOverlay extends Overlay {

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
    private static final int PANEL_RIGHT_MARGIN = 4;
    /** 面板左侧、上方、右侧、下方边距 */
    private static final int PANEL_MARGIN = 12;

    private final Hand hand;
    private final TargetType targetType;
    private final BlockPos blockPos;
    private List<ModelEntry> modelEntries;
    private boolean hasNoModels = false;

    private double scrollY = 0;
    private double maxScrollY = 0;
    private double contentHeight = 0;
    private boolean isDraggingScrollbar = false;
    private double dragStartMouseY = 0;
    private double dragStartScrollY = 0;

    private int panelWidth = 0;
    private int panelLeft = 0, panelRight = 0;
    private int backBtnX = 0, backBtnY = 0;
    private int contentLeft = 0, contentRight = 0;
    private boolean backBtnHovered = false;
    private int headerHeight = 0;
    private int footerHeight = 0;

    private int cachedWidth = 0;
    private int cachedHeight = 0;

    /** 上一次记录的鼠标按下状态（用于 click 边沿检测）。 */
    private boolean lastMouseDown = false;
    /** 上一次记录的鼠标按下时的 Y（用于拖动判定）。 */
    private double lastMouseDownY = 0;

    /** 当前实例的引用（用于静态访问） */
    private static ModelSelectionOverlay currentInstance = null;

    /**
     * 构造手持物品模式的模型选择叠加层。
     *
     * @param hand 玩家手持物品的手
     */
    public ModelSelectionOverlay(Hand hand) {
        this.hand = hand;
        this.targetType = TargetType.ITEM;
        this.blockPos = null;
        currentInstance = this;
        initialize();
    }

    /**
     * 构造已放置方块模式的模型选择叠加层。
     *
     * @param targetType 目标类型
     * @param blockPos 方块位置
     */
    public ModelSelectionOverlay(TargetType targetType, BlockPos blockPos) {
        this.hand = Hand.MAIN_HAND;
        this.targetType = targetType;
        this.blockPos = blockPos;
        currentInstance = this;
        initialize();
    }

    /** 初始化数据与布局（替代 Screen.init()）。 */
    private void initialize() {
        MinecraftClient mc = MinecraftClient.getInstance();
        updateCachedSize();
        recalcLayout();

        Map<String, ModelRegistryManager.ModelDefinition> models = ModelRegistryManager.getAvailableModels();
        hasNoModels = models.isEmpty();

        if (!hasNoModels) {
            this.modelEntries = new ArrayList<>();
            for (Map.Entry<String, ModelRegistryManager.ModelDefinition> e : models.entrySet()) {
                ModelRegistryManager.ModelDefinition def = e.getValue();
                this.modelEntries.add(new ModelEntry(e.getKey(), def.localizedName()));
            }
            this.modelEntries.sort((a, b) -> a.localizedName.compareToIgnoreCase(b.localizedName));
        }

        resetScroll();
        calculateLayout();
    }

    /** 刷新窗口尺寸缓存。 */
    private void updateCachedSize() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc != null && mc.getWindow() != null) {
            cachedWidth = mc.getWindow().getScaledWidth();
            cachedHeight = mc.getWindow().getScaledHeight();
        }
    }

    /** 面板固定宽度（像素）。当屏幕更窄时不超过屏幕宽度的 1/3。 */
    private static final int PANEL_PREFERRED_WIDTH = 380;

    /** 重新计算布局参数。 */
    private void recalcLayout() {
        int preferred = Math.min(PANEL_PREFERRED_WIDTH, cachedWidth / 3);
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
    private void resetScroll() {
        scrollY = 0;
        calculateLayout();
    }

    /** 计算布局参数。 */
    private void calculateLayout() {
        int contentTop = PANEL_MARGIN + headerHeight + CONTENT_TOP_PAD;
        int contentBottom = cachedHeight - PANEL_MARGIN - footerHeight - 10;
        int contentAreaH = contentBottom - contentTop;

        if (modelEntries != null && !modelEntries.isEmpty()) {
            contentHeight = modelEntries.size() * (ROW_HEIGHT + ROW_GAP);
        } else {
            contentHeight = 200;
        }
        calculateScrollBounds(contentAreaH);
        scrollY = MathHelper.clamp(scrollY, 0, maxScrollY);
    }

    /** 计算滚动边界。 */
    private void calculateScrollBounds(int contentAreaH) {
        maxScrollY = Math.max(0, contentHeight - contentAreaH);
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        updateCachedSize();
        recalcLayout();
        calculateLayout();
        renderUIPanel(ctx, mx, my, delta);
        renderBottomRightHints(ctx);
    }

    /**
     * 在游戏画面右下角渲染两行提示文字,距屏幕边缘保留较宽的间距。
     */
    private void renderBottomRightHints(DrawContext ctx) {
        var tr = MinecraftClient.getInstance().textRenderer;
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

    @Override
    public boolean pausesGame() {
        // 不暂停游戏：让世界持续渲染
        return false;
    }

    /**
     * 渲染左侧 UI 面板（左侧 1/3）。
     *
     * <p>vanilla 已在世界帧缓冲上渲染了完整的世界。我们仅在左 1/3 区域
     * 绘制 UI 面板覆盖在世界之上；右 2/3 不绘制任何东西，世界自然透出。
     */
    private void renderUIPanel(DrawContext ctx, int mx, int my, float delta) {
        // 只绘制"内容区"（左、上、下、右均留白），留白区域不绘制任何东西，
        // 让右 2/3 的世界以及左侧面板的边距区域自然透出。

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
    private void renderHeader(DrawContext ctx) {
        int headerTop = PANEL_MARGIN;
        int headerBottom = headerTop + headerHeight;
        ctx.fill(PANEL_MARGIN, headerTop, panelWidth - PANEL_MARGIN, headerBottom, UIConstants.COLOR_MAIN_HEADER);
        ctx.drawBorder(PANEL_MARGIN, headerBottom - 1, panelWidth - PANEL_MARGIN * 2, 1, UIConstants.COLOR_MAIN_BORDER);

        Text titleText = Text.translatable("screen.ocelotsignmod.model_selection.title");
        var tr = MinecraftClient.getInstance().textRenderer;
        int titleW = tr.getWidth(titleText);
        ctx.drawText(tr, titleText,
                PANEL_MARGIN + (panelWidth - PANEL_MARGIN * 2 - titleW) / 2,
                headerTop + (headerHeight - tr.fontHeight) / 2,
                0xFFFFFFFF, false);
    }

    /** 渲染底部栏。 */
    private void renderFooter(DrawContext ctx, int mx, int my) {
        int footerTop = cachedHeight - PANEL_MARGIN - footerHeight;
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
        var tr = MinecraftClient.getInstance().textRenderer;
        ctx.drawText(tr, backTxt,
                backBtnX + (UIConstants.RETURN_BUTTON_WIDTH - tr.getWidth(backTxt)) / 2,
                footerTop + (UIConstants.RETURN_BUTTON_HEIGHT - tr.fontHeight) / 2,
                UIConstants.COLOR_BTN_TEXT, false);
    }

    /** 渲染空状态。 */
    private void renderEmptyState(DrawContext ctx) {
        int cx = panelWidth / 2;
        int cy = cachedHeight / 2;

        drawEmptyIcon(ctx, cx, cy - 64);

        Text head = Text.translatable("screen.ocelotsignmod.model_selection.no_models");
        ctx.drawText(MinecraftClient.getInstance().textRenderer, head,
                cx - MinecraftClient.getInstance().textRenderer.getWidth(head) / 2, cy,
                UIConstants.COLOR_SECTION_TITLE, false);

        Text sub = Text.translatable("screen.ocelotsignmod.model_selection.no_models_hint");
        ctx.drawText(MinecraftClient.getInstance().textRenderer, sub,
                cx - MinecraftClient.getInstance().textRenderer.getWidth(sub) / 2, cy + 22,
                UIConstants.COLOR_DESC_TEXT, false);
    }

    /** 绘制空状态图标。 */
    private void drawEmptyIcon(DrawContext ctx, int cx, int cy) {
        int size = 34;
        ctx.fill(cx - size, cy + 4, cx, cy + size + 4, UIConstants.COLOR_ITEM_BG);
        ctx.drawBorder(cx - size, cy + 4, size, size, UIConstants.COLOR_ITEM_BORDER);
        ctx.fill(cx - size + 7, cy - 3, cx + 7, cy + size + 3, UIConstants.COLOR_ITEM_BG);
        ctx.drawBorder(cx - size + 7, cy - 3, size, size, UIConstants.COLOR_ITEM_BORDER);
        ctx.fill(cx - size + 14, cy - 10, cx + 14, cy + size - 10, UIConstants.COLOR_ITEM_BG);
        ctx.drawBorder(cx - size + 14, cy - 10, size, size, UIConstants.COLOR_ITEM_BORDER);

        Text q = Text.literal("?");
        int qw = MinecraftClient.getInstance().textRenderer.getWidth(q);
        ctx.drawText(MinecraftClient.getInstance().textRenderer, q,
                cx - qw / 2, cy - MinecraftClient.getInstance().textRenderer.fontHeight / 2,
                UIConstants.COLOR_H3_TEXT, false);
    }

    /** 渲染模型列表。 */
    private void renderContent(DrawContext ctx, int mx, int my) {
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
    private void renderScrollbar(DrawContext ctx, int mx, int my) {
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
    private void handleScrollbarDragging(int mx, int my) {
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

    // ==================== 输入派发（由 ClientTickEvents 调用） ====================

    /**
     * 由 {@code ClientTickEvents.END_CLIENT_TICK} 调用，处理本叠加层的输入。
     *
     * <p>这里实现等价于 Screen 的 mouseClicked / mouseReleased / mouseScrolled / keyPressed，
     * 但通过轮询而非回调来实现，以便适配 Overlay 没有内建输入管线的特性。
     */
    public void tickInput() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.getWindow() == null) return;
        long glfwHandle = mc.getWindow().getHandle();
        updateCachedSize();
        recalcLayout();

        // 直接通过 GLFW 获取鼠标坐标并转换为 scaled 坐标
        double[] xpos = new double[1];
        double[] ypos = new double[1];
        GLFW.glfwGetCursorPos(glfwHandle, xpos, ypos);
        double scaleFactor = mc.getWindow().getScaleFactor();
        double mx = xpos[0] / scaleFactor;
        double my = ypos[0] / scaleFactor;
        // 直接通过 GLFW 轮询鼠标左键按下状态，不依赖 Mouse.wasLeftButtonClicked()
        // （后者在 Overlay 模式下行为不可靠）。
        boolean mouseDown = GLFW.glfwGetMouseButton(glfwHandle, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;

        // 鼠标释放（边沿）
        if (lastMouseDown && !mouseDown) {
            handleMouseReleased(mx, my);
        }

        // 鼠标按下（边沿）
        if (!lastMouseDown && mouseDown) {
            handleMouseClicked(mx, my);
            lastMouseDownY = my;
        }

        // 拖动
        if (lastMouseDown && mouseDown && isDraggingScrollbar) {
            handleMouseDragged(mx, my);
        }

        // 通过 Mixin 获取滚轮值（Overlay 没有 ScreenMouseEvents，需要自己处理）
        double scrollDeltaY = bklmc.ocelotsign.client.MouseScrollAccumulator.consumeDeltaY();
        if (scrollDeltaY != 0) {
            this.handleMouseScrolled(scrollDeltaY);
        }

        lastMouseDown = mouseDown;

        // ESC 键关闭
        if (GLFW.glfwGetKey(glfwHandle, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
            closeAndPlaceBlock();
        }
    }

    /**
     * 处理鼠标滚轮事件。
     *
     * @param verticalAmount 垂直滚动量
     */
    public void handleMouseScrolled(double verticalAmount) {
        if (maxScrollY > 0) {
            scrollY = MathHelper.clamp(scrollY - verticalAmount * SCROLL_AMOUNT, 0, maxScrollY);
        }
    }

    /** 鼠标点击处理。 */
    private void handleMouseClicked(double mx, double my) {
        // 只响应左侧 UI 面板区域的鼠标事件
        if (mx >= panelWidth) return;

        int footerTop = cachedHeight - PANEL_MARGIN - footerHeight;
        if (inBounds(mx, my, backBtnX, footerTop,
                UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT)) {
            closeAndPlaceBlock();
            return;
        }

        int sx = panelWidth - PANEL_MARGIN - UIConstants.SCROLLBAR_WIDTH - 3;
        int contentTop = PANEL_MARGIN + headerHeight + CONTENT_TOP_PAD;
        int contentBottom = cachedHeight - PANEL_MARGIN - footerHeight - 10;
        int winH = contentBottom - contentTop;

        if (maxScrollY > 20 && mx >= sx - 2 && mx <= sx + UIConstants.SCROLLBAR_WIDTH + 2) {
            int trackTop = contentTop + 8;
            int trackH = winH - 16;
            float ratio = (float) trackH / (float) (trackH + maxScrollY);
            int thumbH = Math.max(UIConstants.SCROLLBAR_MIN_HEIGHT, (int) (trackH * ratio));
            float scrollPct = maxScrollY > 0 ? (float) scrollY / (float) maxScrollY : 0;
            int thumbY = trackTop + (int) ((trackH - thumbH) * scrollPct);

            if (my >= thumbY && my <= thumbY + thumbH) {
                isDraggingScrollbar = true;
                dragStartMouseY = my;
                dragStartScrollY = scrollY;
                return;
            } else if (my >= trackTop && my <= trackTop + trackH) {
                scrollY = MathHelper.clamp(((my - trackTop) / (double) trackH) * maxScrollY, 0, maxScrollY);
                return;
            }
        }

        if (modelEntries != null) {
            int startY = contentTop - (int) scrollY;
            for (int i = 0; i < modelEntries.size(); i++) {
                int y = startY + i * (ROW_HEIGHT + ROW_GAP);
                if (modelEntries.get(i).mouseClicked(mx, my, contentLeft, y, contentRight - contentLeft)) {
                    return;
                }
            }
        }
    }

    /** 鼠标释放处理。 */
    private void handleMouseReleased(double mx, double my) {
        if (mx >= panelWidth) return;
        if (isDraggingScrollbar) {
            isDraggingScrollbar = false;
        }
    }

    /** 鼠标拖动处理。 */
    private void handleMouseDragged(double mx, double my) {
        if (mx >= panelWidth) return;
        if (isDraggingScrollbar) {
            handleScrollbarDragging((int) mx, (int) my);
        }
    }

    /** 选择模型并同步（不关闭叠加层）。 */
    private void selectModel(String modelId) {
        applySelectionLocally(modelId);
        sendSelectionToServer(modelId);
    }

    /** 关闭叠加层并放置方块。 */
    private void closeAndPlaceBlock() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) {
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
    }

    /** 关闭叠加层。 */
    public void close() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc != null && mc.getOverlay() == this) {
            mc.setOverlay(null);
            // 重新锁定鼠标，恢复正常的第三人称视角输入
            if (!mc.mouse.isCursorLocked()) {
                mc.mouse.lockCursor();
            }
            // 释放 useKey (右键) 状态，避免玩家按住右键打开叠加层、
            // 关闭后 useKey 仍然为 pressed，导致 doItemUse() 在下一个 tick
            // 又触发 UseBlockCallback，把叠加层再次打开。
            if (mc.options != null && mc.options.useKey != null) {
                mc.options.useKey.setPressed(false);
            }
        }
        currentInstance = null;
    }

    /** 本地应用模型选择。 */
    private void applySelectionLocally(String modelId) {
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
    private void sendSelectionToServer(String modelId) {
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
     * 获取当前叠加层实例。
     *
     * @return 当前实例，如果已关闭则返回 null
     */
    public static ModelSelectionOverlay getCurrentInstance() {
        return currentInstance;
    }

    /**
     * 检查叠加层是否正在显示。
     *
     * @return 如果叠加层正在显示则返回 true
     */
    public static boolean isVisible() {
        return currentInstance != null;
    }

    /**
     * 模型列表条目。
     *
     * <p>每行显示：模型名称（左侧） + 选择按钮（右侧）。
     */
    class ModelEntry {
        private static final int BTN_W = 60;
        private static final int BTN_H = 22;
        /** 按钮距离面板右边缘的内边距。 */
        private static final int BTN_RIGHT_MARGIN = 6;
        private static final int NAME_PAD_RIGHT = 0;

        private final String modelId;
        private final String localizedName;

        ModelEntry(String modelId, String localizedName) {
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

            Text selLabel = Text.translatable("screen.ocelotsignmod.model_selection.select");
            ctx.drawText(MinecraftClient.getInstance().textRenderer, selLabel,
                    btnX + (BTN_W - MinecraftClient.getInstance().textRenderer.getWidth(selLabel)) / 2,
                    btnY + (BTN_H - MinecraftClient.getInstance().textRenderer.fontHeight) / 2,
                    UIConstants.COLOR_BTN_TEXT, false);

            int nameMaxW = btnX - (panelLeft + 6);
            var tr = MinecraftClient.getInstance().textRenderer;

            // 在宽度允许的范围内逐字符换行;不依赖 wrapLines,避免 OrderedText 反求字符串。
            // 行高 = fontHeight + 1;最多容纳的行数由 ROW_HEIGHT 决定,
            // 超过行数时仅保留前面行,最后一行做 "..." 截断,
            // 确保总高度恒等于 ROW_HEIGHT,不会撑高整行。
            java.util.List<String> wrappedLines = wrapByCharWidth(localizedName, tr, nameMaxW);

            int maxTextLines = Math.max(1, (ROW_HEIGHT - 4) / (tr.fontHeight + 1));
            if (wrappedLines.size() > maxTextLines) {
                java.util.List<String> truncated = new java.util.ArrayList<>(wrappedLines.subList(0, maxTextLines));
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

    /**
     * 按像素宽度切分字符串为多行(在尽量完整地切在一个\n / 空格 / 字符边界上)。
     * 折行后行宽不超过 {@code maxWidth}。
     */
    private static java.util.List<String> wrapByCharWidth(String s, net.minecraft.client.font.TextRenderer tr, int maxWidth) {
        java.util.List<String> out = new java.util.ArrayList<>();
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
