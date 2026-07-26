package bklmc.ocelotsign.platform;

import bklmc.ocelotsign.OcelotSignMod;
import net.minecraft.resources.ResourceLocation;

/**
 * 本模组标识符的集中定义
 */
public final class ModIdentifiers {
    /** 选择模型数据包标识符 */
    public static final ResourceLocation SELECT_MODEL = of("select_model");

    // 私有构造函数，防止实例化
    private ModIdentifiers() {
    }

    /**
     * 构造本模组命名空间下的标识符。
     *
     * @param path 标识符路径
     * @return 对应的标识符
     */
    public static ResourceLocation of(String path) {
        return ResourceLocation.fromNamespaceAndPath(OcelotSignMod.MOD_ID, path);
    }
}
