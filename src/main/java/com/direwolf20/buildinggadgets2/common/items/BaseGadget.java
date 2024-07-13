package com.direwolf20.buildinggadgets2.common.items;

import com.direwolf20.buildinggadgets2.api.gadgets.GadgetModes;
import com.direwolf20.buildinggadgets2.api.gadgets.GadgetTarget;
import com.direwolf20.buildinggadgets2.common.events.ServerTickHandler;
import com.direwolf20.buildinggadgets2.common.worlddata.BG2Data;
import com.direwolf20.buildinggadgets2.util.*;
import com.direwolf20.buildinggadgets2.util.context.ItemActionContext;
import com.direwolf20.buildinggadgets2.util.datatypes.StatePos;
import com.direwolf20.buildinggadgets2.util.modes.BaseMode;
import com.google.common.collect.ImmutableSortedSet;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.*;

public abstract class BaseGadget extends Item {

    public BaseGadget() {
        super(new Properties()
                .stacksTo(1));
    }

    /**
     * Forge Energy Storage methods
     */

    public abstract int getEnergyMax();

    public abstract int getEnergyCost();

    @Override
    public int getMaxDamage(ItemStack stack) {
        return getEnergyMax();
    }

//    @Override
//    public boolean isBarVisible(ItemStack stack) {
//        var energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
//        if (energy == null) {
//            return false;
//        }
//
//        return (energy.getEnergyStored() < energy.getMaxEnergyStored());
//    }

//    @Override
//    public int getBarWidth(ItemStack stack) {
//        var energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
//        if (energy == null) {
//            return 0;
//        }
//
//        return Math.min(13 * energy.getEnergyStored() / energy.getMaxEnergyStored(), 13);
//    }

//    @Override
//    public int getBarColor(ItemStack stack) {
//        var energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
//        if (energy == null) {
//            return super.getBarColor(stack);
//        }
//
//        return Mth.hsvToRgb(Math.max(0.0F, (float) energy.getEnergyStored() / (float) energy.getMaxEnergyStored()) / 3.0F, 1.0F, 1.0F);
//    }

    @SideOnly(Side.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return;
        }

        boolean sneakPressed = Screen.hasShiftDown();

        if (!sneakPressed) {
            tooltip.add(Component.translatable("buildinggadgets2.tooltips.holdshift",
                            "shift")
                    .withStyle(ChatFormatting.GRAY));
        } else {
            GlobalPos boundTo = GadgetNBT.getBoundPos(stack);
            if (boundTo != null) {
                tooltip.add(Component.translatable("buildinggadgets2.tooltips.boundto", boundTo.dimension().location().getPath(), "[" + boundTo.pos().toShortString() + "]").setStyle(Styles.GOLD));
            }
        }

        var energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energy != null) {
            MutableComponent energyText = !sneakPressed
                    ? Component.translatable("buildinggadgets2.tooltips.energy", MagicHelpers.tidyValue(energy.getEnergyStored()), MagicHelpers.tidyValue(energy.getMaxEnergyStored()))
                    : Component.translatable("buildinggadgets2.tooltips.energy", String.format("%,d", energy.getEnergyStored()), String.format("%,d", energy.getMaxEnergyStored()));

            tooltip.add(energyText.withStyle(ChatFormatting.GREEN));
        }
    }


    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        ItemStack gadget = player.getItemInUse();

        if (world.isRemote){
            return true;
        }

        BlockHitResult lookingAt = VectorHelper.getLookingAt(player,gadget);

        return super.onItemUse(itemStack, player, world, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_);
    }

    /**
     * Implementation level of for the onAction & onShiftAction methods below.
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack gadget = player.getItemInHand(hand);

        if (level.isClientSide()) //No client
            return InteractionResultHolder.success(gadget);

        BlockHitResult lookingAt = VectorHelper.getLookingAt(player, gadget);
        if (level.getBlockState(lookingAt.getBlockPos()).isAir() && GadgetNBT.getAnchorPos(gadget).equals(GadgetNBT.nullPos))
            return InteractionResultHolder.success(gadget);
        ItemActionContext context = new ItemActionContext(lookingAt.getBlockPos(), lookingAt, player, level, hand, gadget);

        if (player.isShiftKeyDown()) {
            if (GadgetNBT.getSetting(gadget, GadgetNBT.ToggleableSettings.BIND.getName())) {
                if (bindToInventory(level, player, gadget, lookingAt)) {
                    GadgetNBT.toggleSetting(gadget, GadgetNBT.ToggleableSettings.BIND.getName()); //Turn off bind
                    return InteractionResultHolder.success(gadget);
                } else {
                    return InteractionResultHolder.fail(gadget);
                }
            }
            return this.onShiftAction(context);
        }

        return this.onAction(context);
    }

    InteractionResultHolder<ItemStack> onAction(ItemActionContext context) {
        return InteractionResultHolder.pass(context.stack());
    }

    InteractionResultHolder<ItemStack> onShiftAction(ItemActionContext context) {
        return InteractionResultHolder.pass(context.stack());
    }

    public boolean bindToInventory(Level level, Player player, ItemStack gadget, BlockHitResult lookingAt) {
        BlockEntity blockEntity = level.getBlockEntity(lookingAt.getBlockPos());
        if (blockEntity != null) {
            var itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, lookingAt.getBlockPos(), lookingAt.getDirection());
            if (itemHandler != null) {
                GadgetNBT.setBoundPos(gadget, new GlobalPos(level.dimension(), lookingAt.getBlockPos()));
                GadgetNBT.setToolValue(gadget, lookingAt.getDirection().ordinal(), GadgetNBT.IntSettings.BIND_DIRECTION.getName());
                player.displayClientMessage(Component.translatable("buildinggadgets2.messages.bindsuccess", lookingAt.getBlockPos().toShortString()), true);
                return true;
            }
        }
        GlobalPos existingBind = GadgetNBT.getBoundPos(gadget);
        if (existingBind == null)
            player.displayClientMessage(Component.translatable("buildinggadgets2.messages.bindfailed"), true);
        else {
            GadgetNBT.clearBoundPos(gadget);
            player.displayClientMessage(Component.translatable("buildinggadgets2.messages.bindremoved"), true);
            return true;
        }
        return false;
    }

    /**
     * Rotates through the registered building modes, useful for key bindings.
     *
     * @param stack the gadget
     * @return the selected mode's id
     */
    public ResourceLocation rotateModes(ItemStack stack) {
        ImmutableSortedSet<BaseMode> modesForGadget = GadgetModes.INSTANCE.getModesForGadget(this.gadgetTarget());
        var arrayOfModes = new ArrayList<>(modesForGadget); // This is required to work with index's
        var currentMode = GadgetNBT.getMode(stack);

        var modeIndex = arrayOfModes.indexOf(currentMode);

        // Fix the mode or move it back to zero if the next index is outside of the list
        if (modeIndex == -1 || (++modeIndex > arrayOfModes.size())) {
            modeIndex = 0; // Use zero if for some reason we can't find the mode
        }

        var mode = arrayOfModes.get(modeIndex);
        GadgetNBT.setMode(stack, mode);

        return mode.getId();
    }

    public abstract GadgetTarget gadgetTarget();

    public static ItemStack getGadget(EntityClientPlayerMP player) {
        ItemStack heldItem = player.getMainHandItem();
        if (!(heldItem.getItem() instanceof BaseGadget)) {
            heldItem = player.getOffhandItem();
            if (!(heldItem.getItem() instanceof BaseGadget)) {
                return ItemStack.EMPTY;
            }
        }
        return heldItem;
    }

    public static BlockPos getHitPos(ItemActionContext context) {
        BlockPos anchorPos = GadgetNBT.getAnchorPos(context.stack());
        return anchorPos.equals(GadgetNBT.nullPos) ? context.pos() : anchorPos;
    }

    public boolean canUndo(Level level, Player player, ItemStack gadget) {
        BG2Data bg2Data = BG2Data.get(Objects.requireNonNull(level.getServer()).overworld());
        UUID undoUUID = GadgetNBT.peekUndoList(gadget);
        if (undoUUID == null) return false;
        ArrayList<StatePos> undoList = bg2Data.peekUndoList(undoUUID);
        for (StatePos statePos : undoList) {
            if (!level.isLoaded(statePos.pos)) {
                player.displayClientMessage(Component.translatable("buildinggadgets2.messages.undofailedunloaded", statePos.pos.toShortString()), true);
                return false;
            }
        }
        return true;
    }

    public void undo(Level level, Player player, ItemStack gadget) {
        if (!canUndo(level, player, gadget)) return;
        BG2Data bg2Data = BG2Data.get(Objects.requireNonNull(level.getServer()).overworld());
        UUID buildUUID = GadgetNBT.popUndoList(gadget);
        ServerTickHandler.stopBuilding(buildUUID);
        ArrayList<StatePos> undoList = bg2Data.popUndoList(buildUUID);
        if (undoList.isEmpty()) return;
        Collections.reverse(undoList);

        ArrayList<BlockPos> todoList = new ArrayList<>();
        for (StatePos statePos : undoList) {
            todoList.add(statePos.pos);
        }
        boolean giveItemsBack = !player.isCreative(); //Might want more conditions later?
        BuildingUtils.removeTickHandler(level, player, todoList, giveItemsBack, giveItemsBack, gadget);
    }
}
