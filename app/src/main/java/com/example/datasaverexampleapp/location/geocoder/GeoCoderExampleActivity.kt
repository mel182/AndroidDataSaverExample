package com.example.datasaverexampleapp.location.geocoder

import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.global_adapter.DataAdapter
import com.example.datasaverexampleapp.type_alias.Drawable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_geo_coder_example.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class GeoCoderExampleActivity : AppCompatActivity() {

    private lateinit var geocoder: Geocoder
    private var reverseGeoCoderAdapter:DataAdapter? = null
    private var forwardGeoCoderAdapter:DataAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geo_coder_example)
        title = "Geo coder Example"

        geocoder = Geocoder(this, Locale.getDefault())

        // Check if geo coder exist
        val geoCoderExist = Geocoder.isPresent()
        geocoder_available_state?.let { geoCoderStateText ->

            if (geoCoderExist)
            {
                geoCoderStateText.text = "Geo coder present"
                geoCoderStateText.setCompoundDrawablesWithIntrinsicBounds(0,0,Drawable.ic_check_mark,0)
            } else {
                geoCoderStateText.text = "Geo coder not present"
                geoCoderStateText.setCompoundDrawablesWithIntrinsicBounds(0,0,Drawable.ic_error_icon,0)
            }
        }

        // Important: The geo coder backend services may have limits to the number and frequency of request.
        //            The limits of the Google Maps-based service include:
        //            * A maximum of 2500 requests per day per device
        //            * No more than 50 QPS (Queries per second)

        reverse_geocoder_result_list?.apply {
            layoutManager = LinearLayoutManager(this@GeoCoderExampleActivity)
            reverseGeoCoderAdapter = DataAdapter()
            adapter = reverseGeoCoderAdapter
        }

        test_reverse_geocoding_button?.setOnClickListener {
            val location = LatLng(51.4462131,3.57172)
            geocoding_lat_lng?.text = "Lat: ${location.latitude} - long: ${location.longitude}"
            reverseGeoCoding(location)
        }

        // Forward geocoding
        forward_geocoder_result_list?.apply {
            layoutManager = LinearLayoutManager(this@GeoCoderExampleActivity)
            forwardGeoCoderAdapter = DataAdapter()
            adapter = forwardGeoCoderAdapter
        }

        forward_geocoding_button?.setOnClickListener {
            val address = "Hobeinstraat 10, 4381PD Vlissingen, Netherlands"
            forward_geocoder_address?.text = address
            forwardGeoCoding(address)
        }
    }

    private fun reverseGeoCoding(location:LatLng)
    {
        no_address_found_view?.visibility = View.GONE

        try {
            val result = geocoder.getFromLocation(location.latitude, location.longitude, 10)
            if (result != null && result.isNotEmpty())
            {
                val dataList:ArrayList<String> = ArrayList()
                for (addressFound in result)
                {
                    val addressLineIndex = addressFound.maxAddressLineIndex

                    if (addressLineIndex != -1)
                    {
                        for (addressLineFound in 0..addressLineIndex)
                        {
                            val addressLine = addressFound.getAddressLine(addressLineFound)
                            // The accuracy and granularity of reverse lookup are entirely dependent on the quality
                            // of data in the geocoding database; as a result, the quality of the results may vary widely
                            // between different countries and locales.
                            dataList.add(addressLine)
                        }
                    }
                }
                reverseGeoCoderAdapter?.update(dataList)
            } else {
                no_result_text?.text = "No address found"
                no_address_found_view?.visibility = View.VISIBLE
            }
        }catch (e:IOException)
        {
            no_result_text?.text = "Error executing reverse geocoding, reason: ${e.message}"
            no_address_found_view?.visibility = View.VISIBLE
        }
    }

    private fun forwardGeoCoding(address:String)
    {
        forward_geocoder_no_address_found_view?.visibility = View.GONE

        try {
            val result = geocoder.getFromLocationName(address, 10)

            if (result != null && result.isNotEmpty())
            {
                val dataList:ArrayList<String> = ArrayList()
                for (addressFound in result)
                {
                    val addressLineIndex = addressFound.maxAddressLineIndex

                    if (addressLineIndex != -1)
                    {
                        for (addressLineFound in 0..addressLineIndex)
                        {
                            val addressLine = addressFound.getAddressLine(addressLineFound)
                            dataList.add(addressLine)
                        }
                    }
                }
                forwardGeoCoderAdapter?.update(dataList)
            } else {
                forward_geocoder_no_result_text?.text = "No location found"
                forward_geocoder_no_address_found_view?.visibility = View.VISIBLE
            }
        }catch (e:IOException)
        {
            forward_geocoder_no_result_text?.text = "Error executing forward geocoding, reason: ${e.message}"
            forward_geocoder_no_address_found_view?.visibility = View.VISIBLE
        }
    }
}