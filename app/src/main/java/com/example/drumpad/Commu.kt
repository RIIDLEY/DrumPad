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

    val serverAPIURL: String = "http://lahoucine-hamsek.site/coucou.php"
    lateinit var sharedPreferences: SharedPreferences
    var volleyRequestQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commu)

       sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        toServerLogin("NbMusique")//Avoir le nombre de musique deja upload par l'utilisateur
        toServerLogin("1etoile")
        toServerLogin("2etoile")
        toServerLogin("3etoile")
        toServerLogin("4etoile")
        toServerLogin("5etoile")
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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Accueil::class.java)
        startActivity(intent)
    }

    fun toServerLogin(fonction: String){
        volleyRequestQueue = Volley.newRequestQueue(this)
        val parameters: MutableMap<String, String> = HashMap()
        parameters.put("fonction",fonction)
        parameters.put("id",sharedPreferences.getString("Login","")!!)
        val strReq: StringRequest = object : StringRequest(
            Method.POST,serverAPIURL,
            Response.Listener { response ->
                Log.i("toServeur", "Send")
                Log.i("Reponse serveur",response )
                val editor = sharedPreferences.edit()
                if(fonction=="NbMusique"){
                    editor.putString("NbMusique", response)
                    Log.i("NbMusique",response )
                }
                if(fonction=="1etoile"){
                    editor.putString("1etoile", response)
                    Log.i("1etoile",response )
                }
                if(fonction=="2etoile"){
                    editor.putString("2etoile", response)
                    Log.i("2etoile",response )
                }
                if(fonction=="3etoile"){
                    editor.putString("3etoile", response)
                    Log.i("3etoile",response )
                }
                if(fonction=="4etoile"){
                    editor.putString("4etoile", response)
                    Log.i("4etoile",response )
                }
                if(fonction=="5etoile"){
                    editor.putString("5etoile", response)
                    Log.i("5etoile",response )
                }
                editor.apply()
            },
            Response.ErrorListener { volleyError -> // error occurred
                Log.i("toServeur", "Error")}) {

            override fun getParams(): MutableMap<String, String> {
                return parameters;
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                // Add your Header paramters here
                return headers
            }
        }
        // Adding request to request queue
        volleyRequestQueue?.add(strReq)
    }

}