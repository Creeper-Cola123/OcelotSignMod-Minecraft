package bklmc.ocelotsign.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * 空白编辑界面（用于承载 {@link ModelSelectionOverlay} 这类不暂停游戏的浮层）。
 *
 * <p>这是一个 {@code shouldPause() == false} 的空白 Screen，
 * 通过 {@link net.fabricmc.fabric.api.client.screen.v1.ScreenEvents}
 * 拦截渲染/输入事件，统一由 {@link ModelSelectionOverlay} 处理 UI 渲染与交互。</p>
 *
 * @see Screen
 * @see PatternAndFontBlankScreen
 */
public class ModelSelectionBlankScreen extends Screen {

    public ModelSelectionBlankScreen() {
        super(Text.empty());
    }

    @Override
    public boolean shouldPause() {
        // 不暂停游戏：让世界持续渲染
        return false;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // 不绘制默认暗色背景，让游戏世界透出
    }

    @Override
    public void close() {
        ModelSelectionOverlay.close();
        super.close();
    }
}
