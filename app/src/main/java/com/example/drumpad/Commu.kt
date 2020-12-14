package com.example.drumpad

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_commu.*
import java.util.HashMap


class Commu : AppCompatActivity() {

    val serverAPIURL: String = "http://lahoucine-hamsek.site/Drumpad.php"
    lateinit var sharedPreferences: SharedPreferences
    var volleyRequestQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commu)


        val fragement = Frag_Server_Musique()
        val fragement2 = Frag_Profil()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragement)
            commit()}

        button.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment, fragement)
                commit()}
        }

        button2.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment, fragement2)
                    commit()}
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onBackPressed() {// bouton retour d'android
        super.onBackPressed()
        val intent = Intent(this, Accueil::class.java)
        startActivity(intent)
    }


}