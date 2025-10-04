package com.example.daviviendabank

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class Menuprin : AppCompatActivity() {

    // Variables
    private lateinit var txtSaldo2: TextView
    private lateinit var txtUser: TextView
    private lateinit var btnProfile: Button
    private lateinit var btnRecargarMiCuenta: LinearLayout
    private lateinit var btnRecargas: LinearLayout
    private lateinit var btnRetirarSinTarjeta: LinearLayout
    private lateinit var btnMenuTransferir: LinearLayout
    private lateinit var btnMenuHistorial: LinearLayout
    private lateinit var btnMenuInicio: LinearLayout
    private lateinit var btnMenuQR: LinearLayout
    private lateinit var btnMisLlaves: LinearLayout
    private lateinit var btnTransferirLlaves: LinearLayout
    private lateinit var btnCertificados: LinearLayout
    private lateinit var btnPazSalvo: LinearLayout
    private lateinit var scrollContent: ScrollView
    private var userId: String? = null




    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menuprin)

        // Inicialización de vistas
        btnProfile = findViewById(R.id.btnProfile)
        btnRecargarMiCuenta = findViewById(R.id.btnRecargarMiCuenta)
        btnRecargas = findViewById(R.id.btnRecargas)
        btnRetirarSinTarjeta = findViewById(R.id.btnRetirarSinTarjeta)
        btnMenuTransferir = findViewById(R.id.btnMenuTransferir)
        btnMenuHistorial = findViewById(R.id.btnMenuHistorial)
        btnMenuInicio = findViewById(R.id.btnMenuInicio)
        btnMenuQR = findViewById(R.id.btnMenuQR)
        btnMisLlaves = findViewById(R.id.btnMisLlaves)
        btnTransferirLlaves = findViewById(R.id.btnTransferirLlave)
        btnCertificados = findViewById(R.id.btnCertificados)
        btnPazSalvo = findViewById(R.id.btnPazSalvo)
        scrollContent = findViewById<ScrollView>(R.id.scrollContent)
        txtUser = findViewById(R.id.txtUser)
        txtSaldo2 = findViewById(R.id.txtSaldo2)

        // Llamamos las funciones
        cargarSaludoUsuario()
        cargarSaldo2()

        // Listeners
        btnProfile.setOnClickListener {
            val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
            val username = sharedPreferences.getString("username", null)

            if (username != null) {
                val intent = Intent(this, Cuenta::class.java)
                intent.putExtra("user", username) // Pasamos el usuario a la activity Cuenta
                startActivity(intent)
            } else {
                Toast.makeText(this, "No hay sesión activa", Toast.LENGTH_SHORT).show()
            }
        }

        btnRecargarMiCuenta.setOnClickListener {
            startActivity(Intent(this, Depositos::class.java))
        }

        btnRecargas.setOnClickListener {
            startActivity(Intent(this, Depositos::class.java))
        }

        btnRetirarSinTarjeta.setOnClickListener {
            startActivity(Intent(this, Retiros::class.java))
        }

        btnMenuTransferir.setOnClickListener {
            startActivity(Intent(this, Tranferencia::class.java))
        }

        btnMenuHistorial.setOnClickListener {
            startActivity(Intent(this, Historial::class.java))
        }

        btnMenuInicio.setOnClickListener {
            scrollContent.post {
                scrollContent.smoothScrollTo(0, 0)
            }
        }

        btnMenuQR.setOnClickListener {
            startActivity(Intent(this, CodigoQR::class.java))
        }

        btnMisLlaves.setOnClickListener {
            val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
            val username = sharedPreferences.getString("username", null)

            if (username != null) {
                val intent = Intent(this, MisLlaves::class.java)
                intent.putExtra("username", username) // <-- PASO CLAVE: Añadir el extra
                startActivity(intent)
            } else {
                Toast.makeText(this, "No hay sesión activa", Toast.LENGTH_SHORT).show()
            }
        }

        btnTransferirLlaves.setOnClickListener {
            startActivity(Intent(this, TransferenciaLlave::class.java))
        }

        btnCertificados.setOnClickListener {
            val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
            val username = sharedPreferences.getString("username", null)

            if (username != null) {
                val userData = BankData.getUserData(username)

                if (userData != null) {
                    val intent = Intent(this, Certificado::class.java)
                    intent.putExtra("identificacion", username) // 🔹 Aquí se pasa el ID o número de cuenta
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No hay sesión activa", Toast.LENGTH_SHORT).show()
            }
        }


        btnPazSalvo.setOnClickListener {
            showCustomDialog()
        }
    }

    // 🚀 Se ejecuta cada vez que la Activity vuelve al frente
    override fun onResume() {
        super.onResume()
        cargarSaldo2()
    }

    // Función para cargar saludo
    private fun cargarSaludoUsuario() {
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)

        if (username != null) {
            val userData = BankData.getUserData(username)
            if (userData != null) {
                val name = userData[1] // Nombre completo
                txtUser.text = "Hola, $name"
            } else {
                txtUser.text = "Hola, Usuario"
            }
        } else {
            txtUser.text = "Hola, Usuario"
        }
    }

    // Función para cargar saldo
    private fun cargarSaldo2() {
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)
        if (username != null) {
            val userData = BankData.getUserData(username)
            if (userData != null) {
                val balance = userData[2].toDoubleOrNull() ?: 0.0
                txtSaldo2.text = formatCurrency(balance)
            } else {
                txtSaldo2.text = "-"
            }
        }
    }

    // Formato de moneda
    private fun formatCurrency(amount: Double): String {
        return java.text.NumberFormat.getCurrencyInstance(java.util.Locale("es", "CO"))
            .format(amount)
    }

    private fun showCustomDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)

        val dialog = AlertDialog.Builder(this, R.style.TransparentDialog)
            .setView(dialogView)
            .create()

        dialogView.findViewById<TextView>(R.id.btnAceptar).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
