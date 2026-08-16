package bklmc.ocelotsign;

import bklmc.ocelotsign.block.ArrowBlocks;
import bklmc.ocelotsign.block.ArrowBlocksLarge;
import bklmc.ocelotsign.block.ArrowBlocksStyle2;
import bklmc.ocelotsign.block.ArrowBlocksStyle3;
import bklmc.ocelotsign.block.custom.CustomModelBlock;
import bklmc.ocelotsign.blockentity.ModBlockEntities;
import bklmc.ocelotsign.client.PatternAndFontBlankScreen;
import bklmc.ocelotsign.client.PatternAndFontOverlay;
import bklmc.ocelotsign.client.PatternRegistry;
import bklmc.ocelotsign.client.gui.ModelSelectionOverlay;
import bklmc.ocelotsign.client.gui.ModelSelectionOverlay.TargetType;
import bklmc.ocelotsign.client.model.ModelRegistryManager;
import bklmc.ocelotsign.client.render.CustomModelBER;
import bklmc.ocelotsign.item.CustomModelBlockItem;
import bklmc.ocelotsign.item.ModelWandItem;
import bklmc.ocelotsign.platform.ClientNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;

/**
 * OcelotSignMod 客户端入口类
 *
 * @see ClientModInitializer
 * @see PatternAndFontOverlay
 * @see CustomModelBlock
 */
@Environment(EnvType.CLIENT)
public class OcelotSignModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(ModBlockEntities.ROAD_SIGN_BLOCK_ENTITY, RoadSignBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.WALL_ROAD_SIGN_BLOCK_ENTITY, WallRoadSignBlockEntityRenderer::new);

        ModelRegistryManager.registerLoader();
        BlockEntityRendererRegistry.register(OcelotSignMod.CUSTOM_MODEL_BLOCK_ENTITY, CustomModelBER::new);
        BlockRenderLayerMap.INSTANCE.putBlock(OcelotSignMod.CUSTOM_MODEL_BLOCK, RenderLayer.getCutout());

        // 注册资源包刷新监听器
        PatternRegistry.registerReloadListener();

        registerCustomModelNetworking();
        registerCustomModelItemUseHandler();
        registerCustomModelBlockUseHandler();
        registerBlockLayers();
        registerOverlayScreenEvents();
        registerOverlayInputTick();
    }

    /**
     * 注册叠加层输入轮询：每客户端 tick 检查 {@code client.getOverlay()}，
     * 如果是 {@link ModelSelectionOverlay} 就调用其 {@code tickInput()} 进行鼠标/键盘派发。
     */
    private void registerOverlayInputTick() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client != null && client.getOverlay() instanceof ModelSelectionOverlay overlay) {
                overlay.tickInput();
            }
        });
    }

    /**
     * 注册图案与字体浮层的屏幕事件监听。
     *
     * <p>拦截屏幕的渲染、鼠标点击/释放/滚动、键盘按键事件，
     * 当浮层可见时优先处理浮层交互。
     */
    private void registerOverlayScreenEvents() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            ScreenEvents.afterRender(screen).register((screen1, context, mouseX, mouseY, tickDelta) -> {
                if (PatternAndFontOverlay.isVisible) {
                    screen1.setFocused(null);
                    PatternAndFontOverlay.render(context, mouseX, mouseY);
                }
            });

            ScreenMouseEvents.allowMouseClick(screen).register((screen1, mouseX, mouseY, button) -> {
                if (PatternAndFontOverlay.isVisible) {
                    if (PatternAndFontOverlay.mouseClicked(mouseX, mouseY, button)) {
                        screen1.setDragging(true);
                    }

                    if (!PatternAndFontOverlay.isVisible && screen1 instanceof PatternAndFontBlankScreen) {
                        screen1.close();
                    }
                    return false;
                }
                return true;
            });

            ScreenMouseEvents.allowMouseRelease(screen).register((screen1, mouseX, mouseY, button) -> {
                if (PatternAndFontOverlay.isVisible) {
                    PatternAndFontOverlay.mouseReleased(mouseX, mouseY, button);
                    screen1.setDragging(false);
                    return false;
                }
                return true;
            });

            ScreenMouseEvents.allowMouseScroll(screen).register((screen1, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
                // 处理 PatternAndFontOverlay 的滚轮
                if (PatternAndFontOverlay.isVisible) {
                    PatternAndFontOverlay.mouseScrolled(mouseX, mouseY, verticalAmount);
                    return false;
                }
                // 处理 ModelSelectionOverlay 的滚轮
                if (ModelSelectionOverlay.isVisible()) {
                    ModelSelectionOverlay overlay = ModelSelectionOverlay.getCurrentInstance();
                    if (overlay != null) {
                        overlay.handleMouseScrolled(verticalAmount);
                    }
                    return false;
                }
                return true;
            });

            ScreenKeyboardEvents.allowKeyPress(screen).register((screen1, key, scancode, modifiers) -> {
                if (PatternAndFontOverlay.isVisible) {
                    if (key == GLFW.GLFW_KEY_ESCAPE) {
                        PatternAndFontOverlay.isVisible = false;
                        if (screen1 instanceof PatternAndFontBlankScreen) {
                            screen1.close();
                        }
                    }
                    return false;
                }
                return true;
            });

            ScreenKeyboardEvents.allowKeyRelease(screen).register((screen1, key, scancode, modifiers) -> {
                return !PatternAndFontOverlay.isVisible;
            });
        });
    }

    /**
     * 注册所有箭头方块的渲染层为 Cutout。
     */
    private static void registerBlockLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ADVANCE_ARROW_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ADVANCE_ARROW_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ADVANCE_ARROW_LEFT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ADVANCE_ARROW_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ADVANCE_ARROW_RIGHT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ADVANCE_ARROW_STRAIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ADVANCE_ARROW_STRAIGHT_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ADVANCE_ARROW_STRAIGHT_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ADVANCE_ARROW_STRAIGHT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ADVANCE_ARROW_STRAIGHT_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ADVANCE_ARROW_STRAIGHT_UTURN_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ADVANCE_ARROW_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ADVANCE_ARROW_UTURN_RIGHT, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ORANGE_ARROW_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ORANGE_ARROW_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ORANGE_ARROW_LEFT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ORANGE_ARROW_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ORANGE_ARROW_RIGHT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ORANGE_ARROW_STRAIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ORANGE_ARROW_STRAIGHT_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ORANGE_ARROW_STRAIGHT_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ORANGE_ARROW_STRAIGHT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ORANGE_ARROW_STRAIGHT_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ORANGE_ARROW_STRAIGHT_UTURN_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ORANGE_ARROW_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ORANGE_ARROW_UTURN_RIGHT, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.ARROW_PROHIBITED, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.PROHIBITED_ARROW_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.PROHIBITED_ARROW_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.PROHIBITED_ARROW_LEFT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.PROHIBITED_ARROW_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.PROHIBITED_ARROW_RIGHT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.PROHIBITED_ARROW_STRAIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.PROHIBITED_ARROW_STRAIGHT_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.PROHIBITED_ARROW_STRAIGHT_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.PROHIBITED_ARROW_STRAIGHT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.PROHIBITED_ARROW_STRAIGHT_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.PROHIBITED_ARROW_STRAIGHT_UTURN_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.PROHIBITED_ARROW_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.PROHIBITED_ARROW_UTURN_RIGHT, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.SPEED_BUMP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.YIELD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocks.DISTANCE_CONFIRM, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ADVANCE_ARROW_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ADVANCE_ARROW_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ADVANCE_ARROW_LEFT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ADVANCE_ARROW_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ADVANCE_ARROW_RIGHT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ADVANCE_ARROW_STRAIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ADVANCE_ARROW_STRAIGHT_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ADVANCE_ARROW_STRAIGHT_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ADVANCE_ARROW_STRAIGHT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ADVANCE_ARROW_STRAIGHT_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ADVANCE_ARROW_STRAIGHT_UTURN_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ADVANCE_ARROW_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ADVANCE_ARROW_UTURN_RIGHT, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ARROW_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ARROW_LEFT_MERGE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ARROW_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ARROW_LEFT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ARROW_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ARROW_RIGHT_MERGE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ARROW_RIGHT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ARROW_STRAIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ARROW_STRAIGHT_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ARROW_STRAIGHT_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ARROW_STRAIGHT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ARROW_STRAIGHT_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ARROW_STRAIGHT_UTURN_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ARROW_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ARROW_UTURN_RIGHT, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ARROW_PROHIBITED, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.PROHIBITED_ARROW_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.PROHIBITED_ARROW_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.PROHIBITED_ARROW_LEFT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.PROHIBITED_ARROW_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.PROHIBITED_ARROW_RIGHT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.PROHIBITED_ARROW_STRAIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.PROHIBITED_ARROW_STRAIGHT_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.PROHIBITED_ARROW_STRAIGHT_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.PROHIBITED_ARROW_STRAIGHT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.PROHIBITED_ARROW_STRAIGHT_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.PROHIBITED_ARROW_STRAIGHT_UTURN_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.PROHIBITED_ARROW_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.PROHIBITED_ARROW_UTURN_RIGHT, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ORANGE_ARROW_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ORANGE_ARROW_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ORANGE_ARROW_LEFT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ORANGE_ARROW_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ORANGE_ARROW_RIGHT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ORANGE_ARROW_STRAIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ORANGE_ARROW_STRAIGHT_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ORANGE_ARROW_STRAIGHT_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ORANGE_ARROW_STRAIGHT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ORANGE_ARROW_STRAIGHT_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ORANGE_ARROW_STRAIGHT_UTURN_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ORANGE_ARROW_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.ORANGE_ARROW_UTURN_RIGHT, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.DECELERATION_CROSSROADS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.SPEED_BUMP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.YIELD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksLarge.DISTANCE_CONFIRM, RenderLayer.getCutout());

        // roadmark_style_2
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ADVANCE_ARROW_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ADVANCE_ARROW_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ADVANCE_ARROW_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ADVANCE_ARROW_STRAIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ADVANCE_ARROW_STRAIGHT_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ADVANCE_ARROW_STRAIGHT_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ADVANCE_ARROW_STRAIGHT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ADVANCE_ARROW_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ADVANCE_ARROW_UTURN_RIGHT, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ARROW_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ARROW_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ARROW_PROHIBITED, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ARROW_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ARROW_STRAIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ARROW_STRAIGHT_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ARROW_STRAIGHT_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ARROW_STRAIGHT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ARROW_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ARROW_UTURN_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ARROW_DOUBLE_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ARROW_DOUBLE_RIGHT, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.PROHIBITED_ARROW_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.PROHIBITED_ARROW_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.PROHIBITED_ARROW_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.PROHIBITED_ARROW_STRAIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.PROHIBITED_ARROW_STRAIGHT_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.PROHIBITED_ARROW_STRAIGHT_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.PROHIBITED_ARROW_STRAIGHT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.PROHIBITED_ARROW_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.PROHIBITED_ARROW_UTURN_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.PROHIBITED_ARROW_DOUBLE_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.PROHIBITED_ARROW_DOUBLE_RIGHT, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ORANGE_ARROW_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ORANGE_ARROW_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ORANGE_ARROW_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ORANGE_ARROW_STRAIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ORANGE_ARROW_STRAIGHT_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ORANGE_ARROW_STRAIGHT_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ORANGE_ARROW_STRAIGHT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ORANGE_ARROW_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ORANGE_ARROW_UTURN_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ORANGE_ARROW_DOUBLE_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle2.ORANGE_ARROW_DOUBLE_RIGHT, RenderLayer.getCutout());

        // roadmark_style_3
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ARROW_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ARROW_LEFT_MERGE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ARROW_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ARROW_LEFT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ARROW_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ARROW_RIGHT_MERGE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ARROW_RIGHT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ARROW_STRAIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ARROW_STRAIGHT_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ARROW_STRAIGHT_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ARROW_STRAIGHT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ARROW_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ARROW_UTURN_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ARROW_PROHIBITED, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.PROHIBITED_ARROW_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.PROHIBITED_ARROW_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.PROHIBITED_ARROW_LEFT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.PROHIBITED_ARROW_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.PROHIBITED_ARROW_RIGHT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.PROHIBITED_ARROW_STRAIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.PROHIBITED_ARROW_STRAIGHT_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.PROHIBITED_ARROW_STRAIGHT_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.PROHIBITED_ARROW_STRAIGHT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.PROHIBITED_ARROW_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.PROHIBITED_ARROW_UTURN_RIGHT, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ORANGE_ARROW_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ORANGE_ARROW_LEFT_MERGE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ORANGE_ARROW_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ORANGE_ARROW_LEFT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ORANGE_ARROW_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ORANGE_ARROW_RIGHT_MERGE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ORANGE_ARROW_RIGHT_UTURN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ORANGE_ARROW_STRAIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ORANGE_ARROW_STRAIGHT_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ORANGE_ARROW_STRAIGHT_LEFT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ORANGE_ARROW_STRAIGHT_RIGHT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ORANGE_ARROW_UTURN_LEFT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ArrowBlocksStyle3.ORANGE_ARROW_UTURN_RIGHT, RenderLayer.getCutout());
    }

    /**
     * 注册自定义模型客户端网络通道。
     */
    private static void registerCustomModelNetworking() {
        ClientNetworking.register();
    }

    /**
     * 注册物品使用回调。
     *
     * <p>手持 {@link CustomModelBlockItem} 时直接放行，模型选择通过
     * Model Wand 右键点击已放置的方块来完成。
     */
    private static void registerCustomModelItemUseHandler() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (!world.isClient || !CustomModelBlockItem.isCustomModelItem(stack)) {
                return ActionResult.PASS;
            }
            return ActionResult.PASS;
        });
    }

    /**
     * 注册方块使用回调。
     *
     * <p>当玩家右键点击 {@link CustomModelBlock} 时，若手持 Model Wand，
     * 打开模型选择界面；其他情况放行由服务端处理。
     */
    private static void registerCustomModelBlockUseHandler() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!world.isClient) {
                return ActionResult.PASS;
            }

            ItemStack stack = player.getStackInHand(hand);
            BlockState state = world.getBlockState(hitResult.getBlockPos());
            if (!(state.getBlock() instanceof CustomModelBlock)) {
                return ActionResult.PASS;
            }

            // Model Wand 右键打开选择叠加层
            if (ModelWandItem.isModelWand(stack)) {
                MinecraftClient mc = MinecraftClient.getInstance();
                mc.setOverlay(new ModelSelectionOverlay(TargetType.BLOCK, hitResult.getBlockPos()));
                // 解锁鼠标，使光标显示出来
                mc.mouse.unlockCursor();
                return ActionResult.SUCCESS;
            }

            return ActionResult.PASS;
        });
    }
}
