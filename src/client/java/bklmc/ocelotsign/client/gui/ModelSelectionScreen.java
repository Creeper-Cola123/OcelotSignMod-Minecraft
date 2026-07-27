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
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 模型选择界面 (1.19.2 兼容版)
 */
public class ModelSelectionScreen extends Screen {

    public enum TargetType { ITEM, BLOCK }

    private static final int HEADER_HEIGHT = UIConstants.HEADER_HEIGHT;
    private static final int COLUMN_GAP = 14;
    private static final int ROW_GAP = 14;
    private static final int CARD_HEIGHT = 110;
    private static final int CARD_INNER_PAD = 6;
    private static final int MODEL_ZONE_H = 58;
    private static final int SCROLL_AMOUNT = UIConstants.SCROLL_AMOUNT;
    private static final int CONTENT_TOP_PAD = 16;
    private static final int MIN_COLS = 2;
    private static final int MAX_COLS = 10;
    private static final int CARD_MIN_WIDTH = 100;

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

    public ModelSelectionScreen(Hand hand) {
        super(Text.translatable("screen.ocelotsignmod.model_selection.title"));
        this.hand = hand;
        this.targetType = TargetType.ITEM;
        this.blockPos = null;
    }

    public ModelSelectionScreen(TargetType targetType, BlockPos blockPos) {
        super(Text.translatable("screen.ocelotsignmod.model_selection.title"));
        this.hand = Hand.MAIN_HAND;
        this.targetType = targetType;
        this.blockPos = blockPos;
    }

    @Override
    protected void init() {
        super.init();
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

        ScreenMouseEvents.allowMouseScroll(this).register((screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
            if (maxScrollY > 0) {
                scrollY = MathHelper.clamp(scrollY - verticalAmount * SCROLL_AMOUNT, 0, maxScrollY);
            }
            return maxScrollY <= 0;
        });
    }

    private void resetScroll() {
        scrollY = 0;
        int contentTop = HEADER_HEIGHT + CONTENT_TOP_PAD;
        int contentBottom = height - UIConstants.RETURN_BUTTON_HEIGHT - 10 - 4;
        int contentAreaH = contentBottom - contentTop;
        calculateScrollBounds(contentAreaH);
        scrollY = MathHelper.clamp(scrollY, 0, maxScrollY);
    }

    private void calculateLayout() {
        int availW = width - UIConstants.SCROLLBAR_WIDTH - COLUMN_GAP - 20;
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

    private void calculateScrollBounds(int contentAreaH) {
        maxScrollY = Math.max(0, contentHeight - contentAreaH);
    }

    @Override
    public void render(MatrixStack matrices, int mx, int my, float delta) {
        DrawableHelper.fill(matrices, 0, 0, width, height, UIConstants.COLOR_MAIN_BG);
        DrawableHelper.fill(matrices, 0, 0, width, HEADER_HEIGHT, UIConstants.COLOR_MAIN_HEADER);
        drawBorder(matrices, 0, HEADER_HEIGHT - 1, width, 1, UIConstants.COLOR_MAIN_BORDER);

        renderHeader(matrices, mx, my);
        renderFooter(matrices, mx, my);

        if (hasNoModels) {
            renderEmptyState(matrices);
        } else {
            calculateLayout();
            handleScrollbarDragging(mx, my);
            renderContent(matrices, mx, my);
            renderScrollbar(matrices, mx, my);
        }

        super.render(matrices, mx, my, delta);
    }

    private void renderHeader(MatrixStack matrices, int mx, int my) {
        int titleW = textRenderer.getWidth(title);
        textRenderer.draw(matrices, title, (width - titleW) / 2, (HEADER_HEIGHT - textRenderer.fontHeight) / 2, 0xFFFFFFFF);
    }

    private void renderFooter(MatrixStack matrices, int mx, int my) {
        backBtnHovered = inBounds(mx, my, backBtnX, backBtnY, UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT);

        int btnBg = backBtnHovered ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG;
        DrawableHelper.fill(matrices, backBtnX, backBtnY, backBtnX + UIConstants.RETURN_BUTTON_WIDTH, backBtnY + UIConstants.RETURN_BUTTON_HEIGHT, btnBg);
        drawBorder(matrices, backBtnX, backBtnY, UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT, UIConstants.COLOR_BTN_BORDER);

        Text backTxt = Text.translatable("screen.ocelotsignmod.model_selection.back");
        textRenderer.draw(matrices, backTxt,
                backBtnX + (UIConstants.RETURN_BUTTON_WIDTH - textRenderer.getWidth(backTxt)) / 2,
                backBtnY + (UIConstants.RETURN_BUTTON_HEIGHT - textRenderer.fontHeight) / 2,
                UIConstants.COLOR_BTN_TEXT);
    }

    private void renderEmptyState(MatrixStack matrices) {
        int cx = width / 2;
        int cy = height / 2;
        drawEmptyIcon(matrices, cx, cy - 64);

        Text head = Text.translatable("screen.ocelotsignmod.model_selection.no_models");
        textRenderer.draw(matrices, head, cx - textRenderer.getWidth(head) / 2, cy, UIConstants.COLOR_SECTION_TITLE);

        Text sub = Text.translatable("screen.ocelotsignmod.model_selection.no_models_hint");
        textRenderer.draw(matrices, sub, cx - textRenderer.getWidth(sub) / 2, cy + 22, UIConstants.COLOR_DESC_TEXT);
    }

    private void drawEmptyIcon(MatrixStack matrices, int cx, int cy) {
        int size = 34;
        DrawableHelper.fill(matrices, cx - size, cy + 4, cx, cy + size + 4, UIConstants.COLOR_ITEM_BG);
        drawBorder(matrices, cx - size, cy + 4, size, size, UIConstants.COLOR_ITEM_BORDER);
        DrawableHelper.fill(matrices, cx - size + 7, cy - 3, cx + 7, cy + size + 3, UIConstants.COLOR_ITEM_BG);
        drawBorder(matrices, cx - size + 7, cy - 3, size, size, UIConstants.COLOR_ITEM_BORDER);
        DrawableHelper.fill(matrices, cx - size + 14, cy - 10, cx + 14, cy + size - 10, UIConstants.COLOR_ITEM_BG);
        drawBorder(matrices, cx - size + 14, cy - 10, size, size, UIConstants.COLOR_ITEM_BORDER);

        Text q = Text.literal("?");
        int qw = textRenderer.getWidth(q);
        textRenderer.draw(matrices, q, cx - qw / 2, cy - textRenderer.fontHeight / 2, UIConstants.COLOR_H3_TEXT);
    }

    private void renderContent(MatrixStack matrices, int mx, int my) {
        int top = HEADER_HEIGHT + CONTENT_TOP_PAD;
        boolean hasScrollbar = maxScrollY > 20;
        int rightEdge = hasScrollbar ? width - UIConstants.SCROLLBAR_WIDTH : width;
        myEnableScissor(0, top, rightEdge, height);

        int totalW = cols * cardWidth + (cols - 1) * COLUMN_GAP;
        int startX = hasScrollbar ? (width - UIConstants.SCROLLBAR_WIDTH - totalW) / 2 : (width - totalW) / 2;
        int startY = top - (int) scrollY;

        if (modelEntries != null) {
            for (int i = 0; i < modelEntries.size(); i++) {
                int col = i % cols;
                int row = i / cols;
                int x = startX + col * (cardWidth + COLUMN_GAP);
                int y = startY + row * (CARD_HEIGHT + ROW_GAP);

                if (y + CARD_HEIGHT < top || y > height) continue;
                modelEntries.get(i).render(matrices, x, y, cardWidth, CARD_HEIGHT, mx, my);
            }
        }

        myDisableScissor();
    }

    private void renderScrollbar(MatrixStack matrices, int mx, int my) {
        int top = HEADER_HEIGHT + CONTENT_TOP_PAD;
        int bottom = height - UIConstants.RETURN_BUTTON_HEIGHT - 10 - 4;
        int winH = bottom - top;
        if (maxScrollY <= 20) return;

        int sx = width - UIConstants.SCROLLBAR_WIDTH - 3;
        int trackTop = top + 8;
        int trackH = winH - 16;

        DrawableHelper.fill(matrices, sx, trackTop, sx + UIConstants.SCROLLBAR_WIDTH, trackTop + trackH, UIConstants.COLOR_SCROLLBAR_TRACK);

        float ratio = (float) winH / (float) (winH + maxScrollY);
        int thumbH = Math.max(UIConstants.SCROLLBAR_MIN_HEIGHT, (int) (trackH * ratio));
        float scrollPct = maxScrollY > 0 ? (float) scrollY / (float) maxScrollY : 0;
        int thumbY = trackTop + (int) ((trackH - thumbH) * scrollPct);

        boolean hover = mx >= sx && mx <= sx + UIConstants.SCROLLBAR_WIDTH && my >= thumbY && my <= thumbY + thumbH;
        int thumbC = hover ? UIConstants.COLOR_SCROLLBAR_THUMB_HOVER : UIConstants.COLOR_SCROLLBAR_THUMB;
        DrawableHelper.fill(matrices, sx, thumbY, sx + UIConstants.SCROLLBAR_WIDTH, thumbY + thumbH, thumbC);
        drawBorder(matrices, sx, thumbY, UIConstants.SCROLLBAR_WIDTH, thumbH, UIConstants.COLOR_ITEM_BORDER);
    }

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

    @Override
    public boolean mouseClicked(double mx, double my, int btn) {
        if (btn == 0) {
            if (inBounds(mx, my, backBtnX, backBtnY, UIConstants.RETURN_BUTTON_WIDTH, UIConstants.RETURN_BUTTON_HEIGHT)) {
                close();
                return true;
            }

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

            if (modelEntries != null) {
                boolean hasScrollbar = maxScrollY > 20;
                int totalW = cols * cardWidth + (cols - 1) * COLUMN_GAP;
                int startX = hasScrollbar ? (width - UIConstants.SCROLLBAR_WIDTH - totalW) / 2 : (width - totalW) / 2;
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

    @Override
    public boolean mouseReleased(double mx, double my, int btn) {
        if (btn == 0 && isDraggingScrollbar) { isDraggingScrollbar = false; return true; }
        return super.mouseReleased(mx, my, btn);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int btn, double dx, double dy) {
        if (isDraggingScrollbar) { handleScrollbarDragging((int) mx, (int) my); return true; }
        return super.mouseDragged(mx, my, btn, dx, dy);
    }

    @Override
    public boolean keyPressed(int keyCode, int sc, int mods) {
        if (keyCode == 256) { close(); return true; }
        return super.keyPressed(keyCode, sc, mods);
    }

    @Override
    public boolean shouldPause() { return false; }

    private void selectModel(String modelId) {
        applySelectionLocally(modelId);
        sendSelectionToServer(modelId);
        closeAndPlaceBlock();
    }

    private void closeAndPlaceBlock() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) { close(); return; }

        ItemStack stack = mc.player.getStackInHand(hand);
        if (!CustomModelBlockItem.isCustomModelItem(stack)) { close(); return; }

        close();

        Direction face = Direction.NORTH;
        HitResult hit = mc.crosshairTarget;
        if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hit;
            net.minecraft.item.ItemPlacementContext ctx = new net.minecraft.item.ItemPlacementContext(mc.player, hand, stack, blockHit);
            stack.useOnBlock(ctx);
        }
    }

    private void applySelectionLocally(String modelId) {
        if (client == null || client.player == null) return;
        if (targetType == TargetType.ITEM) {
            ItemStack stack = client.player.getStackInHand(hand);
            if (CustomModelBlockItem.isCustomModelItem(stack)) {
                stack.getOrCreateNbt().putString(CustomModelBlockItem.SELECTED_MODEL_ID_KEY, modelId);
            }
        }
    }

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

    // ==================== 1.19.2 兼容 ====================

    /** 重命名避免与 DrawableHelper.enableScissor 冲突 */
    private static void myEnableScissor(int x, int y, int width, int height) {
        MinecraftClient client = MinecraftClient.getInstance();
        double scale = client.getWindow().getScaleFactor();
        int windowHeight = client.getWindow().getFramebufferHeight();
        org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);
        org.lwjgl.opengl.GL11.glScissor((int)(x*scale), (int)(windowHeight-(y+height)*scale), (int)(width*scale), (int)(height*scale));
    }

    /** 重命名避免与 DrawableHelper.disableScissor 冲突 */
    private static void myDisableScissor() { org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST); }

    private static void drawBorder(MatrixStack matrices, int x, int y, int w, int h, int color) {
        DrawableHelper.fill(matrices, x, y, x + w, y + 1, color);
        DrawableHelper.fill(matrices, x, y + h - 1, x + w, y + h, color);
        DrawableHelper.fill(matrices, x, y, x + 1, y + h, color);
        DrawableHelper.fill(matrices, x + w - 1, y, x + w, y + h, color);
    }

    // ==================== 内部类 ModelEntry ====================

    class ModelEntry {
        private static final int BTN_W = 50;
        private static final int BTN_H = UIConstants.RETURN_BUTTON_HEIGHT;

        private final String modelId;
        private final String localizedName;
        private final Identifier modelIdentifier;

        ModelEntry(String modelId, String localizedName, Identifier modelIdentifier) {
            this.modelId = modelId;
            this.localizedName = localizedName;
            this.modelIdentifier = modelIdentifier;
        }

        public void render(MatrixStack matrices, int x, int y, int w, int h, int mx, int my) {
            boolean hover = inBounds(mx, my, x, y, w, h);

            DrawableHelper.fill(matrices, x, y, x + w, y + h, UIConstants.COLOR_ITEM_BG);
            drawBorder(matrices, x, y, w, h, hover ? UIConstants.COLOR_H2_TEXT : UIConstants.COLOR_ITEM_BORDER);

            int innerL = x + CARD_INNER_PAD;
            int innerT = y + CARD_INNER_PAD;
            int innerR = x + w - CARD_INNER_PAD;
            int innerB = innerT + MODEL_ZONE_H;
            DrawableHelper.fill(matrices, innerL, innerT, innerR, innerB, UIConstants.COLOR_ITEM_BG);

            renderModelInGui(matrices, x + w / 2, y + CARD_INNER_PAD + MODEL_ZONE_H / 2 + 4);

            int nameTop = innerB + 4;
            int nameMaxW = w - CARD_INNER_PAD * 2;
            List<OrderedText> lines = textRenderer.wrapLines(Text.literal(localizedName), nameMaxW);
            int lineCount = Math.min(lines.size(), 2);
            for (int i = 0; i < lineCount; i++) {
                OrderedText line = lines.get(i);
                int lx = x + (w - textRenderer.getWidth(line)) / 2;
                textRenderer.draw(matrices, line, lx, nameTop + i * (textRenderer.fontHeight + 1), 0xFFFFFFFF);
            }

            int btnX = x + (w - BTN_W) / 2;
            int btnY = y + h - BTN_H - 4;
            boolean btnHover = inBounds(mx, my, btnX, btnY, BTN_W, BTN_H);
            int btnBg = btnHover ? UIConstants.COLOR_BTN_BG_HOVER : UIConstants.COLOR_BTN_BG;
            DrawableHelper.fill(matrices, btnX, btnY, btnX + BTN_W, btnY + BTN_H, btnBg);
            drawBorder(matrices, btnX, btnY, BTN_W, BTN_H, UIConstants.COLOR_BTN_BORDER);

            Text selLabel = Text.translatable("screen.ocelotsignmod.model_selection.select");
            textRenderer.draw(matrices, selLabel, btnX + (BTN_W - textRenderer.getWidth(selLabel)) / 2, btnY + (BTN_H - textRenderer.fontHeight) / 2, 0xFF000000);
        }

        public boolean mouseClicked(double mx, double my, int x, int y, int w, int h) {
            int btnX = x + (w - BTN_W) / 2;
            int btnY = y + h - BTN_H - 4;
            if (inBounds(mx, my, btnX, btnY, BTN_W, BTN_H)) { selectModel(this.modelId); return true; }
            return false;
        }

        private void renderModelInGui(MatrixStack matrices, int cx, int cy) {
            MinecraftClient mc = MinecraftClient.getInstance();
            // 1.19.2: getModel 接受 Identifier
            BakedModel model = mc.getBakedModelManager().getModel(new net.minecraft.client.util.ModelIdentifier(modelIdentifier, ""));            if (model == null || model == mc.getBakedModelManager().getMissingModel()) return;

            matrices.push();
            matrices.translate(cx, cy, 150.0F);
            matrices.scale(22.0F, -22.0F, 22.0F);
            // 1.19.2: 使用 Quaternion 和 Vec3f 替代 RotationAxis
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(+30.0F));
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(45.0F));
            matrices.translate(-0.5F, +0.5F, -0.5F);

            RenderSystem.runAsFancy(() -> {
                DiffuseLighting.disableGuiDepthLighting();
                VertexConsumer vc = mc.getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getCutout());
                mc.getBlockRenderManager().getModelRenderer().render(
                        mc.world, model, Blocks.AIR.getDefaultState(),
                        mc.player.getBlockPos(), matrices, vc,
                        false, mc.world.random, 42L, OverlayTexture.DEFAULT_UV);
                mc.getBufferBuilders().getEntityVertexConsumers().draw();
                DiffuseLighting.enableGuiDepthLighting();
            });

            matrices.pop();
        }
    }
}