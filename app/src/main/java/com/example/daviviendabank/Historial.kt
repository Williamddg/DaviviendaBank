package com.example.daviviendabank

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class Historial : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var txtBackHistorial: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        // Inicializar vistas
        recyclerView = findViewById(R.id.rvTransacciones)
        txtBackHistorial = findViewById(R.id.txtBackHistorial)
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)

        // Configurar RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Botón de volver
        txtBackHistorial.setOnClickListener {
            finish()
        }

        // Cargar historial
        cargarHistorial()
    }

    private fun cargarHistorial() {
        val username = sharedPreferences.getString("username", null) ?: run {
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val transacciones = DatosUsuarios.getTransactionHistory(username)

        if (transacciones != null) {
            recyclerView.adapter = TransaccionAdapter(transacciones)
        } else {
            Toast.makeText(this, "No se encontró historial para este usuario", Toast.LENGTH_SHORT).show()
        }
    }

    class TransaccionAdapter(private val transacciones: List<DatosUsuarios.Transaction>) :
        RecyclerView.Adapter<TransaccionAdapter.ViewHolder>() {

        class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val txtTipo: TextView = view.findViewById(R.id.txtTipo)
            val txtMonto: TextView = view.findViewById(R.id.txtMonto)
            val txtFecha: TextView = view.findViewById(R.id.txtFecha)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.elemento_transaccion, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val t = transacciones[position]
            holder.txtTipo.text = t.description

            when (t.type) {
                "deposito" -> {
                    holder.txtMonto.text = "+${formatCurrency(t.amount)}"
                    holder.txtMonto.setTextColor(Color.GREEN)
                }
                else -> { // Para retiros y transferencias
                    holder.txtMonto.text = "-${formatCurrency(t.amount)}"
                    holder.txtMonto.setTextColor(Color.RED)
                }
            }
            holder.txtFecha.text = t.date
        }

        override fun getItemCount() = transacciones.size

        private fun formatCurrency(amount: Double): String {
            return NumberFormat.getCurrencyInstance(Locale("es", "CO")).format(amount)
        }
    }
}