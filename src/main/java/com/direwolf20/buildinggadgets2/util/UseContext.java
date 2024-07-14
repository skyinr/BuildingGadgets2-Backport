package com.direwolf20.buildinggadgets2.util;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

import java.util.Arrays;

public class UseContext {
    private final World world;
    private final Block setState;
    private final int xPos;
    private final int yPos;
    private final int zPos;
    private final Direction hitSide;
    private final EntityPlayer player;

    private final boolean isFuzzy;
    private final boolean placeOnTop;
    private final int range;
    private final boolean rayTraceFluid;
    private final boolean isConnected;

    public UseContext(World world, EntityPlayer player, Block setState, int xPos, int yPos, int zPos, ItemStack gadget, Direction hitSide, boolean placeOnTop, boolean isConnected) {
        this.world = world;
        this.setState = setState;
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.player = player;

        this.range = GadgetNBT.getToolRange(gadget);
        this.isFuzzy = GadgetNBT.getFuzzy(gadget);
        this.rayTraceFluid = GadgetNBT.shouldRayTraceFluid(gadget);
        this.hitSide = hitSide;

        this.isConnected = isConnected;
        this.placeOnTop = placeOnTop;
    }

    public UseContext(World world, EntityPlayer player, Block setState, int xPos, int yPos, int zPos, ItemStack gadget, Direction hitSide, boolean isConnected) {
        this(world, player, setState, xPos, yPos, zPos, gadget, hitSide, false, isConnected);
    }

    //TODO Backport
//    public BlockPlaceContext createBlockUseContext() {
//        return new BlockPlaceContext(
//                new UseOnContext(
//                        player,
//                        InteractionHand.MAIN_HAND,
//                        VectorHelper.getLookingAt(player, this.rayTraceFluid)
//                )
//        );
//    }

    public boolean isConnected() {
        return isConnected;
    }

    public Block getWorldState(int xPos, int yPos, int zPos) {
        return world.getBlock(xPos, yPos, zPos);
    }

    public World getWorld() {
        return world;
    }

    public Block getSetState() {
        return setState;
    }

    public boolean isFuzzy() {
        return isFuzzy;
    }

    public boolean isRayTraceFluid() {
        return rayTraceFluid;
    }

    public boolean isPlaceOnTop() {
        return placeOnTop;
    }

    public int getRange() {
        return range;
    }

    public int[] getStartPos() {
        return new int[xPos,yPos, zPos];
    }

    public Direction getHitSide() {
        return this.hitSide;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "UseContext{" +
            "world=" + world +
            ", setState=" + setState +
            ", startPos=" + Arrays.toString(getStartPos()) +
            ", hitSide=" + hitSide +
            ", isFuzzy=" + isFuzzy +
            ", placeOnTop=" + placeOnTop +
            ", range=" + range +
            ", rayTraceFluid=" + rayTraceFluid +
            '}';
    }
}
