package com.example.marsi.model

import com.yandex.mapkit.geometry.Point

enum class CourierStatus {
    FREE,
    ON_ROUTE,
    OFFLINE
}

data class Courier(
    val courierId: String,
    val warehouseAddress: String,
    var orderId: String? = null,
    var status: CourierStatus = CourierStatus.FREE,
    val name: String = "Иванов Иван",
    var preferredTransport: String = "Машина",
    var lastLocation: Point? = null,
    var lastUpdateTime: Long = System.currentTimeMillis()
)