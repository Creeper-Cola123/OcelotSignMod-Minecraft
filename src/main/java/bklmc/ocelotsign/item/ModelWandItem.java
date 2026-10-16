package bklmc.ocelotsign.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * 模型切换魔杖
 *
 * @see CustomModelBlockItem
 * @see bklmc.ocelotsign.blockentity.CustomModelBlockEntity
 */
public class ModelWandItem extends Item {
    /** 模型魔杖的工具 ID */
    public static final String TOOL_ID = "model_wand";

    public ModelWandItem(Settings settings) {
        super(settings);
    }

    /**
     * 判断物品是否为模型魔杖。
     *
     * @param stack 待判断的物品栈
     * @return 若是模型魔杖则返回 {@code true}
     */
    public static boolean isModelWand(ItemStack stack) {
        return stack.getItem() instanceof ModelWandItem;
    }
}
