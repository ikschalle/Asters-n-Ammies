package com.github.ikschalle;

import com.github.ikschalle.block.CoinPile;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Asters_n_ammies.MODID)
public class Asters_n_ammies {
    public static final String MODID = "asters_n_ammies";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static final RegistryObject<Block> ASTER_BLOCK = BLOCKS.register("aster", () -> new CoinPile(BlockBehaviour.Properties.of()
            .mapColor(MapColor.DIAMOND)
            .strength(1.0F, 8.0F)
            .requiresCorrectToolForDrops()
            .pushReaction(PushReaction.DESTROY)
            .sound(SoundType.CHAIN)
    ));
    public static final RegistryObject<Block> ASTERETTE_BLOCK = BLOCKS.register("asterette", () -> new CoinPile(BlockBehaviour.Properties.copy(ASTER_BLOCK.get())
            .mapColor(MapColor.COLOR_PURPLE)
    ));

    public static final RegistryObject<Item> ASTER_ITEM = ITEMS.register("aster", () -> new BlockItem(ASTER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> ASTERETTE_ITEM = ITEMS.register("asterette", () -> new BlockItem(ASTERETTE_BLOCK.get(), new Item.Properties()));


    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final RegistryObject<CreativeModeTab> ASTERS_N_AMMIES = CREATIVE_MODE_TABS.register("asters_n_ammies", () ->
            CreativeModeTab.builder().withTabsBefore(CreativeModeTabs.COMBAT)
                    .title(Component.translatable("item_group." + MODID + ".asters_n_ammies"))
                    .icon(() -> ASTER_ITEM.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                    output.accept(ASTER_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
                    output.accept(ASTERETTE_ITEM.get());
                    }).build());


    public Asters_n_ammies() {
        @SuppressWarnings("all") IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        // MinecraftForge.EVENT_BUS.register(this);
        // Register the item to a creative tab
        // modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        // ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        //LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        //if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
        //LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
        //Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    //private void addCreative(BuildCreativeModeTabContentsEvent event) {
        //if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) event.accept(EXAMPLE_BLOCK_ITEM);
    //}

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("Edward Ordaineth Thy Worldly Currency. Edward Taketh Thy Worldly Sins.");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}


