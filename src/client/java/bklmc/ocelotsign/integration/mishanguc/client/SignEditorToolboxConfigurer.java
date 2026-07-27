package bklmc.ocelotsign.integration.mishanguc.client;

import bklmc.ocelotsign.client.PatternAndFontOverlay;
import bklmc.ocelotsign.integration.mishanguc.MishangAccess;
import bklmc.ocelotsign.mixin.client.AbstractSignBlockEditScreenAccessor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import pers.solid.mishang.uc.screen.AbstractSignBlockEditScreen;

/**
 * 扩展 mishanguc 告示牌编辑界面的底部工具栏，插入图案列表按钮。
 */
public final class SignEditorToolboxConfigurer {
    private SignEditorToolboxConfigurer() {
    }

    /**
     * 创建"图案与字体列表"按钮。
     */
    public static Button createViewPatternListButton() {
        return Button.builder(
                        MishangAccess.translatable("message.mishanguc.view_pattern_and_font_list"),
                        button -> {
                            PatternAndFontOverlay.isVisible = true;
                            // 强制互斥：避免与上一次选中的颜色拾取器/调色板叠加高亮
                            PatternAndFontOverlay.selectSidebarTop(PatternAndFontOverlay.SIDEBAR_TOP_DOCS);
                        })
                .bounds(0, 0, 75, 20)
                .tooltip(Tooltip.create(MishangAccess.translatable("message.mishanguc.view_pattern_and_font_list.description")))
                .build();
    }

    /**
     * 将图案列表按钮插入到工具栏，并调整按钮宽度。
     * colorButton 保留在 mishanguc 原生的 toolbox1 位置，不移动。
     */
    public static void extendToolbox(AbstractSignBlockEditScreen<?> screen, Button viewPatternListButton) {
        AbstractWidget[] toolbox3 = new AbstractWidget[]{
                screen.setCustomValueButton,
                screen.flipButton,
                screen.finishButton,
                viewPatternListButton,
                screen.cancelButton,
                screen.rearrangeButton,
                screen.hideButton
        };

        for (AbstractWidget widget : toolbox3) {
            if (widget instanceof Button button) {
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
