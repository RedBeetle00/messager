package com.appy.messager

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.app.Notification
import kotlinx.coroutines.*
import kotlinx.coroutines.launch

class NotificationService : NotificationListenerService() {
    private lateinit var tcpClient: TcpClient

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        tcpClient = TcpClient()
        val scope = CoroutineScope(Dispatchers.Main + Job())

        // Получаем уведомление
        val packageName = sbn.packageName
        val notification = sbn.notification

        // Извлекаем данные
        val title = notification.extras.getString(Notification.EXTRA_TITLE)
        val text = notification.extras.getString(Notification.EXTRA_TEXT)
        val subText = notification.extras.getString(Notification.EXTRA_SUB_TEXT)

        scope.launch {
            // Отправляем данные на ПК
            tcpClient.sendMessage(message = text.toString())
        }
    }
}