package com.example.daviviendabank

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Menuprin : AppCompatActivity() {

    // Variables
    private lateinit var txtSaldo2: TextView
    private lateinit var txtUser: TextView
    private lateinit var btnProfile: Button
    private lateinit var btnRetiros: Button
    private lateinit var btnTransfer: Button
    private lateinit var btnConsultas: Button
    private lateinit var btnDepositar: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menuprin)

        // Inicialización de vistas
        btnProfile = findViewById(R.id.btnProfile)
        btnRetiros = findViewById(R.id.btnRetiros)
        btnTransfer = findViewById(R.id.btnTransfer)
        btnConsultas = findViewById(R.id.btnConsultas)
        btnDepositar = findViewById(R.id.btnDepositar)
        txtUser = findViewById(R.id.txtUser)
        txtSaldo2 = findViewById(R.id.txtSaldo2)

        // Llamamos las funciones
        cargarSaludoUsuario()
        cargarSaldo2()

        // Listeners
        btnProfile.setOnClickListener {
            val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
            val username = sharedPreferences.getString("username", null)

            if (username != null) {
                val intent = Intent(this, Cuenta::class.java)
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

    // 🚀 Se ejecuta cada vez que la Activity vuelve al frente
    override fun onResume() {
        super.onResume()
        cargarSaldo2()
    }

    // Función para cargar saludo
    private fun cargarSaludoUsuario() {
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)

        if (username != null) {
            val userData = BankData.getUserData(username)
            if (userData != null) {
                val name = userData[1] // Nombre completo
                txtUser.text = "Hola, $name"
            } else {
                txtUser.text = "Hola, Usuario"
            }
        } else {
            txtUser.text = "Hola, Usuario"
        }
    }

    // Función para cargar saldo
    private fun cargarSaldo2() {
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)
        if (username != null) {
            val userData = BankData.getUserData(username)
            if (userData != null) {
                val balance = userData[2].toDoubleOrNull() ?: 0.0
                txtSaldo2.text = formatCurrency(balance)
            } else {
                txtSaldo2.text = "-"
            }
        }
    }

    // Formato de moneda
    private fun formatCurrency(amount: Double): String {
        return java.text.NumberFormat.getCurrencyInstance(java.util.Locale("es", "CO"))
            .format(amount)
    }
}
