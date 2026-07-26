package bklmc.ocelotsign.blockentity;

import static bklmc.ocelotsign.OcelotSignMod.CUSTOM_MODEL_BLOCK_ENTITY;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

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
        setChanged();
        if (this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
            if (this.level instanceof ServerLevel serverWorld) {
                serverWorld.getChunkSource().blockChanged(this.worldPosition);
            }
        }
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        this.modelId = nbt.getString("ModelId");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        nbt.putString("ModelId", this.modelId);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }
}
