package com.appy.messager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.*
import android.widget.Button


class MainActivity : ComponentActivity() {
    private lateinit var tcpClient: TcpClient
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tcpClient = TcpClient()

        setContentView(R.layout.layout)

        if (!isServiceEnabled(this)) {
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            startActivity(intent)
        }

        val startButt = findViewById<Button>(R.id.startButton)
        startButt.setOnClickListener {
            scope.launch {
                val isConnected = tcpClient.connect("192.168.1.11", 8080)

                if (isConnected) {
                    // Отправляем сообщение в фоновом потоке
                    println("Hello message send")
                    tcpClient.sendMessage("Hello from Android")
                }
            }
        }
        val stopButt = findViewById<Button>(R.id.stopButton)
        stopButt.setOnClickListener {
            scope.launch {
                tcpClient.disconnect()
            }
        }
    }
}

fun isServiceEnabled(context: Context): Boolean {
    val packageName = context.packageName
    val enabledListeners = NotificationManagerCompat.getEnabledListenerPackages(context)
    return enabledListeners.contains(packageName)
}