package bklmc.ocelotsign.block;

import bklmc.ocelotsign.OcelotSignMod;
import bklmc.ocelotsign.block.custom.RoadMarkBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;

import java.util.ArrayList;
import java.util.List;

/**
 * 大型路面箭头方块
 *
 * @see bklmc.ocelotsign.block.custom.RoadMarkBlock
 * @see ArrowBlocks
 */
public class ArrowBlocksLarge {
    private static final FabricBlockSettings ROAD_MARK_SETTINGS = FabricBlockSettings.of(Material.STONE).strength(0.5f).nonOpaque().collidable(false);

    /**
     * 待注册的方块和物品列表
     */
    private static final List<PendingBlockItem> PENDING_ITEMS = new ArrayList<>();

    /**
     * 已注册的 {@link BlockItem} 列表
     */
    private static final List<RegisteredItem> REGISTERED_ITEMS = new ArrayList<>();

    public static final RoadMarkBlock ADVANCE_ARROW_LEFT = directional("roadmark_large/advance_arrow_left");
    public static final RoadMarkBlock ADVANCE_ARROW_LEFT_RIGHT = directional("roadmark_large/advance_arrow_left_right");
    public static final RoadMarkBlock ADVANCE_ARROW_LEFT_UTURN = directional("roadmark_large/advance_arrow_left_uturn");
    public static final RoadMarkBlock ADVANCE_ARROW_RIGHT = directional("roadmark_large/advance_arrow_right");
    public static final RoadMarkBlock ADVANCE_ARROW_RIGHT_UTURN = directional("roadmark_large/advance_arrow_right_uturn");
    public static final RoadMarkBlock ADVANCE_ARROW_STRAIGHT = directional("roadmark_large/advance_arrow_straight");
    public static final RoadMarkBlock ADVANCE_ARROW_STRAIGHT_LEFT = directional("roadmark_large/advance_arrow_straight_left");
    public static final RoadMarkBlock ADVANCE_ARROW_STRAIGHT_LEFT_RIGHT = directional("roadmark_large/advance_arrow_straight_left_right");
    public static final RoadMarkBlock ADVANCE_ARROW_STRAIGHT_RIGHT = directional("roadmark_large/advance_arrow_straight_right");
    public static final RoadMarkBlock ADVANCE_ARROW_STRAIGHT_UTURN_LEFT = directional("roadmark_large/advance_arrow_straight_uturn_left");
    public static final RoadMarkBlock ADVANCE_ARROW_STRAIGHT_UTURN_RIGHT = directional("roadmark_large/advance_arrow_straight_uturn_right");
    public static final RoadMarkBlock ADVANCE_ARROW_UTURN_LEFT = directional("roadmark_large/advance_arrow_uturn_left");
    public static final RoadMarkBlock ADVANCE_ARROW_UTURN_RIGHT = directional("roadmark_large/advance_arrow_uturn_right");

    public static final RoadMarkBlock ARROW_LEFT = directional("roadmark_large/arrow_left");
    public static final RoadMarkBlock ARROW_LEFT_MERGE = directional("roadmark_large/arrow_left_merge");
    public static final RoadMarkBlock ARROW_LEFT_RIGHT = directional("roadmark_large/arrow_left_right");
    public static final RoadMarkBlock ARROW_LEFT_UTURN = directional("roadmark_large/arrow_left_uturn");
    public static final RoadMarkBlock ARROW_RIGHT = directional("roadmark_large/arrow_right");
    public static final RoadMarkBlock ARROW_RIGHT_MERGE = directional("roadmark_large/arrow_right_merge");
    public static final RoadMarkBlock ARROW_RIGHT_UTURN = directional("roadmark_large/arrow_right_uturn");
    public static final RoadMarkBlock ARROW_STRAIGHT = directional("roadmark_large/arrow_straight");
    public static final RoadMarkBlock ARROW_STRAIGHT_LEFT = directional("roadmark_large/arrow_straight_left");
    public static final RoadMarkBlock ARROW_STRAIGHT_LEFT_RIGHT = directional("roadmark_large/arrow_straight_left_right");
    public static final RoadMarkBlock ARROW_STRAIGHT_RIGHT = directional("roadmark_large/arrow_straight_right");
    public static final RoadMarkBlock ARROW_STRAIGHT_UTURN_LEFT = directional("roadmark_large/arrow_straight_uturn_left");
    public static final RoadMarkBlock ARROW_STRAIGHT_UTURN_RIGHT = directional("roadmark_large/arrow_straight_uturn_right");
    public static final RoadMarkBlock ARROW_UTURN_LEFT = directional("roadmark_large/arrow_uturn_left");
    public static final RoadMarkBlock ARROW_UTURN_RIGHT = directional("roadmark_large/arrow_uturn_right");

    public static final RoadMarkBlock ARROW_PROHIBITED = directional("roadmark_large/arrow_prohibited");
    public static final RoadMarkBlock PROHIBITED_ARROW_LEFT = directional("roadmark_large/prohibited_arrow_left");
    public static final RoadMarkBlock PROHIBITED_ARROW_LEFT_RIGHT = directional("roadmark_large/prohibited_arrow_left_right");
    public static final RoadMarkBlock PROHIBITED_ARROW_LEFT_UTURN = directional("roadmark_large/prohibited_arrow_left_uturn");
    public static final RoadMarkBlock PROHIBITED_ARROW_RIGHT = directional("roadmark_large/prohibited_arrow_right");
    public static final RoadMarkBlock PROHIBITED_ARROW_RIGHT_UTURN = directional("roadmark_large/prohibited_arrow_right_uturn");
    public static final RoadMarkBlock PROHIBITED_ARROW_STRAIGHT = directional("roadmark_large/prohibited_arrow_straight");
    public static final RoadMarkBlock PROHIBITED_ARROW_STRAIGHT_LEFT = directional("roadmark_large/prohibited_arrow_straight_left");
    public static final RoadMarkBlock PROHIBITED_ARROW_STRAIGHT_LEFT_RIGHT = directional("roadmark_large/prohibited_arrow_straight_left_right");
    public static final RoadMarkBlock PROHIBITED_ARROW_STRAIGHT_RIGHT = directional("roadmark_large/prohibited_arrow_straight_right");
    public static final RoadMarkBlock PROHIBITED_ARROW_STRAIGHT_UTURN_LEFT = directional("roadmark_large/prohibited_arrow_straight_uturn_left");
    public static final RoadMarkBlock PROHIBITED_ARROW_STRAIGHT_UTURN_RIGHT = directional("roadmark_large/prohibited_arrow_straight_uturn_right");
    public static final RoadMarkBlock PROHIBITED_ARROW_UTURN_LEFT = directional("roadmark_large/prohibited_arrow_uturn_left");
    public static final RoadMarkBlock PROHIBITED_ARROW_UTURN_RIGHT = directional("roadmark_large/prohibited_arrow_uturn_right");

    public static final RoadMarkBlock ORANGE_ARROW_LEFT = directional("roadmark_large/orange_arrow_left");
    public static final RoadMarkBlock ORANGE_ARROW_LEFT_RIGHT = directional("roadmark_large/orange_arrow_left_right");
    public static final RoadMarkBlock ORANGE_ARROW_LEFT_UTURN = directional("roadmark_large/orange_arrow_left_uturn");
    public static final RoadMarkBlock ORANGE_ARROW_RIGHT = directional("roadmark_large/orange_arrow_right");
    public static final RoadMarkBlock ORANGE_ARROW_RIGHT_UTURN = directional("roadmark_large/orange_arrow_right_uturn");
    public static final RoadMarkBlock ORANGE_ARROW_STRAIGHT = directional("roadmark_large/orange_arrow_straight");
    public static final RoadMarkBlock ORANGE_ARROW_STRAIGHT_LEFT = directional("roadmark_large/orange_arrow_straight_left");
    public static final RoadMarkBlock ORANGE_ARROW_STRAIGHT_LEFT_RIGHT = directional("roadmark_large/orange_arrow_straight_left_right");
    public static final RoadMarkBlock ORANGE_ARROW_STRAIGHT_RIGHT = directional("roadmark_large/orange_arrow_straight_right");
    public static final RoadMarkBlock ORANGE_ARROW_STRAIGHT_UTURN_LEFT = directional("roadmark_large/orange_arrow_straight_uturn_left");
    public static final RoadMarkBlock ORANGE_ARROW_STRAIGHT_UTURN_RIGHT = directional("roadmark_large/orange_arrow_straight_uturn_right");
    public static final RoadMarkBlock ORANGE_ARROW_UTURN_LEFT = directional("roadmark_large/orange_arrow_uturn_left");
    public static final RoadMarkBlock ORANGE_ARROW_UTURN_RIGHT = directional("roadmark_large/orange_arrow_uturn_right");

    public static final RoadMarkBlock DECELERATION_CROSSROADS = directional("roadmark_large/deceleration_crossroads");
    public static final RoadMarkBlock SPEED_BUMP = directional("roadmark_large/speed_bump");
    public static final RoadMarkBlock YIELD = directional("roadmark_large/yield");
    public static final RoadMarkBlock DISTANCE_CONFIRM = directional("roadmark_large/distance_confirm");

    /**
     * 创建方向性箭头方块
     *
     * @param name 方块名称
     * @return 注册后的箭头方块
     */
    private static RoadMarkBlock directional(String name) {
        RoadMarkBlock block = RoadMarkBlock.createDirectionalFacing(new Identifier(OcelotSignMod.MOD_ID, "block/" + name), ROAD_MARK_SETTINGS);
        return registerWithItem(name, block);
    }

    /**
     * 注册方块并创建对应的物品
     *
     * @param id    方块ID
     * @param block 方块实例
     * @return 已注册的方块
     */
    private static RoadMarkBlock registerWithItem(String id, RoadMarkBlock block) {
        RoadMarkBlock registeredBlock = Registry.register(Registries.BLOCK, new Identifier(OcelotSignMod.MOD_ID, id), block);
        registerBlockItem(id, registeredBlock);
        return registeredBlock;
    }

    /**
     * 注册方块物品
     * <p>此方法将方块物品添加到待注册列表，物品将在调用 {@link #registerBlockItems()} 时统一注册。
     *
     * @param id    方块ID
     * @param block 方块实例
     */
    private static void registerBlockItem(String id, Block block) {
        PENDING_ITEMS.add(new PendingBlockItem(block, new Identifier(OcelotSignMod.MOD_ID, id)));
    }

    /**
     * 将待注册的 {@link BlockItem} 批量注册到游戏并添加到 {@link ItemGroup} 中。
     * <p>此方法确保 {@link ItemGroup} 已被正确初始化后才将物品添加到 {@code ModItemGroups} 的 {@code appendItems} 中。
     */
    public static void registerBlockItems() {
        if (REGISTERED_ITEMS.size() == PENDING_ITEMS.size()) {
            return;
        }
        FabricItemSettings settings = new FabricItemSettings();
        for (PendingBlockItem pending : PENDING_ITEMS) {
            BlockItem item = new BlockItem(pending.block, settings);
            Item registered = Registry.register(Registries.ITEM, pending.id, item);
            REGISTERED_ITEMS.add(new RegisteredItem(pending.block, registered));
        }
        PENDING_ITEMS.clear();
        OcelotSignMod.LOGGER.info("Registered {} large arrow block items",
                REGISTERED_ITEMS.size());
    }

    /**
     * 根据方块获取对应的物品，若未找到则返回 {@code null}
     */
    public static Item asItem(Block block) {
        for (RegisteredItem registered : REGISTERED_ITEMS) {
            if (registered.block == block) {
                return registered.item;
            }
        }
        return null;
    }

    private record PendingBlockItem(Block block, Identifier id) {
    }

    private record RegisteredItem(Block block, Item item) {
    }

    /**
     * 注册所有大型箭头方块
     */
    public static void register() {
        OcelotSignMod.LOGGER.info("Registering Large Arrow Blocks for " + OcelotSignMod.MOD_ID);
    }
}
