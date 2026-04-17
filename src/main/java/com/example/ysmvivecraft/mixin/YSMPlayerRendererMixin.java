package com.example.ysmvivecraft.mixin;

import com.example.ysmvivecraft.VRDataCache;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin targeting YSM's player renderer to inject VR data before rendering.
 *
 * <p>This Mixin hooks into YSM's player model rendering pipeline to:
 * <ul>
 *   <li>Update the VR data cache with current Vivecraft tracking data</li>
 *   <li>Allow YSM animation variables to be influenced by VR state</li>
 * </ul>
 * </p>
 *
 * <p>Target class: {@code ysm.client.render.YSMPlayerRenderer}
 * (adjust the target class name to match the actual YSM class)</p>
 */
@Mixin(targets = "ysm.client.render.YSMPlayerRenderer", remap = false)
public class YSMPlayerRendererMixin {

    /**
     * Inject at the beginning of the render method to update VR cache.
     *
     * <p>The exact method signature depends on YSM's version. Common names:
     * - render(AbstractClientPlayer, float, float, PoseStack, MultiBufferSource, int)
     * - renderPlayer(...)
     * </p>
     */
    @Inject(
        method = "render",
        at = @At("HEAD"),
        require = 0  // Don't fail if method not found (optional compatibility)
    )
    private void onRenderHead(Object... args, CallbackInfo ci) {
        // Find the player argument and update VR cache
        for (Object arg : args) {
            if (arg instanceof Player player) {
                VRDataCache.update(player);
                break;
            }
        }
    }
}
