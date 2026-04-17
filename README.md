# YSM Vivecraft Compat

**Yes Steve Model (YSM) × Vivecraft 兼容性模组** | NeoForge 1.21.1

---

## 简介

本模组为 **Yes Steve Model（YSM）** 和 **Vivecraft** 提供兼容性支持，让 VR 玩家在使用自定义 YSM 模型的同时，也能享受到 VR 身体追踪功能。

### 解决的问题

| 问题 | 解决方案 |
|------|---------|
| YSM 自定义模型无法响应 VR 控制器追踪数据 | 将 Vivecraft VR 追踪角度注入 YSM 的 Molang 动画变量系统 |
| Vivecraft VR 手部渲染与 YSM 手臂层冲突（Z-fighting） | 通过 Mixin 协调两者的渲染顺序 |
| VR 玩家头部旋转与 YSM 模型头骨不同步 | 将 HMD 追踪数据映射到 YSM 头骨旋转 |

---

## 依赖

| 模组 | 版本 | 类型 |
|------|------|------|
| NeoForge | 21.1.172+ | 必须 |
| Yes Steve Model (YSM) | 2.5.1+ | 必须 |
| Vivecraft | 1.21.1-1.3.7+ | 必须 |

---

## 安装

1. 确保已安装上述依赖模组
2. 将本模组的 `.jar` 文件放入 `.minecraft/mods/` 文件夹
3. 启动游戏

---

## VR 动画变量

安装本模组后，YSM 模型的动画文件中可使用以下 Molang 变量：

| 变量名 | 类型 | 说明 |
|--------|------|------|
| `variable.is_vr_player` | `0.0 / 1.0` | 玩家是否在 VR 模式下 |
| `variable.vr_head_pitch` | `float (度)` | HMD 头显俯仰角（-90° ~ 90°） |
| `variable.vr_head_yaw` | `float (度)` | HMD 头显水平旋转角 |
| `variable.vr_right_hand_pitch` | `float (度)` | 右手控制器俯仰角 |
| `variable.vr_right_hand_yaw` | `float (度)` | 右手控制器水平角 |
| `variable.vr_right_hand_roll` | `float (度)` | 右手控制器翻滚角 |
| `variable.vr_left_hand_pitch` | `float (度)` | 左手控制器俯仰角 |
| `variable.vr_left_hand_yaw` | `float (度)` | 左手控制器水平角 |
| `variable.vr_left_hand_roll` | `float (度)` | 左手控制器翻滚角 |

---

## 为模型创作者：如何使用 VR 变量

### 1. 动画文件（`vivecraft.animation.json`）

```json
{
  "format_version": "1.8.0",
  "animations": {
    "animation.player.vr_tracking": {
      "loop": true,
      "bones": {
        "head": {
          "rotation": [
            "variable.vr_head_pitch",
            "variable.vr_head_yaw",
            "0"
          ]
        },
        "rightArm": {
          "rotation": [
            "variable.vr_right_hand_pitch",
            "variable.vr_right_hand_yaw",
            "variable.vr_right_hand_roll"
          ]
        },
        "leftArm": {
          "rotation": [
            "variable.vr_left_hand_pitch",
            "variable.vr_left_hand_yaw",
            "variable.vr_left_hand_roll"
          ]
        }
      }
    }
  }
}
```

### 2. 动画控制器（`vivecraft.controller.json`）

```json
{
  "format_version": "1.10.0",
  "animation_controllers": {
    "controller.animation.player.vivecraft_main": {
      "initial_state": "default",
      "states": {
        "default": {
          "animations": [],
          "transitions": [
            { "vr_active": "variable.is_vr_player == 1.0" }
          ]
        },
        "vr_active": {
          "animations": ["vr_tracking"],
          "transitions": [
            { "default": "variable.is_vr_player == 0.0" }
          ]
        }
      }
    }
  }
}
```

> 📁 `example_model/animations/` 目录下有完整的示例文件可直接参考。

---

## 技术实现原理

```
Vivecraft VR Tracking
       ↓
  VivecraftHelper
  (读取 VRAPI 数据)
       ↓
   VRDataCache
  (缓存至线程本地)
       ↓
YSMMolangVariableMixin
  (拦截 YSM Molang 求值)
       ↓
  YSM 动画系统
  (驱动模型骨骼)
       ↓
  屏幕上的 VR 玩家
```

### Mixin 列表

| Mixin 类 | 目标 | 作用 |
|---------|------|------|
| `YSMPlayerRendererMixin` | `ysm.client.render.YSMPlayerRenderer` | 在渲染前更新 VR 缓存 |
| `VivecraftBodyRotationMixin` | Vivecraft 身体旋转辅助类 | 同步 VR 身体旋转到 YSM |
| `YSMLayerMixin` | YSM 模型层渲染器 | 客户端 VR 数据更新 |
| `VivecraftHandRenderMixin` | Vivecraft 手部渲染辅助类 | 防止手部双重渲染 |
| `YSMMolangVariableMixin` | YSM Molang 变量系统 | 注入 VR 追踪变量值 |

---

## 开发者注意事项

### 关于 Mixin 目标类名

本模组使用了 `require = 0` 参数，这意味着如果 Mixin 目标类不存在（如 YSM/Vivecraft 版本更新后类名变化），注入会被忽略而不会崩溃。

如果兼容性失效，请检查：
1. YSM 源码中实际的渲染器类名
2. Vivecraft 中实际的 VR 数据辅助类名
3. YSM 的 Molang 变量评估类名

这些类名可能因版本不同而变化，需要对应更新 Mixin 的 `targets` 参数。

### 本地构建

```bash
# 克隆项目
git clone <本仓库地址>

# 构建
./gradlew build

# 输出在 build/libs/ 目录
```

---

## 许可证

MIT License - 详见 [LICENSE](LICENSE) 文件

---

## 致谢

- [Yes Steve Model](https://modrinth.com/mod/yes-steve-model) - by TartaricAcid
- [Vivecraft](https://github.com/Vivecraft/VivecraftMod) - VR Minecraft
- [GeckoLib](https://github.com/bernie-g/geckolib) - YSM 的动画后端
