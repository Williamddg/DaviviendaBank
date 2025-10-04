package com.example.daviviendabank

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MisLlaves : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switchDav: Switch
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switchId: Switch
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switchNum: Switch
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switchEmail: Switch
    private lateinit var rvLlaves: RecyclerView
    private lateinit var txtBack: TextView

    private var userId: String? = null
    private var userData: List<String>? = null

    private lateinit var keyAdapter: KeyAdapter

    // Variables para almacenar los valores de las llaves
    private lateinit var keyDav: String
    private lateinit var keyId: String
    private lateinit var keyNum: String
    private lateinit var keyEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_llaves)
        Log.d("MisLlavesDebug", "onCreate: Activity started")

        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        // Patrón híbrido: Intent > SharedPreferences
        userId = intent.getStringExtra("username") ?: sharedPreferences.getString("username", null)

        if (userId == null) {
            Log.e("MisLlavesDebug", "onCreate: userId is NULL. Redirecting to main.")
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show()
            val intent = android.content.Intent(this, MainActivity::class.java)
            intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return // Detener la ejecución de onCreate
        }
        Log.d("MisLlavesDebug", "onCreate: userId = $userId")

        userData = BankData.getUserData(userId!!)
        if (userData == null) {
            Log.e("MisLlavesDebug", "onCreate: userData is NULL for userId $userId. Finishing activity.")
            Toast.makeText(this, "Error al cargar datos del usuario", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        Log.d("MisLlavesDebug", "onCreate: userData loaded successfully.")

        initViews()
        generateKeyValues()
        setupListeners()
        setupRecyclerView()
        loadInitialSwitchState()
    }

    private fun initViews() {
        switchDav = findViewById(R.id.switchdav)
        switchId = findViewById(R.id.switchid)
        switchNum = findViewById(R.id.switchnum)
        switchEmail = findViewById(R.id.switchemail)
        rvLlaves = findViewById(R.id.listllaves) // Corregido
        txtBack = findViewById(R.id.txtBackReg)      // Corregido
    }

    private fun generateKeyValues() {
        val phone = userData!![4]
        val id = userId!!
        val email = userData!![3]

        keyDav = "dav$phone"
        keyId = id
        keyNum = phone
        keyEmail = email
    }

    private fun setupListeners() {
        txtBack.setOnClickListener { finish() }

        switchDav.setOnCheckedChangeListener { _, isChecked ->
            handleKeyChange(keyDav, isChecked)
        }
        switchId.setOnCheckedChangeListener { _, isChecked ->
            handleKeyChange(keyId, isChecked)
        }
        switchNum.setOnCheckedChangeListener { _, isChecked ->
            handleKeyChange(keyNum, isChecked)
        }
        switchEmail.setOnCheckedChangeListener { _, isChecked ->
            handleKeyChange(keyEmail, isChecked)
        }
    }

    private fun handleKeyChange(key: String, isActive: Boolean) {
        if (isActive) {
            BankData.addKey(userId!!, key)
        } else {
            BankData.removeKey(userId!!, key)
        }
        // Actualizar la lista visual
        updateKeyList()
    }

    private fun setupRecyclerView() {
        rvLlaves.layoutManager = LinearLayoutManager(this)
        keyAdapter = KeyAdapter(emptyList())
        rvLlaves.adapter = keyAdapter
        updateKeyList()
    }

    private fun updateKeyList() {
        val activeKeys = BankData.getUserKeys(userId!!)
        keyAdapter.updateKeys(activeKeys)
    }

    private fun loadInitialSwitchState() {
        val activeKeys = BankData.getUserKeys(userId!!).map { it.key }

        // Usar un bloque para evitar múltiples listeners al inicio
        switchDav.post { switchDav.isChecked = activeKeys.contains(keyDav) }
        switchId.post { switchId.isChecked = activeKeys.contains(keyId) }
        switchNum.post { switchNum.isChecked = activeKeys.contains(keyNum) }
        switchEmail.post { switchEmail.isChecked = activeKeys.contains(keyEmail) }
    }

    // --- RecyclerView Adapter ---
    // --- RecyclerView Adapter ---
    class KeyAdapter(private var keys: List<BankData.UserKey>) : RecyclerView.Adapter<KeyAdapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val txtNombreLlave: TextView = itemView.findViewById(R.id.txtNombreLlave)
            val txtTipoLlave: TextView = itemView.findViewById(R.id.txtTipoLlave)
            val txtEstadoLlave: TextView = itemView.findViewById(R.id.txtEstadoLlave)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_llave, parent, false)
            return ViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val key = keys[position]

            holder.txtNombreLlave.text = key.key  // Aquí va la llave activa
            holder.txtTipoLlave.text = "Tipo: ${getTipoDesdeLlave(key.key)}"
            holder.txtEstadoLlave.text = "Activada: ${key.activationDate}"
        }

        override fun getItemCount() = keys.size

        @SuppressLint("NotifyDataSetChanged")
        fun updateKeys(newKeys: List<BankData.UserKey>) {
            keys = newKeys
            notifyDataSetChanged()
        }

        private fun getTipoDesdeLlave(key: String): String {    //función para mostrar el tipo de llave
            return when {
                key.startsWith("dav") -> "Llave Davivienda"
                key.contains("@") -> "Correo"
                key.all { it.isDigit() } && key.startsWith("3") -> "Número teléfono"
                key.all { it.isDigit() } -> "Identificación"
                else -> "ID"
            }
        }

    }

}