package com.example.speed

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.MaterialToolbar
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class MapasActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private val villanueva = LatLng(40.449762386234276, -4.006325508007479)  // Ubicación base
    private val ZOOM_LEVEL = 13f

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mapas)

        val searchEditText: EditText = findViewById(R.id.searchEditText)
        val searchButton: Button = findViewById(R.id.searchButton)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                fetchTalleresFromGooglePlaces(query)
            } else {
                Toast.makeText(this, "Ingrese un texto de búsqueda.", Toast.LENGTH_SHORT).show()
            }
        }
        // Imagen para volver atrás (lo haces dentro de este método)
        val navigationIcon = findViewById<ImageView>(R.id.navigationIcon)
        navigationIcon.setOnClickListener {
            onBackPressed() // Llama al método de retroceso al hacer clic en la imagen
        }
    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(villanueva, ZOOM_LEVEL))
        googleMap.addMarker(MarkerOptions().position(villanueva).title("Villanueva de la Cañada"))

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    private fun enableMyLocation() {
        if (::googleMap.isInitialized) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.isMyLocationEnabled = true
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation()
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchTalleresFromGooglePlaces(query: String) {
        val apiKey = "AIzaSyA6xQTaBnZqSTUk1KP8uRd5kx0twrEPPhk"  // Aquí coloca tu clave de API de Google
        val location = "${villanueva.latitude},${villanueva.longitude}"
        val url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=${query}&location=${location}&radius=5000&key=${apiKey}"

        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("PlaceAPI", "Failed to fetch nearby talleres from Google Places", e)
                showFallbackMessage("Error al buscar talleres cercanos. Verifica tu conexión.")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.let {
                        val responseBody = it.string()
                        Log.d("PlaceAPI", "Google Places Response: $responseBody")

                        try {
                            val json = JSONArray(responseBody)
                            Handler(mainLooper).post {
                                googleMap.clear()
                                for (i in 0 until json.length()) {
                                    val place = json.getJSONObject(i)
                                    val geometry = place.optJSONObject("geometry")
                                    val location = geometry?.optJSONObject("location")
                                    val lat = location?.optDouble("lat")
                                    val lon = location?.optDouble("lng")
                                    val name = place.optString("name", "Taller desconocido")

                                    if (lat != null && lon != null) {
                                        googleMap.addMarker(
                                            MarkerOptions()
                                                .position(LatLng(lat, lon))
                                                .title(name)
                                        )
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("PlaceAPI", "Error parsing response from Google Places", e)
                            showFallbackMessage("Error al procesar la respuesta.")
                        }
                    }
                } else {
                    Log.e("PlaceAPI", "HTTP Error: ${response.code}")
                    showFallbackMessage("No se pudieron obtener los talleres desde Google Places.")
                }
            }
        })
    }
    private fun fetchNearbyTalleres() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://overpass-api.de/api/interpreter")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OverpassAPI", "Failed to fetch nearby talleres", e)
                showFallbackMessage("Error al buscar talleres cercanos.")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.let {
                        val responseBody = it.string()
                        Log.d("OverpassAPI", "Overpass Response: $responseBody")
                        try {
                            val json = JSONObject(responseBody)
                            val elements = json.optJSONArray("elements") // Manejar la estructura JSON que viene en la respuesta

                            if (elements != null) {
                                Handler(mainLooper).post {
                                    googleMap.clear()
                                    for (i in 0 until elements.length()) {
                                        val element = elements.getJSONObject(i)
                                        val lat = element.optJSONObject("geometry")?.optJSONObject("location")?.optDouble("lat")
                                        val lon = element.optJSONObject("geometry")?.optJSONObject("location")?.optDouble("lng")
                                        val name = element.optString("name", "Taller desconocido")

                                        if (lat != null && lon != null) {
                                            googleMap.addMarker(
                                                MarkerOptions()
                                                    .position(LatLng(lat, lon))
                                                    .title(name)
                                            )
                                        }
                                    }
                                }
                            } else {
                                Log.e("OverpassAPI", "No elements found in Overpass response")
                                showFallbackMessage("No se encontraron talleres.")
                            }
                        } catch (e: JSONException) {
                            Log.e("OverpassAPI", "Error parsing JSON response from Overpass", e)
                            showFallbackMessage("Error al procesar la respuesta.")
                        }
                    }
                } else {
                    Log.e("OverpassAPI", "HTTP Error: ${response.code}")
                    showFallbackMessage("No se pudieron obtener los talleres desde Overpass.")
                }
            }
        })
    }


    private fun showFallbackMessage(message: String) {
        Handler(mainLooper).post {
            Toast.makeText(this@MapasActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
    override fun onBackPressed() {
        if (isDialogOpen()) {
            // Si existe un diálogo abierto, ciérralo
            getCurrentDialog()?.dismiss()
        } else {
            super.onBackPressed()  // Llama a la funcionalidad de retroceso normal
        }
    }
    // Método para verificar si un diálogo está abierto
    private fun isDialogOpen(): Boolean {
        val currentDialog = this.supportFragmentManager.findFragmentByTag("android:switcher")
        return currentDialog != null
    }

    // Método para obtener el diálogo actual
    private fun getCurrentDialog(): AlertDialog? {
        return this.supportFragmentManager.findFragmentByTag("android:switcher") as? AlertDialog
    }
}







