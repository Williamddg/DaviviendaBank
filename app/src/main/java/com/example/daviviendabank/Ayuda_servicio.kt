package com.example.daviviendabank

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Ayuda_servicio : AppCompatActivity() {

    private lateinit var txtVolver: TextView
    private lateinit var txtNecesitoAyuda: TextView
    private lateinit var txtCanalesAtencion: TextView
    private lateinit var txtDefensorConsumidor: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ayuda_servicio)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        txtVolver = findViewById(R.id.txtBack)
        txtNecesitoAyuda = findViewById(R.id.txtLlamar)
        txtCanalesAtencion = findViewById(R.id.txtChatSoporte)
        txtDefensorConsumidor = findViewById(R.id.txtMasReclamaciones)
    }

    private fun setupClickListeners() {
        txtVolver.setOnClickListener {
            finish()
        }

        txtNecesitoAyuda.setOnClickListener {
            showToast("Próximamente: Necesito ayuda")
        }

        txtCanalesAtencion.setOnClickListener {
            showToast("Próximamente: Canales de atención")
        }

        txtDefensorConsumidor.setOnClickListener {
            showToast("Próximamente: Defensor del consumidor")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
