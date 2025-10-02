package com.example.daviviendabank

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Menuprin : AppCompatActivity() {

    //Variables

    private lateinit var txtBackMenuprin: TextView

    private lateinit var btnProfile: Button
    private lateinit var btnRetiros: Button
    private lateinit var btnTransfer: Button
    private lateinit var btnConsultas: Button
    private lateinit var btnDepositar: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menuprin)

        // Inicialización de vistas
        txtBackMenuprin = findViewById(R.id.txtBackMenuprin)
        btnProfile = findViewById(R.id.btnProfile)
        btnRetiros = findViewById(R.id.btnRetiros)
        btnTransfer = findViewById(R.id.btnTransfer)
        btnConsultas = findViewById(R.id.btnConsultas)
        btnDepositar = findViewById(R.id.btnDepositar)

        // Listeners
        txtBackMenuprin.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnProfile.setOnClickListener {
            //startActivity(Intent(this, Perfil::class.java))
            val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
            val username = sharedPreferences.getString("username", null)

            if (username != null) {
                val intent = Intent(this, Perfil::class.java)
                intent.putExtra("user", username) // Pasamos el usuario al Perfil
                startActivity(intent)
            } else {
                Toast.makeText(this, "No hay sesión activa", Toast.LENGTH_SHORT).show()
            }
        }

        btnRetiros.setOnClickListener {
                startActivity(Intent(this, Retiros::class.java))
        }

        btnTransfer.setOnClickListener {
            startActivity(Intent(this, Tranferencia::class.java))
        }

        btnConsultas.setOnClickListener {
            startActivity(Intent(this, Consultas::class.java))
        }

        btnDepositar.setOnClickListener {
            startActivity(Intent(this, Depositos::class.java))
        }

    }
}