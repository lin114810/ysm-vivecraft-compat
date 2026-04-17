package org.vivecraft.api.data;

/**
 * STUB - 编译时占位，运行时由 Vivecraft 提供真实实现
 */
public enum VRBodyPart {
    HEAD, MAIN_HAND, OFF_HAND;

    public static VRBodyPart fromInteractionHand(net.minecraft.world.InteractionHand hand) {
        return hand == net.minecraft.world.InteractionHand.MAIN_HAND ? MAIN_HAND : OFF_HAND;
    }
}
