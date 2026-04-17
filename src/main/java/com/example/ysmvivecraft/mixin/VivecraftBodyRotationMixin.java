package com.example.ysmvivecraft.mixin;

import com.example.ysmvivecraft.VRDataCache;
import com.example.ysmvivecraft.VivecraftHelper;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin targeting Vivecraft's body rotation system to fix body rotation for YSM models.
 *
 * <p>When a player uses VR, Vivecraft updates the player's body rotation based on HMD/controller
 * data. This Mixin ensures that YSM's model also respects those rotations.</p>
 *
 * <p>Target class: {@code org.vivecraft.client.render.VRPlayerRenderer}
 * or the specific Vivecraft class that handles body rotation.</p>
 */
@Mixin(targets = "org.vivecraft.client_vr.render.helpers.RenderHelper", remap = false)
public class VivecraftBodyRotationMixin {

    /**
     * Inject to intercept VR body rotation calculations.
     *
     * <p>This allows us to notify YSM of the new body rotation values.</p>
     */
    @Inject(
        method = "applyVRBodyTransforms",
        at = @At("RETURN"),
        require = 0
    )
    private static void afterBodyTransforms(Player player, CallbackInfo ci) {
        if (VivecraftHelper.isVRPlayer(player)) {
            VRDataCache.update(player);
        }
    }
}
