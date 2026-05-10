package com.example.marsi.model
import com.yandex.mapkit.geometry.Point

data class Order(
    val id: String,
    val address: String,
    val destination: Point
)