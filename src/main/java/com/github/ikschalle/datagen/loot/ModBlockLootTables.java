package com.github.ikschalle.datagen.loot;

import com.github.ikschalle.Asters_n_ammies;
import com.github.ikschalle.block.CoinPile;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.IntStream;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.createCoinPileDrops(Asters_n_ammies.ASTER_BLOCK.get());
        this.createCoinPileDrops(Asters_n_ammies.ASTERETTE_BLOCK.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks(){
         return Asters_n_ammies.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    private void createCoinPileDrops(Block block) {
        LootTable.Builder table = LootTable.lootTable();
        LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1));

        for (int i = 1; i <= 16; i++) {
            pool.add(LootItem.lootTableItem(block)
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                    .hasProperty(CoinPile.COINS, i)))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(i)))
            );
        }

        add(block, table.withPool(pool));
    }
}

