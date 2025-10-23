package com.github.ikschalle.block;

import com.github.ikschalle.util.GeneralUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CoinPile extends Block implements SimpleWaterloggedBlock {
    public static final IntegerProperty COINS = IntegerProperty.create("coins", 1,16);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final Supplier<VoxelShape> voxelShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.515625, 0, 0.515625, 0.984375, 0.25, 0.984375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.515625, 0.25, 0.515625, 0.984375, 0.5, 0.984375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.515625, 0.5, 0.515625, 0.984375, 0.75, 0.984375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.515625, 0.75, 0.515625, 0.984375, 1, 0.984375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.515625, 0.75, 0.015625, 0.984375, 1, 0.484375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.015625, 0.75, 0.015625, 0.484375, 1, 0.484375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.015625, 0.75, 0.515625, 0.484375, 1, 0.984375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.015625, 0.5, 0.015625, 0.484375, 0.75, 0.484375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.515625, 0.5, 0.015625, 0.984375, 0.75, 0.484375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.015625, 0.5, 0.515625, 0.484375, 0.75, 0.984375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.515625, 0.25, 0.015625, 0.984375, 0.5, 0.484375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.015625, 0.25, 0.015625, 0.484375, 0.5, 0.484375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.015625, 0.25, 0.515625, 0.484375, 0.5, 0.984375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.515625, 0, 0.015625, 0.984375, 0.25, 0.484375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.015625, 0, 0.015625, 0.484375, 0.25, 0.484375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.015625, 0, 0.515625, 0.484375, 0.25, 0.984375), BooleanOp.OR);

        return shape;
    };


    public static final Map<Direction, VoxelShape> SHAPE = net.minecraft.Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
        }
    });


    public CoinPile(Properties properties){
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(COINS, 1)
                .setValue(WATERLOGGED, Boolean.FALSE)
        );
    }


    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }


    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());
        if (blockstate.is(this)) {
            int i = blockstate.getValue(COINS);
            return blockstate.setValue(COINS, Math.min(16, i + 1));
        } else {
            return super.getStateForPlacement(context);
        }
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COINS, WATERLOGGED, FACING);
    }

}
