package com.direwolf20.buildinggadgets2.common.items;

import com.direwolf20.buildinggadgets2.client.screen.ScreenOpener;
import com.direwolf20.buildinggadgets2.util.GadgetNBT;
import com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class TemplateItem extends Item {

    public TemplateItem() {
        this.setMaxStackSize(1);

        AnimatedTooltipHandler.addItemTooltip(new ItemStack(this), () -> "buildinggadgets2.templatename");
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        if (!player.isSneaking()) {
            return super.onItemUse(itemStack, player, world, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_);
        }
        if (world.isRemote) {
            ScreenOpener.openMaterialList(itemStack);
        }
        return super.onItemUse(itemStack, player, world, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_);
    }
}
