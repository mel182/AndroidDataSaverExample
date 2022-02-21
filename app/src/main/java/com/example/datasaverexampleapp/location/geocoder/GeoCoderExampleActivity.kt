package com.example.datasaverexampleapp.location.geocoder

import android.location.Geocoder
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityGeoCoderExampleBinding
import com.example.datasaverexampleapp.global_adapter.DataAdapter
import com.example.datasaverexampleapp.type_alias.Drawable
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.*

class GeoCoderExampleActivity : AppCompatActivity() {

    private lateinit var geocoder: Geocoder
    private var reverseGeoCoderAdapter:DataAdapter? = null
    private var forwardGeoCoderAdapter:DataAdapter? = null
    private var binding: ActivityGeoCoderExampleBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geo_coder_example)
        title = "Geo coder Example"

        geocoder = Geocoder(this, Locale.getDefault())

        binding?.apply {
            // Check if geo coder exist
            val geoCoderExist = Geocoder.isPresent()
            geocoderAvailableState.let { geoCoderStateText ->

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

            reverseGeocoderResultList.apply {
                layoutManager = LinearLayoutManager(this@GeoCoderExampleActivity)
                reverseGeoCoderAdapter = DataAdapter()
                adapter = reverseGeoCoderAdapter
            }

            testReverseGeocodingButton.setOnClickListener {
                val location = LatLng(51.4462131,3.57172)
                geocodingLatLng.text = "Lat: ${location.latitude} - long: ${location.longitude}"
                reverseGeoCoding(location)
            }

            // Forward geocoding
            forwardGeocoderResultList.apply {
                layoutManager = LinearLayoutManager(this@GeoCoderExampleActivity)
                forwardGeoCoderAdapter = DataAdapter()
                adapter = forwardGeoCoderAdapter
            }

            forwardGeocodingButton.setOnClickListener {
                val address = "Hobeinstraat 10, 4381PD Vlissingen, Netherlands"
                forwardGeocoderAddress.text = address
                forwardGeoCoding(address)
            }
        }
    }

    private fun reverseGeoCoding(location:LatLng)
    {
        binding?.apply {
            noAddressFoundView.visibility = View.GONE

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
                    noResultText.text = "No address found"
                    noAddressFoundView.visibility = View.VISIBLE
                }
            }catch (e:IOException)
            {
                noResultText.text = "Error executing reverse geocoding, reason: ${e.message}"
                noAddressFoundView.visibility = View.VISIBLE
            }
        }
    }

    private fun forwardGeoCoding(address:String)
    {
        binding?.apply {
            forwardGeocoderNoAddressFoundView.visibility = View.GONE

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
                                // This overload is particularly useful when working with a map, letting you restrict the search to the visible area.
                                dataList.add(addressLine)
                            }
                        }
                    }
                    forwardGeoCoderAdapter?.update(dataList)
                } else {
                    forwardGeocoderNoResultText.text = "No location found"
                    forwardGeocoderNoAddressFoundView.visibility = View.VISIBLE
                }
            }catch (e:IOException)
            {
                forwardGeocoderNoResultText.text = "Error executing forward geocoding, reason: ${e.message}"
                forwardGeocoderNoAddressFoundView.visibility = View.VISIBLE
            }
        }
    }
}