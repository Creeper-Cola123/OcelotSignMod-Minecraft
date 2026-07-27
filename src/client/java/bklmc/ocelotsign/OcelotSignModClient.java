package bklmc.ocelotsign;

import bklmc.ocelotsign.block.custom.CustomModelBlock;
import bklmc.ocelotsign.blockentity.ModBlockEntities;
import bklmc.ocelotsign.client.PatternAndFontBlankScreen;
import bklmc.ocelotsign.client.PatternAndFontOverlay;
import bklmc.ocelotsign.client.PatternRegistry;
import bklmc.ocelotsign.client.gui.ModelSelectionScreen;
import bklmc.ocelotsign.client.gui.ModelSelectionScreen.TargetType;
import bklmc.ocelotsign.client.model.ModelRegistryManager;
import bklmc.ocelotsign.client.render.CustomModelBER;
import bklmc.ocelotsign.item.CustomModelBlockItem;
import bklmc.ocelotsign.item.ModelWandItem;
import bklmc.ocelotsign.platform.ClientNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
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

        // 注册资源包刷新监听器
        PatternRegistry.registerReloadListener();

        registerCustomModelNetworking();
        registerCustomModelItemUseHandler();
        registerCustomModelBlockUseHandler();
        registerOverlayScreenEvents();
    }

    /**
     * 注册图案与字体浮层的屏幕事件监听。
     *
     * <p>拦截屏幕的渲染、鼠标点击/释放/滚动、键盘按键事件，
     * 当浮层可见时优先处理浮层交互。
     */
    private void registerOverlayScreenEvents() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            ScreenEvents.afterExtract(screen).register((screen1, context, mouseX, mouseY, tickDelta) -> {
                if (PatternAndFontOverlay.isVisible) {
                    screen1.setFocused(null);
                    PatternAndFontOverlay.render(context, mouseX, mouseY);
                }
            });

            ScreenMouseEvents.allowMouseClick(screen).register((screen1, event) -> {
                if (PatternAndFontOverlay.isVisible) {
                    if (PatternAndFontOverlay.mouseClicked(event.x(), event.y(), event.button())) {
                        screen1.setDragging(true);
                    }

                    if (!PatternAndFontOverlay.isVisible && screen1 instanceof PatternAndFontBlankScreen) {
                        screen1.onClose();
                    }
                    return false;
                }
                return true;
            });

            ScreenMouseEvents.allowMouseRelease(screen).register((screen1, event) -> {
                if (PatternAndFontOverlay.isVisible) {
                    PatternAndFontOverlay.mouseReleased(event.x(), event.y(), event.button());
                    screen1.setDragging(false);
                    return false;
                }
                return true;
            });

            ScreenMouseEvents.allowMouseScroll(screen).register((screen1, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
                if (PatternAndFontOverlay.isVisible) {
                    PatternAndFontOverlay.mouseScrolled(mouseX, mouseY, verticalAmount);
                    return false;
                }
                return true;
            });

            ScreenKeyboardEvents.allowKeyPress(screen).register((screen1, event) -> {
                if (PatternAndFontOverlay.isVisible) {
                    if (event.key() == GLFW.GLFW_KEY_ESCAPE) {
                        PatternAndFontOverlay.isVisible = false;
                        if (screen1 instanceof PatternAndFontBlankScreen) {
                            screen1.onClose();
                        }
                    }
                    return false;
                }
                return true;
            });

            ScreenKeyboardEvents.allowKeyRelease(screen).register((screen1, event) -> {
                return !PatternAndFontOverlay.isVisible;
            });
        });
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
        UseItemCallback.EVENT.register((player, world, hand) -> InteractionResult.PASS);
    }

    /**
     * 注册方块使用回调。
     *
     * <p>当玩家右键点击 {@link CustomModelBlock} 时，若手持 Model Wand，
     * 打开模型选择界面；其他情况放行由服务端处理。
     */
    private static void registerCustomModelBlockUseHandler() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!world.isClientSide()) {
                return InteractionResult.PASS;
            }

            ItemStack stack = player.getItemInHand(hand);
            BlockState state = world.getBlockState(hitResult.getBlockPos());
            if (!(state.getBlock() instanceof CustomModelBlock)) {
                return InteractionResult.PASS;
            }

            // Model Wand 右键打开选择界面
            if (ModelWandItem.isModelWand(stack)) {
                Minecraft.getInstance().setScreen(new ModelSelectionScreen(TargetType.BLOCK, hitResult.getBlockPos()));
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.PASS;
        });
    }
}
