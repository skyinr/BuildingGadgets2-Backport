package com.direwolf20.buildinggadgets2;

import com.direwolf20.buildinggadgets2.common.blockentities.TemplateManagerBE;
import com.direwolf20.buildinggadgets2.common.capabilities.EnergyStorageItemstack;
import com.direwolf20.buildinggadgets2.common.commands.BuildingGadgets2Commands;
import com.direwolf20.buildinggadgets2.common.items.BaseGadget;
import com.direwolf20.buildinggadgets2.common.network.PacketHandler;
import com.direwolf20.buildinggadgets2.setup.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import net.minecraftforge.common.ForgeModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = BuildingGadgets2.MODID, name = "building-gadgets2-backport", acceptedMinecraftVersions = "[1.7.10]")
public class BuildingGadgets2 {
    public static final String MODID = "buildinggadgets2";
    public static final Logger LOGGER = LogManager.getLogger();

    @SidedProxy(clientSide = "com.direwolf20.buildinggadgets2.setup.ClientProxy", serverSide = "com.direwolf20.buildinggadgets2.setup.CommonProxy")
    public static CommonProxy proxy;

    public BuildingGadgets2() {
        EventBus bus = FMLCommonHandler.instance().bus();
        // Register the deferred registry
        Registration.init();
        Config.register(container);

        eventBus.addListener(ModSetup::init);
        ModSetup.TABS.register(eventBus);
        eventBus.addListener(this::registerCapabilities);
        eventBus.addListener(PacketHandler::registerNetworking);
        ForgeModContainer.getConfig().EVENT_BUS.addListener(BuildingGadgets2Commands::registerCommands);

        if (FMLLoader.getDist().isClient()) {
            eventBus.addListener(ClientSetup::init);
        }
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        Registration.init();
        proxy.init(event);
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.EnergyStorage.ITEM,
            (itemStack, context) -> new EnergyStorageItemstack(((BaseGadget) itemStack.getItem()).getEnergyMax(), itemStack),
            Registration.Building_Gadget.get(),
            Registration.Exchanging_Gadget.get(),
            Registration.CopyPaste_Gadget.get(),
            Registration.CutPaste_Gadget.get(),
            Registration.Destruction_Gadget.get()
        );
        event.registerBlock(Capabilities.ItemHandler.BLOCK,
            (level, pos, state, be, side) -> ((TemplateManagerBE) be).itemHandler,
            // blocks to register for
            Registration.TemplateManager.get());
    }
}
