package com.example.drumpad

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.retour
import kotlinx.android.synthetic.main.activity_mes_creations.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class MesCreations : AppCompatActivity() {

    var seekbarcoroutine: Job? = null
    private var mp: MediaPlayer? = null
    private var currentSong: MutableList<Int> = mutableListOf(R.raw.cymbal1)
    private val ListeFichier: ArrayList<String> = ArrayList<String>()
    private var sizeListe: Int = 0
    private var idOnPlay: Int = 0
    private var titre: String = ""
    private var titreActuel: String = ""
    val serverAPIURL: String = "http://lahoucine-hamsek.site/coucou.php"
    lateinit var sharedPreferences: SharedPreferences
    private var nouvellemusique: Boolean = false
    private var isStop: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mes_creations)

        val wallpaperDirectory = File("/storage/emulated/0/DrumPadRec/")
        wallpaperDirectory.mkdirs()

        retour.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        getFichier()
        Log.i("Fichier", ListeFichier[0])
        controlSound(ListeFichier[0])

        skip.setOnClickListener {
            SeekBar.progress = 0
            nouvellemusique=true
            if (mp!==null){
                SeekBar.progress = 0
                mp?.stop()
                mp?.reset()
                mp?.release()
                mp = null
            }
            if (idOnPlay==sizeListe-1){
                idOnPlay=0
            }else{
                idOnPlay+=1
            }
            SeekBar.progress = 0
            controlSound(ListeFichier[idOnPlay])
        }

        back.setOnClickListener {
            SeekBar.progress = 0
            nouvellemusique=true
            if (mp!==null){
                mp?.stop()
                mp?.reset()
                mp?.release()
                mp = null
            }
            if (idOnPlay==0){
                idOnPlay=sizeListe-1
            }else{
                idOnPlay-=1
            }
            SeekBar.progress = 0
            controlSound(ListeFichier[idOnPlay])
        }

    }

    private fun getFichier(){
        File("/storage/emulated/0/DrumPadRec").list().forEach {
            ListeFichier.add("/storage/emulated/0/DrumPadRec/" + it)
        }
        sizeListe = ListeFichier.size
    }

    private fun controlSound(File: String){
        SeekBar.progress = 0

        for(i in 31..File.length-5){
            titre+=File[i]
        }
        titreMusique.text = titre
        titreActuel = titre;
        titre = ""

        start.setOnClickListener {
            nouvellemusique=false
            if (mp==null) {
                mp = MediaPlayer()
                mp?.setDataSource(File)
                Log.i("DataSource", File)
                //mp?.setOnPreparedListener(OnPreparedListener {
                  //  mp?.start()
                    //initialiseSeekBar()
                //})
                mp?.prepare()
                Log.d("MesCreations", "ID:${mp!!.audioSessionId}")
            }
            // else{
                mp?.start()
                initialiseSeekBar()
            //}



            Log.d("MesCreations", "Durée: ${mp!!.duration / 1000} seconds")
        }

        pause.setOnClickListener {
            mp?.pause()
            Log.d("MesCreations", "Je suis en pause: ${mp!!.currentPosition / 1000} seconds")
        }
/*
        stop.setOnClickListener {
                isStop=true
                SeekBar.progress = 0
                mp?.stop()
                mp?.reset()
                mp?.release()
                mp=null

        }*/

        upload.setOnClickListener {
            if (sharedPreferences.getString("Login", "")?.isNotEmpty()!!) {
                UploadUtility(this).uploadFile(File)
                toServerLogin("$titreActuel.mp3", sharedPreferences.getString("Login", "")!!)
            }else{
                val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
                    .setTitle("Vous devez avoir un compte pour upload votre musique")
                dialog.show()
            }
        }

        SeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mp?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        }

    fun initialiseSeekBar(){
        SeekBar.max = mp!!.duration
        var max = mp!!.duration
        var pos = 0
        var div:Long = (100/max*1000).toLong()
        seekbarcoroutine = GlobalScope.launch {
           while (pos != max){
               if(nouvellemusique==false){
                   try {
                       mp?.let {
                           SeekBar.progress = it.currentPosition
                           pos = it.currentPosition
                       }
                   }catch (e: Exception){
                   }
                   delay(130)
               }
               if (nouvellemusique==true){
                   Log.i("BREAK", "BREAK")
                   SeekBar.progress = 0
                   break
               }
                }
            SeekBar.progress = 0
            //delay(130)
            SeekBar.progress = 0
            }

        }

    fun toServerLogin(musique: String, createur: String){
        volleyRequestQueue = Volley.newRequestQueue(this)
        val parameters: MutableMap<String, String> = HashMap()
        parameters.put("musique", musique)
        parameters.put("createur", createur)
        parameters.put("fonction", "insertMusique")
        val strReq: StringRequest = object : StringRequest(
            Method.POST, serverAPIURL,
            Response.Listener { response ->
                Log.i("toServeur", "Send")
                Toast.makeText(this, "Reponse $response", Toast.LENGTH_SHORT).show()
                rep = response
            },
            Response.ErrorListener { volleyError -> // error occurred
                Log.i("toServeur", "Error")
            }) {

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
