package com.direwolf20.buildinggadgets2.setup;

import com.direwolf20.buildinggadgets2.util.GadgetNBT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BG2DataComponents {
    public static final String BOUND_GLOBAL_POS = "bound_global_pos";
    public static final String ANCHOR_POS = "anchor_pos";
    public static final String RENDER_TYPE = "render_type";
    public static final String ANCHOR_SIDE = "anchor_side";
    public static final String ANCHOR_LIST = "anchor_list";
    public static final String COPY_START_POS = "copy_start_pos";
    public static final String COPY_END_POS = "copy_end_pos";
    public static final String RELATIVE_PASTE = "relative_paste";
    public static final String GADGET_UUID = "gadget_uuid";
    public static final String COPY_UUID = "copy_uuid";
    public static final String GADGET_BLOCKSTATE = "gadget_blockstate";
    public static final String UNDO_LIST = "undo_list";
    public static final String GADGET_RANGE = "gadget_range";
    public static final String TEMPLATE_NAME = "template_name";
    public static final String GADGET_MODE = "gadget_mode";

    public static final String ENERGY = "energy";

    public static final Map<GadgetNBT.ToggleableSettings, ArrayList<Boolean>> SETTING_TOGGLES = new HashMap<>();
    public static final Map<GadgetNBT.IntSettings, ArrayList<Integer>> SETTING_VALUES = new HashMap<>();

//    public static void genSettingToggles() {
//        for (GadgetNBT.ToggleableSettings toggleableSetting : GadgetNBT.ToggleableSettings.values()) {
//            DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> SETTING_TOGGLE = COMPONENTS.register(toggleableSetting.getName() + "_toggle", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL.orElse(false)).networkSynchronized(ByteBufCodecs.BOOL).build());
//            SETTING_TOGGLES.put(toggleableSetting, SETTING_TOGGLE);
//        }
//    }
//
//    public static void genSettingValues() {
//        for (GadgetNBT.IntSettings intSetting : GadgetNBT.IntSettings.values()) {
//            DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SETTING_VALUE = COMPONENTS.register(intSetting.getName() + "_value", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
//            SETTING_VALUES.put(intSetting, SETTING_VALUE);
//        }
//    }
//
//    private static @NotNull <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, final Codec<T> codec) {
//        return register(name, codec, null);
//    }
//
//    private static @NotNull <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, final Codec<T> codec, @Nullable final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
//        if (streamCodec == null) {
//            return COMPONENTS.register(name, () -> DataComponentType.<T>builder().persistent(codec).build());
//        } else {
//            return COMPONENTS.register(name, () -> DataComponentType.<T>builder().persistent(codec).networkSynchronized(streamCodec).build());
//        }
//    }
}
