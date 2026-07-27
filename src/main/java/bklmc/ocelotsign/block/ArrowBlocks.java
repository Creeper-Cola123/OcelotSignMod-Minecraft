package bklmc.ocelotsign.block;

import bklmc.ocelotsign.OcelotSignMod;
import bklmc.ocelotsign.block.custom.RoadMarkBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * 道路箭头方块注册中心
 *
 * @see bklmc.ocelotsign.block.custom.RoadMarkBlock
 */
public class ArrowBlocks {
    private static final FabricBlockSettings ROAD_MARK_SETTINGS = FabricBlockSettings.of(Material.STONE).strength(0.5f).nonOpaque().collidable(false);

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
     *
     * @param id    注册 ID
     * @param block 对应的方块
     */
    private static void registerBlockItem(String id, Block block) {
        Registry.register(Registry.ITEM, new Identifier(OcelotSignMod.MOD_ID, id),
                new BlockItem(block, new Item.Settings()));
    }

    /**
     * 初始化道路箭头方块注册。
     */
    public static void registerArrowBlocks() {
        OcelotSignMod.LOGGER.info("Registering Advance Arrow Blocks for " + OcelotSignMod.MOD_ID);
    }
}
