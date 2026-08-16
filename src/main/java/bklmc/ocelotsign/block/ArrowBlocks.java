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
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

/**
 * 道路箭头方块注册中心
 *
 * @see bklmc.ocelotsign.block.custom.RoadMarkBlock
 */
public class ArrowBlocks {
    private static final FabricBlockSettings ROAD_MARK_SETTINGS = FabricBlockSettings.of(Material.STONE).strength(0.5f).nonOpaque().collidable(false);

    /**
     * 待注册的方块物品列表。每项包含一个已注册的方块和它在物品注册表中应使用的标识符。
     * <p>实际的 {@link BlockItem} 注册会延迟到 {@link #registerBlockItems()}
     * 中执行；具体分类由 {@code ModItemGroups} 中的 {@code ROAD_MARKS} 通过
     * {@code appendItems} 显式列举。</p>
     */
    private static final List<PendingBlockItem> PENDING_ITEMS = new ArrayList<>();

    /**
     * 已注册的 {@link BlockItem} 列表，键是方块，值是对应的物品。
     */
    private static final List<RegisteredItem> REGISTERED_ITEMS = new ArrayList<>();

    public static final RoadMarkBlock ADVANCE_ARROW_LEFT = directional("roadmark/advance_arrow_left");
    public static final RoadMarkBlock ADVANCE_ARROW_LEFT_RIGHT = directional("roadmark/advance_arrow_left_right");
    public static final RoadMarkBlock ADVANCE_ARROW_LEFT_UTURN = directional("roadmark/advance_arrow_left_uturn");
    public static final RoadMarkBlock ADVANCE_ARROW_RIGHT = directional("roadmark/advance_arrow_right");
    public static final RoadMarkBlock ADVANCE_ARROW_RIGHT_UTURN = directional("roadmark/advance_arrow_right_uturn");
    public static final RoadMarkBlock ADVANCE_ARROW_STRAIGHT = directional("roadmark/advance_arrow_straight");
    public static final RoadMarkBlock ADVANCE_ARROW_STRAIGHT_LEFT = directional("roadmark/advance_arrow_straight_left");
    public static final RoadMarkBlock ADVANCE_ARROW_STRAIGHT_LEFT_RIGHT = directional("roadmark/advance_arrow_straight_left_right");
    public static final RoadMarkBlock ADVANCE_ARROW_STRAIGHT_RIGHT = directional("roadmark/advance_arrow_straight_right");
    public static final RoadMarkBlock ADVANCE_ARROW_STRAIGHT_UTURN_LEFT = directional("roadmark/advance_arrow_straight_uturn_left");
    public static final RoadMarkBlock ADVANCE_ARROW_STRAIGHT_UTURN_RIGHT = directional("roadmark/advance_arrow_straight_uturn_right");
    public static final RoadMarkBlock ADVANCE_ARROW_UTURN_LEFT = directional("roadmark/advance_arrow_uturn_left");
    public static final RoadMarkBlock ADVANCE_ARROW_UTURN_RIGHT = directional("roadmark/advance_arrow_uturn_right");

    public static final RoadMarkBlock ORANGE_ARROW_LEFT = directional("roadmark/orange_arrow_left");
    public static final RoadMarkBlock ORANGE_ARROW_LEFT_RIGHT = directional("roadmark/orange_arrow_left_right");
    public static final RoadMarkBlock ORANGE_ARROW_LEFT_UTURN = directional("roadmark/orange_arrow_left_uturn");
    public static final RoadMarkBlock ORANGE_ARROW_RIGHT = directional("roadmark/orange_arrow_right");
    public static final RoadMarkBlock ORANGE_ARROW_RIGHT_UTURN = directional("roadmark/orange_arrow_right_uturn");
    public static final RoadMarkBlock ORANGE_ARROW_STRAIGHT = directional("roadmark/orange_arrow_straight");
    public static final RoadMarkBlock ORANGE_ARROW_STRAIGHT_LEFT = directional("roadmark/orange_arrow_straight_left");
    public static final RoadMarkBlock ORANGE_ARROW_STRAIGHT_LEFT_RIGHT = directional("roadmark/orange_arrow_straight_left_right");
    public static final RoadMarkBlock ORANGE_ARROW_STRAIGHT_RIGHT = directional("roadmark/orange_arrow_straight_right");
    public static final RoadMarkBlock ORANGE_ARROW_STRAIGHT_UTURN_LEFT = directional("roadmark/orange_arrow_straight_uturn_left");
    public static final RoadMarkBlock ORANGE_ARROW_STRAIGHT_UTURN_RIGHT = directional("roadmark/orange_arrow_straight_uturn_right");
    public static final RoadMarkBlock ORANGE_ARROW_UTURN_LEFT = directional("roadmark/orange_arrow_uturn_left");
    public static final RoadMarkBlock ORANGE_ARROW_UTURN_RIGHT = directional("roadmark/orange_arrow_uturn_right");

    public static final RoadMarkBlock ARROW_PROHIBITED = directional("roadmark/arrow_prohibited");
    public static final RoadMarkBlock PROHIBITED_ARROW_LEFT = directional("roadmark/prohibited_arrow_left");
    public static final RoadMarkBlock PROHIBITED_ARROW_LEFT_RIGHT = directional("roadmark/prohibited_arrow_left_right");
    public static final RoadMarkBlock PROHIBITED_ARROW_LEFT_UTURN = directional("roadmark/prohibited_arrow_left_uturn");
    public static final RoadMarkBlock PROHIBITED_ARROW_RIGHT = directional("roadmark/prohibited_arrow_right");
    public static final RoadMarkBlock PROHIBITED_ARROW_RIGHT_UTURN = directional("roadmark/prohibited_arrow_right_uturn");
    public static final RoadMarkBlock PROHIBITED_ARROW_STRAIGHT = directional("roadmark/prohibited_arrow_straight");
    public static final RoadMarkBlock PROHIBITED_ARROW_STRAIGHT_LEFT = directional("roadmark/prohibited_arrow_straight_left");
    public static final RoadMarkBlock PROHIBITED_ARROW_STRAIGHT_LEFT_RIGHT = directional("roadmark/prohibited_arrow_straight_left_right");
    public static final RoadMarkBlock PROHIBITED_ARROW_STRAIGHT_RIGHT = directional("roadmark/prohibited_arrow_straight_right");
    public static final RoadMarkBlock PROHIBITED_ARROW_STRAIGHT_UTURN_LEFT = directional("roadmark/prohibited_arrow_straight_uturn_left");
    public static final RoadMarkBlock PROHIBITED_ARROW_STRAIGHT_UTURN_RIGHT = directional("roadmark/prohibited_arrow_straight_uturn_right");
    public static final RoadMarkBlock PROHIBITED_ARROW_UTURN_LEFT = directional("roadmark/prohibited_arrow_uturn_left");
    public static final RoadMarkBlock PROHIBITED_ARROW_UTURN_RIGHT = directional("roadmark/prohibited_arrow_uturn_right");

    public static final RoadMarkBlock SPEED_BUMP = directional("roadmark/speed_bump");
    public static final RoadMarkBlock YIELD = directional("roadmark/yield");
    public static final RoadMarkBlock DISTANCE_CONFIRM = directional("roadmark/distance_confirm");

    /**
     * 创建具有方向属性的道路标线方块。
     *
     * @param name 方块名称
     * @return 已注册的道路标线方块
     */
    private static RoadMarkBlock directional(String name) {
        RoadMarkBlock block = RoadMarkBlock.createDirectionalFacing(new Identifier(OcelotSignMod.MOD_ID, "block/" + name), ROAD_MARK_SETTINGS);
        return registerWithItem(name, block);
    }

    /**
     * 注册方块并同时注册对应物品。
     *
     * @param id    注册 ID
     * @param block 待注册的方块
     * @return 已注册的方块
     */
    private static RoadMarkBlock registerWithItem(String id, RoadMarkBlock block) {
        RoadMarkBlock registeredBlock = Registry.register(Registry.BLOCK, new Identifier(OcelotSignMod.MOD_ID, id), block);
        registerBlockItem(id, registeredBlock);
        return registeredBlock;
    }

    /**
     * 注册方块对应的物品。
     * <p>本方法只会把方块和物品 ID 加入到待注册列表，{@link BlockItem} 的实际注册推迟到
     * {@link #registerBlockItems()} 中执行。</p>
     *
     * @param id    注册 ID
     * @param block 对应的方块
     */
    private static void registerBlockItem(String id, Block block) {
        PENDING_ITEMS.add(new PendingBlockItem(block, new Identifier(OcelotSignMod.MOD_ID, id)));
    }

    /**
     * 把本类所有待注册的方块以 {@link BlockItem} 形式注册到物品注册表中。
     * <p>本方法不会为物品设置 {@link ItemGroup}，避免污染其他模组的物品栏；具体分类由
     * {@code ModItemGroups} 中的对应 {@link ItemGroup} 通过 {@code appendItems} 显式列举。</p>
     */
    public static void registerBlockItems() {
        if (REGISTERED_ITEMS.size() == PENDING_ITEMS.size()) {
            return;
        }
        FabricItemSettings settings = new FabricItemSettings();
        for (PendingBlockItem pending : PENDING_ITEMS) {
            BlockItem item = new BlockItem(pending.block, settings);
            Item registered = Registry.register(Registry.ITEM, pending.id, item);
            REGISTERED_ITEMS.add(new RegisteredItem(pending.block, registered));
        }
        PENDING_ITEMS.clear();
        OcelotSignMod.LOGGER.info("Registered {} arrow block items",
                REGISTERED_ITEMS.size());
    }

    /**
     * 根据方块查找对应的物品。如果方块尚未完成物品注册则返回 {@code null}。
     */
    public static Item asItem(Block block) {
        for (RegisteredItem registered : REGISTERED_ITEMS) {
            if (registered.block == block) {
                return registered.item;
            }
        }
        return null;
    }

    /**
     * 待注册的方块物品条目。
     */
    private record PendingBlockItem(Block block, Identifier id) {
    }

    /**
     * 已注册的方块物品条目。
     */
    private record RegisteredItem(Block block, Item item) {
    }

    /**
     * 初始化道路箭头方块注册。
     */
    public static void registerArrowBlocks() {
        OcelotSignMod.LOGGER.info("Registering Advance Arrow Blocks for " + OcelotSignMod.MOD_ID);
    }
}
