package com.example.ysmvivecraft;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionfc;
import org.vivecraft.api.VRAPI;
import org.vivecraft.api.data.VRBodyPart;
import org.vivecraft.api.data.VRBodyPartData;
import org.vivecraft.api.data.VRPose;

import javax.annotation.Nullable;

/**
 * Utility class to safely access Vivecraft's VR tracking data.
 *
 * <p>All methods handle the case where Vivecraft might not be loaded,
 * using safe reflection or try-catch to avoid ClassNotFoundException.</p>
 */
public class VivecraftHelper {

    private static boolean vivecraftPresent = false;
    private static boolean checkedPresence = false;

    /**
     * Check if Vivecraft mod is currently loaded and active.
     */
    public static boolean isVivecraftPresent() {
        if (!checkedPresence) {
            try {
                Class.forName("org.vivecraft.api.VRAPI");
                vivecraftPresent = true;
                YSMVivecraftCompat.LOGGER.info("[YSM-Vivecraft] Vivecraft detected!");
            } catch (ClassNotFoundException e) {
                vivecraftPresent = false;
                YSMVivecraftCompat.LOGGER.warn("[YSM-Vivecraft] Vivecraft not found, VR features will be disabled.");
            }
            checkedPresence = true;
        }
        return vivecraftPresent;
    }

    /**
     * Check if a specific player is currently using VR via Vivecraft.
     *
     * @param player the player to check
     * @return true if the player is in VR mode
     */
    public static boolean isVRPlayer(Player player) {
        if (!isVivecraftPresent()) return false;
        try {
            return VRAPI.instance().isVRPlayer(player);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the current VR pose of a player.
     *
     * @param player the player to query
     * @return the VRPose, or null if not available
     */
    @Nullable
    public static VRPose getVRPose(Player player) {
        if (!isVivecraftPresent()) return null;
        try {
            return VRAPI.instance().getVRPose(player);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get the head (HMD) position of a VR player in Minecraft world coordinates.
     *
     * @param player the player
     * @return world position of the head, or null if unavailable
     */
    @Nullable
    public static Vec3 getHeadPosition(Player player) {
        VRPose pose = getVRPose(player);
        if (pose == null) return null;
        VRBodyPartData head = pose.getHead();
        if (head == null) return null;
        return head.getPos();
    }

    /**
     * Get the head (HMD) direction vector of a VR player.
     *
     * @param player the player
     * @return normalized direction vector the player is looking, or null if unavailable
     */
    @Nullable
    public static Vec3 getHeadDirection(Player player) {
        VRPose pose = getVRPose(player);
        if (pose == null) return null;
        VRBodyPartData head = pose.getHead();
        if (head == null) return null;
        return head.getDir();
    }

    /**
     * Get the head rotation quaternion of a VR player.
     *
     * @param player the player
     * @return quaternion rotation, or null if unavailable
     */
    @Nullable
    public static Quaternionfc getHeadRotation(Player player) {
        VRPose pose = getVRPose(player);
        if (pose == null) return null;
        VRBodyPartData head = pose.getHead();
        if (head == null) return null;
        return head.getRotation();
    }

    /**
     * Get the main hand controller position.
     *
     * @param player the player
     * @return world position of the main hand controller, or null if unavailable
     */
    @Nullable
    public static Vec3 getMainHandPosition(Player player) {
        VRPose pose = getVRPose(player);
        if (pose == null) return null;
        VRBodyPartData hand = pose.getMainHand();
        if (hand == null) return null;
        return hand.getPos();
    }

    /**
     * Get the main hand controller direction.
     *
     * @param player the player
     * @return direction of the main hand controller, or null if unavailable
     */
    @Nullable
    public static Vec3 getMainHandDirection(Player player) {
        VRPose pose = getVRPose(player);
        if (pose == null) return null;
        VRBodyPartData hand = pose.getMainHand();
        if (hand == null) return null;
        return hand.getDir();
    }

    /**
     * Get the off hand controller position.
     *
     * @param player the player
     * @return world position of the off hand controller, or null if unavailable
     */
    @Nullable
    public static Vec3 getOffHandPosition(Player player) {
        VRPose pose = getVRPose(player);
        if (pose == null) return null;
        VRBodyPartData hand = pose.getOffHand();
        if (hand == null) return null;
        return hand.getPos();
    }

    /**
     * Get the off hand controller direction.
     *
     * @param player the player
     * @return direction of the off hand controller, or null if unavailable
     */
    @Nullable
    public static Vec3 getOffHandDirection(Player player) {
        VRPose pose = getVRPose(player);
        if (pose == null) return null;
        VRBodyPartData hand = pose.getOffHand();
        if (hand == null) return null;
        return hand.getDir();
    }

    /**
     * Get the yaw angle (in degrees) of the main hand controller.
     * This is useful for rotating the YSM arm bone to match the VR controller.
     *
     * @param player the player
     * @return yaw in degrees, or 0.0 if unavailable
     */
    public static double getMainHandYaw(Player player) {
        VRPose pose = getVRPose(player);
        if (pose == null) return 0.0;
        VRBodyPartData hand = pose.getMainHand();
        if (hand == null) return 0.0;
        // Convert radians to degrees
        return Math.toDegrees(hand.getYaw());
    }

    /**
     * Get the pitch angle (in degrees) of the main hand controller.
     *
     * @param player the player
     * @return pitch in degrees, or 0.0 if unavailable
     */
    public static double getMainHandPitch(Player player) {
        VRPose pose = getVRPose(player);
        if (pose == null) return 0.0;
        VRBodyPartData hand = pose.getMainHand();
        if (hand == null) return 0.0;
        return Math.toDegrees(hand.getPitch());
    }

    /**
     * Get the roll angle (in degrees) of the main hand controller.
     *
     * @param player the player
     * @return roll in degrees, or 0.0 if unavailable
     */
    public static double getMainHandRoll(Player player) {
        VRPose pose = getVRPose(player);
        if (pose == null) return 0.0;
        VRBodyPartData hand = pose.getMainHand();
        if (hand == null) return 0.0;
        return Math.toDegrees(hand.getRoll());
    }

    /**
     * Get the yaw angle (in degrees) of the off hand controller.
     *
     * @param player the player
     * @return yaw in degrees, or 0.0 if unavailable
     */
    public static double getOffHandYaw(Player player) {
        VRPose pose = getVRPose(player);
        if (pose == null) return 0.0;
        VRBodyPartData hand = pose.getOffHand();
        if (hand == null) return 0.0;
        return Math.toDegrees(hand.getYaw());
    }

    /**
     * Get the pitch angle (in degrees) of the off hand controller.
     *
     * @param player the player
     * @return pitch in degrees, or 0.0 if unavailable
     */
    public static double getOffHandPitch(Player player) {
        VRPose pose = getVRPose(player);
        if (pose == null) return 0.0;
        VRBodyPartData hand = pose.getOffHand();
        if (hand == null) return 0.0;
        return Math.toDegrees(hand.getPitch());
    }

    /**
     * Get the roll angle (in degrees) of the off hand controller.
     *
     * @param player the player
     * @return roll in degrees, or 0.0 if unavailable
     */
    public static double getOffHandRoll(Player player) {
        VRPose pose = getVRPose(player);
        if (pose == null) return 0.0;
        VRBodyPartData hand = pose.getOffHand();
        if (hand == null) return 0.0;
        return Math.toDegrees(hand.getRoll());
    }

    /**
     * Get the pitch angle (in degrees) of the head/HMD.
     *
     * @param player the player
     * @return pitch in degrees, or 0.0 if unavailable
     */
    public static double getHeadPitch(Player player) {
        VRPose pose = getVRPose(player);
        if (pose == null) return 0.0;
        VRBodyPartData head = pose.getHead();
        if (head == null) return 0.0;
        return Math.toDegrees(head.getPitch());
    }

    /**
     * Get the yaw angle (in degrees) of the head/HMD.
     *
     * @param player the player
     * @return yaw in degrees, or 0.0 if unavailable
     */
    public static double getHeadYaw(Player player) {
        VRPose pose = getVRPose(player);
        if (pose == null) return 0.0;
        VRBodyPartData head = pose.getHead();
        if (head == null) return 0.0;
        return Math.toDegrees(head.getYaw());
    }

    /**
     * Check if the player is in seated VR mode.
     *
     * @param player the player
     * @return true if player is seated in VR
     */
    public static boolean isSeatedVR(Player player) {
        VRPose pose = getVRPose(player);
        if (pose == null) return false;
        return pose.isSeated();
    }

    /**
     * Check if the player is using left-handed VR.
     *
     * @param player the player
     * @return true if player is left-handed in VR
     */
    public static boolean isLeftHandedVR(Player player) {
        VRPose pose = getVRPose(player);
        if (pose == null) return false;
        return pose.isLeftHanded();
    }
}
