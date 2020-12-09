package com.example.drumpad

import android.app.AlertDialog
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_mes_creations.*
import kotlinx.android.synthetic.main.activity_mes_creations.view.*
import kotlinx.android.synthetic.main.dialog_note.*
import kotlinx.android.synthetic.main.dialog_note.view.*
import kotlinx.android.synthetic.main.fragment_first_fragement.*
import kotlinx.android.synthetic.main.fragment_first_fragement.view.*
import kotlinx.coroutines.*
import java.util.HashMap


class FirstFragement : Fragment() {

    val serverAPIURL: String = "http://lahoucine-hamsek.site/test.php"
    var server = "http://lahoucine-hamsek.site/uploads/"
    var firtmusique: String = "http://lahoucine-hamsek.site/uploads/titre2.mp3"
    var toserver = ""
    var rep = ""
    var titre: String = ""
    var nbMax: Int = 0
    var premier: Boolean = true
    var fini2: Boolean = false
    var fini: Boolean = false
    private var mp: MediaPlayer? = null
    private var nbmusique: Int = 0
    lateinit var radioButton: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_first_fragement, container, false)
        // Inflate the layout for this fragment

        //controlSound("http://lahoucine-hamsek.site/uploads/recording1.mp3","recording1.mp3")
        toServerLogin(0,"getNbMax","","")
        Log.i("getNbMax",nbMax.toString())

       toServerLogin(nbmusique,"musique","","")

        GlobalScope.launch {
            delay(1500)
            toserver = server + rep
            Log.i("toserveur1",toserver)
            Log.i("rep",rep)
            fini = true
        }

        controlSound(firtmusique,"titre2.mp3")
        while (true && fini2==false){
            if (fini == true){
                Log.i("CONTROLSOUND","J4AI TOURNE")
                controlSound(toserver,rep)
                fini2 = true
            }
        }


/*
        mp = MediaPlayer()
        mp?.setDataSource(firtmusique)
        mp?.prepare()
        view.titre?.text = "titre2"
        controlSound("oui","oui")
*/
        view.skip_Co.setOnClickListener {
            Log.i("SKIP","skip")
            if (mp!==null){
                mp?.stop()
                mp?.reset()
                mp?.release()
                mp = null
            }
            if (nbmusique==nbMax-1){
                nbmusique=0
            }else{
                nbmusique+=1
            }
            toServerLogin(nbmusique,"musique","","")
            toserver = server + rep
            controlSound(toserver,rep)
            Log.i("nbMusique",nbmusique.toString())
        }

        view.back_Co.setOnClickListener {
            Log.i("BACK","back")
            if (mp!==null){
                mp?.stop()
                mp?.reset()
                mp?.release()
                mp = null
            }
            if (nbmusique==0){
                nbmusique=nbMax-1
            }else{
                nbmusique-=1
            }
            toServerLogin(nbmusique,"musique","","")
            toserver = server + rep
            controlSound(toserver,rep)
        }
        return view


    }

    override fun onStart() {
        super.onStart()
    }


    private fun controlSound(File: String, namefile: String) {
        if (File != "oui"){
            Log.i("controlSound","tourne")
            for (i in 0..namefile.length - 5) {
                titre += namefile[i]
            }
            view?.titre?.text = titre
            Log.i("titre",titre)
            Log.i("filename",namefile)
            titre = ""
        }

        view?.start_Co?.setOnClickListener {
            Log.i("START","start")
            if (mp == null) {
                mp = MediaPlayer()
                mp?.setDataSource(File)
                mp?.prepare()
                //mp?.start()
                //Log.d("MesCreations", "ID:${mp!!.audioSessionId}")
            }
            // initSeekBar()

            mp?.start()
            //Log.d("MesCreations", "Durée: ${mp!!.duration / 1000} seconds")
        }

        view?.pause_Co?.setOnClickListener {
            Log.i("PAUSE","pause")
            mp?.pause()
            //Log.d("MesCreations", "Je suis en pause: ${mp!!.currentPosition / 1000} seconds")
        }

        view?.stop_Co?.setOnClickListener {
            Log.i("STOP","stop")
            mp?.stop()
            mp?.reset()
            mp?.release()
            mp = null
        }

        view?.note?.setOnClickListener {
            val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_note,null)
            val dialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                .setPositiveButton("Voter"){dialog, which ->
                    val selectedOption: Int = view?.radioGroup1.checkedRadioButtonId
                    radioButton = view?.findViewById(selectedOption)
                    Log.i("Envoyé","Oui")
                    Log.i("Envoyé",namefile)
                    toServerLogin(0,"etoile",radioButton.text.toString(),namefile)
                }
                .setView(view)
            dialog.show()
        }
        toserver = ""
    }

    fun vote(){

    }

    fun toServerLogin(id: Int, fonction: String,etoile: String, namefile: String){
        volleyRequestQueue = Volley.newRequestQueue(requireContext())
        val parameters: MutableMap<String, String> = HashMap()
        parameters.put("fonction",fonction)
        parameters.put("id",id.toString())
        parameters.put("etoile",etoile)
        parameters.put("musique",namefile)
        val strReq: StringRequest = object : StringRequest(
            Method.POST,serverAPIURL,
            Response.Listener { response ->
                Log.i("toServeur", "Send")
                //Toast.makeText(requireContext(), "Reponse $response", Toast.LENGTH_SHORT).show()
                Log.i("reponse du serveur",response )
                if (fonction == "getNbMax"){
                    nbMax = response.toInt()
                }
                /*if (fonction == "musique" && (premier)){
                    premier = false
                    toserver = server + response
                    Log.i("j",toserver)
                    controlSound(toserver,rep)
                }*/
                else{
                    rep = response
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

}

