package com.example.ysmvivecraft;

import net.minecraft.world.entity.player.Player;

/**
 * Cache for VR data per player to avoid expensive API calls each frame.
 *
 * <p>Stores the latest VR tracking data for use in YSM animation variable injection.
 * Data is updated each tick/frame by the Mixin hooks.</p>
 */
public class VRDataCache {

    // Whether the player is in VR
    public static final ThreadLocal<Boolean> IS_VR_PLAYER = ThreadLocal.withInitial(() -> false);

    // Head tracking angles
    public static final ThreadLocal<Float> HEAD_PITCH = ThreadLocal.withInitial(() -> 0.0f);
    public static final ThreadLocal<Float> HEAD_YAW = ThreadLocal.withInitial(() -> 0.0f);

    // Main hand (right hand by default) controller angles
    public static final ThreadLocal<Float> RIGHT_HAND_PITCH = ThreadLocal.withInitial(() -> 0.0f);
    public static final ThreadLocal<Float> RIGHT_HAND_YAW = ThreadLocal.withInitial(() -> 0.0f);
    public static final ThreadLocal<Float> RIGHT_HAND_ROLL = ThreadLocal.withInitial(() -> 0.0f);

    // Off hand (left hand by default) controller angles
    public static final ThreadLocal<Float> LEFT_HAND_PITCH = ThreadLocal.withInitial(() -> 0.0f);
    public static final ThreadLocal<Float> LEFT_HAND_YAW = ThreadLocal.withInitial(() -> 0.0f);
    public static final ThreadLocal<Float> LEFT_HAND_ROLL = ThreadLocal.withInitial(() -> 0.0f);

    /**
     * Update the VR data cache for a given player.
     * Should be called before rendering a player's YSM model.
     *
     * @param player the player to update data for
     */
    public static void update(Player player) {
        boolean isVR = VivecraftHelper.isVRPlayer(player);
        IS_VR_PLAYER.set(isVR);

        if (isVR) {
            HEAD_PITCH.set((float) VivecraftHelper.getHeadPitch(player));
            HEAD_YAW.set((float) VivecraftHelper.getHeadYaw(player));

            boolean leftHanded = VivecraftHelper.isLeftHandedVR(player);

            // In Vivecraft, main hand = dominant hand
            // If left-handed, main hand is left, otherwise right
            if (leftHanded) {
                LEFT_HAND_PITCH.set((float) VivecraftHelper.getMainHandPitch(player));
                LEFT_HAND_YAW.set((float) VivecraftHelper.getMainHandYaw(player));
                LEFT_HAND_ROLL.set((float) VivecraftHelper.getMainHandRoll(player));

                RIGHT_HAND_PITCH.set((float) VivecraftHelper.getOffHandPitch(player));
                RIGHT_HAND_YAW.set((float) VivecraftHelper.getOffHandYaw(player));
                RIGHT_HAND_ROLL.set((float) VivecraftHelper.getOffHandRoll(player));
            } else {
                RIGHT_HAND_PITCH.set((float) VivecraftHelper.getMainHandPitch(player));
                RIGHT_HAND_YAW.set((float) VivecraftHelper.getMainHandYaw(player));
                RIGHT_HAND_ROLL.set((float) VivecraftHelper.getMainHandRoll(player));

                LEFT_HAND_PITCH.set((float) VivecraftHelper.getOffHandPitch(player));
                LEFT_HAND_YAW.set((float) VivecraftHelper.getOffHandYaw(player));
                LEFT_HAND_ROLL.set((float) VivecraftHelper.getOffHandRoll(player));
            }
        } else {
            // Reset to defaults when not in VR
            HEAD_PITCH.set(0.0f);
            HEAD_YAW.set(0.0f);
            RIGHT_HAND_PITCH.set(0.0f);
            RIGHT_HAND_YAW.set(0.0f);
            RIGHT_HAND_ROLL.set(0.0f);
            LEFT_HAND_PITCH.set(0.0f);
            LEFT_HAND_YAW.set(0.0f);
            LEFT_HAND_ROLL.set(0.0f);
        }
    }
}
