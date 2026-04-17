package com.example.ysmvivecraft;

import net.minecraft.world.entity.player.Player;

/**
 * YSM Animation Variable Provider for Vivecraft VR data.
 *
 * <p>This class provides the bridge between Vivecraft's VR tracking data
 * and YSM's Molang animation variable system.</p>
 *
 * <p>YSM uses GeckoLib's Molang system for animations. To inject custom variables,
 * we provide values that YSM's animation controller can read.</p>
 *
 * <h2>Usage in YSM Animation Files (.animation.json)</h2>
 * <p>Once this mod is installed, the following custom variables become available
 * in your YSM model's animation controller:</p>
 *
 * <pre>{@code
 * // Check if player is in VR
 * variable.is_vr_player
 *
 * // Head/HMD tracking
 * variable.vr_head_pitch   // Head pitch in degrees (-90 to 90)
 * variable.vr_head_yaw     // Head yaw in degrees (-180 to 180)
 *
 * // Right hand controller
 * variable.vr_right_hand_pitch
 * variable.vr_right_hand_yaw
 * variable.vr_right_hand_roll
 *
 * // Left hand controller
 * variable.vr_left_hand_pitch
 * variable.vr_left_hand_yaw
 * variable.vr_left_hand_roll
 * }</pre>
 *
 * <h2>Example Animation Controller Usage</h2>
 * <pre>{@code
 * {
 *   "format_version": "1.10.0",
 *   "animation_controllers": {
 *     "controller.animation.player.vr_arms": {
 *       "initial_state": "default",
 *       "states": {
 *         "default": {
 *           "animations": [
 *             { "vr_right_arm": "variable.is_vr_player == 1.0" },
 *             { "vr_left_arm": "variable.is_vr_player == 1.0" }
 *           ],
 *           "transitions": []
 *         }
 *       }
 *     }
 *   }
 * }
 * }</pre>
 *
 * <h2>Example Animation File</h2>
 * <pre>{@code
 * {
 *   "format_version": "1.8.0",
 *   "animations": {
 *     "animation.player.vr_right_arm": {
 *       "loop": true,
 *       "bones": {
 *         "rightArm": {
 *           "rotation": [
 *             "variable.vr_right_hand_pitch",
 *             "variable.vr_right_hand_yaw",
 *             "variable.vr_right_hand_roll"
 *           ]
 *         }
 *       }
 *     }
 *   }
 * }
 * }</pre>
 */
public class YSMVRAnimationProvider {

    /**
     * Variable names used in YSM Molang animations.
     * These match what YSM model creators should use in their animation files.
     */
    public static final String VAR_IS_VR = "variable.is_vr_player";
    public static final String VAR_HEAD_PITCH = "variable.vr_head_pitch";
    public static final String VAR_HEAD_YAW = "variable.vr_head_yaw";
    public static final String VAR_RIGHT_HAND_PITCH = "variable.vr_right_hand_pitch";
    public static final String VAR_RIGHT_HAND_YAW = "variable.vr_right_hand_yaw";
    public static final String VAR_RIGHT_HAND_ROLL = "variable.vr_right_hand_roll";
    public static final String VAR_LEFT_HAND_PITCH = "variable.vr_left_hand_pitch";
    public static final String VAR_LEFT_HAND_YAW = "variable.vr_left_hand_yaw";
    public static final String VAR_LEFT_HAND_ROLL = "variable.vr_left_hand_roll";

    /**
     * Get a float value for a named YSM Molang variable, sourced from VR data.
     *
     * <p>This method is called by the Mixin hooks when YSM evaluates a Molang expression.
     * Returns the corresponding VR tracking value, or null if the variable is not
     * a VR variable.</p>
     *
     * @param variableName the Molang variable name (e.g., "variable.vr_head_pitch")
     * @param player       the player being rendered
     * @return the float value for the variable, or null if not a VR variable
     */
    public static Float getVariable(String variableName, Player player) {
        if (player == null || !VivecraftHelper.isVivecraftPresent()) {
            return null;
        }

        return switch (variableName) {
            case "is_vr_player" -> VRDataCache.IS_VR_PLAYER.get() ? 1.0f : 0.0f;
            case "vr_head_pitch" -> VRDataCache.HEAD_PITCH.get();
            case "vr_head_yaw" -> VRDataCache.HEAD_YAW.get();
            case "vr_right_hand_pitch" -> VRDataCache.RIGHT_HAND_PITCH.get();
            case "vr_right_hand_yaw" -> VRDataCache.RIGHT_HAND_YAW.get();
            case "vr_right_hand_roll" -> VRDataCache.RIGHT_HAND_ROLL.get();
            case "vr_left_hand_pitch" -> VRDataCache.LEFT_HAND_PITCH.get();
            case "vr_left_hand_yaw" -> VRDataCache.LEFT_HAND_YAW.get();
            case "vr_left_hand_roll" -> VRDataCache.LEFT_HAND_ROLL.get();
            default -> null;
        };
    }
}
