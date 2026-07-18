package bklmc.ocelotsign.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import pers.solid.mishang.uc.blockentity.BlockEntityWithText;
import pers.solid.mishang.uc.screen.AbstractSignBlockEditScreen;
import pers.solid.mishang.uc.text.TextContext;

import java.util.List;

/**
 * 指示牌编辑界面
 *
 * @param <T> 关联的方块实体类型
 * @see AbstractSignBlockEditScreen
 * @see PatternAndFontOverlay
 */
@Environment(EnvType.CLIENT)
public class OcelotSignEditScreen<T extends BlockEntityWithText> extends AbstractSignBlockEditScreen<T> {
    public final ButtonWidget patternAndFontListButton;

    public OcelotSignEditScreen(T entity, BlockPos blockPos, List<TextContext> textContextsEditing) {
        super(entity, blockPos, textContextsEditing);
        this.patternAndFontListButton = ButtonWidget.builder(
                Text.translatable("message.ocelotsign.pattern_and_font_list"),
                button -> openPatternAndFontList()
        ).size(100, 20).build();
    }

    /**
     * 打开图样和字体列表悬浮层。
     */
    private void openPatternAndFontList() {
        PatternAndFontOverlay.isVisible = true;
        // 强制互斥：避免上一次会话残留的任意顶层项标志导致多个侧边栏项同时高亮
        PatternAndFontOverlay.selectSidebarTop(PatternAndFontOverlay.SIDEBAR_TOP_DOCS);
    }

    @Override
    protected void init() {
        super.init();
        patternAndFontListButton.setPosition(width / 2 - 100, height - 30);
        finishButton.setPosition(width / 2, height - 30);
        finishButton.setWidth(80);
        addDrawableChild(patternAndFontListButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }
}
