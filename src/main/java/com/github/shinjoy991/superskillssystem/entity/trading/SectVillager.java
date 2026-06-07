package com.github.shinjoy991.superskillssystem.entity.trading;

import com.github.shinjoy991.superskillssystem.gui.menu.SectVillagerMenu;
import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.helpers.PlayerInfo;
import com.github.shinjoy991.superskillssystem.helpers.skill.ActiveSkill;
import com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkillInstance;
import com.github.shinjoy991.superskillssystem.helpers.skill.SectTypes;
import com.github.shinjoy991.superskillssystem.helpers.skill.SkillRegistry;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;
import java.util.Map;

public class SectVillager extends Villager {

    private SectTypes sectType = SectTypes.NONE;

    public SectTypes getSectType() {
        return sectType;
    }
    public void setSectType(SectTypes sectType) {
        this.sectType = sectType;
    }

    public SectVillager(EntityType<? extends Villager> type, Level level) {
        super(type, level);
        this.setVillagerData(
                this.getVillagerData()
                        .setProfession(ModVillagers.SECT_MASTER.get())
                        .setLevel(5)
        );
    }

    // Gọi sau khi set sectType để cập nhật profession tương ứng
    public void updateProfessionBySect() {
        VillagerProfession prof;
        switch (this.sectType) {
            case ARCHER -> prof = ModVillagers.SECT_ARCHER.get();
            case WARRIOR -> prof = ModVillagers.SECT_WARRIOR.get();
            default -> prof = ModVillagers.SECT_MASTER.get();
        }
        this.setVillagerData(this.getVillagerData().setProfession(prof).setLevel(5));
    }

    // NBT
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putString("SectType", sectType.name());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("SectType")) {
            try {
                sectType = SectTypes.valueOf(tag.getString("SectType"));
            } catch (Exception ignored) {
                sectType = SectTypes.NONE;
            }
            // Cập nhật profession sau khi load
            if (!this.level().isClientSide()) {
                updateProfessionBySect();
            }
        }
    }

    @Override
    protected void updateTrades() {
        VillagerData villagerdata = this.getVillagerData();
        Int2ObjectMap<VillagerTrades.ItemListing[]> int2objectmap = VillagerTrades.TRADES.get(villagerdata.getProfession());
        if (int2objectmap != null && !int2objectmap.isEmpty()) {
            VillagerTrades.ItemListing[] avillagertrades$itemlisting = int2objectmap.get(villagerdata.getLevel());
            if (avillagertrades$itemlisting != null) {
                MerchantOffers merchantoffers = this.getOffers();
                this.addOffersFromItemListings(merchantoffers, avillagertrades$itemlisting, 2);
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.getItem() != Items.VILLAGER_SPAWN_EGG && super.isAlive() && !super.isTrading() && !super.isSleeping() && !player.isSecondaryUseActive()) {
            if (super.isBaby()) {
                this.setUnhappy();
                return InteractionResult.sidedSuccess(super.level().isClientSide);
            }

            // Chưa có sect → không mở menu
            if (this.sectType == SectTypes.NONE) {
                if (!super.level().isClientSide) {
                    this.setUnhappy();
                }
                return InteractionResult.sidedSuccess(super.level().isClientSide);
            }

            if (hand == InteractionHand.MAIN_HAND) {
                player.awardStat(Stats.TALKED_TO_VILLAGER);
            }
            if (!super.level().isClientSide) {
                this.startTrading(player);
            }
            return InteractionResult.sidedSuccess(super.level().isClientSide);
        }
        else {
            return super.mobInteract(player, hand);
        }
    }

    private void setUnhappy() {
        this.setUnhappyCounter(40);
        if (!this.level().isClientSide()) {
            this.playSound(SoundEvents.VILLAGER_NO, this.getSoundVolume(), this.getVoicePitch());
        }

    }

    private void startTrading(Player p_35537_) {
        if (p_35537_ instanceof ServerPlayer serverPlayer) {
            this.setTradingPlayer(p_35537_);
            this.openTradingScreen1(serverPlayer, this.getDisplayName());
        }
    }

    private void openTradingScreen1(ServerPlayer player, Component title) {
        // Collect active skill IDs for this sect from registry
        List<ResourceLocation> sectActiveSkillIds = SkillRegistry.SKILLS.entrySet().stream()
                .filter(e -> {
                    try {
                        ActiveSkill instance = e.getValue()
                                .getConstructor(net.minecraft.world.entity.LivingEntity.class, int.class)
                                .newInstance(player, 1);
                        return instance.getSectType() == this.sectType;
                    } catch (Exception ex) {
                        return false;
                    }
                })
                .map(Map.Entry::getKey)
                .collect(java.util.stream.Collectors.toList());

        // Player's active skill levels
        Map<String, Integer> playerActiveSkillLevels = AllPlayersInfo.get(player.getUUID()).getActiveSkillsMap();

        NetworkHooks.openScreen(player, new SimpleMenuProvider((id, inv, pl)
                        -> new SectVillagerMenu(id, inv, this, this.sectType,
                        AllPlayersInfo.get(player.getUUID()).getPassiveSkillsInstances(),
                        sectActiveSkillIds,
                        playerActiveSkillLevels), title
                ),
                buf -> {
                    PlayerInfo playerInfo = AllPlayersInfo.get(player.getUUID());
                    List<PassiveSkillInstance> playerPassive = playerInfo.getPassiveSkillsInstances();

                    // 1. Ghi sectType
                    buf.writeEnum(this.sectType);

                    // 2. Ghi passive skills của player
                    CompoundTag tag = new CompoundTag();
                    ListTag skillList = new ListTag();
                    for (PassiveSkillInstance instance : playerPassive) {
                        CompoundTag skillTag = new CompoundTag();
                        skillTag.putString("SkillName", instance.getName());
                        skillTag.putInt("Level", instance.getLevel());
                        skillList.add(skillTag);
                    }
                    tag.put("PassiveSkills", skillList);
                    buf.writeNbt(tag);

                    // 3. Ghi danh sách active skill Id của sect này
                    CompoundTag activeIdTag = new CompoundTag();
                    ListTag activeIdList = new ListTag();
                    for (ResourceLocation id2 : sectActiveSkillIds) {
                        activeIdList.add(net.minecraft.nbt.StringTag.valueOf(id2.toString()));
                    }
                    activeIdTag.put("ActiveSkillIds", activeIdList);
                    buf.writeNbt(activeIdTag);

                    // 4. Ghi level active skill của player
                    CompoundTag playerActiveTag = new CompoundTag();
                    ListTag playerActiveList = new ListTag();
                    for (Map.Entry<String, Integer> entry : playerActiveSkillLevels.entrySet()) {
                        CompoundTag skillTag = new CompoundTag();
                        skillTag.putString("SkillId", entry.getKey());
                        skillTag.putInt("Level", entry.getValue());
                        playerActiveList.add(skillTag);
                    }
                    playerActiveTag.put("PlayerActiveSkills", playerActiveList);
                    buf.writeNbt(playerActiveTag);
                }
        );
    }


    // ─── Work Block: Warrior = Grindstone, Archer = Fletching Table ───────────
    private static final int WORK_SCAN_RADIUS = 3;
    private static final int WORK_SCAN_INTERVAL = 40; // ticks

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide() && this.tickCount % WORK_SCAN_INTERVAL == 0) {
            scanNearbyWorkBlock();
        }
    }

    private void scanNearbyWorkBlock() {
        BlockPos pos = this.blockPosition();
        Level level = this.level();

        for (int dx = -WORK_SCAN_RADIUS; dx <= WORK_SCAN_RADIUS; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -WORK_SCAN_RADIUS; dz <= WORK_SCAN_RADIUS; dz++) {
                    BlockPos check = pos.offset(dx, dy, dz);
                    Block block = level.getBlockState(check).getBlock();

                    SectTypes newSect = null;
                    if (block == Blocks.GRINDSTONE) {
                        newSect = SectTypes.WARRIOR;
                    } else if (block == Blocks.FLETCHING_TABLE) {
                        newSect = SectTypes.ARCHER;
                    }

                    if (newSect != null && newSect != this.sectType) {
                        this.sectType = newSect;
                        updateProfessionBySect();
                        return;
                    }
                }
            }
        }
    }
}