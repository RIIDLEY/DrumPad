package com.example.drumpad


import android.graphics.Insets.add
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.OneShotPreDrawListener.add
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_commu.*
import java.util.HashMap


class Commu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commu)

       val fragement = FirstFragement()
        val fragement2 = SecondFragement()


        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragement)
            commit()}

        button.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment, fragement)
                commit()}
        }

        button2.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment, fragement2)
                commit()}
        }
    }


}