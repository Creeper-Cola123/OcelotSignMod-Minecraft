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
     */
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ModIdentifiers.SELECT_MODEL, (client, handler, buf, responseSender) -> {});
    }
}
