package com.github.shinjoy991.superskillssystem.gui;

import com.github.shinjoy991.superskillssystem.gui.menu.SectVillagerMenu;
import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;

public class SectMerchantResultSlot extends Slot {
    private final SectMerchantContainer slots;
    private final Player player;
    private int removeCount;
    private final Merchant merchant;

    public SectMerchantResultSlot(Player p_40083_, Merchant p_40084_, SectMerchantContainer p_40085_, int p_40086_, int p_40087_, int p_40088_) {
        super(p_40085_, p_40086_, p_40087_, p_40088_);
        this.player = p_40083_;
        this.merchant = p_40084_;
        this.slots = p_40085_;
    }

//    public void onTake(Player p_150631_, ItemStack p_150632_) {
//        this.checkTakeAchievements(p_150632_);
//        MerchantOffer merchantoffer = this.slots.getActiveOffer();
//        if (merchantoffer != null) {
//            ItemStack itemstack = this.slots.getItem(0);
//            ItemStack itemstack1 = this.slots.getItem(1);
//            if (merchantoffer.take(itemstack, itemstack1) || merchantoffer.take(itemstack1, itemstack)) {
//                this.merchant.notifyTrade(merchantoffer);
//                p_150631_.awardStat(Stats.TRADED_WITH_VILLAGER);
//                this.slots.setItem(0, itemstack);
//                this.slots.setItem(1, itemstack1);
//            }
//
//            this.merchant.overrideXp(this.merchant.getVillagerXp() + merchantoffer.getXp());
//        }
//
//    }

    public void onTake(Player player, ItemStack stack) {
//        System.out.println("onTake called in SectVillagerMenu,  is client player:" + player.level().isClientSide);

//        this.checkTakeAchievements(stack);
        MerchantOffer merchantoffer = this.slots.getActiveOffer();
        if (merchantoffer != null) {
            ItemStack itemstack = this.slots.getItem(0);
            ItemStack itemstack1 = this.slots.getItem(1);
            if (merchantoffer.take(itemstack, itemstack1) || merchantoffer.take(itemstack1, itemstack)) {
                this.merchant.notifyTrade(merchantoffer);
                player.awardStat(Stats.TRADED_WITH_VILLAGER);
                this.slots.setItem(0, itemstack);
                this.slots.setItem(1, itemstack1);
            }

            this.merchant.overrideXp(this.merchant.getVillagerXp() + merchantoffer.getXp());
        }

        AbstractContainerMenu abstractcontainermenu = player.containerMenu;
        if (abstractcontainermenu instanceof SectVillagerMenu sectMenu) {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains("SkillName", Tag.TAG_STRING)) {
                String skillName = tag.getString("SkillName");
                sectMenu.rebuildOffersByPassiveSkillNameAndLevelChange(skillName, 1);

                if (player instanceof ServerPlayer serverPlayer) {
                    String skillId = tag.getString("SkillId");
                   // AllPlayersInfo.get(serverPlayer.getUUID()).addPassiveSkill(skillName, 1);
                    if (skillId.isEmpty()) {
                        AllPlayersInfo.get(serverPlayer.getUUID()).addPassiveSkill(skillName, 1);
                    } else {
                        // active skill
                        AllPlayersInfo.get(serverPlayer.getUUID()).addActiveSkill(skillId, 1);
                    }
                    serverPlayer.connection.send(new ClientboundSetCarriedItemPacket(serverPlayer.getInventory().selected)); // sync slot
                    serverPlayer.containerMenu.setCarried(ItemStack.EMPTY); // xóa khỏi chuột

                }
            }


//        if (player instanceof ServerPlayer serverPlayer) {
//            CompoundTag tag = stack.getTag();
////            System.out.println("tag is: " + tag);
//            if (tag != null && tag.contains("SkillName", Tag.TAG_STRING)) {
////                System.out.println("tag not null");
//                String skillName = tag.getString("SkillName");
//                int skillLevel = tag.contains("SkillLevel", Tag.TAG_INT) ? tag.getInt("SkillLevel") : 0;
//                if (skillLevel != 0) {
//                    AllPlayersInfo.get(serverPlayer.getUUID()).addPassiveSkill(skillName, 1);
//                }
            }
//            serverPlayer.connection.send(new ClientboundSetCarriedItemPacket(serverPlayer.getInventory().selected)); // sync slot
//            serverPlayer.containerMenu.setCarried(ItemStack.EMPTY); // xóa khỏi chuột

            // Update the SectVillagerMenu with the new skill information
//            AbstractContainerMenu abstractcontainermenu = player.containerMenu;
//                if (abstractcontainermenu instanceof SectVillagerMenu sectMenu) {
//                    sectMenu.rebuildOffers(AllPlayersInfo.get(serverPlayer.getUUID()).getPassiveSkillsInstances());
//                }

//        } else {
//            AbstractContainerMenu abstractcontainermenu = player.containerMenu;
//            if (abstractcontainermenu instanceof SectVillagerMenu sectMenu) {
//                CompoundTag tag = stack.getTag();
//                if (tag != null && tag.contains("SkillName", Tag.TAG_STRING)) {
//                    String skillName = tag.getString("SkillName");
//                    sectMenu.rebuildOffersClientByPassiveSkillNameAndLevelChange(skillName, 1);
//            }
//        }
    }


    // Not changed methods

    public boolean mayPlace(ItemStack p_40095_) {
        return false;
    }

    public ItemStack remove(int p_40090_) {
        if (this.hasItem()) {
            this.removeCount += Math.min(p_40090_, this.getItem().getCount());
        }

        return super.remove(p_40090_);
    }

    protected void onQuickCraft(ItemStack p_40097_, int p_40098_) {
        this.removeCount += p_40098_;
        this.checkTakeAchievements(p_40097_);
    }

    protected void checkTakeAchievements(ItemStack p_40100_) {
        p_40100_.onCraftedBy(this.player.level(), this.player, this.removeCount);
        this.removeCount = 0;
    }

    // Not changed methods end

}
