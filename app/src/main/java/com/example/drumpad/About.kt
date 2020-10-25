package com.example.drumpad

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class About : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private val rtspUrl = "http://lahoucine-hamsek.site/Jasmin.mp3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        mediaPlayer = MediaPlayer()

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

        mediaPlayer.setDataSource("http://lahoucine-hamsek.site/Jasmin.mp3")

        mediaPlayer.prepare()

//        mediaPlayer.setOnPreparedListener( MediaPlayer.OnPreparedListener() {
//            fun onPrepared(mp: MediaPlayer) {
//                mp.start();
//            }})

        mediaPlayer.start()

//        val sampleUrl = "http://lahoucine-hamsek.site/Jasmin.mp3" // your URL here
//        val mediaPlayer: MediaPlayer? = MediaPlayer().apply {
//            setAudioStreamType(AudioManager.STREAM_MUSIC) //to send the object to the initialized state
//            setDataSource(sampleUrl) //to set media source and send the object to the initialized state
//            prepare() //to send the object to the prepared state, this may take time for fetching and decoding
//            start() //to start the music and send the object to started state
//        }
        }



    override fun onStop() {
        mediaPlayer.stop()
        mediaPlayer.release()

        super.onStop()
    }
    }


