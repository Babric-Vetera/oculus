package com.github.qe7.oculus.mixins;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface AccessorMinecraft {

    @Accessor("theMinecraft")
    static Minecraft getMinecraft() {
        throw new AssertionError();
    }
}
