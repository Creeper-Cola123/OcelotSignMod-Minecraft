package bklmc.ocelotsign.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * 空白编辑界面 (1.19.2 兼容版)
 *
 * @see Screen
 */
public class PatternAndFontBlankScreen extends Screen {

    public PatternAndFontBlankScreen() {
        super(Text.empty());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void renderBackground(MatrixStack matrices) {
    }

    @Override
    public void close() {
        PatternAndFontOverlay.isVisible = false;
        super.close();
    }
}