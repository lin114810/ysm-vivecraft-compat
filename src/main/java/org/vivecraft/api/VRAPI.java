package org.vivecraft.api;

import net.minecraft.world.entity.player.Player;
import org.vivecraft.api.data.VRPose;
import org.vivecraft.api.data.VRPoseHistory;
import org.vivecraft.common.api_impl.VRAPIImpl;

import javax.annotation.Nullable;

/**
 * STUB - 编译时占位，运行时由 Vivecraft 提供真实实现
 */
public interface VRAPI {
    boolean isVRPlayer(Player player);

    @Nullable
    VRPose getVRPose(Player player);

    @Nullable
    VRPoseHistory getHistoricalVRPoses(Player player);

    static VRAPI instance() {
        return VRAPIImpl.INSTANCE;
    }
}
