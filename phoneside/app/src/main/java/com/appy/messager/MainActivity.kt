package com.appy.messager

import android.os.Bundle
import androidx.activity.ComponentActivity
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val client = TcpClient()
        client.connect(serverIp = "192.168.1.11", port = 8080)
        client.sendMessage("Hello")
        client.disconnect()
        println("Yo")
        exitProcess(0)
    }
}
