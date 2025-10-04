package com.example.daviviendabank

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class Saldo : AppCompatActivity() {

    // Variables de vistas
    private lateinit var txtBackSaldo: TextView
    private lateinit var txtNameSaldo: TextView
    private lateinit var txtSaldo: TextView
    private lateinit var btnTransSaldo: Button
    private lateinit var btnDepositarSaldo: Button
    private lateinit var btnLogoutSaldo: Button
    private lateinit var btnHistorial: Button

    // Variables de sesión
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saldo)

        // Inicialización de vistas
        txtBackSaldo = findViewById(R.id.txtBackSaldo)
        txtNameSaldo = findViewById(R.id.txtNameSaldo)
        txtSaldo = findViewById(R.id.txtSaldo)
        btnTransSaldo = findViewById(R.id.btnTransSaldo)
        btnDepositarSaldo = findViewById(R.id.btnDepositarSaldo)
        btnLogoutSaldo = findViewById(R.id.btnLogoutSaldo)
        btnHistorial = findViewById(R.id.btnHistorial)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)

        // Configurar listeners
        configurarListeners()

        // Cargar datos del usuario
        cargarDatosUsuario()
    }

    override fun onResume() {
        super.onResume()
        // Actualizar los datos cada vez que la actividad vuelve a primer plano
        cargarDatosUsuario()
    }

    private fun configurarListeners() {
        txtBackSaldo.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnTransSaldo.setOnClickListener {
            startActivity(Intent(this, Tranferencia::class.java))
        }

        btnDepositarSaldo.setOnClickListener {
            startActivity(Intent(this, Depositos::class.java))
        }

        btnHistorial.setOnClickListener {
            startActivity(Intent(this, Historial::class.java))
        }

        btnLogoutSaldo.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun cargarDatosUsuario() {
        val username = sharedPreferences.getString("username", null) ?: run {
            mostrarToast("Sesión expirada")
            finish()
            return
        }

        val userData = DatosUsuarios.getUserData(username)

        if (userData != null) {
            // userData: [0: password, 1: fullName, 2: balance, 3: email, 4: phone, 5: city]
            val fullName = userData[1]
            val balance = userData[2].toDoubleOrNull() ?: 0.0

            txtNameSaldo.text = fullName
            txtSaldo.text = formatCurrency(balance)
        } else {
            mostrarToast("Error al obtener datos del usuario")
            txtNameSaldo.text = username // Fallback
            txtSaldo.text = "-"
        }
    }

    private fun cerrarSesion() {
        sharedPreferences.edit().clear().apply()
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun formatCurrency(amount: Double): String {
        return NumberFormat.getCurrencyInstance(Locale("es", "CO")).format(amount)
    }

    private fun mostrarToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}