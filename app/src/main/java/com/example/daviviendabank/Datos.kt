package com.example.daviviendabank

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Datos : AppCompatActivity() {

    private lateinit var txtVolver: TextView
    private lateinit var txtUser3: TextView
    private lateinit var txtCelular: TextView
    private lateinit var txtCorreo: TextView
    private lateinit var txtIdentificacion: TextView
    private lateinit var txtCiudad: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_datos)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        loadUserData()
        setupClickListeners()
    }

    private fun initViews() {
        txtVolver = findViewById(R.id.txtBack)
        txtUser3 = findViewById(R.id.txtUser3)
        txtCelular = findViewById(R.id.txtCelular)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtIdentificacion = findViewById(R.id.txtIdentificacion)
        txtCiudad = findViewById(R.id.txtCiudad)
    }

    private fun loadUserData() {
        val nombre = intent.getStringExtra("nombre")
        val celular = intent.getStringExtra("celular")
        val correo = intent.getStringExtra("correo")
        val identificacion = intent.getStringExtra("identificacion")
        val ciudad = intent.getStringExtra("ciudad")

        txtUser3.text = nombre
        txtCelular.text = celular
        txtCorreo.text = correo
        txtIdentificacion.text = identificacion
        txtCiudad.text = ciudad
    }

    private fun setupClickListeners() {
        txtVolver.setOnClickListener {
            finish()
        }
    }
}
