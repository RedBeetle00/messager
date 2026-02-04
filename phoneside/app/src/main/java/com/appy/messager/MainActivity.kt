package com.appy.messager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var tcpClient: TcpClient
    private val serviceJob = Job()
    val scope = CoroutineScope(Dispatchers.IO + serviceJob)
    var isEnable = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)
        tcpClient = TcpClient()

        val button = findViewById<Button>(R.id.btnToggleService)

        updateButtonText(button)

        button.setOnClickListener {
            if (!isEnable) {
                isEnable = true
                println(isEnable)
                if (!isServiceEnabled(this)) {
                    startActivity(
                        Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                    )
                    return@setOnClickListener
                }

                // Получаем ввод для IP
                val editTextHost = findViewById<EditText>(R.id.host_text)
                val textHost = editTextHost.text.toString()

                // Полчаем ввод для порта
                val editTextPort = findViewById<EditText>(R.id.port_text)
                val textPort = editTextPort.text.toString().toInt()

                scope.launch {
                    println("Connect scope is launch")
                    tcpClient.connect(textHost, textPort)
                }
            }
            else {
                isEnable = false
                println(isEnable)
                scope.launch {
                    println("Disconnect scope is launch")
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

    private fun updateButtonText(button: Button) {
        button.text = "Start"
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, NotificationService::class.java))
        serviceJob.cancel()    }

    fun mainMessageSend(text: CharSequence, title: String) {
        scope.launch {
            println("Trying to send message")
            // Отправляем данные на ПК
            tcpClient.sendMessage("$title:\n$text\n")
        }
    }
}