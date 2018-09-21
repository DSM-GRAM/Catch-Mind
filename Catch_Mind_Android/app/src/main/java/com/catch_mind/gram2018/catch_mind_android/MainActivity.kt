package com.catch_mind.gram2018.catch_mind_android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionWord = findViewById(R.id.question_word)
        round = findViewById(R.id.round)
        myscore = findViewById(R.id.my_Score)
        otherScore = findViewById(R.id.other_score)

        socket = SocketApplication.get()
        socket.connect()

        socket.on(Socket.EVENT_CONNECT, onConnect)

    }

    var onConnect = Emitter.Listener { args ->
        setData(args[0].toString(), args[1].toString(), args[2].toString(), args[3].toString())
    }

    fun setData(word: String ,roundNum: String, myscore: String, otherScore: String){
        questionWord.setText(word)
        round.setText("ROUND $roundNum")
        this.myscore.setText(myscore)
        this.otherScore.setText(otherScore)
    }
}
