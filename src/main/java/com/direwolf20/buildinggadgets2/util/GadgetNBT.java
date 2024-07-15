package com.direwolf20.buildinggadgets2.util;

import com.direwolf20.buildinggadgets2.api.gadgets.GadgetModes;
import com.direwolf20.buildinggadgets2.api.gadgets.GadgetTarget;
import com.direwolf20.buildinggadgets2.common.items.*;
import com.direwolf20.buildinggadgets2.common.worlddata.BG2Data;
import com.direwolf20.buildinggadgets2.setup.BG2DataComponents;
import com.direwolf20.buildinggadgets2.util.modes.BaseMode;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedSet;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Locale;
import java.util.UUID;

public class GadgetNBT {
    public enum ToggleableSettings {
        AFFECT_TILES,
        RAYTRACE_FLUID,
        PLACE_ON_TOP,
        PASTE_REPLACE,
        BIND,
        FUZZY,
        CONNECTED_AREA;

        public static ToggleableSettings byName(String name) {
            return ToggleableSettings.valueOf(name.toUpperCase(Locale.ROOT));
        }

        public String getName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }

    public enum IntSettings {
        BIND_DIRECTION,
        LEFT,
        RIGHT,
        UP,
        DOWN,
        DEPTH;

        public static IntSettings byName(String name) {
            return IntSettings.valueOf(name.toUpperCase(Locale.ROOT));
        }

        public String getName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }

    /*public enum NBTValues {
        FUZZY("fuzzy"),
        CONNECTED_AREA("connected_area");

        public final String value;

        NBTValues(String value) {
            this.value = value;
        }
    }*/

    public enum RenderTypes {
        GROW("buildinggadgets2.grow"),
        FADE("buildinggadgets2.fade"),
        SQUISH("buildinggadgets2.squish"),
        GROWUP("buildinggadgets2.growup"),
        RISEUP("buildinggadgets2.riseup"),
        SNAP("buildinggadgets2.snap"),
        ;

        private final String lang;

        RenderTypes(String lang) {
            this.lang = lang;
        }

        public RenderTypes next() {
            // This will return the next value, wrapping around to the start if necessary
            return values()[(this.ordinal() + 1) % values().length];
        }

        public byte getPosition() {
            return (byte) this.ordinal();
        }

        public String getLang() {
            return lang;
        }

        public static RenderTypes getByOrdinal(byte ordinal) {
            return RenderTypes.values()[ordinal];
        }
    }

    public static final int[] nullPos = new int[]{-999, -999, -999};
    public static final int[] zeroPos = new int[]{0, 0, 0};
    final static int undoListSize = 10;

//    public static void setBoundPos(ItemStack gadget, GlobalPos globalPos) {
//        gadget.setTagInfo(globalPos,BG2DataComponents.BOUND_GLOBAL_POS);
////        gadget.set(BG2DataComponents.BOUND_GLOBAL_POS, globalPos);
//    }
//
//    public static GlobalPos getBoundPos(ItemStack gadget) {
//        return gadget.getOrDefault(BG2DataComponents.BOUND_GLOBAL_POS, null);
//    }

    public static void clearBoundPos(ItemStack gadget) {
        gadget.getTagCompound().removeTag(BG2DataComponents.BOUND_GLOBAL_POS);
    }

    public static void setAnchorPos(ItemStack gadget, int xPos, int yPos, int zPos) {
        gadget.getTagCompound().setIntArray(BG2DataComponents.ANCHOR_POS, new int[]{xPos, yPos, zPos});
    }

    public static void setRenderType(ItemStack gadget, byte renderType) {
        gadget.getTagCompound().setByte(BG2DataComponents.RENDER_TYPE, renderType);
    }

    public static byte getRenderTypeByte(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.getTagCompound().hasKey(BG2DataComponents.RENDER_TYPE)) {
            stack.getTagCompound().setByte(BG2DataComponents.RENDER_TYPE, (byte) 0);
        }
        return stack.getTagCompound().getByte(BG2DataComponents.RENDER_TYPE);
    }

    public static RenderTypes getRenderType(ItemStack stack) {
        return RenderTypes.getByOrdinal(getRenderTypeByte(stack));
    }

    public static int[] getAnchorPos(ItemStack gadget) {
        if (!gadget.hasTagCompound()) {
            gadget.setTagCompound(new NBTTagCompound());
        }
        if (!gadget.getTagCompound().hasKey(BG2DataComponents.ANCHOR_POS)) {
            gadget.getTagCompound().setIntArray(BG2DataComponents.ANCHOR_POS, nullPos);
        }
        return gadget.getTagCompound().getIntArray(BG2DataComponents.ANCHOR_POS);
    }

    public static void clearAnchorPos(ItemStack gadget) {
        if (!gadget.hasTagCompound()) {
            return;
        }
        gadget.getTagCompound().removeTag(BG2DataComponents.ANCHOR_POS);
        gadget.getTagCompound().removeTag(BG2DataComponents.ANCHOR_LIST);
        gadget.getTagCompound().removeTag(BG2DataComponents.ANCHOR_SIDE);
    }

    public static NBTTagList getAnchorList(ItemStack gadget) {
        if (!gadget.hasTagCompound()) {
            gadget.setTagCompound(new NBTTagCompound());
        }
        if (!gadget.getTagCompound().hasKey(BG2DataComponents.ANCHOR_LIST)) {
            gadget.getTagCompound().setTag(BG2DataComponents.ANCHOR_LIST, new NBTTagList());
        }
        return gadget.getTagCompound().getTagList(BG2DataComponents.ANCHOR_LIST, 11);
    }

    public static void setAnchorList(ItemStack gadget, NBTTagList anchorList) {
        if (!gadget.hasTagCompound()) {
            gadget.setTagCompound(new NBTTagCompound());
        }
        gadget.getTagCompound().setTag(BG2DataComponents.ANCHOR_LIST, anchorList);
    }

    public static void setAnchorSide(ItemStack stack, ForgeDirection side) {
        if (side == null)
            stack.getTagCompound().removeTag(BG2DataComponents.ANCHOR_SIDE);
        else
            stack.getTagCompound().setInteger(BG2DataComponents.ANCHOR_SIDE, side.ordinal());
    }

    public static ForgeDirection getAnchorSide(ItemStack stack) {
        if (!stack.getTagCompound().hasKey(BG2DataComponents.ANCHOR_SIDE)) {
            return null;
        }
        return ForgeDirection.values()[stack.getTagCompound().getInteger(BG2DataComponents.ANCHOR_SIDE)];
    }

    public static void setCopyStartPos(ItemStack gadget, int[] blockPos) {
        gadget.getTagCompound().setIntArray(BG2DataComponents.COPY_START_POS, blockPos);
    }

    public static int[] getCopyStartPos(ItemStack gadget) {
        if (!gadget.hasTagCompound()) {
            gadget.setTagCompound(new NBTTagCompound());
        }
        if (!gadget.getTagCompound().hasKey(BG2DataComponents.COPY_START_POS)) {
            gadget.getTagCompound().setIntArray(BG2DataComponents.COPY_START_POS, nullPos);
        }
        return gadget.getTagCompound().getIntArray(BG2DataComponents.COPY_START_POS);
    }

    public static void setRelativePaste(ItemStack gadget, int[] blockPos) {
        gadget.getTagCompound().setIntArray(BG2DataComponents.RELATIVE_PASTE, blockPos);
    }

    public static int[] getRelativePaste(ItemStack gadget) {
        if (!gadget.hasTagCompound()) {
            gadget.setTagCompound(new NBTTagCompound());
        }
        if (!gadget.getTagCompound().hasKey(BG2DataComponents.RELATIVE_PASTE)) {
            gadget.getTagCompound().setIntArray(BG2DataComponents.RELATIVE_PASTE, zeroPos);
        }
        return gadget.getTagCompound().getIntArray(BG2DataComponents.RELATIVE_PASTE);
    }

    public static void setCopyEndPos(ItemStack gadget, int[] blockPos) {
        gadget.getTagCompound().setIntArray(BG2DataComponents.COPY_END_POS, blockPos);
    }

    public static int[] getCopyEndPos(ItemStack gadget) {
        if (!gadget.hasTagCompound()) {
            gadget.setTagCompound(new NBTTagCompound());
        }
        if (!gadget.getTagCompound().hasKey(BG2DataComponents.COPY_END_POS)) {
            gadget.getTagCompound().setIntArray(BG2DataComponents.COPY_END_POS, nullPos);
        }
        return gadget.getTagCompound().getIntArray(BG2DataComponents.COPY_START_POS);
    }

    public static UUID setUUID(ItemStack gadget) {
        UUID uuid = UUID.randomUUID();
        gadget.getTagCompound().setString(BG2DataComponents.GADGET_UUID, uuid.toString());
        return uuid;
    }

    public static UUID getUUID(ItemStack gadget) {
        if (!gadget.getTagCompound().hasKey(BG2DataComponents.GADGET_UUID))
            return setUUID(gadget);
        return UUID.fromString(gadget.getTagCompound().getString(BG2DataComponents.GADGET_UUID));
    }

    public static UUID setCopyUUID(ItemStack gadget) {
        UUID uuid = UUID.randomUUID();
        return setCopyUUID(gadget, uuid);
    }

    public static UUID setCopyUUID(ItemStack gadget, UUID uuid) {
        gadget.getTagCompound().setString(BG2DataComponents.COPY_UUID, uuid.toString());
        return uuid;
    }

    public static UUID getCopyUUID(ItemStack gadget) {
        if (!gadget.getTagCompound().hasKey(BG2DataComponents.COPY_UUID)) {
            return setCopyUUID(gadget);
        }
        return UUID.fromString(gadget.getTagCompound().getString(BG2DataComponents.COPY_UUID));
    }

    public static boolean hasCopyUUID(ItemStack gadget) {
        return gadget.getTagCompound().hasKey(BG2DataComponents.COPY_UUID);
    }

    public static void clearCopyUUID(ItemStack gadget) {
        gadget.getTagCompound().removeTag(BG2DataComponents.COPY_UUID);
    }

    public static void setGadgetBlock(ItemStack gadget, Block block) {
        gadget.getTagCompound().setInteger(BG2DataComponents.GADGET_BLOCKSTATE,
            Block.getIdFromBlock(block));
    }

    public static Block getGadgetBlockState(ItemStack gadget) {
        if (!gadget.hasTagCompound()) {
            gadget.setTagCompound(new NBTTagCompound());
        }
        if (!gadget.getTagCompound().hasKey(BG2DataComponents.GADGET_BLOCKSTATE)) {
            gadget.getTagCompound().setInteger(BG2DataComponents.GADGET_BLOCKSTATE, Block.getIdFromBlock(Blocks.air));
        }
        return Block.getBlockById(gadget.getTagCompound().getInteger(BG2DataComponents.GADGET_BLOCKSTATE));
    }

    public static boolean shouldRayTraceFluid(ItemStack stack) {
        return getSetting(stack, ToggleableSettings.RAYTRACE_FLUID.getName());
    }

    public static NBTTagList getUndoList(ItemStack gadget) {
        if (!gadget.hasTagCompound()) {
            gadget.setTagCompound(new NBTTagCompound());
        }
        if (!gadget.getTagCompound().hasKey(BG2DataComponents.UNDO_LIST)) {
            gadget.getTagCompound().setTag(BG2DataComponents.UNDO_LIST, new NBTTagList());
        }
        return gadget.getTagCompound().getTagList(BG2DataComponents.UNDO_LIST, 8);
    }

    public static void setUndoList(ItemStack gadget, NBTTagList undoList) {
        gadget.getTagCompound().setTag(BG2DataComponents.UNDO_LIST, undoList);
    }

    public static void addToUndoList(ItemStack gadget, UUID uuid, BG2Data bg2Data) {
        NBTTagList undoList = getUndoList(gadget);
        if (undoList.tagCount() >= undoListSize) {
            //TODO forEach remove time:2024-07-15 13:50
            UUID removal = UUID.fromString(undoList.removeTag(0).toString());
            bg2Data.removeFromUndoList(removal);
        }
        undoList.appendTag(new NBTTagString(uuid.toString()));
        setUndoList(gadget, undoList);
    }

    public static UUID peekUndoList(ItemStack gadget) {
        NBTTagList undoList = getUndoList(gadget);
        if (undoList.tagCount() == 0) return null;
        //TODO maybe use stream?
        return UUID.fromString(undoList.getStringTagAt(0));
    }

    public static UUID popUndoList(ItemStack gadget) {
        NBTTagList undoList = getUndoList(gadget);
        if (undoList.tagCount() == 0) return null;
        UUID uuid = UUID.fromString(undoList.removeTag(undoList.tagCount() - 1).toString());
        setUndoList(gadget, undoList);
        return uuid;
    }

    public static boolean toggleSetting(ItemStack stack, String setting) {
        ToggleableSettings toggleableSetting = ToggleableSettings.byName(setting);
        stack.update(BG2DataComponents.SETTING_TOGGLES.get(toggleableSetting), false, k -> !k);
        return stack.getOrDefault(BG2DataComponents.SETTING_TOGGLES.get(toggleableSetting), false);
    }

    public static boolean getSetting(ItemStack stack, String setting) {
        ToggleableSettings toggleableSetting = ToggleableSettings.byName(setting);
        return stack.getOrDefault(BG2DataComponents.SETTING_TOGGLES.get(toggleableSetting), false);
    }

    public static boolean getPasteReplace(ItemStack stack) {
        if (!stack.has(BG2DataComponents.SETTING_TOGGLES.get(ToggleableSettings.PASTE_REPLACE))) {
            if (stack.getItem() instanceof GadgetCutPaste)
                return toggleSetting(stack, ToggleableSettings.PASTE_REPLACE.getName()); //Make PasteReplace true by default for cut/paste gadget
            else
                return false;
        }
        return getSetting(stack, ToggleableSettings.PASTE_REPLACE.getName());
    }

    public static void setToolRange(ItemStack stack, int range) {
        stack.set(BG2DataComponents.GADGET_RANGE, range);
    }

    public static int getToolRange(ItemStack stack) {
        return stack.getOrDefault(BG2DataComponents.GADGET_RANGE, 1);
    }

    public static void setToolValue(ItemStack stack, int value, String valueName) {
        stack.set(BG2DataComponents.SETTING_VALUES.get(IntSettings.byName(valueName)), value);
    }

    public static int getToolValue(ItemStack stack, String valueName) {
        return stack.getOrDefault(BG2DataComponents.SETTING_VALUES.get(IntSettings.byName(valueName)), 0);
    }

    public static void setTemplateName(ItemStack stack, String name) {
        stack.set(BG2DataComponents.TEMPLATE_NAME, name);
    }

    public static String getTemplateName(ItemStack stack) {
        return stack.getOrDefault(BG2DataComponents.TEMPLATE_NAME, "");
    }

    public static boolean getFuzzy(ItemStack stack) {
        return getSetting(stack, ToggleableSettings.FUZZY.getName());
    }

    /**
     * Safely get a mode based on the given mode id. When one is not present, we'll default to the first one in the list.
     *
     * @param stack the gadget
     * @return the correct mode for the gadget based on the gadget modes registry
     */
    public static BaseMode getMode(ItemStack stack) {
        // Checks if the current item if a gadget, if it's not, throw the game! You shouldn't be using this if you're not a gadget!
        Preconditions.checkArgument(stack.getItem() instanceof BaseGadget, "You can not get a mode of a non-gadget item");

        String mode = stack.getOrDefault(BG2DataComponents.GADGET_MODE, "");
        GadgetTarget gadgetTarget = ((BaseGadget) stack.getItem()).gadgetTarget();

        ImmutableSortedSet<BaseMode> modesForGadget = GadgetModes.INSTANCE.getModesForGadget(gadgetTarget);
        if (mode.isEmpty()) {
            if (stack.getItem() instanceof GadgetBuilding)
                return modesForGadget.stream()
                    .filter(m -> m.getId().getPath().equals("build_to_me"))
                    .findFirst()
                    .orElse(modesForGadget.first());
            if (stack.getItem() instanceof GadgetExchanger)
                return modesForGadget.stream()
                    .filter(m -> m.getId().getPath().equals("surface"))
                    .findFirst()
                    .orElse(modesForGadget.first());
            if (stack.getItem() instanceof GadgetCutPaste)
                return modesForGadget.stream()
                    .filter(m -> m.getId().getPath().equals("cut"))
                    .findFirst()
                    .orElse(modesForGadget.first());
            if (stack.getItem() instanceof GadgetCopyPaste)
                return modesForGadget.stream()
                    .filter(m -> m.getId().getPath().equals("copy"))
                    .findFirst()
                    .orElse(modesForGadget.first());
            return modesForGadget.first();
        }

        var id = ResourceLocation.parse(mode);
        return modesForGadget.stream()
            .filter(m -> m.getId().equals(id))
            .findFirst()
            .orElse(modesForGadget.first());
    }

    public static void setMode(ItemStack gadget, BaseMode mode) {
        gadget.set(BG2DataComponents.GADGET_MODE, mode.getId().toString());
    }
}
