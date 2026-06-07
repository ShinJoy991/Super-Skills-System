package com.github.shinjoy991.superskillssystem.gui.menu;

import com.github.shinjoy991.superskillssystem.Config;
import com.github.shinjoy991.superskillssystem.config.ReadConfig;
import com.github.shinjoy991.superskillssystem.gui.SectMerchantContainer;
import com.github.shinjoy991.superskillssystem.gui.SectMerchantResultSlot;
import com.github.shinjoy991.superskillssystem.helpers.PlayerClientData;
import com.github.shinjoy991.superskillssystem.helpers.skill.*;
import com.github.shinjoy991.superskillssystem.register.RegisterMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.ClientSideMerchant;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SectVillagerMenu extends AbstractContainerMenu {
//    protected static final int PAYMENT1_SLOT = 0;
//    protected static final int PAYMENT2_SLOT = 1;
//    protected static final int RESULT_SLOT = 2;
//    private static final int INV_SLOT_START = 3;
//    private static final int INV_SLOT_END = 30;
//    private static final int USE_ROW_SLOT_START = 30;
//    private static final int USE_ROW_SLOT_END = 39;
//    private static final int SELLSLOT1_X = 136;
//    private static final int SELLSLOT2_X = 162;
//    private static final int BUYSLOT_X = 220;
//    private static final int ROW_Y = 37;
    private final Merchant trader;
    private final SectMerchantContainer tradeContainer;
    private final Container resultContainer = new ResultContainer();
    private SectTypes sectType;
    private boolean showProgressBar;
    private boolean canRestock;
    private int selectedCategory = 0;
    private List<PassiveSkillInstance> playerPassiveSkills;
    private MerchantOffers cachedOffers = new MerchantOffers();

    // Active skill list nhận từ server khi mở menu (toàn bộ active skill của sect này)
    private List<ResourceLocation> sectActiveSkillIds = new ArrayList<>();
    // Level active skill của player (skillId string -> level)
    private Map<String, Integer> playerActiveSkillLevels = new HashMap<>();


    // Server constructor
    public SectVillagerMenu(int p45298, Inventory p45299, Merchant sectVillager, SectTypes sectType,
                            List<PassiveSkillInstance> playerPassiveSkills,
                            List<ResourceLocation> sectActiveSkillIds,
                            Map<String, Integer> playerActiveSkillLevels) {
        this(p45298, p45299, sectVillager);
        this.sectType = sectType;
        this.playerPassiveSkills = playerPassiveSkills;
        this.sectActiveSkillIds = sectActiveSkillIds;
        this.playerActiveSkillLevels = playerActiveSkillLevels;
        rebuildOffers(playerPassiveSkills);
    }

    // Client constructor
    public SectVillagerMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(
                id,
                inv,
                new ClientSideMerchant(inv.player),
                buf.readEnum(SectTypes.class),
                readPlayerPassiveSkills(buf),
                readSectActiveSkillIds(buf),
                readPlayerActiveSkillLevels(buf)
        );
    }

    public SectVillagerMenu(int id, Inventory inv, Merchant merchant) {
        super(RegisterMenu.SECT_VILLAGER_MENU.get(), id);
        this.trader = merchant;
        this.tradeContainer = new SectMerchantContainer(merchant, this);
        this.addSlot(new Slot(this.tradeContainer, 0, 136, 25 + 25));
        this.addSlot(new Slot(this.tradeContainer, 1, 162, 25 + 25));
        this.addSlot(new SectMerchantResultSlot(inv.player, merchant, this.tradeContainer, 2, 220, 25 + 25));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inv, j + i * 9 + 9, 108 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inv, k, 108 + k * 18, 142));
        }

    }


    // Hàm static tách riêng để đọc dữ liệu (Only Client)
    private static List<PassiveSkillInstance> readPlayerPassiveSkills(FriendlyByteBuf buf) {
        List<PassiveSkillInstance> skills = new ArrayList<>();

        CompoundTag tag = buf.readNbt();
        if (tag != null && tag.contains("PassiveSkills", Tag.TAG_LIST)) {
            ListTag skillList = tag.getList("PassiveSkills", Tag.TAG_COMPOUND);
            for (Tag t : skillList) {
                CompoundTag skillTag = (CompoundTag) t;
                String name = skillTag.getString("SkillName");
                int level = skillTag.getInt("Level");

                PassiveSkill skill = PlayerClientData.warriorGlobalPassiveSkills.stream()
                        .filter(s -> s.name.equals(name))
                        .findFirst()
                        .orElse(null);

                if (skill != null) {
                    skills.add(new PassiveSkillInstance(skill, level));
                }
            }
        }

        return skills;
    }
    private static List<ResourceLocation> readSectActiveSkillIds(FriendlyByteBuf buf) {
        List<ResourceLocation> ids = new ArrayList<>();
        CompoundTag tag = buf.readNbt();
        if (tag != null && tag.contains("ActiveSkillIds", Tag.TAG_LIST)) {
            ListTag list = tag.getList("ActiveSkillIds", Tag.TAG_STRING);
            for (Tag t : list) {
                ResourceLocation id = ResourceLocation.tryParse(t.getAsString());
                if (id != null) ids.add(id);
            }
        }
        return ids;
    }
    private static Map<String, Integer> readPlayerActiveSkillLevels(FriendlyByteBuf buf) {
        Map<String, Integer> map = new HashMap<>();
        CompoundTag tag = buf.readNbt();
        if (tag != null && tag.contains("PlayerActiveSkills", Tag.TAG_LIST)) {
            ListTag list = tag.getList("PlayerActiveSkills", Tag.TAG_COMPOUND);
            for (Tag t : list) {
                CompoundTag ct = (CompoundTag) t;
                map.put(ct.getString("SkillId"), ct.getInt("Level"));
            }
        }
        return map;
    }

    public void setShowProgressBar(boolean p_40049_) {
        this.showProgressBar = false;
    }

    // Client + Server
    @Override
    public void slotsChanged(Container p_40040_) {
        this.tradeContainer.updateSellItem();
        super.slotsChanged(p_40040_);
    }
    public void tryMoveItems(int p_40073_) {
        if (p_40073_ >= 0 && this.getOffers().size() > p_40073_) {
//            System.out.println("trymove2 "+ this.trader.getTradingPlayer().level().isClientSide);
            ItemStack itemstack = this.tradeContainer.getItem(0);
            if (!itemstack.isEmpty()) {
//                System.out.println("trymove3 "+ this.trader.getTradingPlayer().level().isClientSide);
                if (!this.moveItemStackTo(itemstack, 3, 39, true)) {
                    return;
                }

                this.tradeContainer.setItem(0, itemstack);
            }

            ItemStack itemstack1 = this.tradeContainer.getItem(1);
//            System.out.println("trymove4 "+ this.trader.getTradingPlayer().level().isClientSide);
            if (!itemstack1.isEmpty()) {
//                System.out.println("trymove5 "+ this.trader.getTradingPlayer().level().isClientSide);
                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
                    return;
                }

                this.tradeContainer.setItem(1, itemstack1);
            }
//            System.out.println("trymove6 "+ this.trader.getTradingPlayer().level().isClientSide);
            if (this.tradeContainer.getItem(0).isEmpty() && this.tradeContainer.getItem(1).isEmpty()) {
                ItemStack itemstack2 = this.getOffers().get(p_40073_).getCostA();
                this.moveFromInventoryToPaymentSlot(0, itemstack2);
                ItemStack itemstack3 = this.getOffers().get(p_40073_).getCostB();
                this.moveFromInventoryToPaymentSlot(1, itemstack3);
//                System.out.println("trymove7 "+ this.trader.getTradingPlayer().level().isClientSide);
            }

        }
    }
    private void moveFromInventoryToPaymentSlot(int p_40061_, ItemStack p_40062_) {
        if (!p_40062_.isEmpty()) {
            for (int i = 3; i < 39; ++i) {
                ItemStack itemstack = this.slots.get(i).getItem();
                if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(p_40062_, itemstack)) {
                    ItemStack itemstack1 = this.tradeContainer.getItem(p_40061_);
                    int j = itemstack1.isEmpty() ? 0 : itemstack1.getCount();
                    int k = Math.min(p_40062_.getMaxStackSize() - j, itemstack.getCount());
                    ItemStack itemstack2 = itemstack.copy();
                    int l = j + k;
                    itemstack.shrink(k);
                    itemstack2.setCount(l);
                    this.tradeContainer.setItem(p_40061_, itemstack2);
                    if (l >= p_40062_.getMaxStackSize()) {
                        break;
                    }
                }
            }
        }

    }
    public void rebuildOffers(List<PassiveSkillInstance> playerPassiveSkills) {
        this.playerPassiveSkills = playerPassiveSkills;

        // todo:
        List<PassiveSkill> listPassiveSkills = PlayerClientData.warriorGlobalPassiveSkills;


        // Map skillName -> level
        Map<String, Integer> skillLevelMap = new HashMap<>();
        for (PassiveSkillInstance psi : this.playerPassiveSkills) {
            skillLevelMap.put(psi.getName(), psi.getLevel());
        }
        MerchantOffers offers = new MerchantOffers();
        for (PassiveSkill skill : listPassiveSkills) {
            int playerLevel = skillLevelMap.getOrDefault(skill.name, 0);
            int nextLevel = playerLevel + 1;
            int emeraldCost;
            int diamondCost;
            if (nextLevel > 20) {
                nextLevel = 20;
                emeraldCost = 0;
                diamondCost = 0;
            }
            else {
                if (nextLevel < 6) {
                    emeraldCost = nextLevel;
                    diamondCost = nextLevel;
                }
                else if (nextLevel < 11) {
                    emeraldCost = nextLevel;
                    diamondCost = nextLevel - 2;
                }
                else if (nextLevel < 16) {
                    emeraldCost = nextLevel + 3;
                    diamondCost = nextLevel - 3;
                }
                else if (nextLevel < 20) {
                    emeraldCost = (int) Math.round(nextLevel * 1.2);
                    diamondCost = (int) Math.round(nextLevel * 0.85);
                }
                else {
                    emeraldCost = nextLevel * 2;
                    diamondCost = nextLevel * 2;
                }
                emeraldCost = Math.max(1, (int) (emeraldCost * Config.SECT_PRICE_MULTIPLIER));
                diamondCost = Math.max(1, (int) (diamondCost * Config.SECT_PRICE_MULTIPLIER));
            }


            ItemStack costA = new ItemStack(Items.EMERALD, emeraldCost);
            ItemStack costB = diamondCost > 0 ? new ItemStack(Items.DIAMOND, diamondCost) : ItemStack.EMPTY;

            ItemStack result = new ItemStack(Items.BOOK, nextLevel);
            result.setHoverName(
                    skill.getTranslatableName()
                            .copy()
                            .setStyle(Style.EMPTY.withItalic(false))
                            .append(Component.literal(" - Lv." + (nextLevel)).withStyle(ChatFormatting.GRAY))
            );

            CompoundTag nbt = result.getOrCreateTag();
            nbt.putString("SkillName", skill.name);
            // Build lore từ PassiveSkillInstance.getInfo()
            try {
                PassiveSkillInstance tempInstance = new PassiveSkillInstance(skill, nextLevel);
                List<Component> loreComponents = tempInstance.getLoreInfo(tempInstance.getSectType().equals(PlayerClientData.sect));

                ListTag loreList = new ListTag();

                for (Component line : loreComponents) {
                    loreList.add(
                            StringTag.valueOf(Component.Serializer.toJson(line))
                    );
                }
                CompoundTag displayTag = result.getOrCreateTagElement("display");
                displayTag.put("Lore", loreList);
            } catch (Exception e) {
                // ignore
            }

            offers.add(new MerchantOffer(costA, costB, result, 9999, 0, 0));
        }

        // Add active skills
        for (ResourceLocation skillId : this.sectActiveSkillIds) {
            // Tạo dummy instance trước để lấy name
            Class<? extends ActiveSkill> clazz = SkillRegistry.get(skillId);
            if (clazz == null) continue;

            ActiveSkill dummy;
            try {
                dummy = clazz.getConstructor(net.minecraft.world.entity.LivingEntity.class, int.class)
                        .newInstance(null, 1);
            } catch (Exception e) {
                continue;
            }

            String skillName = dummy.getName(); // lấy đúng name từ instance

            int playerLevel = playerActiveSkillLevels.getOrDefault(skillId.toString(), 0);
            int nextLevel = Math.min(playerLevel + 1, 20);

            int emeraldCost;
            int diamondCost;
            if (playerLevel >= 20) {
                emeraldCost = 0;
                diamondCost = 0;
            } else {
                if (nextLevel < 6) {
                    emeraldCost = nextLevel;
                    diamondCost = nextLevel;
                } else if (nextLevel < 11) {
                    emeraldCost = nextLevel;
                    diamondCost = nextLevel - 2;
                } else if (nextLevel < 16) {
                    emeraldCost = nextLevel + 3;
                    diamondCost = nextLevel - 3;
                } else if (nextLevel < 20) {
                    emeraldCost = (int) Math.round(nextLevel * 1.2);
                    diamondCost = (int) Math.round(nextLevel * 0.85);
                } else {
                    emeraldCost = nextLevel * 2;
                    diamondCost = nextLevel * 2;
                }
                emeraldCost = Math.max(1, (int) (emeraldCost * Config.SECT_PRICE_MULTIPLIER));
                diamondCost = Math.max(1, (int) (diamondCost * Config.SECT_PRICE_MULTIPLIER));
            }

            ItemStack costA = new ItemStack(Items.EMERALD, emeraldCost);
            ItemStack costB = diamondCost > 0 ? new ItemStack(Items.DIAMOND, diamondCost) : ItemStack.EMPTY;

            ItemStack result = new ItemStack(Items.BOOK, nextLevel);
            result.setHoverName(
                    Component.translatable("skill.name." + skillName)
                            .copy()
                            .setStyle(Style.EMPTY.withItalic(false))
                            .withStyle(ChatFormatting.GOLD)
                            .append(Component.literal(" - Lv." + nextLevel).withStyle(ChatFormatting.GRAY))
            );

            CompoundTag nbt = result.getOrCreateTag();
            nbt.putString("SkillName", skillName);
            nbt.putString("SkillId", skillId.toString());

            // Build lore từ getInfo(), bỏ dòng đầu (name) và dòng level
            try {
                List<Component> loreComponents = dummy.getLoreInfo(dummy.getSectType().equals(PlayerClientData.sect));

                ListTag loreList = new ListTag();

                for (Component line : loreComponents) {
                    loreList.add(
                            StringTag.valueOf(
                                    Component.Serializer.toJson(line)
                            )
                    );
                }

                CompoundTag displayTag = result.getOrCreateTagElement("display");
                displayTag.put("Lore", loreList);
            } catch (Exception e) {
                // ignore
            }

            offers.add(new MerchantOffer(costA, costB, result, 9999, 0, 0));
        }

        this.cachedOffers = offers;
    }
    public void setSelectionHint(int p_40064_) {
        this.tradeContainer.setSelectionHint(p_40064_);
    }

    public boolean stillValid(Player p_40042_) {
        return this.trader.getTradingPlayer() == p_40042_;
    }

    public int getTraderXp() {
        return 0;
    }

    public int getFutureTraderXp() {
        return 0;
    }

    public void setXp(int p_40067_) {
        this.trader.overrideXp(0);
    }

    public SectTypes getSectType() {
        return this.sectType;
    }

    public void setSectType(SectTypes sect) {
        this.sectType = sect;
    }

    public void setCanRestock(boolean p_40059_) {
        this.canRestock = false;
    }

    public boolean canRestock() {
        return false;
    }

    public boolean canTakeItemForPickAll(ItemStack p_40044_, Slot p_40045_) {
        return false;
    }

    public ItemStack quickMoveStack(@NotNull Player player, int slotId) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotId);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (slotId == 2) {
//                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
//                    return ItemStack.EMPTY;
//                }
                slot.set(ItemStack.EMPTY);
                slot.onTake(player, itemstack1);
//                return ItemStack.EMPTY;
//
                slot.onQuickCraft(itemstack1, itemstack);
                this.playTradeSound();
            }
            else if (slotId != 0 && slotId != 1) {
                if (slotId >= 3 && slotId < 30) {
                    if (!this.moveItemStackTo(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (slotId >= 30 && slotId < 39 && !this.moveItemStackTo(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            }
            else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    private void playTradeSound() {
        if (!this.trader.isClientSide()) {
            Entity entity = (Entity) this.trader;
            entity.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), this.trader.getNotifyTradeSound(), SoundSource.NEUTRAL, 1.0F, 1.0F, false);
        }

    }

    // Server
    public void removed(Player p_40051_) {
        super.removed(p_40051_);
        this.trader.setTradingPlayer((Player) null);
        if (!this.trader.isClientSide()) {
            if (!p_40051_.isAlive() || p_40051_ instanceof ServerPlayer && ((ServerPlayer) p_40051_).hasDisconnected()) {
                ItemStack itemstack = this.tradeContainer.removeItemNoUpdate(0);
                if (!itemstack.isEmpty()) {
                    p_40051_.drop(itemstack, false);
                }

                itemstack = this.tradeContainer.removeItemNoUpdate(1);
                if (!itemstack.isEmpty()) {
                    p_40051_.drop(itemstack, false);
                }
            }
            else if (p_40051_ instanceof ServerPlayer) {
                p_40051_.getInventory().placeItemBackInInventory(this.tradeContainer.removeItemNoUpdate(0));
                p_40051_.getInventory().placeItemBackInInventory(this.tradeContainer.removeItemNoUpdate(1));
            }

        }
    }



    public void setOffers(MerchantOffers offers) {
        this.cachedOffers = offers;
        this.trader.overrideOffers(offers);
    }
    public void setSelectedCategory(int category) {
        this.selectedCategory = category;
    }
    public int getSelectedCategory() {
        return this.selectedCategory;
    }
    public MerchantOffers getOffers() {
        return this.cachedOffers;
    }

    public boolean showProgressBar() {
        return false;
    }

    public int getMaxCategories() {
        // Trả về số lượng category bạn muốn hiển thị
        return 3; // Ví dụ: 3 category
    }


    public void rebuildOffersByPassiveSkillNameAndLevelChange(String skillName, int level) {
        boolean found = false;

        for (int i = 0; i < this.playerPassiveSkills.size(); i++) {
            PassiveSkillInstance psi = this.playerPassiveSkills.get(i);
            if (psi.getName().equals(skillName)) {
                int currentLevel = psi.getLevel();
                this.playerPassiveSkills.set(i, new PassiveSkillInstance(psi.getSkill(), currentLevel + level));
                found = true;
                break;
            }
        }

        if (!found) {
            PassiveSkill skill;
            if (this.trader.isClientSide()) {
                skill = PlayerClientData.warriorGlobalPassiveSkills.stream()
                        .filter(s -> s.name.equals(skillName))
                        .findFirst()
                        .orElse(null);
            } else {
                skill = ReadConfig.passiveSkills.stream()
                        .filter(s -> s.name.equals(skillName))
                        .findFirst()
                        .orElse(null);
            }

            if (skill != null) {
                this.playerPassiveSkills.add(new PassiveSkillInstance(skill, level));
            }
        }

        rebuildOffers(this.playerPassiveSkills);
    }

    public void rebuildOffersByActiveSkillIdAndLevelChange(String skillId, int levelDelta) {
        // Đảm bảo map là mutable
        if (!(this.playerActiveSkillLevels instanceof HashMap)) {
            this.playerActiveSkillLevels = new HashMap<>(this.playerActiveSkillLevels);
        }
        int current = this.playerActiveSkillLevels.getOrDefault(skillId, 0);
        this.playerActiveSkillLevels.put(skillId, Math.min(current + levelDelta, 20));
        rebuildOffers(this.playerPassiveSkills);
    }


}