package com.example.daviviendabank

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class CodigoQR : AppCompatActivity() {

    private lateinit var txtUser: TextView
    private lateinit var imgQR: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // IMPORTANTE: setContentView antes de findViewById
        enableEdgeToEdge()
        setContentView(R.layout.activity_codigo_qr)

        txtUser = findViewById(R.id.txtUser4)
        imgQR = findViewById(R.id.imgQR)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Volver al hacer click en "← Volver"
        findViewById<TextView>(R.id.txtBack).setOnClickListener { finish() }

        cargarUsuario()
    }

    private fun cargarUsuario() {
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)

        if (username != null) {
            val userData = BankData.getUserData(username)
            if (userData != null) {
                val name = userData.getOrNull(1) ?: "Usuario"

                txtUser.text = "Código QR de $name"

                // Genera el QR automáticamente con la identificación (username)
                generarQR(username)
                return
            }
        }

        txtUser.text = "Código QR de Usuario"
        generarQR("0000")
    }

    private fun generarQR(texto: String) {
        try {
            val size = 512
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(texto, BarcodeFormat.QR_CODE, size, size)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            // --> Usa bitMatrix.get(x, y) (no bitMatrix[x, y])
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }

            imgQR.setImageBitmap(bitmap)

        } catch (e: WriterException) {
            e.printStackTrace()
            // Opcional: mostrar un drawable de fallback o ocultar imgQR
        }
    }
}
