package com.example.drumpad


import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.fragment_frag_profil.view.*


class Frag_Profil : Fragment() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val view: View = inflater!!.inflate(R.layout.fragment_frag_profil, container, false)
        // return inflater.inflate(R.layout.fragment_second_fragement, container, false)
        val l: String = sharedPreferences.getString("Login","")!!
        view.pseudo.text = l
        view.textView.text = "Nombre de musique en ligne : " + sharedPreferences.getString("NbMusique","")!!
        view.textView2.text = "Nombre d'étoile collectée : " + 0
        return view
    }

    override fun onStart() {
        super.onStart()
    }


}

