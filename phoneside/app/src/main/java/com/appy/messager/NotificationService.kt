package com.appy.messager

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.app.Notification
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.launch

class NotificationService : NotificationListenerService() {
    private lateinit var tcpClient: TcpClient

    override fun onListenerConnected() {
        // Вызывается, когда служба успешно подключена к системе
        Log.d("NotificationListener", "Service connected")
    }

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

        println("Message must be sented")

        scope.launch {
            // Отправляем данные на ПК
            tcpClient.sendMessage(text.toString())
        }
    }
}