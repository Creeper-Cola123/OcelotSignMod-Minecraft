package bklmc.ocelotsign.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
    public CustomModelBlockItem(net.minecraft.block.Block block, Settings settings) {
        super(block, settings);
    }

    /** 生成临时缓存键 */
    private static String pendingKey(UUID playerId, Hand hand) {
        return playerId + ":" + hand.name();
    }

    /**
     * 在服务端缓存一次模型选择，待后续 {@link #useOnBlock} 时应用。
     *
     * @param playerId 玩家 UUID。
     * @param hand     交互的手。
     * @param modelId  选中的模型 ID。
     */
    public static void rememberPendingSelection(UUID playerId, Hand hand, String modelId) {
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
    public static String takePendingSelection(UUID playerId, Hand hand) {
        return PENDING_SERVER_SELECTIONS.remove(pendingKey(playerId, hand));
    }

    /**
     * 将选中的模型写入玩家手中的物品 NBT。
     *
     * @param player  服务端玩家。
     * @param hand    交互的手。
     * @param modelId 选中的模型 ID。
     */
    public static void applySelectionToStack(ServerPlayerEntity player, Hand hand, String modelId) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isEmpty() || !isCustomModelItem(stack)) {
            return;
        }
        setModelIdToStack(stack, modelId);
        player.setStackInHand(hand, stack);
        player.currentScreenHandler.sendContentUpdates();
        rememberPendingSelection(player.getUuid(), hand, modelId);
    }

    /** 确保物品有已选模型，优先从服务端临时缓存中取 */
    private static boolean ensureSelectedModel(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        if (hasSelectedModel(stack)) {
            return true;
        }

        PlayerEntity player = context.getPlayer();
        if (player == null || context.getWorld().isClient()) {
            return false;
        }

        String pending = takePendingSelection(player.getUuid(), context.getHand());
        if (pending == null) {
            return false;
        }

        setModelIdToStack(stack, pending);
        if (player instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.setStackInHand(context.getHand(), stack);
            serverPlayer.currentScreenHandler.sendContentUpdates();
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
     * @see #useOnBlock
     */
    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
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
    public ActionResult useOnBlock(ItemUsageContext context) {
        return super.useOnBlock(context);
    }

    /**
     * 判断物品是否已选择模型。
     *
     * @param stack 物品栈
     * @return 若已选择模型则返回 {@code true}
     */
    public static boolean hasSelectedModel(ItemStack stack) {
        NbtCompound nbt = getCustomNbt(stack);
        return nbt != null && nbt.contains(SELECTED_MODEL_ID_KEY);
    }

    /**
     * 获取物品已选的模型 ID。
     *
     * @param stack 物品栈
     * @return 模型 ID，未选择时返回空字符串
     */
    public static String getSelectedModelId(ItemStack stack) {
        NbtCompound nbt = getCustomNbt(stack);
        if (nbt != null && nbt.contains(SELECTED_MODEL_ID_KEY)) {
            return nbt.getString(SELECTED_MODEL_ID_KEY, "");
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
    private static NbtCompound getCustomNbt(ItemStack stack) {
        NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (customData == null) return null;
        return customData.copyNbt();
    }

    /**
     * 将模型 ID 写入物品的自定义数据组件。
     */
    private static void setModelIdToStack(ItemStack stack, String modelId) {
        NbtCompound existing = getCustomNbt(stack);
        NbtCompound nbt = existing != null ? existing.copy() : new NbtCompound();
        nbt.putString(SELECTED_MODEL_ID_KEY, modelId);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }
}
