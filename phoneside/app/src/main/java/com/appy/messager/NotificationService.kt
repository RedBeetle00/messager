package com.appy.messager

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.app.Notification
import kotlinx.coroutines.*
import kotlinx.coroutines.launch

class NotificationService : NotificationListenerService() {
    private lateinit var tcpClient: TcpClient
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        tcpClient = TcpClient()
        scope.launch {
            tcpClient.connect("192.168.1.11", 8080)
            val scope = CoroutineScope(Dispatchers.Main + Job())

            // Получаем уведомление
            val packageName = sbn.packageName
            val notification = sbn.notification

            // Извлекаем данные
            val title = notification.extras.getString(Notification.EXTRA_TITLE)
            val text = notification.extras.getString(Notification.EXTRA_TEXT)
            val subText = notification.extras.getString(Notification.EXTRA_SUB_TEXT)

            // Отправляем данные на ПК
            tcpClient.sendMessage(text.toString())

            tcpClient.disconnect()
        }
    }
}