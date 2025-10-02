
package com.example.daviviendabank

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object BankData {

    // Estructura para el historial de transacciones
    data class Transaction(
        val type: String,
        val amount: Double,
        val date: String,
        val description: String
    )

    // Key: User ID (documento), Value: Lista de datos del usuario
    // Formato de la lista: [0: password, 1: fullName, 2: balance, 3: email, 4: phone, 5: city]
    val users = mutableMapOf<String, MutableList<String>>()

    // Historial de transacciones por usuario
    // Key: User ID, Value: Lista de transacciones
    val transactionHistory = mutableMapOf<String, MutableList<Transaction>>()

    init {
        // Usuario de prueba
        users["12345"] = mutableListOf("password123", "Felipe Pérez", "1000000", "felipe.perez@email.com", "3001234567", "Bogotá")
        users["54321"] = mutableListOf("admin", "Ana García", "500000", "ana.garcia@email.com", "3109876543", "Medellín")
        
        // Inicializar historiales vacíos
        transactionHistory["12345"] = mutableListOf()
        transactionHistory["54321"] = mutableListOf()
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun addTransaction(userId: String, type: String, amount: Double, description: String) {
        val transaction = Transaction(type, amount, getCurrentDate(), description)
        transactionHistory[userId]?.add(0, transaction) // Añadir al principio de la lista
    }

    // --- Funciones de Lógica de Negocio ---

    fun login(userId: String, pass: String): Boolean {
        val userData = users[userId]
        return userData != null && userData[0] == pass
    }

    fun registerUser(id: String, pass: String, name: String, email: String, phone: String, city: String): Boolean {
        if (users.containsKey(id)) {
            return false // El usuario ya existe
        }
        users[id] = mutableListOf(pass, name, "0", email, phone, city)
        transactionHistory[id] = mutableListOf() // Crear historial para el nuevo usuario
        addTransaction(id, "registro", 0.0, "Creación de cuenta")
        return true
    }

    fun getUserData(userId: String): List<String>? {
        return users[userId]
    }

    fun performDeposit(userId: String, amount: Double): Pair<Boolean, String> {
        val userData = users[userId]
        if (userData == null) {
            return Pair(false, "Usuario no encontrado.")
        }

        val currentBalance = userData[2].toDouble()
        val newBalance = currentBalance + amount
        userData[2] = newBalance.toString()
        
        addTransaction(userId, "deposito", amount, "Depósito a cuenta propia")
        return Pair(true, newBalance.toString())
    }

    fun performWithdrawal(userId: String, amount: Double): Pair<Boolean, String> {
        val userData = users[userId]
        if (userData == null) {
            return Pair(false, "Usuario no encontrado.")
        }

        val currentBalance = userData[2].toDouble()
        if (currentBalance < amount) {
            return Pair(false, "Fondos insuficientes.")
        }

        val newBalance = currentBalance - amount
        userData[2] = newBalance.toString()
        
        addTransaction(userId, "retiro", amount, "Retiro de cuenta propia")
        return Pair(true, newBalance.toString())
    }

    fun performTransfer(originId: String, destinationId: String, amount: Double): Pair<Boolean, String> {
        val originUserData = users[originId]
        val destinationUserData = users[destinationId]

        if (originUserData == null) {
            return Pair(false, "La cuenta de origen no existe.")
        }
        if (destinationUserData == null) {
            return Pair(false, "La cuenta de destino no existe.")
        }

        val originBalance = originUserData[2].toDouble()
        if (originBalance < amount) {
            return Pair(false, "Fondos insuficientes.")
        }

        // Actualizar saldos
        val newOriginBalance = originBalance - amount
        originUserData[2] = newOriginBalance.toString()

        val destinationBalance = destinationUserData[2].toDouble()
        val newDestinationBalance = destinationBalance + amount
        destinationUserData[2] = newDestinationBalance.toString()

        // Añadir a historiales
        addTransaction(originId, "transferencia", amount, "Transferencia enviada a cuenta $destinationId")
        addTransaction(destinationId, "deposito", amount, "Transferencia recibida de cuenta $originId")

        return Pair(true, "Transferencia exitosa.")
    }
    
    fun getTransactionHistory(userId: String): List<Transaction>? {
        return transactionHistory[userId]
    }
}
