package com.example.daviviendabank

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class Registrar : AppCompatActivity() {

    // Variables
    private lateinit var txtBackReg: TextView
    private lateinit var txtSignIn: TextView
    private lateinit var txtName: EditText
    private lateinit var txtEmail: EditText
    private lateinit var spinnerCity: Spinner // Cambiado de EditText a Spinner
    private lateinit var txtPhone: EditText
    private lateinit var txtId: EditText
    private lateinit var txtPassword: EditText
    private lateinit var txtPassword2: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        // Inicialización de vistas
        txtBackReg = findViewById(R.id.txtBackReg)
        txtSignIn = findViewById(R.id.txtSignIn)
        txtName = findViewById(R.id.txtNombre)
        txtEmail = findViewById(R.id.txtEmail)
        spinnerCity = findViewById(R.id.ListaCiudades) // ID del Spinner
        txtPhone = findViewById(R.id.txtPhone)
        txtId = findViewById(R.id.txtId)
        txtPassword = findViewById(R.id.txtPass)
        txtPassword2 = findViewById(R.id.txtPass2)
        btnRegister = findViewById(R.id.btnIngresar)

        // Configurar el Spinner
        setupCitySpinner()

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

    private fun setupCitySpinner() {
        // Crear un array con el hint como primer elemento
        val citiesWithHint = mutableListOf("Ciudad")
        citiesWithHint.addAll(resources.getStringArray(R.array.Arreglo_ciudades))
        
        ArrayAdapter(
            this,
            R.layout.elemento_ciudad, // Layout para el elemento seleccionado
            citiesWithHint
        ).also { adapter ->
            // Layout para el dropdown
            adapter.setDropDownViewResource(R.layout.desplegable_elemento_ciudad)
            spinnerCity.adapter = adapter
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
        val city = spinnerCity.selectedItem.toString() // Obtener el item seleccionado del Spinner

        if (id.isEmpty() || pass.isEmpty() || pass2.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() || city.isEmpty() || city == "Ciudad") {
            alertaCamposVacios()
            return
        }

        if (pass != pass2) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        val success = DatosUsuarios.registerUser(id, pass, name, email, phone, city)

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
