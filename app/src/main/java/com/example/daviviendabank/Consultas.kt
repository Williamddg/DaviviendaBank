package com.example.daviviendabank

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Consultas : AppCompatActivity() {

    //Variables
    private lateinit var txtBackConsultas: TextView
    private lateinit var btnSaldo: Button
    private lateinit var btnClave: Button
    private lateinit var btnHistorial: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_consultas)

        // Inicialización de vistas
        txtBackConsultas = findViewById(R.id.txtBackConsulta)
        btnSaldo = findViewById(R.id.btnSaldo)
        btnClave = findViewById(R.id.btnClave)
        btnHistorial = findViewById(R.id.btnHistorial)

        // Listeners
        txtBackConsultas.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnSaldo.setOnClickListener {
            startActivity(Intent(this, Saldo::class.java))
        }

        btnClave.setOnClickListener {
            startActivity(Intent(this, Clave::class.java))
        }

        btnHistorial.setOnClickListener {
            startActivity(Intent(this, Historial::class.java))
        }

    }
}