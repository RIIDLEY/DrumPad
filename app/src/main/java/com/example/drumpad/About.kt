package com.example.drumpad

import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement
import java.util.*


class About : AppCompatActivity() {
    private lateinit var player: MediaPlayer
    private lateinit var mediaPlayer: MediaPlayer
    private val rtspUrl = "http://lahoucine-hamsek.site/Jasmin.mp3"

//    internal var conn: Connection? = null
    internal var username = "username" // provide the username
    internal var password = "password"
    internal var status: Statement? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

//        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//        StrictMode.setThreadPolicy(policy)

        //player = MediaPlayer.create(this@About, Uri.parse("http://lahoucine-hamsek.site/Jasmin.mp3"))
        Log.i("TEST", "coucou")
        //player.start()

        }


    override fun onStart() {
        //Connection().execute()
        login()
        Log.i("TEST", "coucou1")
        super.onStart()
    }

    override fun onStop() {
       //mediaPlayer.stop()
        //mediaPlayer.release()
        super.onStop()
    }

//        private fun connectionBDD(){
//            val connectionProps = Properties()
//            connectionProps.put("root", username)
//            connectionProps.put("(Lahoucine16)", password)
//            try {
//                Class.forName("com.mysql.jdbc.Driver").newInstance()
//                conn = DriverManager.getConnection("jdbc:" + "mysql" + "://" + "lahoucine-hamsek.site" + ":" + "3306" + "/" + "", connectionProps)
//                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
//                Log.i("TEST","OK")
//            }
//            catch (ex: SQLException){
//                Log.i("TEST","Erreur")
//                ex.printStackTrace()
//            }
//
//        }

    fun getConnectionBDD() {
//        val connectionProps = Properties()
//        connectionProps.put("user", username)
//        connectionProps.put("password", password)
        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance()
            val conn = DriverManager.getConnection(
                "jdbc:mariadb://lahoucine-hamsek.site?serverTimezone=UTC",
                "root",
                "(Lahoucine16)"
            )
        } catch (ex: SQLException) {
            // handle any errors
            Log.i("Erreur", "Pas co")
            ex.printStackTrace()
        } catch (ex: Exception) {
            // handle any errors
            Log.i("Erreur", "Pas co")
            ex.printStackTrace()
        }
        Log.i("GG", "Co")
    }


    fun login(){
        val queue = Volley.newRequestQueue(this)
        val url = "https://www.google.com"

// Request a string response from the provided URL.
        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { Toast.makeText(this, "Pas OK", Toast.LENGTH_SHORT).show() })

// Add the request to the RequestQueue.
        queue.add(stringRequest)
    }
    }




class Connection : AsyncTask<Void, Void, Void>() {

    override fun doInBackground(vararg params: Void?): Void? {
        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance()
            val conn = DriverManager.getConnection(
                "jdbc:mariadb:///www.lahoucine-hamsek.site?serverTimezone=UTC",
                "root",
                "(Lahoucine16)"
            )
            Log.i("GG", "Co")
        } catch (ex: SQLException) {
            // handle any errors
            Log.i("Erreur", "Pas co")
            ex.printStackTrace()
        } catch (ex: Exception) {
            // handle any errors
            Log.i("Erreur", "Pas co")
            ex.printStackTrace()
        }


        return null
    }
    }

