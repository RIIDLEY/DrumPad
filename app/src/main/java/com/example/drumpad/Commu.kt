package com.example.drumpad


import android.content.SharedPreferences
import android.graphics.Insets.add
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.OneShotPreDrawListener.add
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.preference.PreferenceManager
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_commu.*
import java.util.HashMap


class Commu : AppCompatActivity() {

    val serverAPIURL: String = "http://lahoucine-hamsek.site/coucou.php"
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commu)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        toServerLogin("NbMusique")
        val fragement = FirstFragement()
        val fragement2 = SecondFragement()
        val fragement3 = ThirdFragment()

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

    fun toServerLogin(fonction: String){
        volleyRequestQueue = Volley.newRequestQueue(this)
        val parameters: MutableMap<String, String> = HashMap()
        parameters.put("fonction",fonction)
        parameters.put("id",sharedPreferences.getString("Login","")!!)
        val strReq: StringRequest = object : StringRequest(
            Method.POST,serverAPIURL,
            Response.Listener { response ->
                Log.i("toServeur", "Send")
                //Toast.makeText(requireContext(), "Reponse $response", Toast.LENGTH_SHORT).show()
                Log.i("NBmusique",response )
                Log.i("NBmusique",sharedPreferences.getString("Login","")!! )
                val editor = sharedPreferences.edit()
                editor.putString("NbMusique", response)
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