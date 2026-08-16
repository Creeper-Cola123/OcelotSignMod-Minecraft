package bklmc.ocelotsign.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * 客户端网络通道注册。
 */
@Environment(EnvType.CLIENT)
public final class ClientNetworking {
    private ClientNetworking() {
    }

    /**
     * 注册所有客户端网络通道处理器。
     * 
     * <p>注意：payload 类型已在主端（ServerNetworking.register()）注册，
     * 此处无需重复注册。</p>
     */
    public static void register() {
        // payload 类型已在 ServerNetworking.registerPayloadTypes() 中注册
        // 此处只需注册处理器（如果需要接收 S2C 数据包）
        // 本模组的 select_model 是 C2S 数据包，客户端只需发送，不需要接收处理器
    }
}
