package com.direwolf20.buildinggadgets2.util.context;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Simple context to share common required fields for a item action.
 */
public record ItemActionContext(
        BlockPos pos,
        Vec3 hitResult,
        EntityPlayer player,
        World level,
        ItemStack stack
) {
}
