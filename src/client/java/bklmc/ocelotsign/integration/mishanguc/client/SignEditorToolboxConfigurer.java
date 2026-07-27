package bklmc.ocelotsign.integration.mishanguc.client;

import bklmc.ocelotsign.client.PatternAndFontOverlay;
import bklmc.ocelotsign.mixin.client.AbstractSignBlockEditScreenAccessor;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import pers.solid.mishang.uc.screen.AbstractSignBlockEditScreen;
import pers.solid.mishang.uc.util.TextBridge;

/**
 * 扩展 mishanguc 告示牌编辑界面的底部工具栏，插入图案列表按钮。(1.19.2 兼容版)
 */
public final class SignEditorToolboxConfigurer {
    private SignEditorToolboxConfigurer() {
    }

    /**
     * 创建"图案与字体列表"按钮。
     */
    public static ButtonWidget createViewPatternListButton() {
        // 1.19.2: 使用旧版 ButtonWidget 构造器（无 tooltip）
        return new ButtonWidget(
                0, 0, 75, 20,
                TextBridge.translatable("message.mishanguc.view_pattern_and_font_list"),
                btn -> {
                    PatternAndFontOverlay.isVisible = true;
                    PatternAndFontOverlay.selectSidebarTop(PatternAndFontOverlay.SIDEBAR_TOP_DOCS);
                }
        );
    }

    /**
     * 将图案列表按钮插入到工具栏，并调整按钮宽度。
     */
    public static void extendToolbox(AbstractSignBlockEditScreen<?> screen, ButtonWidget viewPatternListButton) {
        ClickableWidget[] toolbox3 = new ClickableWidget[]{
                screen.setCustomValueButton,
                screen.flipButton,
                screen.finishButton,
                viewPatternListButton,
                screen.cancelButton,
                screen.rearrangeButton,
                screen.hideButton
        };

        for (ClickableWidget widget : toolbox3) {
            if (widget instanceof ButtonWidget button) {
                if (button == screen.finishButton || button == viewPatternListButton) {
                    button.setWidth(85);
                } else if (button == screen.setCustomValueButton) {
                    button.setWidth(80);
                } else {
                    button.setWidth(40);
                }
            }
        }

        ((AbstractSignBlockEditScreenAccessor) screen).ocelotsign$setToolbox3(toolbox3);
    }
}