package com.example.drumpad

import android.app.AlertDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.SeekBar
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.alert_dialog_note.view.*
import kotlinx.android.synthetic.main.fragment_frag_server_musique.*
import kotlinx.android.synthetic.main.fragment_frag_server_musique.view.*
import kotlinx.coroutines.*
import java.util.HashMap


class Frag_Server_Musique : Fragment() {

    var volleyRequestQueue: RequestQueue? = null
    val serverAPIURL: String = "http://lahoucine-hamsek.site/test.php"
    var serverFolder = "http://lahoucine-hamsek.site/uploads/"
    var firtmusique: String = "http://lahoucine-hamsek.site/uploads/daft.mp3"
    var URLfile = ""
    var file = ""
    var titre: String = ""
    var nbMax: Int = 0
    var sortdelaboucle: Boolean = false
    var threadfini: Boolean = false
    var mp: MediaPlayer? = null
    var nbmusique: Int = 0
    var seekbarcoroutine: Job? = null
    var nouvellemusique: Boolean = false
    var artiste: String = "RIDLEY"
    lateinit var radioButton: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStop() {
        super.onStop()
        if (mp!==null){
            mp?.stop()
            mp?.reset()
            mp?.release()
            mp = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mp!==null){
            mp?.stop()
            mp?.reset()
            mp?.release()
            mp = null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_frag_server_musique, container, false)

        toServerLogin(0,"getNbMax","","")
        Log.i("getNbMax",nbMax.toString())

       toServerLogin(nbmusique,"musique","","")

        view.skip_Co.setOnClickListener {
            Log.i("SKIP","skip")
            SeekBarFrag.progress = 0
            nouvellemusique=true
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
            URLfile = serverFolder + file
            controlSound(URLfile,file)
            Log.i("nbMusique",nbmusique.toString())
        }

        view.back_Co.setOnClickListener {
            Log.i("BACK","back")
            SeekBarFrag.progress = 0
            nouvellemusique=true
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
            URLfile = serverFolder + file
            controlSound(URLfile,file)
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        controlSound(firtmusique,"daft.mp3")
    }


    private fun controlSound(File: String, namefile: String) {
        view?.SeekBarFrag?.progress=0
        if (File != "oui"){
            Log.i("controlSound","tourne")
            for (i in 0..namefile.length - 5) {
                titre += namefile[i]
            }
            view?.titre?.text = titre
            view?.FragArtiste?.text = "Artiste : " + artiste
            Log.i("titre",titre)
            Log.i("filename",namefile)
            titre = ""
            artiste = ""
        }

        view?.start_Co?.setOnClickListener {
            nouvellemusique=false
            Log.i("START","start")
            if (mp == null) {
                mp = MediaPlayer()
                mp?.setDataSource(File)
                mp?.prepare()
            }
            initialiseSeekBar()

            mp?.start()
        }

        view?.pause_Co?.setOnClickListener {
            Log.i("PAUSE","pause")
            mp?.pause()
        }

        view?.SeekBarFrag?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mp?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

            view?.note?.setOnClickListener {
            val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialog_note,null)
            val dialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                .setTitle("Donne une note !")
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
        URLfile = ""


    }

    fun initialiseSeekBar(){
        view?.SeekBarFrag?.max = mp!!.duration
        var max = mp!!.duration
        var pos = 0
        var div:Long = (100/max*1000).toLong()
        seekbarcoroutine = GlobalScope.launch {
            while (pos != max){
                if(nouvellemusique==false){
                    try {
                        mp?.let {
                            view?.SeekBarFrag?.progress = it.currentPosition
                            pos = it.currentPosition
                        }
                    }catch (e: Exception){
                    }
                    delay(130)
                }
                if (nouvellemusique==true){
                    Log.i("BREAK", "BREAK")
                    view?.SeekBarFrag?.progress = 0
                    break
                }
            }
            view?.SeekBarFrag?.progress = 0
            //delay(130)
            view?.SeekBarFrag?.progress = 0
        }

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
                if (fonction == "getNbMax"){
                    Log.i("getNbMax",response)
                    nbMax = response.toInt()
                }
                if(fonction == "artiste"){
                    Log.i("Debug","JE SUIS LA")
                    Log.i("Artiste",response)
                    artiste = response
                }
                else{
                    file = response
                    Log.i("MusiqueServer",file)
                    toServerLogin(0,"artiste","",file)
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

