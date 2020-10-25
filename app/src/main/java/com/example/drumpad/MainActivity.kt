package com.example.drumpad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_accueil.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        Pad.setOnClickListener {
            val intent = Intent(this, DrumPad::class.java)
            startActivity(intent)
        }

        login.setOnClickListener {
            val intent = Intent(this, FtpServer::class.java)
            startActivity(intent)
        }

        about.setOnClickListener {
            val intent = Intent(this, About::class.java)
            startActivity(intent)
        }

    }
}

