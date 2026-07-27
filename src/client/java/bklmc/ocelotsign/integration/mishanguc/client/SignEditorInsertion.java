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
    }

    public static void insertText(AbstractSignBlockEditScreen<?> screen, String text) {
        TextFieldListWidget textFieldListWidget = screen.textFieldListWidget;
        List<TextFieldListWidget.Entry> children = textFieldListWidget.children();
        int index = children.size();

        // 1.19.2: 使用 screen.addTextField
        screen.addTextField(index);
        TextFieldListWidget.Entry newEntry = textFieldListWidget.children().get(index);
        newEntry.textFieldWidget.setText(text);
        newEntry.textFieldWidget.setCursorToEnd();

        TextContext textContext = getTextContextForWidget(screen, newEntry.textFieldWidget);
        SignTextCommandApplier.apply(newEntry, screen, textContext);
        syncSignPreview(screen);
        focusOnNewEntry(screen, textFieldListWidget, index);
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