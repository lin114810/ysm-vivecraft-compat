package com.example.ysmvivecraft.mixin.client;

import com.example.ysmvivecraft.VRDataCache;
import com.example.ysmvivecraft.VivecraftHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Client-side Mixin for YSM's model layer rendering.
 *
 * <p>This hooks into YSM's GeckoLib-based animation system to inject
 * VR tracking angles into the model's bone animations.</p>
 *
 * <p>YSM uses GeckoLib for animation. The key integration point is through
 * YSM's AnimationController or its custom molang variable registry.</p>
 *
 * <p>Target: YSM's YSMGeoRenderer or similar animation renderer class</p>
 */
@Mixin(targets = "ysm.client.render.GeoLayerRenderer", remap = false)
public class YSMLayerMixin {

    /**
     * Inject before YSM renders its model layer to prepare VR bone data.
     *
     * <p>We inject at HEAD to ensure VR data is fresh before the animation
     * system reads molang variables.</p>
     */
    @Inject(
        method = "renderForEntity",
        at = @At("HEAD"),
        require = 0
    )
    private void onRenderLayerHead(
        Player player,
        float partialTick,
        PoseStack poseStack,
        MultiBufferSource bufferSource,
        int packedLight,
        CallbackInfo ci
    ) {
        if (player != null && VivecraftHelper.isVivecraftPresent()) {
            VRDataCache.update(player);
        }
    }
}
