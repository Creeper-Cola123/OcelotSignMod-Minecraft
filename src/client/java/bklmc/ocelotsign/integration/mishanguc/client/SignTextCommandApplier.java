package bklmc.ocelotsign.integration.mishanguc.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.ReportedException;
import pers.solid.mishang.uc.screen.AbstractSignBlockEditScreen;
import pers.solid.mishang.uc.screen.TextFieldListWidget;
import pers.solid.mishang.uc.text.SpecialDrawable;
import bklmc.ocelotsign.integration.mishanguc.MishangAccess;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析并应用告示牌编辑界面文本框中的命令语法。
 */
public final class SignTextCommandApplier {
    private static final Pattern TEXT_COMMAND_PATTERN = Pattern.compile("^-(\\w+?) (.+)$");

    private SignTextCommandApplier() {
    }

    public static void apply(TextFieldListWidget.Entry entry, AbstractSignBlockEditScreen<?> screen) {
        try {
            applyInternal(entry);
        } catch (Exception e) {
            entry.textFieldWidget.setTextColor(0xffff5555);
            entry.textFieldWidget.setTooltip(Tooltip.create(Component.literal(e.getMessage() == null ? e.toString() : e.getMessage())));
        }
        screen.changed = true;
    }

    private static void applyInternal(TextFieldListWidget.Entry entry) {
        String text = entry.textFieldWidget.getValue();
        Matcher matcher = TEXT_COMMAND_PATTERN.matcher(text);
        entry.textFieldWidget.setTooltip(null);
        entry.textFieldWidget.setTextColor(0xffe0e0e0);
        if (matcher.matches()) {
            String name = matcher.group(1);
            String value = matcher.group(2);
            switch (name) {
                case "literal" -> {
                    entry.textContext.extra = null;
                    entry.textContext.text = MishangAccess.literal(value);
                }
                case "json" -> applyJsonCommand(entry, value);
                default -> {
                    try {
                        SpecialDrawable specialDrawable = SpecialDrawable.fromStringArgs(entry.textContext, name, value);
                        if (specialDrawable == null) {
                            entry.textContext.extra = null;
                            entry.textContext.text = MishangAccess.literal(text);
                        } else if (specialDrawable != SpecialDrawable.INVALID) {
                            entry.textContext.extra = specialDrawable;
                            entry.textContext.text = MishangAccess.literal("");
                        } else {
                            entry.textFieldWidget.setTextColor(0xffff5555);
                        }
                    } catch (Exception e) {
                        entry.textContext.extra = null;
                        entry.textContext.text = MishangAccess.literal(text);
                    }
                }
            }
        } else {
            entry.textContext.extra = null;
            entry.textContext.text = MishangAccess.literal(text);
        }
    }

    /**
     * 处理 "-json {...}" 命令。
     */
    private static void applyJsonCommand(TextFieldListWidget.Entry entry, String value) {
        entry.textContext.extra = null;
        JsonElement element;
        try {
            element = JsonParser.parseString(value);
        } catch (JsonParseException | IllegalStateException e) {
            entry.textFieldWidget.setTextColor(0xffff5555);
            entry.textFieldWidget.setTooltip(Tooltip.create(Component.literal(e.getMessage())));
            entry.textContext.text = MishangAccess.literal(value);
            return;
        }
        if (!element.isJsonObject()) {
            // 不支持的格式（非对象），回退为 literal
            entry.textContext.text = MishangAccess.literal(value);
            return;
        }
        JsonObject obj = element.getAsJsonObject();

        // 提取支持的基础字段
        String textStr = obj.has("text") && obj.get("text").isJsonPrimitive()
                ? obj.get("text").getAsString()
                : "";
        Identifier fontId = obj.has("font") && obj.get("font").isJsonPrimitive()
                ? Identifier.tryParse(obj.get("font").getAsString())
                : null;
        Integer color = obj.has("color") && obj.get("color").isJsonPrimitive()
                ? parseColor(obj.get("color").getAsString())
                : null;
        boolean bold = obj.has("bold") && obj.get("bold").isJsonPrimitive()
                && obj.get("bold").getAsBoolean();
        boolean italic = obj.has("italic") && obj.get("italic").isJsonPrimitive()
                && obj.get("italic").getAsBoolean();

        // 只支持扁平 JSON（无 siblings/extra）。否则警告并保留为 literal。
        if (obj.has("with") || obj.has("extra")
                || obj.has("clickEvent") || obj.has("hoverEvent")
                || obj.has("keybind") || obj.has("translate")
                || obj.has("score") || obj.has("selector")
                || obj.has("nbt")) {
            entry.textFieldWidget.setTextColor(0xffff5555);
            entry.textFieldWidget.setTooltip(Tooltip.create(Component.literal(
                    "Unsupported JSON features. Only text/font/color/bold/italic are supported here.")));
        }

        MutableComponent resolved = MishangAccess.literal(textStr);
        Style style = Style.EMPTY
                .withBold(bold)
                .withItalic(italic)
                .withFont(new net.minecraft.network.chat.FontDescription.Resource(fontId));
        if (color != null) {
            style = style.withColor(color);
        }
        resolved.setStyle(style);
        entry.textContext.text = resolved;
    }

    /**
     * 解析简单的颜色字符串："white", "red" 等命名颜色或 "#RRGGBB" 十六进制。
     */
    private static Integer parseColor(String s) {
        if (s == null) return null;
        if (s.startsWith("#")) {
            try {
                return 0xFF000000 | Integer.parseInt(s.substring(1), 16) & 0xFFFFFF;
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return switch (s.toLowerCase()) {
            case "black" -> 0xFF000000;
            case "dark_blue" -> 0xFF0000AA;
            case "dark_green" -> 0xFF00AA00;
            case "dark_aqua" -> 0xFF00AAAA;
            case "dark_red" -> 0xFFAA0000;
            case "dark_purple" -> 0xFFAA00AA;
            case "gold" -> 0xFFFFAA00;
            case "gray" -> 0xFFAAAAAA;
            case "dark_gray" -> 0xFF555555;
            case "blue" -> 0xFF5555FF;
            case "green" -> 0xFF55FF55;
            case "aqua" -> 0xFF55FFFF;
            case "red" -> 0xFFFF5555;
            case "light_purple" -> 0xFFFF55FF;
            case "yellow" -> 0xFFFFFF55;
            case "white" -> 0xFFFFFFFF;
            default -> null;
        };
    }
}