package com.github.shinjoy991.superskillssystem.register;

import com.github.shinjoy991.superskillssystem.SSS;
import com.github.shinjoy991.superskillssystem.block.CrustedMagma;
import com.github.shinjoy991.superskillssystem.block.blockentity.PrimeEXPGrinder;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterBlock {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, SSS.MODID);
    public static final RegistryObject<Block> CRUSTEDMAGMA =
            BLOCKS.register("crusted_magma_block", CrustedMagma::new);

    public static final RegistryObject<Block> PRIME_EXP_GRINDER =
            BLOCKS.register("prime_exp_grinder", PrimeEXPGrinder::new);
}

