package bklmc.ocelotsign.mixin.client;

import net.minecraft.client.gui.widget.ClickableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import pers.solid.mishang.uc.screen.AbstractSignBlockEditScreen;

/**
 * 通过 Accessor 访问 {@link AbstractSignBlockEditScreen#toolbox3}。
 */
@Mixin(value = AbstractSignBlockEditScreen.class, remap = false)
public interface AbstractSignBlockEditScreenAccessor {
    @Mutable
    @Accessor("toolbox3")
    void ocelotsign$setToolbox3(ClickableWidget[] toolbox3);
}
