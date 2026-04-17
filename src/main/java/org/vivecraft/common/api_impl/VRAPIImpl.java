package org.vivecraft.common.api_impl;

import net.minecraft.world.entity.player.Player;
import org.vivecraft.api.VRAPI;
import org.vivecraft.api.data.VRPose;
import org.vivecraft.api.data.VRPoseHistory;

import javax.annotation.Nullable;

/**
 * STUB - 编译时占位，运行时由 Vivecraft 提供真实实现
 * 运行时此类会被 Vivecraft 的真实实现替换。
 */
public class VRAPIImpl implements VRAPI {

    /** STUB 单例，运行时被替换 */
    public static final VRAPI INSTANCE = new VRAPIImpl();

    @Override
    public boolean isVRPlayer(Player player) {
        return false;
    }

    @Override
    public @Nullable VRPose getVRPose(Player player) {
        return null;
    }

    @Override
    public @Nullable VRPoseHistory getHistoricalVRPoses(Player player) {
        return null;
    }
}
