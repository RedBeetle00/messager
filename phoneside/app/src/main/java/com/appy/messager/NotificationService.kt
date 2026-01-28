package com.appy.messager

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.app.Notification
import kotlinx.coroutines.*
import kotlinx.coroutines.launch

class NotificationService : NotificationListenerService() {


    private lateinit var tcpClient: TcpClient
    private val serviceJob = Job()
    private val scope = CoroutineScope(Dispatchers.IO + serviceJob)

    // Список пакетов для игнорирования
    private val systemPackages = setOf(
        "android",
        "com.android.systemui"
    )

    override fun onCreate() {
        super.onCreate()
        tcpClient = TcpClient()
    }

    companion object {
        @Volatile
        var enabled = false
    }

    override fun onListenerConnected() {
        enabled = true
        scope.launch {
            tcpClient.connect("192.168.1.11", 8080)
        }
    }

    override fun onListenerDisconnected() {
        enabled = false
        scope.launch {
            tcpClient.disconnect()
        }
    }

    // Как только приходит уведомление
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (systemPackages.contains(sbn.packageName)) {
            return // Пропускаем системные уведомления
        }
        scope.launch {
            // Получаем уведомление
            val notification = sbn.notification

            // Извлекаем данные
            val extras = notification.extras
            val title = extras.getString(Notification.EXTRA_TITLE).orEmpty()

            val text = extras.getCharSequence(Notification.EXTRA_BIG_TEXT)
                ?: extras.getCharSequence(Notification.EXTRA_TEXT)
                ?: extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES)?.joinToString("\n")
                ?: ""

            // Отправляем данные на ПК
            tcpClient.sendMessage("$title:\n$text\n")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        runBlocking {
            onListenerDisconnected()
        }
        serviceJob.cancel()
    }
}