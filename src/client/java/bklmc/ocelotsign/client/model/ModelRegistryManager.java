package bklmc.ocelotsign.client.model;

import bklmc.ocelotsign.OcelotSignMod;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 客户端模型注册表管理器
 */
public class ModelRegistryManager {
    /**
     * 模型定义记录
     */
    public record ModelDefinition(String localizedName, Identifier modelIdentifier) {}

    /** 可用模型映射表（模型ID -> 模型定义）。 */
    private static final Map<String, ModelDefinition> AVAILABLE_MODELS = new HashMap<>();
    /** 已注册的模型标识符集合。 */
    private static final Set<Identifier> REGISTERED_MODELS = new HashSet<>();

    /**
     * 注册模型加载器和资源重载监听器。
     */
    public static void registerLoader() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(
                new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return OcelotSignMod.id("model_definitions");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        reloadDefinitions(manager);
                    }
                }
        );

        ModelLoadingPlugin.register(pluginContext -> {
            ResourceManager manager = MinecraftClient.getInstance().getResourceManager();
            reloadDefinitions(manager);

            for (Identifier modelIdentifier : REGISTERED_MODELS) {
                pluginContext.addModels(modelIdentifier);
                OcelotSignMod.LOGGER.debug("注册自定义模型: {}", modelIdentifier);
            }
        });
    }

    /** 重新加载模型定义。 */
    private static void reloadDefinitions(ResourceManager manager) {
        AVAILABLE_MODELS.clear();
        REGISTERED_MODELS.clear();

        collectModelsFromDefinitions(manager);
        collectModelsFromCustomModelFiles(manager);

        REGISTERED_MODELS.add(OcelotSignMod.id("block/custom_model_fallback"));
    }

    /** 从 model_definitions 目录收集模型。 */
    private static void collectModelsFromDefinitions(ResourceManager manager) {
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
                REGISTERED_MODELS.add(modelIdentifier);
            } catch (Exception e) {
                OcelotSignMod.LOGGER.error("解析模型定义失败: {}", entry.getKey(), e);
            }
        }
    }

    /** 扫描 models/custom_models 目录。 */
    private static void collectModelsFromCustomModelFiles(ResourceManager manager) {
        Map<Identifier, Resource> resources = manager.findResources(
                "models/custom_models",
                id -> id.getPath().endsWith(".json")
        );

        for (Identifier resourceId : resources.keySet()) {
            Identifier modelIdentifier = toModelIdentifier(resourceId);
            REGISTERED_MODELS.add(modelIdentifier);

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
        return OcelotSignMod.id("custom_models/" + modelId);
    }

    /**
     * 检查指定模型 ID 是否在本地资源包中有定义。
     *
     * <p>当服务器同步了模型 ID，但玩家未加载对应资源包时返回 false。
     *
     * @param modelId 模型ID
     * @return 是否有定义
     */
    public static boolean hasModelDefinition(String modelId) {
        return AVAILABLE_MODELS.containsKey(modelId);
    }

    /**
     * 获取 Fallback 模型标识符。
     *
     * <p>当模型在本地有定义但资源包中的模型 JSON 加载失败时使用。
     *
     * @return Fallback模型标识符
     */
    public static Identifier getFallbackModelIdentifier() {
        return OcelotSignMod.id("block/custom_model_fallback");
    }
}
