package bklmc.ocelotsign.platform;

import bklmc.ocelotsign.blockentity.CustomModelBlockEntity;
import bklmc.ocelotsign.integration.mishanguc.MishangAccess;
import bklmc.ocelotsign.integration.mishanguc.MishangIdentifiers;
import bklmc.ocelotsign.item.CustomModelBlockItem;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Hand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务端网络通道注册
 */
public final class ServerNetworking {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerNetworking.class);

    // 私有构造函数，防止实例化
    private ServerNetworking() {
    }

    /**
     * 注册所有服务端网络通道处理器。
     */
    public static void register() {
        registerMishangucPacketHandler();
        registerModelSelectionHandler();
    }

    // 注册 mishanguc 的告示牌编辑数据包处理器（若可用）
    private static void registerMishangucPacketHandler() {
        if (!MishangAccess.isAvailable()) {
            LOGGER.debug("Mishanguc 不可用；跳过告示牌编辑数据包处理器");
            return;
        }

        Object handler = MishangAccess.getEditSignFinishPacketHandler();
        if (handler != null) {
            try {
                ServerPlayNetworking.registerGlobalReceiver(
                        MishangIdentifiers.EDIT_SIGN_FINISH,
                        (ServerPlayNetworking.PlayChannelHandler) handler
                );
            } catch (Exception e) {
                LOGGER.error("注册 mishanguc 数据包处理器失败", e);
            }
        }
    }

    // 注册自定义模型选择数据包处理器
    private static void registerModelSelectionHandler() {
        ServerPlayNetworking.registerGlobalReceiver(ModIdentifiers.SELECT_MODEL, (server, player, handler, buf, responseSender) -> {
            String selectedId = buf.readString();
            boolean hasBlockPos = buf.readBoolean();
            BlockPos blockPos = hasBlockPos ? buf.readBlockPos() : null;
            boolean isMainHand = !hasBlockPos && buf.readBoolean();
            Hand hand = isMainHand ? Hand.MAIN_HAND : Hand.OFF_HAND;

            // 参数合法性校验
            if (selectedId.isEmpty() || selectedId.length() > 256 || selectedId.contains("\0")) {
                return;
            }

            if (!hasBlockPos) {
                CustomModelBlockItem.rememberPendingSelection(player.getUuid(), hand, selectedId);
            }

            // 服务端主线程执行的任务
            Runnable task = () -> {
                if (blockPos != null) {
                    var blockEntity = player.getWorld().getBlockEntity(blockPos);
                    if (blockEntity instanceof CustomModelBlockEntity customBE) {
                        customBE.setModelId(selectedId);
                        if (player.getWorld() instanceof ServerWorld) {
                            ServerWorld serverWorld = (ServerWorld) player.getWorld();
                            serverWorld.getChunkManager().markForUpdate(blockPos);
                        }
                    }
                    return;
                }

                CustomModelBlockItem.applySelectionToStack(player, hand, selectedId);
            };

            // 确保在服务端主线程执行
            if (server.isOnThread()) {
                task.run();
            } else {
                server.execute(task);
            }
        });
    }
}
