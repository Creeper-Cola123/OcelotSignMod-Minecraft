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
        TextContext textContext = getTextContextFromEntry(entry);
        if (textContext == null) return;
        apply(entry, screen, textContext);
    }

    /**
     * 将文本框内容作为命令解析并应用到文本上下文。
     *
     * @param entry       文本框条目。
     * @param screen      所属编辑界面，用于标记变更状态。
     * @param textContext 要应用的文本上下文。
     */
    public static void apply(TextFieldListWidget.Entry entry, AbstractSignBlockEditScreen<?> screen, TextContext textContext) {
        try {
            applyInternal(entry, textContext);
        } catch (CommandSyntaxException e) {
            entry.textFieldWidget.setEditableColor(0xffff5555);
            entry.textFieldWidget.setTooltip(Tooltip.of(Text.literal(e.getRawMessage().getString())));
        }
        screen.changed = true;
    }

    /**
     * 从 Entry 中获取 TextContext。优先使用 textContext 字段，如果不存在则通过反射从 screen 获取。
     */
    private static TextContext getTextContextFromEntry(TextFieldListWidget.Entry entry) {
        // 尝试直接访问 textContext 字段（1.20.1+ 版本）
        try {
            java.lang.reflect.Field textContextField = TextFieldListWidget.Entry.class.getDeclaredField("textContext");
            textContextField.setAccessible(true);
            return (TextContext) textContextField.get(entry);
        } catch (Exception e) {
            // 如果字段不存在，通过反射从 screen 获取
        }
        return getTextContextFromScreen(entry);
    }

    /**
     * 通过反射从 AbstractSignBlockEditScreen 的 contextToWidgetBiMap 中获取 Entry 对应的 TextContext。
     */
    private static TextContext getTextContextFromScreen(TextFieldListWidget.Entry entry) {
        try {
            // 尝试获取 screen 引用
            java.lang.reflect.Field parentField = TextFieldListWidget.Entry.class.getDeclaredField("parent");
            parentField.setAccessible(true);
            TextFieldListWidget parent = (TextFieldListWidget) parentField.get(entry);
            if (parent == null) return null;

            java.lang.reflect.Field screenField = TextFieldListWidget.class.getDeclaredField("screen");
            screenField.setAccessible(true);
            AbstractSignBlockEditScreen<?> screen = (AbstractSignBlockEditScreen<?>) screenField.get(parent);
            if (screen == null) return null;

            java.lang.reflect.Field field = AbstractSignBlockEditScreen.class.getDeclaredField("contextToWidgetBiMap");
            field.setAccessible(true);
            Object biMapObj = field.get(screen);
            if (biMapObj == null) return null;

            // 遍历 BiMap 的 entries
            java.lang.reflect.Method entriesMethod = biMapObj.getClass().getMethod("entries");
            @SuppressWarnings("unchecked")
            Iterable<?> entries = (Iterable<?>) entriesMethod.invoke(biMapObj);
            for (Object e : entries) {
                java.lang.reflect.Method getValue = e.getClass().getMethod("getValue");
                Object widget = getValue.invoke(e);
                if (widget == entry.textFieldWidget) {
                    java.lang.reflect.Method getKey = e.getClass().getMethod("getKey");
                    return (TextContext) getKey.invoke(e);
                }
            }
        } catch (Exception e) {
            // 忽略异常
        }
        return null;
    }

    private static void applyInternal(TextFieldListWidget.Entry entry, TextContext textContext) throws CommandSyntaxException {
        String text = entry.textFieldWidget.getText();
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