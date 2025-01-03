@file:Suppress("UNNECESSARY_SAFE_CALL")

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
import androidx.core.view.iterator
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.base_classes.BaseActivity
import com.example.datasaverexampleapp.databinding.ActivityGoogleMapsBinding
import com.example.datasaverexampleapp.type_alias.Drawable
import com.example.datasaverexampleapp.type_alias.Layout
import com.example.datasaverexampleapp.type_alias.ViewByID
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class GoogleMapsActivity : BaseActivity(Layout.activity_google_maps), OnMapReadyCallback, GoogleMap.OnMarkerClickListener , CompoundButton.OnCheckedChangeListener, GoogleMap.OnCircleClickListener, GoogleMap.OnPolygonClickListener, GoogleMap.OnPolylineClickListener
{
    private var map:GoogleMap? = null
    private var menu:Menu? = null
    private var circle: Circle? = null
    private var polygon: Polygon? = null
    private var polyline: Polyline? = null
    private var groundOverlay: GroundOverlay? = null
    private var marker : Marker? = null
    private val TAG = "GOOGLE_MAPS"
    private var binding: ActivityGoogleMapsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityGoogleMapsBinding>(
            this, Layout.activity_google_maps
        )

        // Obtain the support map fragment and request the Google Map object.
        supportFragmentManager.findFragmentById(ViewByID.map)?.apply {

            if (this is SupportMapFragment)
                getMapAsync(this@GoogleMapsActivity)
        }

        setMapType()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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

            ViewByID.all_gesture_enabled -> {

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

            ViewByID.indoor_level_picker_enabled -> {

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

            ViewByID.map_toolbar -> {

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

            ViewByID.rotate_gesture_enabled -> {

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

            ViewByID.scroll_gesture_enabled -> {

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

            ViewByID.tilt_gesture_enabled -> {

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

            ViewByID.zoom_control_enabled -> {

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

            ViewByID.zoom_gesture_enabled -> {

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

            ViewByID.location_zoom -> {
                animateLocationZoom()
                true
            }

            ViewByID.location_bound -> {
                animateLocationBound()
                true
            }

            ViewByID.location_tilt_bearing -> {
                animateLocationBearingTilt()
                true
            }

            ViewByID.add_marker -> {
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

            ViewByID.add_marker_with_alpha -> {
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

            ViewByID.add_flat_marker -> {
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

            ViewByID.add_marker_anchor -> {
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

            ViewByID.remove_marker -> {
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

            ViewByID.circle -> {

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

            ViewByID.polygon -> {

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

            ViewByID.polygon_with_geodesic -> {

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

            ViewByID.polygon_with_holes -> {

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

            ViewByID.polyline -> {

                if (item.title == "Add polyline")
                {
                    // Add polyline
                    drawPolyline()
                    item.title = "Remove polyline"
                } else {
                    // Remove polyline
                    polyline?.remove()
                    polyline = null
                    item.title = "Add polyline"
                }
                true
            }

            ViewByID.add_image_ground_overlay -> {

                if (item.title == "Add image ground overlay")
                {
                    // Add image ground overlay
                    addImageOverlay()
                    item.title = "Remove image ground overlay"
                } else {
                    // Remove image ground overlay
                    groundOverlay?.remove()
                    groundOverlay = null
                    item.title = "Add image ground overlay"
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
        binding?.apply {
            mapTypeNormalRadioButton.isChecked = true
            mapTypeNormalRadioButton.setOnCheckedChangeListener(this@GoogleMapsActivity)
            mapTypeSateliteRadioButton.setOnCheckedChangeListener(this@GoogleMapsActivity)
            mapTypeTerrainRadioButton.setOnCheckedChangeListener(this@GoogleMapsActivity)
            mapTypeHybridRadioButton.setOnCheckedChangeListener(this@GoogleMapsActivity)
        }
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

    override fun onMapReady(googlemap: GoogleMap) {

        googlemap?.apply {
            setOnMarkerClickListener(this@GoogleMapsActivity)
            setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter{

                // Returning a View from the 'getInfoWindow' handler will replace the info window in its
                // entirely, while returning a View only from the 'getInfoContents' handler will keep the
                // same and background as the default info window, replacing only the contents. If 'null'
                // is returned from both handler the default info window will be displayed.
                //
                //NOTE: The 'onMarkerClick(marker: Marker?)' must return 'false'.
                override fun getInfoWindow(p0: Marker): View {
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

                override fun getInfoContents(p0: Marker): View {
                    // Define a view to replace the interior of the info window
                    // This method is only called if 'getInfoWindow(Marker)' first returns NULL
                    val view = windowInfoLayout?.apply {
                        findViewById<TextView>(ViewByID.info_window_title)?.apply {
                            text = p0.title
                        }
                        findViewById<TextView>(ViewByID.info_window_description)?.apply {
                            text = p0.snippet
                        }
                    }
                    return view?:windowInfoLayout
                }
            })

            // The click handlers for each Listener receive an instance of the Shape that was clicked!
            // Note: If multiple shapes or Markers overlap at the touch point, the click event is sent first
            //       to the markers, then to each shape (in z-index order) until a marker or shape with a click
            //       handler is found.
            //       AT MOST ONE HANDLER WILL BE TRIGGERED
            setOnCircleClickListener(this@GoogleMapsActivity)
            setOnPolygonClickListener(this@GoogleMapsActivity)
            setOnPolylineClickListener(this@GoogleMapsActivity)

            this@GoogleMapsActivity.map = this
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

        binding?.apply {
            when(buttonView?.id)
            {
                mapTypeNormalRadioButton.id -> {

                    if (isChecked)
                        this@GoogleMapsActivity.map?.mapType = GoogleMap.MAP_TYPE_NORMAL
                }
                mapTypeSateliteRadioButton.id -> {

                    if (isChecked)
                        this@GoogleMapsActivity.map?.mapType = GoogleMap.MAP_TYPE_SATELLITE
                }
                mapTypeTerrainRadioButton.id -> {

                    if (isChecked)
                        this@GoogleMapsActivity.map?.mapType = GoogleMap.MAP_TYPE_TERRAIN
                }
                mapTypeHybridRadioButton.id -> {

                    if (isChecked)
                        this@GoogleMapsActivity.map?.mapType = GoogleMap.MAP_TYPE_HYBRID
                }
            }
        }
    }

    private val windowInfoLayout by lazy { layoutInflater.inflate(Layout.item_google_maps_marker_window_info_layout,null) }

    override fun onMarkerClick(marker: Marker): Boolean {

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
        this.map?.addCircle(circleOption)?.also { circle ->
            circle.isClickable = true
            this.circle = circle
        }
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
                 LatLng(51.44181008729837,3.5744523777480106))
            .fillColor(Color.argb(50,255,0,0))

        // Note that the polygon will automatically join the last point to the first, so there's no need to close it
        // yourself. You can also use the polygon Options 'addAll' method to supply a List of 'LatLng' objects.
        this.map?.addPolygon(polygonOptions)?.also { polygon ->
            polygon.isClickable = true
            this.polygon = polygon
        }
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
                LatLng(51.44181008729837,3.5744523777480106))
            .fillColor(Color.argb(50,255,0,0))
            .geodesic(true) // Request geodesic option

        this.map?.addPolygon(polygonOptions)?.also { polygon ->
            polygon.isClickable = true
            this.polygon = polygon
        }
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
                LatLng(51.44181008729837,3.5744523777480106))
            .fillColor(Color.argb(50,255,0,0))
            .addHole(holes)

        // Note that the polygon will automatically join the last point to the first, so there's no need to close it
        // yourself. You can also use the polygon Options 'addAll' method to supply a List of 'LatLng' objects.
        this.map?.addPolygon(polygonOptions)?.also { polygon ->
            polygon.isClickable = true
            this.polygon = polygon
        }

        this.menu?.apply {
            findItem(ViewByID.polygon)?.isEnabled = false
            findItem(ViewByID.polygon_with_geodesic)?.isEnabled = false
        }
    }

    private fun drawPolyline()
    {
        if (polyline != null)
            return

        // A polyline is defined in much the same way as a Polygon; however, the ends won't be connected and
        // the shape can't be filled. Create a new 'PolyLineOptions' object, specifying the point individually,
        // or as a List, using the 'add' method as described for Polygons.
        val polylineOptions = PolylineOptions()
            .add(LatLng(51.44621138025606,3.565902060762918),
                LatLng(51.44709618556605,3.568722739497014),
                LatLng(51.44802754105433,3.5689282194047958),
                LatLng(51.447980973731106,3.5736729372753935),
                LatLng(51.446036745603166,3.5736168973005444),
                LatLng(51.445164803755326,3.5735028331244156),
                LatLng(51.444097641243566,3.5720449223313966),
                LatLng(51.44332114491954,3.5728530193023365),
                LatLng(51.44181008729837,3.5744523777480106))
            .geodesic(true)

        // Polyline segments can be geodesic, and you define the color and style of the stroke, the joint
        // types and end caps. Once defined, use the 'addPolyline' method to add the Polyline Options to your
        // Google Map.
        this.map?.addPolyline(polylineOptions)?.also { polyline ->
            polyline.isClickable = true
            this.polyline = polyline
        }
    }

    @SuppressLint("NewApi")
    private fun addImageOverlay()
    {
        if (groundOverlay != null)
            return

        // In addition to Markers and shapes, it's also possible to create a 'GroundOverlay', which will place
        // an image tied to latitude/longitude coordinates over section of the map.
        val icon = getBitmap(resources.getDrawable(Drawable.ic_marker,null) as VectorDrawable)
        val rottnest = LatLng(51.44621138025606,3.565902060762918)

        // To add a Ground Overlay, create a new GroundOverlayOptions, specifying the image to overlay as
        // a 'BitmapDescriptor' as well as the position at which to place the image. The image position can
        // specified as either a 'LatLng' anchor at the south West point with a width (and optionally height), or
        // as a 'LatLngBound' that contains both the South West and North East anchors.
        val rottnestOverlay = GroundOverlayOptions().image(icon).position(rottnest,200f,200f)
        groundOverlay = this.map?.addGroundOverlay(rottnestOverlay)

        // Note: The length and width of Ground Overlay must be powers of two. If your source image doesn't conform
        //       to this requirement, it will be adjusted.
    }

    override fun onCircleClick(circle: Circle) {
        Toast.makeText(this,"Circle clicked!",Toast.LENGTH_SHORT).show()
    }

    override fun onPolygonClick(polygon: Polygon) {
        Toast.makeText(this,"Polygon clicked!",Toast.LENGTH_SHORT).show()
    }

    override fun onPolylineClick(polyline: Polyline) {
        Toast.makeText(this,"Polyline clicked!",Toast.LENGTH_SHORT).show()
    }
}