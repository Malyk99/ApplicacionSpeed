package com.example.speed

data class Usuario(
    val id: Int = 0,
    val nombre: String,
    val email: String,
    val contrasena: String,
    val idCoche: Int? = null // Puede ser null si no hay un coche asociado
)