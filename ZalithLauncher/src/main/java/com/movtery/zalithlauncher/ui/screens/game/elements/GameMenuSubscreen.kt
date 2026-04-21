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

package com.movtery.zalithlauncher.ui.screens.game.elements

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Mouse
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.movtery.zalithlauncher.R
import com.movtery.zalithlauncher.setting.AllSettings
import com.movtery.zalithlauncher.setting.enums.GestureActionType
import com.movtery.zalithlauncher.setting.enums.MouseControlMode
import com.movtery.zalithlauncher.setting.unit.floatRange
import com.movtery.zalithlauncher.ui.components.DualMenuSubscreen
import com.movtery.zalithlauncher.ui.components.MenuListLayout
import com.movtery.zalithlauncher.ui.components.MenuSliderLayout
import com.movtery.zalithlauncher.ui.components.MenuState
import com.movtery.zalithlauncher.ui.components.MenuSwitchButton
import com.movtery.zalithlauncher.ui.components.MenuTextButton
import com.movtery.zalithlauncher.ui.control.HotbarRule
import com.movtery.zalithlauncher.ui.control.gyroscope.isGyroscopeAvailable
import com.movtery.zalithlauncher.ui.theme.cardTitleColor
import com.movtery.zalithlauncher.viewmodel.GamepadViewModel

private sealed interface IconTab {
    data class ImageVectorTab(val icon: ImageVector, val iconSize: Dp = 18.dp): IconTab
    data class PainterTab(val iconRes: Int, val iconSize: Dp = 18.dp): IconTab
}

private val controlTabs = listOf(
    //概览
    IconTab.ImageVectorTab(Icons.Outlined.Dashboard),
    //虚拟鼠标设置
    IconTab.ImageVectorTab(Icons.Outlined.Mouse, iconSize = 16.dp),
    //手柄设置
    IconTab.ImageVectorTab(Icons.Outlined.SportsEsports),
    //手势控制设置
    IconTab.ImageVectorTab(Icons.Outlined.TouchApp),
    //陀螺仪设置
    IconTab.PainterTab(R.drawable.ic_mobile_rotate)
)

@Composable
fun GameMenuSubscreen(
    state: MenuState,
    controlMenuTabIndex: Int,
    onControlMenuTabChange: (Int) -> Unit,
    gamepadViewModel: GamepadViewModel,
    closeScreen: () -> Unit,
    onForceClose: () -> Unit,
    onSwitchLog: () -> Unit,
    enableTerracotta: Boolean,
    onOpenTerracottaMenu: () -> Unit,
    onRefreshWindowSize: () -> Unit,
    onInputMethod: () -> Unit,
    onSendKeycode: () -> Unit,
    onReplacementControl: () -> Unit,
    onManageJoystick: () -> Unit,
    onEditLayout: () -> Unit
) {
    //检查陀螺仪是否可用
    val context = LocalContext.current

    DualMenuSubscreen(
        state = state,
        closeScreen = closeScreen,
        leftMenuContent = {
            val pagerState = rememberPagerState(pageCount = { controlTabs.size })

            LaunchedEffect(controlMenuTabIndex) {
                pagerState.animateScrollToPage(controlMenuTabIndex)
            }

            Column {
                //顶贴标签栏
                SecondaryScrollableTabRow(
                    selectedTabIndex = controlMenuTabIndex,
                    edgePadding = 0.dp,
                    minTabWidth = 58.dp,
                    containerColor = cardTitleColor(),
                ) {
                    controlTabs.forEachIndexed { index, iconTab ->
                        Tab(
                            selected = index == controlMenuTabIndex,
                            onClick = {
                                onControlMenuTabChange(index)
                            },
                            icon = {
                                when (iconTab) {
                                    is IconTab.ImageVectorTab -> {
                                        Icon(
                                            modifier = Modifier.size(iconTab.iconSize),
                                            imageVector = iconTab.icon,
                                            contentDescription = null
                                        )
                                    }
                                    is IconTab.PainterTab -> {
                                        Icon(
                                            modifier = Modifier.size(iconTab.iconSize),
                                            painter = painterResource(iconTab.iconRes),
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                ) { page ->
                    when (page) {
                        0 -> {
                            ControlOverview(
                                modifier = Modifier.fillMaxSize(),
                                closeScreen = closeScreen,
                                onInputMethod = onInputMethod,
                                onSendKeycode = onSendKeycode,
                                onReplacementControl = onReplacementControl,
                                onManageJoystick = onManageJoystick,
                                onEditLayout = onEditLayout
                            )
                        }
                        1 -> ControlMouse(modifier = Modifier.fillMaxSize())
                        2 -> ControlGamepad(
                            modifier = Modifier.fillMaxSize(),
                            gamepadViewModel = gamepadViewModel
                        )
                        3 -> ControlGesture(modifier = Modifier.fillMaxSize())
                        4 -> ControlGyroscope(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        },
        rightMenuTitle = {
            Text(
                modifier = Modifier.padding(all = 8.dp),
                text = stringResource(R.string.game_menu_title),
                style = MaterialTheme.typography.titleMedium
            )
        },
        rightMenuContent = {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(all = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                //强制关闭
                item {
                    MenuTextButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.game_button_force_close),
                        onClick = onForceClose
                    )
                }
                //日志输出
                item {
                    MenuTextButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.game_menu_option_switch_log),
                        onClick = onSwitchLog
                    )
                }

                //如果开启多人联机，则展示这个按钮
                if (enableTerracotta) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    //打开联机菜单
                    item {
                        MenuTextButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.terracotta_menu),
                            onClick = onOpenTerracottaMenu
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                //开启菜单悬浮窗
                item {
                    MenuSwitchButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.game_menu_option_show_menu),
                        switch = AllSettings.showMenuBall.state,
                        onSwitch = { value ->
                            AllSettings.showMenuBall.save(value)
                            if (!value) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.game_menu_option_show_menu_hided),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    )
                }
                //菜单悬浮窗不透明度
                item {
                    MenuSliderLayout(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(R.string.game_menu_option_menu_ball_opacity),
                        value = AllSettings.menuBallOpacity.state,
                        valueRange = AllSettings.menuBallOpacity.floatRange,
                        onValueChange = { value ->
                            AllSettings.menuBallOpacity.updateState(value)
                        },
                        onValueChangeFinished = { value ->
                            AllSettings.menuBallOpacity.save(value)
                        },
                        suffix = "%",
                        enabled = AllSettings.showMenuBall.state
                    )
                }
                //帧率显示
                item {
                    MenuSwitchButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.game_menu_option_switch_fps),
                        switch = AllSettings.showFPS.state,
                        onSwitch = { AllSettings.showFPS.save(it) },
                        enabled = AllSettings.showMenuBall.state
                    )
                }
                //内存显示
                item {
                    MenuSwitchButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.game_menu_option_switch_memory),
                        switch = AllSettings.showMemory.state,
                        onSwitch = { AllSettings.showMemory.save(it) },
                        enabled = AllSettings.showMenuBall.state
                    )
                }
                //游戏窗口分辨率
                item {
                    MenuSliderLayout(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(R.string.settings_renderer_resolution_scale_title),
                        value = AllSettings.resolutionRatio.state,
                        valueRange = AllSettings.resolutionRatio.floatRange,
                        onValueChange = { value ->
                            AllSettings.resolutionRatio.updateState(value)
//                        onRefreshWindowSize()
                        },
                        onValueChangeFinished = { value ->
                            AllSettings.resolutionRatio.save(value)
                            onRefreshWindowSize()
                        },
                        suffix = "%",
                    )
                }
            }
        }
    )
}

@Composable
private fun ControlOverview(
    modifier: Modifier = Modifier,
    closeScreen: () -> Unit,
    onInputMethod: () -> Unit,
    onSendKeycode: () -> Unit,
    onReplacementControl: () -> Unit,
    onManageJoystick: () -> Unit,
    onEditLayout: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        //切换输入法
        item {
            MenuTextButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.game_menu_option_input_method),
                onClick = {
                    onInputMethod()
                    closeScreen()
                }
            )
        }
        //发送键值
        item {
            MenuTextButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.game_menu_option_send_keycode),
                onClick = {
                    onSendKeycode()
                    closeScreen()
                }
            )
        }
        //更换控制布局
        item {
            MenuTextButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.game_menu_option_replacement_control),
                onClick = {
                    onReplacementControl()
                    closeScreen()
                }
            )
        }
        //编辑布局
        item {
            MenuTextButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.control_manage_info_edit),
                onClick = {
                    onEditLayout()
                    closeScreen()
                }
            )
        }
        //控制布局不透明度
        item {
            MenuSliderLayout(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.game_menu_option_controls_opacity),
                value = AllSettings.controlsOpacity.state,
                valueRange = AllSettings.controlsOpacity.floatRange,
                onValueChange = { AllSettings.controlsOpacity.updateState(it) },
                onValueChangeFinished = { AllSettings.controlsOpacity.save(it) },
                suffix = "%"
            )
        }

        //管理摇杆
        item {
            MenuTextButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.game_styles_joystick),
                onClick = {
                    onManageJoystick()
                    closeScreen()
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        //快捷栏定位规则
        item {
            MenuListLayout(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.game_menu_option_hotbar_rule),
                items = HotbarRule.entries,
                currentItem = AllSettings.hotbarRule.state,
                onItemChange = { AllSettings.hotbarRule.save(it) },
                getItemText = { stringResource(it.nameRes) }
            )
        }

        //快捷栏宽度
        item {
            MenuSliderLayout(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.game_menu_option_hotbar_width),
                value = AllSettings.hotbarWidth.state / 10f,
                valueRange = 0f..100f,
                enabled = AllSettings.hotbarRule.state == HotbarRule.Custom,
                onValueChange = { value ->
                    AllSettings.hotbarWidth.updateState((value * 10f).toInt())
                },
                onValueChangeFinished = { value ->
                    AllSettings.hotbarWidth.save((value * 10f).toInt())
                },
                suffix = "%",
            )
        }

        //快捷栏高度
        item {
            MenuSliderLayout(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.game_menu_option_hotbar_height),
                value = AllSettings.hotbarHeight.state / 10f,
                valueRange = 0f..100f,
                enabled = AllSettings.hotbarRule.state == HotbarRule.Custom,
                onValueChange = { value ->
                    AllSettings.hotbarHeight.updateState((value * 10f).toInt())
                },
                onValueChangeFinished = { value ->
                    AllSettings.hotbarHeight.save((value * 10f).toInt())
                },
                suffix = "%",
            )
        }
    }
}

@Composable
private fun ControlMouse(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        //隐藏虚拟鼠标
        item {
            MenuSwitchButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.settings_control_mouse_hide_title),
                switch = AllSettings.hideMouse.state,
                onSwitch = { AllSettings.hideMouse.save(it) },
                enabled = AllSettings.mouseControlMode.state == MouseControlMode.CLICK
            )
        }
        //触控板式操作
        item {
            MenuSwitchButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.settings_control_mouse_enable_click_title),
                switch = AllSettings.enableMouseClick.state,
                onSwitch = { AllSettings.enableMouseClick.save(it) },
                enabled = AllSettings.mouseControlMode.state == MouseControlMode.SLIDE
            )
        }
        //鼠标控制模式
        item {
            MenuListLayout(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.settings_control_mouse_control_mode_title),
                items = MouseControlMode.entries,
                currentItem = AllSettings.mouseControlMode.state,
                onItemChange = { AllSettings.mouseControlMode.save(it) },
                getItemText = { stringResource(it.nameRes) }
            )
        }
        //虚拟鼠标大小
        item {
            MenuSliderLayout(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.settings_control_mouse_size_title),
                value = AllSettings.mouseSize.state,
                valueRange = AllSettings.mouseSize.floatRange,
                onValueChange = { AllSettings.mouseSize.updateState(it) },
                onValueChangeFinished = { AllSettings.mouseSize.save(it) },
                suffix = "Dp"
            )
        }
        //虚拟鼠标灵敏度
        item {
            MenuSliderLayout(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.settings_control_mouse_sensitivity_title),
                value = AllSettings.cursorSensitivity.state,
                valueRange = AllSettings.cursorSensitivity.floatRange,
                onValueChange = { AllSettings.cursorSensitivity.updateState(it) },
                onValueChangeFinished = { AllSettings.cursorSensitivity.save(it) },
                suffix = "%"
            )
        }
        //抓获鼠标滑动灵敏度
        item {
            MenuSliderLayout(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.settings_control_mouse_capture_sensitivity_title),
                value = AllSettings.mouseCaptureSensitivity.state,
                valueRange = AllSettings.mouseCaptureSensitivity.floatRange,
                onValueChange = { AllSettings.mouseCaptureSensitivity.updateState(it) },
                onValueChangeFinished = { AllSettings.mouseCaptureSensitivity.save(it) },
                suffix = "%"
            )
        }
        //虚拟鼠标长按触发的延迟
        item {
            MenuSliderLayout(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.settings_control_mouse_long_press_delay_title),
                value = AllSettings.mouseLongPressDelay.state,
                valueRange = AllSettings.mouseLongPressDelay.floatRange,
                onValueChange = { AllSettings.mouseLongPressDelay.updateState(it) },
                onValueChangeFinished = { AllSettings.mouseLongPressDelay.save(it) },
                suffix = "ms"
            )
        }
    }
}

@Composable
private fun ControlGamepad(
    gamepadViewModel: GamepadViewModel,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        //手柄控制总开关
        item {
            MenuSwitchButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.settings_gamepad_title),
                switch = AllSettings.gamepadControl.state,
                onSwitch = { AllSettings.gamepadControl.save(it) }
            )
        }

        //手柄死区缩放
        item {
            MenuSliderLayout(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.settings_gamepad_deadzone_title),
                value = AllSettings.gamepadDeadZoneScale.state,
                valueRange = AllSettings.gamepadDeadZoneScale.floatRange,
                enabled = AllSettings.gamepadControl.state,
                onValueChange = { AllSettings.gamepadDeadZoneScale.updateState(it) },
                onValueChangeFinished = { AllSettings.gamepadDeadZoneScale.save(it) },
                suffix = "%"
            )
        }

        //摇杆指针灵敏度
        item {
            MenuSliderLayout(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.settings_gamepad_cursor_sensitivity_title),
                value = AllSettings.gamepadCursorSensitivity.state,
                valueRange = AllSettings.gamepadCursorSensitivity.floatRange,
                enabled = AllSettings.gamepadControl.state,
                onValueChange = { AllSettings.gamepadCursorSensitivity.updateState(it) },
                onValueChangeFinished = { AllSettings.gamepadCursorSensitivity.save(it) },
                suffix = "%"
            )
        }

        //摇杆视角灵敏度
        item {
            MenuSliderLayout(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.settings_gamepad_camera_sensitivity_title),
                value = AllSettings.gamepadCameraSensitivity.state,
                valueRange = AllSettings.gamepadCameraSensitivity.floatRange,
                enabled = AllSettings.gamepadControl.state,
                onValueChange = { AllSettings.gamepadCameraSensitivity.updateState(it) },
                onValueChangeFinished = { AllSettings.gamepadCameraSensitivity.save(it) },
                suffix = "%"
            )
        }

        //手柄映射配置切换
        item {
            val list = remember(gamepadViewModel) {
                gamepadViewModel.getAllConfigKeys()
            }

            if (list.isNotEmpty()) {
                MenuListLayout(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.settings_gamepad_config_title),
                    items = list,
                    currentItem = AllSettings.gamepadMappingConfig.state,
                    onItemChange = { name ->
                        AllSettings.gamepadMappingConfig.save(name)
                        gamepadViewModel.reloadAllMappings()
                    },
                    getItemText = { it },
                    enabled = AllSettings.gamepadControl.state,
                )
            } else {
                MenuTextButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.settings_gamepad_config_no_items),
                    onClick = {},
                    enabled = false
                )
            }
        }
    }
}

@Composable
private fun ControlGesture(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        //手势控制
        item {
            MenuSwitchButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.settings_control_gesture_control_title),
                switch = AllSettings.gestureControl.state,
                onSwitch = { AllSettings.gestureControl.save(it) }
            )
        }

        //点击触发的操作类型
        item {
            MenuListLayout(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.settings_control_gesture_tap_action_title),
                items = GestureActionType.entries,
                currentItem = AllSettings.gestureTapMouseAction.state,
                onItemChange = { AllSettings.gestureTapMouseAction.save(it) },
                getItemText = { stringResource(it.nameRes) },
                enabled = AllSettings.gestureControl.state
            )
        }

        //长按触发的操作类型
        item {
            MenuListLayout(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.settings_control_gesture_long_press_action_title),
                items = GestureActionType.entries,
                currentItem = AllSettings.gestureLongPressMouseAction.state,
                onItemChange = { AllSettings.gestureLongPressMouseAction.save(it) },
                getItemText = { stringResource(it.nameRes) },
                enabled = AllSettings.gestureControl.state
            )
        }

        //手势长按触发的延迟
        item {
            MenuSliderLayout(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.settings_control_gesture_long_press_delay_title),
                value = AllSettings.gestureLongPressDelay.state,
                valueRange = AllSettings.gestureLongPressDelay.floatRange,
                enabled = AllSettings.gestureControl.state,
                onValueChange = { AllSettings.gestureLongPressDelay.updateState(it) },
                onValueChangeFinished = { AllSettings.gestureLongPressDelay.save(it) },
                suffix = "ms"
            )
        }
    }
}

@Composable
private fun ControlGyroscope(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isGyroscopeAvailable = remember(context) {
        isGyroscopeAvailable(context = context)
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        //陀螺仪控制
        item {
            MenuSwitchButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.settings_control_gyroscope_title),
                switch = AllSettings.gyroscopeControl.state,
                onSwitch = { AllSettings.gyroscopeControl.save(it) },
                enabled = isGyroscopeAvailable
            )
        }

        //陀螺仪控制灵敏度
        item {
            MenuSliderLayout(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.settings_control_gyroscope_sensitivity_title),
                value = AllSettings.gyroscopeSensitivity.state,
                valueRange = AllSettings.gyroscopeSensitivity.floatRange,
                enabled = isGyroscopeAvailable && AllSettings.gyroscopeControl.state,
                onValueChange = { AllSettings.gyroscopeSensitivity.updateState(it) },
                onValueChangeFinished = { AllSettings.gyroscopeSensitivity.save(it) },
                suffix = "%"
            )
        }

        //陀螺仪采样率
        item {
            MenuSliderLayout(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.settings_control_gyroscope_sample_rate_title),
                value = AllSettings.gyroscopeSampleRate.state,
                valueRange = AllSettings.gyroscopeSampleRate.floatRange,
                enabled = isGyroscopeAvailable && AllSettings.gyroscopeControl.state,
                onValueChange = { AllSettings.gyroscopeSampleRate.updateState(it) },
                onValueChangeFinished = { AllSettings.gyroscopeSampleRate.save(it) },
                suffix = "ms"
            )
        }

        //陀螺仪数值平滑
        item {
            MenuSwitchButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.settings_control_gyroscope_smoothing_title),
                switch = AllSettings.gyroscopeSmoothing.state,
                onSwitch = { AllSettings.gyroscopeSmoothing.save(it) },
                enabled = isGyroscopeAvailable && AllSettings.gyroscopeControl.state
            )
        }

        //陀螺仪平滑处理的窗口大小
        item {
            MenuSliderLayout(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.settings_control_gyroscope_smoothing_window_title),
                value = AllSettings.gyroscopeSmoothingWindow.state,
                valueRange = AllSettings.gyroscopeSmoothingWindow.floatRange,
                enabled = isGyroscopeAvailable && AllSettings.gyroscopeControl.state && AllSettings.gyroscopeSmoothing.state,
                onValueChange = { AllSettings.gyroscopeSmoothingWindow.updateState(it) },
                onValueChangeFinished = { AllSettings.gyroscopeSmoothingWindow.save(it) },
            )
        }

        //反转 X 轴
        item {
            MenuSwitchButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.settings_control_gyroscope_invert_x_title),
                switch = AllSettings.gyroscopeInvertX.state,
                onSwitch = { AllSettings.gyroscopeInvertX.save(it) },
                enabled = isGyroscopeAvailable && AllSettings.gyroscopeControl.state
            )
        }

        //反转 Y 轴
        item {
            MenuSwitchButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.settings_control_gyroscope_invert_y_title),
                switch = AllSettings.gyroscopeInvertY.state,
                onSwitch = { AllSettings.gyroscopeInvertY.save(it) },
                enabled = isGyroscopeAvailable && AllSettings.gyroscopeControl.state
            )
        }
    }
}