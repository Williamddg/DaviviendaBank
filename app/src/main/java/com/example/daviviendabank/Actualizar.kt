package com.example.daviviendabank

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class Actualizar : AppCompatActivity() {

    private lateinit var txtNombre: EditText
    private lateinit var txtEmail: EditText
    private lateinit var spinnerCity: Spinner
    private lateinit var txtId: EditText
    private lateinit var txtPhone: EditText
    private lateinit var btnActualizar: Button
    private lateinit var txtBack: TextView

    private var originalId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar)

        initViews()
        loadInitialData()
        setupClickListeners()
    }

    private fun initViews() {
        txtNombre = findViewById(R.id.txtNombre)
        txtEmail = findViewById(R.id.txtEmail)
        spinnerCity = findViewById(R.id.ListaCiudades)
        txtId = findViewById(R.id.txtId)
        txtPhone = findViewById(R.id.txtPhone)
        btnActualizar = findViewById(R.id.btnActualizar)
        txtBack = findViewById(R.id.txtBackReg) // Corregido
    }

    private fun loadInitialData() {
        originalId = intent.getStringExtra("identificacion") ?: ""
        if (originalId.isEmpty()) {
            Toast.makeText(this, "Error: No se pudo cargar el usuario", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val userData = DatosUsuarios.getUserData(originalId)
        if (userData == null) {
            Toast.makeText(this, "Error: Datos de usuario no encontrados", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Formato: [0: password, 1: fullName, 2: balance, 3: email, 4: phone, 5: city]
        txtNombre.setText(userData[1])
        txtEmail.setText(userData[3])
        txtId.setText(originalId)
        txtPhone.setText(userData[4])
        setupCitySpinner(userData[5])
    }

    private fun setupCitySpinner(currentCity: String?) {
        val cities = resources.getStringArray(R.array.Arreglo_ciudades).toMutableList()
        val adapter = ArrayAdapter(this, R.layout.elemento_ciudad, cities)
        adapter.setDropDownViewResource(R.layout.desplegable_elemento_ciudad)
        spinnerCity.adapter = adapter

        if (currentCity != null) {
            val cityPosition = adapter.getPosition(currentCity)
            if (cityPosition >= 0) {
                spinnerCity.setSelection(cityPosition)
            }
        }
    }

    private fun setupClickListeners() {
        btnActualizar.setOnClickListener {
            updateUserData()
        }
        txtBack.setOnClickListener {
            finish()
        }
    }

    private fun updateUserData() {
        val newId = txtId.text.toString().trim()
        val newName = txtNombre.text.toString().trim()
        val newEmail = txtEmail.text.toString().trim()
        val newPhone = txtPhone.text.toString().trim()
        val newCity = spinnerCity.selectedItem.toString()

        if (newId.isEmpty() || newName.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty() || newCity.isEmpty()) {
            showResultDialog("Error de actualización", "Todos los campos son obligatorios.")
            return
        }

        val (success, message) = DatosUsuarios.updateUser(originalId, newId, newName, newEmail, newPhone, newCity)

        showResultDialog(if (success) "Éxito" else "Error de actualización", message)

        if (success) {
            // Opcional: podrías cerrar esta activity y refrescar la anterior
            // finish()
        }
    }

    private fun showResultDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
                if (title == "Éxito") {
                    finish() // Vuelve a la pantalla de Datos si la actualización fue exitosa
                }
            }
            .show()
    }
}