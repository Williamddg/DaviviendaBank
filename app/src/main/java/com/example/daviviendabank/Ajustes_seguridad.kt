package com.example.daviviendabank

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Ajustes_seguridad : AppCompatActivity() {

    private lateinit var txtVolver: TextView
    private lateinit var txtViajes: TextView
    private lateinit var txtLimites: TextView
    private lateinit var txtCambiarClave: TextView
    private lateinit var txtLimitesPSE: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ajustes_seguridad)

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
        txtViajes = findViewById(R.id.txtViajes)
        txtLimites = findViewById(R.id.txtLimites)
        txtCambiarClave = findViewById(R.id.txtCambiarClave)
        txtLimitesPSE = findViewById(R.id.txtLimitesPSE)
    }

    private fun setupClickListeners() {
        txtVolver.setOnClickListener {
            finish()
        }

        txtViajes.setOnClickListener {
            showToast("Próximamente: Cambiar clave")
        }

        txtLimites.setOnClickListener {
            showToast("Próximamente: Definir clave transaccional")
        }

        txtCambiarClave.setOnClickListener {
            showToast("Próximamente: Reportar fraude")
        }

        txtLimitesPSE.setOnClickListener {
            showToast("Próximamente: Cambiar clave")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
