package com.direwolf20.buildinggadgets2.common.items;

import com.direwolf20.buildinggadgets2.api.gadgets.GadgetTarget;
import com.direwolf20.buildinggadgets2.client.screen.ScreenOpener;
import com.direwolf20.buildinggadgets2.common.events.ServerBuildList;
import com.direwolf20.buildinggadgets2.common.events.ServerTickHandler;
import com.direwolf20.buildinggadgets2.common.worlddata.BG2Data;
import com.direwolf20.buildinggadgets2.config.BG2Config;
import com.direwolf20.buildinggadgets2.util.BuildingUtils;
import com.direwolf20.buildinggadgets2.util.GadgetNBT;
import com.direwolf20.buildinggadgets2.util.GadgetUtils;
import com.direwolf20.buildinggadgets2.util.VectorHelper;
import com.direwolf20.buildinggadgets2.util.context.ItemActionContext;
import com.direwolf20.buildinggadgets2.util.datatypes.StatePos;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

import java.util.*;

public class GadgetDestruction extends BaseGadget {
    public GadgetDestruction() {
        super();
    }

    @Override
    public int getEnergyMax() {
        return BG2Config.DESTRUCTIONGADGET_MAXPOWER;
    }

    @Override
    public int getEnergyCost() {
        return BG2Config.DESTRUCTIONGADGET_COST;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return;
        }

        tooltip.add(Component.translatable("buildinggadgets2.voidwarning")
                .withStyle(ChatFormatting.RED));

        boolean sneakPressed = Screen.hasShiftDown();

        if (sneakPressed) {

        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack gadget = player.getItemInHand(hand);

        BlockHitResult lookingAt = VectorHelper.getLookingAt(player, gadget);
        ItemActionContext context = new ItemActionContext(lookingAt.getBlockPos(), lookingAt, player, level, hand, gadget);

        if (player.isShiftKeyDown()) {
            return this.onShiftAction(context);
        }

        if (level.isClientSide()) //No client
            return InteractionResultHolder.success(gadget);

        return this.onAction(context);
    }

    @Override
    InteractionResultHolder<ItemStack> onAction(ItemActionContext context) {
        var gadget = context.stack();

        BlockPos anchor = GadgetNBT.getAnchorPos(gadget);
        Direction anchorSide = GadgetNBT.getAnchorSide(gadget);

        if (context.level().getBlockState(VectorHelper.getLookingAt(context.player(), gadget).getBlockPos()) == Blocks.AIR.defaultBlockState() && anchor == null)
            return InteractionResultHolder.pass(gadget);

        BlockPos startBlock = getHitPos(context);
        Direction facing = (anchorSide == null) ? context.hitResult().getDirection() : anchorSide;

        ArrayList<StatePos> destroyList = GadgetUtils.getDestructionArea(context.level(), startBlock, facing, context.player(), gadget);
        ArrayList<BlockPos> destroyPosList = new ArrayList<>();
        destroyList.forEach(e -> destroyPosList.add(e.pos));
        destroyPosList.sort(Comparator.comparingDouble(blockPos -> blockPos.distSqr(context.player().blockPosition())));

        UUID buildUUID = BuildingUtils.removeTickHandler(context.level(), context.player(), destroyPosList, false, true, gadget);
        GadgetUtils.addToUndoList(context.level(), gadget, new ArrayList<>(), buildUUID); //If we placed anything at all, add to the undoList
        GadgetNBT.clearAnchorPos(gadget);
        return InteractionResultHolder.success(gadget);
    }

    @Override
    InteractionResultHolder<ItemStack> onShiftAction(ItemActionContext context) {
        if (context.level().isClientSide)
            ScreenOpener.openDestructionScreen(context.stack());

        return super.onShiftAction(context);
    }

    /**
     * Undo is handled differently for destroyer - since we voided the blocks we removed we can just return them for free
     */
    @Override
    public void undo(Level level, Player player, ItemStack gadget) {
        if (!canUndo(level, player, gadget)) return;
        BG2Data bg2Data = BG2Data.get(Objects.requireNonNull(level.getServer()).overworld());
        UUID buildUUID = GadgetNBT.popUndoList(gadget);
        UUID newBuildUUID = UUID.randomUUID(); //Use a new build UUID for the undo-building process
        ServerTickHandler.stopBuilding(buildUUID);
        ArrayList<StatePos> undoList = bg2Data.popUndoList(buildUUID);
        if (undoList.isEmpty()) return;
        Collections.reverse(undoList); //Undo backwards :)

        for (StatePos statePos : undoList) {
            if (statePos.state.isAir()) continue; //Since we store air now
            ServerTickHandler.addToMap(newBuildUUID, statePos, level, GadgetNBT.getRenderTypeByte(gadget), player, false, false, gadget, ServerBuildList.BuildType.UNDO_DESTROY, false, BlockPos.ZERO);
        }
    }

    /**
     * Used to retrieve the correct building modes in various places
     */
    @Override
    public GadgetTarget gadgetTarget() {
        return GadgetTarget.DESTRUCTION;
    }
}
