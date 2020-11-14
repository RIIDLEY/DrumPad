package com.example.drumpad

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.login.*
import java.util.HashMap
var rep: String = ""
class Login : AppCompatActivity() {

    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)



        retour.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        boutonlog.setOnClickListener {
            Log.i("Login","Bouton Oui")
            toServerLogin(pseudo.text.toString(),mdp.text.toString())
            progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Connection")
            progressDialog.setMessage("En cours de connection")
            progressDialog.show()
            val handler = Handler()
            handler.postDelayed({ changeView() }, 1000)

        }

    }

    fun registerFunc(view: View) {
        val intent = Intent(this, Register::class.java)
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
                Toast.makeText(this, "Reponse $response", Toast.LENGTH_SHORT).show()
                rep = response
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
        if (rep == "OK"){
            progressDialog.dismiss()
            Toast.makeText(this, "Connecté", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Commu::class.java)
            startActivity(intent)
        }else{
            progressDialog.dismiss()
            Toast.makeText(this, "Pseudo ou mot de passe incorrect", Toast.LENGTH_SHORT).show()
        }
    }

}