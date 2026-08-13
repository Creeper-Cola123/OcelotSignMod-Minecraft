package bklmc.ocelotsign.client.gui;

import bklmc.ocelotsign.client.UIConstants;
import bklmc.ocelotsign.client.model.ModelRegistryManager;
import bklmc.ocelotsign.item.CustomModelBlockItem;
import bklmc.ocelotsign.platform.ServerNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
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
    /** 面板左侧、上方、右侧、下方边距 */
    private static final int PANEL_MARGIN = 12;

    private final InteractionHand hand;
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

    /**
     * 构造手持物品模式的模型选择叠加层。
     *
     * @param hand 玩家手持物品的手
     */
    public ModelSelectionOverlay(InteractionHand hand) {
        this.hand = hand;
        this.targetType = TargetType.ITEM;
        this.blockPos = null;
        initialize();
    }

    /**
     * 构造已放置方块模式的模型选择叠加层。
     *
     * @param targetType 目标类型
     * @param blockPos 方块位置
     */
    public ModelSelectionOverlay(TargetType targetType, BlockPos blockPos) {
        this.hand = InteractionHand.MAIN_HAND;
        this.targetType = targetType;
        this.blockPos = blockPos;
        initialize();
    }

    /** 初始化数据与布局（替代 Screen.init()）。 */
    private void initialize() {
        Minecraft mc = Minecraft.getInstance();
        updateCachedSize();
        recalcLayout();

        Map<String, ModelRegistryManager.ModelDefinition> models = ModelRegistryManager.getAvailableModels();
        hasNoModels = models.isEmpty();

        if (!hasNoModels) {
            this.modelEntries = new ArrayList<>();
            for (Map.Entry<String, ModelRegistryManager.ModelDefinition> e : models.entrySet()) {
                ModelRegistryManager.ModelDefinition def = e.getValue();
                this.modelEntries.add(new ModelEntry(e.getKey(), def.localizedName(), def.modelIdentifier()));
            }
            this.modelEntries.sort((a, b) -> a.localizedName.compareToIgnoreCase(b.localizedName));
        }

        resetScroll();
        calculateLayout();
    }

    /** 刷新窗口尺寸缓存。 */
    private void updateCachedSize() {
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && mc.getWindow() != null) {
            cachedWidth = mc.getWindow().getGuiScaledWidth();
            cachedHeight = mc.getWindow().getGuiScaledHeight();
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
        scrollY = Mth.clamp(scrollY, 0, maxScrollY);
    }

    /** 计算滚动边界。 */
    private void calculateScrollBounds(int contentAreaH) {
        maxScrollY = Math.max(0, contentHeight - contentAreaH);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor ctx, int mx, int my, float delta) {
        updateCachedSize();
        recalcLayout();
        calculateLayout();
        renderUIPanel(ctx, mx, my, delta);
        renderBottomRightHints(ctx);
    }

    /**
     * 在游戏画面右下角渲染两行提示文字,距屏幕边缘保留较宽的间距。
     */
    private void renderBottomRightHints(GuiGraphicsExtractor ctx) {
        Font tr = Minecraft.getInstance().font;
        Component line1 = Component.translatable("screen.ocelotsignmod.model_selection.hint_switch_model");
        Component line2 = Component.translatable("screen.ocelotsignmod.model_selection.hint_import_resource_pack");

        int w1 = tr.width(line1);
        int w2 = tr.width(line2);
        int maxW = Math.max(w1, w2);
        int lineH = tr.lineHeight + 2;
        int padding = 4;
        // 距离屏幕右下角的内边距(像素),留出更大空隙避免贴边
        int margin = 24;

        int bx = cachedWidth - maxW - padding * 2 - margin;
        int by = cachedHeight - lineH * 2 - padding * 2 - margin;

        ctx.fill(bx, by, cachedWidth - margin, cachedHeight - margin, 0xAA000000);

        ctx.text(tr, line1, bx + padding, by + padding, 0xFFFFFFFF, false);
        ctx.text(tr, line2, bx + padding, by + padding + lineH, 0xFFFFFFFF, false);
    }

    @Override
    public boolean isPauseScreen() {
        // 不暂停游戏：让世界持续渲染
        return false;
    }

    /**
     * 渲染左侧 UI 面板（左侧 1/3）。
     *
     * <p>vanilla 已在世界帧缓冲上渲染了完整的世界。我们仅在左 1/3 区域
     * 绘制 UI 面板覆盖在世界之上；右 2/3 不绘制任何东西，世界自然透出。
     */
    private void renderUIPanel(GuiGraphicsExtractor ctx, int mx, int my, float delta) {
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
    private void renderHeader(GuiGraphicsExtractor ctx) {
        int headerTop = PANEL_MARGIN;
        int headerBottom = headerTop + headerHeight;
        ctx.fill(PANEL_MARGIN, headerTop, panelWidth - PANEL_MARGIN, headerBottom, UIConstants.COLOR_MAIN_HEADER);
        ctx.fill(PANEL_MARGIN, headerBottom - 1, panelWidth - PANEL_MARGIN * 2 + PANEL_MARGIN, headerBottom, UIConstants.COLOR_MAIN_BORDER);

        Component titleText = Component.translatable("screen.ocelotsignmod.model_selection.title");
        Font tr = Minecraft.getInstance().font;
        int titleW = tr.width(titleText);
        ctx.text(tr, titleText,
                PANEL_MARGIN + (panelWidth - PANEL_MARGIN * 2 - titleW) / 2,
                headerTop + (headerHeight - tr.lineHeight) / 2,
                0xFFFFFFFF, false);
    }

    /** 渲染底部栏。 */
    private void renderFooter(GuiGraphicsExtractor ctx, int mx, int my) {
        int footerTop = cachedHeight - PANEL_MARGIN - footerHeight;
        backBtnHovered = inBounds(mx, my, backBtnX, footerTop,
                UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT);

        int btnBg = backBtnHovered ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG;
        ctx.fill(backBtnX, footerTop,
                backBtnX + UIConstants.RETURN_BUTTON_WIDTH,
                footerTop + UIConstants.RETURN_BUTTON_HEIGHT,
                btnBg);
        ctx.outline(backBtnX, footerTop,
                UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT,
                UIConstants.COLOR_BTN_BORDER);

        Component backTxt = Component.translatable("screen.ocelotsignmod.model_selection.back");
        Font tr = Minecraft.getInstance().font;
        ctx.text(tr, backTxt,
                backBtnX + (UIConstants.RETURN_BUTTON_WIDTH - tr.width(backTxt)) / 2,
                footerTop + (UIConstants.RETURN_BUTTON_HEIGHT - tr.lineHeight) / 2,
                UIConstants.COLOR_BTN_TEXT, false);
    }

    /** 渲染空状态。 */
    private void renderEmptyState(GuiGraphicsExtractor ctx) {
        int cx = panelWidth / 2;
        int cy = cachedHeight / 2;

        drawEmptyIcon(ctx, cx, cy - 64);

        Component head = Component.translatable("screen.ocelotsignmod.model_selection.no_models");
        ctx.text(Minecraft.getInstance().font, head,
                cx - Minecraft.getInstance().font.width(head) / 2, cy,
                UIConstants.COLOR_SECTION_TITLE, false);

        Component sub = Component.translatable("screen.ocelotsignmod.model_selection.no_models_hint");
        ctx.text(Minecraft.getInstance().font, sub,
                cx - Minecraft.getInstance().font.width(sub) / 2, cy + 22,
                UIConstants.COLOR_DESC_TEXT, false);
    }

    /** 绘制空状态图标。 */
    private void drawEmptyIcon(GuiGraphicsExtractor ctx, int cx, int cy) {
        int size = 34;
        ctx.fill(cx - size, cy + 4, cx, cy + size + 4, UIConstants.COLOR_ITEM_BG);
        ctx.outline(cx - size, cy + 4, size, size, UIConstants.COLOR_ITEM_BORDER);
        ctx.fill(cx - size + 7, cy - 3, cx + 7, cy + size + 3, UIConstants.COLOR_ITEM_BG);
        ctx.outline(cx - size + 7, cy - 3, size, size, UIConstants.COLOR_ITEM_BORDER);
        ctx.fill(cx - size + 14, cy - 10, cx + 14, cy + size - 10, UIConstants.COLOR_ITEM_BG);
        ctx.outline(cx - size + 14, cy - 10, size, size, UIConstants.COLOR_ITEM_BORDER);

        Component q = Component.literal("?");
        int qw = Minecraft.getInstance().font.width(q);
        ctx.text(Minecraft.getInstance().font, q,
                cx - qw / 2, cy - Minecraft.getInstance().font.lineHeight / 2,
                UIConstants.COLOR_H3_TEXT, false);
    }

    /** 渲染模型列表。 */
    private void renderContent(GuiGraphicsExtractor ctx, int mx, int my) {
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
    private void renderScrollbar(GuiGraphicsExtractor ctx, int mx, int my) {
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
        ctx.outline(sx, thumbY, UIConstants.SCROLLBAR_WIDTH, thumbH, UIConstants.COLOR_ITEM_BORDER);
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
            scrollY = Mth.clamp(dragStartScrollY + d, 0, maxScrollY);
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
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.getWindow() == null) return;
        long glfwHandle = mc.getWindow().handle();
        updateCachedSize();
        recalcLayout();

        double mx = mc.mouseHandler.getScaledXPos(mc.getWindow());
        double my = mc.mouseHandler.getScaledYPos(mc.getWindow());
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
        }

        // 拖动
        if (lastMouseDown && mouseDown && isDraggingScrollbar) {
            handleMouseDragged(mx, my);
        }

        // 滚轮由 MouseScrollMixin 处理。

        lastMouseDown = mouseDown;

        // ESC 键关闭
        if (GLFW.glfwGetKey(glfwHandle, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
            closeAndPlaceBlock();
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
                scrollY = Mth.clamp(((my - trackTop) / (double) trackH) * maxScrollY, 0, maxScrollY);
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

    /** 鼠标滚轮处理（当前未启用，留作未来扩展）。 */
    @SuppressWarnings("unused")
    private void handleMouseScrolled(double mx, double my, double verticalAmount) {
        if (mx >= panelWidth) return;
        if (maxScrollY > 0) {
            scrollY = Mth.clamp(scrollY - verticalAmount * SCROLL_AMOUNT, 0, maxScrollY);
        }
    }

    /**
     * 由 Mixin 或 Fabric ScreenEvents 调用，传入 GLFW 的原始滚轮值。
     *
     * <p>GLFW 给出的 vertical 值正负方向取决于平台，因此取反方向以符合通常的"滚轮向上滚动列表向下"直觉。
     *
     * @param horizontal 水平滚动量（一般不使用）
     * @param vertical 垂直滚动量
     */
    public void handleMouseScrolledFromMixin(double horizontal, double vertical) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.getWindow() == null) return;

        // 使用 scaled 坐标获取鼠标位置
        double mx = mc.mouseHandler.getScaledXPos(mc.getWindow());
        double my = mc.mouseHandler.getScaledYPos(mc.getWindow());

        // 只响应左侧面板区域的滚轮事件
        if (mx >= panelWidth) return;

        // 计算内容区域边界
        int contentTop = PANEL_MARGIN + headerHeight + CONTENT_TOP_PAD;
        int contentBottom = cachedHeight - PANEL_MARGIN - footerHeight - 10;

        // 检查鼠标是否在内容区域内
        if (my < contentTop || my > contentBottom) return;

        // 执行滚动
        // Minecraft 的 onMouseScroll 中 vertical > 0 表示滚轮向上滚动，
        // 此时内容应该向下移动（scrollY 减小，露出下面的内容）；
        // vertical < 0 表示滚轮向下滚动，scrollY 应增大，露出上面的内容。
        if (maxScrollY > 0 && vertical != 0) {
            scrollY = Mth.clamp(scrollY - vertical * SCROLL_AMOUNT, 0, maxScrollY);
        }
    }

    /** 选择模型并同步（不关闭叠加层）。 */
    private void selectModel(String modelId) {
        applySelectionLocally(modelId);
        sendSelectionToServer(modelId);
    }

    /** 关闭叠加层并放置方块。 */
    private void closeAndPlaceBlock() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            close();
            return;
        }

        ItemStack stack = mc.player.getItemInHand(hand);
        if (!CustomModelBlockItem.isCustomModelItem(stack)) {
            close();
            return;
        }

        close();

        net.minecraft.core.Direction face = net.minecraft.core.Direction.NORTH;
        net.minecraft.world.phys.HitResult hit = mc.hitResult;
        if (hit != null && hit.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
            net.minecraft.world.phys.BlockHitResult blockHit = (net.minecraft.world.phys.BlockHitResult) hit;
            face = blockHit.getDirection();
            net.minecraft.world.item.context.UseOnContext ctx = new net.minecraft.world.item.context.UseOnContext(
                    mc.player, hand, blockHit);
            stack.useOn(ctx);
        }
    }

    /** 关闭叠加层。 */
    private void close() {
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && mc.getOverlay() == this) {
            mc.setOverlay(null);
            // 重新锁定鼠标，恢复正常的第三人称视角输入
            if (!mc.mouseHandler.isMouseGrabbed()) {
                mc.mouseHandler.grabMouse();
            }
            // 释放 useKey (右键) 状态，避免玩家按住右键打开叠加层、
            // 关闭后 useKey 仍然为 pressed，导致 doItemUse() 在下一个 tick
            // 又触发 UseBlockCallback，把叠加层再次打开。
            if (mc.options != null && mc.options.keyUse != null) {
                mc.options.keyUse.setDown(false);
            }
        }
    }

    /** 本地应用模型选择。 */
    private void applySelectionLocally(String modelId) {
        Minecraft client = Minecraft.getInstance();
        if (client == null || client.player == null) return;
        if (targetType == TargetType.ITEM) {
            ItemStack stack = client.player.getItemInHand(hand);
            if (CustomModelBlockItem.isCustomModelItem(stack)) {
                setModelIdToStack(stack, modelId);
            }
        }
    }

    /** 将模型 ID 写入物品的自定义数据组件。 */
    private static void setModelIdToStack(ItemStack stack, String modelId) {
        CompoundTag existing = getCustomNbt(stack);
        CompoundTag nbt = existing != null ? existing.copy() : new CompoundTag();
        nbt.putString(CustomModelBlockItem.SELECTED_MODEL_ID_KEY, modelId);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
    }

    /** 获取物品的自定义 NBT 数据。 */
    private static CompoundTag getCustomNbt(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return null;
        return customData.copyTag();
    }

    /** 发送模型选择到服务器。 */
    private void sendSelectionToServer(String modelId) {
        ServerNetworking.SelectModelPayload payload = new ServerNetworking.SelectModelPayload(
                modelId,
                targetType == TargetType.BLOCK && blockPos != null,
                blockPos,
                hand == InteractionHand.MAIN_HAND
        );
        ClientPlayNetworking.send(payload);
    }

    private static boolean inBounds(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
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
        private final Identifier modelIdentifier;

        ModelEntry(String modelId, String localizedName, Identifier modelIdentifier) {
            this.modelId = modelId;
            this.localizedName = localizedName;
            this.modelIdentifier = modelIdentifier;
        }

        public void render(GuiGraphicsExtractor ctx, int x, int y, int rowWidth, int mx, int my, int panelLeft, int panelRight) {
            boolean rowHover = inBounds(mx, my, panelLeft, y, panelRight - panelLeft, ROW_HEIGHT);

            ctx.fill(panelLeft, y, panelRight, y + ROW_HEIGHT,
                    rowHover ? UIConstants.COLOR_ITEM_BG : UIConstants.COLOR_MAIN_BG);
            if (rowHover) {
                ctx.outline(panelLeft, y, panelRight - panelLeft, ROW_HEIGHT, UIConstants.COLOR_ITEM_BORDER);
            }

            int btnX = panelRight - BTN_W - BTN_RIGHT_MARGIN;
            int btnY = y + (ROW_HEIGHT - BTN_H) / 2;
            boolean btnHover = inBounds(mx, my, btnX, btnY, BTN_W, BTN_H);
            int btnBg = btnHover ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG;
            ctx.fill(btnX, btnY, btnX + BTN_W, btnY + BTN_H, btnBg);
            ctx.outline(btnX, btnY, BTN_W, BTN_H, UIConstants.COLOR_BTN_BORDER);

            Component selLabel = Component.translatable("screen.ocelotsignmod.model_selection.select");
            ctx.text(Minecraft.getInstance().font, selLabel,
                    btnX + (BTN_W - Minecraft.getInstance().font.width(selLabel)) / 2,
                    btnY + (BTN_H - Minecraft.getInstance().font.lineHeight) / 2,
                    UIConstants.COLOR_BTN_TEXT, false);

            int nameMaxW = btnX - (panelLeft + 6);
            Font tr = Minecraft.getInstance().font;

            // 在宽度允许的范围内逐字符换行;不依赖 wrapLines,避免 OrderedText 反求字符串。
            // 行高 = lineHeight + 1;最多容纳的行数由 ROW_HEIGHT 决定,
            // 超过行数时仅保留前面行,最后一行做 "..." 截断,
            // 确保总高度恒等于 ROW_HEIGHT,不会撑高整行。
            java.util.List<String> wrappedLines = wrapByCharWidth(localizedName, tr, nameMaxW);

            int maxTextLines = Math.max(1, (ROW_HEIGHT - 4) / (tr.lineHeight + 1));
            if (wrappedLines.size() > maxTextLines) {
                java.util.List<String> truncated = new java.util.ArrayList<>(wrappedLines.subList(0, maxTextLines));
                String last = truncated.get(maxTextLines - 1);
                while (!last.isEmpty() && tr.width(last + "...") > nameMaxW) {
                    last = last.substring(0, last.length() - 1);
                }
                truncated.set(maxTextLines - 1, last + "...");
                wrappedLines = truncated;
            }

            int textBlockH = wrappedLines.size() * (tr.lineHeight + 1) - 1;
            int textY = y + Math.max(0, (ROW_HEIGHT - textBlockH) / 2);
            int textX = panelLeft + 6;
            for (int li = 0; li < wrappedLines.size(); li++) {
                ctx.text(tr, Component.literal(wrappedLines.get(li)),
                        textX,
                        textY + li * (tr.lineHeight + 1),
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
    private static java.util.List<String> wrapByCharWidth(String s, Font tr, int maxWidth) {
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
                if (tr.width(line + ch) > maxWidth) {
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