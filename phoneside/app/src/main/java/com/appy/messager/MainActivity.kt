package com.appy.messager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.core.app.NotificationManagerCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)

        val button = findViewById<Button>(R.id.btnToggleService)

        updateButtonText(button)

        button.setOnClickListener {
            if (!isServiceEnabled(this)) {
                startActivity(
                    Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                )
                return@setOnClickListener
            }

            updateButtonText(button)
        }
    }

    fun isServiceEnabled(context: Context): Boolean {
        val packageName = context.packageName
        val enabledListeners = NotificationManagerCompat.getEnabledListenerPackages(context)
        return enabledListeners.contains(packageName)
    }

    private fun updateButtonText(button: Button) {
        button.text = "Start"
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, NotificationService::class.java))
    }
}