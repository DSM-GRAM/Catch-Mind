package com.jinwoo.catch_mind.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.jinwoo.catch_mind.Application.SocketApplication
import com.jinwoo.catch_mind.Dialog.EndDialog
import com.jinwoo.catch_mind.DrawClass
import com.jinwoo.catch_mind.Model.SettingModel
import com.jinwoo.catch_mind.R
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main_sub.*
import java.util.*
import kotlin.concurrent.schedule

class MainSubActivity : AppCompatActivity() {
    private lateinit var socket : Socket

    lateinit var drawClass: DrawClass

    var SettingData = SettingModel()

    lateinit var answer: String
    var timeCounter = 30
    var timeMinute = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_sub)

        drawClass = DrawClass(this)
        drawlayout.addView(drawClass)

        socket = SocketApplication.get()
        socket.connect()

        socket.on("connect", onConnect)

        timerStart()

        setData()

        socket.on("color", color)
        socket.on("location", location)

        submiting.setOnClickListener { v ->
            if(answer_edit.toString() == answer){
                socket.emit("collect")
                SettingData.round += 1
                SettingData.myscore += 10
                val intent = Intent(this@MainSubActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            Toast.makeText(applicationContext, "오답이네요!", Toast.LENGTH_SHORT).show()
        }

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
                        break
                    }
                }
                SettingData.round += 1
                val intent = Intent(this@MainSubActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
    fun result(win_or_lose: String){
        val dialog = EndDialog(this, win_or_lose, SettingData.myscore.toString(), SettingData.otherscore.toString())
        dialog.show()
    }

    var color = Emitter.Listener{ args ->
        Thread {
            drawClass.drawPaint!!.color = args[0].toString().toInt()
        }
    }

    var location = Emitter.Listener { args ->
        runOnUiThread{
            drawClass.touchX = args[0].toString().toFloat()
            drawClass.touchY= args[0].toString().toFloat()
        }
    }

    var onConnect = Emitter.Listener { args ->
        Thread{
            answer = args[0].toString()
        }
    }
}