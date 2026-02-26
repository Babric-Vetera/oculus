package com.github.qe7.oculus.mixins;

import com.github.qe7.oculus.OculusMod;
import com.github.qe7.oculus.interfaces.IGameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.Material;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {

    @Shadow
    private Minecraft mc;

    @Unique
    private float zoomLevel = DEFAULT_ZOOM;

    @Unique
    private static final float DEFAULT_FOV = 70.0F;
    @Unique
    private static final float WATER_FOV = 60.0F;
    @Unique
    private static final float DEFAULT_ZOOM = 0.5F;
    @Unique
    private static final float MIN_ZOOM = 0.2F;
    @Unique
    private static final float MAX_ZOOM = 1.0F;
    @Unique
    private static final float ZOOM_STEP = 0.1F;

    /*
     * Inject into EntityRenderer.getFOVModifier to modify the field of view based on whether the player is underwater,
     * whether the player is dead, and whether the custom oculus zoom key is held down. If the zoom key is held down,
     * also allow adjusting the zoom level with the mouse wheel.
     */
    @Inject(method = "getFOVModifier", at = @At("HEAD"), cancellable = true)
    private void getFOVModifier(float partialTicks, CallbackInfoReturnable<Float> cir) {
        if (OculusMod.INSTANCE.failedInitialization) {
            return;
        }

        if (mc == null || mc.renderViewEntity == null) {
            return;
        }

        EntityLiving view = mc.renderViewEntity;
        float fov = DEFAULT_FOV;

        if (view.isInsideOfMaterial(Material.water)) {
            fov = WATER_FOV;
        }

        if (view.health <= 0) {
            float deathTime = view.deathTime + partialTicks;
            fov /= (1.0F - 500.0F / (deathTime + 500.0F)) * 2.0F + 1.0F;
        }

        KeyBinding oculus = null;
        if (mc.gameSettings instanceof IGameSettings) {
            oculus = ((IGameSettings) mc.gameSettings).oculus$getOculusKeyBinding();
        }

        if (oculus != null) {
            int keyCode = oculus.keyCode;
            boolean zoomKeyDown = keyCode >= 0 && Keyboard.isKeyDown(keyCode);

            if (zoomKeyDown) {
                fov *= zoomLevel;

                int wheel = Mouse.getDWheel();
                if (wheel != 0) {
                    if (wheel < 0) {
                        zoomLevel = Math.min(MAX_ZOOM, zoomLevel + ZOOM_STEP);
                    } else {
                        zoomLevel = Math.max(MIN_ZOOM, zoomLevel - ZOOM_STEP);
                    }
                }
            } else {
                zoomLevel = DEFAULT_ZOOM;
            }
        }

        cir.setReturnValue(fov);
    }
}
