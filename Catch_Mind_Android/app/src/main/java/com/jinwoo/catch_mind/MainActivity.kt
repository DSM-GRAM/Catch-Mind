package com.jinwoo.catch_mind

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import io.socket.client.Socket
import io.socket.emitter.Emitter

class MainActivity : AppCompatActivity() {

    lateinit var socket : Socket
    lateinit var questionWord: TextView
    lateinit var round: TextView
    lateinit var myscore: TextView
    lateinit var otherScore: TextView
    lateinit var red_color: View
    lateinit var pink_color: View
    lateinit var orange_color: View
    lateinit var yellow_color: View
    lateinit var green_color: View
    lateinit var yellow_green_color: View
    lateinit var sky_color: View
    lateinit var black_color: View
    lateinit var purple_color: View
    lateinit var light_purple_color: View
    lateinit var gray_color: View
    lateinit var amethyst_color: View

    lateinit var drawClass: DrawClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionWord = findViewById(R.id.word)
        round = findViewById(R.id.round)
        myscore = findViewById(R.id.myscore)
        otherScore = findViewById(R.id.otherscore)
        red_color = findViewById(R.id.red_color)
        pink_color = findViewById(R.id.pink_color)
        orange_color = findViewById(R.id.orange_color)
        yellow_color = findViewById(R.id.yellow_color)
        green_color = findViewById(R.id.green_color)
        yellow_green_color = findViewById(R.id.yellow_green_color)
        sky_color = findViewById(R.id.sky_color)
        black_color = findViewById(R.id.black_color)
        purple_color = findViewById(R.id.purple_color)
        light_purple_color = findViewById(R.id.light_purple_color)
        gray_color = findViewById(R.id.gray_color)
        amethyst_color = findViewById(R.id.amethyst_color)

        socket = SocketApplication.get()
        socket.connect()

        socket.on(Socket.EVENT_CONNECT, onConnect)

        red_color.setOnClickListener { v -> drawClass = DrawClass(this, ) }
        pink_color.setOnClickListener { v -> drawClass = DrawClass(this, )}
        orange_color.setOnClickListener { v -> drawClass = DrawClass(this, )}
        yellow_color.setOnClickListener { v -> drawClass = DrawClass(this, )}
        green_color.setOnClickListener { v -> drawClass = DrawClass(this, )}
        yellow_green_color.setOnClickListener { v -> drawClass = DrawClass(this, )}
        sky_color.setOnClickListener { v -> drawClass = DrawClass(this, )}
        black_color.setOnClickListener { v -> drawClass = DrawClass(this, )}
        purple_color.setOnClickListener { v -> drawClass = DrawClass(this, )}
        light_purple_color.setOnClickListener { v -> drawClass = DrawClass(this, )}
        gray_color.setOnClickListener { v -> drawClass = DrawClass(this, )}
        amethyst_color.setOnClickListener { v -> drawClass = DrawClass(this, )}



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
}
