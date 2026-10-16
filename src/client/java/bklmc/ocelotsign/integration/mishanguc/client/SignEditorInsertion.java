package bklmc.ocelotsign.integration.mishanguc.client;

import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.Identifier;
import pers.solid.mishang.uc.screen.AbstractSignBlockEditScreen;
import pers.solid.mishang.uc.screen.TextFieldListWidget;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.text.TextureSpecialDrawable;

import java.util.List;

/**
 * 向 mishanguc 告示牌编辑界面插入纹理或文本行。
 */
public final class SignEditorInsertion {
    private SignEditorInsertion() {
    }

    /**
     * 插入一个纹理条目到编辑界面末尾。
     *
     * @param screen      编辑界面。
     * @param identifier 纹理标识符。
     */
    public static void insertTexture(AbstractSignBlockEditScreen<?> screen, Identifier identifier) {
        TextFieldListWidget textFieldListWidget = screen.textFieldListWidget;
        int index = textFieldListWidget.children().size();
        TextContext textContext = createTextContextForInsert(textFieldListWidget, index);

        // 清除所有现有条目的文本选区，避免插入后多行同时高亮
        clearTextSelections(textFieldListWidget);

        TextureSpecialDrawable textureDrawable = new TextureSpecialDrawable(identifier, textContext);
        textContext.extra = textureDrawable;

        // 使用 screen.addTextField (4 参数版本) 兼容 1.19.4
        screen.addTextField(index, textContext, false, true);
        syncSignPreview(screen);
    }

    /**
     * 插入一段文本内容到编辑界面末尾。
     *
     * @param screen 编辑界面。
     * @param text   要插入的文本。
     */
    public static void insertText(AbstractSignBlockEditScreen<?> screen, String text) {
        TextFieldListWidget textFieldListWidget = screen.textFieldListWidget;
        int index = textFieldListWidget.children().size();
        TextContext textContext = createTextContextForInsert(textFieldListWidget, index);

        // 清除所有现有条目的文本选区，避免插入后多行同时高亮
        clearTextSelections(textFieldListWidget);

        // 先调用 addTextField 创建条目（textContext.text 为 null，mishanguc 会生成默认文本）
        screen.addTextField(index, textContext, false, true);

        // 获取新创建的 Entry 并设置纯文本
        List<TextFieldListWidget.Entry> children = textFieldListWidget.children();
        if (children.size() > index) {
            TextFieldListWidget.Entry newEntry = children.get(index);
            newEntry.textFieldWidget.setText(text);
            newEntry.textFieldWidget.setCursorToEnd();

            // 应用命令并更新预览
            SignTextCommandApplier.apply(newEntry, screen, textContext);
        }

        syncSignPreview(screen);
    }

    /**
     * 将所有现有 Entry 上 TextFieldWidget 的文本选区起点和终点都设回当前光标位置，
     * 从而取消高亮。这样在调用 {@code addTextField} 添加新行并切换焦点后，
     * 不会出现"新行 + 上一行"同时被选中的情况。
     */
    private static void clearTextSelections(TextFieldListWidget textFieldListWidget) {
        for (TextFieldListWidget.Entry entry : textFieldListWidget.children()) {
            TextFieldWidget widget = entry.textFieldWidget;
            int cursor = widget.getCursor();
            widget.setSelectionStart(cursor);
            widget.setSelectionEnd(cursor);
        }
    }

    /**
     * 标记方块实体已修改并通知客户端更新。
     */
    private static void syncSignPreview(AbstractSignBlockEditScreen<?> screen) {
        screen.entity.markDirty();
    }

    /**
     * 为插入的新条目创建 {@link TextContext}。若不是首行，则从前一行继承样式。
     */
    private static TextContext createTextContextForInsert(TextFieldListWidget textFieldListWidget, int index) {
        List<TextFieldListWidget.Entry> children = textFieldListWidget.children();
        if (index > 0) {
            TextContext prevTextContext = getTextContextFromEntry(children.get(index - 1));
            if (prevTextContext != null) {
                TextContext textContext = prevTextContext.clone();
                textContext.text = null;
                textContext.extra = null;
                return textContext;
            }
        }
        return new TextContext();
    }

    /**
     * 从 Entry 中获取 TextContext。优先使用 textContext 字段，如果不存在则返回 null。
     */
    private static TextContext getTextContextFromEntry(TextFieldListWidget.Entry entry) {
        try {
            java.lang.reflect.Field textContextField = TextFieldListWidget.Entry.class.getDeclaredField("textContext");
            textContextField.setAccessible(true);
            return (TextContext) textContextField.get(entry);
        } catch (Exception e) {
            return null;
        }
    }
}
