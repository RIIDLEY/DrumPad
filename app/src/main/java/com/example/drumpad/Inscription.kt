package com.example.drumpad

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_inscription.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.HashMap


class Inscription : AppCompatActivity() {

    lateinit var progressDialog: ProgressDialog
    var volleyRequestQueue: RequestQueue? = null
    val serverAPIURL: String = "http://lahoucine-hamsek.site/Drumpad.php"
    var reponseServer: String = ""
    var isPseudoValid: Boolean = false
    var mdpNull: Boolean = false
    var pseudoNull: Boolean = false
    var mailNull: Boolean = false
    var pseudoStock: String = ""
    var mdpStock: String = ""
    var mailStock: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscription)

        retour.setOnClickListener {
            val intent = Intent(this, Accueil::class.java)
            startActivity(intent)
        }
    }


    fun inscription(view: View) {

        Log.i("Pseudo", pseudo.text.toString())
        Log.i("mdp", mdp.text.toString())
        Log.i("mdpsec", mdpSec.text.toString())
        Log.i("mail", mail.text.toString())
        Log.i("isEmailValid", isEmailValid().toString())
        Log.i("isMdpValid", isMdpValid().toString())

        pseudoStock = pseudo.text.toString()
        mdpStock = mdp.text.toString()
        mailStock = mail.text.toString()

        mdpNull = mdp.text.toString() != ""
        pseudoNull = pseudo.text.toString() != ""
        mailNull = mail.text.toString() != ""
        toServer(pseudo.text.toString(),"","","isInDBClient")

    }

    fun verif(){
        if (isPseudoValid) {
            if (isEmailValid() && isMdpValid() && mdpNull && pseudoNull && mailNull) {
                if (mdp.text.toString() == mdpSec.text.toString()) {
                    progressDialog = ProgressDialog(this)
                    progressDialog.setTitle("Connection")
                    progressDialog.setMessage("En cours de connexion")
                    progressDialog.show()
                    toServer(
                        pseudoStock,
                        mdpStock,
                        mailStock,
                        "singup"
                    )
                }else {
                    Toast.makeText(
                        this,
                        "MDP et confirmation MDP ne sont pas identique",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, "Champ vide ou mail non valide", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "Pseudo deja utilisé", Toast.LENGTH_SHORT).show()
        }
    }

    fun isEmailValid(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(mail.text.toString()).matches()
    }

    fun isMdpValid(): Boolean {
        return mdp.text.toString() == mdpSec.text.toString()
    }


    fun toServer(pseudo: String, mdp: String, mail: String, fonction: String) {
        volleyRequestQueue = Volley.newRequestQueue(this)
        val parameters: MutableMap<String, String> = HashMap()
        // Add your parameters in HashMap
        parameters.put("pseudo",pseudo)
        parameters.put("mdp",mdp)
        parameters.put("mail",mail)
        parameters.put("fonction",fonction)
        val strReq: StringRequest = object : StringRequest(
            Method.POST,serverAPIURL,
            Response.Listener { response ->
                Log.i("toServeur", "Send")
                if (fonction=="isInDBClient"){
                    Log.i("isPseudoValid",response)
                    isPseudoValid = response.toBoolean()
                    verif()
                }else{
                   Log.i("Reponse serveur",response)
                    reponseServer = response
                    changeView()
                }
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
            Toast.makeText(this, "Compte crée", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Enregistrement::class.java)
            startActivity(intent)
        }else{
            progressDialog.dismiss()
            Toast.makeText(this, "Probleme lors de la création du compte", Toast.LENGTH_SHORT).show()
        }
    }
}