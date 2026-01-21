
// Весь этот код был написан нейросетью. Я его заменю как только разберусь как он работает
package com.appy.messager

import java.net.InetSocketAddress

class TcpClient {
    private var socket: Socket? = null
    private var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null

    fun connect(serverIp: String, port: Int) {
        try {
            // Работа с сетью должна быть в отдельном потоке
            Thread {
                socket = Socket()
                // Таймаут подключения
                socket?.connect(InetSocketAddress(serverIp, port), 5000)

                outputStream = socket?.getOutputStream()
                inputStream = socket?.getInputStream()

                // Начать прослушивание входящих сообщений
                startReceiving()
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendMessage(message: String) {
        try {
            outputStream?.write(message.toByteArray())
            outputStream?.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startReceiving() {
        Thread {
            val buffer = ByteArray(1024)
            var bytes: Int

            try {
                while (inputStream?.read(buffer).also { bytes = it ?: -1 } != -1) {
                    val message = String(buffer, 0, bytes)
                    // Обработать полученное сообщение
                    handleMessage(message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun disconnect() {
        try {
            socket?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}