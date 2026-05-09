package com.example.marsi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingRouterType
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import com.example.marsi.data.InMemoryCourierRepository
import com.example.marsi.model.CourierStatus

class MainActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var userLocationLayer: UserLocationLayer
    private lateinit var drivingRouter: DrivingRouter
    private var mapObjects: MapObjectCollection? = null
    private var routePolyline: PolylineMapObject? = null

    private lateinit var fabBuildRoute: FloatingActionButton
    private lateinit var fabFocus: FloatingActionButton
    private lateinit var chatOverlay: View

    private val courierRepository = InMemoryCourierRepository()
    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(courierRepository) }

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
        MapKitFactory.initialize(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.mapview)
        val ivMenu = findViewById<ImageView>(R.id.iv_menu_icon)
        val tvEta = findViewById<TextView>(R.id.tvEta)
        // tvStatus удалён из макета, больше не инициализируем

        fabBuildRoute = findViewById(R.id.fabBuildRoute)
        fabFocus = findViewById(R.id.fabFocusLocation)

        chatOverlay = findViewById(R.id.chat_overlay)
        val btnCloseChat = chatOverlay.findViewById<Button>(R.id.btnCloseChat)

        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter(DrivingRouterType.COMBINED)
        mapObjects = mapView.map.mapObjects.addCollection()

        setupUserLocation()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.coordinator)) { v, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            (tvEta.layoutParams as? android.view.ViewGroup.MarginLayoutParams)?.topMargin =
                statusBarHeight + 16
            (ivMenu.layoutParams as? android.view.ViewGroup.MarginLayoutParams)?.topMargin =
                statusBarHeight + 16
            insets
        }

        viewModel.etaText.observe(this) { tvEta.text = it }

        // Статус только в лог
        viewModel.courier.observe(this) { courier ->
            val statusString = when (courier.status) {
                CourierStatus.FREE -> "Свободен"
                CourierStatus.ON_ROUTE -> "В пути"
                CourierStatus.OFFLINE -> "Не в сети"
            }
            Log.d("CourierStatus", statusString)

            if (viewModel.selectedTransport.value != courier.preferredTransport) {
                viewModel.selectedTransport.value = courier.preferredTransport
            }
        }

        viewModel.route.observe(this) { route ->
            if (route != null) {
                drawRoute(route)
            } else {
                routePolyline?.let { mapObjects?.remove(it) }
                routePolyline = null
            }
        }

        val btnTransport = findViewById<MaterialButton>(R.id.btnTransport)
        viewModel.selectedTransport.observe(this) { transport ->
            btnTransport.text = transport
        }

        btnTransport.setOnClickListener { showTransportDialog() }
        findViewById<View>(R.id.btnChat).setOnClickListener { chatOverlay.visibility = View.VISIBLE }
        btnCloseChat.setOnClickListener { chatOverlay.visibility = View.GONE }
        chatOverlay.setOnClickListener { chatOverlay.visibility = View.GONE }

        findViewById<View>(R.id.btnCompleteOrder).setOnClickListener {
            viewModel.completeOrder()
        }

        ivMenu.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        fabBuildRoute.setOnClickListener {
            val userPos = userLocationLayer.cameraPosition()?.target
            if (userPos != null) {
                val destination = Point(58.5215, 31.2755)
                val transport = viewModel.selectedTransport.value ?: "Машина"
                viewModel.buildRoute(userPos, destination, transport, drivingRouter)
            }
        }

        fabFocus.setOnClickListener {
            val target = userLocationLayer.cameraPosition()?.target
            target?.let {
                mapView.mapWindow.map.move(
                    com.yandex.mapkit.map.CameraPosition(it, 15f, 0f, 0f),
                    Animation(Animation.Type.SMOOTH, 1f),
                    null
                )
            }
        }
    }

    private fun showTransportDialog() {
        val options = arrayOf("Машина", "Самокат", "Пешком")
        AlertDialog.Builder(this)
            .setTitle("Выберите способ передвижения")
            .setSingleChoiceItems(options, 0) { dialog, which ->
                viewModel.selectedTransport.value = options[which]
                dialog.dismiss()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun setupUserLocation() {
        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocationLayer.isVisible = true

        userLocationLayer.setObjectListener(object : UserLocationObjectListener {
            override fun onObjectAdded(view: UserLocationView) {
                view.pin.setIcon(
                    ImageProvider.fromResource(this@MainActivity, android.R.drawable.ic_menu_mylocation)
                )
            }
            override fun onObjectRemoved(view: UserLocationView) {}
            override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {}
        })
    }

    private fun drawRoute(route: DrivingRoute) {
        routePolyline?.let { mapObjects?.remove(it) }
        routePolyline = mapObjects?.addPolyline(route.geometry)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}