package bklmc.ocelotsign.mixin.client;

import bklmc.ocelotsign.integration.mishanguc.client.SignEditorInsertion;
import bklmc.ocelotsign.integration.mishanguc.client.SignEditorToolboxConfigurer;
import bklmc.ocelotsign.mixin_interfaces.ISignEditorExtension;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.solid.mishang.uc.screen.AbstractSignBlockEditScreen;

/**
 * 通过 Mixin 扩展 mishanguc 的 {@link AbstractSignBlockEditScreen}：
 * <ul>
 *   <li>注入图案列表按钮至底部工具栏；</li>
 *   <li>实现 {@link ISignEditorExtension} 接口以支持从外部插入纹理或文本。</li>
 * </ul>
 */
@Mixin(value = AbstractSignBlockEditScreen.class, remap = false)
public abstract class AbstractSignBlockEditScreenMixin implements ISignEditorExtension {

    /**
     * 图案与字体列表按钮。
     */
    @Unique
    private final ButtonWidget viewPatternListButton = SignEditorToolboxConfigurer.createViewPatternListButton();

    /**
     * 初始化工具栏，插入图案列表按钮。
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        SignEditorToolboxConfigurer.extendToolbox(
                (AbstractSignBlockEditScreen<?>) (Object) this,
                viewPatternListButton
        );
    }

    @Override
    public void ocelotsign$insertTexture(Identifier identifier) {
        SignEditorInsertion.insertTexture((AbstractSignBlockEditScreen<?>) (Object) this, identifier);
    }

    @Override
    public void ocelotsign$insertText(String text) {
        SignEditorInsertion.insertText((AbstractSignBlockEditScreen<?>) (Object) this, text);
    }
}
