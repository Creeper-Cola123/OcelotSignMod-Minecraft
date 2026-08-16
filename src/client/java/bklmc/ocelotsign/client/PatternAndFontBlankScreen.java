package bklmc.ocelotsign.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * 空白编辑界面
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

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    @Override
    public void close() {
        PatternAndFontOverlay.isVisible = false;
        super.close();
    }
}
