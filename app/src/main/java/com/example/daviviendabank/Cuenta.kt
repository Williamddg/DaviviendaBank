package com.example.daviviendabank

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Cuenta : AppCompatActivity() {

    private lateinit var txtUser2: EditText
    private lateinit var sharedPreferences: SharedPreferences

    private var fullName: String = ""
    private var email: String = ""
    private var city: String = ""
    private var idUser: String = ""
    private var phone: String = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cuenta)

        // Ajuste de paddings por sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar vistas
        txtUser2 = findViewById(R.id.txtUser2)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)

        // Obtener username guardado en sesión
        val username = sharedPreferences.getString("username", null) ?: run {
            showToast("Sesión expirada")
            finish()
            return
        }

        // Cargar datos del usuario
        obtenerDatosUsuario(username)
    }

    private fun obtenerDatosUsuario(username: String) {
        val userData = BankData.getUserData(username)

        if (userData != null) {
            // userData: [0: password, 1: fullName, 2: balance, 3: email, 4: phone, 5: city]
            fullName = userData[1]
            email = userData[3]
            city = userData[5]
            idUser = username
            phone = userData[4]

            // 👇 Por ahora solo mostramos el nombre
            txtUser2.setText(fullName)
        } else {
            showToast("Error al obtener datos del usuario")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
