package com.jinwoo.catch_mind

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.util.*
import kotlin.concurrent.schedule
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var socket : Socket

    lateinit var drawClass: DrawClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var timeCounter = 0
        var timeMinute = 3

        socket = SocketApplication.get()
        socket.connect()

        socket.on("connect", onConnect)
        socket.on("Win", win)
        socket.on("Lose", lose)


        runOnUiThread{
            Timer("SettingUp", false).schedule(1000) {
                timeCounter--
                if(timeCounter < 0) {
                    timeCounter = 59
                    timeMinute--
                }
                timer.setText("$timeMinute:$timeCounter")
                if(timeMinute < 0) {
                    val intent = Intent(this@MainActivity, MainSubActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        red_color.setOnClickListener { v -> colorClick(0xFF8585) }
        pink_color.setOnClickListener { v -> colorClick(0xFF98C8) }
        orange_color.setOnClickListener { v -> colorClick(0xFFCE85) }
        yellow_color.setOnClickListener { v -> colorClick(0xFBFB8E) }
        green_color.setOnClickListener { v -> colorClick(0x56E69E) }
        yellow_green_color.setOnClickListener { v -> colorClick(0xA7FBB0) }
        sky_color.setOnClickListener { v -> colorClick(0xABF9F4) }
        black_color.setOnClickListener { v -> colorClick(0x000000) }
        purple_color.setOnClickListener { v -> colorClick(0x9570FF) }
        light_purple_color.setOnClickListener { v -> colorClick(0xA4A7FF) }
        gray_color.setOnClickListener { v -> colorClick(0x757575) }
        amethyst_color.setOnClickListener { v -> colorClick(0xD784FF) }
    }

    var onConnect = Emitter.Listener { args ->
        runOnUiThread {
            setData(args[0].toString(), args[1].toString(), args[2].toString(), args[3].toString())
        }
    }

    fun setData(receive_word: String ,receive_roundNum: String, receive_myscore: String, receive_otherScore: String){
        word.setText(receive_word)
        round.setText("ROUND $receive_roundNum")
        myscore.setText(receive_myscore)
        otherscore.setText(receive_otherScore)
    }

    fun colorClick(color :Long) {
        socket.emit("color", color)
        drawClass = DrawClass(this, color)
        drawlayout.addView(drawClass)
    }

    var win = Emitter.Listener{ args ->
        val dialog = EndDialog(this, args[0].toString(), args[1].toString(), args[2].toString())
        dialog.show()
    }

    var lose = Emitter.Listener{ args ->
        val dialog = EndDialog(this, args[0].toString(), args[1].toString(), args[2].toString())
        dialog.show()
    }
}
