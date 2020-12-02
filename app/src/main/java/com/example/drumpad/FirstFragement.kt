package com.example.drumpad

import android.media.MediaPlayer
import android.net.Uri
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
import kotlinx.android.synthetic.main.activity_mes_creations.*
import kotlinx.android.synthetic.main.activity_mes_creations.view.*
import kotlinx.android.synthetic.main.fragment_first_fragement.*
import kotlinx.android.synthetic.main.fragment_first_fragement.view.*
import kotlinx.coroutines.*
import java.util.HashMap


class FirstFragement : Fragment() {

    val serverAPIURL: String = "http://lahoucine-hamsek.site/test.php"
    var server = "http://lahoucine-hamsek.site/uploads/"
    var toserver = ""
    var rep = ""
    var titre: String = ""
    var nbMax: Int = 0
    private var mp: MediaPlayer? = null
    private var nbmusique: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_first_fragement, container, false)
        // Inflate the layout for this fragment

        toServerLogin(0,"getNbMax")
        Log.i("getNbMax",nbMax.toString())
        toServerLogin(nbmusique,"musique")

        val jobs: Job = GlobalScope.launch {
            delay(1000)
            toserver = server + rep
            Log.i("toserveur1",toserver)
            Log.i("rep",rep)
        }

        GlobalScope.launch {
            jobs.start()
            jobs.join()
            Log.i("Thread","JE SUIS FINI")
        }
        controlSound(toserver,rep)
        Log.i("CONTROLSOUND","J4AI TOURNE")

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
            toServerLogin(nbmusique,"musique")
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
            toServerLogin(nbmusique,"musique")
            toserver = server + rep
            controlSound(toserver,rep)
        }
        return view


    }

    override fun onStart() {
        super.onStart()
    }


    private fun controlSound(File: String, namefile: String) {

        Log.i("controlSound","tourne")

        for (i in 0..namefile.length - 5) {
            titre += namefile[i]
        }
        view?.titre?.text = titre
        titre = ""

        view?.start_Co?.setOnClickListener {
            Log.i("START","start")
            if (mp == null) {
                mp = MediaPlayer()
                mp?.setDataSource(File)
                mp?.prepare()
                mp?.start()
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
        toserver = ""
    }



    fun toServerLogin(id: Int, fonction: String){
        volleyRequestQueue = Volley.newRequestQueue(requireContext())
        val parameters: MutableMap<String, String> = HashMap()
        parameters.put("fonction",fonction)
        parameters.put("id",id.toString())
        val strReq: StringRequest = object : StringRequest(
            Method.POST,serverAPIURL,
            Response.Listener { response ->
                Log.i("toServeur", "Send")
                //Toast.makeText(requireContext(), "Reponse $response", Toast.LENGTH_SHORT).show()
                Log.i("reponse du serveur",response )
                if (fonction == "getNbMax"){
                    nbMax = response.toInt()
                }else{
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

    suspend fun init(){

    }
}

