package bklmc.ocelotsign.client.gui;

import bklmc.ocelotsign.OcelotSignMod;
import bklmc.ocelotsign.client.UIConstants;
import bklmc.ocelotsign.client.model.ModelRegistryManager;
import bklmc.ocelotsign.item.CustomModelBlockItem;
import bklmc.ocelotsign.platform.ServerNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 模型选择界面
 *
 * @see ModelRegistryManager
 */
public class ModelSelectionScreen extends Screen {
    /**
     * 选择目标类型
     */
    public enum TargetType {
        ITEM, BLOCK
    }

    /** 布局常量 - 标题栏高度。 */
    private static final int HEADER_HEIGHT = UIConstants.HEADER_HEIGHT;
    private static final int COLUMN_GAP = 14;
    private static final int ROW_GAP = 14;
    private static final int CARD_HEIGHT = 110;
    private static final int CARD_INNER_PAD = 6;
    private static final int MODEL_ZONE_H = 58;
    private static final int SCROLL_AMOUNT = UIConstants.SCROLL_AMOUNT;
    private static final int CONTENT_TOP_PAD = 16;
    private static final int CONTENT_BOTTOM_PAD = 8;

    /** 列数与卡片宽度的约束常量。 */
    private static final int MIN_COLS = 2;
    private static final int MAX_COLS = 10;
    private static final int CARD_MIN_WIDTH = 100;
    private static final int CARD_MAX_WIDTH = 200;

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

    private int backBtnX = 0, backBtnY = 0;
    private boolean backBtnHovered = false;
    private int cardWidth = CARD_MIN_WIDTH;
    private int cols = MIN_COLS;

    /**
     * 构造手持物品模式的模型选择界面。
     *
     * @param hand 玩家手持物品的手
     */
    public ModelSelectionScreen(InteractionHand hand) {
        super(Component.translatable("screen.ocelotsignmod.model_selection.title"));
        this.hand = hand;
        this.targetType = TargetType.ITEM;
        this.blockPos = null;
    }

    /**
     * 构造已放置方块模式的模型选择界面。
     *
     * @param targetType 目标类型
     * @param blockPos 方块位置
     */
    public ModelSelectionScreen(TargetType targetType, BlockPos blockPos) {
        super(Component.translatable("screen.ocelotsignmod.model_selection.title"));
        this.hand = InteractionHand.MAIN_HAND;
        this.targetType = targetType;
        this.blockPos = blockPos;
    }

    @Override
    protected void init() {
        super.init();
        // 返回按钮底部居中
        backBtnX = (width - UIConstants.RETURN_BUTTON_WIDTH) / 2;
        backBtnY = height - UIConstants.RETURN_BUTTON_HEIGHT - 10;

        Map<String, ModelRegistryManager.ModelDefinition> models = ModelRegistryManager.getAvailableModels();
        hasNoModels = models.isEmpty();

        if (!hasNoModels) {
            this.modelEntries = new ArrayList<>();
            for (Map.Entry<String, ModelRegistryManager.ModelDefinition> e : models.entrySet()) {
                ModelRegistryManager.ModelDefinition def = e.getValue();
                this.modelEntries.add(new ModelEntry(e.getKey(), def.localizedName(), def.modelIdentifier()));
            }
        }

        resetScroll();
        calculateLayout();

        // 注册滚轮事件
        ScreenMouseEvents.allowMouseScroll(this).register((screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
            if (maxScrollY > 0) {
                scrollY = Mth.clamp(scrollY - verticalAmount * SCROLL_AMOUNT, 0, maxScrollY);
            }
            return maxScrollY <= 0;
        });
    }

    /** 重置滚动位置。 */
    private void resetScroll() {
        scrollY = 0;
        int contentTop = HEADER_HEIGHT + CONTENT_TOP_PAD;
        int contentBottom = height - UIConstants.RETURN_BUTTON_HEIGHT - 10 - 4;
        int contentAreaH = contentBottom - contentTop;
        calculateScrollBounds(contentAreaH);
        scrollY = Mth.clamp(scrollY, 0, maxScrollY);
    }

    /** 计算布局参数。 */
    private void calculateLayout() {
        int availW = width - UIConstants.SCROLLBAR_WIDTH - COLUMN_GAP - 20;

        // 动态计算列数与卡片宽度
        this.cols = Mth.clamp((availW + COLUMN_GAP) / (CARD_MIN_WIDTH + COLUMN_GAP), MIN_COLS, MAX_COLS);
        this.cardWidth = (availW - (this.cols - 1) * COLUMN_GAP) / this.cols;

        int contentTop = HEADER_HEIGHT + CONTENT_TOP_PAD;
        int contentBottom = height - UIConstants.RETURN_BUTTON_HEIGHT - 10 - 4;
        int contentAreaH = contentBottom - contentTop;

        if (modelEntries != null && !modelEntries.isEmpty()) {
            int rows = (int) Math.ceil((double) modelEntries.size() / this.cols);
            contentHeight = rows * CARD_HEIGHT + (rows - 1) * ROW_GAP;
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

    /**
     * 渲染界面。
     *
     * @param ctx 绘制上下文
     * @param mx 鼠标X坐标
     * @param my 鼠标Y坐标
     * @param delta 帧间时间差
     */
    @Override
    public void extractRenderState(GuiGraphicsExtractor ctx, int mx, int my, float delta) {
        // 先调用父类渲染（绘制背景），再绘制自定义UI
        super.extractRenderState(ctx, mx, my, delta);


        // 背景
        ctx.fill(0, 0, width, height, UIConstants.COLOR_MAIN_BG);
        // 标题栏
        ctx.fill(0, 0, width, HEADER_HEIGHT, UIConstants.COLOR_MAIN_HEADER);
        // 标题栏底边
        ctx.outline(0, HEADER_HEIGHT - 1, width, 1, UIConstants.COLOR_MAIN_BORDER);

        renderHeader(ctx, mx, my);
        renderFooter(ctx, mx, my);

        if (hasNoModels) {
            renderEmptyState(ctx);
        } else {
            calculateLayout();
            handleScrollbarDragging(mx, my);
            renderContent(ctx, mx, my);
            renderScrollbar(ctx, mx, my);
        }
    }

    /** 渲染标题栏。 */
    private void renderHeader(GuiGraphicsExtractor ctx, int mx, int my) {
        // 居中白色标题
        int titleW = font.width(title);
        ctx.text(font, title,
                (width - titleW) / 2,
                (HEADER_HEIGHT - font.lineHeight) / 2,
                0xFFFFFFFF, false);
    }

    /** 渲染底部栏。 */
    private void renderFooter(GuiGraphicsExtractor ctx, int mx, int my) {
        backBtnHovered = inBounds(mx, my, backBtnX, backBtnY,
                UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT);

        int btnBg = backBtnHovered ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG;
        ctx.fill(backBtnX, backBtnY,
                backBtnX + UIConstants.RETURN_BUTTON_WIDTH,
                backBtnY + UIConstants.RETURN_BUTTON_HEIGHT,
                btnBg);
        ctx.outline(backBtnX, backBtnY,
                UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT,
                UIConstants.COLOR_BTN_BORDER);

        Component backTxt = Component.translatable("screen.ocelotsignmod.model_selection.back");
        ctx.text(font, backTxt,
                backBtnX + (UIConstants.RETURN_BUTTON_WIDTH - font.width(backTxt)) / 2,
                backBtnY + (UIConstants.RETURN_BUTTON_HEIGHT - font.lineHeight) / 2,
                UIConstants.COLOR_BTN_TEXT, false);
    }

    /** 渲染空状态。 */
    private void renderEmptyState(GuiGraphicsExtractor ctx) {
        int cx = width / 2;
        int cy = height / 2;

        drawEmptyIcon(ctx, cx, cy - 64);

        // 主提示
        Component head = Component.translatable("screen.ocelotsignmod.model_selection.no_models");
        ctx.text(font, head, cx - font.width(head) / 2, cy, UIConstants.COLOR_SECTION_TITLE, false);

        // 副提示
        Component sub = Component.translatable("screen.ocelotsignmod.model_selection.no_models_hint");
        ctx.text(font, sub, cx - font.width(sub) / 2, cy + 22, UIConstants.COLOR_DESC_TEXT, false);
    }

    /** 绘制空状态图标。 */
    private void drawEmptyIcon(GuiGraphicsExtractor ctx, int cx, int cy) {
        int size = 34;
        // 三层叠块
        ctx.fill(cx - size, cy + 4, cx, cy + size + 4, UIConstants.COLOR_ITEM_BG);
        ctx.outline(cx - size, cy + 4, size, size, UIConstants.COLOR_ITEM_BORDER);
        ctx.fill(cx - size + 7, cy - 3, cx + 7, cy + size + 3, UIConstants.COLOR_ITEM_BG);
        ctx.outline(cx - size + 7, cy - 3, size, size, UIConstants.COLOR_ITEM_BORDER);
        ctx.fill(cx - size + 14, cy - 10, cx + 14, cy + size - 10, UIConstants.COLOR_ITEM_BG);
        ctx.outline(cx - size + 14, cy - 10, size, size, UIConstants.COLOR_ITEM_BORDER);

        // 问号
        Component q = Component.literal("?");
        int qw = font.width(q);
        ctx.text(font, q, cx - qw / 2, cy - font.lineHeight / 2, UIConstants.COLOR_H3_TEXT, false);
    }

    /** 渲染卡片网格。 */
    private void renderContent(GuiGraphicsExtractor ctx, int mx, int my) {
        int top = HEADER_HEIGHT + CONTENT_TOP_PAD;
        int bottom = height - UIConstants.RETURN_BUTTON_HEIGHT - 10 - 4;
        boolean hasScrollbar = maxScrollY > 20;
        int rightEdge = hasScrollbar ? width - UIConstants.SCROLLBAR_WIDTH : width;
        ctx.enableScissor(0, top, rightEdge, bottom);

        int totalW = cols * cardWidth + (cols - 1) * COLUMN_GAP;
        int startX = hasScrollbar ? (width - UIConstants.SCROLLBAR_WIDTH - totalW) / 2
                                  : (width - totalW) / 2;
        int startY = top - (int) scrollY;

        if (modelEntries != null) {
            for (int i = 0; i < modelEntries.size(); i++) {
                int col = i % cols;
                int row = i / cols;
                int x = startX + col * (cardWidth + COLUMN_GAP);
                int y = startY + row * (CARD_HEIGHT + ROW_GAP);

                if (y + CARD_HEIGHT < top || y > height) continue;
                modelEntries.get(i).render(ctx, x, y, cardWidth, CARD_HEIGHT, mx, my);
            }
        }

        ctx.disableScissor();
    }

    /** 渲染滚动条。 */
    private void renderScrollbar(GuiGraphicsExtractor ctx, int mx, int my) {
        int top = HEADER_HEIGHT + CONTENT_TOP_PAD;
        int bottom = height - UIConstants.RETURN_BUTTON_HEIGHT - 10 - 4;
        int winH = bottom - top;
        if (maxScrollY <= 20) return;

        int sx = width - UIConstants.SCROLLBAR_WIDTH - 3;
        int trackTop = top + 8;
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
        int top = HEADER_HEIGHT + CONTENT_TOP_PAD;
        int bottom = height - UIConstants.RETURN_BUTTON_HEIGHT - 10 - 4;
        int winH = bottom - top;
        float ratio = (float) winH / (float) (winH + maxScrollY);
        int thumbH = Math.max(UIConstants.SCROLLBAR_MIN_HEIGHT, (int) (winH * ratio));
        int range = winH - thumbH;
        if (range > 0) {
            double d = ((my - dragStartMouseY) / range) * maxScrollY;
            scrollY = Mth.clamp(dragStartScrollY + d, 0, maxScrollY);
        }
    }

    /**
     * 鼠标点击事件。
     *
     * @param event 鼠标事件
     * @param doubleClick 是否是双击
     * @return 是否消费事件
     */
    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        final double mx = event.x();
        final double my = event.y();
        final int btn = event.button();
        if (btn == 0) {
            // 返回按钮
            if (inBounds(mx, my, backBtnX, backBtnY,
                    UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT)) {
                onClose();
                return true;
            }

            // 滚动条
            int sx = width - UIConstants.SCROLLBAR_WIDTH - 3;
            int top = HEADER_HEIGHT + CONTENT_TOP_PAD;
            int bottom = height - UIConstants.RETURN_BUTTON_HEIGHT - 10 - 4;

            if (maxScrollY > 20 && mx >= sx - 2 && mx <= sx + UIConstants.SCROLLBAR_WIDTH + 2) {
                int trackTop = top + 8;
                int trackH = bottom - top - 16;
                float ratio = (float) (bottom - top - 16) / (float) ((bottom - top - 16) + maxScrollY);
                int thumbH = Math.max(UIConstants.SCROLLBAR_MIN_HEIGHT, (int) ((bottom - top - 16) * ratio));
                float scrollPct = maxScrollY > 0 ? (float) scrollY / (float) maxScrollY : 0;
                int thumbY = trackTop + (int) ((trackH - thumbH) * scrollPct);

                if (my >= thumbY && my <= thumbY + thumbH) {
                    isDraggingScrollbar = true;
                    dragStartMouseY = my;
                    dragStartScrollY = scrollY;
                    return true;
                } else if (my >= trackTop && my <= trackTop + trackH) {
                    scrollY = Mth.clamp(((my - trackTop) / (double) trackH) * maxScrollY, 0, maxScrollY);
                    return true;
                }
            }

            // 卡片
            if (modelEntries != null) {
                boolean hasScrollbar = maxScrollY > 20;
                int totalW = cols * cardWidth + (cols - 1) * COLUMN_GAP;
                int startX = hasScrollbar ? (width - UIConstants.SCROLLBAR_WIDTH - totalW) / 2
                                          : (width - totalW) / 2;
                int startY = top - (int) scrollY;

                for (int i = 0; i < modelEntries.size(); i++) {
                    int col = i % cols;
                    int row = i / cols;
                    int x = startX + col * (cardWidth + COLUMN_GAP);
                    int y = startY + row * (CARD_HEIGHT + ROW_GAP);

                    if (modelEntries.get(i).mouseClicked(mx, my, x, y, cardWidth, CARD_HEIGHT)) {
                        return true;
                    }
                }
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    /**
     * 鼠标释放事件。
     *
     * @param event 鼠标事件
     * @return 是否消费事件
     */
    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        final double mx = event.x();
        final double my = event.y();
        final int btn = event.button();
        if (btn == 0 && isDraggingScrollbar) {
            isDraggingScrollbar = false;
            return true;
        }
        return super.mouseReleased(event);
    }

    /**
     * 鼠标拖拽事件。
     *
     * @param event 鼠标事件
     * @param dx X轴偏移量
     * @param dy Y轴偏移量
     * @return 是否消费事件
     */
    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        final double mx = event.x();
        final double my = event.y();
        final int btn = event.button();
        if (isDraggingScrollbar) {
            handleScrollbarDragging((int) mx, (int) my);
            return true;
        }
        return super.mouseDragged(event, dx, dy);
    }

    /**
     * 按键按下事件。
     *
     * @param event 按键事件
     * @return 是否消费事件
     */
    @Override
    public boolean keyPressed(KeyEvent event) {
        final int keyCode = event.key();
        if (keyCode == 256) { onClose(); return true; }
        return super.keyPressed(event);
    }

    /**
     * 是否暂停游戏。
     *
     * @return false 不暂停
     */
    @Override
    public boolean isPauseScreen() { return false; }

    /** 选择模型并同步。 */
    private void selectModel(String modelId) {
        applySelectionLocally(modelId);
        sendSelectionToServer(modelId);
        closeAndPlaceBlock();
    }

    /** 关闭界面并放置方块。 */
    private void closeAndPlaceBlock() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            onClose();
            return;
        }

        ItemStack stack = mc.player.getItemInHand(hand);
        if (!CustomModelBlockItem.isCustomModelItem(stack)) {
            onClose();
            return;
        }

        onClose();

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

    /** 本地应用模型选择。 */
    private void applySelectionLocally(String modelId) {
        if (Minecraft.getInstance().player == null) return;
        if (targetType == TargetType.ITEM) {
            ItemStack stack = Minecraft.getInstance().player.getItemInHand(hand);
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

    /**
     * 判断点是否在矩形范围内。
     *
     * @param mx 点X坐标
     * @param my 点Y坐标
     * @param x 矩形左上X
     * @param y 矩形左上Y
     * @param w 矩形宽度
     * @param h 矩形高度
     * @return 是否在范围内
     */
    private static boolean inBounds(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    /**
     * 模型卡片条目。
     *
     * <p>单个模型的卡片展示，包含预览、名称与选择按钮。
     */
    class ModelEntry {
        private static final int BTN_W = 50;
        private static final int BTN_H = UIConstants.RETURN_BUTTON_HEIGHT;

        private final String modelId;
        private final String localizedName;
        private final Identifier modelIdentifier;

        /**
         * 构造模型卡片条目。
         *
         * @param modelId 模型ID
         * @param localizedName 本地化名称
         * @param modelIdentifier 模型标识符
         */
        ModelEntry(String modelId, String localizedName, Identifier modelIdentifier) {
            this.modelId = modelId;
            this.localizedName = localizedName;
            this.modelIdentifier = modelIdentifier;
        }

        /**
         * 渲染卡片。
         *
         * @param ctx 绘制上下文
         * @param x X坐标
         * @param y Y坐标
         * @param w 宽度
         * @param h 高度
         * @param mx 鼠标X
         * @param my 鼠标Y
         */
        public void render(GuiGraphicsExtractor ctx, int x, int y, int w, int h, int mx, int my) {
            boolean hover = inBounds(mx, my, x, y, w, h);

            // 卡片背景
            ctx.fill(x, y, x + w, y + h, UIConstants.COLOR_ITEM_BG);
            ctx.outline(x, y, w, h, hover ? UIConstants.COLOR_H2_TEXT : UIConstants.COLOR_ITEM_BORDER);

            // 模型预览区
            int innerL = x + CARD_INNER_PAD;
            int innerT = y + CARD_INNER_PAD;
            int innerR = x + w - CARD_INNER_PAD;
            int innerB = innerT + MODEL_ZONE_H;
            ctx.fill(innerL, innerT, innerR, innerB, UIConstants.COLOR_ITEM_BG);

            // 渲染模型
            renderModelInGui(ctx, x + w / 2, y + CARD_INNER_PAD + MODEL_ZONE_H / 2 + 4);

            // 模型名
            int nameTop = innerB + 4;
            int nameMaxW = w - CARD_INNER_PAD * 2;
            List<net.minecraft.util.FormattedCharSequence> lines =
                    font.split(Component.literal(localizedName), nameMaxW);
            int lineCount = Math.min(lines.size(), 2);
            for (int i = 0; i < lineCount; i++) {
                net.minecraft.util.FormattedCharSequence line = lines.get(i);
                int lx = x + (w - font.width(line)) / 2;
                ctx.text(font, line, lx, nameTop + i * (font.lineHeight + 1),
                        0xFFFFFFFF, false);
            }

            // 选择按钮
            int btnX = x + (w - BTN_W) / 2;
            int btnY = y + h - BTN_H - 4;
            boolean btnHover = inBounds(mx, my, btnX, btnY, BTN_W, BTN_H);
            int btnBg = btnHover ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG;
            ctx.fill(btnX, btnY, btnX + BTN_W, btnY + BTN_H, btnBg);
            ctx.outline(btnX, btnY, BTN_W, BTN_H, UIConstants.COLOR_BTN_BORDER);

            Component selLabel = Component.translatable("screen.ocelotsignmod.model_selection.select");
            ctx.text(font, selLabel,
                    btnX + (BTN_W - font.width(selLabel)) / 2,
                    btnY + (BTN_H - font.lineHeight) / 2,
                    0xFF000000, false);
        }

        /**
         * 鼠标点击处理。
         *
         * @param mx 鼠标X
         * @param my 鼠标Y
         * @param x 卡片X
         * @param y 卡片Y
         * @param w 卡片宽度
         * @param h 卡片高度
         * @return 是否消费事件
         */
        public boolean mouseClicked(double mx, double my, int x, int y, int w, int h) {
            int btnX = x + (w - BTN_W) / 2;
            int btnY = y + h - BTN_H - 4;
            if (inBounds(mx, my, btnX, btnY, BTN_W, BTN_H)) {
                selectModel(this.modelId);
                return true;
            }
            return false;
        }

        /**
         * 在GUI中渲染模型预览。
         *
         * <p>26.1 的 GUI 改为渲染状态提取管线，{@code GuiGraphicsExtractor} 只能提交
         * 物品与实体，无法再把任意独立方块模型直接画进界面，因此这里改为绘制
         * 自定义模型方块的物品图标作为预览占位。</p>
         */
        private void renderModelInGui(GuiGraphicsExtractor ctx, int cx, int cy) {
            if (ModelRegistryManager.getBakedModel(modelIdentifier) == null) {
                return;
            }
            ItemStack icon = new ItemStack(OcelotSignMod.CUSTOM_MODEL_BLOCK);
            ctx.item(icon, cx - 8, cy - 8);
        }
    }
}
