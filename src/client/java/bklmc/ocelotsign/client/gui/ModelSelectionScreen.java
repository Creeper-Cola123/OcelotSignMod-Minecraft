package bklmc.ocelotsign.client.gui;

import bklmc.ocelotsign.client.UIConstants;
import bklmc.ocelotsign.client.model.ModelRegistryManager;
import bklmc.ocelotsign.item.CustomModelBlockItem;
import bklmc.ocelotsign.platform.ModIdentifiers;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 模型选择界面。
 *
 * <p>作为一个不暂停游戏的 Screen 渲染在游戏世界之中（参考项目中
 * 现成的 {@code PatternAndFontBlankScreen} 模式）：
 * <ul>
 *     <li>不会暂停游戏，世界持续渲染</li>
 *     <li>左 1/3 区域绘制客户端 UI 面板</li>
 *     <li>右 2/3 区域完全不影响，世界原样透出</li>
 * </ul>
 */
public class ModelSelectionScreen extends Screen {

    /**
     * 选择目标类型
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
    /** 面板左边、上边、右边、下边边距 */
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

    /**
     * 构建手持物品模式的模型选择界面。
     *
     * @param hand 玩家手持物品的手
     */
    public ModelSelectionScreen(Hand hand) {
        super(Text.empty());
        this.hand = hand;
        this.targetType = TargetType.ITEM;
        this.blockPos = null;
        initialize();
    }

    /**
     * 构建已放置方块模式的模型选择界面。
     *
     * @param targetType 目标类型
     * @param blockPos 方块位置
     */
    public ModelSelectionScreen(TargetType targetType, BlockPos blockPos) {
        super(Text.empty());
        this.hand = Hand.MAIN_HAND;
        this.targetType = targetType;
        this.blockPos = blockPos;
        initialize();
    }

    /** 初始化数据与布局 */
    private void initialize() {
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

    /** 更新窗口尺寸缓存 */
    private void updateCachedSize() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc != null && mc.getWindow() != null) {
            cachedWidth = mc.getWindow().getScaledWidth();
            cachedHeight = mc.getWindow().getScaledHeight();
        }
    }

    /** 面板宽度固定（像素）。当屏幕更窄时不超过屏幕宽度的 1/3 */
    private static final int PANEL_PREFERRED_WIDTH = 380;

    /** 重新计算布局参数 */
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

    /** 重置滚动位置 */
    private void resetScroll() {
        scrollY = 0;
        calculateLayout();
    }

    /** 计算布局参数 */
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

    /** 计算滚动边界 */
    private void calculateScrollBounds(int contentAreaH) {
        maxScrollY = Math.max(0, contentHeight - contentAreaH);
    }

    @Override
    public void render(MatrixStack matrices, int mx, int my, float delta) {
        updateCachedSize();
        recalcLayout();
        calculateLayout();
        renderUIPanel(matrices, mx, my, delta);
        renderBottomRightHints(matrices);
    }

    /**
     * 在游戏画面右下角渲染两行提示文字，距屏幕边缘保留较宽的间距。
     */
    private void renderBottomRightHints(MatrixStack matrices) {
        var tr = textRenderer;
        Text line1 = Text.translatable("screen.ocelotsignmod.model_selection.hint_switch_model");
        Text line2 = Text.translatable("screen.ocelotsignmod.model_selection.hint_import_resource_pack");

        int w1 = tr.getWidth(line1);
        int w2 = tr.getWidth(line2);
        int maxW = Math.max(w1, w2);
        int lineH = tr.fontHeight + 2;
        int padding = 4;
        // 距离屏幕右下角的内边距（像素），留出更大空间避免截边
        int margin = 24;

        int bx = cachedWidth - maxW - padding * 2 - margin;
        int by = cachedHeight - lineH * 2 - padding * 2 - margin;

        DrawableHelper.fill(matrices, bx, by, cachedWidth - margin, cachedHeight - margin, 0xAA000000);

        tr.draw(matrices, line1, bx + padding, by + padding, 0xFFFFFFFF);
        tr.draw(matrices, line2, bx + padding, by + padding + lineH, 0xFFFFFFFF);
    }

    @Override
    public boolean shouldPause() {
        // 不暂停游戏：让世界持续渲染
        return false;
    }

    /**
     * 渲染左侧 UI 面板（左 1/3）。
     *
     * <p>vanilla 已在世界画面缓存上渲染完了完整的视野。我们的仅在左 1/3 区域
     * 绘制 UI 面板覆盖在世界之上；其余 2/3 不绘制任何东西，世界自然透出。
     */
    private void renderUIPanel(MatrixStack matrices, int mx, int my, float delta) {
        // 内容区域背景
        DrawableHelper.fill(matrices, PANEL_MARGIN, PANEL_MARGIN,
                panelWidth - PANEL_MARGIN, cachedHeight - PANEL_MARGIN,
                UIConstants.COLOR_MAIN_BG);

        renderHeader(matrices);
        renderFooter(matrices, mx, my);

        if (hasNoModels) {
            renderEmptyState(matrices);
        } else {
            handleScrollbarDragging(mx, my);
            renderContent(matrices, mx, my);
            renderScrollbar(matrices, mx, my);
        }
    }

    /** 渲染标题栏 */
    private void renderHeader(MatrixStack matrices) {
        int headerTop = PANEL_MARGIN;
        int headerBottom = headerTop + headerHeight;
        DrawableHelper.fill(matrices, PANEL_MARGIN, headerTop, panelWidth - PANEL_MARGIN, headerBottom, UIConstants.COLOR_MAIN_HEADER);
        drawOutline(matrices, PANEL_MARGIN, headerBottom - 1, panelWidth - PANEL_MARGIN * 2, 1, UIConstants.COLOR_MAIN_BORDER);

        Text titleText = Text.translatable("screen.ocelotsignmod.model_selection.title");
        int titleW = textRenderer.getWidth(titleText);
        textRenderer.draw(matrices, titleText,
                PANEL_MARGIN + (panelWidth - PANEL_MARGIN * 2 - titleW) / 2,
                headerTop + (headerHeight - textRenderer.fontHeight) / 2,
                0xFFFFFFFF);
    }

    /** 渲染底部栏 */
    private void renderFooter(MatrixStack matrices, int mx, int my) {
        int footerTop = cachedHeight - PANEL_MARGIN - footerHeight;
        backBtnHovered = inBounds(mx, my, backBtnX, footerTop,
                UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT);

        int btnBg = backBtnHovered ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG;
        DrawableHelper.fill(matrices, backBtnX, footerTop,
                backBtnX + UIConstants.RETURN_BUTTON_WIDTH,
                footerTop + UIConstants.RETURN_BUTTON_HEIGHT,
                btnBg);
        drawOutline(matrices, backBtnX, footerTop,
                UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT,
                UIConstants.COLOR_BTN_BORDER);

        Text backTxt = Text.translatable("screen.ocelotsignmod.model_selection.back");
        textRenderer.draw(matrices, backTxt,
                backBtnX + (UIConstants.RETURN_BUTTON_WIDTH - textRenderer.getWidth(backTxt)) / 2,
                footerTop + (UIConstants.RETURN_BUTTON_HEIGHT - textRenderer.fontHeight) / 2,
                UIConstants.COLOR_BTN_TEXT);
    }

    /** 渲染空状态 */
    private void renderEmptyState(MatrixStack matrices) {
        int cx = panelWidth / 2;
        int cy = cachedHeight / 2;

        drawEmptyIcon(matrices, cx, cy - 64);

        Text head = Text.translatable("screen.ocelotsignmod.model_selection.no_models");
        textRenderer.draw(matrices, head,
                cx - textRenderer.getWidth(head) / 2, cy,
                UIConstants.COLOR_SECTION_TITLE);

        Text sub = Text.translatable("screen.ocelotsignmod.model_selection.no_models_hint");
        textRenderer.draw(matrices, sub,
                cx - textRenderer.getWidth(sub) / 2, cy + 22,
                UIConstants.COLOR_DESC_TEXT);
    }

    /** 绘制空状态图标（单个灰色边框方块） */
    private void drawEmptyIcon(MatrixStack matrices, int cx, int cy) {
        int size = 34;
        int half = size / 2;
        int x1 = cx - half;
        int y1 = cy - half;

        // 绘制灰色边框方块（每边不同颜色，模拟 3D 效果）
        // 顶边 - 浅灰
        drawOutline(matrices, x1, y1, size, 1, 0xFFBBBBBB);
        // 底边 - 深灰
        drawOutline(matrices, x1, y1 + size - 1, size, 1, 0xFF666666);
        // 左竖边 - 浅灰
        drawOutline(matrices, x1, y1, 1, size, 0xFF999999);
        // 右竖边 - 深灰
        drawOutline(matrices, x1 + size - 1, y1, 1, size, 0xFF444444);

        // 绘制问号
        Text q = Text.literal("?");
        int qw = textRenderer.getWidth(q);
        textRenderer.draw(matrices, q,
                cx - qw / 2, cy - textRenderer.fontHeight / 2,
                UIConstants.COLOR_H3_TEXT);
    }

    /** 渲染模型列表 */
    private void renderContent(MatrixStack matrices, int mx, int my) {
        int contentTop = PANEL_MARGIN + headerHeight + CONTENT_TOP_PAD;
        int contentBottom = cachedHeight - PANEL_MARGIN - footerHeight - 10;
        boolean hasScrollbar = maxScrollY > 20;
        int rightEdge = hasScrollbar ? panelWidth - PANEL_MARGIN - UIConstants.SCROLLBAR_WIDTH : panelWidth - PANEL_MARGIN;
        myEnableScissor(PANEL_MARGIN, contentTop, rightEdge, contentBottom);

        int startY = contentTop - (int) scrollY;

        if (modelEntries != null) {
            for (int i = 0; i < modelEntries.size(); i++) {
                int y = startY + i * (ROW_HEIGHT + ROW_GAP);
                if (y + ROW_HEIGHT < contentTop || y > contentBottom) continue;
                modelEntries.get(i).render(matrices, contentLeft, y, contentRight - contentLeft, mx, my, panelLeft, panelRight);
            }
        }

        myDisableScissor();
    }

    /** 渲染滚动条 */
    private void renderScrollbar(MatrixStack matrices, int mx, int my) {
        int contentTop = PANEL_MARGIN + headerHeight + CONTENT_TOP_PAD;
        int contentBottom = cachedHeight - PANEL_MARGIN - footerHeight - 10;
        int winH = contentBottom - contentTop;
        if (maxScrollY <= 20) return;

        int sx = panelWidth - PANEL_MARGIN - UIConstants.SCROLLBAR_WIDTH - 3;
        int trackTop = contentTop + 8;
        int trackH = winH - 16;

        DrawableHelper.fill(matrices, sx, trackTop, sx + UIConstants.SCROLLBAR_WIDTH, trackTop + trackH, UIConstants.COLOR_SCROLLBAR_TRACK);

        float ratio = (float) winH / (float) (winH + maxScrollY);
        int thumbH = Math.max(UIConstants.SCROLLBAR_MIN_HEIGHT, (int) (trackH * ratio));
        float scrollPct = maxScrollY > 0 ? (float) scrollY / (float) maxScrollY : 0;
        int thumbY = trackTop + (int) ((trackH - thumbH) * scrollPct);

        boolean hover = mx >= sx && mx <= sx + UIConstants.SCROLLBAR_WIDTH
                && my >= thumbY && my <= thumbY + thumbH;
        int thumbC = hover ? UIConstants.COLOR_SCROLLBAR_THUMB_HOVER : UIConstants.COLOR_SCROLLBAR_THUMB;
        DrawableHelper.fill(matrices, sx, thumbY, sx + UIConstants.SCROLLBAR_WIDTH, thumbY + thumbH, thumbC);
        drawOutline(matrices, sx, thumbY, UIConstants.SCROLLBAR_WIDTH, thumbH, UIConstants.COLOR_ITEM_BORDER);
    }

    // ==================== Screen 标准事件覆盖 ====================

    /**
     * 鼠标点击事件
     *
     * <p>覆盖 {@link Screen#mouseClicked} 以处理按钮点击和滚动条拖拽开始。
     */
    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button != 0) return super.mouseClicked(mx, my, button); // 只处理左键

        // 只响应左侧 UI 面板区域内的鼠标事件
        if (mx >= panelWidth) return super.mouseClicked(mx, my, button);

        // 返回按钮
        int footerTop = cachedHeight - PANEL_MARGIN - footerHeight;
        if (inBounds(mx, my, backBtnX, footerTop,
                UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT)) {
            closeAndPlaceBlock();
            return true;
        }

        // 滚动条区域点击
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
                // 点击滑块：开始拖拽
                isDraggingScrollbar = true;
                dragStartMouseY = my;
                dragStartScrollY = scrollY;
                return true;
            } else if (my >= trackTop && my <= trackTop + trackH) {
                // 点击轨道：跳转
                scrollY = MathHelper.clamp(((my - trackTop) / (double) trackH) * maxScrollY, 0, maxScrollY);
                return true;
            }
        }

        // 列表项目按钮点击
        if (modelEntries != null) {
            int startY = contentTop - (int) scrollY;
            for (int i = 0; i < modelEntries.size(); i++) {
                int y = startY + i * (ROW_HEIGHT + ROW_GAP);
                if (modelEntries.get(i).mouseClicked(mx, my, contentLeft, y, contentRight - contentLeft)) {
                    return true;
                }
            }
        }

        return super.mouseClicked(mx, my, button);
    }

    /**
     * 鼠标拖拽事件
     *
     * <p>覆盖 {@link Screen#mouseDragged} 以处理滚动条拖拽动作
     */
    @Override
    public boolean mouseDragged(double mx, double my, int button, double deltaX, double deltaY) {
        if (button != 0 || !isDraggingScrollbar) return super.mouseDragged(mx, my, button, deltaX, deltaY);
        if (mx >= panelWidth) return true;

        handleScrollbarDragging((int) mx, (int) my);
        return true;
    }

    /**
     * 鼠标释放事件
     *
     * <p>覆盖 {@link Screen#mouseReleased} 以结束滚动条拖拽
     */
    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        if (button != 0) return super.mouseReleased(mx, my, button);

        if (isDraggingScrollbar) {
            isDraggingScrollbar = false;
        }

        return super.mouseReleased(mx, my, button);
    }

    /**
     * 鼠标滚轮事件
     *
     * <p>覆盖 {@link Screen#mouseScrolled} 以处理内容滚动。
     */
    @Override
    public boolean mouseScrolled(double mx, double my, double amount) {
        if (mx >= panelWidth) return super.mouseScrolled(mx, my, amount);

        int contentTop = PANEL_MARGIN + headerHeight + CONTENT_TOP_PAD;
        int contentBottom = cachedHeight - PANEL_MARGIN - footerHeight - 10;

        if (my < contentTop || my > contentBottom) return super.mouseScrolled(mx, my, amount);

        if (maxScrollY > 0 && amount != 0) {
            scrollY = MathHelper.clamp(scrollY - amount * SCROLL_AMOUNT, 0, maxScrollY);
            return true;
        }

        return super.mouseScrolled(mx, my, amount);
    }

    /**
     * 键盘按键事件
     *
     * <p>覆盖 {@link Screen#keyPressed} 以处理 ESC 键关闭界面。
     */
    @Override
    public boolean keyPressed(int keyCode, int sc, int mods) {
        if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE) {
            closeAndPlaceBlock();
            return true;
        }
        return super.keyPressed(keyCode, sc, mods);
    }

    /** 滚动条拖拽处理 */
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

    /** 选择模型并同步（不关闭界面） */
    private void selectModel(String modelId) {
        applySelectionLocally(modelId);
        sendSelectionToServer(modelId);
    }

    /** 关闭界面并放置方块 */
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

    /**
     * 关闭界面
     *
     * <p>覆盖 {@link Screen#close}，在关闭时重新锁定鼠标光标。
     */
    @Override
    public void close() {
        MinecraftClient mc = MinecraftClient.getInstance();
        // 仅在当前界面仍处于激活状态时执行清理逻辑
        if (mc != null && mc.currentScreen == this) {
            if (!mc.mouse.isCursorLocked()) {
                mc.mouse.lockCursor();
            }
            if (mc.options != null && mc.options.useKey != null) {
                mc.options.useKey.setPressed(false);
            }
            mc.setScreen(null);
        }
    }

    /** 本地应用模型选择 */
    private void applySelectionLocally(String modelId) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;

        if (targetType == TargetType.ITEM) {
            ItemStack stack = client.player.getStackInHand(hand);
            if (CustomModelBlockItem.isCustomModelItem(stack)) {
                NbtCompound nbt = stack.getNbt();
                NbtCompound customData = nbt != null ? nbt.copy() : new NbtCompound();
                customData.putString(CustomModelBlockItem.SELECTED_MODEL_ID_KEY, modelId);
                stack.setNbt(customData);
            }
        }
        /** 新编辑器 */ else if (targetType == TargetType.BLOCK && blockPos != null && client.world != null) {
            net.minecraft.block.entity.BlockEntity be = client.world.getBlockEntity(blockPos);
            if (be instanceof bklmc.ocelotsign.blockentity.CustomModelBlockEntity customBE) {
                // 直接调用该方法即可。因为 CustomModelBlockEntity 中已经写好了 markDirty 和 updateListeners
                // BER 每一帧都会调用 getModelId()，所以方块在世界中间接切换模型。
                customBE.setModelId(modelId);
            }
        }
    }

    /** 发送模型选择到服务器 */
    private void sendSelectionToServer(String modelId) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(modelId);
        if (targetType == TargetType.BLOCK && blockPos != null) {
            buf.writeBoolean(true);
            buf.writeBlockPos(blockPos);
        } else {
            buf.writeBoolean(false);
            buf.writeBoolean(hand == Hand.MAIN_HAND);
        }
        ClientPlayNetworking.send(ModIdentifiers.SELECT_MODEL, buf);
    }

    private static boolean inBounds(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    /** 绘制 1px 边框 */
    private static void drawOutline(MatrixStack matrices, int x, int y, int w, int h, int color) {
        DrawableHelper.fill(matrices, x, y, x + w, y + 1, color);
        DrawableHelper.fill(matrices, x, y + h - 1, x + w, y + h, color);
        DrawableHelper.fill(matrices, x, y, x + 1, y + h, color);
        DrawableHelper.fill(matrices, x + w - 1, y, x + w, y + h, color);
    }

    /** 启用裁剪（兼容 1.19.4 API） */
    private void myEnableScissor(int x, int y, int width, int height) {
        MinecraftClient client = MinecraftClient.getInstance();
        double scale = client.getWindow().getScaleFactor();
        int windowHeight = client.getWindow().getFramebufferHeight();
        org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);
        org.lwjgl.opengl.GL11.glScissor((int)(x * scale), (int)(windowHeight - (y + height) * scale), (int)(width * scale), (int)(height * scale));
    }

    /** 禁用裁剪 */
    private void myDisableScissor() {
        org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);
    }

    // ==================== 内部类：ModelEntry ====================

    /**
     * 模型列表条目
     *
     * <p>每行显示：模型名称（左侧）+ 选择按钮（右侧）。
     */
    class ModelEntry {
        private static final int BTN_W = 60;
        private static final int BTN_H = 22;
        /** 按钮距离面板右边缘的内边距 */
        private static final int BTN_RIGHT_MARGIN = 6;
        /** 名称距离面板左边缘的内边距 */
        private static final int NAME_PAD_LEFT = 6;

        private final String modelId;
        private final String localizedName;
        private final Identifier modelIdentifier;

        ModelEntry(String modelId, String localizedName, Identifier modelIdentifier) {
            this.modelId = modelId;
            this.localizedName = localizedName;
            this.modelIdentifier = modelIdentifier;
        }

        public void render(MatrixStack matrices, int x, int y, int rowWidth, int mx, int my, int panelLeft, int panelRight) {
            boolean rowHover = inBounds(mx, my, panelLeft, y, panelRight - panelLeft, ROW_HEIGHT);

            DrawableHelper.fill(matrices, panelLeft, y, panelRight, y + ROW_HEIGHT,
                    rowHover ? UIConstants.COLOR_ITEM_BG : UIConstants.COLOR_MAIN_BG);
            if (rowHover) {
                drawOutline(matrices, panelLeft, y, panelRight - panelLeft, ROW_HEIGHT, UIConstants.COLOR_ITEM_BORDER);
            }

            // 选择按钮
            int btnX = panelRight - BTN_W - BTN_RIGHT_MARGIN;
            int btnY = y + (ROW_HEIGHT - BTN_H) / 2;
            boolean btnHover = inBounds(mx, my, btnX, btnY, BTN_W, BTN_H);
            int btnBg = btnHover ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG;
            DrawableHelper.fill(matrices, btnX, btnY, btnX + BTN_W, btnY + BTN_H, btnBg);
            drawOutline(matrices, btnX, btnY, BTN_W, BTN_H, UIConstants.COLOR_BTN_BORDER);

            Text selLabel = Text.translatable("screen.ocelotsignmod.model_selection.select");
            textRenderer.draw(matrices, selLabel,
                    btnX + (BTN_W - textRenderer.getWidth(selLabel)) / 2,
                    btnY + (BTN_H - textRenderer.fontHeight) / 2,
                    UIConstants.COLOR_BTN_TEXT);

            // 模型名称
            int nameLeft = panelLeft + NAME_PAD_LEFT;
            int nameMaxW = btnX - nameLeft - 4;

            List<String> wrappedLines = wrapByCharWidth(localizedName, textRenderer, nameMaxW);
            int maxTextLines = Math.max(1, (ROW_HEIGHT - 4) / (textRenderer.fontHeight + 1));
            if (wrappedLines.size() > maxTextLines) {
                List<String> truncated = new ArrayList<>(wrappedLines.subList(0, maxTextLines));
                String last = truncated.get(maxTextLines - 1);
                while (!last.isEmpty() && textRenderer.getWidth(last + "...") > nameMaxW) {
                    last = last.substring(0, last.length() - 1);
                }
                truncated.set(maxTextLines - 1, last + "...");
                wrappedLines = truncated;
            }

            int textBlockH = wrappedLines.size() * (textRenderer.fontHeight + 1) - 1;
            int textY = y + Math.max(0, (ROW_HEIGHT - textBlockH) / 2);
            for (int li = 0; li < wrappedLines.size(); li++) {
                textRenderer.draw(matrices, Text.literal(wrappedLines.get(li)),
                        nameLeft,
                        textY + li * (textRenderer.fontHeight + 1),
                        0xFF000000);
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
     * 按像素宽度切割字符串为多行
     */
    private static List<String> wrapByCharWidth(String s, net.minecraft.client.font.TextRenderer tr, int maxWidth) {
        List<String> out = new ArrayList<>();
        if (s == null || s.isEmpty()) {
            out.add("");
            return out;
        }
        for (String segment : s.split("\n", -1)) {
            StringBuilder line = new StringBuilder();
            for (int i = 0; i < segment.length(); ) {
                int cp = segment.codePointAt(i);
                String ch = new String(Character.toChars(cp));
                i += Character.charCount(cp);
                if (tr.getWidth(line + ch) > maxWidth) {
                    if (line.length() == 0) {
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
