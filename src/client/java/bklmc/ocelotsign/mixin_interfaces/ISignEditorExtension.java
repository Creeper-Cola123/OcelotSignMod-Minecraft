package bklmc.ocelotsign.mixin_interfaces;

import net.minecraft.util.Identifier;

/**
 * 告示牌编辑器屏幕的 Mixin 扩展接口。
 *
 * <p>提供向 mishanguc 告示牌编辑器插入纹理和文本的方法。
 * 实现通过 {@code AbstractSignBlockEditScreenMixin} 注入。
 *
 * @see bklmc.ocelotsign.client.PatternAndFontOverlay
 */
public interface ISignEditorExtension {
    /**
     * 向编辑界面插入一个纹理条目。
     *
     * @param identifier 纹理标识符
     */
    void ocelotsign$insertTexture(Identifier identifier);

    /**
     * 向编辑界面插入一段文本内容。
     *
     * @param text 文本内容
     */
    void ocelotsign$insertText(String text);
}
