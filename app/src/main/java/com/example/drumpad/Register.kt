package com.example.drumpad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_register.*
import java.util.HashMap

var volleyRequestQueue: RequestQueue? = null
val serverAPIURL: String = "http://lahoucine-hamsek.site/coucou.php"

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
        Log.i("isEmailValid", isEmailValid().toString())
        Log.i("isMdpValid", isMdpValid().toString())
        val mdpNull = mdp.text.toString() != ""
        val pseudoNull = pseudo.text.toString() != ""
        val mailNull = mail.text.toString() != ""

     if (isEmailValid() && isMdpValid() && mdpNull && pseudoNull && mailNull){
         toServer(pseudo.text.toString(),mdp.text.toString(),mail.text.toString())
     }

    }

    fun isEmailValid(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(mail.text.toString()).matches()
    }

    fun isMdpValid(): Boolean {
        return mdp.text.toString() == mdpSec.text.toString()
    }

    fun toServer(pseudo: String, mdp: String, mail: String) {
        volleyRequestQueue = Volley.newRequestQueue(this)
        val parameters: MutableMap<String, String> = HashMap()
        // Add your parameters in HashMap
        parameters.put("pseudo",pseudo)
        parameters.put("mdp",mdp)
        parameters.put("mail",mail)
        parameters.put("fonction","singup")
        val strReq: StringRequest = object : StringRequest(
            Method.POST,serverAPIURL,
            Response.Listener { response ->
                Log.i("toServeur", "Send")
                Toast.makeText(this, "Reponse $response", Toast.LENGTH_SHORT).show()
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