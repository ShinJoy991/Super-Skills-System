//package com.github.shinjoy991.sss.mixin;
//
//import com.github.shinjoy991.balanced_enchantments.register.RegisterBlock;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//
//@Mixin(WalkNodeEvaluator.class)
//public abstract class WalkNodeProcessorMixin {
//
//    @Inject(
//            method = "isBurningBlock",
//            at = @At("RETURN"),
//            cancellable = true)
//    private static void modifyIsBurningBlock(BlockState p_237233_0_,
//            CallbackInfoReturnable<Boolean> cir) {
//        cir.setReturnValue(cir.getReturnValue() || p_237233_0_.is(RegisterBlock.CRUSTEDMAGMA.get()));
//    }
//}