package net.fabricmc.example.mixin;

import net.fabricmc.example.mixin.accessor.GuiAccessor;
import net.minecraft.src.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public final class MixinGuiMainMenu extends MixinGuiScreen {

    @Inject(method = "drawScreen", at = @At("TAIL"))
    public void drawScreen(int j, int f, float par3, CallbackInfo ci) {
        final String example = "Example Mixin(s) have been hooked!";
        ((GuiAccessor) (Object) this).callDrawString(this.fontRenderer, example, this.width - this.fontRenderer.getStringWidth(example) - 2, 2, 16777215);
    }
}
