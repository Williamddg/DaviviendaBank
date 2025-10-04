package com.example.daviviendabank

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Clave : AppCompatActivity() {

    private lateinit var txtBackClave: TextView
    private lateinit var txtPasswordAntigua: EditText
    private lateinit var txtPasswordNueva: EditText
    private lateinit var txtPasswordNueva2: EditText
    private lateinit var btnConfirmar: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clave)

        // Inicialización de vistas
        txtBackClave = findViewById(R.id.txtBackClave)
        txtPasswordAntigua = findViewById(R.id.txtpasswordAntigua)
        txtPasswordNueva = findViewById(R.id.txtpasswordNueva)
        txtPasswordNueva2 = findViewById(R.id.txtpasswordNueva2)
        btnConfirmar = findViewById(R.id.btnConfirmarNewPassword)
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)

        // Listeners
        txtBackClave.setOnClickListener {
            finish() // Cierra la actividad actual y vuelve a la anterior
        }

        btnConfirmar.setOnClickListener {
            cambiarClave()
        }
    }

    private fun cambiarClave() {
        val oldPass = txtPasswordAntigua.text.toString()
        val newPass = txtPasswordNueva.text.toString()
        val newPass2 = txtPasswordNueva2.text.toString()
        val username = sharedPreferences.getString("username", null)

        if (username == null) {
            showToast("Error: Sesión no encontrada.")
            return
        }

        if (oldPass.isEmpty() || newPass.isEmpty() || newPass2.isEmpty()) {
            showToast("Todos los campos son obligatorios.")
            return
        }

        if (newPass != newPass2) {
            showToast("Las contraseñas nuevas no coinciden.")
            return
        }

        if (newPass.length < 6) {
            showToast("La nueva contraseña debe tener al menos 6 caracteres.")
            return
        }

        val (success, message) = DatosUsuarios.changePassword(username, oldPass, newPass)

        showToast(message)

        if (success) {
            txtPasswordAntigua.text.clear()
            txtPasswordNueva.text.clear()
            txtPasswordNueva2.text.clear()
            finish() // Opcional: cerrar la pantalla después del éxito
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
