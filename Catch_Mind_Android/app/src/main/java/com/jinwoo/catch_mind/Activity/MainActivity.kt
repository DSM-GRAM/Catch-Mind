package com.jinwoo.catch_mind.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jinwoo.catch_mind.*
import com.jinwoo.catch_mind.Application.SocketApplication
import com.jinwoo.catch_mind.Dialog.EndDialog
import com.jinwoo.catch_mind.Model.SettingModel
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.util.*
import kotlin.concurrent.schedule
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var socket : Socket

    lateinit var drawClass: DrawClass

    var SettingData = SettingModel()

    var timeCounter = 0
    var timeMinute = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawClass = DrawClass(this)
        drawlayout.addView(drawClass)

        socket = SocketApplication.get()
        socket.connect()

        socket.on("connect", onConnect)

        socket.on("correct", correct)

        timerStart()

        setData()

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

        if(SettingData.round > 5) {
            if (SettingData.myscore > SettingData.otherscore) {
                result("WIN!")
            } else if(SettingData.otherscore > SettingData.myscore){
                result("LOSE..")
            } else {
                result("DRAW!")
            }
        }
    }

    fun setData(){
        round.setText("ROUND ${SettingData.round}")
        myscore.setText(SettingData.myscore.toString())
        otherscore.setText(SettingData.otherscore.toString())
    }

    fun colorClick(color: Long) {
        socket.emit("color", color)
        drawClass.paintColor = color.toInt()
    }

    fun timerStart(){
        runOnUiThread{
            Timer("SettingUp", false).schedule(1000) {
                while (true) {
                    Thread.sleep(1000)
                    timeCounter--

                    if (timeCounter < 0) {
                        timeCounter = 59
                        timeMinute--
                    }
                    timer.setText("$timeMinute:$timeCounter")
                    if (timeMinute == 0 && timeCounter == 0) {
                        break;
                    }
                }
                SettingData.round += 1
                val intent = Intent(this@MainActivity, MainSubActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    fun result(win_or_lose: String){
        val dialog = EndDialog(this, win_or_lose, SettingData.myscore.toString(), SettingData.otherscore.toString())
        dialog.show()
    }

    var onConnect = Emitter.Listener { args ->
        runOnUiThread {
            word.setText(args[0].toString())
        }
    }

    var correct = Emitter.Listener { args ->
        runOnUiThread{
            SettingData.round += 1
            SettingData.otherscore += 10
            val intent = Intent(this@MainActivity, MainSubActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
