package bklmc.ocelotsign;

import bklmc.ocelotsign.block.ArrowBlocks;
import bklmc.ocelotsign.block.ArrowBlocksLarge;
import bklmc.ocelotsign.block.ModBlocks;
import bklmc.ocelotsign.block.custom.CustomModelBlock;
import bklmc.ocelotsign.blockentity.CustomModelBlockEntity;
import bklmc.ocelotsign.blockentity.ModBlockEntities;
import bklmc.ocelotsign.item.CustomModelBlockItem;
import bklmc.ocelotsign.item.ModelWandItem;
import bklmc.ocelotsign.item.ModItemGroups;
import bklmc.ocelotsign.item.ModItems;
import bklmc.ocelotsign.platform.ServerNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 模组主入口
 *
 * <p>客户端入口位于客户端专属源集中的同名类。</p>
 *
 * @see OcelotSignModClient
 */
public class OcelotSignMod implements ModInitializer {
    public static final String MOD_ID = "ocelotsignmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static BlockEntityType<CustomModelBlockEntity> CUSTOM_MODEL_BLOCK_ENTITY;
    public static CustomModelBlock CUSTOM_MODEL_BLOCK;
    public static ModelWandItem MODEL_WAND;

    /**
     * @param path 资源路径
     * @return 命名空间为 {@link #MOD_ID} 的标识符
     */
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModBlocks.registerModBlocks();
        ArrowBlocks.registerArrowBlocks();
        ArrowBlocksLarge.register();
        ModBlockEntities.registerModBlockEntities();
        ModItems.registerModItems();
        ModItemGroups.registerItemGroups();

        registerCustomModelBlock();
        registerModelWand();
        ServerNetworking.register();
    }

    private static void registerCustomModelBlock() {
        CUSTOM_MODEL_BLOCK = new CustomModelBlock(BlockBehaviour.Properties.of().strength(1.0f).noOcclusion());
        CUSTOM_MODEL_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                id("custom_model_block_entity"),
                FabricBlockEntityTypeBuilder.create(CustomModelBlockEntity::new, CUSTOM_MODEL_BLOCK).build()
        );
        Registry.register(BuiltInRegistries.BLOCK, id("custom_model_block"), CUSTOM_MODEL_BLOCK);
        Registry.register(BuiltInRegistries.ITEM, id("custom_model_block"),
                new CustomModelBlockItem(CUSTOM_MODEL_BLOCK, new Item.Properties()));
    }

    private static void registerModelWand() {
        MODEL_WAND = new ModelWandItem(new Item.Properties().stacksTo(1));
        Registry.register(BuiltInRegistries.ITEM, id("model_wand"), MODEL_WAND);
    }
}
