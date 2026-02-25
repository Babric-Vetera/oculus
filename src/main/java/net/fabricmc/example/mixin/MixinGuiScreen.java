package net.fabricmc.example.mixin;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {

    @Shadow
    protected FontRenderer fontRenderer;

    @Shadow
    public int width;
}
