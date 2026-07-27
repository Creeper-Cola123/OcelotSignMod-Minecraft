package bklmc.ocelotsign.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import pers.solid.mishang.uc.blockentity.BlockEntityWithText;
import pers.solid.mishang.uc.screen.AbstractSignBlockEditScreen;
import pers.solid.mishang.uc.text.TextContext;

import java.util.List;

/**
 * 指示牌编辑界面 (1.19.2 兼容版)
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
        // 1.19.2 使用旧版 ButtonWidget 构造器
        this.patternAndFontListButton = new ButtonWidget(
                width / 2 - 100, height - 30, 100, 20,
                Text.translatable("message.ocelotsignmod.pattern_and_font_list"),
                button -> openPatternAndFontList()
        );
    }

    private void openPatternAndFontList() {
        PatternAndFontOverlay.isVisible = true;
        PatternAndFontOverlay.selectSidebarTop(PatternAndFontOverlay.SIDEBAR_TOP_DOCS);
    }

    @Override
    protected void init() {
        super.init();
        // 1.19.2 使用 setX/setY 而非 setPosition
        patternAndFontListButton.x = width / 2 - 100;
        patternAndFontListButton.y = height - 30;
        finishButton.x = width / 2;
        finishButton.y = height - 30;
        finishButton.setWidth(80);
        addDrawableChild(patternAndFontListButton);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
    }
}