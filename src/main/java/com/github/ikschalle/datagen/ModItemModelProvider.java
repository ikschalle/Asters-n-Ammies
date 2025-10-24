package com.github.ikschalle.datagen;

import com.github.ikschalle.Asters_n_ammies;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Asters_n_ammies.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(Asters_n_ammies.ASTER_ITEM);
        simpleItem(Asters_n_ammies.ASTERETTE_ITEM);
    }

    @SuppressWarnings("all") private ItemModelBuilder simpleItem(RegistryObject<Item> item){
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Asters_n_ammies.MODID, "item/" + item.getId().getPath()));
    }
}
