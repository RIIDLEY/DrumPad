package com.example.drumpad

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.media.SoundPool
import android.os.Bundle
import android.os.Environment
import android.util.LayoutDirection
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class DrumPad : AppCompatActivity() {

    private var sp: SoundPool? = null
    private val soundId = 2
    var loaded = false
    var mediaPlayer1: MediaPlayer? = null
    var mediaPlayer2: MediaPlayer? = null

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    private var recordingStopped: Boolean = false
    private var recEnCours: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        mediaPlayer1 = MediaPlayer.create(this, R.raw.clapanalog)
        mediaPlayer2 = MediaPlayer.create(this, R.raw.kickelectro01)


        mediaRecorder = MediaRecorder()

        output = Environment.getExternalStorageDirectory().absolutePath + "/recording1.mp3"

        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setAudioEncodingBitRate(128000)
        mediaRecorder?.setAudioSamplingRate(44100)
        mediaRecorder?.setOutputFile(output)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login.setOnClickListener {
            val intent = Intent(this, FtpServer::class.java)
            startActivity(intent)
        }

        retour.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        recbtn.setOnClickListener {
                val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_view,null)
                val dialog: AlertDialog.Builder = AlertDialog.Builder(this,R.style.CustomDialog).setView(view)


                if (recEnCours){
                    Log.i("recbtn","non")
                    stopRecording()
                    recEnCours = false
                }else{
                    AlertDialog.Builder(this).setNegativeButton("Commencer à enregistre",DialogInterface.OnClickListener(function =  rec)).setTitle("Mettez le son de votre smartphone au maximum !").show()
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

    private fun startRecording() {
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

    private fun stopRecording(){
            mediaRecorder?.stop()
            mediaRecorder?.release()
            Toast.makeText(this, "C'est dans la boite", Toast.LENGTH_SHORT).show()
    }



    val rec = { dialog: DialogInterface, which: Int ->
        startRecording()
    }


}