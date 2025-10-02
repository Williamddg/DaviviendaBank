package com.example.daviviendabank

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Perfil : AppCompatActivity() {

    // Variables de vistas
    private lateinit var txtBackProfile: TextView
    private lateinit var txtName: TextView
    private lateinit var txtEmailProfile: TextView
    private lateinit var txtCityProfile: TextView
    private lateinit var txtIdProfile: TextView
    private lateinit var txtPhoneProfile: TextView
    private lateinit var btnLogout: Button

    // SharedPreferences para la sesión
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Inicialización de vistas
        txtBackProfile = findViewById(R.id.txtBackProfile)
        txtName = findViewById(R.id.txtName)
        txtEmailProfile = findViewById(R.id.txtEmailProfile)
        txtCityProfile = findViewById(R.id.txtCityProfile)
        txtIdProfile = findViewById(R.id.txtIdProfile)
        txtPhoneProfile = findViewById(R.id.txtPhoneProfile)
        btnLogout = findViewById(R.id.btnLogout)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)

        // Obtener el nombre de usuario de SharedPreferences
        val username = sharedPreferences.getString("username", null) ?: run {
            redirectToLoginWithMessage("Sesión expirada")
            return
        }

        // Cargar datos del usuario
        obtenerDatosUsuario(username)

        // Configurar el botón de volver
        txtBackProfile.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Configurar el botón de cerrar sesión
        btnLogout.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun obtenerDatosUsuario(username: String) {
        val userData = BankData.getUserData(username)

        if (userData != null) {
            // userData: [0: password, 1: fullName, 2: balance, 3: email, 4: phone, 5: city]
            val name = userData[1]
            val email = userData[3]
            val city = userData[5]
            val id = username
            val phone = userData[4]

            mostrarDatosEnPantalla(name, email, city, id, phone)
        } else {
            showToast("Error al obtener datos del usuario")
        }
    }

    private fun mostrarDatosEnPantalla(
        name: String,
        email: String,
        city: String,
        user: String,
        phone: String
    ) {
        runOnUiThread {
            txtName.text = name
            txtEmailProfile.text = "Email: $email"
            txtCityProfile.text = "Ciudad: $city"
            txtIdProfile.text = "Identificación: $user"
            txtPhoneProfile.text = "Teléfono: $phone"
        }
    }

    private fun cerrarSesion() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun redirectToLoginWithMessage(message: String) {
        showToast(message)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}