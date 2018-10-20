package com.jinwoo.catch_mind.Activity

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.app.ProgressDialog
import android.app.Service
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.jinwoo.catch_mind.Application.SocketApplication
import com.jinwoo.catch_mind.BindService
import com.jinwoo.catch_mind.R
import io.socket.client.Socket
import io.socket.emitter.Emitter


class ReadyActivity: AppCompatActivity() {

    val socket = SocketApplication.socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ready)

        Toast.makeText(this,"대기 중입니다.", Toast.LENGTH_SHORT)

        socket.connect()

        socket.emit("ready")

        socket.on("all ready", completeReady)
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
    }

    var completeReady = Emitter.Listener { args ->
        var player = args[0].toString().toInt()
        Log.e("player", "$player")
        if(player == 0) {
            var intent = Intent(this@ReadyActivity, MainActivity::class.java)
            startActivity(intent)
        } else if(player == 1){
            var intent = Intent(this@ReadyActivity, MainSubActivity::class.java)
            startActivity(intent)
        }
    }
}