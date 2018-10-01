package com.jinwoo.catch_mind

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    private lateinit var socket : Socket

    lateinit var questionWord: TextView
    lateinit var round: TextView
    lateinit var myscore: TextView
    lateinit var otherScore: TextView

    var red_color: View = findViewById(R.id.red_color)
    var pink_color: View = findViewById(R.id.pink_color)
    var orange_color: View = findViewById(R.id.orange_color)
    var yellow_color: View = findViewById(R.id.yellow_color)
    var green_color: View = findViewById(R.id.green_color)
    var yellow_green_color: View = findViewById(R.id.yellow_green_color)
    var sky_color: View = findViewById(R.id.sky_color)
    var black_color: View = findViewById(R.id.black_color)
    var purple_color: View = findViewById(R.id.purple_color)
    var light_purple_color: View = findViewById(R.id.light_purple_color)
    var gray_color: View = findViewById(R.id.gray_color)
    var amethyst_color: View = findViewById(R.id.amethyst_color)
    var stage: FrameLayout = findViewById(R.id.drawlayout)

    lateinit var drawClass: DrawClass
    lateinit var timerText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var timeCounter = 0
        var timeMinute = 3

        questionWord = findViewById(R.id.word)
        round = findViewById(R.id.round)
        myscore = findViewById(R.id.myscore)
        otherScore = findViewById(R.id.otherscore)
        timerText = findViewById(R.id.timer)

        socket = SocketApplication.get()
        socket.connect()

        socket.on(Socket.EVENT_CONNECT, onConnect)
        socket.on("gameOver", end)


        runOnUiThread{
            Timer("SettingUp", false).schedule(1000) {
                timeCounter--
                if(timeCounter < 0) {
                    timeCounter = 59
                    timeMinute--
                }
                timerText.setText("$timeMinute:$timeCounter")
                if(timeMinute < 0) {
                    val intent = Intent(this@MainActivity, MainSubActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        red_color.setOnClickListener { v -> colorClick() }
        pink_color.setOnClickListener { v -> colorClick() }
        orange_color.setOnClickListener { v -> colorClick() }
        yellow_color.setOnClickListener { v -> colorClick() }
        green_color.setOnClickListener { v -> colorClick() }
        yellow_green_color.setOnClickListener { v -> colorClick() }
        sky_color.setOnClickListener { v -> colorClick() }
        black_color.setOnClickListener { v -> colorClick() }
        purple_color.setOnClickListener { v -> colorClick() }
        light_purple_color.setOnClickListener { v -> colorClick() }
        gray_color.setOnClickListener { v -> colorClick() }
        amethyst_color.setOnClickListener { v -> colorClick() }
    }

    var onConnect = Emitter.Listener { args ->
        runOnUiThread({
            setData(args[0].toString(), args[1].toString(), args[2].toString(), args[3].toString())
        })
    }

    fun setData(word: String ,roundNum: String, myscore: String, otherScore: String){
        questionWord.setText(word)
        round.setText("ROUND $roundNum")
        this.myscore.setText(myscore)
        this.otherScore.setText(otherScore)
    }

    fun colorClick(color :Long) {
        socket.emit("color", color)
        drawClass = DrawClass(this, color)
        stage.addView(drawClass)
    }

    var end = Emitter.Listener{ args ->

    }
}
