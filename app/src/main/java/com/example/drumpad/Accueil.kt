package com.example.drumpad

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_accueil.*

class Accueil : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        Pad.setOnClickListener {
            val intent = Intent(this, DrumPad::class.java)
            startActivity(intent)
        }

        crea.setOnClickListener {
            val intent = Intent(this, MesCreations::class.java)
            startActivity(intent)
        }

        about.setOnClickListener {
            val intent = Intent(this, About::class.java)
            startActivity(intent)
        }

        commu.setOnClickListener {
            if (sharedPreferences.getString("Login","")?.isNotEmpty()!!){//Si a deja ete log
                val intent = Intent(this, Commu::class.java)
                startActivity(intent)
            }else{//sinon il vas se log
                val intent = Intent(this, Enregistrement::class.java)
                startActivity(intent)
            }
        }

    }
}
