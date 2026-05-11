package com.github.ikschalle.block;

import com.github.ikschalle.Asters_n_Ammies;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;

public class HungeringHogBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public final int ASTERETTE_RATIO = 64;
    public final int ASTER_RATIO = 1;
    public static final MapCodec<HungeringHogBlock> CODEC = simpleCodec(HungeringHogBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final VoxelShape X_AXIS_AABB = Block.box(
            3,1.75,4,
            13,9.75,12
    );
    public static final VoxelShape Z_AXIS_AABB = Block.box(
            4,1.75,3,
            12,9.75,13
    );

    public HungeringHogBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        Direction direction = blockState.getValue(FACING);
        return direction.getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());

        return this.defaultBlockState()
                .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER)
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        Item item = stack.getItem();
        Item aster = Asters_n_Ammies.ASTER_ITEM.asItem();
        Item asterette = Asters_n_Ammies.ASTERETTE_ITEM.asItem();
        int held_quantity = stack.getCount();

        if (item == aster) {
            tryConvert(held_quantity,ASTER_RATIO, ASTERETTE_RATIO, asterette, stack, player, level, pos);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        } else if (item == asterette) {
            tryConvert(held_quantity,ASTERETTE_RATIO, ASTER_RATIO, aster, stack, player, level, pos);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.CONSUME;
    }

    static void tryConvert(int held_quantity, int ratio_from, int ratio_to, Item item, ItemStack stack, Player player, Level level, BlockPos pos) {
        if (held_quantity < ratio_from) {
            playSound(level, pos, SoundEvents.PIG_AMBIENT);
            return;
        }
        ItemStack reward = new ItemStack(item, ratio_to);
        stack.consume(ratio_from, player);
        playSound(level, pos, SoundEvents.AMETHYST_BLOCK_CHIME);
        playSound(level, pos, SoundEvents.GENERIC_EAT);
        playSound(level, pos, SoundEvents.CHICKEN_EGG);
        DefaultDispenseItemBehavior.spawnItem(level, reward, 2, Direction.UP, Vec3.atBottomCenterOf(pos).relative(Direction.UP, 0.7));
    }

    static void playSound(Level level, BlockPos pos, SoundEvent sound){
        level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(
                FACING,
                WATERLOGGED
        );
    }

}
