package com.example.marsi.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.marsi.model.Courier
import com.example.marsi.model.CourierStatus

interface CourierRepository {
    fun getCourier(): LiveData<Courier>
    fun updateStatus(status: CourierStatus)
    fun updateOrderId(orderId: String?)
    fun updateLocation(location: com.yandex.mapkit.geometry.Point)
    fun updateWarehouseAddress(newAddress: String)
    fun updatePreferredTransport(transport: String)

}

class InMemoryCourierRepository : CourierRepository {
    private val courierLiveData = MutableLiveData(
        Courier(
            courierId = "cour_001",
            warehouseAddress = "ул. Ленина, 5",
            name = "Иванов Иван"
        )
    )

    override fun getCourier(): LiveData<Courier> = courierLiveData

    override fun updateStatus(status: CourierStatus) {
        courierLiveData.value?.let {
            courierLiveData.value = it.copy(status = status, lastUpdateTime = System.currentTimeMillis())
        }
    }

    override fun updateOrderId(orderId: String?) {
        courierLiveData.value?.let {
            courierLiveData.value = it.copy(orderId = orderId, lastUpdateTime = System.currentTimeMillis())
        }
    }

    override fun updateLocation(location: com.yandex.mapkit.geometry.Point) {
        courierLiveData.value?.let {
            courierLiveData.value = it.copy(lastLocation = location, lastUpdateTime = System.currentTimeMillis())
        }
    }

    override fun updateWarehouseAddress(newAddress: String) {
        courierLiveData.value?.let {
            courierLiveData.value = it.copy(warehouseAddress = newAddress, lastUpdateTime = System.currentTimeMillis())
        }
    }

    override fun updatePreferredTransport(transport: String) {
        courierLiveData.value?.let {
            courierLiveData.value = it.copy(preferredTransport = transport)
        }
    }
}