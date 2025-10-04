package com.example.daviviendabank

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Ayuda_servicio : AppCompatActivity() {

    private lateinit var txtVolver: TextView
    private lateinit var txtLlamar: TextView
    private lateinit var txtChatSoporte: TextView
    private lateinit var txtMasReclamaciones: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ayuda_servicio)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        txtVolver = findViewById(R.id.txtBack)
        txtLlamar = findViewById(R.id.txtLlamar)
        txtChatSoporte = findViewById(R.id.txtChatSoporte)
        txtMasReclamaciones = findViewById(R.id.txtMasReclamaciones)
    }

    private fun setupClickListeners() {
        txtVolver.setOnClickListener {
            finish()
        }

        txtLlamar.setOnClickListener {
            showCustomDialog()
        }

        txtChatSoporte.setOnClickListener {
            showCustomDialog()
        }

        txtMasReclamaciones.setOnClickListener {
            showCustomDialog()
        }
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
