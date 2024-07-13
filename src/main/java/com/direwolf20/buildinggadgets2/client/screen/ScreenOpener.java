package com.direwolf20.buildinggadgets2.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class ScreenOpener {
    public static void openDestructionScreen(ItemStack itemstack) {
        Minecraft.getMinecraft().displayGuiScreen(new DestructionGUI(itemstack, false));
    }

    public static void openMaterialList(ItemStack itemstack) {
        Minecraft.getMinecraft().displayGuiScreen(new MaterialListGUI(itemstack));
    }
}
