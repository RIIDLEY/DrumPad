package com.example.drumpad

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.retour
import kotlinx.android.synthetic.main.activity_mes_creations.*
import kotlinx.android.synthetic.main.dialog_view.*
import java.io.File

class MesCreations : AppCompatActivity() {

    private var mp: MediaPlayer? = null
    private var currentSong: MutableList<Int> = mutableListOf(R.raw.cymbal1)
    private val ListeFichier: ArrayList<String> = ArrayList<String>()
    private var sizeListe: Int = 0
    private var idOnPlay: Int = 0
    private var titre: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mes_creations)

        val wallpaperDirectory = File("/storage/emulated/0/DrumPadRec/")
        wallpaperDirectory.mkdirs()

        retour.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        getFichier()
        Log.i("Fichier",ListeFichier[0])
        controlSound(ListeFichier[0])

        skip.setOnClickListener {
            if (mp!==null){
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
            controlSound(ListeFichier[idOnPlay])
        }

        back.setOnClickListener {
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
            controlSound(ListeFichier[idOnPlay])
        }

    }



    private fun getFichier(){
        File("/storage/emulated/0/DrumPadRec").list().forEach {
            ListeFichier.add("/storage/emulated/0/DrumPadRec/"+it)
        }
        sizeListe = ListeFichier.size
    }

    private fun controlSound(File: String){

        for(i in 31..File.length-5){
            titre+=File[i]
        }
        titreMusique.text = titre
        titre = ""

        start.setOnClickListener {
            if (mp==null) {
                mp = MediaPlayer()
                mp?.setDataSource(File)
                mp?.prepare()
                Log.d("MesCreations", "ID:${mp!!.audioSessionId}")
            }
               // initSeekBar()

            mp?.start()
            Log.d("MesCreations","Durée: ${mp!!.duration/1000} seconds")
        }

        pause.setOnClickListener {
            mp?.pause()
            Log.d("MesCreations","Je suis en pause: ${mp!!.currentPosition/1000} seconds")
        }

        stop.setOnClickListener {
            mp?.stop()
            mp?.reset()
            mp?.release()
            mp = null
        }

        upload.setOnClickListener {
            UploadUtility(this).uploadFile(File)
        }



        //   SeekBar.setOnSeekBarChangeListener(object )
        }

    }
