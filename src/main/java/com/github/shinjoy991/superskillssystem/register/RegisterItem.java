package com.github.shinjoy991.superskillssystem.register;

import com.github.shinjoy991.superskillssystem.SSS;
import com.github.shinjoy991.superskillssystem.item.ExpBookConsumeItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class RegisterItem {
    public static final DeferredRegister<Item> ITEMS;
    public static final RegistryObject<Item> ICON;
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB;
    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB;

    public static final RegistryObject<BlockItem> CRUSTED_MAGMA_BLOCK;
    public static final RegistryObject<BlockItem> PRIME_EXP_GRINDER;

    public static final RegistryObject<Item> PRIME_EXP_BOOK;
    public static final RegistryObject<Item> PRIME_EXP_ORB;

    public static final RegistryObject<Item> SECT_VILLAGER_SPAWN_EGG;

    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SSS.MODID);
        ICON = ITEMS.register("mod_icon", () -> new Item(new Item.Properties()) {
            public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
                super.appendHoverText(stack, world, tooltip, flag);
                tooltip.add(Component.literal("This is just an icon, what are you planning for?"));
            }
        });
        CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SSS.MODID);

        CRUSTED_MAGMA_BLOCK = ITEMS.register("crusted_magma_block_0", () -> new BlockItem((Block)RegisterBlock.CRUSTEDMAGMA.get(), new Item.Properties()));
        PRIME_EXP_GRINDER = ITEMS.register("prime_exp_grinder", () -> new BlockItem((Block)RegisterBlock.PRIME_EXP_GRINDER.get(), new Item.Properties()));

        PRIME_EXP_BOOK = ITEMS.register("prime_exp_book", () -> new ExpBookConsumeItem(new Item.Properties())
        {
            public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
                super.appendHoverText(stack, world, tooltip, flag);
                tooltip.add(Component.literal("Gain 30 prime exp"));
            }

            @Override
            public Component getName(ItemStack stack) {
                return Component.translatable(this.getDescriptionId(stack)).withStyle(ChatFormatting.LIGHT_PURPLE);
            }
        });

        PRIME_EXP_ORB = ITEMS.register("prime_exp_orb", () -> new Item(new Item.Properties()) {
            @Override
            public Component getName(ItemStack stack) {
                return Component.translatable(this.getDescriptionId(stack)).withStyle(ChatFormatting.LIGHT_PURPLE);
            }
        });

        // Spawn Eggs
        SECT_VILLAGER_SPAWN_EGG = ITEMS.register("sect_villager_spawn_egg",
                () -> new ForgeSpawnEggItem(
                        RegisterEntity.SECT_VILLAGER,
                        0x8B6F47, // màu chính
                        0xC2B280, // màu đốm
                        new Item.Properties()));


        // Creative Tab
        CREATIVE_TAB = CREATIVE_MODE_TAB.register("creative_tab", () -> CreativeModeTab.builder().icon(() -> new ItemStack((ItemLike)ICON.get())).displayItems((parameters, output) -> {
            output.accept(new ItemStack(CRUSTED_MAGMA_BLOCK.get()));
            output.accept(new ItemStack(PRIME_EXP_GRINDER.get()));
            output.accept(new ItemStack(PRIME_EXP_BOOK.get()));
            output.accept(new ItemStack(PRIME_EXP_ORB.get()));
            output.accept(SECT_VILLAGER_SPAWN_EGG.get());
        }).title(Component.translatable("itemGroup.sss.creative_tab")).build());
    }
}