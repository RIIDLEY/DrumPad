package com.example.drumpad

import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.media.SoundPool
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_drumpad.*
import java.io.File
import java.io.IOException


class DrumPad : AppCompatActivity() {


    var mediaPlayer1: MediaPlayer? = null
    var mediaPlayer2: MediaPlayer? = null
    var fichiermp3: String? = null
    var mediaRecorder: MediaRecorder? = null
    var recEnCours: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        val wallpaperDirectory = File("/storage/emulated/0/DrumPadRec/")
        wallpaperDirectory.mkdirs()
        mediaPlayer1 = MediaPlayer.create(this, R.raw.clapanalog)
        mediaPlayer2 = MediaPlayer.create(this, R.raw.kickelectro01)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drumpad)


        retour.setOnClickListener {
            val intent = Intent(this, Accueil::class.java)
            startActivity(intent)
        }

        mamusique.setOnClickListener {
            val intent = Intent(this, MesCreations::class.java)
            startActivity(intent)
        }

        recbtn.setOnClickListener {
                val viewDialog: View = LayoutInflater.from(this).inflate(R.layout.alert_dialog_rec,null)
                val text: EditText = viewDialog.findViewById<EditText>(R.id.titre)
                val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
                    .setPositiveButton("Commencer à enregistre"){dialog, which ->
                        Log.i("test",text.text.toString())
                        prepareRecording(text.text.toString())
                        startRecording()
                    }
                    .setTitle("Merci d'entrer un nom pour\n l'enregistrement")
                    .setView(viewDialog)
                if (recEnCours){
                    Log.i("recbtn","non")
                    stopRecording()
                    recEnCours = false
                }else{
                    dialog.show()
                    Log.i("recbtn","oui")
                    recEnCours = true
                }

        }

    }


    fun playSound1(view: View) {
        mediaPlayer1?.start()
    }

    fun playSound2(view: View) {
        mediaPlayer2?.start()
    }

    fun playSound3(view: View) {
        mediaPlayer1?.start()
    }

    fun playSound4(view: View) {
        mediaPlayer1?.start()
    }

    fun playSound5(view: View) {
        mediaPlayer1?.start()
    }

    fun playSound6(view: View) {
        mediaPlayer1?.start()
    }

    fun playSound7(view: View) {
        mediaPlayer1?.start()
    }

    fun playSound8(view: View) {
        mediaPlayer1?.start()
    }

    fun playSound9(view: View) {
        mediaPlayer1?.start()
    }


    //--------------------------

    fun prepareRecording(nomfichier: String){
        Log.i("prepareRecording","ok")

        mediaRecorder = MediaRecorder()

        fichiermp3 = Environment.getExternalStorageDirectory().absolutePath + "/DrumPadRec/" + nomfichier + ".mp3"

        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setAudioEncodingBitRate(128000)
        mediaRecorder?.setAudioSamplingRate(44100)
        mediaRecorder?.setOutputFile(fichiermp3)
    }


     fun startRecording() {
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            Toast.makeText(this, "A vous de jouer", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

     fun stopRecording(){
            mediaRecorder?.stop()
            mediaRecorder?.release()
            Toast.makeText(this, "C'est dans la boite", Toast.LENGTH_SHORT).show()
    }

}