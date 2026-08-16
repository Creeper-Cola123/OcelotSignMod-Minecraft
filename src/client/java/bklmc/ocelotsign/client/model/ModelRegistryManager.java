package bklmc.ocelotsign.client.model;

import bklmc.ocelotsign.OcelotSignMod;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.FabricBakedModelManager;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户端模型注册表管理器
 *
 * <p>在 1.21.8 中，自定义模型通过 {@link ExtraModelKey} + {@link UnbakedExtraModel}
 * 注册到 {@code ModelLoadingPlugin}，并在渲染时通过
 * {@code FabricBakedModelManager#getModel(ExtraModelKey)} 取出
 * {@link BlockStateModel} 进行渲染。</p>
 */
public class ModelRegistryManager {
    /**
     * 模型定义记录
     */
    public record ModelDefinition(String localizedName, Identifier modelIdentifier) {}

    /** 可用模型映射表（模型ID -> 模型定义）。 */
    private static final Map<String, ModelDefinition> AVAILABLE_MODELS = new HashMap<>();

    /** 已注册的额外模型键。键为模型 {@link Identifier}，值为对应的 {@link ExtraModelKey}。 */
    private static final Map<Identifier, ExtraModelKey<BlockStateModel>> MODEL_KEYS = new HashMap<>();

    /** 已注册的额外模型键。键为模型 {@link Identifier}。 */
    private static final Map<Identifier, BlockStateModel> BAKED_MODELS = new HashMap<>();

    /** 标记是否已注册过模型加载器。 */
    private static boolean registered = false;

    /**
     * 注册模型加载器。只应调用一次。
     */
    public static synchronized void registerLoader() {
        if (registered) {
            return;
        }
        registered = true;

        ModelLoadingPlugin.register(pluginContext -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ResourceManager manager = client.getResourceManager();

            collectModelDefinitions(manager);

            for (Map.Entry<String, ModelDefinition> entry : AVAILABLE_MODELS.entrySet()) {
                String modelId = entry.getKey();
                Identifier modelIdentifier = entry.getValue().modelIdentifier();

                ExtraModelKey<BlockStateModel> key = ExtraModelKey.create(() -> "ocelotsign:custom_model/" + modelIdentifier);
                MODEL_KEYS.put(modelIdentifier, key);
                pluginContext.addModel(key, SimpleUnbakedExtraModel.blockStateModel(modelIdentifier));
                OcelotSignMod.LOGGER.debug("注册自定义模型: {}", modelIdentifier);
            }

            // 注册 fallback 模型
            Identifier fallbackId = Identifier.of("ocelotsignmod", "block/custom_model_fallback");
            ExtraModelKey<BlockStateModel> fallbackKey = ExtraModelKey.create(() -> "ocelotsign:custom_model/fallback");
            MODEL_KEYS.put(fallbackId, fallbackKey);
            pluginContext.addModel(fallbackKey, SimpleUnbakedExtraModel.blockStateModel(fallbackId));
            OcelotSignMod.LOGGER.debug("注册 Fallback 模型: {}", fallbackId);

            // 注册默认物品模型（用于 fallback 渲染）
            Identifier defaultItemModelId = Identifier.of("ocelotsignmod", "item/custom_model_block");
            ExtraModelKey<BlockStateModel> defaultItemKey = ExtraModelKey.create(() -> "ocelotsign:custom_model/default_item");
            MODEL_KEYS.put(defaultItemModelId, defaultItemKey);
            pluginContext.addModel(defaultItemKey, SimpleUnbakedExtraModel.blockStateModel(defaultItemModelId));
            OcelotSignMod.LOGGER.debug("注册默认物品模型: {}", defaultItemModelId);
        });
    }

    /** 收集模型定义。 */
    private static void collectModelDefinitions(ResourceManager manager) {
        AVAILABLE_MODELS.clear();

        // 从 model_definitions 目录收集模型
        Map<Identifier, Resource> resources = manager.findResources(
                "model_definitions",
                id -> id.getPath().endsWith(".json")
        );

        for (Map.Entry<Identifier, Resource> entry : resources.entrySet()) {
            try (InputStreamReader reader = new InputStreamReader(entry.getValue().getInputStream())) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                if (!json.has("model_id") || !json.has("localized_name")) {
                    OcelotSignMod.LOGGER.warn("模型定义缺少必要字段: {}", entry.getKey());
                    continue;
                }

                String modelId = json.get("model_id").getAsString();
                String name = json.get("localized_name").getAsString();
                if (modelId.isEmpty()) {
                    continue;
                }

                Identifier modelIdentifier = resolveModelIdentifier(entry.getKey().getNamespace(), modelId);
                AVAILABLE_MODELS.put(modelId, new ModelDefinition(name, modelIdentifier));
            } catch (Exception e) {
                OcelotSignMod.LOGGER.error("解析模型定义失败: {}", entry.getKey(), e);
            }
        }

        // 从 models/custom_models 目录收集模型
        Map<Identifier, Resource> customModels = manager.findResources(
                "models/custom_models",
                id -> id.getPath().endsWith(".json")
        );

        for (Map.Entry<Identifier, Resource> entry : customModels.entrySet()) {
            Identifier modelIdentifier = toModelIdentifier(entry.getKey());
            String modelId = modelIdentifier.getPath().substring("custom_models/".length());
            AVAILABLE_MODELS.computeIfAbsent(modelId, id -> new ModelDefinition(modelId, modelIdentifier));
        }
    }

    /** 资源ID转模型标识符 */
    private static Identifier toModelIdentifier(Identifier resourceId) {
        String path = resourceId.getPath();
        if (path.startsWith("models/")) {
            path = path.substring("models/".length());
        }
        if (path.endsWith(".json")) {
            path = path.substring(0, path.length() - ".json".length());
        }
        return Identifier.of(resourceId.getNamespace(), path);
    }

    /** 解析模型标识符 */
    private static Identifier resolveModelIdentifier(String namespace, String modelId) {
        if (modelId.contains(":")) {
            return Identifier.of(modelId);
        }

        String path = modelId.startsWith("custom_models/") ? modelId : "custom_models/" + modelId;
        return Identifier.of(namespace, path);
    }

    /**
     * 获取所有可用模型。
     *
     * @return 不可修改的模型映射表
     */
    public static Map<String, ModelDefinition> getAvailableModels() {
        return Collections.unmodifiableMap(AVAILABLE_MODELS);
    }

    /**
     * 根据模型 ID 获取模型标识符。
     *
     * @param modelId 模型ID
     * @return 模型标识符
     */
    public static Identifier getModelIdentifier(String modelId) {
        ModelDefinition definition = AVAILABLE_MODELS.get(modelId);
        if (definition != null) {
            return definition.modelIdentifier();
        }
        return Identifier.of("ocelotsignmod", "custom_models/" + modelId);
    }

    /**
     * 检查指定模型 ID 是否在本地资源包中有定义。
     *
     * @param modelId 模型ID
     * @return 是否有定义
     */
    public static boolean hasModelDefinition(String modelId) {
        return AVAILABLE_MODELS.containsKey(modelId);
    }

    /**
     * 获取 Fallback 方块模型标识符。
     *
     * @return Fallback 方块模型标识符
     */
    public static Identifier getFallbackModelIdentifier() {
        return Identifier.of("ocelotsignmod", "block/custom_model_fallback");
    }

    /**
     * 获取 Fallback 物品模型标识符。
     * 用于在没有模型时渲染旋转的物品图标。
     *
     * @return Fallback 物品模型标识符
     */
    public static Identifier getFallbackItemModelIdentifier() {
        return Identifier.of("ocelotsignmod", "item/custom_model_block");
    }

    /**
     * 获取与指定模型标识符关联的额外模型键。
     *
     * @param modelIdentifier 模型资源标识符
     * @return 对应的额外模型键；若未注册则返回 {@code null}
     */
    @org.jetbrains.annotations.Nullable
    public static ExtraModelKey<BlockStateModel> getModelKey(Identifier modelIdentifier) {
        return MODEL_KEYS.get(modelIdentifier);
    }

    /**
     * 通过模型标识符获取烘焙后的模型。
     *
     * @param modelIdentifier 模型资源标识符
     * @return 烘焙后的模型；若未找到则返回 {@code null}
     */
    @org.jetbrains.annotations.Nullable
    public static BlockStateModel getModel(Identifier modelIdentifier) {
        ExtraModelKey<BlockStateModel> key = MODEL_KEYS.get(modelIdentifier);
        if (key == null) {
            OcelotSignMod.LOGGER.warn("模型键未找到: {}", modelIdentifier);
            return null;
        }
        BakedModelManager modelManager = MinecraftClient.getInstance().getBakedModelManager();
        if (modelManager instanceof FabricBakedModelManager fabricModelManager) {
            return fabricModelManager.getModel(key);
        }
        OcelotSignMod.LOGGER.warn("BakedModelManager 未实现 FabricBakedModelManager");
        return null;
    }
}
