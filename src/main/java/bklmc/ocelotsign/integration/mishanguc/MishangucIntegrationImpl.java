package bklmc.ocelotsign.integration.mishanguc;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * {@link IMishangucIntegration} ????
 *
 * <p>1.21.1 ???? {@code pers.solid.mishang.uc.text.TextContext.fromNbt(NbtElement, RegistryWrapper.WrapperLookup)}
 * ??? 1.20.4 ??????</p>
 *
 * @see IMishangucIntegration
 * @see MishangAccess
 */
public final class MishangucIntegrationImpl implements IMishangucIntegration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MishangucIntegrationImpl.class);
    private static MishangucIntegrationImpl INSTANCE;

    private final boolean available;
    private final Class<?> textContextClass;
    private final Class<?> textBridgeClass;
    private final MethodHandle fromNbtHandle;
    private final MethodHandle defaultContextCloneHandle;
    private final MethodHandle translatableHandle;
    private final MethodHandle literalHandle;
    private final MethodHandle emptyHandle;
    private final Object editSignFinishPacketHandler;
    private final ResourceLocation editSignFinishPacketId;
    private final boolean hasPacketHandler;
    private final Object textCopyTool;
    private final boolean hasTextCopyTool;

    private MishangucIntegrationImpl() {
        boolean initAvailable = true;
        Class<?> tc = null;
        Class<?> tb = null;
        MethodHandle fh = null;
        MethodHandle dch = null;
        MethodHandle th = null;
        MethodHandle lh = null;
        MethodHandle eh = null;
        Object ph = null;
        ResourceLocation pid = null;
        boolean hasPh = false;
        Object tct = null;
        boolean hasTct = false;

        try {
            tc = Class.forName("pers.solid.mishang.uc.text.TextContext");
            tb = Class.forName("pers.solid.mishang.uc.util.TextBridge");

            // TextContext.fromNbt(NbtElement, RegistryWrapper.WrapperLookup)
            Method fromNbtMethod = tc.getMethod("fromNbt", Tag.class, HolderLookup.Provider.class);
            fh = MethodHandles.lookup().unreflect(fromNbtMethod);

            Class<?> wallSignBeClass = Class.forName("pers.solid.mishang.uc.blockentity.WallSignBlockEntity");
            java.lang.reflect.Field defaultField = wallSignBeClass.getField("DEFAULT_TEXT_CONTEXT");
            defaultField.setAccessible(true);
            dch = MethodHandles.lookup().unreflectGetter(defaultField);

            Method translatableMethod = tb.getMethod("translatable", String.class);
            th = MethodHandles.lookup().unreflect(translatableMethod);

            Method literalMethod = tb.getMethod("literal", String.class);
            lh = MethodHandles.lookup().unreflect(literalMethod);

            Method emptyMethod = tb.getMethod("empty");
            eh = MethodHandles.lookup().unreflect(emptyMethod);

            Class<?> betClass = Class.forName("pers.solid.mishang.uc.blockentity.BlockEntityWithText");
            java.lang.reflect.Field packetHandlerField = betClass.getField("PACKET_HANDLER");
            packetHandlerField.setAccessible(true);
            ph = packetHandlerField.get(null);
            hasPh = ph != null;

            // 1.21.1 ? SignEditFinishPayload ? ID ? PacketType
            Class<?> payloadClass = Class.forName("pers.solid.mishang.uc.networking.SignEditFinishPayload");
            java.lang.reflect.Field idField = payloadClass.getField("ID");
            idField.setAccessible(true);
            Object payloadType = idField.get(null);
            if (payloadType != null) {
                pid = extractIdentifier(payloadType);
            }

            // 1.21.1 ???? TextCopyToolItem.TEXT_COPY_TOOL
            try {
                Class<?> itemsClass = Class.forName("pers.solid.mishang.uc.item.TextCopyToolItem");
                java.lang.reflect.Field textCopyToolField = itemsClass.getField("TEXT_COPY_TOOL");
                textCopyToolField.setAccessible(true);
                tct = textCopyToolField.get(null);
                hasTct = tct != null;
            } catch (ClassNotFoundException | NoSuchFieldException ignored) {
                // ?? MishangucItems.TEXT_COPY_TOOL
                try {
                    Class<?> itemsClass = Class.forName("pers.solid.mishang.uc.item.MishangucItems");
                    java.lang.reflect.Field textCopyToolField = itemsClass.getField("TEXT_COPY_TOOL");
                    textCopyToolField.setAccessible(true);
                    tct = textCopyToolField.get(null);
                    hasTct = tct != null;
                } catch (ClassNotFoundException | NoSuchFieldException ignored2) {
                    // ???
                }
            }
        } catch (ClassNotFoundException e) {
            LOGGER.warn("???? Mishanguc??????");
            initAvailable = false;
        } catch (NoSuchMethodException | NoSuchFieldException e) {
            LOGGER.warn("Mishanguc API ?????????: {}", e.getMessage());
            initAvailable = false;
        } catch (IllegalAccessException e) {
            LOGGER.error("???? mishanguc ??: {}", e.getMessage());
            initAvailable = false;
        } catch (Throwable t) {
            LOGGER.error("mishanguc ???????", t);
            initAvailable = false;
        }

        this.available = initAvailable;
        this.textContextClass = tc;
        this.textBridgeClass = tb;
        this.fromNbtHandle = fh;
        this.defaultContextCloneHandle = dch;
        this.translatableHandle = th;
        this.literalHandle = lh;
        this.emptyHandle = eh;
        this.editSignFinishPacketHandler = ph;
        this.editSignFinishPacketId = pid;
        this.hasPacketHandler = hasPh;
        this.textCopyTool = tct;
        this.hasTextCopyTool = hasTct;
    }

    /** ?????? */
    public static MishangucIntegrationImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MishangucIntegrationImpl();
        }
        return INSTANCE;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public Object getDefaultTextContext() {
        if (!available) return null;
        try {
            Object defaultCtx = defaultContextCloneHandle.invoke();
            if (defaultCtx != null) {
                Method cloneMethod = textContextClass.getMethod("clone");
                return cloneMethod.invoke(defaultCtx);
            }
        } catch (Throwable t) {
            LOGGER.error("???????????", t);
        }
        return null;
    }

    @Override
    public List<?> readTextContextsFromNbt(CompoundTag nbt) {
        if (!available) return Collections.emptyList();

        try {
            Object defaultCtx = getDefaultTextContext();
            if (defaultCtx == null) {
                return ImmutableList.of();
            }

            // 1.21.1 ? WallSignBlockEntity ? "text" ????? "texts"
            Tag nbtText = nbt.get("texts");
            if (nbtText == null) {
                // ?? 1.20.4 ? "text" ??
                nbtText = nbt.get("text");
            }
            if (nbtText == null) {
                return ImmutableList.of();
            }

            // ?? MinecraftClient.world ?? registryLookup
            // ?????????? dummy registry
            HolderLookup.Provider registryLookup = resolveRegistryLookup();

            if (nbtText instanceof ListTag) {
                ImmutableList.Builder<Object> builder = new ImmutableList.Builder<>();
                for (Tag element : (ListTag) nbtText) {
                    builder.add(fromNbtHandle.invoke(element, registryLookup));
                }
                return builder.build();
            } else if (nbtText instanceof CompoundTag || nbtText instanceof StringTag) {
                return ImmutableList.of(fromNbtHandle.invoke(nbtText, registryLookup));
            }
        } catch (Throwable t) {
            LOGGER.error("? NBT ?????????", t);
        }
        return ImmutableList.of();
    }

    @Override
    public MutableComponent textContextToStyledText(Object context) {
        if (!available || context == null) return Component.literal("");

        try {
            Method asStyledText = textContextClass.getMethod("asStyledText");
            return (MutableComponent) asStyledText.invoke(context);
        } catch (Throwable t) {
            LOGGER.error("???????", t);
            return Component.literal("");
        }
    }

    @Override
    public MutableComponent translatable(String key) {
        if (!available) return Component.literal("");
        try {
            return (MutableComponent) translatableHandle.invoke(key);
        } catch (Throwable t) {
            LOGGER.error("????????: {}", key, t);
            return Component.literal("");
        }
    }

    @Override
    public MutableComponent literal(String text) {
        if (!available) return Component.literal(text);
        try {
            return (MutableComponent) literalHandle.invoke(text);
        } catch (Throwable t) {
            LOGGER.error("????????", t);
            return Component.literal(text);
        }
    }

    @Override
    public MutableComponent empty() {
        if (!available) return Component.literal("");
        try {
            return (MutableComponent) emptyHandle.invoke();
        } catch (Throwable t) {
            LOGGER.error("???????", t);
            return Component.literal("");
        }
    }

    @Override
    public ResourceLocation getEditSignFinishPacketId() {
        return editSignFinishPacketId;
    }

    @Override
    public Object getEditSignFinishPacketHandler() {
        return hasPacketHandler ? editSignFinishPacketHandler : null;
    }

    @Override
    public Object getTextCopyTool() {
        return hasTextCopyTool ? textCopyTool : null;
    }

    /**
     * ??????? RegistryWrapper.WrapperLookup?
     * ?????????? dummy registry lookup?
     */
    private static HolderLookup.Provider resolveRegistryLookup() {
        try {
            Class<?> clientClass = Class.forName("net.minecraft.client.MinecraftClient");
            java.lang.reflect.Method getInstance = clientClass.getMethod("getInstance");
            Object client = getInstance.invoke(null);
            if (client != null) {
                java.lang.reflect.Method getWorld = clientClass.getMethod("getWorld");
                Object world = getWorld.invoke(client);
                if (world != null) {
                    java.lang.reflect.Method getRegistryManager = world.getClass().getMethod("getRegistryManager");
                    return (HolderLookup.Provider) getRegistryManager.invoke(world);
                }
            }
        } catch (Throwable t) {
            // ?? - ???????? registry ???
        }
        // ??????? lookup?TextContext.fromNbt ????? registries
        return DummyWrapperLookup.INSTANCE;
    }

    /**
     * ? PacketType ??? Identifier?
     */
    private static ResourceLocation extractIdentifier(Object payloadType) {
        try {
            Method getIdMethod = payloadType.getClass().getMethod("getId");
            Object idValue = getIdMethod.invoke(payloadType);
            if (idValue instanceof ResourceLocation id) {
                return id;
            }
        } catch (NoSuchMethodException ignored) {
            try {
                Method getIdMethod = payloadType.getClass().getMethod("getPacketId");
                Object idValue = getIdMethod.invoke(payloadType);
                if (idValue instanceof ResourceLocation id) {
                    return id;
                }
            } catch (NoSuchMethodException ignored2) {
                // ??
            } catch (Throwable ignored3) {
                // ??
            }
        } catch (Throwable ignored) {
            // ??
        }
        return null;
    }

    /**
     * ? registry lookup ??????????????
     * ??? TextContext.fromNbt ? RegistryLookup ????????????? registries?
     */
    @SuppressWarnings("rawtypes")
    private static final class DummyWrapperLookup implements HolderLookup.Provider {
        static final DummyWrapperLookup INSTANCE = new DummyWrapperLookup();

        private DummyWrapperLookup() {
        }

        @Override
        public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(net.minecraft.resources.ResourceKey<? extends net.minecraft.core.Registry<? extends T>> key) {
            return Optional.empty();
        }

        @Override
        public Stream<net.minecraft.resources.ResourceKey<? extends net.minecraft.core.Registry<?>>> listRegistries() {
            return Stream.empty();
        }
    }
}
