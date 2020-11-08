package com.example.drumpad

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import java.util.HashMap




class SecondFragement : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_second_fragement, container, false)
       // return inflater.inflate(R.layout.fragment_second_fragement, container, false)

        view.button.setOnClickListener {
            toServerLogin(pseudo.text.toString(),mdp.text.toString())
            Log.i("T","Bouton")
        }

        return view
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
                Toast.makeText(requireContext(), "Reponse $response", Toast.LENGTH_SHORT).show()
                test = response
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
}

