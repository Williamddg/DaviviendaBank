package com.example.daviviendabank

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button

class MainActivity : AppCompatActivity() {
    //Variables
    private lateinit var btnLogin:Button
    private lateinit var btnRegistrar:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Tomamos los datos del xml
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrar = findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        btnRegistrar.setOnClickListener {
            val intent = Intent(this, Registrar::class.java)
            startActivity(intent)
        }


    }
}