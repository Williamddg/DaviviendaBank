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

class Retiros : AppCompatActivity() {

    //Variables
    private lateinit var txtBackRetiros: TextView
    private lateinit var txtRetiro: EditText
    private lateinit var btnRetirar: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retiros)

        // Inicialización de vistas
        txtBackRetiros = findViewById(R.id.txtBackRetiros)
        txtRetiro = findViewById(R.id.txtRetiro)
        btnRetirar = findViewById(R.id.btnRetirar)
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)

        // Listeners
        txtBackRetiros.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnRetirar.setOnClickListener {
            realizarRetiro()
        }
    }

    private fun realizarRetiro() {
        val montoStr = txtRetiro.text.toString().trim()
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

        val (success, message) = BankData.performWithdrawal(username, monto)

        if (success) {
            val nuevoSaldo = message.toDoubleOrNull() ?: 0.0
            showToast("Retiro exitoso! Nuevo saldo: ${formatCurrency(nuevoSaldo)}")
            txtRetiro.text.clear()
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