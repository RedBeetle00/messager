package com.appy.messager

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.app.Notification
import kotlinx.coroutines.*

class NotificationService : NotificationListenerService() {
    private lateinit var tcpClient: TcpClient
    private lateinit var mainActivity: MainActivity

    // Список пакетов для игнорирования
    private val systemPackages = setOf(
        "android",
        "com.android.systemui"
    )

    override fun onCreate() {
        super.onCreate()
        tcpClient = TcpClient()
        mainActivity = MainActivity()
    }

    // Как только приходит уведомление
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (systemPackages.contains(sbn.packageName)) {
            return // Пропускаем системные уведомления
        }
        // Получаем уведомление
            val notification = sbn.notification

            // Извлекаем данные
            val extras = notification.extras
            val title = extras.getString(Notification.EXTRA_TITLE).orEmpty()

            val text = extras.getCharSequence(Notification.EXTRA_BIG_TEXT)
                ?: extras.getCharSequence(Notification.EXTRA_TEXT)
                ?: extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES)?.joinToString("\n")
                ?: ""
        mainActivity.mainMessageSend(text, title)
    }

    override fun onDestroy() {
        super.onDestroy()
        runBlocking {
            onListenerDisconnected()
        }
    }
}