package com.example.marsi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.driving.*
import com.yandex.mapkit.geometry.Point
import com.yandex.runtime.Error
import com.example.marsi.data.CourierRepository
import com.example.marsi.model.Courier
import com.example.marsi.model.CourierStatus

class MainViewModel(private val courierRepository: CourierRepository) : ViewModel() {

    val courier: LiveData<Courier> = courierRepository.getCourier()

    private val _etaText = MutableLiveData<String>()
    val etaText: LiveData<String> = _etaText

    private val _route = MutableLiveData<DrivingRoute?>()
    val route: LiveData<DrivingRoute?> = _route

    val selectedTransport = MutableLiveData<String>("Машина")

    private var drivingSession: DrivingSession? = null

    fun buildRoute(startPoint: Point, endPoint: Point, transport: String, router: DrivingRouter) {
        drivingSession = null
        courierRepository.updateLocation(startPoint)
        courierRepository.updatePreferredTransport(transport)

        val requestPoints = listOf(
            RequestPoint(startPoint, RequestPointType.WAYPOINT, null, null),
            RequestPoint(endPoint, RequestPointType.WAYPOINT, null, null)
        )

        // Здесь можно задавать разные параметры для самоката / машины
        val drivingOptions = DrivingOptions()
        val vehicleOptions = VehicleOptions()

        drivingSession = router.requestRoutes(
            requestPoints,
            drivingOptions,
            vehicleOptions,
            object : DrivingSession.DrivingRouteListener {
                override fun onDrivingRoutes(routes: MutableList<DrivingRoute>) {
                    if (routes.isNotEmpty()) {
                        val mainRoute = routes[0]
                        _route.postValue(mainRoute)

                        val totalSeconds = mainRoute.metadata.weight.time.value.toInt()
                        val minutes = totalSeconds / 60
                        val seconds = totalSeconds % 60
                        _etaText.postValue(String.format("%02d:%02d", minutes, seconds))
                        courierRepository.updateStatus(CourierStatus.ON_ROUTE)
                    }
                }
                override fun onDrivingRoutesError(error: Error) {
                    _etaText.postValue("Ошибка построения маршрута")
                }
            }
        )
    }

    fun completeOrder() {
        drivingSession = null
        _route.value = null
        _etaText.value = ""
        courierRepository.updateOrderId(null)
        courierRepository.updateStatus(CourierStatus.FREE)
    }

    fun clearRoute() {
        drivingSession = null
        _route.value = null
        _etaText.value = ""
    }
}