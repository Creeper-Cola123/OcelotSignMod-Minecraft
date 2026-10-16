package bklmc.ocelotsign.data;

import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.util.Identifier;
import bklmc.ocelotsign.OcelotSignMod;

import java.util.Optional;

/**
 * 模组自定义模型定义
 */
public final class OcelotSignModModels {
    public static final Model ROAD_MARK = createBlock("road_mark", TextureKey.TEXTURE);
    public static final Model ROAD_MARK_ON_SLAB = createBlock("road_mark_on_slab", "_on_slab", TextureKey.TEXTURE);

    private static Model createBlock(String name, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(OcelotSignMod.id("block/" + name)), Optional.empty(), requiredTextureKeys);
    }

    // 创建带变体的方块模型
    private static Model createBlock(String name, String variant, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(OcelotSignMod.id("block/" + name)), Optional.of(variant), requiredTextureKeys);
    }
}
