package com.example.daviviendabank

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class Depositos : AppCompatActivity() {

    //Variables
    private lateinit var txtBackDepositos: TextView
    private lateinit var txtDeposito: EditText
    private lateinit var btnDeposito: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_depositos)

        // Inicialización de vistas
        txtBackDepositos = findViewById(R.id.txtBackDepositos)
        txtDeposito = findViewById(R.id.txtDeposito)
        btnDeposito = findViewById(R.id.btnDeposito)
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)

        // Listeners
        txtBackDepositos.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnDeposito.setOnClickListener {
            realizarDeposito()
        }
    }

    private fun realizarDeposito() {
        val montoStr = txtDeposito.text.toString().trim()
        val username = sharedPreferences.getString("username", null) ?: run {
            showToast("Sesión expirada")
            return
        }

        if (montoStr.isEmpty()) {
            showToast("Ingrese un monto válido")
            return
        }

        val monto = montoStr.toDoubleOrNull() ?: run {
            showToast("Monto inválido")
            return
        }

        if (monto < 10000) {
            showToast("El monto mínimo es \$10,000")
            return
        }

        if (monto > 10000000) {
            showToast("El monto máximo es \$10,000,000")
            return
        }

        val (success, message) = DatosUsuarios.performDeposit(username, monto)

        if (success) {
            val nuevoSaldo = message.toDoubleOrNull() ?: 0.0
            showToast("Depósito exitoso! Nuevo saldo: ${formatCurrency(nuevoSaldo)}")
            txtDeposito.text.clear()
        } else {
            showToast("Error: $message")
        }
    }

    private fun formatCurrency(amount: Double): String {
        return NumberFormat.getCurrencyInstance(Locale("es", "CO")).format(amount)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}