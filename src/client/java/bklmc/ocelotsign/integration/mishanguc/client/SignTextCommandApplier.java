package bklmc.ocelotsign.integration.mishanguc.client;

import com.google.gson.JsonParseException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.gui.tooltip.Tooltip;
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
 * 解析并应用告示牌编辑界面文本框中的命令语法。
 * 支持 {@code -literal}、{@code -json} 以及 mishanguc 的图案类命令。
 */
public final class SignTextCommandApplier {
    /**
     * 匹配形如 {@code -<命令名> <内容>} 的文本。
     */
    private static final Pattern TEXT_COMMAND_PATTERN = Pattern.compile("^-(\\w+?) (.+)$");

    private SignTextCommandApplier() {
    }

    /**
     * 将文本框内容作为命令解析并应用到文本上下文。
     *
     * @param entry  文本框条目。
     * @param screen 所属编辑界面，用于标记变更状态。
     */
    public static void apply(TextFieldListWidget.Entry entry, AbstractSignBlockEditScreen<?> screen) {
        try {
            applyInternal(entry);
        } catch (CommandSyntaxException e) {
            entry.textFieldWidget.setEditableColor(0xffff5555);
            entry.textFieldWidget.setTooltip(Tooltip.of(Text.literal(e.getRawMessage().getString())));
        }
        screen.changed = true;
    }

    private static void applyInternal(TextFieldListWidget.Entry entry) throws CommandSyntaxException {
        String text = entry.textFieldWidget.getText();
        TextContext textContext = entry.textContext;
        Matcher matcher = TEXT_COMMAND_PATTERN.matcher(text);
        entry.textFieldWidget.setTooltip(null);
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
                        entry.textFieldWidget.setTooltip(Tooltip.of(Text.literal(e.getMessage())));
                    }
                }
                default -> {
                    SpecialDrawable specialDrawable = SpecialDrawable.fromStringArgs(textContext, name, value);
                    if (specialDrawable == null) {
                        textContext.extra = null;
                        textContext.text = TextBridge.literal(text);
                    } else if (specialDrawable != SpecialDrawable.INVALID) {
                        textContext.extra = specialDrawable;
                        textContext.text = TextBridge.literal("");
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
