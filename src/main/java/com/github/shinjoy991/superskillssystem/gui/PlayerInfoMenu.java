package com.github.shinjoy991.superskillssystem.gui;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import com.github.shinjoy991.superskillssystem.register.RegisterItem;
import com.github.shinjoy991.superskillssystem.register.RegisterMenu;

public class PlayerInfoMenu extends AbstractContainerMenu {
    public static final int MAX_NAME_LENGTH = 35;
    public static final int INPUT_SLOT = 0;
    public static final int ADDITIONAL_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    private static final int INV_SLOT_START = 3;
    private static final int INV_SLOT_END = 30;
    private static final int USE_ROW_SLOT_START = 30;
    private static final int USE_ROW_SLOT_END = 39;
    private final Container resultSlots = new ResultContainer();
    final Container repairSlots = new SimpleContainer(2) {
        public void setChanged() {
            super.setChanged();
            PlayerInfoMenu.this.slotsChanged(this);
        }
    };
    private final ContainerLevelAccess access;

    public PlayerInfoMenu(int windowId, Inventory p_39564_) {
        this(windowId, p_39564_, ContainerLevelAccess.NULL);
    }

    public PlayerInfoMenu(int windowId, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(windowId, playerInventory);
    }

    public PlayerInfoMenu(int p_39566_, Inventory p_39567_, final ContainerLevelAccess p_39568_) {
        super(RegisterMenu.PRIME_EXP_GRINDER_MENU.get(), p_39566_);
        this.access = p_39568_;
    }

    public void slotsChanged(Container p_39570_) {
        super.slotsChanged(p_39570_);
        if (p_39570_ == this.repairSlots) {
            this.createResult();
        }

    }

    private void createResult() {
        ItemStack input1 = this.repairSlots.getItem(0);
        ItemStack input2 = this.repairSlots.getItem(1);

        if (input1.isEmpty() && input2.isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            return;
        }

        int totalCount = input1.getCount() + input2.getCount();
        ItemStack result = new ItemStack(RegisterItem.PRIME_EXP_ORB.get(), totalCount);
        this.resultSlots.setItem(0, result);

        this.broadcastChanges();
    }

    public void removed(Player p_39586_) {
        super.removed(p_39586_);
        this.access.execute((p_39575_, p_39576_) -> {
            this.clearContainer(p_39586_, this.repairSlots);
        });
    }

    public boolean stillValid(Player p_39572_) {
       // return stillValid(this.access, p_39572_, RegisterBlock.PRIME_EXP_GRINDER.get());
        return true;
    }

    public ItemStack quickMoveStack(Player p_39588_, int p_39589_) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(p_39589_);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            ItemStack itemstack2 = this.repairSlots.getItem(0);
            ItemStack itemstack3 = this.repairSlots.getItem(1);

            if (p_39589_ == 2) {
                slot.set(ItemStack.EMPTY);
                slot.onTake(p_39588_, itemstack1);
                return ItemStack.EMPTY;
            }
            else if (p_39589_ != 0 && p_39589_ != 1) {
                if (!itemstack2.isEmpty() && !itemstack3.isEmpty()) {
                    if (p_39589_ >= 3 && p_39589_ < 30) {
                        if (!this.moveItemStackTo(itemstack1, 30, 39, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (p_39589_ >= 30 && p_39589_ < 39 && !this.moveItemStackTo(itemstack1, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
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

            slot.onTake(p_39588_, itemstack1);
        }

        return itemstack;
    }
}