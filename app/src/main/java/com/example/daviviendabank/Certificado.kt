package com.example.daviviendabank

import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Certificado : AppCompatActivity() {

    private lateinit var txtVolver: TextView
    private lateinit var txtPropietario: TextView
    private lateinit var txtCuenta: TextView
    private lateinit var btnDescargar: Button

    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_certificado)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ✅ Ahora obtenemos el usuario desde la sesión guardada, no desde putExtra
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        userId = sharedPreferences.getString("username", null)

        initViews()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
    }

    private fun initViews() {
        txtVolver = findViewById(R.id.txtBack)
        txtPropietario = findViewById(R.id.txtPropietario)
        txtCuenta = findViewById(R.id.txtCuenta)
        btnDescargar = findViewById(R.id.btnDescargar)
    }

    private fun loadUserData() {
        if (userId == null) {
            Toast.makeText(this, "Sesión no encontrada", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val userData = DatosUsuarios.getUserData(userId!!)
        if (userData == null) {
            Toast.makeText(this, "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        txtPropietario.text = userData[1]
        txtCuenta.text = userId
    }

    private fun setupClickListeners() {
        txtVolver.setOnClickListener { finish() }

        btnDescargar.setOnClickListener {
            if (userId == null) {
                Toast.makeText(this, "Sesión no válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userData = DatosUsuarios.getUserData(userId!!)
            if (userData == null) {
                Toast.makeText(this, "No se encontraron los datos del usuario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nombre = userData[1]
            val saldo = userData[2]
            val email = userData[3]
            val telefono = userData[4]
            val ciudad = userData[5]
            val cuenta = userId!!

            val historial = DatosUsuarios.getTransactionHistory(cuenta)
            val fechaApertura = if (!historial.isNullOrEmpty()) {
                historial.last().date // última transacción = la más antigua
            } else {
                "No disponible"
            }

            crearCertificadoPdf(nombre, cuenta, saldo, ciudad, fechaApertura)
        }
    }

    private fun crearCertificadoPdf(
        nombre: String,
        cuenta: String,
        saldo: String,
        ciudad: String,
        fechaApertura: String
    ) {
        try {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // Tamaño A4
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            val paint = Paint()

            // Formato de fecha actual
            val sdf = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("es", "CO"))
            val fechaActual = sdf.format(Date())

            // --- TÍTULO ---
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 26f
            paint.isFakeBoldText = true
            canvas.drawText("CERTIFICADO", pageInfo.pageWidth / 2f, 100f, paint)

            // --- Estilo base para el resto del texto ---
            paint.textAlign = Paint.Align.LEFT
            paint.textSize = 14f
            paint.isFakeBoldText = false

            val lineHeight = paint.textSize * 1.0f  // interlineado sencillo (1.0)
            val yInicio = 160f

            // --- Encabezado ---
            canvas.drawText("$ciudad,", 80f, yInicio, paint)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(fechaActual, pageInfo.pageWidth - 80f, yInicio, paint)

            paint.textAlign = Paint.Align.LEFT
            canvas.drawText("COLOMBIA,", 80f, yInicio + lineHeight, paint)
            canvas.drawText("A quien interese", 80f, yInicio + (lineHeight * 2f), paint)

            // --- Cuerpo ---
            var y = yInicio + (lineHeight * 4f)
            val texto1 =
                "Por medio de la presente hacemos constar que $nombre con cédula de ciudadanía número $cuenta posee en el Banco Davivienda:"
            val anchoTexto = pageInfo.pageWidth - 160f
            val textoDividido = dividirTexto(texto1, paint, anchoTexto)
            for (linea in textoDividido) {
                canvas.drawText(linea, 80f, y, paint)
                y += lineHeight
            }

            y += lineHeight * 1.5f
            paint.isFakeBoldText = true
            canvas.drawText("Cuenta de ahorros", 80f, y, paint)
            y += lineHeight * 1.5f

            paint.isFakeBoldText = false
            paint.textAlign = Paint.Align.LEFT
            canvas.drawText("Número:", 80f, y, paint)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(cuenta, pageInfo.pageWidth - 80f, y, paint)

            y += lineHeight
            paint.textAlign = Paint.Align.LEFT
            canvas.drawText("Saldo a la fecha:", 80f, y, paint)
            paint.textAlign = Paint.Align.RIGHT
            val saldoDouble = saldo.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0
            canvas.drawText(formatCurrency(saldoDouble), pageInfo.pageWidth - 100f, y, paint)

            y += lineHeight
            paint.textAlign = Paint.Align.LEFT
            canvas.drawText("Fecha de apertura:", 80f, y, paint)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(fechaApertura, pageInfo.pageWidth - 80f, y, paint)

            // --- Pie ---
            y += lineHeight * 3f
            paint.textAlign = Paint.Align.LEFT
            canvas.drawText("Cordialmente,", 80f, y, paint)

            y += lineHeight * 2f
            paint.isFakeBoldText = true
            canvas.drawText("BANCO DAVIVIENDA", 80f, y, paint)

            pdfDocument.finishPage(page)

            // Guardar PDF
            val fileName = "Certificado_${cuenta}.pdf"
            val downloadsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)

            val outputStream = FileOutputStream(file)
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()
            outputStream.close()

            Toast.makeText(this, "Certificado descargado en Descargas/$fileName", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al generar PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


    // Divide texto largo en varias líneas automáticas
    private fun dividirTexto(texto: String, paint: Paint, anchoMax: Float): List<String> {
        val palabras = texto.split(" ")
        val lineas = mutableListOf<String>()
        var lineaActual = ""

        for (palabra in palabras) {
            val prueba = if (lineaActual.isEmpty()) palabra else "$lineaActual $palabra"
            if (paint.measureText(prueba) <= anchoMax) {
                lineaActual = prueba
            } else {
                lineas.add(lineaActual)
                lineaActual = palabra
            }
        }
        if (lineaActual.isNotEmpty()) lineas.add(lineaActual)
        return lineas
    }

    private fun formatCurrency(amount: Double): String {
        return NumberFormat.getCurrencyInstance(Locale("es", "CO")).format(amount)
    }
}
