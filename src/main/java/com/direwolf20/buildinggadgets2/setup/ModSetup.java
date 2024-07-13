package com.direwolf20.buildinggadgets2.setup;

import com.direwolf20.buildinggadgets2.common.events.ServerTickHandler;
import com.direwolf20.buildinggadgets2.integration.AE2Integration;
import com.direwolf20.buildinggadgets2.integration.AE2Methods;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

public class ModSetup {
    public static void init() {
        MinecraftForge.EVENT_BUS.register(ServerTickHandler.class);
        if (AE2Integration.isLoaded()) {
            AE2Methods.registerItems();
        }
    }

    public static final CreativeTabs TAB_BUILDINGGADGETS2 = new CreativeTabs("Building Gadgets 2") {
        @Override
        public Item getTabIconItem() {
            return Registration.Building_Gadget;
        }
    };
}
