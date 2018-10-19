package com.jinwoo.catch_mind.Dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.jinwoo.catch_mind.R
import kotlinx.android.synthetic.main.gg_dialog.*
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import com.jinwoo.catch_mind.Application.SocketApplication


class EndDialog(context: Context, result_text1: String, result_text2: String, result_text3: String): Dialog(context) {
    private val LAYOUT = R.layout.gg_dialog
    var result_text1: String
    var result_text2: String
    var result_text3: String
    var socket = SocketApplication.get()

    init {
        this.result_text1 = result_text1
        this.result_text2 = result_text2
        this.result_text3 = result_text3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LAYOUT)

        win_or_lose.setText(result_text1)
        Rmyscore.setText(result_text2)
        Rotherscore.setText(result_text3)
        game_close.setOnClickListener { v ->
            socket.disconnect()
            System.exit(0)
        }
    }
}