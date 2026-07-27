package bklmc.ocelotsign.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * 空白编辑界面
 *
 * @see Screen
 */
public class PatternAndFontBlankScreen extends Screen {

    public PatternAndFontBlankScreen() {
        super(Component.empty());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void renderBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
    }

    @Override
    public void onClose() {
        PatternAndFontOverlay.isVisible = false;
        super.onClose();
    }
}
