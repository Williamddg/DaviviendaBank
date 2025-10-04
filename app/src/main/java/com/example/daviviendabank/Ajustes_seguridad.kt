package com.example.daviviendabank

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Ajustes_seguridad : AppCompatActivity() {

    private lateinit var txtVolver: TextView
    private lateinit var txtViajes: TextView
    private lateinit var txtLimites: TextView
    private lateinit var txtCambiarClave: TextView
    private lateinit var txtLimitesPSE: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ajustes_seguridad)

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
        txtViajes = findViewById(R.id.txtViajes)
        txtLimites = findViewById(R.id.txtLimites)
        txtCambiarClave = findViewById(R.id.txtCambiarClave)
        txtLimitesPSE = findViewById(R.id.txtLimitesPSE)
    }

    private fun setupClickListeners() {
        txtVolver.setOnClickListener {
            finish()
        }

        txtViajes.setOnClickListener {
            showCustomDialog()
        }

        txtLimites.setOnClickListener {
            showCustomDialog()
        }

        txtCambiarClave.setOnClickListener {
            startActivity(Intent(this, Clave::class.java))
        }

        txtLimitesPSE.setOnClickListener {
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
