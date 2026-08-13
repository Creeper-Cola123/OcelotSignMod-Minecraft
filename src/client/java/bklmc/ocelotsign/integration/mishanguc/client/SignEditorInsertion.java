package bklmc.ocelotsign.integration.mishanguc.client;

import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.Identifier;
import pers.solid.mishang.uc.screen.AbstractSignBlockEditScreen;
import pers.solid.mishang.uc.screen.TextFieldListWidget;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.text.TextureSpecialDrawable;

import java.util.List;

/**
 * 向 mishanguc 告示牌编辑界面插入纹理或文本行。(1.19.2 兼容版)
 */
public final class SignEditorInsertion {
    private SignEditorInsertion() {
    }

    public static void insertTexture(AbstractSignBlockEditScreen<?> screen, Identifier identifier) {
        TextFieldListWidget textFieldListWidget = screen.textFieldListWidget;
        List<TextFieldListWidget.Entry> children = textFieldListWidget.children();
        int index = children.size();
        TextContext textContext = createTextContextForInsert(textFieldListWidget, index, screen);

        TextureSpecialDrawable textureDrawable = new TextureSpecialDrawable(identifier, textContext);
        textContext.extra = textureDrawable;

        // 1.19.2: 使用 screen.addTextField
        screen.addTextField(index);
        // 获取新创建的 entry
        TextFieldListWidget.Entry newEntry = textFieldListWidget.children().get(index);
        // 替换其 TextContext 的 extra
        TextContext newContext = getTextContextForWidget(screen, newEntry.textFieldWidget);
        if (newContext != null) {
            newContext.extra = textureDrawable;
        }
        SignTextCommandApplier.apply(newEntry, screen, textContext);
        syncSignPreview(screen);
        focusOnNewEntry(screen, textFieldListWidget, index);

        // 在 focusOnNewEntry 之后重新设置文本，确保输入框显示正确内容
        newEntry.textFieldWidget.setText("-texture " + identifier.toString());
        newEntry.textFieldWidget.setCursorToEnd();
    }

    public static void insertText(AbstractSignBlockEditScreen<?> screen, String text) {
        TextFieldListWidget textFieldListWidget = screen.textFieldListWidget;
        List<TextFieldListWidget.Entry> children = textFieldListWidget.children();
        int index = children.size();

        // 1.19.2: 使用 screen.addTextField
        screen.addTextField(index);
        TextFieldListWidget.Entry newEntry = textFieldListWidget.children().get(index);

        // 先获取 TextContext
        TextContext textContext = getTextContextForWidget(screen, newEntry.textFieldWidget);
        if (textContext == null) {
            // 如果无法获取 TextContext，直接设置文本并返回
            newEntry.textFieldWidget.setText(text);
            newEntry.textFieldWidget.setCursorToEnd();
            syncSignPreview(screen);
            focusOnNewEntry(screen, textFieldListWidget, index);
            return;
        }

        // 解析命令语法，设置 textContext.text
        newEntry.textFieldWidget.setText(text);
        newEntry.textFieldWidget.setCursorToEnd();
        SignTextCommandApplier.apply(newEntry, screen, textContext);

        syncSignPreview(screen);
        focusOnNewEntry(screen, textFieldListWidget, index);

        // 关键：在 focusOnNewEntry 之后，再次确保 TextFieldWidget 显示正确的文本
        // 因为 mishanguc 的 setSelected 可能触发内部监听器，从 textContext 同步文本
        // 如果 textContext.text 是特殊格式（如空字符串），会导致显示异常
        // 重新设置文本以确保 TextFieldWidget 和 textContext 保持一致
        newEntry.textFieldWidget.setText(text);
        newEntry.textFieldWidget.setCursorToEnd();
    }

    private static void syncSignPreview(AbstractSignBlockEditScreen<?> screen) {
        screen.entity.markDirty();
    }

    private static void focusOnNewEntry(
            AbstractSignBlockEditScreen<?> screen,
            TextFieldListWidget textFieldListWidget,
            int index
    ) {
        List<TextFieldListWidget.Entry> children = textFieldListWidget.children();
        if (index < children.size()) {
            TextFieldListWidget.Entry newEntry = children.get(index);
            textFieldListWidget.setFocused(true);
            // 1.19.2: setTextFieldFocused 是 package-private，用 setSelected 替代
            textFieldListWidget.setSelected(newEntry);
            textFieldListWidget.setScrollAmount(textFieldListWidget.getScrollAmount());
            if (!children.isEmpty()) {
                screen.setFocused(textFieldListWidget);
            }
        }
    }

    private static TextContext createTextContextForInsert(TextFieldListWidget textFieldListWidget, int index, AbstractSignBlockEditScreen<?> screen) {
        List<TextFieldListWidget.Entry> children = textFieldListWidget.children();
        if (index > 0 && index - 1 < children.size()) {
            TextFieldWidget prevWidget = children.get(index - 1).textFieldWidget;
            TextContext prev = getTextContextForWidget(screen, prevWidget);
            if (prev != null) {
                TextContext textContext = prev.clone();
                textContext.text = null;
                textContext.extra = null;
                return textContext;
            }
        }
        return new TextContext();
    }

    @SuppressWarnings("unchecked")
    private static TextContext getTextContextForWidget(AbstractSignBlockEditScreen<?> screen, TextFieldWidget widget) {
        try {
            java.lang.reflect.Field field = AbstractSignBlockEditScreen.class.getDeclaredField("contextToWidgetBiMap");
            field.setAccessible(true);
            com.google.common.collect.BiMap<TextContext, TextFieldWidget> biMap =
                    (com.google.common.collect.BiMap<TextContext, TextFieldWidget>) field.get(screen);
            return biMap.inverse().get(widget);
        } catch (Exception e) {
            return null;
        }
    }
}