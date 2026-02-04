package com.appy.messager

import kotlinx.coroutines.*
import java.net.Socket
import java.io.OutputStream

class TcpClient {
    private var socket: Socket? = null
    private var outputStream: OutputStream? = null

    // Подключение к серверу
    suspend fun connect(host: String, port: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            socket = Socket(host, port).apply {
                soTimeout = 5000
            }
            outputStream = socket!!.getOutputStream()
            true
        } catch (_: Exception) {
            socket = null
            outputStream = null
            false
        }
    }

    // Отправка уведомления
    suspend fun sendMessage(message: String): Boolean = withContext(Dispatchers.IO) {
        println("Message sending")
        try {
            val out = outputStream ?: return@withContext false
            out.write(message.toByteArray())
            out.flush()
            println("Message send $message")
            true
        } catch (_: Exception) {
            println("Send message does not work")
            false
        }
    }

    // Отключение от сервера
    suspend fun disconnect() = withContext(Dispatchers.IO) {
        try {
            outputStream?.close()
            socket?.close()
        } finally {
            outputStream = null
            socket = null
        }
    }
}