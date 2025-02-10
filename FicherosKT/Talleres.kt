package com.example.speed

data class Talleres(
    val nombre: String = "",
    val imagenId: String = "",
    val piezas: ArrayList<PiezasStock> = ArrayList()
)