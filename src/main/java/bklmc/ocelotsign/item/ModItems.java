package bklmc.ocelotsign.item;

import bklmc.ocelotsign.OcelotSignMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * 模组物品注册中心
 *
 * @see ItemGroupTabItem
 * @see ModItemGroups
 */
public class ModItems {

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(OcelotSignMod.MOD_ID, name), item);
    }

    public static final ItemGroupTabItem ROAD_SIGNS_ICON =
            new ItemGroupTabItem(new Item.Properties().setId(OcelotSignMod.itemKey("item_group/road_signs")));
    public static final ItemGroupTabItem WALL_ROAD_SIGNS_ICON =
            new ItemGroupTabItem(new Item.Properties().setId(OcelotSignMod.itemKey("item_group/wall_road_signs")));
    public static final ItemGroupTabItem PILLARS_ICON =
            new ItemGroupTabItem(new Item.Properties().setId(OcelotSignMod.itemKey("item_group/pillars")));

    public static void registerModItems() {
        OcelotSignMod.LOGGER.info("Registering Mod Items for " + OcelotSignMod.MOD_ID);
        registerItem("item_group/road_signs", ROAD_SIGNS_ICON);
        registerItem("item_group/wall_road_signs", WALL_ROAD_SIGNS_ICON);
        registerItem("item_group/pillars", PILLARS_ICON);
    }

    private static void registerBlockItems(String id, Block block) {
        Registry.register(BuiltInRegistries.ITEM, OcelotSignMod.id(id),
                new BlockItem(block, new Item.Properties()
                        .setId(OcelotSignMod.itemKey(id))
                        .useBlockDescriptionPrefix()));
    }

    private static Block registerWithItem(String id, Block block) {
        Block registeredBlock = Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(OcelotSignMod.MOD_ID, id), block);
        registerBlockItems(id, registeredBlock);
        return registeredBlock;
    }
}
