package com.github.qe7.oculus.mixins;

import com.github.qe7.oculus.interfaces.IGameSettings;
import net.minecraft.src.GameSettings;
import net.minecraft.src.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameSettings.class)
public abstract class MixinGameSettings implements IGameSettings {

    @Shadow
    public KeyBinding[] keyBindings;

    @Unique
    public KeyBinding oculusKeyBinding = new KeyBinding("Camera Zoom", Keyboard.KEY_C);

    /**
     * Inject into the GameSettings constructor to add our custom key binding to the keyBindings array.
     */
    @Inject(method = "<init>*", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        KeyBinding[] newKeyBindings = new KeyBinding[keyBindings.length + 1];
        System.arraycopy(keyBindings, 0, newKeyBindings, 0, keyBindings.length);
        newKeyBindings[keyBindings.length] = oculusKeyBinding;
        keyBindings = newKeyBindings;
    }

    @Unique
    @Override
    public KeyBinding oculus$getOculusKeyBinding() {
        return oculusKeyBinding;
    }
}
