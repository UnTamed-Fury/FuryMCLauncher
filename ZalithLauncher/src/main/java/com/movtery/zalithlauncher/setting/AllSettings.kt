/*
 * Zalith Launcher 2
 * Copyright (C) 2025 MovTery <movtery228@qq.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */

package com.movtery.zalithlauncher.setting

import android.os.Build
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.movtery.layer_controller.data.POSITION_RANGE
import com.movtery.layer_controller.utils.snap.SnapMode
import com.movtery.zalithlauncher.game.path.GamePathManager
import com.movtery.zalithlauncher.game.version.installed.GraphicsApi
import com.movtery.zalithlauncher.info.InfoDistributor
import com.movtery.zalithlauncher.setting.enums.AppLanguage
import com.movtery.zalithlauncher.setting.enums.DarkMode
import com.movtery.zalithlauncher.setting.enums.GestureActionType
import com.movtery.zalithlauncher.setting.enums.MirrorSourceType
import com.movtery.zalithlauncher.setting.enums.MouseControlMode
import com.movtery.zalithlauncher.ui.control.HotbarRule
import com.movtery.zalithlauncher.ui.control.gamepad.JoystickMode
import com.movtery.zalithlauncher.ui.control.mouse.CENTER_HOTSPOT
import com.movtery.zalithlauncher.ui.control.mouse.CursorHotspot
import com.movtery.zalithlauncher.ui.control.mouse.LEFT_TOP_HOTSPOT
import com.movtery.zalithlauncher.ui.theme.ColorThemeType
import com.movtery.zalithlauncher.utils.animation.TransitionAnimationType

object AllSettings : SettingsRegistry() {
    //Renderer
    /**
     * 全局渲染器
     * Global renderer
     */
    val renderer = stringSetting("renderer", "")

    /**
     * Vulkan 驱动器
     * Vulkan driver
     */
    val vulkanDriver = stringSetting("vulkanDriver", "default turnip")

    /**
     * 图形 API（Minecraft 26.2+）
     * Graphics API (Minecraft 26.2+)
     */
    val graphicsApi = enumSetting("graphicsApi", GraphicsApi.DEFAULT_OPENGL)

    /**
     * 分辨率
     * Resolution ratio
     */
    val resolutionRatio = intSetting("resolutionRatio", 100, 25..300)

    /**
     * 游戏页面全屏化
     * Game screen fullscreen
     */
    val gameFullScreen = boolSetting("gameFullScreen", true)

    /**
     * 使用 SurfaceView 渲染
     * Use SurfaceView for rendering
     */
    val useSurfaceView = boolSetting("useSurfaceView", false)

    /**
     * 持续性能模式
     * Sustained performance mode
     */
    val sustainedPerformance = boolSetting("sustainedPerformance", false)

    /**
     * 使用系统的 Vulkan 驱动
     * Use system Vulkan driver
     */
    val zinkPreferSystemDriver = boolSetting("zinkPreferSystemDriver", false)

    /**
     * Zink 垂直同步
     * Vsync in Zink
     */
    val vsyncInZink = boolSetting("vsyncInZink", false)

    /**
     * 强制在高性能核心运行
     * Force running on high-performance cores
     */
    val bigCoreAffinity = boolSetting("bigCoreAffinity", false)

    /**
     * 启用着色器日志输出
     * Enable shader log output
     */
    val dumpShaders = boolSetting("dumpShaders", false)

    //Game
    /**
     * 版本隔离
     * Version isolation
     */
    val versionIsolation = boolSetting("versionIsolation", true)

    /**
     * 不检查游戏完整性
     * Skip game integrity check
     */
    val skipGameIntegrityCheck = boolSetting("skipGameIntegrityCheck", false)

    /**
     * 版本自定义信息
     * Version custom info
     */
    val versionCustomInfo = stringSetting("versionCustomInfo", "${InfoDistributor.LAUNCHER_IDENTIFIER}[zl_version]")

    /**
     * 启动器的Java环境
     * Launcher Java environment
     */
    val javaRuntime = stringSetting("javaRuntime", "")

    /**
     * 自动选择Java环境
     * Auto pick Java environment
     */
    val autoPickJavaRuntime = boolSetting("autoPickJavaRuntime", true)

    /**
     * 游戏内存分配大小
     * Game memory allocation size
     */
    val ramAllocation = intSetting("ramAllocation", null, min = 256)

    /**
     * 自定义Jvm启动参数
     * Custom JVM startup arguments
     */
    val jvmArgs = stringSetting("jvmArgs", "")

    /**
     * 已禁用的原生库插件列表
     * List of disabled native library plugins
     */
    val disableNativeLibPlugins = stringListSetting("nativeLibPlugins", emptyList())

    /**
     * 启动游戏时自动展示日志，直到游戏开始渲染
     * Automatically show logs when starting the game until it begins rendering
     */
    val showLogAutomatic = boolSetting("showLogAutomatic", false)

    /**
     * 日志字体大小
     * Log font size
     */
    val logTextSize = intSetting("logTextSize", 15, 5..20)

    /**
     * 日志缓冲区刷新时间
     * Log buffer flush interval
     */
    val logBufferFlushInterval = intSetting("logBufferFlushInterval", 200, 100..1000)

    //Control
    /**
     * 实体鼠标控制
     * Physical mouse control
     */
    val physicalMouseMode = boolSetting("physicalMouseMode", true)

    /**
     * 按键键值，按下按键呼出输入法
     * Key value to trigger the input method
     */
    val physicalKeyImeCode = intSetting("physicalKeyImeCode", null)

    /**
     * 隐藏虚拟鼠标
     * Hide virtual mouse
     */
    val hideMouse = boolSetting("hideMouse", false)

    /**
     * 虚拟鼠标大小（Dp）
     * Virtual mouse size (Dp)
     */
    val mouseSize = intSetting("mouseSize", 24, 5..50)

    /**
     * 虚拟鼠标箭头热点坐标
     * Virtual mouse arrow hotspot coordinates
     */
    val arrowMouseHotspot = parcelableSetting("arrowMouseHotspot", LEFT_TOP_HOTSPOT)

    /**
     * 虚拟鼠标链接选择热点坐标
     * Virtual mouse link selection hotspot coordinates
     */
    val linkMouseHotspot = parcelableSetting("linkMouseHotspot", CursorHotspot(xPercent = 23, yPercent = 0))

    /**
     * 虚拟鼠标输入选择热点坐标
     * Virtual mouse text input selection hotspot coordinates
     */
    val iBeamMouseHotspot = parcelableSetting("iBeamMouseHotspot", CENTER_HOTSPOT)

    /**
     * 虚拟鼠标十字热点坐标
     * Virtual mouse crosshair hotspot coordinates
     */
    val crossHairMouseHotspot = parcelableSetting("crossHairMouseHotspot", CENTER_HOTSPOT)

    /**
     * 虚拟鼠标调整大小（上下）热点坐标
     * Virtual mouse resize (North-South) hotspot coordinates
     */
    val resizeNSMouseHotspot = parcelableSetting("resizeNSMouseHotspot", CENTER_HOTSPOT)

    /**
     * 虚拟鼠标调整大小（左右）热点坐标
     * Virtual mouse resize (East-West) hotspot coordinates
     */
    val resizeEWMouseHotspot = parcelableSetting("resizeEWMouseHotspot", CENTER_HOTSPOT)

    /**
     * 虚拟鼠标调整大小（全部方向）热点坐标
     * Virtual mouse resize (All directions) hotspot coordinates
     */
    val resizeAllMouseHotspot = parcelableSetting("resizeAllMouseHotspot", CENTER_HOTSPOT)

    /**
     * 虚拟鼠标禁止/无效操作热点坐标
     * Virtual mouse not allowed/invalid action hotspot coordinates
     */
    val notAllowedMouseHotspot = parcelableSetting("notAllowedMouseHotspot", CENTER_HOTSPOT)

    /**
     * 虚拟鼠标灵敏度
     * Virtual mouse sensitivity
     */
    val cursorSensitivity = intSetting("cursorSensitivity", 100, 25..300)

    /**
     * 被抓获指针移动灵敏度
     * Captured pointer movement sensitivity
     */
    val mouseCaptureSensitivity = intSetting("mouseCaptureSensitivity", 100, 25..300)

    /**
     * 虚拟鼠标控制模式
     * Virtual mouse control mode
     */
    val mouseControlMode = enumSetting("mouseControlMode", MouseControlMode.SLIDE)

    /**
     * 鼠标控制长按延迟
     * Mouse control long press delay
     */
    val mouseLongPressDelay = intSetting("mouseLongPressDelay", 300, 100..1000)

    /**
     * 是否开启虚拟鼠标点击操作
     * Whether to enable virtual mouse click operations
     */
    val enableMouseClick = boolSetting("enableMouseClick", true)

    /**
     * 是否启用手柄控制
     * Whether to enable gamepad control
     */
    val gamepadControl = boolSetting("gamepadControl", true)

    /**
     * 摇杆死区缩放
     * Joystick dead zone scale
     */
    val gamepadDeadZoneScale = intSetting("gamepadDeadZoneScale", 100, 50..200)

    /**
     * 手柄映射配置
     * Gamepad mapping configuration
     */
    val gamepadMappingConfig = stringSetting("gamepadMappingConfig", "default")

    /**
     * 摇杆控制模式
     * Joystick control mode
     */
    val joystickControlMode = enumSetting("joystickControlMode", JoystickMode.LeftMovement)

    /**
     * 手柄摇杆控制鼠标指针时的灵敏度
     * Sensitivity when the gamepad joystick controls the mouse pointer
     */
    val gamepadCursorSensitivity = intSetting("gamepadCursorSensitivity", 100, 25..300)

    /**
     * 手柄摇杆控制游戏视角时的灵敏度
     * Sensitivity when the gamepad joystick controls the game camera
     */
    val gamepadCameraSensitivity = intSetting("gamepadCameraSensitivity", 100, 25..300)

    /**
     * 手势控制
     * Gesture control
     */
    val gestureControl = boolSetting("gestureControl", false)

    /**
     * 手势控制点击时触发的鼠标按钮
     * Mouse button triggered by gesture control tap
     */
    val gestureTapMouseAction = enumSetting("gestureTapMouseAction", GestureActionType.MOUSE_RIGHT)

    /**
     * 手势控制长按时触发的鼠标按钮
     * Mouse button triggered by gesture control long press
     */
    val gestureLongPressMouseAction = enumSetting("gestureLongPressMouseAction", GestureActionType.MOUSE_LEFT)

    /**
     * 手势控制长按延迟
     * Gesture control long press delay
     */
    val gestureLongPressDelay = intSetting("gestureLongPressDelay", 300, 100..1000)

    /**
     * 陀螺仪控制
     * Gyroscope control
     */
    val gyroscopeControl = boolSetting("gyroscopeControl", false)

    /**
     * 陀螺仪控制灵敏度
     * Gyroscope control sensitivity
     */
    val gyroscopeSensitivity = intSetting("gyroscopeSensitivity", 100, 25..300)

    /**
     * 陀螺仪采样率
     * Gyroscope sample rate
     */
    val gyroscopeSampleRate = intSetting("gyroscopeSampleRate", 16, 5..50)

    /**
     * 陀螺仪数值平滑
     * Gyroscope value smoothing
     */
    val gyroscopeSmoothing = boolSetting("gyroscopeSmoothing", true)

    /**
     * 陀螺仪平滑处理的窗口大小
     * Window size for gyroscope smoothing processing
     */
    val gyroscopeSmoothingWindow = intSetting("gyroscopeSmoothingWindow", 4, 2..10)

    /**
     * 反转 X 轴
     * Invert X axis
     */
    val gyroscopeInvertX = boolSetting("gyroscopeInvertX", false)

    /**
     * 反转 Y 轴
     * Invert Y axis
     */
    val gyroscopeInvertY = boolSetting("gyroscopeInvertY", false)

    //Launcher
    /**
     * 颜色主题色
     * Android 12+ 默认动态主题色
     */
    val launcherColorTheme = enumSetting(
        "launcherColorTheme",
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) ColorThemeType.DYNAMIC
        else ColorThemeType.EMBERMIRE
    )

    /**
     * 自定义颜色主题色
     * Custom color theme color
     */
    val launcherCustomColor = intSetting("launcherCustomColor", Color.Blue.toArgb())

    /**
     * 启动器UI深色主题
     * Launcher UI dark mode
     */
    val launcherDarkMode = enumSetting("launcherDarkMode", DarkMode.FollowSystem)

    /**
     * 启动器语言
     * Launcher language
     */
    val launcherLanguage = enumSetting("launcherLanguage", AppLanguage.FOLLOW_SYSTEM)

    /**
     * 启动器部分屏幕全屏
     * Launcher partial screen fullscreen
     */
    val launcherFullScreen = boolSetting("launcherFullScreen", true)

    /**
     * 持续型节日彩蛋效果
     * Persistent festive egg effects
     */
    val launcherFestivalEffects = boolSetting("launcherFestivalEffects", true)

    /**
     * 动画倍速
     * Animation speed multiplier
     */
    val launcherAnimateSpeed = intSetting("launcherAnimateSpeed", 5, 0..10)

    /**
     * 动画幅度
     * Animation amplitude
     */
    val launcherAnimateExtent = intSetting("launcherAnimateExtent", 5, 0..10)

    /**
     * 启动器页面切换动画类型
     * Launcher page transition animation type
     */
    val launcherSwapAnimateType = enumSetting("launcherSwapAnimateType", TransitionAnimationType.JELLY_BOUNCE)

    /**
     * 启动器背景元素不透明度
     * Launcher background element opacity
     */
    val launcherBackgroundOpacity = intSetting("launcherBackgroundOpacity", 80, 20..100)

    /**
     * 启动器视频背景音量
     * Launcher video background volume
     */
    val videoBackgroundVolume = intSetting("videoBackgroundVolume", 0, 0..100)

    /**
     * 启动器上次检查更新时，用户选择忽略的版本号
     * Version number the user chose to ignore the last time the launcher checked for updates
     */
    val lastIgnoredVersion = intSetting("lastIgnoredVersion", null)

    /**
     * 启动器日志保留天数
     * Launcher log retention days
     */
    val launcherLogRetentionDays = intSetting("launcherLogRetentionDays", 7, 1..14)

    /**
     * 下载版本附加内容镜像源类型
     * Mirror source type for downloading version add-on content
     */
    val fetchModLoaderSource = enumSetting("fetchModLoaderSource", MirrorSourceType.OFFICIAL_FIRST)

    /**
     * 文件下载镜像源类型
     * Mirror source type for file downloads
     */
    val fileDownloadSource = enumSetting("fileDownloadSource", MirrorSourceType.OFFICIAL_FIRST)

    /**
     * 资源搜索镜像源类型
     * Mirror source type for asset searches
     */
    val assetSearchSource = enumSetting("assetSearchSource", MirrorSourceType.OFFICIAL_FIRST)

    /**
     * 资源下载镜像源类型
     * Mirror source type for asset downloads
     */
    val assetDownloadSource = enumSetting("assetDownloadSource", MirrorSourceType.OFFICIAL_FIRST)

    //Control
    /**
     * 全局默认控制布局文件名
     * Global default control layout filename
     */
    val controlLayout = stringSetting("controlLayout", "")

    //Other
    /**
     * 当前选择的账号
     * Currently selected account
     */
    val currentAccount = stringSetting("currentAccount", "")

    /**
     * 当前选择的游戏目录id
     * Currently selected game directory ID
     */
    val currentGamePathId = stringSetting("currentGamePathId", GamePathManager.DEFAULT_ID)

    /**
     * 启动器任务菜单是否展开
     * Whether the launcher task menu is expanded
     */
    val launcherTaskMenuExpanded = boolSetting("launcherTaskMenuExpanded", true)

    /**
     * 在游戏菜单悬浮窗上显示帧率
     * Show FPS on the game menu floating window
     */
    val showFPS = boolSetting("showFPS", true)

    /**
     * 在游戏菜单悬浮窗上显示内存
     * Show memory usage on the game menu floating window
     */
    val showMemory = boolSetting("showMemory", false)

    /**
     * 在游戏画面上展示菜单悬浮窗
     * Show menu floating ball on the game screen
     */
    val showMenuBall = boolSetting("showMenuBall", true)

    /**
     * 游戏菜单悬浮窗位置
     * Game menu floating ball position
     */
    val menuBallPos = offsetSetting("menuBallPos", Offset.Zero)

    /**
     * 游戏菜单悬浮窗不透明度
     * Game menu floating ball opacity
     */
    val menuBallOpacity = intSetting("menuBallOpacity", 100, 20..100)

    /**
     * 快捷栏判定箱计算规则
     * Hotbar bounding box calculation rule
     */
    val hotbarRule = enumSetting("hotbarRule", HotbarRule.Auto)

    /**
     * 快捷栏宽度百分比
     * Hotbar width percentage
     */
    val hotbarWidth = intSetting("hotbarWidth", 500, 0..1000)

    /**
     * 快捷栏高度百分比
     * Hotbar height percentage
     */
    val hotbarHeight = intSetting("hotbarHeight", 100, 0..1000)

    /**
     * 游戏内控制布局的整体不透明度
     * Overall opacity of the in-game control layout
     */
    val controlsOpacity = intSetting("controlsOpacity", 100, 0..100)

    /**
     * 控制布局编辑器：是否开启控件吸附功能
     * Control layout editor: Whether to enable widget snapping
     */
    val editorEnableWidgetSnap = boolSetting("editorEnableWidgetSnap", true)

    /**
     * 控制布局编辑器：是否在所有控件层范围内吸附
     * Control layout editor: Whether to snap across all widget layers
     */
    val editorSnapInAllLayers = boolSetting("editorSnapInAllLayers", false)

    /**
     * 控制布局编辑器：控件吸附模式
     * Control layout editor: Widget snapping mode
     */
    val editorWidgetSnapMode = enumSetting("editorWidgetSnapMode", SnapMode.FullScreen)

    /**
     * 是否启用陶瓦联机
     * Whether to enable Terracotta multiplayer
     */
    val enableTerracotta = boolSetting("enableTerracotta", false)

    /**
     * 陶瓦联机公告版本号
     * Terracotta multiplayer announcement version number
     */
    val terracottaNoticeVer = intSetting("terracottaNoticeVer", -1)

    //特殊样式
    // Special styles
    /**
     * 是否在游戏中启用摇杆移动组件
     * Whether to enable the joystick movement component in-game
     */
    val enableJoystickControl = boolSetting("enableJoystickControl", false)

    /**
     * 游戏中摇杆移动组件的 X 坐标 0~10000
     * X coordinate of the joystick movement component in-game (0~10000)
     */
    val joystickControlX = intSetting("joystickControlX", 5000, POSITION_RANGE)

    /**
     * 游戏中摇杆移动组件的 Y 坐标 0~10000
     * Y coordinate of the joystick movement component in-game (0~10000)
     */
    val joystickControlY = intSetting("joystickControlY", 5000, POSITION_RANGE)

    /**
     * 游戏中摇杆移动组件的大小 Dp
     * Size of the joystick movement component in-game (Dp)
     */
    val joystickControlSize = intSetting("joystickControlSize", 120, 80..180)

    /**
     * 游戏中摇杆移动组件是否使用控制布局提供的样式
     * Whether the joystick movement component in-game uses the style provided by the control layout
     */
    val joystickUseStyleByLayout = boolSetting("joystickUseStyleByLayout", true)

    /**
     * 游戏中摇杆移动组件是否在使用实体鼠标时隐藏
     * Whether the joystick movement component in-game is hidden when using a physical mouse
     */
    val joystickHideWhenMouse = boolSetting("joystickHideWhenMouse", true)

    /**
     * 游戏中摇杆移动组件是否在使用手柄时隐藏
     * Whether the joystick movement component in-game is hidden when using a gamepad
     */
    val joystickHideWhenGamepad = boolSetting("joystickHideWhenGamepad", true)

    /**
     * 摇杆组件死区缩放
     * Joystick component dead zone scale
     */
    val joystickDeadZoneRatio = intSetting("joystickDeadZoneRatio", 30, 10..50)

    /**
     * 摇杆前进锁判定范围
     * Joystick forward lock detection range
     */
    val joystickLockThreshold = intSetting("joystickLockThreshold", 30, 5..100)

    /**
     * 游戏中摇杆移动组件是否可锁定
     * Whether the joystick movement component in-game can be locked
     */
    val joystickControlCanLock = boolSetting("joystickControlCanLock", true)

    /**
     * 游戏中摇杆移动组件在前进锁定时，是否强制疾跑
     * Whether the joystick movement component in-game forces sprinting when forward-locked
     */
    val joystickControlLockSpring = boolSetting("joystickControlLockSpring", true)

    /**
     * 上次检查更新的时间戳
     * Timestamp of the last update check
     */
    val lastUpgradeCheck = longSetting("lastUpgradeCheck", 0L)

    /**
     * 玩家结束运行游戏的次数
     * Number of times the player has finished running the game
     */
    val finishedGame = intSetting("finishedGame", 0)

    /**
     * 是否在打开启动器时，根据特定的运行游戏次数，显示赞助支持弹窗
     * Whether to show a sponsorship support dialog when opening the launcher based on the number of times the game has been run
     */
    val showSponsorship = boolSetting("showSponsorship", true)
}