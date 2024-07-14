package com.direwolf20.buildinggadgets2.util;

import com.direwolf20.buildinggadgets2.common.blocks.RenderBlock;
import com.direwolf20.buildinggadgets2.common.tags.BG2BlockTags;
import com.direwolf20.buildinggadgets2.common.worlddata.BG2Data;
import com.direwolf20.buildinggadgets2.setup.Registration;
import com.direwolf20.buildinggadgets2.util.datatypes.StatePos;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GadgetUtils {
    /*private static final ImmutableList<Property<?>> ALLOWED_PROPERTIES = ImmutableList.of(
            BlockStateProperties.FACING, BlockStateProperties.AXIS, BlockStateProperties.HORIZONTAL_FACING,
            BlockStateProperties.CHEST_TYPE, BlockStateProperties.SLAB_TYPE
    );*/

    //TODO backport
//    private static final ImmutableList<Property<?>> DENY_PROPERTIES = ImmutableList.of(
//            BlockStateProperties.AGE_1, BlockStateProperties.AGE_2, BlockStateProperties.AGE_3, BlockStateProperties.AGE_4,
//            BlockStateProperties.AGE_5, BlockStateProperties.AGE_7, BlockStateProperties.AGE_15, BlockStateProperties.AGE_25,
//            DoublePlantBlock.HALF, BlockStateProperties.WATERLOGGED, BlockStateProperties.LIT, BlockStateProperties.HAS_RECORD,
//            BlockStateProperties.HAS_BOOK, BlockStateProperties.OPEN, BlockStateProperties.STAGE
//    );

    public static boolean isValidBlock(Block block, World world, int xPos, int yPos, int zPos) {
        if (OreDictionary.doesOreNameExist(BG2BlockTags.BG2DENY.toString())) return false;
        if (block.getDamageValue(world, xPos, yPos, zPos) < 0) return false;
        if (block instanceof IFluidBlock) return false;
        // ——————Old Code——————
//        if (block.is(BG2BlockTags.BG2DENY)) return false;
//        if (block.getDestroySpeed(level, blockPos) < 0) return false;
//        if (!block.getFluidState().isEmpty() && !block.getFluidState().isSource()) return false;
        return true;
    }

    public static boolean isValidDestroyBlock(Block block, World world, int xPos, int yPos, int zPos) {
        if (block.getMaterial() == Material.air) return false;
        if (block.getDamageValue(world, xPos, yPos, zPos) < 0) return false;
        if (block instanceof RenderBlock) return false;
        // ——————Old Code——————
//        if (block.isAir()) return false;
//        if (block.getDestroySpeed(world, blockPos) < 0) return false;
//        if (block.getBlock() instanceof RenderBlock) return false;
        return true;
    }

    public static boolean setBlock(ItemStack gadget, Block block) {
        Block placeState = cleanBlockState(block);
        GadgetNBT.setGadgetBlock(gadget, placeState);
        return true;
    }

    public static ItemStack getSimpleItemForBlock(Block block) {
        return new ItemStack(block);
    }

    public static ItemStack getItemForBlock(Block block, World world, int xPos, int yPos, int zPos, EntityPlayer player) {
        Minecraft minecraft = Minecraft.getMinecraft();
        float blockReachDistance = minecraft.playerController.getBlockReachDistance();
        return block.getPickBlock(player.rayTrace(blockReachDistance, 1.0F), world, xPos, yPos, zPos, player);
    }

    public static List<ItemStack> getDropsForBlockState(WorldServer world, int xPos, int yPos, int zPos, Block block, EntityPlayer player) {
        ItemStack tempStack = new ItemStack(Registration.Exchanging_Gadget);
        tempStack.addEnchantment(Enchantment.silkTouch, 1);

        List<ItemStack> drops = new ArrayList<>();
        try {
            drops = new ArrayList<>(getDropsForBlockStateGadget(world, xPos, yPos, zPos, block, tempStack));
        } catch (Exception e) {
            //No-op
        }
        ItemStack baseItem;
        if (player != null) {
            baseItem = getItemForBlock(block, world, xPos, yPos, zPos, player);
        } else {
            baseItem = getSimpleItemForBlock(block);
        }
        if (drops.stream().filter(e -> ItemStack.areItemStacksEqual(e, baseItem)).toList().isEmpty()) {
            drops = new ArrayList<>();
            if (!baseItem.isItemStackDamageable()) {
                drops.add(baseItem);
            }
            return drops;
        }
        return drops;

        // ——————Old Code——————
//        ItemStack tempStack = new ItemStack(Registration.Exchanging_Gadget.get());
//        HolderLookup.RegistryLookup<Enchantment> registrylookup = level.getServer().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
//        tempStack.enchant(registrylookup.getOrThrow(Enchantments.SILK_TOUCH), 1);
//        List<ItemStack> drops = new ArrayList<>();
//        try {
//            drops = new ArrayList<>(getDropsForBlockStateGadget(level, blockPos, blockState, tempStack));
//        } catch (Exception e) {
//            //No-Op
//        }
//        ItemStack baseItem;
//        if (player != null)
//            baseItem = getItemForBlock(blockState, level, blockPos, , player); //Sometimes we have the player, sometimes not!
//        else
//            baseItem = getSimpleItemForBlock(blockState);
//        if (drops.stream().filter(e -> ItemStack.isSameItem(e, baseItem)).toList().isEmpty()) { //If the item we expect to find isn't in the drops list, something weird happened, like wheat seeds from tall grass
//            drops = new ArrayList<>();
//            if (!baseItem.isEmpty())
//                drops.add(baseItem);
//            return drops;
//        }
//        return drops;
    }

    public static List<ItemStack> getDropsForBlockStateGadget(WorldServer world, int xPos, int yPos, int zPos, Block block, ItemStack gadget) {
        return block.getDrops(world, xPos, yPos, zPos, 0, Integer.MAX_VALUE);
    }

    public static Block cleanBlockState(Block sourceState) {
        return sourceState;
        // ——————Old Code——————
//        BlockState placeState = sourceState.getBlock().defaultBlockState();
//        for (Property<?> prop : sourceState.getProperties()) {
//            if (!DENY_PROPERTIES.contains(prop)) {
//                placeState = applyProperty(placeState, sourceState, prop);
//            }
//        }
//        return placeState;
    }

    public static void addToUndoList(World world, ItemStack gadget, ArrayList<StatePos> buildList, UUID uuid) {
        BG2Data bg2Data = BG2Data.get(DimensionManager.getWorld(0));
        bg2Data.addToUndoList(uuid, buildList, world);
        GadgetNBT.addToUndoList(gadget, uuid, bg2Data);
        //——————Old Code——————
//        BG2Data bg2Data = BG2Data.get(Objects.requireNonNull(level.getServer()).overworld());
//        bg2Data.addToUndoList(uuid, buildList, level);
//        GadgetNBT.addToUndoList(gadget, uuid, bg2Data);
    }

    public static void addToUndoList(World world, ItemStack gadget, ArrayList<StatePos> buildList) {
        addToUndoList(world, gadget, buildList, UUID.randomUUID());
    }

    //——————Old Code——————
//    private static <T extends Comparable<T>> BlockState applyProperty(BlockState state, BlockState from, Property<T> prop) {
//        return state.setValue(prop, from.getValue(prop));
//    }

    public static AxisAlignedBB getSquareArea(int xPos, int yPos, int zPos, ForgeDirection face, int range) {
        return switch (face) {
            case UP, DOWN ->
                // If you're looking up or down, the area will extend east-west and north-south
                AxisAlignedBB.getBoundingBox(xPos - range, yPos, zPos - range, xPos + range, yPos, zPos + range);
            case NORTH, SOUTH ->
                // If you're looking north or south, the area will extend up-down and east-west
                AxisAlignedBB.getBoundingBox(xPos - range, yPos - range, zPos, xPos + range, yPos + range, zPos);
            case EAST, WEST ->
                // If you're looking east or west, the area will extend up-down and north-south
                AxisAlignedBB.getBoundingBox(xPos, yPos - range, zPos - range, xPos, yPos + range, zPos + range);
            default -> throw new IllegalStateException("Unexpected value: " + face);
        };
    }

    public static ArrayList<StatePos> getDestructionArea(World level, int xPos, int yPos, int zPos, ForgeDirection face, EntityPlayer player, ItemStack gadget) {
        int depth = GadgetNBT.getToolValue(gadget, GadgetNBT.IntSettings.DEPTH.getName());

        if (gadget.isItemStackDamageable() || depth == 0 || !player.isBlocking()) {
            return new ArrayList<>();
        }

        boolean vertical = face.getRotation(ForgeDirection.UP) != ForgeDirection.UNKNOWN;

        ForgeDirection up = vertical ? ForgeDirection.getOrientation(player.getTeleportDirection()) : ForgeDirection.UP;
        ForgeDirection down = up.getOpposite();
        ForgeDirection right = vertical? up.getOpposite(): face.getOpposite();
        ForgeDirection left = right.getOpposite();

        //TODO code: 2024-07-15 1:32
        AxisAlignedBB box = VecHelpers.aabbFromBlockPos()

        //——————Old Code——————
//        int depth = GadgetNBT.getToolValue(gadget, GadgetNBT.IntSettings.DEPTH.getName());
//
//        if (gadget.isEmpty() || depth == 0 || !player.mayBuild())
//            return new ArrayList<>();
//
//        boolean vertical = face.getAxis().isVertical();
//        ForgeDirection up = vertical ? player.getDirection() : ForgeDirection.UP;
//        ForgeDirection down = up.getOpposite();
//        ForgeDirection right = vertical ? up.getClockWise() : face.getCounterClockWise();
//        ForgeDirection left = right.getOpposite();
//
//        BlockPos first = pos.relative(left, GadgetNBT.getToolValue(gadget, GadgetNBT.IntSettings.LEFT.getName())).relative(up, GadgetNBT.getToolValue(gadget, GadgetNBT.IntSettings.UP.getName()));
//        BlockPos second = pos.relative(right, GadgetNBT.getToolValue(gadget, GadgetNBT.IntSettings.RIGHT.getName()))
//            .relative(down, GadgetNBT.getToolValue(gadget, GadgetNBT.IntSettings.DOWN.getName()))
//            .relative(face.getOpposite(), depth - 1);
//
//        //boolean isFluidOnly = getIsFluidOnly(gadget); //Todo
//        AABB box = VecHelpers.aabbFromBlockPos(first, second);
//        ArrayList<StatePos> returnList = new ArrayList<>();
//        BlockPos.betweenClosedStream(box).map(BlockPos::immutable).forEach(blockPos -> {
//            BlockState blockState = level.getBlockState(blockPos);
//            if (!level.isClientSide) { //Only check these on server side
//                if (!level.mayInteract(player, blockPos))
//                    return; //Chunk Protection like spawn and FTB Utils
//                BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, blockPos, level.getBlockState(blockPos), player);
//                if (NeoForge.EVENT_BUS.post(event).isCanceled()) return;
//            }
//            if (blockState.hasBlockEntity() && !GadgetNBT.getSetting(gadget, GadgetNBT.ToggleableSettings.AFFECT_TILES.getName()))
//                return;
//            if (isValidDestroyBlock(blockState, level, blockPos))
//                returnList.add(new StatePos(blockState, blockPos));
//        });
//        return returnList;
    }

    //Because contains doesn't use <= just <
    public static boolean direContains(AABB aabb, double x, double y, double z) {
        return x >= aabb.minX && x <= aabb.maxX && y >= aabb.minY && y <= aabb.maxY && z >= aabb.minZ && z <= aabb.maxZ;
    }

    public static boolean direContains(AABB aabb, BlockPos pos) {
        return direContains(aabb, xPos, yPos, zPos);
    }
}
