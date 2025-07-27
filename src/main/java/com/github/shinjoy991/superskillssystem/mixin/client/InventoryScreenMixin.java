//package com.github.shinjoy991.superskillssystem.mixin.client;
//
//import com.github.shinjoy991.superskillssystem.gui.PlayerInfoMenu;
//import com.github.shinjoy991.superskillssystem.gui.screen.PlayerInfoScreen;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.components.ImageButton;
//import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
//import net.minecraft.client.gui.screens.inventory.InventoryScreen;
//import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.inventory.InventoryMenu;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
//import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
//@Mixin(InventoryScreen.class)
//public abstract class InventoryScreenMixin extends AbstractContainerScreen<InventoryMenu> {
//
//    @Shadow public abstract RecipeBookComponent getRecipeBookComponent();
//
//    @Shadow @Final private RecipeBookComponent recipeBookComponent;
//
//    public InventoryScreenMixin() {
//        super(null, null, null);
//    }
//
//    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/InventoryScreen;setInitialFocus(Lnet/minecraft/client/gui/components/events/GuiEventListener;)V"))
//    private void addCustomButton(CallbackInfo ci) {
//        RecipeBookComponent recipeBook = this.recipeBookComponent;
//
//        int x = recipeBook.getRectangle().left();
//        int y = recipeBook.getRectangle().bottom();
//
//        ResourceLocation TEXTURE = new ResourceLocation("sss", "textures/gui/button/prime_exp_button.png");
//        this.addRenderableWidget(new ImageButton(
//                x, y, 16, 16,
//                0, 0, 16,
//                TEXTURE, 16, 32,
//                btn -> {
////                    Minecraft.getInstance().setScreen(new PlayerInfoScreen(
////                            new PlayerInfoMenu(0, Minecraft.getInstance().player.getInventory()),
////                            Minecraft.getInstance().player.getInventory(),
////                            Component.literal("Prime EXP")
////                    ));
//                }
//        ));
//    }
//}