package com.github.ikschalle;

import com.github.ikschalle.block.CoinPileBlock;
import com.github.ikschalle.block.HungeringHogBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.PushReaction;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Asters_n_Ammies.MODID)
public class Asters_n_Ammies {
    public static final String MODID = "asters_n_ammies";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredBlock<Block> HUNGERING_HOG_BLOCK = BLOCKS.register("hungering_hog",
            () -> new HungeringHogBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_PINK)
            .strength(0.0F, 0.0F)
            .pushReaction(PushReaction.DESTROY)
            .sound(SoundType.DECORATED_POT)
    ));
    public static final DeferredBlock<Block> ASTER_BLOCK = BLOCKS.register("aster",
            () -> new CoinPileBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.DIAMOND)
            .strength(1.0F, 8.0F)
            .requiresCorrectToolForDrops()
            .pushReaction(PushReaction.DESTROY)
            .sound(SoundType.CHAIN)
    ));
    public static final DeferredBlock<Block> ASTERETTE_BLOCK = BLOCKS.register("asterette",
            () -> new CoinPileBlock(BlockBehaviour.Properties.ofFullCopy(ASTER_BLOCK.get())
            .mapColor(MapColor.COLOR_PURPLE)
    ));

    public static final DeferredItem<Item> HUNGERING_HOG_ITEM = ITEMS.register("hungering_hog", () -> new BlockItem(HUNGERING_HOG_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<Item> ASTER_ITEM = ITEMS.register("aster", () -> new BlockItem(ASTER_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<Item> ASTERETTE_ITEM = ITEMS.register("asterette", () -> new BlockItem(ASTERETTE_BLOCK.get(), new Item.Properties()));


    // Creates a creative tab with the id "asters_n_ammies:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group." + MODID + ".asters_n_ammies")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ASTER_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ASTER_ITEM.get());
                output.accept(ASTERETTE_ITEM.get());
                output.accept(HUNGERING_HOG_ITEM.get());
            }).build());


    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Asters_n_Ammies(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (AstersnAmmies) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
//        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }


    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

//        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
//            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
//        }
//
//        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());
//
//        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("Edward Ordaineth Thy Worldly Currency. Edward Taketh Thy Worldly Sins.");
    }
}
