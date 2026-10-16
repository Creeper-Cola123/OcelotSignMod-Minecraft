package bklmc.ocelotsign.blockentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import static bklmc.ocelotsign.OcelotSignMod.CUSTOM_MODEL_BLOCK_ENTITY;

/**
 * 动态模型方块实体
 *
 * @see bklmc.ocelotsign.block.custom.CustomModelBlock
 * @see bklmc.ocelotsign.item.CustomModelBlockItem
 * @see BlockEntity
 */
public class CustomModelBlockEntity extends BlockEntity {
    private String modelId = "";

    public CustomModelBlockEntity(BlockPos pos, BlockState state) {
        super(CUSTOM_MODEL_BLOCK_ENTITY, pos, state);
    }

    public String getModelId() {
        return modelId;
    }

    /**
     * @param modelId 新的模型 ID；{@code null} 视为空字符串
     */
    public void setModelId(String modelId) {
        this.modelId = modelId == null ? "" : modelId;
        markDirty();
        if (this.world != null && !this.world.isClient) {
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
            if (this.world instanceof ServerWorld serverWorld) {
                serverWorld.getChunkManager().markForUpdate(this.pos);
            }
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.modelId = nbt.getString("ModelId");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString("ModelId", this.modelId);
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
