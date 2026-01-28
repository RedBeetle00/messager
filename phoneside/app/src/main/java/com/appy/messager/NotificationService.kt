package com.appy.messager

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.app.Notification
import kotlinx.coroutines.*
import kotlinx.coroutines.launch

class NotificationService : NotificationListenerService() {
    private lateinit var tcpClient: TcpClient
    private val serviceJob = Job()
    private val scope = CoroutineScope(Dispatchers.Main + serviceJob)

    // Список системных пакетов для игнорирования
    val systemPackages = listOf(
        // "android",      // Системные уведомления Android
        "com.android.systemui", // Панель уведомлений
        // "com.android.settings", // Настройки
        // "com.google.android.apps.messaging" // Если хотите исключить конкретное приложение
    )

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        tcpClient = TcpClient()
        if (systemPackages.contains(sbn.packageName)) {
            return // Пропускаем системные уведомления
        }

        scope.launch {
            // Получаем уведомление
            val notification = sbn.notification

            // Извлекаем данные
            val title = notification.extras.getString(Notification.EXTRA_TITLE)
            val text = notification.extras.getString(Notification.EXTRA_TEXT)

            if (tcpClient.connect("192.168.1.11", 8080)) {
                // Отправляем данные на ПК
                tcpClient.sendMessage("$title:\n$text\n")

                // Отключаемся
                tcpClient.disconnect()
            }
        }
    }

    override fun onDestroy() {
        serviceJob.cancel()
        super.onDestroy()
    }
}