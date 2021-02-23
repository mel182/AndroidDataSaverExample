package com.example.datasaverexampleapp.location.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.iterator
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.base_classes.BaseActivity
import com.example.datasaverexampleapp.type_alias.Layout
import com.example.datasaverexampleapp.type_alias.ViewByID
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_google_maps.*

class GoogleMapsActivity : BaseActivity(Layout.activity_google_maps), OnMapReadyCallback, CompoundButton.OnCheckedChangeListener
{
    private var map:GoogleMap? = null
    private var menu:Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtain the support map fragment and request the Google Map object.
        supportFragmentManager.findFragmentById(ViewByID.map)?.apply {

            if (this is SupportMapFragment)
                getMapAsync(this@GoogleMapsActivity)
        }

        setMapType()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_google_map,menu)
        this.menu = menu
        return true
    }

    @SuppressLint("MissingPermission")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId)
        {
            R.id.building_enabled -> {

                item.isChecked = if (item.isChecked)
                {
                    this.map?.isBuildingsEnabled = false
                    false
                } else {
                    this.map?.isBuildingsEnabled = true
                    true
                }
                true
            }

            R.id.indoor_enabled -> {

                item.isChecked = if (item.isChecked)
                {
                    this.map?.isIndoorEnabled = false
                    false
                } else {
                    this.map?.isIndoorEnabled = true
                    true
                }
                true
            }

            R.id.traffic_enabled -> {

                item.isChecked = if (item.isChecked)
                {
                    this.map?.isTrafficEnabled = false
                    false
                } else {
                    this.map?.isTrafficEnabled = true
                    true
                }
                true
            }

            R.id.compass_enabled -> {

                item.isChecked = if (item.isChecked)
                {
                    this.map?.uiSettings?.apply {
                        isMyLocationButtonEnabled = false
                        isCompassEnabled = false
                    }
                    false
                } else {

                    this.map?.uiSettings?.apply {
                        isMyLocationButtonEnabled = true
                        isCompassEnabled = true
                    }
                    Toast.makeText(this,"Rotate map to display compass",Toast.LENGTH_SHORT).show()
                    true
                }
                true
            }

            ViewByID.my_location_enabled -> {

                item.isChecked = if (item.isChecked)
                {
                    this.map?.isMyLocationEnabled = false
                    false
                } else {

                    if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) && isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION))
                    {
                        this.map?.isMyLocationEnabled = true
                    } else {

                        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION )) { granted ->

                            item.isChecked = granted
                            if (granted)
                            {
                                this.map?.isMyLocationEnabled = true
                            }
                        }
                    }
                    true
                }

                true
            }

            R.id.all_gesture_enabled -> {

                item.isChecked = if (item.isChecked)
                {
                    this.map?.uiSettings?.setAllGesturesEnabled(false)
                    disableAllGestures(false)
                    false
                } else {
                    this.map?.uiSettings?.setAllGesturesEnabled(true)
                    disableAllGestures(true)
                    true
                }
                true
            }

            R.id.indoor_level_picker_enabled -> {

                item.isChecked = if (item.isChecked)
                {
                    this.map?.uiSettings?.isIndoorLevelPickerEnabled = false
                    false
                } else {
                    this.map?.uiSettings?.isIndoorLevelPickerEnabled = true
                    true
                }
                true
            }

            R.id.map_toolbar -> {

                item.isChecked = if (item.isChecked)
                {
                    this.map?.uiSettings?.isMapToolbarEnabled = false
                    false
                } else {
                    this.map?.uiSettings?.isMapToolbarEnabled = true
                    true
                }
                true
            }

            R.id.rotate_gesture_enabled -> {

                item.isChecked = if (item.isChecked)
                {
                    this.map?.uiSettings?.isRotateGesturesEnabled = false
                    false
                } else {
                    this.map?.uiSettings?.isRotateGesturesEnabled = true
                    true
                }
                true
            }

            R.id.scroll_gesture_enabled -> {

                item.isChecked = if (item.isChecked)
                {
                    this.map?.uiSettings?.isScrollGesturesEnabled = false
                    false
                } else {
                    this.map?.uiSettings?.isScrollGesturesEnabled = true
                    true
                }
                true
            }

            R.id.tilt_gesture_enabled -> {

                item.isChecked = if (item.isChecked)
                {
                    this.map?.uiSettings?.isTiltGesturesEnabled = false
                    false
                } else {
                    this.map?.uiSettings?.isTiltGesturesEnabled = true
                    true
                }
                true
            }

            R.id.zoom_control_enabled -> {

                item.isChecked = if (item.isChecked)
                {
                    this.map?.uiSettings?.isZoomControlsEnabled = false
                    false
                } else {
                    this.map?.uiSettings?.isZoomControlsEnabled = true
                    true
                }
                true
            }

            R.id.zoom_gesture_enabled -> {

                item.isChecked = if (item.isChecked)
                {
                    this.map?.uiSettings?.isZoomGesturesEnabled = false
                    false
                } else {
                    this.map?.uiSettings?.isZoomGesturesEnabled = true
                    true
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun disableAllGestures(enabled:Boolean)
    {
        this.menu?.apply {

            for (item in iterator())
            {
                when(item.itemId)
                {
                    ViewByID.rotate_gesture_enabled, ViewByID.scroll_gesture_enabled, ViewByID.tilt_gesture_enabled, ViewByID.zoom_control_enabled, ViewByID.zoom_gesture_enabled -> {
                        item.isChecked = enabled
                    }
                }
            }
        }
    }

    private fun setMapType()
    {
        map_type_normal_radioButton?.isChecked = true
        map_type_normal_radioButton?.setOnCheckedChangeListener(this)
        map_type_satelite_radioButton?.setOnCheckedChangeListener(this)
        map_type_terrain_radioButton?.setOnCheckedChangeListener(this)
        map_type_hybrid_radioButton?.setOnCheckedChangeListener(this)
    }

    override fun onMapReady(googlemap: GoogleMap?) {
        this.map = googlemap
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

        when(buttonView?.id)
        {
            map_type_normal_radioButton?.id -> {

                if (isChecked)
                    this.map?.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
            map_type_satelite_radioButton?.id -> {

                if (isChecked)
                    this.map?.mapType = GoogleMap.MAP_TYPE_SATELLITE
            }
            map_type_terrain_radioButton?.id -> {

                if (isChecked)
                    this.map?.mapType = GoogleMap.MAP_TYPE_TERRAIN
            }
            map_type_hybrid_radioButton?.id -> {

                if (isChecked)
                    this.map?.mapType = GoogleMap.MAP_TYPE_HYBRID
            }
        }
    }
}