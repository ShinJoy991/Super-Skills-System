//package com.github.shinjoy991.sss.register;
//
//import com.github.shinjoy991.balanced_enchantments.BalancedEnchantments;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.item.*;
//import net.minecraft.world.item.enchantment.Enchantment;
//import net.minecraft.world.item.enchantment.EnchantmentCategory;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import net.minecraftforge.registries.RegistryObject;
//
//public class RegisterEnch {
//    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
//            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS,
//                    BalancedEnchantments.MOD_ID);
//    public static final EnchantmentCategory IS_HOE = EnchantmentCategory.create("is_hoe",
//            (item) -> item instanceof HoeItem);
//    public static final EnchantmentCategory IS_BOOTS = EnchantmentCategory.create("is_boots",
//            (item) -> item instanceof ArmorItem && ((ArmorItem) item).getEquipmentSlot() == EquipmentSlot.FEET);
//    public static final EnchantmentCategory IS_BOW = EnchantmentCategory.create("is_bow",
//            (item) -> item instanceof BowItem);
//    public static final EnchantmentCategory IS_BOW_CROSSBOW = EnchantmentCategory.create(
//            "is_bow_crossbow",
//            (item) -> item instanceof BowItem
//                    || item instanceof CrossbowItem);
//    public static final EnchantmentCategory IS_SWORD_TRIDENT_AXE = EnchantmentCategory.create(
//            "is_sword_trident_axe",
//            (item) -> item instanceof AxeItem
//                    || item instanceof SwordItem
//                    || item instanceof TridentItem);
//    public static final EnchantmentCategory IS_TOOL = EnchantmentCategory.create(
//            "is_tool",
//            (item) -> item instanceof AxeItem
//                    || item instanceof HoeItem
//                    || item instanceof PickaxeItem
//                    || item instanceof ShovelItem);
//    public static final EnchantmentCategory IS_PICKAXE = EnchantmentCategory.create(
//            "is_pickaxe",
//            (item) -> item instanceof PickaxeItem);
//    public static final EnchantmentCategory IS_TOOL_AND_WEAPON = EnchantmentCategory.create(
//            "is_tool_and_weapon",
//            (item) -> item instanceof AxeItem
//                    || item instanceof HoeItem
//                    || item instanceof PickaxeItem
//                    || item instanceof ShovelItem
//                    || item instanceof SwordItem
//                    || item instanceof TridentItem);
//    public static final EnchantmentCategory IS_TOOL_AND_WEAPON_AND_ARMOR = EnchantmentCategory.create(
//            "is_tool_and_weapon_and_armor",
//            (item) -> item instanceof AxeItem
//                    || item instanceof HoeItem
//                    || item instanceof PickaxeItem
//                    || item instanceof ShovelItem
//                    || item instanceof SwordItem
//                    || item instanceof TridentItem
//                    || item instanceof ArmorItem
//    );
//    public static RegistryObject<Enchantment> HUNTING_INSTINCT = null;
//    public static RegistryObject<Enchantment> TRUE_SHARPNESS = null;
//    public static RegistryObject<Enchantment> MIGHTY_FORCE = null;
//    public static RegistryObject<Enchantment> STATUS_PROTECTION = null;
//    public static RegistryObject<Enchantment> BLOCK_MASK_SOUL = null;
//    public static RegistryObject<Enchantment> VOLLEY = null;
//    public static RegistryObject<Enchantment> MAGMA_WALKER = null;
//    public static RegistryObject<Enchantment> SUPER_CHARGED = null;
//    public static RegistryObject<Enchantment> HERO_LANDING = null;
//    public static RegistryObject<Enchantment> C_MOON = null;
//    public static RegistryObject<Enchantment> WEAPON_ART = null;
//    public static RegistryObject<Enchantment> ICE_ASPECT = null;
//    public static RegistryObject<Enchantment> INVISIBLE_SHIELD = null;
//    public static RegistryObject<Enchantment> MINING_FOCUS = null;
//    public static RegistryObject<Enchantment> AUTO_MACHINE = null;
//    public static RegistryObject<Enchantment> LINKING = null;
//    public static RegistryObject<Enchantment> CURSE_OF_PROVOCATION = null;
//    public static RegistryObject<Enchantment> CURSE_OF_DURABILITY = null;
//    public static RegistryObject<Enchantment> CURSE_OF_UNSTABLE = null;
//}
