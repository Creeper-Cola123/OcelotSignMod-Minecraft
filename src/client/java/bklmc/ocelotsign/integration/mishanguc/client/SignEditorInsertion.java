package bklmc.ocelotsign.integration.mishanguc.client;

import net.minecraft.resources.Identifier;
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
            // 清空文本和附加内容，只保留样式信息。
            textContext.text = null;
            textContext.extra = null;
        } else {
            textContext = new TextContext();
        }

        // 将纹理附加到 TextContext 上，再创建对应的纹理绘制器。
        TextureSpecialDrawable textureDrawable = new TextureSpecialDrawable(identifier, textContext);
        textContext.extra = textureDrawable;

        // mishanguc for mc1.21.x / 26.x：addTextField 位于 TextFieldListWidget 上，而不是编辑界面上。
        TextFieldListWidget.Entry newEntry = textFieldListWidget.addTextField(index, textContext, false);
        // 应用条目上的命令字段（mishanguc 要求独立调用 apply）。
        SignTextCommandApplier.apply(newEntry, screen);
        // 通知告示牌实体重新生成预览。
        syncSignPreview(screen);
        // 聚焦到新插入的条目，便于用户立即编辑。
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

        // mishanguc for mc1.21.x / 26.x：addTextField 位于 TextFieldListWidget 上，而不是编辑界面上。
        TextFieldListWidget.Entry newEntry = textFieldListWidget.addTextField(index, textContext, false);
        // 将传入的文本写入新条目的输入框，并把光标移动到末尾，方便继续追加内容。
        // 26.x 走新版 Yarn 映射：EditBox#setValue / moveCursorToEnd。
        newEntry.textFieldWidget.setValue(text);
        newEntry.textFieldWidget.moveCursorToEnd(false);
        // 应用条目上的命令字段（mishanguc 要求独立调用 apply）。
        SignTextCommandApplier.apply(newEntry, screen);
        // 通知告示牌实体重新生成预览。
        syncSignPreview(screen);
        // 聚焦到新插入的条目，便于用户立即编辑。
        focusOnNewEntry(screen, textFieldListWidget, index);
    }

    /**
     * 通知告示牌实体当前数据已变更，需要刷新预览显示。
     * <p>
     * 26.x 走新版 Yarn 映射：BlockEntity#setChanged 取代了 1.21.x 的 markDirty。
     */
    private static void syncSignPreview(AbstractSignBlockEditScreen<?> screen) {
        screen.entity.setChanged();
    }

    /**
     * 把焦点转移到新插入的条目上，让用户可以直接输入或调整文本。
     * <p>
     * 26.x 的 {@link TextFieldListWidget} 与 1.21.x 一样，仅保留
     * {@link pers.solid.mishang.uc.screen.EntryListWidget#setFocused} 单参数版本，
     * 三参数 {@code setFocused(Element, boolean, boolean)} 已被移除；
     * 旧代码里 {@code setScrollAmount(textFieldListWidget.scrollAmount())}
     * 是一个无意义的自赋值（原版的 scrollY 残留），这里也一并清理。
     */
    private static void focusOnNewEntry(
            AbstractSignBlockEditScreen<?> screen,
            TextFieldListWidget textFieldListWidget,
            int index
    ) {
        TextFieldListWidget.Entry newEntry = textFieldListWidget.children().get(index);
        // 先把列表的焦点设为新条目，使列表渲染出对应的选中状态。
        textFieldListWidget.setFocused(newEntry);
        if (!textFieldListWidget.children().isEmpty()) {
            // 把屏幕焦点交给列表，再把列表焦点交给条目，最后把键盘焦点交给条目的输入框，
            // 这样玩家无需额外点击就能直接键入。
            screen.setFocused(textFieldListWidget);
            textFieldListWidget.setFocused(newEntry);
            if (newEntry.textFieldWidget != null) {
                newEntry.textFieldWidget.setFocused(true);
                // 26.x 走新版 Yarn 映射：EditBox#moveCursorToEnd。
                newEntry.textFieldWidget.moveCursorToEnd(false);
            }
        }
    }
}
