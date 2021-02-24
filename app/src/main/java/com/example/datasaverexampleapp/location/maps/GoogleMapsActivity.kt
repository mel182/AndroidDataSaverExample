package com.example.datasaverexampleapp.location.maps

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.alpha
import androidx.core.view.iterator
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.base_classes.BaseActivity
import com.example.datasaverexampleapp.type_alias.Drawable
import com.example.datasaverexampleapp.type_alias.Layout
import com.example.datasaverexampleapp.type_alias.ViewByID
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_google_maps.*

class GoogleMapsActivity : BaseActivity(Layout.activity_google_maps), OnMapReadyCallback, GoogleMap.OnMarkerClickListener , CompoundButton.OnCheckedChangeListener
{
    private var map:GoogleMap? = null
    private var menu:Menu? = null
    private var circle: Circle? = null
    private var polygon: Polygon? = null
    private val TAG = "GOOGLE_MAPS"

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

            R.id.location_zoom -> {
                animateLocationZoom()
                true
            }

            R.id.location_bound -> {
                animateLocationBound()
                true
            }

            R.id.location_tilt_bearing -> {
                animateLocationBearingTilt()
                true
            }

            R.id.add_marker -> {
                addMarkers()
                this@GoogleMapsActivity.menu?.findItem(ViewByID.remove_marker)?.also {
                    it.isEnabled = true
                }
                this@GoogleMapsActivity.menu?.findItem(ViewByID.add_marker_with_alpha)?.also {
                    it.isEnabled = false
                }
                this@GoogleMapsActivity.menu?.findItem(ViewByID.add_flat_marker)?.also {
                    it.isEnabled = false
                }
                this@GoogleMapsActivity.menu?.findItem(ViewByID.add_marker_anchor)?.also {
                    it.isEnabled = false
                }
                item.isEnabled = false
                true
            }

            R.id.add_marker_with_alpha -> {
                addMarkerWithAlpha(0.6f)
                this@GoogleMapsActivity.menu?.findItem(ViewByID.remove_marker)?.also {
                    it.isEnabled = true
                }
                this@GoogleMapsActivity.menu?.findItem(ViewByID.add_marker)?.also {
                    it.isEnabled = false
                }
                this@GoogleMapsActivity.menu?.findItem(ViewByID.add_flat_marker)?.also {
                    it.isEnabled = false
                }
                this@GoogleMapsActivity.menu?.findItem(ViewByID.add_marker_anchor)?.also {
                    it.isEnabled = false
                }
                item.isEnabled = false
                true
            }

            R.id.add_flat_marker -> {
                addFlatMarker()
                this@GoogleMapsActivity.menu?.findItem(ViewByID.remove_marker)?.also {
                    it.isEnabled = true
                }
                this@GoogleMapsActivity.menu?.findItem(ViewByID.add_marker)?.also {
                    it.isEnabled = false
                }
                this@GoogleMapsActivity.menu?.findItem(ViewByID.add_marker_with_alpha)?.also {
                    it.isEnabled = false
                }
                this@GoogleMapsActivity.menu?.findItem(ViewByID.add_marker_anchor)?.also {
                    it.isEnabled = false
                }
                item.isEnabled = false
                true
            }

            R.id.add_marker_anchor -> {
                addMarkerWithAnchor()
                this@GoogleMapsActivity.menu?.findItem(ViewByID.remove_marker)?.also {
                    it.isEnabled = true
                }
                this@GoogleMapsActivity.menu?.findItem(ViewByID.add_marker)?.also {
                    it.isEnabled = false
                }
                this@GoogleMapsActivity.menu?.findItem(ViewByID.add_marker_with_alpha)?.also {
                    it.isEnabled = false
                }
                this@GoogleMapsActivity.menu?.findItem(ViewByID.add_flat_marker)?.also {
                    it.isEnabled = false
                }
                item.isEnabled = false
                true
            }

            R.id.remove_marker -> {
                removeMarkers()

                this@GoogleMapsActivity.menu?.findItem(ViewByID.add_marker)?.also {
                    it.isEnabled = true
                }

                this@GoogleMapsActivity.menu?.findItem(ViewByID.add_marker_with_alpha)?.also {
                    it.isEnabled = true
                }

                this@GoogleMapsActivity.menu?.findItem(ViewByID.add_flat_marker)?.also {
                    it.isEnabled = true
                }

                this@GoogleMapsActivity.menu?.findItem(ViewByID.add_marker_anchor)?.also {
                    it.isEnabled = true
                }

                item.isEnabled = false
                true
            }

            R.id.circle -> {

                if (item.title == "Add circle")
                {
                    // Add circle
                    drawCircle()
                    item.title = "Remove circle"
                } else {
                    // Remove circle
                    circle?.remove()
                    item.title = "Add circle"
                }
                true
            }

            R.id.polygon -> {

                if (item.title == "Add polygon")
                {
                    // Add polygon
                    drawPolygon()
                    item.title = "Remove polygon"
                } else {
                    // Remove polygon
                    polygon?.remove()
                    polygon = null
                    this.menu?.apply {
                        findItem(ViewByID.polygon_with_geodesic)?.isEnabled = true
                        findItem(ViewByID.polygon_with_holes)?.isEnabled = true
                    }
                    item.title = "Add polygon"
                }
                true
            }

            R.id.polygon_with_geodesic -> {

                if (item.title == "Add polygon geodesic")
                {
                    // Add polygon with geodesic
                    drawPolygonWithGeodesic()
                    item.title = "Remove polygon geodesic"
                } else {
                    // Remove polygon with geodesic
                    polygon?.remove()
                    polygon = null
                    this.menu?.apply {
                        findItem(ViewByID.polygon)?.isEnabled = true
                        findItem(ViewByID.polygon_with_holes)?.isEnabled = true
                    }

                    item.title = "Add polygon geodesic"
                }
                true
            }

            R.id.polygon_with_holes -> {

                if (item.title == "Add polygon with holes")
                {
                    // Add polygon with holes
                    drawPolygonWithHoles()
                    item.title = "Remove polygon with holes"
                } else {
                    // Remove polygon with holes
                    polygon?.remove()
                    polygon = null
                    this.menu?.apply {
                        findItem(ViewByID.polygon)?.isEnabled = true
                        findItem(ViewByID.polygon_with_geodesic)?.isEnabled = true
                    }
                    item.title = "Add polygon with holes"
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

    private fun animateLocationZoom()
    {
        // The following list shows the approximate level of detail corresponding with a range of zoom levels:
        // - 1: World
        // - 5: Landmass/continent
        // - 10: City
        // - 15: Streets
        // - 20: Buildings

        val locationLat = 51.44532050293472
        val locationLong = 3.5702898372151664
        val locationLatLng = LatLng(locationLat,locationLong)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(locationLatLng,16f)
        this.map?.animateCamera(cameraUpdate)
    }

    private fun animateLocationBound()
    {
        val location1Lat = 51.446543764158385
        val location1Long = 3.571811651500974
        val location1 = LatLng(location1Lat,location1Long)

        val location2Lat = 51.44527953868199
        val location2Long = 3.570289837215167
        val location2 = LatLng(location2Lat,location2Long)

        val locationBound = LatLngBounds.builder()
            .include(location1)
            .include(location2)
            .build()

        val boundPadding = 16
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(locationBound,boundPadding)

        // Once you've created a new camera update, you must apply it using either the 'moveCamera' of 'animateCamera'
        // methods on the Google Map object. The 'moveCamera' method will cause the camera to immediately "jump" to the
        // new position and orientation, while 'animateCamera' will smoothly transition from the current Camera Position
        // to the new one; you can optionally choose to specify the duration of the animation.

        // Since animate camera updates can be interrupted, either by a user gesture or through a call to 'stopAnimation'.
        // If you want to be notified of a successful completion or interruption, you can pass in an optional 'CancelableCallback'.
        val duration = 2000
        this.map?.animateCamera(cameraUpdate, duration, object: GoogleMap.CancelableCallback{

            override fun onFinish() {
                // The camera update animation completed successfully
            }

            override fun onCancel() {
                // The camera update animation was cancelled
            }
        })
    }

    private fun animateLocationBearingTilt()
    {
        // This method modify the heading (rotation) or tilt (angle) of the camera, use the camera Position
        // Builder to generate a new Camera position, to be passed into the Camera Update Factory's static
        // 'newCameraPosition' method.

        val locationLat = 51.446543764158385
        val locationLong = 3.571811651500974
        val location = LatLng(locationLat,locationLong)

        val cameraPosition = CameraPosition.builder()
            .bearing(0f)
            .target(location)
            .tilt(10f)
            .zoom(15f)
            .build()

        val positionUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        this.map?.animateCamera(positionUpdate)
    }

    private var marker : Marker? = null

    @SuppressLint("NewApi")
    private fun addMarkers()
    {
        // You can add interactive, customizable markers to a Google Map using the 'addMarker' method,
        // passing in a 'MarkerOptions' object that specifies a latitude/longitude position at which to place
        // the marker

        if (marker == null)
        {
            val icon = getBitmap(resources.getDrawable(Drawable.ic_marker,null) as VectorDrawable)
            val locationLat = 51.44540510129993
            val locationLong = 3.5701767353453784
            val position = LatLng(locationLat,locationLong)
            marker = this.map?.addMarker(MarkerOptions().position(position).icon(icon).title("Aldi").snippet("This is a supermarker in Vlissingen"))
        }

        // When a Marker is selected, the map toolbar is displayed, providing the user with a shortcut to display,
        // or navigate to, the Marker location in the Google Maps app. To disable the toolbar modify the Google Map UI settings:
        // 'uiSettings.isMapToolbarEnabled = false'
    }

    @SuppressLint("NewApi")
    private fun addMarkerWithAlpha(alpha:Float)
    {
        if (marker == null)
        {
            val icon = getBitmap(resources.getDrawable(Drawable.ic_marker,null) as VectorDrawable)
            val locationLat = 51.44540510129993
            val locationLong = 3.5701767353453784
            val position = LatLng(locationLat,locationLong)
            marker = this.map?.addMarker(MarkerOptions()
                .position(position)
                .icon(icon)
                .title("Aldi")
                .snippet("This is a supermarket in Vlissingen")
                .alpha(alpha))
        }
    }

    @SuppressLint("NewApi")
    private fun addFlatMarker()
    {
        if (marker == null)
        {
            val icon = getBitmap(resources.getDrawable(Drawable.ic_marker,null) as VectorDrawable)
            val locationLat = 51.44540510129993
            val locationLong = 3.5701767353453784
            val position = LatLng(locationLat,locationLong)
            marker = this.map?.addMarker(MarkerOptions()
                .position(position)
                .icon(icon)
                .title("Aldi")
                .snippet("This is a supermarket in Vlissingen")
                .flat(true))
            // the flat method, you can set the orientation of the Marker to be flat against the Map, causing
            // it to rotate and change perspective as the map rotate or tilted
        }
    }

    @SuppressLint("NewApi")
    private fun addMarkerWithAnchor()
    {
        if (marker == null)
        {
            val icon = getBitmap(resources.getDrawable(Drawable.ic_marker,null) as VectorDrawable)
            val locationLat = 51.44540510129993
            val locationLong = 3.5701767353453784
            val position = LatLng(locationLat,locationLong)
            marker = this.map?.addMarker(MarkerOptions()
                .position(position)
                .icon(icon)
                .title("Aldi")
                .snippet("This is a supermarket in Vlissingen")
                .anchor(0.5f,0.5f)
                .rotation(90f))
            // This anchor option rotate a Marker around a specified anchor point, using a combination of the 'anchor'
            // and 'rotation' methods within the Marker Options. Rotation is measured in degrees clockwise, and anchor
            // represent the center of rotation in terms of a proportion of the size of the images in the horizontal
            // and vertical directions.
        }
    }

    private fun removeMarkers()
    {
        // To remove a Marker, you must maintain a reference to it when it's added and call its 'remove' method:
        this.marker?.remove()
        this.marker = null
    }

    private fun getBitmap(drawable: VectorDrawable): BitmapDescriptor
    {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onMapReady(googlemap: GoogleMap?) {

        googlemap?.apply {
            setOnMarkerClickListener(this@GoogleMapsActivity)
            setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter{

                // Returning a View from the 'getInfoWindow' handler will replace the info window in its
                // entirely, while returning a View only from the 'getInfoContents' handler will keep the
                // same and background as the default info window, replacing only the contents. If 'null'
                // is returned from both handler the default info window will be displayed.
                //
                //NOTE: The 'onMarkerClick(marker: Marker?)' must return 'false'.
                override fun getInfoWindow(p0: Marker?): View {
                    // Define a view to entirely replace the default info window
                    val view = windowInfoLayout?.apply {
                        findViewById<TextView>(ViewByID.info_window_title)?.apply {
                            text = p0?.title
                        }
                        findViewById<TextView>(ViewByID.info_window_description)?.apply {
                            text = p0?.snippet
                        }
                    }
                    return view?:windowInfoLayout
                }

                override fun getInfoContents(p0: Marker?): View {
                    // Define a view to replace the interior of the info window
                    // This method is only called if 'getInfoWindow(Marker)' first returns NULL
                    val view = windowInfoLayout?.apply {
                        findViewById<TextView>(ViewByID.info_window_title)?.apply {
                            text = p0?.title
                        }
                        findViewById<TextView>(ViewByID.info_window_description)?.apply {
                            text = p0?.snippet
                        }
                    }
                    return view?:windowInfoLayout
                }
            })
            this@GoogleMapsActivity.map = this
        }
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

    private val windowInfoLayout by lazy { layoutInflater.inflate(Layout.item_google_maps_marker_window_info_layout,null) }

    override fun onMarkerClick(marker: Marker?): Boolean {

        // The 'onMarkerClick' handler will receive an instance of the Marker selected. Return 'true' if your
        // handler should replace the default behaviour, or 'false' if the info window should still be displayed.
        Toast.makeText(this,"Marker clicked: ${marker?.title}",Toast.LENGTH_SHORT).show()
        return false
    }

    private fun drawCircle()
    {
        // Depending on the size and location of the circle, and the current zoom. The Mercator project used
        // by the Google Map may result in the circle appearing as an ellipse.
        val locationLat = 51.44540510129993
        val locationLong = 3.5701767353453784
        val position = LatLng(locationLat,locationLong)

        // To add a circle to your map, create a new 'CircleOptions' object, specifying the center and radius
        // of the circle and any additional settings such as fill color or stroke
        val circleOption = CircleOptions()
            .center(position)
            .radius(100.0) // 100 meter
            .fillColor( Color.argb(50,255,0,0))
            .strokeColor(Color.RED)

        // Pass the circle Options into the Google Map's 'addCircle' method. Note that it will return
        // a mutable 'Circle' object that can modified at run time.
        circle = this.map?.addCircle(circleOption)
    }

    private fun drawPolygon()
    {
        if (polygon != null)
            return

        // This is a irregular enclosed shape by defining a polygon using the 'PolygonOptions' class.
        // Use the 'add' method to define a series of latitude/longitude pairs that each define a node in the shape.
        // The default fill is transparent, so specify the fill, stroke and joint types as needed to modify the shape
        // appearance.
        val polygonOptions = PolygonOptions()
            .add(LatLng(51.44621138025606,3.565902060762918),
                 LatLng(51.44709618556605,3.568722739497014),
                 LatLng(51.44802754105433,3.5689282194047958),
                 LatLng(51.447980973731106,3.5736729372753935),
                 LatLng(51.446036745603166,3.5736168973005444),
                 LatLng(51.445164803755326,3.5735028331244156),
                 LatLng(51.444097641243566,3.5720449223313966),
                 LatLng(51.44332114491954,3.5728530193023365),
                 LatLng(51.44181008729837,3.5744523777480106),
                 LatLng(51.44695167623209,3.5794861482673306))
            .fillColor(Color.argb(50,255,0,0))

        // Note that the polygon will automatically join the last point to the first, so there's no need to close it
        // yourself. You can also use the polygon Options 'addAll' method to supply a List of 'LatLng' objects.
        polygon = this.map?.addPolygon(polygonOptions)

        this.menu?.apply {
            findItem(ViewByID.polygon_with_geodesic)?.isEnabled = false
            findItem(ViewByID.polygon_with_holes)?.isEnabled = false
        }
    }

    private fun drawPolygonWithGeodesic()
    {
        if (polygon != null)
            return

        // By default, the polygon is drawn as straight lines on the Mercator projection used to display
        // the Google Map. You can use the 'geodesic' method within the Polygon Options to request each segment
        // be drawn such that it represent the shortest path along the Earth's surface. Geodesic segments
        // will typically appear as curved lines when observed on the Google Map.
        val polygonOptions = PolygonOptions()
            .add(LatLng(51.44621138025606,3.565902060762918),
                LatLng(51.44709618556605,3.568722739497014),
                LatLng(51.44802754105433,3.5689282194047958),
                LatLng(51.447980973731106,3.5736729372753935),
                LatLng(51.446036745603166,3.5736168973005444),
                LatLng(51.445164803755326,3.5735028331244156),
                LatLng(51.444097641243566,3.5720449223313966),
                LatLng(51.44332114491954,3.5728530193023365),
                LatLng(51.44181008729837,3.5744523777480106),
                LatLng(51.44695167623209,3.5794861482673306))
            .fillColor(Color.argb(50,255,0,0))
            .geodesic(true) // Request geodesic option

        polygon = this.map?.addPolygon(polygonOptions)
        this.menu?.apply {
            findItem(ViewByID.polygon)?.isEnabled = false
            findItem(ViewByID.polygon_with_holes)?.isEnabled = false
        }
    }

    private fun drawPolygonWithHoles()
    {
        if (polygon != null)
            return

        val holes = listOf(
            LatLng(51.44641655824572, 3.5718260629023724),
            LatLng(51.44546172431629, 3.5717923921982533),
            LatLng(51.445052503663106, 3.5737116223330623)
        )

        val polygonOptions = PolygonOptions()
            .add(LatLng(51.44621138025606,3.565902060762918),
                LatLng(51.44709618556605,3.568722739497014),
                LatLng(51.44802754105433,3.5689282194047958),
                LatLng(51.447980973731106,3.5736729372753935),
                LatLng(51.446036745603166,3.5736168973005444),
                LatLng(51.445164803755326,3.5735028331244156),
                LatLng(51.444097641243566,3.5720449223313966),
                LatLng(51.44332114491954,3.5728530193023365),
                LatLng(51.44181008729837,3.5744523777480106),
                LatLng(51.44695167623209,3.5794861482673306))
            .fillColor(Color.argb(50,255,0,0))
            .addHole(holes)

        // Note that the polygon will automatically join the last point to the first, so there's no need to close it
        // yourself. You can also use the polygon Options 'addAll' method to supply a List of 'LatLng' objects.
        polygon = this.map?.addPolygon(polygonOptions)

        this.menu?.apply {
            findItem(ViewByID.polygon)?.isEnabled = false
            findItem(ViewByID.polygon_with_geodesic)?.isEnabled = false
        }
    }



}