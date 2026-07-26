package bklmc.ocelotsign.integration.mishanguc.client;

import net.minecraft.resources.Identifier;
import pers.solid.mishang.uc.screen.AbstractSignBlockEditScreen;
import pers.solid.mishang.uc.screen.TextFieldListWidget;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.text.TextureSpecialDrawable;

/**
 * 向 mishanguc 告示牌编辑界面插入纹理或文本行。
 */
public final class SignEditorInsertion {
    private SignEditorInsertion() {
    }

    /**
     * 插入一个纹理条目到编辑界面末尾。
     */
    public static void insertTexture(AbstractSignBlockEditScreen<?> screen, Identifier identifier) {
        TextFieldListWidget textFieldListWidget = screen.textFieldListWidget;
        int index = textFieldListWidget.children().size();

        TextContext textContext;
        if (index > 0) {
            TextContext prev = textFieldListWidget.children().get(index - 1).textContext;
            textContext = prev.clone();
            textContext.text = null;
            textContext.extra = null;
        } else {
            textContext = new TextContext();
        }

        TextureSpecialDrawable textureDrawable = new TextureSpecialDrawable(identifier, textContext);
        textContext.extra = textureDrawable;

        // addTextField 位于 TextFieldListWidget 上，而不是编辑界面上。
        TextFieldListWidget.Entry newEntry = textFieldListWidget.addTextField(index, textContext, false);
        SignTextCommandApplier.apply(newEntry, screen);
        syncSignPreview(screen);
        focusOnNewEntry(screen, textFieldListWidget, index);
    }

    /**
     * 插入一段文本内容到编辑界面末尾。
     */
    public static void insertText(AbstractSignBlockEditScreen<?> screen, String text) {
        TextFieldListWidget textFieldListWidget = screen.textFieldListWidget;
        int index = textFieldListWidget.children().size();

        TextContext textContext;
        if (index > 0) {
            TextContext prev = textFieldListWidget.children().get(index - 1).textContext;
            textContext = prev.clone();
            textContext.text = null;
            textContext.extra = null;
        } else {
            textContext = new TextContext();
        }

        // addTextField 位于 TextFieldListWidget 上，而不是编辑界面上。
        TextFieldListWidget.Entry newEntry = textFieldListWidget.addTextField(index, textContext, false);
        newEntry.textFieldWidget.setValue(text);
        newEntry.textFieldWidget.moveCursorToEnd(false);
        SignTextCommandApplier.apply(newEntry, screen);
        syncSignPreview(screen);
        focusOnNewEntry(screen, textFieldListWidget, index);
    }

    private static void syncSignPreview(AbstractSignBlockEditScreen<?> screen) {
        screen.entity.setChanged();
    }

    private static void focusOnNewEntry(
            AbstractSignBlockEditScreen<?> screen,
            TextFieldListWidget textFieldListWidget,
            int index
    ) {
        TextFieldListWidget.Entry newEntry = textFieldListWidget.children().get(index);
        textFieldListWidget.setFocused(newEntry, false, false);
        textFieldListWidget.setScrollAmount(textFieldListWidget.scrollAmount());
        if (!textFieldListWidget.children().isEmpty()) {
            screen.setFocused(textFieldListWidget);
        }
    }
}
