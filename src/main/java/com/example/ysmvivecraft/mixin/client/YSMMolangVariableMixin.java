package com.example.ysmvivecraft.mixin.client;

import com.example.ysmvivecraft.YSMVRAnimationProvider;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to inject VR variables into YSM's Molang variable evaluation.
 *
 * <p>YSM uses a custom Molang evaluator (based on GeckoLib's MolangParser)
 * to evaluate animation variables. This Mixin intercepts variable lookups
 * and injects our VR tracking data when the variable name matches.</p>
 *
 * <p>Target class: YSM's MolangVariable or similar class responsible for
 * resolving custom "variable.*" expressions in animations.</p>
 *
 * <h3>How YSM Molang works:</h3>
 * <ol>
 *   <li>Animation JSON references variables like {@code variable.is_vr_player}</li>
 *   <li>YSM evaluates these at runtime by looking up registered variable providers</li>
 *   <li>We intercept the lookup and return VR data for our custom variables</li>
 * </ol>
 */
@Mixin(targets = "ysm.common.animation.molang.MolangVariable", remap = false)
public class YSMMolangVariableMixin {

    /**
     * Intercept Molang variable evaluation to inject VR values.
     *
     * <p>The target method typically has a signature like:
     * {@code double eval(MolangLookup lookup)}</p>
     *
     * <p>We need to inject early enough to override the default value
     * when the variable name matches a VR variable.</p>
     */
    @Inject(
        method = "eval",
        at = @At("HEAD"),
        require = 0,
        cancellable = true
    )
    private void onEvalVariable(Object lookup, CallbackInfoReturnable<Double> cir) {
        // Get the variable name (this depends on YSM's internal structure)
        // The field name may be different in the actual YSM source
        // Try to get variableName from this instance
        try {
            // Access the variable name field via reflection if needed
            String varName = getVariableName();
            if (varName != null && varName.startsWith("vr_") || "is_vr_player".equals(varName)) {
                // Get the player from the lookup context
                Player player = getPlayerFromLookup(lookup);
                Float vrValue = YSMVRAnimationProvider.getVariable(varName, player);
                if (vrValue != null) {
                    cir.setReturnValue(vrValue.doubleValue());
                }
            }
        } catch (Exception e) {
            // Silently ignore if we can't inject - the animation will use default values
        }
    }

    /**
     * Helper method to get the variable name from this Molang variable instance.
     * The actual field name depends on YSM's internal structure.
     *
     * @return the variable name, or null if unable to determine
     */
    private String getVariableName() {
        try {
            // Common field names in Molang implementations
            var fields = this.getClass().getDeclaredFields();
            for (var field : fields) {
                if (field.getType() == String.class) {
                    field.setAccessible(true);
                    return (String) field.get(this);
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    /**
     * Helper to extract a Player from a Molang lookup context.
     *
     * @param lookup the Molang lookup object
     * @return the player, or null if not found
     */
    private Player getPlayerFromLookup(Object lookup) {
        if (lookup instanceof Player player) return player;
        try {
            // Try to find a Player field in the lookup object
            var fields = lookup.getClass().getDeclaredFields();
            for (var field : fields) {
                if (Player.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    return (Player) field.get(lookup);
                }
            }
        } catch (Exception ignored) {}
        return null;
    }
}
