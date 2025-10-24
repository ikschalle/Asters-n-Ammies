package com.github.ikschalle.block;

import com.github.ikschalle.util.GeneralUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CoinPile extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty COINS = IntegerProperty.create("coins", 1,16);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Shapes.box(0.5D,0.01D,0.5D,1,0.25D,1D),
            Shapes.box(0D,0.01D,0.5D,0.5D,0.25D,1D),
            Shapes.box(0.5D,0.01D,0D,1D,0.25D,0.5D),
            Shapes.box(0D,0.01D,0D,0.5D,0.25D,0.5D),
            Shapes.box(0.5D,0.25D,0.5D,1D,0.5D,1D),
            Shapes.box(0D,0.25D,0.5D,0.5D,0.5D,1D),
            Shapes.box(0.5D,0.25D,0D,1D,0.5D,0.5D),
            Shapes.box(0D,0.25D,0D,0.5D,0.5D,0.5D),
            Shapes.box(0.5D,0.5D,0.5D,1D,0.75D,1D),
            Shapes.box(0D,0.5D,0.5D,0.5D,0.75D,1D),
            Shapes.box(0.5D,0.5D,0D,1D,0.75D,0.5D),
            Shapes.box(0D,0.5D,0D,0.5D,0.75D,0.5D),
            Shapes.box(0.5D,0.75D,0.5D,1D,1D,1D),
            Shapes.box(0D,0.75D,0.5D,0.5D,1D,1D),
            Shapes.box(0.5D,0.75D,0D,1D,1D,0.5D),
            Shapes.box(0D,0.75D,0D,0.5D,1D,0.5D)
    };

    public CoinPile(Properties properties){
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(COINS, 1)
                .setValue(WATERLOGGED, Boolean.FALSE)
        );
    }

    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        int coins = blockState.getValue(COINS)-1;
        VoxelShape shape = Shapes.empty();

        for (int i = 0; i <= coins ; i++) {
            shape = Shapes.join(shape, SHAPES[i], BooleanOp.OR);
        }
        return shape;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) { // On place w/ context
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());
        if (blockstate.is(this)) {
            int i = blockstate.getValue(COINS);
            return blockstate.setValue(COINS, Math.min(i + 1, 16));
        } else {
            return super.getStateForPlacement(context);
        }
    }


    public boolean canBeReplaced(@NotNull BlockState blockState, BlockPlaceContext blockPlaceContext) {
        return !blockPlaceContext.isSecondaryUseActive() && blockPlaceContext.getItemInHand().is(this.asItem()) && blockState.getValue(COINS) < 17 || super.canBeReplaced(blockState, blockPlaceContext);
    }


    public BlockState updateShape(BlockState blockState, Direction direction, BlockState state, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        if (!blockState.canSurvive(levelAccessor, blockPos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            if (blockState.getValue(WATERLOGGED)) {
                levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
            }

            return super.updateShape(blockState, direction, state, levelAccessor, blockPos, blockPos1);
        }
    }


    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COINS, WATERLOGGED, FACING);
    }

}
