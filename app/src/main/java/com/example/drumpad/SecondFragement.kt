package com.example.drumpad

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_second_fragement.*
import kotlinx.android.synthetic.main.fragment_second_fragement.view.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login.mdp
import kotlinx.android.synthetic.main.login.pseudo
import kotlinx.coroutines.delay
import java.util.HashMap


var Yest: String = "u"

class SecondFragement : Fragment() {

    //var dialog: AlertDialog.Builder = AlertDialog.Builder(context)
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_second_fragement, container, false)
       // return inflater.inflate(R.layout.fragment_second_fragement, container, false)

        view.button.setOnClickListener {
            Log.i("Login","Bouton Oui")
            toServerLogin(pseudo.text.toString(),mdp.text.toString())
            progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Connection")
            progressDialog.setMessage("En cours de connection")
            progressDialog.show()
            val handler = Handler()
            var mApp = Var()
            mApp.globalVar = "coucou"
            var strGlobalVar = mApp.globalVar
            Log.i("VARGLOBAL",strGlobalVar)
            handler.postDelayed({ changeView() }, 500)


            Log.i("Retour serveur", Yest)
        }

        return view
    }

    override fun onStart() {
        super.onStart()
    }


    fun toServerLogin(pseudo: String, mdp: String){
        volleyRequestQueue = Volley.newRequestQueue(requireContext())
        val parameters: MutableMap<String, String> = HashMap()
        parameters.put("pseudo",pseudo)
        parameters.put("mdp",mdp)
        parameters.put("fonction","login")
        val strReq: StringRequest = object : StringRequest(
            Method.POST,serverAPIURL,
            Response.Listener { response ->
                Log.i("toServeur", "Send")
                //Toast.makeText(requireContext(), "Reponse $response", Toast.LENGTH_SHORT).show()
                Log.i("deServeur",response)
                Yest = response
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

    fun registerFunc(view: View) {
        val intent = Intent(requireContext(), Register::class.java)
        startActivity(intent)
    }

    fun changeView(){
        Log.i("ChanegView","Je suis la")
        var mApp = Var()
        if (Yest == "OK"){
            mApp.setLog(true)
            progressDialog.dismiss()
            Toast.makeText(requireContext(), "Connecté", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, Commu::class.java)
            startActivity(intent)
        }else{
            mApp.setLog(false)
            progressDialog.dismiss()
            Toast.makeText(requireContext(), "Pseudo ou mot de passe incorrect", Toast.LENGTH_SHORT).show()
        }
    }


    fun test(){
        Log.i("Thread","Premier")
        Thread.sleep(20000)
        Log.i("Thread","fin")
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}

