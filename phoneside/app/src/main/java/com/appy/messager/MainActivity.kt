package com.appy.messager

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock.sleep
import androidx.activity.ComponentActivity
import kotlinx.coroutines.*


class MainActivity : ComponentActivity() {
    private lateinit var tcpClient: TcpClient
    private lateinit var notificationService: NotificationService
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        tcpClient = TcpClient()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.layout)

        // Launch permission menu
        //val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
        //startActivity(intent)

        scope.launch {
            val isConnected = tcpClient.connect("192.168.1.11", 8080)

            if (isConnected) {
                var i = 0
                while (i != 5) {
                    i++
                    // Отправляем сообщение в фоновом потоке
                    tcpClient.sendMessage("Hello from Android")
                }

            }
            tcpClient.disconnect()
        }
    }
}