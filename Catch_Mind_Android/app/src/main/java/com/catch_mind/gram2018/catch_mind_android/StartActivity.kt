package com.catch_mind.gram2018.catch_mind_android

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class StartActivity : AppCompatActivity() {

    lateinit var start_text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        start_text = findViewById(R.id.touch_start)

        start_text.setOnClickListener { v ->
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}