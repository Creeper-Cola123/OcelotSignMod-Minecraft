package bklmc.ocelotsign.client.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.Identifier;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 告示牌模板注册表
 */
public class SignTemplateRegistry {
    /**
     * 模板条目。
     */
    public static class TemplateItem {
        public String id;
        public String category;
        public String name;
        public Identifier model;
        public float zOffset;
        public String renderMode;
    }

    /** 按分类分组的模板映射表。 */
    public static final Map<String, List<TemplateItem>> CATEGORIZED_TEMPLATES = new LinkedHashMap<>();
    /** 全量模板映射表（ID -> 模板）。 */
    public static final Map<String, TemplateItem> ALL_TEMPLATES = new HashMap<>();

    /**
     * 从资源包加载模板配置。
     */
    public static void loadTemplates() {
        CATEGORIZED_TEMPLATES.clear();
        ALL_TEMPLATES.clear();

        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        Identifier jsonId = Identifier.fromNamespaceAndPath("ocelotsignmod", "sign_templates.json");

        try {
            List<Resource> resources = collectAllTemplateResources(manager, jsonId);
            for (Resource resource : resources) {
                try (InputStreamReader reader = new InputStreamReader(resource.open(), StandardCharsets.UTF_8)) {
                    JsonElement root = JsonParser.parseReader(reader);
                    if (root.isJsonArray()) {
                        JsonArray array = root.getAsJsonArray();
                        for (JsonElement element : array) {
                            JsonObject obj = element.getAsJsonObject();
                            TemplateItem item = new TemplateItem();
                            item.id = obj.get("id").getAsString();
                            item.category = obj.has("category") ? obj.get("category").getAsString() : "其他";
                            item.name = obj.get("name").getAsString();
                            item.model = Identifier.parse(obj.get("model").getAsString());
                            item.zOffset = obj.has("z_offset") ? obj.get("z_offset").getAsFloat() : 0.0f;
                            item.renderMode = obj.has("render_mode") ? obj.get("render_mode").getAsString() : "SINGLE_SIDED";

                            ALL_TEMPLATES.put(item.id, item);
                            CATEGORIZED_TEMPLATES.computeIfAbsent(item.category, k -> new ArrayList<>()).add(item);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[OcelotSign] 读取资源包动态模型 JSON 失败，请检查文件是否存在格式错误。");
        }
    }

    /**
     * 收集来自所有资源包的同名资源。
     * <p>{@link ResourceManager#getAllResources(Identifier)} 在同名资源被合并后只返回合并结果，
     * 此处先通过 {@link ResourceManager#findResources(String, java.util.function.Predicate)} 找到所有匹配项，
     * 再对每个 Identifier 调用 {@code getAllResources} 取回全部原始数据。
     */
    private static List<Resource> collectAllTemplateResources(ResourceManager manager, Identifier targetId) {
        List<Resource> result = new ArrayList<>();
        try {
            Map<Identifier, Resource> allById = manager.listResources(
                    targetId.getPath(),
                    id -> id.getNamespace().equals(targetId.getNamespace())
                            && id.getPath().equals(targetId.getPath())
            );
            for (Identifier id : allById.keySet()) {
                try {
                    result.addAll(manager.getResourceStack(id));
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }
        if (result.isEmpty()) {
            try {
                result.addAll(manager.getResourceStack(targetId));
            } catch (Exception ignored) {
            }
        }
        return result;
    }
}
