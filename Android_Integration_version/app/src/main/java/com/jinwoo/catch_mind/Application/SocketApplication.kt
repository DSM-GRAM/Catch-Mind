package com.jinwoo.catch_mind.Application

import android.app.Application
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketApplication: Application() {
        val socket = IO.socket("http://192.168.43.122:7000")
}