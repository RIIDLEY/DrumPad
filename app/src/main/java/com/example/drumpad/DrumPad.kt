package com.example.drumpad

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*



class DrumPad : AppCompatActivity() {

    private var sp: SoundPool? = null
    private val soundId = 2
    var loaded = false
    var mediaPlayer1: MediaPlayer? = null
    var mediaPlayer2: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        mediaPlayer1 = MediaPlayer.create(this, R.raw.clapanalog)
        mediaPlayer2 = MediaPlayer.create(this, R.raw.kickelectro01)
        sp = SoundPool(2, AudioManager.STREAM_MUSIC, 1)
        sp!!.load(applicationContext, R.raw.sound00, 2)
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


//            btn2.setOnClickListener {
//                mediaPlayer?.start()
//            }
    }

    fun playSound(view: View) {
        sp?.setOnLoadCompleteListener(object : SoundPool.OnLoadCompleteListener {
            override fun onLoadComplete(soundPool: SoundPool?, mySoundId: Int, status: Int) {
                loaded = true
            }
        })
        //mediaPlayer?.start()
        if (loaded){
            sp?.play(soundId, 1f, 1f, 0, 0, 1f)
            Toast.makeText(this, "Playing sound. . . .", Toast.LENGTH_SHORT).show()
        }
        Log.i("TEST", "JE SUIS LA")
    }

    fun playSound1(view: View) {
        mediaPlayer1?.start()
    }

    fun playSound2(view: View) {
        mediaPlayer2?.start()
    }
}