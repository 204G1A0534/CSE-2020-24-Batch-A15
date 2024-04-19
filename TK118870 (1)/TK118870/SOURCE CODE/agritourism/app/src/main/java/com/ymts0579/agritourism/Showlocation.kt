package com.ymts0579.agritourism

import android.graphics.Color
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.ymts0579.agritourism.databinding.ActivityShowlocationBinding

class Showlocation : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityShowlocationBinding
    var to=""
    var from=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowlocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        to=intent.getStringExtra("to").toString()
        from=intent.getStringExtra("from").toString()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val ge= Geocoder(this).getFromLocationName(to,1)
        val lat1=ge[0].latitude
        val long1=ge[0].longitude
        val sydney = LatLng(lat1,long1)
        mMap.addMarker(MarkerOptions().position(sydney).title("Farmer location $to "))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16f))


        val ge1= Geocoder(this).getFromLocationName(from,1)
        val lat2=ge1[0].latitude
        val long2=ge1[0].longitude
        val sydney1 = LatLng(lat2,long2)
        mMap.addMarker(MarkerOptions().position(sydney1).title("your location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney1, 16f))
        mMap.addPolygon(PolygonOptions().fillColor(Color.BLUE).strokeWidth(10f).add(sydney, sydney1))
    }
}