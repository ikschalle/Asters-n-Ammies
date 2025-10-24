package com.github.ikschalle.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CoinPile extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty COINS = IntegerProperty.create("coins", 1,16);

    public CoinPile(Properties properties){
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(COINS, 1)
        );
    }

    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        double coin_layers = 0.25 * Math.floor((double) (blockState.getValue(COINS) + 3) / 4); // every layer is 4 coins, add more collision when new layer starts (terms in sequence 4n-3)
        return Shapes.box(0.015625D, 0D, 0.015625D, 0.984375D, coin_layers, 0.984375D);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) { // On place w/ context
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        if (blockstate.is(this)) {
            int i = blockstate.getValue(COINS);
            return blockstate.setValue(COINS, Math.min(i + 1, 16));
        } else {
            return this.defaultBlockState().setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
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

    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, COINS);
    }

}
