package com.github.ikschalle.datagen.loot;

import com.github.ikschalle.Asters_n_ammies;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(Asters_n_ammies.ASTER_BLOCK.get());
        this.dropSelf(Asters_n_ammies.ASTERETTE_BLOCK.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks(){
         return Asters_n_ammies.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
