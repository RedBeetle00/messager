package com.appy.messager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.app.NotificationManagerCompat


class MainActivity : ComponentActivity() {
    private lateinit var tcpClient: TcpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tcpClient = TcpClient()

        setContentView(R.layout.layout)

        if (!isServiceEnabled(this)) {
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            startActivity(intent)
        }
    }

    fun isServiceEnabled(context: Context): Boolean {
        val packageName = context.packageName
        val enabledListeners = NotificationManagerCompat.getEnabledListenerPackages(context)
        return enabledListeners.contains(packageName)
    }
}