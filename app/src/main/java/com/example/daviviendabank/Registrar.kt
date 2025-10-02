package com.example.daviviendabank

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class Registrar : AppCompatActivity() {

    // Variables
    private lateinit var txtBackReg: TextView
    private lateinit var txtSignIn: TextView
    private lateinit var txtName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtCity: TextView
    private lateinit var txtPhone: TextView
    private lateinit var txtId: TextView
    private lateinit var txtPassword: TextView
    private lateinit var txtPassword2: TextView
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        // Inicialización de vistas
        txtBackReg = findViewById(R.id.txtBackReg)
        txtSignIn = findViewById(R.id.txtSignIn)
        txtName = findViewById(R.id.txtNombre)
        txtEmail = findViewById(R.id.txtEmail)
        txtCity = findViewById(R.id.txtCiudad)
        txtPhone = findViewById(R.id.txtPhone)
        txtId = findViewById(R.id.txtId)
        txtPassword = findViewById(R.id.txtPass)
        txtPassword2 = findViewById(R.id.txtPass2)
        btnRegister = findViewById(R.id.btnIngresar)

        // Listeners
        txtBackReg.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        txtSignIn.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        btnRegister.setOnClickListener {
            register()
        }
    }

    private fun alertaCamposVacios() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error de registro")
        builder.setMessage("Todos los campos son obligatorios.")
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun alertaUsuarioExistente() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error de registro")
        builder.setMessage("El número de documento ya está registrado.")
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun register() {
        val id = txtId.text.toString().trim()
        val pass = txtPassword.text.toString().trim()
        val pass2 = txtPassword2.text.toString().trim()
        val name = txtName.text.toString().trim()
        val email = txtEmail.text.toString().trim()
        val phone = txtPhone.text.toString().trim()
        val city = txtCity.text.toString().trim()

        if (id.isEmpty() || pass.isEmpty() || pass2.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() || city.isEmpty()) {
            alertaCamposVacios()
            return
        }

        if (pass != pass2) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        val success = BankData.registerUser(id, pass, name, email, phone, city)

        if (success) {
            val intent = Intent(this, RegistroExitoso::class.java).apply {
                putExtra("nombre", name)
            }
            startActivity(intent)
            finish()
        } else {
            alertaUsuarioExistente()
        }
    }
}