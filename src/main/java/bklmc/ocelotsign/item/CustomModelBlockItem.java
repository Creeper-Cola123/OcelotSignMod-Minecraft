package bklmc.ocelotsign.item;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

/**
 * 自定义模型方块对应的物品
 *
 * @see bklmc.ocelotsign.blockentity.CustomModelBlockEntity
 * @see ModelWandItem
 */
public class CustomModelBlockItem extends BlockItem {
    public static final String SELECTED_MODEL_ID_KEY = "SelectedModelId";

    /**
     * 服务端临时缓存：选择包与放置包可能在同一 tick 到达，
     * 需先于 NBT 正式写入，供放置校验使用。
     */
    private static final Map<String, String> PENDING_SERVER_SELECTIONS = new ConcurrentHashMap<>();

    /**
     * 构造自定义模型方块物品。
     *
     * @param block    关联的方块
     * @param settings 物品设置
     */
    public CustomModelBlockItem(net.minecraft.world.level.block.Block block, Properties settings) {
        super(block, settings);
    }

    /** 生成临时缓存键 */
    private static String pendingKey(UUID playerId, InteractionHand hand) {
        return playerId + ":" + hand.name();
    }

    /**
     * 在服务端缓存一次模型选择，待后续 {@link #useOn} 时应用。
     *
     * @param playerId 玩家 UUID。
     * @param hand     交互的手。
     * @param modelId  选中的模型 ID。
     */
    public static void rememberPendingSelection(UUID playerId, InteractionHand hand, String modelId) {
        if (modelId != null && !modelId.isEmpty()) {
            PENDING_SERVER_SELECTIONS.put(pendingKey(playerId, hand), modelId);
        }
    }

    /**
     * 取出并清除服务端的缓存选择。
     *
     * @param playerId 玩家 UUID。
     * @param hand     交互的手。
     * @return 缓存的模型 ID，若无则返回 {@code null}。
     */
    @Nullable
    public static String takePendingSelection(UUID playerId, InteractionHand hand) {
        return PENDING_SERVER_SELECTIONS.remove(pendingKey(playerId, hand));
    }

    /**
     * 将选中的模型写入玩家手中的物品 NBT。
     *
     * @param player  服务端玩家。
     * @param hand    交互的手。
     * @param modelId 选中的模型 ID。
     */
    public static void applySelectionToStack(ServerPlayer player, InteractionHand hand, String modelId) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty() || !isCustomModelItem(stack)) {
            return;
        }
        setModelIdToStack(stack, modelId);
        player.setItemInHand(hand, stack);
        player.containerMenu.broadcastChanges();
        rememberPendingSelection(player.getUUID(), hand, modelId);
    }

    /** 确保物品有已选模型，优先从服务端临时缓存中取 */
    private static boolean ensureSelectedModel(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        if (hasSelectedModel(stack)) {
            return true;
        }

        Player player = context.getPlayer();
        if (player == null || context.getLevel().isClientSide()) {
            return false;
        }

        String pending = takePendingSelection(player.getUUID(), context.getHand());
        if (pending == null) {
            return false;
        }

        setModelIdToStack(stack, pending);
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.setItemInHand(context.getHand(), stack);
            serverPlayer.containerMenu.broadcastChanges();
        }
        return true;
    }

    /**
     * 使用物品时直接委托给父类，不做额外检查。
     *
     * @param world 世界
     * @param user  玩家
     * @param hand  手
     * @return 使用结果
     * @see #useOn
     */
    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        return super.use(world, user, hand);
    }

    /**
     * 放置方块时直接委托给父类，不做额外检查。
     *
     * <p>若物品 NBT 中有已选模型，会由方块的放置逻辑处理。
     * 模型选择可通过 Model Wand 右键已放置方块完成。</p>
     *
     * @param context 使用上下文
     * @return 使用结果
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        return super.useOn(context);
    }

    /**
     * 判断物品是否已选择模型。
     *
     * @param stack 物品栈
     * @return 若已选择模型则返回 {@code true}
     */
    public static boolean hasSelectedModel(ItemStack stack) {
        CompoundTag nbt = getCustomNbt(stack);
        return nbt != null && nbt.contains(SELECTED_MODEL_ID_KEY);
    }

    /**
     * 获取物品已选的模型 ID。
     *
     * @param stack 物品栈
     * @return 模型 ID，未选择时返回空字符串
     */
    public static String getSelectedModelId(ItemStack stack) {
        CompoundTag nbt = getCustomNbt(stack);
        if (nbt != null && nbt.contains(SELECTED_MODEL_ID_KEY)) {
            return nbt.getStringOr(SELECTED_MODEL_ID_KEY, "");
        }
        return "";
    }

    /**
     * 判断物品是否为自定义模型物品。
     *
     * @param stack 物品栈
     * @return 若是自定义模型物品则返回 {@code true}
     */
    public static boolean isCustomModelItem(ItemStack stack) {
        return stack.getItem() instanceof CustomModelBlockItem;
    }

    /**
     * 获取物品的自定义 NBT 数据（存储在 DataComponentTypes.CUSTOM_DATA 下）。
     */
    @Nullable
    private static CompoundTag getCustomNbt(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return null;
        return customData.copyTag();
    }

    /**
     * 将模型 ID 写入物品的自定义数据组件。
     */
    private static void setModelIdToStack(ItemStack stack, String modelId) {
        CompoundTag existing = getCustomNbt(stack);
        CompoundTag nbt = existing != null ? existing.copy() : new CompoundTag();
        nbt.putString(SELECTED_MODEL_ID_KEY, modelId);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
    }
}
