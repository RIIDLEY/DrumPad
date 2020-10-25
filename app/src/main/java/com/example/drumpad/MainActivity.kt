package com.example.drumpad

import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    private var sp: SoundPool? = null
    private val soundId = 2
    var loaded = false
    override fun onCreate(savedInstanceState: Bundle?) {

        var mediaPlayer: MediaPlayer? = MediaPlayer.create(this, R.raw.cymbal1)

        sp = SoundPool(2, AudioManager.STREAM_MUSIC, 1)
        sp!!.load(applicationContext, R.raw.cymbal1, 2)

            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

//            btn2.setOnClickListener {
//                mediaPlayer?.start()
//            }
        }

        fun playSound(view: View) {
                sp?.play(soundId, 1f, 1f, 0, 0, 1f)
                Toast.makeText(this, "Playing sound. . . .", Toast.LENGTH_SHORT).show()
                Log.i("TEST", "JE SUIS LA")
        }
    }
