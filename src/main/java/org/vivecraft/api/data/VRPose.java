package org.vivecraft.api.data;

import javax.annotation.Nullable;
import net.minecraft.world.InteractionHand;

/**
 * STUB - 编译时占位，运行时由 Vivecraft 提供真实实现
 */
public interface VRPose {
    @Nullable VRBodyPartData getBodyPartData(VRBodyPart vrBodyPart);
    boolean isSeated();
    boolean isLeftHanded();

    default @Nullable VRBodyPartData getHead() {
        return getBodyPartData(VRBodyPart.HEAD);
    }
    default @Nullable VRBodyPartData getMainHand() {
        return getBodyPartData(VRBodyPart.MAIN_HAND);
    }
    default @Nullable VRBodyPartData getOffHand() {
        return getBodyPartData(VRBodyPart.OFF_HAND);
    }
    default @Nullable VRBodyPartData getHand(InteractionHand hand) {
        return getBodyPartData(VRBodyPart.fromInteractionHand(hand));
    }
}
