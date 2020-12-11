package com.example.drumpad


import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_frag_profil.view.*
import java.util.HashMap


class Frag_Profil : Fragment() {

    lateinit var sharedPreferences: SharedPreferences
    var volleyRequestQueue: RequestQueue? = null
    val serverAPIURL: String = "http://lahoucine-hamsek.site/Drumpad.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val view: View = inflater!!.inflate(R.layout.fragment_frag_profil, container, false)
        val l: String = sharedPreferences.getString("Login","")!!
        view.pseudo.text = l
        view.textView.text = "Nombre de musique en ligne : " + sharedPreferences.getString("NbMusique","")!!
        view.etoile1.text = "1 etoile : " + sharedPreferences.getString("1etoile","")!!
        view.etoile2.text = "2 etoile : " + sharedPreferences.getString("2etoile","")!!
        view.etoile3.text = "3 etoile : " + sharedPreferences.getString("3etoile","")!!
        view.etoile4.text = "4 etoile : " + sharedPreferences.getString("4etoile","")!!
        view.etoile5.text = "5 etoile : " + sharedPreferences.getString("5etoile","")!!
        return view
    }

    override fun onStart() {
        super.onStart()
    }

}

