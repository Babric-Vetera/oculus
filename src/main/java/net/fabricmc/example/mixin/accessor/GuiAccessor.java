package net.fabricmc.example.mixin.accessor;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Gui.class)
public interface GuiAccessor {

    @Invoker("drawString")
    void callDrawString(FontRenderer fr, String text, int x, int y, int color);
}
