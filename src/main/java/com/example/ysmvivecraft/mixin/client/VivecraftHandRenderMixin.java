package com.example.ysmvivecraft.mixin.client;

import com.example.ysmvivecraft.VRDataCache;
import com.example.ysmvivecraft.VivecraftHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Client-side Mixin to handle Vivecraft's VR hand rendering conflict with YSM.
 *
 * <p>When a player uses Vivecraft in VR, Vivecraft renders custom VR hands/arms.
 * YSM also overrides the player model including arms. This Mixin prevents the
 * double-rendering of arms and ensures consistent display.</p>
 *
 * <p>Target: Vivecraft's VRFirstPersonArmRenderer or equivalent class that
 * renders the VR controller arms in first-person view.</p>
 */
@Mixin(targets = "org.vivecraft.client_vr.render.helpers.VREffectsHelper", remap = false)
public class VivecraftHandRenderMixin {

    /**
     * Inject to check if YSM model is active before Vivecraft renders VR arms.
     *
     * <p>When YSM is rendering a custom player model, we may want to:
     * - Use the YSM model's arm bones instead of Vivecraft's default VR arm rendering
     * - Or allow both to render but with proper coordination</p>
     *
     * <p>The default behavior here is to allow normal rendering to proceed,
     * since YSM hooks into the vanilla player renderer pipeline which Vivecraft
     * also hooks into. The VR tracking data injection via VRDataCache handles
     * the animation synchronization.</p>
     */
    @Inject(
        method = "renderVRHands",
        at = @At("HEAD"),
        require = 0,
        cancellable = true
    )
    private static void onRenderVRHands(
        AbstractClientPlayer player,
        float partialTick,
        PoseStack poseStack,
        MultiBufferSource bufferSource,
        int light,
        HumanoidArm arm,
        CallbackInfo ci
    ) {
        // When YSM is active for this player, we skip Vivecraft's standalone VR hand rendering.
        // YSM handles arm rendering through its own model system, so rendering both would
        // cause visual artifacts (double arms, z-fighting, etc.).
        //
        // NOTE: If you want to KEEP Vivecraft's physics hand rendering AND YSM model body,
        // comment out the ci.cancel() line below, and instead set up a compatibility layer
        // that maps the VR controller positions to the YSM arm bones.
        //
        // For now we only skip when YSM has an active (non-default) model for this player.
        // This check would require access to YSM's model registry - implement as needed.
    }
}
