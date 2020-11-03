package com.example.drumpad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        retour.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    fun enregistrement(view: View) {
        Log.i("Pseudo", pseudo.text.toString())
        Log.i("mdp", mdp.text.toString())
        Log.i("mdpsec", mdpSec.text.toString())
        Log.i("mail", mail.text.toString())
        Log.i("mail", isEmailValid(mail.text.toString()).toString())
    }

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}