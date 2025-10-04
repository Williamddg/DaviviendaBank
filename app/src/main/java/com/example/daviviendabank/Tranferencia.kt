package com.example.daviviendabank

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Tranferencia : AppCompatActivity() {

    //Variables
    private lateinit var txtBackTrans: TextView
    private lateinit var txtNumeroCuenta: EditText
    private lateinit var txtMonto: EditText
    private lateinit var btnTransferir: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tranferencia)

        // Inicialización de vistas
        txtBackTrans = findViewById(R.id.txtBackTrans)
        txtNumeroCuenta = findViewById(R.id.txtNumerodeCuenta)
        txtMonto = findViewById(R.id.txtValor)
        btnTransferir = findViewById(R.id.btnTrans)
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)

        // Listeners
        txtBackTrans.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnTransferir.setOnClickListener {
            realizarTransferencia()
        }
    }

    private fun realizarTransferencia() {
        val cuentaDestino = txtNumeroCuenta.text.toString().trim()
        val montoTexto = txtMonto.text.toString().trim()
        val usuario = sharedPreferences.getString("username", "") ?: ""

        if (usuario.isEmpty()) {
            mostrarMensaje("Error: No se encontró el usuario de origen")
            return
        }

        if (cuentaDestino.isEmpty()) {
            mostrarMensaje("Ingresa un número de cuenta de destino")
            return
        }

        if (montoTexto.isEmpty()) {
            mostrarMensaje("Ingresa un monto")
            return
        }

        val monto = montoTexto.toDoubleOrNull()
        if (monto == null || monto <= 0) {
            mostrarMensaje("El monto debe ser un número mayor a cero")
            return
        }

        if (usuario == cuentaDestino) {
            mostrarMensaje("No puedes transferirte a ti mismo")
            return
        }

        val (success, message) = BankData.performTransfer(usuario, cuentaDestino, monto)

        if (success) {
            mostrarMensaje("Transferencia exitosa!")
            txtMonto.text.clear()
            txtNumeroCuenta.text.clear()
        } else {
            mostrarMensaje("Error: $message")
        }
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
