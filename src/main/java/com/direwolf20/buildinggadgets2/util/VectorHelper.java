package com.direwolf20.buildinggadgets2.util;

import com.direwolf20.buildinggadgets2.config.BG2Config;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.Fluid;

/**
 * @implNote The main reason behind this is so we have control over the RayTraceContext,
 * this means that we can use COLLIDER so it traces through non-collidable objects
 */
public class VectorHelper {
    public static Vec3 getLookingAt(EntityPlayer player, ItemStack tool) {
//        return getLookingAt(player, GadgetNBT.shouldRayTraceFluid(tool) ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE);
        throw new RuntimeException("Not Backport");
    }

    public static Vec3 getLookingAt(EntityPlayer player, boolean shouldRayTrace) {
//        return getLookingAt(player, shouldRayTrace ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE);
        throw new RuntimeException("Not Backport");
    }

    public static Vec3 getLookingAt(EntityPlayer player, Fluid rayTraceFluid) {
//        double rayTraceRange = BG2Config.RAYTRACE_RANGE;
//        HitResult result = player.pick(rayTraceRange, 0f, rayTraceFluid != ClipContext.Fluid.NONE);
        //TODO Not Backport

        return player.getPosition(1.0F);
    }
}
