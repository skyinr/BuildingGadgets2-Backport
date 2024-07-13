package com.direwolf20.buildinggadgets2.common.blocks;

import com.direwolf20.buildinggadgets2.common.blockentities.TemplateManagerBE;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TemplateManager extends Block
//    implements EntityBlock
{
//    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public TemplateManager() {
        super(Material.rock);
        this.setHardness(2f)
            .setResistance(2f);

    }

    @Override
    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
        if (blockBroken != this) {
            TileEntity tileEntity = worldIn.getTileEntity(x, y, z);
            if (tileEntity != null && tileEntity instanceof TemplateManagerBE) {
                ((TemplateManagerBE) tileEntity).itemHandler.isItemValid();
            }
        }
        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    //    @Override
//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
//        super.createBlockStateDefinition(builder);
//        builder.add(FACING);
//    }

//    @Override
//    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
//        if (newState.getBlock() != this) {
//            BlockEntity blockEntity = worldIn.getBlockEntity(pos);
//            if (blockEntity != null && blockEntity instanceof TemplateManagerBE) {
//                var cap = worldIn.getCapability(Capabilities.ItemHandler.BLOCK, pos, state, blockEntity, null);
//                if (cap != null) {
//                    for (int i = 0; i < cap.getSlots(); ++i) {
//                        Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), cap.getStackInSlot(i));
//                    }
//                }
//            }
//
//        }
//        super.onRemove(state, worldIn, pos, newState, isMoving);
//    }

//    @Nullable
//    @Override
//    public BlockState getStateForPlacement(BlockPlaceContext context) {
//        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
//    }

//    @Nullable
//    @Override
//    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
//        return new TemplateManagerBE(blockPos, blockState);
//    }
//
//    @Override
//    public InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult hit) {
//        if (level.isClientSide)
//            return InteractionResult.SUCCESS;
//
//        BlockEntity te = level.getBlockEntity(blockPos);
//        if (!(te instanceof TemplateManagerBE templateManagerBE))
//            return InteractionResult.FAIL;
//
//        ((ServerPlayer) player).openMenu(new SimpleMenuProvider(
//                (windowId, playerInventory, playerEntity) -> new TemplateManagerContainer(windowId, playerInventory, templateManagerBE), Component.translatable("")), (buf -> {
//            buf.writeBlockPos(blockPos);
//        }));
//        return InteractionResult.SUCCESS;
//    }
}
