package com.example.daviviendabank

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DatosUsuarios {

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

    // Estructura para las llaves de usuario
    data class UserKey(val key: String, val activationDate: String)

    // Mapa de llaves por usuario
    // Key: User ID, Value: Lista de llaves activas
    val userKeys = mutableMapOf<String, MutableList<UserKey>>()


    init {
        // Usuario de prueba
        users["1"] = mutableListOf("123456", "Usuario Prueba", "500000", "prueba@gmail.com", "3", "Bogotá")
        users["1029141647"] = mutableListOf("123456", "William Diaz", "10000000", "wdiqzgonzalez44@gmail.com", "3112781234", "Medellín")

        // Inicializar historiales vacíos
        transactionHistory["1"] = mutableListOf()
        transactionHistory["1029141647"] = mutableListOf()

        // Inicializar listas de llaves vacías
        userKeys["1"] = mutableListOf()
        userKeys["1029141647"] = mutableListOf()
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

    // --- Funciones para Llaves ---

    fun getUserKeys(userId: String): List<UserKey> {
        return userKeys[userId] ?: emptyList()
    }

    fun addKey(userId: String, key: String) {
        val keys = userKeys.getOrPut(userId) { mutableListOf() }
        // Evitar duplicados
        if (keys.none { it.key == key }) {
            keys.add(UserKey(key, getCurrentDate()))
            addTransaction(userId, "seguridad", 0.0, "Llave '$key' activada.")
        }
    }

    fun removeKey(userId: String, key: String) {
        userKeys[userId]?.let { keys ->
            keys.removeAll { it.key == key }
            addTransaction(userId, "seguridad", 0.0, "Llave '$key' desactivada.")
        }
    }

    fun findUserIdByKey(key: String): String? {
        for ((userId, keys) in userKeys) {
            if (keys.any { it.key.equals(key, ignoreCase = true) }) {
                return userId
            }
        }
        return null
    }

    fun performTransferWithKey(originId: String, destinationKey: String, amount: Double): Pair<Boolean, String> {
        val destinationId = findUserIdByKey(destinationKey)
            ?: return Pair(false, "La llave de destino no existe.")

        if (originId == destinationId) {
            return Pair(false, "No puedes transferirte a ti mismo.")
        }

        val originUserData = users[originId]
            ?: return Pair(false, "La cuenta de origen no existe.")

        val originBalance = originUserData[2].toDouble()
        if (originBalance < amount) {
            return Pair(false, "Fondos insuficientes.")
        }

        // Reutilizamos la lógica de transferencia normal
        val (success, message) = performTransfer(originId, destinationId, amount)

        // Si la transferencia fue exitosa, ajustamos el mensaje del historial
        if (success) {
            // El historial ya se añade en performTransfer, pero lo sobrescribimos para cambiar el mensaje
            transactionHistory[originId]?.removeAt(0) // Eliminar el último registro de transferencia
            addTransaction(originId, "transferencia", amount, "Transferencia enviada a llave '$destinationKey'")
        }

        return Pair(success, message)
    }

    fun updateUser(originalId: String, newId: String, newName: String, newEmail: String, newPhone: String, newCity: String): Pair<Boolean, String> {
        // 1. Verificar si el nuevo ID ya está en uso por otro usuario
        if (users.containsKey(newId) && originalId != newId) {
            return Pair(false, "El nuevo número de documento ya está registrado por otro usuario.")
        }

        // 2. Verificar si el nuevo correo ya está en uso por otro usuario
        val emailOwner = users.entries.find { it.value[3] == newEmail && it.key != originalId }
        if (emailOwner != null) {
            return Pair(false, "El nuevo correo electrónico ya está registrado por otro usuario.")
        }

        val currentUserData = users[originalId] ?: return Pair(false, "Usuario original no encontrado.")

        // Actualizar los datos del usuario
        currentUserData[1] = newName
        currentUserData[3] = newEmail
        currentUserData[4] = newPhone
        currentUserData[5] = newCity

        // Si el ID cambió, mover los datos a la nueva clave
        if (originalId != newId) {
            users[newId] = currentUserData
            users.remove(originalId)

            // Mover también el historial de transacciones
            transactionHistory[newId] = transactionHistory[originalId] ?: mutableListOf()
            transactionHistory.remove(originalId)
            addTransaction(newId, "actualizacion", 0.0, "Actualización de datos personales (cambio de ID).")
        } else {
            // Si el ID no cambió, solo registrar la actualización
            addTransaction(originalId, "actualizacion", 0.0, "Actualización de datos personales.")
        }

        return Pair(true, "Datos actualizados correctamente.")
    }

    fun changePassword(userId: String, oldPass: String, newPass: String): Pair<Boolean, String> {
        val userData = users[userId]
        if (userData == null) {
            return Pair(false, "Usuario no encontrado.")
        }
        if (userData[0] != oldPass) {
            return Pair(false, "La contraseña antigua es incorrecta.")
        }
        // Update the password
        userData[0] = newPass
        // Add a transaction for security logging
        addTransaction(userId, "seguridad", 0.0, "Cambio de contraseña exitoso.")
        return Pair(true, "Contraseña actualizada exitosamente.")
    }
}