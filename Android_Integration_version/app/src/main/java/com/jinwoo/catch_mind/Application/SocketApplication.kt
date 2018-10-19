package com.jinwoo.catch_mind.Application

import android.app.Application
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class SocketApplication: Application() {

    companion object {
        lateinit var socket: Socket
        fun get(): Socket{
            try {
                socket = IO.socket("http://192.168.137.191:7000")
            } catch ( e: URISyntaxException) {
                e.printStackTrace()
            }
            return socket
        }
    }
}