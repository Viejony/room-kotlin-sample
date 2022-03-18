package com.example.roomkotlinsample.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.roomkotlinsample.R

class InitScreenActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init_screen)

        val enterButton: Button = findViewById(R.id.bt_enter)
        val userTextView: TextView = findViewById(R.id.txt_user)

        // Set enter button listener
        enterButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("user", userTextView.text.toString())
            startActivity(intent)
        }
    }
}