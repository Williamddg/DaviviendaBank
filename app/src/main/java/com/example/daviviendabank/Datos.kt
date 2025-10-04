package com.example.daviviendabank

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

    private lateinit var btnActualizar: Button
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_datos)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Guardar el ID de usuario de forma persistente en la activity
        userId = intent.getStringExtra("identificacion")

        initViews()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        // Cargar/Recargar los datos cada vez que la activity es visible
        loadUserData()
    }

    private fun initViews() {
        txtVolver = findViewById(R.id.txtBack)
        txtUser3 = findViewById(R.id.txtUser3)
        txtCelular = findViewById(R.id.txtCelular)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtIdentificacion = findViewById(R.id.txtIdentificacion)
        txtCiudad = findViewById(R.id.txtCiudad)
        btnActualizar = findViewById(R.id.btnActualizar)
    }

    private fun loadUserData() {
        if (userId == null) return

        val userData = DatosUsuarios.getUserData(userId!!)
        if (userData == null) {
            // Manejar el caso en que el usuario ya no exista
            finish()
            return
        }

        // Formato: [0: password, 1: fullName, 2: balance, 3: email, 4: phone, 5: city]
        txtUser3.text = userData[1]
        txtCelular.text = userData[4]
        txtCorreo.text = userData[3]
        txtIdentificacion.text = userId
        txtCiudad.text = userData[5]
    }

    private fun setupClickListeners() {
        txtVolver.setOnClickListener {
            finish()
        }

        btnActualizar.setOnClickListener {
            val intent = Intent(this, Actualizar::class.java).apply {
                putExtra("identificacion", txtIdentificacion.text.toString())
            }
            startActivity(intent)
        }
    }
}
