package com.example.daviviendabank

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class Login : AppCompatActivity() {

    //Variables
    private lateinit var txtBack:TextView
    private lateinit var edtuser:EditText
    private lateinit var edtpassw: EditText
    private lateinit var btnlogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //datos del xml
        txtBack = findViewById(R.id.txtBack);
        edtuser = findViewById(R.id.edittxtDoc);
        edtpassw = findViewById(R.id.editTextPassw);
        btnlogin = findViewById(R.id.btnIniciarSesion);

        //Funciones
        txtBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() //para volver a la pantalla anterior
        }

        btnlogin.setOnClickListener {
            login()
        }
    }

    private fun alertaLoginFallido() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error de inicio de sesión")
        builder.setMessage("Usuario o contraseña incorrectos.")
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun login() {
        val user = edtuser.text.toString().trim()
        val pass = edtpassw.text.toString().trim()

        if (user.isEmpty() || pass.isEmpty()) {
            alertaLoginFallido()
            return
        }

        if (DatosUsuarios.login(user, pass)) {
            // Guardar el usuario en SharedPreferences
            val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("username", user) // Guardamos el nombre de usuario
            editor.apply() // Aplicamos los cambios

            // Redirigir al menú principal
            val intent = Intent(this, Menuprin::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
            finish()
        } else {
            alertaLoginFallido()
        }
    }
}
