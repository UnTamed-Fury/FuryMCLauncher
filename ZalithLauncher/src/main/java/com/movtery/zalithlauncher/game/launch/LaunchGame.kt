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

package com.movtery.zalithlauncher.game.launch

import android.content.Context
import com.movtery.zalithlauncher.R
import com.movtery.zalithlauncher.coroutine.TaskSystem
import com.movtery.zalithlauncher.game.account.AccountsManager
import com.movtery.zalithlauncher.game.account.auth_server.ResponseException
import com.movtery.zalithlauncher.game.account.microsoft.MinecraftProfileException
import com.movtery.zalithlauncher.game.account.microsoft.NotPurchasedMinecraftException
import com.movtery.zalithlauncher.game.account.microsoft.XboxLoginException
import com.movtery.zalithlauncher.game.account.microsoft.toLocal
import com.movtery.zalithlauncher.game.version.download.DownloadMode
import com.movtery.zalithlauncher.game.version.download.MinecraftDownloader
import com.movtery.zalithlauncher.game.version.installed.Version
import com.movtery.zalithlauncher.game.version.installed.VersionFolders
import com.movtery.zalithlauncher.game.version.mod.AllModReader
import com.movtery.zalithlauncher.ui.activities.runGame
import com.movtery.zalithlauncher.utils.logging.Logger.lError
import com.movtery.zalithlauncher.utils.network.isNetworkAvailable
import com.movtery.zalithlauncher.viewmodel.ErrorViewModel
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.HttpStatusCode
import java.net.ConnectException
import java.net.UnknownHostException
import java.nio.channels.UnresolvedAddressException

object LaunchGame {
    var isLaunching: Boolean = false
        private set

    fun launchGame(
        context: Context,
        version: Version,
        exitActivity: () -> Unit,
        submitError: (ErrorViewModel.ThrowableMessage) -> Unit
    ) {
        if (isLaunching) return

        val account = AccountsManager.getCurrentAccount() ?: return

        isLaunching = true

        val downloadTask = MinecraftDownloader(
            context = context,
            version = version.getVersionInfo()?.minecraftVersion ?: version.getVersionName(),
            customName = version.getVersionName(),
            verifyIntegrity = !version.skipGameIntegrityCheck(),
            mode = DownloadMode.VERIFY_AND_REPAIR,
            onCompletion = {
                val modsDir = VersionFolders.MOD.getDir(version.getGameDir())
                val reader = AllModReader(modsDir)
                for (mod in reader.readAllLocals()) {
                    if (mod.id == "touchcontroller") {
                        //安装TouchController后，自动开启代理
                        version.enableTouchProxy = true
                        break
                    }
                }

                runGame(context, version)
                exitActivity()
            },
            onError = { message ->
                submitError(
                    ErrorViewModel.ThrowableMessage(
                        title = context.getString(R.string.minecraft_download_failed),
                        message = message
                    )
                )
            }
        ).getDownloadTask()

        fun runDownloadTask() {
            TaskSystem.submitTask(downloadTask) { isLaunching = false }
        }

        //刷新条件：已联网且令牌过期
        //认证服务器账号的expiresAt为0，能够保证每次都刷新账号
        val loginTask = if (isNetworkAvailable(context) && System.currentTimeMillis() > account.expiresAt - 5 * 60 * 1000) {
            AccountsManager.performLoginTask(
                context = context,
                account = account,
                onSuccess = { acc, _ ->
                    AccountsManager.suspendSaveAccount(acc)
                },
                onFailed = { error ->
                    val message: String = when (error) {
                        is NotPurchasedMinecraftException -> toLocal(context)
                        is MinecraftProfileException -> error.toLocal(context)
                        is XboxLoginException -> error.toLocal(context)
                        is ResponseException -> error.responseMessage
                        is HttpRequestTimeoutException -> context.getString(R.string.error_timeout)
                        is UnknownHostException, is UnresolvedAddressException -> context.getString(R.string.error_network_unreachable)
                        is ConnectException -> context.getString(R.string.error_connection_failed)
                        is io.ktor.client.plugins.ResponseException -> {
                            val statusCode = error.response.status
                            val res = when (statusCode) {
                                HttpStatusCode.Unauthorized -> R.string.error_unauthorized
                                HttpStatusCode.NotFound -> R.string.error_notfound
                                else -> R.string.error_client_error
                            }
                            context.getString(res, statusCode)
                        }
                        else -> {
                            lError("An unknown exception was caught!", error)
                            val errorMessage = error.localizedMessage ?: error.message ?: error::class.qualifiedName ?: "Unknown error"
                            context.getString(R.string.error_unknown, errorMessage)
                        }
                    }

                    submitError(
                        ErrorViewModel.ThrowableMessage(
                            title = context.getString(R.string.account_logging_in_failed),
                            message = message
                        )
                    )
                },
                onFinally = { runDownloadTask() }
            )
        } else {
            null
        }

        loginTask?.let { task ->
            TaskSystem.submitTask(task)
        } ?: run {
            version.offlineAccountLogin = true
            runDownloadTask()
        }
    }
}
