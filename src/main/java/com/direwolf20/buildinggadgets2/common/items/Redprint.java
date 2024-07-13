package com.direwolf20.buildinggadgets2.common.items;

import com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Redprint extends Item {

    public Redprint() {
        this.setMaxStackSize(1);

        AnimatedTooltipHandler.addItemTooltip(new ItemStack(this),()->"buildinggadgets2.templatename");
    }
}
