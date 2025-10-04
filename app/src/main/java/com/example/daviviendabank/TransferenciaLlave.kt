package com.example.daviviendabank

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TransferenciaLlave : AppCompatActivity() {

    private lateinit var txtLlave: EditText
    private lateinit var txtValor: EditText
    private lateinit var btnTransferir: Button
    private lateinit var txtBack: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transferencia_llave)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        txtLlave = findViewById(R.id.txtLlave)
        txtValor = findViewById(R.id.txtValor)
        btnTransferir = findViewById(R.id.btnTransLlave)
        txtBack = findViewById(R.id.txtBackTransLlave)
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
    }

    private fun setupListeners() {
        txtBack.setOnClickListener { finish() }
        btnTransferir.setOnClickListener { realizarTransferencia() }
    }

    private fun realizarTransferencia() {
        val destinationKey = txtLlave.text.toString().trim()
        val amountText = txtValor.text.toString().trim()
        val originId = sharedPreferences.getString("username", null)

        if (originId == null) {
            mostrarMensaje("Error: Sesión no encontrada.")
            return
        }

        if (destinationKey.isEmpty() || amountText.isEmpty()) {
            mostrarMensaje("Todos los campos son obligatorios.")
            return
        }

        val amount = amountText.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            mostrarMensaje("El monto debe ser un número mayor a cero.")
            return
        }

        val (success, message) = DatosUsuarios.performTransferWithKey(originId, destinationKey, amount)

        mostrarMensaje(message)

        if (success) {
            // Redirigir al historial
            val intent = Intent(this, Historial::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}
