package com.direwolf20.buildinggadgets2.common.blocks;

import com.direwolf20.buildinggadgets2.common.blockentities.RenderBlockBE;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class RenderBlock extends Block
//    implements EntityBlock
{
    public RenderBlock() {
        super(Material.air);
        this.setResistance(20f)
            .setBlockUnbreakable();
    }

    @Override
    public int getRenderType() {
        return super.getRenderType();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canCollideCheck(int meta, boolean includeLiquid) {
        return false;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return (lvl, pos, blockState, t) -> {
                if (t instanceof RenderBlockBE tile) {
                    tile.tickClient();
                }
            };
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof RenderBlockBE tile) {
                tile.tickServer();
            }
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RenderBlockBE(pos, state);
    }


    //These 2 methods after the shadows under the block
    @Override
    public boolean propagatesSkylightDown(BlockState p_48740_, BlockGetter p_48741_, BlockPos p_48742_) {
        /*if (p_48741_.getBlockEntity(p_48742_) instanceof RenderBlockBE renderBlockBE && renderBlockBE.renderBlock != null && !(renderBlockBE.renderBlock.getBlock() instanceof RenderBlock) && !renderBlockBE.shrinking) {
            return renderBlockBE.renderBlock.propagatesSkylightDown(p_48741_, p_48742_);
        }*/
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
        /*if (p_48732_.getBlockEntity(p_48733_) instanceof RenderBlockBE renderBlockBE && renderBlockBE.renderBlock != null && !(renderBlockBE.renderBlock.getBlock() instanceof RenderBlock) && !renderBlockBE.shrinking) {
            float blockShade = renderBlockBE.renderBlock.getShadeBrightness(p_48732_, p_48733_);
            return blockShade;
        }*/
        return 1.0F;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        /*if (pLevel.getBlockEntity(pPos) instanceof RenderBlockBE renderBlockBE && renderBlockBE.renderBlock != null && !(renderBlockBE.renderBlock.getBlock() instanceof RenderBlock) && !renderBlockBE.shrinking) {
            return renderBlockBE.renderBlock.getOcclusionShape(pLevel, pPos);
        }*/
        return super.getOcclusionShape(pState, pLevel, pPos);
    }

    @Override
    @Deprecated
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return true;
    }
}
