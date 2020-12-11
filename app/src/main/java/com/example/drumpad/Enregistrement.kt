package com.example.drumpad

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_enregistrement.*
import kotlinx.coroutines.*
import java.util.HashMap



class Enregistrement : AppCompatActivity() {

    var reponseServer: String = ""
    var key1: String = "Login"
    var key2: String = "Pass"
    val serverAPIURL: String = "http://lahoucine-hamsek.site/Drumpad.php"
    var volleyRequestQueue: RequestQueue? = null
    lateinit var progressDialog: ProgressDialog
    lateinit var sharedPreferences: SharedPreferences
    var login: String = ""
    var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enregistrement)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        retour.setOnClickListener {
            val intent = Intent(this, Accueil::class.java)
            startActivity(intent)
        }

        boutonlog.setOnClickListener {
            Log.i("Login","Bouton Oui")
            login = pseudo.text.toString()
            password = mdp.text.toString()
            toServerLogin(pseudo.text.toString(),mdp.text.toString())
            progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Connection")
            progressDialog.setMessage("En cours de connection")
            progressDialog.show()
            GlobalScope.launch {
                delay(1000)
                changeView()
            }

        }

    }

    override fun onStart() {
        super.onStart()
        Log.i("LogGVar",sharedPreferences.getString("Login","").toString())
        if (sharedPreferences.getString("Login","")?.isNotEmpty()!!){
            val l: String = sharedPreferences.getString(key1,"")!!
            val p: String = sharedPreferences.getString(key2,"")!!
            toServerLogin(l,p)
            progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Connection")
            progressDialog.setMessage("En cours de connection")
            progressDialog.show()
            GlobalScope.launch {
                delay(1000)
                changeView()
            }
        }
    }

    fun registerFunc(view: View) {
        val intent = Intent(this, Inscription::class.java)
        startActivity(intent)
    }

    fun toServerLogin(pseudo: String, mdp: String){
        volleyRequestQueue = Volley.newRequestQueue(this)
        val parameters: MutableMap<String, String> = HashMap()
        parameters.put("pseudo",pseudo)
        parameters.put("mdp",mdp)
        parameters.put("fonction","login")
        val strReq: StringRequest = object : StringRequest(
            Method.POST,serverAPIURL,
            Response.Listener { response ->
                Log.i("toServeur", "Send")
                //Toast.makeText(this, "Reponse $response", Toast.LENGTH_SHORT).show()
                reponseServer = response
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

    fun changeView(){
        Log.i("ChanegView","Je suis la")
        if (reponseServer == "OK"){
            progressDialog.dismiss()

            val editor = sharedPreferences.edit()
            editor.putString(key1, login)
            editor.putString(key2, password)
            editor.apply()

            //Toast.makeText(this, "Connecté", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Commu::class.java)
            startActivity(intent)
        }else{
            progressDialog.dismiss()
            Toast.makeText(this, "Pseudo ou mot de passe incorrect", Toast.LENGTH_SHORT).show()
        }
    }

}