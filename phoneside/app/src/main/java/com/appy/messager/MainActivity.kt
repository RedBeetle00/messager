package com.appy.messager

import TcpClient
import android.os.Bundle
import android.os.SystemClock.sleep
import androidx.activity.ComponentActivity
import kotlinx.coroutines.*


class MainActivity : ComponentActivity() {
    private lateinit var tcpClient: TcpClient
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)
        scope.launch {
            tcpClient = TcpClient()
            val isConnected = tcpClient.connect("192.168.1.11", 8080)

            if (isConnected) {
                // Отправляем сообщение в фоновом потоке
                val success = tcpClient.sendMessage("Hello from Android")
            }
                sleep(1000)
                tcpClient.disconnect()
        }
    }
}