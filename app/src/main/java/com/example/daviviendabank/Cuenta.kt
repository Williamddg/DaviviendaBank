package com.example.daviviendabank

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Cuenta : AppCompatActivity() {

    private lateinit var txtUser2: TextView
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var txtCerrarSesion: TextView
    private lateinit var txtMisDatos: TextView
    private lateinit var txtMisDavipuntos: TextView
    private lateinit var txtAjustesSeguridad: TextView
    private lateinit var txtAyudaServicio: TextView
    private lateinit var imgPersonal: ImageView

    private var nombre: String? = null
    private var celular: String? = null
    private var correo: String? = null
    private var identificacion: String? = null
    private var ciudad: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cuenta)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()

        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)

        val username = sharedPreferences.getString("username", null) ?: run {
            showToast("Sesión expirada")
            redirectToMain()
            return
        }

        loadUserData(username)
        setupClickListeners()
    }

    private fun initViews() {
        txtUser2 = findViewById(R.id.txtUser2)
        txtCerrarSesion = findViewById(R.id.txtCerrarSesion)
        txtMisDatos = findViewById(R.id.txtMisDatos)
        txtMisDavipuntos = findViewById(R.id.txtMisDavipuntos)
        txtAjustesSeguridad = findViewById(R.id.txtAjustesSeguridad)
        txtAyudaServicio = findViewById(R.id.txtAyudaServicio)
        imgPersonal = findViewById(R.id.imgPersonal)
    }

    private fun loadUserData(username: String) {
        val userData = DatosUsuarios.getUserData(username)
        if (userData != null) {
            nombre = userData[1]
            celular = userData[4]
            correo = userData[3]
            identificacion = username
            ciudad = userData[5]

            txtUser2.text = nombre
        } else {
            showToast("Error al obtener datos del usuario")
        }
    }

    private fun setupClickListeners() {
        txtCerrarSesion.setOnClickListener { cerrarSesion() }
        txtMisDatos.setOnClickListener {
            val intent = Intent(this, Datos::class.java).apply {
                putExtra("nombre", nombre)
                putExtra("celular", celular)
                putExtra("correo", correo)
                putExtra("identificacion", identificacion)
                putExtra("ciudad", ciudad)
            }
            startActivity(intent)
        }
        txtMisDavipuntos.setOnClickListener { startActivity(Intent(this, Davipuntos::class.java)) }
        txtAjustesSeguridad.setOnClickListener { startActivity(Intent(this, Ajustes_seguridad::class.java)) }
        txtAyudaServicio.setOnClickListener { startActivity(Intent(this, Ayuda_servicio::class.java)) }
        imgPersonal.setOnClickListener { recreate() }
    }

    private fun cerrarSesion() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        redirectToMain()
    }

    private fun redirectToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
