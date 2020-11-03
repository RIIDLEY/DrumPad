package com.example.drumpad

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.serverftp.*

class FtpServer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.serverftp)

;

        retour.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    fun registerFunc(view: View) {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
    }


}