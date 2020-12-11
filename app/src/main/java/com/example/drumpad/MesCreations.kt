package com.example.drumpad

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_mes_creations.retour
import kotlinx.android.synthetic.main.activity_mes_creations.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Files
import java.util.*
import kotlin.collections.ArrayList


class MesCreations : AppCompatActivity() {

    var seekbarcoroutine: Job? = null
    var mp: MediaPlayer? = null
    val ListeFichier: ArrayList<String> = ArrayList<String>()
    var sizeListe: Int = 0
    var idOnPlay: Int = 0
    var titre: String = ""
    var titreActuel: String = ""
    val serverAPIURL: String = "http://lahoucine-hamsek.site/Drumpad.php"
    var nouvellemusique: Boolean = false
    var volleyRequestQueue: RequestQueue? = null
    var rep: String = ""
    var isInDB: Boolean = false
    lateinit var sharedPreferences: SharedPreferences
    var namefile: String = ""

    override fun onStop() {
        super.onStop()
        if (mp!==null){
            mp?.stop()
            mp?.reset()
            mp?.release()
            mp = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mp!==null){
            mp?.stop()
            mp?.reset()
            mp?.release()
            mp = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mes_creations)

        val wallpaperDirectory = File("/storage/emulated/0/DrumPadRec/")
        wallpaperDirectory.mkdirs()

        retour.setOnClickListener {
            val intent = Intent(this, Accueil::class.java)
            startActivity(intent)
        }

        getFichier()

        if(!ListeFichier.isEmpty()){
            Log.i("Fichier", ListeFichier[0])
            controlSound(ListeFichier[0])
        }
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
            if(!ListeFichier.isEmpty()) {
                controlSound(ListeFichier[idOnPlay])
            }
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
            if(!ListeFichier.isEmpty()) {
                controlSound(ListeFichier[idOnPlay])
            }
        }

    }

    private fun getFichier(){
        ListeFichier.clear()
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
        if (sharedPreferences.getString("Login", "")?.isNotEmpty()!!) {
            toServerLogin(titre+".mp3",sharedPreferences.getString("Login", "")!!,"isInDBMusique")
        }
        titreMusique.text = titre.replace("-"," ",true)
        titreActuel = titre
        titre = ""

        start.setOnClickListener {
            nouvellemusique=false
            if (mp==null) {
                mp = MediaPlayer()
                mp?.setDataSource(File)
                Log.i("DataSource", File)
                mp?.prepare()
                Log.d("MesCreations", "ID:${mp!!.audioSessionId}")
            }
                mp?.start()
                initialiseSeekBar()

            Log.d("MesCreations", "Durée: ${mp!!.duration / 1000} seconds")
        }

        pause.setOnClickListener {
            mp?.pause()
            Log.d("MesCreations", "Je suis en pause: ${mp!!.currentPosition / 1000} seconds")
        }

        upload.setOnClickListener {
            if(!isInDB){
                if (sharedPreferences.getString("Login", "")?.isNotEmpty()!!) {
                    Log.i("UploadFileName",File)
                    UploadUtility(this).uploadFile(File)
                    toServerLogin("$titreActuel.mp3", sharedPreferences.getString("Login", "")!!,"insertMusique")
                }else{
                    val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
                        .setTitle("")
                        .setMessage("Vous devez avoir un compte pour upload votre musique.\nRendez vous dans l'onglet Communauté")
                        .setNegativeButton("Ok",null)
                    dialog.show()
                }
            }else{
                Toast.makeText(this, "Un autre artiste a deja mis en ligne \nune musique avec le meme nom", Toast.LENGTH_SHORT).show()
            }

        }

        remove.setOnClickListener {
            val monfichier: File = File(File)
                if(monfichier.delete()){
                    Toast.makeText(this, "Musique supprimé", Toast.LENGTH_SHORT).show()
                    getFichier()
                    SeekBar.progress = 0
                    nouvellemusique=true
                    if (mp!==null){
                        SeekBar.progress = 0
                        mp?.stop()
                        mp?.reset()
                        mp?.release()
                        mp = null
                    }
                    getFichier()
                    idOnPlay=0
                    SeekBar.progress = 0
                    if(!ListeFichier.isEmpty()) {
                        controlSound(ListeFichier[idOnPlay])
                    }
                }

        }

        rename.setOnClickListener {
            val monfichier: File = File(File)
            val viewDialog: View = LayoutInflater.from(this).inflate(R.layout.alert_dialog_rename,null)
            val text: EditText = viewDialog.findViewById<EditText>(R.id.titre)

            val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
                .setPositiveButton("Renommer"){dialog, which ->
                    Log.i("Nom fichier",text.text.toString())
                    Log.i("contains",ListeFichier.contains(text.text.toString()+".mp3").toString())
                    namefile = text.text.toString().replace(" ", "-",true)
                    if(!ListeFichier.contains(namefile+".mp3")){
                        var nouveauFichier: File = File("/storage/emulated/0/DrumPadRec/"+namefile+".mp3")
                        if(monfichier.renameTo(nouveauFichier)){
                            Log.i("RENAME","OUI")
                        }else{
                            Log.i("RENAME","NON")
                        }
                        getFichier()
                        idOnPlay=0
                        SeekBar.progress = 0
                        if(!ListeFichier.isEmpty()) {
                            controlSound(ListeFichier[idOnPlay])
                        }
                    }else{
                        Toast.makeText(this, "Ce nom est déjà utilisé", Toast.LENGTH_SHORT).show()
                    }
                }
                .setTitle("Merci d'entrer un nouveau titre")
                .setView(viewDialog)
            dialog.show()
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

    fun toServerLogin(musique: String, createur: String, fonction: String){
        volleyRequestQueue = Volley.newRequestQueue(this)
        val parameters: MutableMap<String, String> = HashMap()
        parameters.put("musique", musique)
        parameters.put("createur", createur)
        parameters.put("fonction", fonction)
        val strReq: StringRequest = object : StringRequest(
            Method.POST, serverAPIURL,
            Response.Listener { response ->
                Log.i("toServeur", "Send")
                if(fonction=="isInDBMusique"){
                    Log.i("DEBUG","JE SUIS LA")
                    isInDB = response.toBoolean()
                    Log.i("isInDBMusique", response)
                }else{
                    rep = response
                }
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
