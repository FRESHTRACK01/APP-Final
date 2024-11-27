package com.example.fresh_track

data class Producto(
    val id: Int,
    val nombreProducto: String,
    val tipo: String,
    val cantidad: Int,
    val fechaEntrada: String,
    val fechaSalida: String,
    val precio: Double,
    val codigo: String,
    val imagenPath: String? // El signo de interrogación indica que este campo es nullable
)