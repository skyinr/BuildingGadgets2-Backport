package com.direwolf20.buildinggadgets2.common.items;

import com.direwolf20.buildinggadgets2.api.gadgets.GadgetTarget;
import com.direwolf20.buildinggadgets2.common.blocks.RenderBlock;
import com.direwolf20.buildinggadgets2.common.events.ServerBuildList;
import com.direwolf20.buildinggadgets2.common.events.ServerTickHandler;
import com.direwolf20.buildinggadgets2.common.worlddata.BG2Data;
import com.direwolf20.buildinggadgets2.config.BG2Config;
import com.direwolf20.buildinggadgets2.util.BuildingUtils;
import com.direwolf20.buildinggadgets2.util.GadgetNBT;
import com.direwolf20.buildinggadgets2.util.GadgetUtils;
import com.direwolf20.buildinggadgets2.util.context.ItemActionContext;
import com.direwolf20.buildinggadgets2.util.datatypes.StatePos;
import com.direwolf20.buildinggadgets2.util.modes.BaseMode;
import com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;

import java.util.*;

import static com.direwolf20.buildinggadgets2.util.BuildingUtils.hasEnoughEnergy;
import static com.direwolf20.buildinggadgets2.util.BuildingUtils.useEnergy;

public class GadgetExchanger extends BaseGadget {
    public GadgetExchanger() {
        super();
    }

    @Override
    public int getEnergyMax() {
        return BG2Config.EXCHANGINGGADGET_MAXPOWER;
    }

    @Override
    public int getEnergyCost() {
        return BG2Config.EXCHANGINGGADGET_COST;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer p_77624_2_, List<String> p_77624_3_, boolean p_77624_4_) {
        AnimatedTooltipHandler.addItemTooltip(itemStack,
            AnimatedTooltipHandler.translatedText("buildinggadgets2.tooltips.mode"));
        AnimatedTooltipHandler.addItemTooltip(itemStack,
            AnimatedTooltipHandler.translatedText("buildinggadgets2.tooltips.range"));
        AnimatedTooltipHandler.addItemTooltip(itemStack,
            AnimatedTooltipHandler.translatedText("buildinggadgets2.tooltips.blockstate"));
    }

    @Override
    public EnumAction getItemUseAction(ItemStack gadget) {
        Block setState = GadgetNBT.getGadgetBlockState(gadget);

        if (setState.getMaterial() == Material.air) {
            return EnumAction.none;
        }
        BaseMode mode = GadgetNBT.getMode(gadget);

        return super.getItemUseAction(gadget);
    }

    @Override
    EnumAction<ItemStack> onAction(ItemActionContext context) {
        var gadget = context.stack();

        BlockState setState = GadgetNBT.getGadgetBlockState(gadget);
        if (setState.isAir()) return InteractionResultHolder.pass(gadget);

        var mode = GadgetNBT.getMode(gadget);
        ArrayList<StatePos> buildList = mode.collect(context.hitResult().getDirection(), context.player(), getHitPos(context), setState);

        UUID buildUUID = BuildingUtils.exchange(context.level(), context.player(), buildList, getHitPos(context), gadget, true, true);
        GadgetUtils.addToUndoList(context.level(), gadget, new ArrayList<>(), buildUUID);
        GadgetNBT.clearAnchorPos(gadget);
        return InteractionResultHolder.success(gadget);
    }

    /**
     * Selects the block assuming you're actually looking at one
     */
    @Override
    InteractionResultHolder<ItemStack> onShiftAction(ItemActionContext context) {
        BlockState blockState = context.level().getBlockState(context.pos());
        if (!GadgetUtils.isValidBlock(blockState, context.level(), context.pos()) || blockState.getBlock() instanceof RenderBlock) {
            context.player().displayClientMessage(Component.translatable("buildinggadgets2.messages.invalidblock"), true);
            return super.onShiftAction(context);
        }
        if (GadgetUtils.setBlock(context.stack(), blockState))
            return InteractionResultHolder.success(context.stack());

        return super.onShiftAction(context);
    }

    /**
     * Undo is handled differently for exchanger - we want to look at what the blocks that were replaced were previously, and grab them from the players inventory to undo it.
     */
    @Override
    public void undo(Level level, Player player, ItemStack gadget) {
        if (!canUndo(level, player, gadget)) return;
        BG2Data bg2Data = BG2Data.get(Objects.requireNonNull(level.getServer()).overworld());
        UUID buildUUID = GadgetNBT.popUndoList(gadget);
        ServerTickHandler.stopBuilding(buildUUID);
        ArrayList<StatePos> undoList = bg2Data.popUndoList(buildUUID);
        if (undoList.isEmpty()) return;
        Collections.reverse(undoList);
        UUID newBuildUUID = UUID.randomUUID();

        for (StatePos pos : undoList) {
            if (pos.state.isAir()) continue; //Since we store air now
            if (!pos.state.canSurvive(level, pos.pos)) continue;
            if (!player.isCreative() && !hasEnoughEnergy(gadget)) {
                player.displayClientMessage(Component.translatable("buildinggadgets2.messages.outofpower"), true);
                break; //Break out if we're out of power
            }
            if (!player.isCreative())
                useEnergy(gadget);
            ServerTickHandler.addToMap(newBuildUUID, new StatePos(pos.state, pos.pos), level, GadgetNBT.getRenderTypeByte(gadget), player, true, true, gadget, ServerBuildList.BuildType.EXCHANGE, true, GadgetNBT.nullPos);
        }
    }

    /**
     * Used to retrieve the correct building modes in various places
     */
    @Override
    public GadgetTarget gadgetTarget() {
        return GadgetTarget.EXCHANGING;
    }

    /**
     * For Silk Touch - The tag that allows silk touch ALSO allows fortune, so I have to deny fortune after adding the tag....SUPER FUN!
     */
    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 3;
    }

    @Override
    public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
        return super.isPrimaryItemFor(stack, enchantment) && canAcceptEnchantments(enchantment);
    }

    private boolean canAcceptEnchantments(Holder<Enchantment> enchantment) {
        return !enchantment.is(Enchantments.FORTUNE);
    }
}
