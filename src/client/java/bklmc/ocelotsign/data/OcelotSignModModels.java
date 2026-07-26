package bklmc.ocelotsign.data;

import bklmc.ocelotsign.OcelotSignMod;

import java.util.Optional;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;

/**
 * 模组自定义模型定义
 */
public final class OcelotSignModModels {
    public static final ModelTemplate ROAD_MARK = createBlock("road_mark", TextureSlot.TEXTURE);
    public static final ModelTemplate ROAD_MARK_ON_SLAB = createBlock("road_mark_on_slab", "_on_slab", TextureSlot.TEXTURE);

    private static ModelTemplate createBlock(String name, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(OcelotSignMod.id("block/" + name)), Optional.empty(), requiredTextureKeys);
    }

    // 创建带变体的方块模型
    private static ModelTemplate createBlock(String name, String variant, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(OcelotSignMod.id("block/" + name)), Optional.of(variant), requiredTextureKeys);
    }
}
