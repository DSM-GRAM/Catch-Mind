package com.jinwoo.catch_mind

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.util.*
import kotlin.concurrent.schedule

class MainSubActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_sub)

    }
}