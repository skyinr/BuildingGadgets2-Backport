package com.direwolf20.buildinggadgets2.util;

import net.minecraft.util.AxisAlignedBB;

public class VecHelpers {
    public static AxisAlignedBB aabbFromBlockPos(int xStart, int yStart, int zStart, int xEnd, int yEnd, int zEnd) {
        return AxisAlignedBB.getBoundingBox(xStart, yStart, zStart, xEnd, yEnd, zEnd);
    }
}
