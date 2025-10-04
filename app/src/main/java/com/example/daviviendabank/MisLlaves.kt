package com.example.daviviendabank

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MisLlaves : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var switchDav: SwitchCompat
    private lateinit var switchId: SwitchCompat
    private lateinit var switchNum: SwitchCompat
    private lateinit var switchEmail: SwitchCompat
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

        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        userId = sharedPreferences.getString("username", null)

        if (userId == null) {
            finish() // No debería estar aquí sin sesión
            return
        }

        userData = BankData.getUserData(userId!!)

        if (userData == null) {
            finish() // Datos de usuario no encontrados
            return
        }

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
    class KeyAdapter(private var keys: List<BankData.UserKey>) : RecyclerView.Adapter<KeyAdapter.ViewHolder>() {

        // ViewHolder ahora solo contiene un TextView simple.
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = view as TextView
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            // Crear un TextView programáticamente para evitar problemas de inflación de XML.
            val textView = TextView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                textSize = 16f
                setTextColor(resources.getColor(android.R.color.white, null))
                setPadding(16, 16, 16, 16)
            }
            return ViewHolder(textView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val key = keys[position]
            holder.textView.text = "Llave: ${key.key}\nActivada: ${key.activationDate}"
        }

        override fun getItemCount() = keys.size

        fun updateKeys(newKeys: List<BankData.UserKey>) {
            keys = newKeys
            notifyDataSetChanged()
        }
    }
}
