package com.example.marsi.model

data class Dispatcher(
    val id: String,
    val name: String,
    val warehouseId: String,
    val shiftSchedule: Map<String, String> = emptyMap()
)
