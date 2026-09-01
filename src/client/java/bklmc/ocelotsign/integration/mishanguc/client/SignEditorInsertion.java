package bklmc.ocelotsign.integration.mishanguc.client;

import net.minecraft.util.Identifier;
import pers.solid.mishang.uc.screen.AbstractSignBlockEditScreen;
import pers.solid.mishang.uc.screen.TextFieldListWidget;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.text.TextureSpecialDrawable;

/**
 * 用于向 mishanguc 告示牌编辑界面注入纹理或文本行的工具类。
 */
public final class SignEditorInsertion {
    private SignEditorInsertion() {
    }

    /**
     * 在编辑界面末尾追加一个纹理条目。
     *
     * @param screen     当前的告示牌编辑界面
     * @param identifier 要插入的纹理资源标识符
     */
    public static void insertTexture(AbstractSignBlockEditScreen<?> screen, Identifier identifier) {
        // 取当前条目数量作为新条目的插入下标（追加到末尾）。
        TextFieldListWidget textFieldListWidget = screen.textFieldListWidget;
        int index = textFieldListWidget.children().size();

        // 复用上一条目的样式作为新条目的默认值，避免每次插入都从空白开始。
        TextContext textContext;
        if (index > 0) {
            TextContext prev = textFieldListWidget.children().get(index - 1).textContext;
            textContext = prev.clone();
            textContext.text = null;
            textContext.extra = null;
        } else {
            textContext = new TextContext();
        }

        // 将纹理附加到 TextContext 上，再创建对应的纹理绘制器。
        TextureSpecialDrawable textureDrawable = new TextureSpecialDrawable(identifier, textContext);
        textContext.extra = textureDrawable;

        TextFieldListWidget.Entry newEntry = textFieldListWidget.addTextField(index, textContext, false);
        SignTextCommandApplier.apply(newEntry, screen);
        syncSignPreview(screen);
        focusOnNewEntry(screen, textFieldListWidget, index);
    }

    /**
     * 在编辑界面末尾追加一段文本内容。
     *
     * @param screen 当前的告示牌编辑界面
     * @param text   要插入的文本字符串
     */
    public static void insertText(AbstractSignBlockEditScreen<?> screen, String text) {
        // 取当前条目数量作为新条目的插入下标（追加到末尾）。
        TextFieldListWidget textFieldListWidget = screen.textFieldListWidget;
        int index = textFieldListWidget.children().size();

        // 复用上一条目的样式作为新条目的默认值，避免每次插入都从空白开始。
        TextContext textContext;
        if (index > 0) {
            TextContext prev = textFieldListWidget.children().get(index - 1).textContext;
            textContext = prev.clone();
            // 清空文本和附加内容，只保留样式信息。
            textContext.text = null;
            textContext.extra = null;
        } else {
            textContext = new TextContext();
        }

        TextFieldListWidget.Entry newEntry = textFieldListWidget.addTextField(index, textContext, false);
        newEntry.textFieldWidget.setText(text);
        newEntry.textFieldWidget.setCursorToEnd(false);
        SignTextCommandApplier.apply(newEntry, screen);
        syncSignPreview(screen);
        focusOnNewEntry(screen, textFieldListWidget, index);
    }

    /**
     * 通知告示牌实体当前数据已变更，需要刷新预览显示。
     */
    private static void syncSignPreview(AbstractSignBlockEditScreen<?> screen) {
        screen.entity.markDirty();
    }

    /**
     * 把焦点转移到新插入的条目上，让用户可以直接输入或调整文本。
     * <p>
     * 这里显式聚焦到列表、再聚焦到条目内部的文本框，模拟旧版本的行为。
     */
    private static void focusOnNewEntry(
            AbstractSignBlockEditScreen<?> screen,
            TextFieldListWidget textFieldListWidget,
            int index
    ) {
        TextFieldListWidget.Entry newEntry = textFieldListWidget.children().get(index);
        textFieldListWidget.setFocused(newEntry);
        if (!textFieldListWidget.children().isEmpty()) {
            screen.setFocused(textFieldListWidget);
            textFieldListWidget.setFocused(newEntry);
            if (newEntry.textFieldWidget != null) {
                newEntry.textFieldWidget.setFocused(true);
                newEntry.textFieldWidget.setCursorToEnd(false);
            }
        }
    }
}
