// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.example.mapwithmarker

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
// [START maps_marker_on_map_ready]
class MapsMarkerActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    // [START_EXCLUDE]
    // [START maps_marker_get_map_async]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps)

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

    }
    // [END maps_marker_get_map_async]
    // [END_EXCLUDE]

    // [START maps_marker_on_map_ready_add_marker]
    override fun onMapReady(googleMap: GoogleMap) {

        val latLngBoundsBuilder = LatLngBounds.builder()

        PLACES.forEach { (name, latLng) ->
            val location = MarkerOptions().position(latLng)
                .title("Marker in $name")

            latLngBoundsBuilder.include(latLng)

            val marker = googleMap.addMarker(location)
            marker?.tag = latLng
        }

        googleMap.apply {
            setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                override fun getInfoContents(marker: Marker): View? {
                    val infoWindow = layoutInflater.inflate(R.layout.info_window, null)
                    infoWindow.findViewById<TextView>(R.id.name).text = marker.title
                    return infoWindow
                }

                override fun getInfoWindow(marker: Marker): View? = null
            })

            setOnInfoWindowClickListener(this@MapsMarkerActivity)
            setOnMapLoadedCallback {
                moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBoundsBuilder.build(),
                    resources.getDimensionPixelSize(R.dimen.default_padding)))
            }
        }

    }
    // [END maps_marker_on_map_ready_add_marker]

    companion object {
        val PLACES = mapOf(
            "BRISBANE" to LatLng(-27.47093, 153.0235),
            "MELBOURNE" to LatLng(-37.81319, 144.96298),
            "DARWIN" to LatLng(-12.4634, 130.8456),
            "SYDNEY" to LatLng(-33.87365, 151.20689),
            "ADELAIDE" to LatLng(-34.92873, 138.59995),
            "PERTH" to LatLng(-31.952854, 115.857342),
            "ALICE_SPRINGS" to LatLng(-24.6980, 133.8807)
        )
    }

    override fun onInfoWindowClick(marker: Marker) {
        Toast.makeText(this, "Info window clicked!", Toast.LENGTH_SHORT).show()
    }
}
// [END maps_marker_on_map_ready]
