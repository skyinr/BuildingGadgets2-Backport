package com.direwolf20.buildinggadgets2.client.particles;

import com.direwolf20.buildinggadgets2.client.particles.fluidparticle.FluidFlowParticle;
import com.direwolf20.buildinggadgets2.client.particles.itemparticle.ItemFlowParticle;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

//@EventBusSubscriber(modid = BuildingGadgets2.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ParticleRenderDispatcher {

    @SubscribeEvent
    public static void registerProviders(RegisterParticleProvidersEvent evt) {
        evt.registerSpecial(ModParticles.ITEMFLOWPARTICLE.get(), ItemFlowParticle.FACTORY);
        evt.registerSpecial(ModParticles.FLUIDFLOWPARTICLE.get(), FluidFlowParticle.FACTORY);
    }
}
