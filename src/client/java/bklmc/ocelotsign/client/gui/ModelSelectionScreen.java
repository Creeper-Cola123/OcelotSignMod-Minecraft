package bklmc.ocelotsign.client.gui;

import bklmc.ocelotsign.client.UIConstants;
import bklmc.ocelotsign.client.model.ModelRegistryManager;
import bklmc.ocelotsign.item.CustomModelBlockItem;
import bklmc.ocelotsign.platform.ModIdentifiers;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.MathHelper;

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

    private int backBtnX = 0, backBtnY = 0;
    private boolean backBtnHovered = false;
    private int cardWidth = CARD_MIN_WIDTH;
    private int cols = MIN_COLS;

    /**
     * 构造手持物品模式的模型选择界面。
     *
     * @param hand 玩家手持物品的手
     */
    public ModelSelectionScreen(Hand hand) {
        super(Text.translatable("screen.ocelotsignmod.model_selection.title"));
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
        super(Text.translatable("screen.ocelotsignmod.model_selection.title"));
        this.hand = Hand.MAIN_HAND;
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
                scrollY = MathHelper.clamp(scrollY - verticalAmount * SCROLL_AMOUNT, 0, maxScrollY);
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
        scrollY = MathHelper.clamp(scrollY, 0, maxScrollY);
    }

    /** 计算布局参数。 */
    private void calculateLayout() {
        int availW = width - UIConstants.SCROLLBAR_WIDTH - COLUMN_GAP - 20;

        // 动态计算列数与卡片宽度
        this.cols = MathHelper.clamp((availW + COLUMN_GAP) / (CARD_MIN_WIDTH + COLUMN_GAP), MIN_COLS, MAX_COLS);
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
        scrollY = MathHelper.clamp(scrollY, 0, maxScrollY);
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
    public void render(DrawContext ctx, int mx, int my, float delta) {
        // 背景
        ctx.fill(0, 0, width, height, UIConstants.COLOR_MAIN_BG);
        // 标题栏
        ctx.fill(0, 0, width, HEADER_HEIGHT, UIConstants.COLOR_MAIN_HEADER);
        // 标题栏底边
        ctx.drawBorder(0, HEADER_HEIGHT - 1, width, 1, UIConstants.COLOR_MAIN_BORDER);

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

        super.render(ctx, mx, my, delta);
    }

    /** 渲染标题栏。 */
    private void renderHeader(DrawContext ctx, int mx, int my) {
        // 居中白色标题
        int titleW = textRenderer.getWidth(title);
        ctx.drawText(textRenderer, title,
                (width - titleW) / 2,
                (HEADER_HEIGHT - textRenderer.fontHeight) / 2,
                0xFFFFFFFF, false);
    }

    /** 渲染底部栏。 */
    private void renderFooter(DrawContext ctx, int mx, int my) {
        backBtnHovered = inBounds(mx, my, backBtnX, backBtnY,
                UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT);

        int btnBg = backBtnHovered ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG;
        ctx.fill(backBtnX, backBtnY,
                backBtnX + UIConstants.RETURN_BUTTON_WIDTH,
                backBtnY + UIConstants.RETURN_BUTTON_HEIGHT,
                btnBg);
        ctx.drawBorder(backBtnX, backBtnY,
                UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT,
                UIConstants.COLOR_BTN_BORDER);

        Text backTxt = Text.translatable("screen.ocelotsignmod.model_selection.back");
        ctx.drawText(textRenderer, backTxt,
                backBtnX + (UIConstants.RETURN_BUTTON_WIDTH - textRenderer.getWidth(backTxt)) / 2,
                backBtnY + (UIConstants.RETURN_BUTTON_HEIGHT - textRenderer.fontHeight) / 2,
                UIConstants.COLOR_BTN_TEXT, false);
    }

    /** 渲染空状态。 */
    private void renderEmptyState(DrawContext ctx) {
        int cx = width / 2;
        int cy = height / 2;

        drawEmptyIcon(ctx, cx, cy - 64);

        // 主提示
        Text head = Text.translatable("screen.ocelotsignmod.model_selection.no_models");
        ctx.drawText(textRenderer, head, cx - textRenderer.getWidth(head) / 2, cy, UIConstants.COLOR_SECTION_TITLE, false);

        // 副提示
        Text sub = Text.translatable("screen.ocelotsignmod.model_selection.no_models_hint");
        ctx.drawText(textRenderer, sub, cx - textRenderer.getWidth(sub) / 2, cy + 22, UIConstants.COLOR_DESC_TEXT, false);
    }

    /** 绘制空状态图标。 */
    private void drawEmptyIcon(DrawContext ctx, int cx, int cy) {
        int size = 34;
        // 三层叠块
        ctx.fill(cx - size, cy + 4, cx, cy + size + 4, UIConstants.COLOR_ITEM_BG);
        ctx.drawBorder(cx - size, cy + 4, size, size, UIConstants.COLOR_ITEM_BORDER);
        ctx.fill(cx - size + 7, cy - 3, cx + 7, cy + size + 3, UIConstants.COLOR_ITEM_BG);
        ctx.drawBorder(cx - size + 7, cy - 3, size, size, UIConstants.COLOR_ITEM_BORDER);
        ctx.fill(cx - size + 14, cy - 10, cx + 14, cy + size - 10, UIConstants.COLOR_ITEM_BG);
        ctx.drawBorder(cx - size + 14, cy - 10, size, size, UIConstants.COLOR_ITEM_BORDER);

        // 问号
        Text q = Text.literal("?");
        int qw = textRenderer.getWidth(q);
        ctx.drawText(textRenderer, q, cx - qw / 2, cy - textRenderer.fontHeight / 2, UIConstants.COLOR_H3_TEXT, false);
    }

    /** 渲染卡片网格。 */
    private void renderContent(DrawContext ctx, int mx, int my) {
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
    private void renderScrollbar(DrawContext ctx, int mx, int my) {
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
        ctx.drawBorder(sx, thumbY, UIConstants.SCROLLBAR_WIDTH, thumbH, UIConstants.COLOR_ITEM_BORDER);
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
            scrollY = MathHelper.clamp(dragStartScrollY + d, 0, maxScrollY);
        }
    }

    /**
     * 鼠标点击事件。
     *
     * @param mx 鼠标X坐标
     * @param my 鼠标Y坐标
     * @param btn 鼠标按钮
     * @return 是否消费事件
     */
    @Override
    public boolean mouseClicked(double mx, double my, int btn) {
        if (btn == 0) {
            // 返回按钮
            if (inBounds(mx, my, backBtnX, backBtnY,
                    UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT)) {
                close();
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
                    scrollY = MathHelper.clamp(((my - trackTop) / (double) trackH) * maxScrollY, 0, maxScrollY);
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
        return super.mouseClicked(mx, my, btn);
    }

    /**
     * 鼠标释放事件。
     *
     * @param mx 鼠标X坐标
     * @param my 鼠标Y坐标
     * @param btn 鼠标按钮
     * @return 是否消费事件
     */
    @Override
    public boolean mouseReleased(double mx, double my, int btn) {
        if (btn == 0 && isDraggingScrollbar) {
            isDraggingScrollbar = false;
            return true;
        }
        return super.mouseReleased(mx, my, btn);
    }

    /**
     * 鼠标拖拽事件。
     *
     * @param mx 鼠标X坐标
     * @param my 鼠标Y坐标
     * @param btn 鼠标按钮
     * @param dx X轴偏移量
     * @param dy Y轴偏移量
     * @return 是否消费事件
     */
    @Override
    public boolean mouseDragged(double mx, double my, int btn, double dx, double dy) {
        if (isDraggingScrollbar) {
            handleScrollbarDragging((int) mx, (int) my);
            return true;
        }
        return super.mouseDragged(mx, my, btn, dx, dy);
    }

    /**
     * 按键按下事件。
     *
     * @param keyCode 键码
     * @param sc 扫描码
     * @param mods 修饰键
     * @return 是否消费事件
     */
    @Override
    public boolean keyPressed(int keyCode, int sc, int mods) {
        if (keyCode == 256) { close(); return true; }
        return super.keyPressed(keyCode, sc, mods);
    }

    /**
     * 是否暂停游戏。
     *
     * @return false 不暂停
     */
    @Override
    public boolean shouldPause() { return false; }

    /** 选择模型并同步。 */
    private void selectModel(String modelId) {
        applySelectionLocally(modelId);
        sendSelectionToServer(modelId);
        closeAndPlaceBlock();
    }

    /** 关闭界面并放置方块。 */
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

    /** 本地应用模型选择。 */
    private void applySelectionLocally(String modelId) {
        if (client == null || client.player == null) return;
        if (targetType == TargetType.ITEM) {
            ItemStack stack = client.player.getStackInHand(hand);
            if (CustomModelBlockItem.isCustomModelItem(stack)) {
                stack.getOrCreateNbt().putString(CustomModelBlockItem.SELECTED_MODEL_ID_KEY, modelId);
            }
        }
    }

    /** 发送模型选择到服务器。 */
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
        public void render(DrawContext ctx, int x, int y, int w, int h, int mx, int my) {
            boolean hover = inBounds(mx, my, x, y, w, h);

            // 卡片背景
            ctx.fill(x, y, x + w, y + h, UIConstants.COLOR_ITEM_BG);
            ctx.drawBorder(x, y, w, h, hover ? UIConstants.COLOR_H2_TEXT : UIConstants.COLOR_ITEM_BORDER);

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
            List<net.minecraft.text.OrderedText> lines =
                    textRenderer.wrapLines(net.minecraft.text.Text.literal(localizedName), nameMaxW);
            int lineCount = Math.min(lines.size(), 2);
            for (int i = 0; i < lineCount; i++) {
                net.minecraft.text.OrderedText line = lines.get(i);
                int lx = x + (w - textRenderer.getWidth(line)) / 2;
                ctx.drawText(textRenderer, line, lx, nameTop + i * (textRenderer.fontHeight + 1),
                        0xFFFFFFFF, false);
            }

            // 选择按钮
            int btnX = x + (w - BTN_W) / 2;
            int btnY = y + h - BTN_H - 4;
            boolean btnHover = inBounds(mx, my, btnX, btnY, BTN_W, BTN_H);
            int btnBg = btnHover ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG;
            ctx.fill(btnX, btnY, btnX + BTN_W, btnY + BTN_H, btnBg);
            ctx.drawBorder(btnX, btnY, BTN_W, BTN_H, UIConstants.COLOR_BTN_BORDER);

            Text selLabel = Text.translatable("screen.ocelotsignmod.model_selection.select");
            ctx.drawText(textRenderer, selLabel,
                    btnX + (BTN_W - textRenderer.getWidth(selLabel)) / 2,
                    btnY + (BTN_H - textRenderer.fontHeight) / 2,
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

        /** 在GUI中渲染模型预览。 */
        private void renderModelInGui(DrawContext ctx, int cx, int cy) {
            MinecraftClient mc = MinecraftClient.getInstance();
            BakedModel model = mc.getBakedModelManager().getModel(modelIdentifier);
            if (model == null || model == mc.getBakedModelManager().getMissingModel()) return;

            MatrixStack ms = ctx.getMatrices();
            ms.push();
            ms.translate(cx, cy, 150.0F);
            ms.scale(22.0F, -22.0F, 22.0F);
            ms.multiply(RotationAxis.POSITIVE_X.rotationDegrees(+30.0F));
            ms.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45.0F));
            ms.translate(-0.5F, +0.5F, -0.5F);

            RenderSystem.runAsFancy(() -> {
                DiffuseLighting.disableGuiDepthLighting();
                VertexConsumer vc = mc.getBufferBuilders().getEntityVertexConsumers()
                        .getBuffer(RenderLayer.getCutout());
                mc.getBlockRenderManager().getModelRenderer().render(
                        mc.world, model, Blocks.AIR.getDefaultState(),
                        mc.player.getBlockPos(), ms, vc,
                        false, mc.world.random, 42L, OverlayTexture.DEFAULT_UV);
                mc.getBufferBuilders().getEntityVertexConsumers().draw();
                DiffuseLighting.enableGuiDepthLighting();
            });

            ms.pop();
        }
    }
}
