package bklmc.ocelotsign.platform;

import bklmc.ocelotsign.blockentity.CustomModelBlockEntity;
import bklmc.ocelotsign.item.CustomModelBlockItem;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务端网络通道注册
 */
public final class ServerNetworking {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerNetworking.class);

    /**
     * 自定义模型选择数据包的 ID。
     */
    public static final CustomPayload.Id<SelectModelPayload> SELECT_MODEL_ID = new CustomPayload.Id<>(ModIdentifiers.SELECT_MODEL);

    /**
     * 自定义模型选择数据包的编解码器。
     */
    public static final PacketCodec<RegistryByteBuf, SelectModelPayload> CODEC = new PacketCodec<>() {
        @Override
        public void encode(RegistryByteBuf buf, SelectModelPayload value) {
            buf.writeString(value.modelId);
            buf.writeBoolean(value.hasBlockPos);
            if (value.hasBlockPos && value.blockPos != null) {
                buf.writeBlockPos(value.blockPos);
            }
            buf.writeBoolean(value.isMainHand);
        }

        @Override
        public SelectModelPayload decode(RegistryByteBuf buf) {
            return new SelectModelPayload(buf);
        }
    };

    /**
     * 自定义模型选择数据包。
     */
    public static final class SelectModelPayload implements CustomPayload {
        public String modelId;
        public boolean hasBlockPos;
        public BlockPos blockPos;
        public boolean isMainHand;

        public SelectModelPayload(RegistryByteBuf buf) {
            this.modelId = buf.readString();
            this.hasBlockPos = buf.readBoolean();
            if (this.hasBlockPos) {
                this.blockPos = buf.readBlockPos();
            } else {
                this.blockPos = null;
            }
            this.isMainHand = buf.readBoolean();
        }

        public SelectModelPayload(String modelId, boolean hasBlockPos, BlockPos blockPos, boolean isMainHand) {
            this.modelId = modelId;
            this.hasBlockPos = hasBlockPos;
            this.blockPos = blockPos;
            this.isMainHand = isMainHand;
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return SELECT_MODEL_ID;
        }
    }

    private ServerNetworking() {
    }

    /**
     * 注册所有服务端网络通道处理器。
     */
    public static void register() {
        registerPayloadTypes();
        registerModelSelectionHandler();
    }

    /**
     * 注册自定义数据包类型。
     */
    private static void registerPayloadTypes() {
        // 注册 C2S (客户端到服务端) payload 类型
        PayloadTypeRegistry.playC2S().register(SELECT_MODEL_ID, CODEC);
    }

    /**
     * 注册自定义模型选择数据包处理器。
     */
    private static void registerModelSelectionHandler() {
        ServerPlayNetworking.registerGlobalReceiver(SELECT_MODEL_ID, (payload, context) -> {
            String selectedId = payload.modelId;
            Hand hand = payload.isMainHand ? Hand.MAIN_HAND : Hand.OFF_HAND;
            BlockPos blockPos = payload.hasBlockPos ? payload.blockPos : null;
            var player = context.player();
            var server = context.server();

            // 参数合法性校验
            if (selectedId.isEmpty() || selectedId.length() > 256 || selectedId.contains("\0")) {
                return;
            }

            if (!payload.hasBlockPos) {
                CustomModelBlockItem.rememberPendingSelection(player.getUuid(), hand, selectedId);
            }

            // 服务端主线程执行的任务
            Runnable task = () -> {
                if (blockPos != null) {
                    var blockEntity = player.getWorld().getBlockEntity(blockPos);
                    if (blockEntity instanceof CustomModelBlockEntity customBE) {
                        customBE.setModelId(selectedId);
                        if (player.getWorld() instanceof ServerWorld serverWorld) {
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
