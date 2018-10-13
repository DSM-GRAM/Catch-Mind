package com.jinwoo.catch_mind.Activity

import android.app.ActionBar
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    var SettingData = SettingModel

    var timeCounter = 10
    var timeMinute = 0

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

        eraser.setOnClickListener{ v -> colorClick("#FFFFFF", 50f)}
        red_color.setOnClickListener { v -> colorClick("#FF8585", 5f)}
        pink_color.setOnClickListener { v -> colorClick("#FF98C8", 5f) }
        orange_color.setOnClickListener { v -> colorClick("#FFCE85", 5f) }
        yellow_color.setOnClickListener { v -> colorClick("#FBFB8E", 5f) }
        green_color.setOnClickListener { v -> colorClick("#56E69E", 5f) }
        yellow_green_color.setOnClickListener { v -> colorClick("#A7FBB0", 5f) }
        sky_color.setOnClickListener { v -> colorClick("#ABF9F4", 5f) }
        black_color.setOnClickListener { v -> colorClick("#000000", 5f) }
        purple_color.setOnClickListener { v -> colorClick("#9570FF", 5f) }
        light_purple_color.setOnClickListener { v -> colorClick("#A4A7FF", 5f) }
        gray_color.setOnClickListener { v -> colorClick("#757575", 5f) }
        amethyst_color.setOnClickListener { v -> colorClick("#D784FF", 5f) }

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

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect();
    }

    fun setData(){
        round.setText("ROUND ${SettingData.round}")
        myscore.setText(SettingData.myscore.toString())
        otherscore.setText(SettingData.otherscore.toString())
    }

    fun colorClick(color: String, width: Float) {
        Log.e("선택", "레드!!!!")
        socket.emit("color", color, width)
        drawClass.setColor(color, width)
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
                Log.e("라운드 데이터", "${SettingData.round}")
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
