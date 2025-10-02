package com.example.daviviendabank

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegistroExitoso : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_exitoso)

        val txtMensaje: TextView = findViewById(R.id.txtMssWelcome)
        val btnContinuar: Button = findViewById(R.id.btnContinuar)

        txtMensaje.text = "¡Gracias por unirte, ${intent.getStringExtra("nombre")}! Esperamos que disfrutes esta nueva oportunidad!"
        btnContinuar.setOnClickListener { finish() }



    }
}