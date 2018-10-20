package com.jinwoo.catch_mind.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.jinwoo.catch_mind.Application.SocketApplication
import com.jinwoo.catch_mind.AutoDrawClass
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
    val socket = SocketApplication.socket
    lateinit var AutodrawClass: AutoDrawClass

    var SettingData = SettingModel

    var answer: String = "개구리"
    var timeCounter = 30
    var timeMinute = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_sub)

        Log.e("라운드 데이터", "${SettingData.round}")

        AutodrawClass = AutoDrawClass(this)
        drawlayout.addView(AutodrawClass)

        Thread{ socket.on("onConnect", onConnect) }

        timerStart()

        setData()

        socket.on("color", color)

        submiting.setOnClickListener { v ->
            if(answer_edit.toString() == answer){
                socket.emit("correct")
                SettingData.round += 1
                SettingData.myscore += 10
                val intent = Intent(this@MainSubActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else {
                Toast.makeText(this, "오답이네요!", Toast.LENGTH_SHORT).show()
            }
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
                    runOnUiThread{
                        timer.setText("$timeMinute:$timeCounter")
                    }
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
        var params: WindowManager.LayoutParams = dialog.window.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window.attributes = params
        dialog.show()
        onStop()
    }

    var color = Emitter.Listener{ args ->
        AutodrawClass.setColor(args[0].toString(), args[1].toString().toFloat())
    }

    var onConnect = Emitter.Listener { args ->
        Thread{
            answer = args[0].toString()
        }
    }
}