package bklmc.ocelotsign.integration.mishanguc;

import net.minecraft.util.Identifier;

/**
 * mishanguc 模组标识符的集中引用
 */
public final class MishangIdentifiers {
    public static final String NAMESPACE = "mishanguc";
    public static final Identifier EDIT_SIGN_FINISH = of("edit_sign_finish");
    public static final Identifier ROADS_ITEM_GROUP = of("roads");
    private MishangIdentifiers() {
    }

    /**
     * 构造 mishanguc 命名空间下的标识符。
     *
     * @param path 标识符路径
     * @return 对应的标识符
     */
    public static Identifier of(String path) {
        return Identifier.of(NAMESPACE, path);
    }
}
