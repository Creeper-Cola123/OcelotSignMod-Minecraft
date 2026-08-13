package bklmc.ocelotsign.integration.mishanguc.client;

import com.google.gson.JsonParseException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import pers.solid.mishang.uc.screen.AbstractSignBlockEditScreen;
import pers.solid.mishang.uc.screen.TextFieldListWidget;
import pers.solid.mishang.uc.text.SpecialDrawable;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析并应用告示牌编辑界面文本框中的命令语法。(1.19.2 兼容版)
 */
public final class SignTextCommandApplier {
    private static final Pattern TEXT_COMMAND_PATTERN = Pattern.compile("^-(\\w+?) (.+)$");

    private SignTextCommandApplier() {
    }

    public static void apply(TextFieldListWidget.Entry entry, AbstractSignBlockEditScreen<?> screen, TextContext textContext) {
        try {
            applyInternal(entry, textContext);
        } catch (CommandSyntaxException e) {
            entry.textFieldWidget.setEditableColor(0xffff5555);
        }
        screen.changed = true;
    }

    private static void applyInternal(TextFieldListWidget.Entry entry, TextContext textContext) throws CommandSyntaxException {
        String text = entry.textFieldWidget.getText();
        if (textContext == null) return;

        Matcher matcher = TEXT_COMMAND_PATTERN.matcher(text);
        entry.textFieldWidget.setEditableColor(0xffe0e0e0);
        if (matcher.matches()) {
            String name = matcher.group(1);
            String value = matcher.group(2);
            switch (name) {
                case "literal" -> {
                    textContext.extra = null;
                    textContext.text = TextBridge.literal(value);
                }
                case "json" -> {
                    try {
                        textContext.extra = null;
                        textContext.text = Text.Serializer.fromLenientJson(value);
                    } catch (JsonParseException | IllegalStateException e) {
                        entry.textFieldWidget.setEditableColor(0xffff5555);
                    }
                }
                default -> {
                    SpecialDrawable specialDrawable = SpecialDrawable.fromStringArgs(textContext, name, value);
                    if (specialDrawable == null) {
                        textContext.extra = null;
                        textContext.text = TextBridge.literal(text);
                    } else if (specialDrawable != SpecialDrawable.INVALID) {
                        textContext.extra = specialDrawable;
                        // 保留原始命令文本（如 -pattern xxx），这样输入框会显示命令而不是空字符串
                        textContext.text = TextBridge.literal(text);
                    } else {
                        entry.textFieldWidget.setEditableColor(0xffff5555);
                    }
                }
            }
        } else {
            textContext.extra = null;
            textContext.text = TextBridge.literal(text);
        }
    }
}