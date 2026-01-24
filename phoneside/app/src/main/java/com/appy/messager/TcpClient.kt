import kotlinx.coroutines.*
import java.net.Socket
import java.io.OutputStream

class TcpClient {
    private var socket: Socket? = null
    private var outputStream: OutputStream? = null

    suspend fun connect(host: String, port: Int): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            socket = Socket(host, port)
            outputStream = socket?.getOutputStream()
            println("Connect")
            true
        } catch (e: Exception) {
            println("Does not connect")
            false
        }
    }

    suspend fun sendMessage(message: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            outputStream?.write(message.toByteArray())
            outputStream?.flush()
            println("Message send")
            true
        } catch (e: Exception) {
            println("Does not message send")
            false
        }
    }

    suspend fun disconnect() = withContext(Dispatchers.IO) {
        try {
            socket?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}