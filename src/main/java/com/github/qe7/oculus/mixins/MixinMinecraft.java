package com.github.qe7.oculus.mixins;

import com.github.qe7.oculus.OculusMod;
import com.github.qe7.oculus.interfaces.IGameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GameSettings;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow
    public GameSettings gameSettings;

    @Inject(method = "startGame", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        OculusMod.INSTANCE.verifyInitialization();
    }

    /**
     * Redirect the call to InventoryPlayer.changeCurrentItem(int) made inside Minecraft.runTick(...)
     * If the configured oculus key is held down, swallow the call (prevent slot change).
     */
    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/InventoryPlayer;changeCurrentItem(I)V"))
    private void redirectChangeCurrentItem(InventoryPlayer inventory, int amount) {
        if (OculusMod.INSTANCE.failedInitialization) {
            return;
        }

        boolean zoomKeyDown = false;

        if (this.gameSettings instanceof IGameSettings) {
            KeyBinding oculus = ((IGameSettings) this.gameSettings).oculus$getOculusKeyBinding();
            if (oculus != null) {
                int kc = oculus.keyCode;
                if (kc >= 0 && Keyboard.isKeyDown(kc)) {
                    zoomKeyDown = true;
                }
            }
        }

        if (zoomKeyDown) {
            return;
        }

        inventory.changeCurrentItem(amount);
    }
}
