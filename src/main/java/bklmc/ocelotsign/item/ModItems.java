package bklmc.ocelotsign.item;

import bklmc.ocelotsign.OcelotSignMod;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

/**
 * 模组物品注册中心
 *
 * @see ItemGroupTabItem
 * @see ModItemGroups
 */
public class ModItems {

    private static RegistryKey<Item> getItemKey(String id) {
        return RegistryKey.of(Registries.ITEM.getKey(), Identifier.of(OcelotSignMod.MOD_ID, id));
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, getItemKey(name), item);
    }

    public static final ItemGroupTabItem ROAD_SIGNS_ICON = new ItemGroupTabItem(new Item.Settings().registryKey(getItemKey("item_group/road_signs")));
    public static final ItemGroupTabItem WALL_ROAD_SIGNS_ICON = new ItemGroupTabItem(new Item.Settings().registryKey(getItemKey("item_group/wall_road_signs")));
    public static final ItemGroupTabItem PILLARS_ICON = new ItemGroupTabItem(new Item.Settings().registryKey(getItemKey("item_group/pillars")));

    public static void registerModItems() {
        OcelotSignMod.LOGGER.info("Registering Mod Items for " + OcelotSignMod.MOD_ID);
        registerItem("item_group/road_signs", ROAD_SIGNS_ICON);
        registerItem("item_group/wall_road_signs", WALL_ROAD_SIGNS_ICON);
        registerItem("item_group/pillars", PILLARS_ICON);
    }

    private static void registerBlockItems(String id, Block block) {
        Registry.register(Registries.ITEM, getItemKey(id),
                new BlockItem(block, new Item.Settings().registryKey(getItemKey(id))));
    }

    private static Block registerWithItem(String id, Block block) {
        Block registeredBlock = Registry.register(Registries.BLOCK, Identifier.of(OcelotSignMod.MOD_ID, id), block);
        registerBlockItems(id, registeredBlock);
        return registeredBlock;
    }
}
