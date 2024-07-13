package com.direwolf20.buildinggadgets2.setup;

import com.direwolf20.buildinggadgets2.BuildingGadgets2;
import com.direwolf20.buildinggadgets2.common.blockentities.RenderBlockBE;
import com.direwolf20.buildinggadgets2.common.blockentities.TemplateManagerBE;
import com.direwolf20.buildinggadgets2.common.blocks.RenderBlock;
import com.direwolf20.buildinggadgets2.common.blocks.TemplateManager;
import com.direwolf20.buildinggadgets2.common.containers.TemplateManagerContainer;
import com.direwolf20.buildinggadgets2.common.items.*;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;

import java.util.function.Supplier;

import static com.direwolf20.buildinggadgets2.BuildingGadgets2.MODID;
import static com.direwolf20.buildinggadgets2.client.particles.ModParticles.PARTICLE_TYPES;

public class Registration {

//    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MODID);
//    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
//    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Registries.MENU, MODID);
//    private static final DeferredRegister<SoundEvent> SOUND_REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, BuildingGadgets2.MODID);
//    public static final Supplier<SoundEvent> BEEP = SOUND_REGISTRY.register("beep", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BuildingGadgets2.MODID, "beep")));

    public static void init() {
//        BLOCKS.register(eventBus);
//        ITEMS.register(eventBus);
//        BLOCK_ENTITIES.register(eventBus);
//        CONTAINERS.register(eventBus);
//        SOUND_REGISTRY.register(eventBus);
//        PARTICLE_TYPES.register(eventBus);
//        BG2DataComponents.genSettingToggles();
//        BG2DataComponents.genSettingValues();
//        BG2DataComponents.COMPONENTS.register(eventBus);
    }

    //Blocks
    public static final Block RenderBlock = registerBlock(RenderBlock::new, "render_block");
    public static final Block TemplateManager = registerBlock(TemplateManager::new, "template_manager");
    public static final Item TemplateManager_ITEM = registerItem(() -> new ItemBlock(TemplateManager), "template_manager");

    //BlockEntities (Not TileEntities - Honest)
//    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RenderBlockBE>> RenderBlock_BE = BLOCK_ENTITIES.register("renderblock", () -> BlockEntityType.Builder.of(RenderBlockBE::new, RenderBlock.get()).build(null));
//    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TemplateManagerBE>> TemplateManager_BE = BLOCK_ENTITIES.register("templatemanager", () -> BlockEntityType.Builder.of(TemplateManagerBE::new, TemplateManager.get()).build(null));
    //public static final RegistryObject<BlockEntityType<LaserConnectorBE>> LaserConnector_BE = BLOCK_ENTITIES.register("laserconnector", () -> BlockEntityType.Builder.of(LaserConnectorBE::new, LaserConnector.get()).build(null));

    //Items
    public static final Item Building_Gadget = registerItem(GadgetBuilding::new, "gadget_building");
    public static final Item Exchanging_Gadget = registerItem(GadgetExchanger::new, "gadget_exchanging");
    public static final Item CopyPaste_Gadget = registerItem(GadgetCopyPaste::new, "gadget_copy_paste");
    public static final Item CutPaste_Gadget = registerItem(GadgetCutPaste::new, "gadget_cut_paste");
    public static final Item Destruction_Gadget = registerItem(GadgetDestruction::new, "gadget_destruction");
    public static final Item Template = registerItem(TemplateItem::new, "template");
    public static final Item Redprint = registerItem(Redprint::new, "redprint");

    public static void registerAllTileEntity() {
        GameRegistry.registerTileEntity(RenderBlockBE.class, "renderblock");
        GameRegistry.registerTileEntity(TemplateManagerBE.class, "templatemanager");
    }

    public static <T extends Block> Block registerBlock(Supplier<T> block, String name) {
        return GameRegistry.registerBlock(block.get()
                .setBlockName(name)
                .setBlockTextureName(name),
            MODID);
    }

    public static <T extends Item> Item registerItem(Supplier<T> item, String name) {
        return GameRegistry.registerItem(item.get(), name, MODID);
    }

    //Containers
//    public static final DeferredHolder<MenuType<?>, MenuType<TemplateManagerContainer>> TemplateManager_Container = CONTAINERS.register("templatemanager",
//        () -> IMenuTypeExtension.create(TemplateManagerContainer::new));
}
