package com.direwolf20.buildinggadgets2.setup;

import com.direwolf20.buildinggadgets2.client.KeyBindings;
import com.direwolf20.buildinggadgets2.client.events.EventKeyInput;
import com.direwolf20.buildinggadgets2.client.events.RenderLevelLast;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;

public class ClientSetup {
    public static void init(final FMLClientHandler event) {
        MinecraftForge.EVENT_BUS.register(KeyBindings.class);

        //Register our Render Events Class
        MinecraftForge.EVENT_BUS.register(RenderLevelLast.class);
        MinecraftForge.EVENT_BUS.register(EventKeyInput.class);
    }

    @SubscribeEvent
    public static void registerScreens(GuiScreenEvent event) {
//        event.register(Registration.TemplateManager_Container.get(), TemplateManagerGUI::new);
    }

//    @SubscribeEvent
//    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
//        //Register Block Entity Renders
//        event.registerBlockEntityRenderer(Registration.RenderBlock_BE.get(), RenderBlockBER::new);
//    }

//    @SubscribeEvent
//    public static void registerTooltipFactory(RegisterClientTooltipComponentFactoriesEvent event) {
//        //LOGGER.debug("Registering custom tooltip component factories for {}", Reference.MODID);
//        //event.register(EventTooltip.CopyPasteTooltipComponent.Data.class, EventTooltip.CopyPasteTooltipComponent::new);
//    }
}
