package org.vivecraft.api.data;

import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionfc;

import javax.annotation.Nullable;

/**
 * STUB - 编译时占位，运行时由 Vivecraft 提供真实实现
 */
public interface VRBodyPartData {
    Vec3 getPos();
    Vec3 getDir();
    double getPitch();
    double getYaw();
    double getRoll();
    Quaternionfc getRotation();
}
