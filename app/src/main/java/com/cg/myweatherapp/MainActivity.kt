package com.cg.myweatherapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.lang.Exception

val PREF_NAME="location"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()
        setContentView(R.layout.activity_main)
    }


    fun buttonClick(view: View) {
        when(view.id)
        {

            R.id.cityB->{

                val cityName=cityE.text.toString()
                if(cityName.isNotEmpty())
                {
                    if(validCity(cityName))
                    {
                        try {
                            val geoCoder= Geocoder(this)
                            val locationFromCity=geoCoder.getFromLocationName(cityName,1)
                            Log.d("MainActivity","$locationFromCity")
                            Log.d("MainActivity", "City Latitude: ${locationFromCity[0].latitude},Longitude: ${locationFromCity[0].longitude}")
                            val intent= Intent(this,NavigationActivity::class.java)
                            val pref=getSharedPreferences(PREF_NAME, MODE_PRIVATE)
                            val editor=pref.edit()
                            editor.putFloat("lat",locationFromCity[0].latitude.toFloat())
                            editor.putFloat("long",locationFromCity[0].longitude.toFloat())
                            intent.putExtra("subAdminArea",locationFromCity[0].subAdminArea)
                            intent.putExtra("city",cityName)

                            startActivity(intent)
                        }
                        catch (exception:Exception)
                        {
                           Toast.makeText(this,"No Data available for the $cityName or Check Your Network", Toast.LENGTH_SHORT).show()
                        }

                    }
                    else
                    {
                        Toast.makeText(this,"Enter Valid City!",Toast.LENGTH_SHORT).show()

                    }
                }
                else
                {
                    Toast.makeText(this,"Enter City Name!",Toast.LENGTH_SHORT).show()
                }

            }
            R.id.currentLocB->{
                val lManager = view.context.getSystemService(LOCATION_SERVICE) as LocationManager
                val providerList = lManager.getProviders(true)
                var providerName = ""
                if (providerList.isNotEmpty()) {
                    if (providerList.contains(LocationManager.GPS_PROVIDER)) {
                        providerName = LocationManager.GPS_PROVIDER
                    } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
                        providerName = LocationManager.NETWORK_PROVIDER
                    } else {
                        providerName = providerList[0]
                    }
                    Log.d("LocationFragment"," Location provider name:$providerName")

                }
                //val currentLoc=lManager.getCurrentLocation(providerName,,,)
                val currentLoc=lManager.getLastKnownLocation(providerName)
                Log.d("MainActivity","Current Location: $currentLoc")
                Log.d(
                    "MainActivity",
                    "Current Location latitude:${currentLoc?.latitude},Longitude:${currentLoc?.longitude}"
                )
                val pref=getSharedPreferences(PREF_NAME, MODE_PRIVATE)
                val editor=pref.edit()
                editor.putFloat("lat",currentLoc?.latitude!!.toFloat())
                editor.putFloat("long",currentLoc?.longitude!!.toFloat())
                editor.commit()
                Log.d("MainActivity",currentLoc.toString())
                val intent=Intent(this,NavigationActivity::class.java)
                val geocoder=Geocoder(this)
                val loc=geocoder.getFromLocation(currentLoc?.latitude!!,currentLoc?.longitude!!,1)
                Log.d("MainActivity","${loc[0]}")
                intent.putExtra("subAdminArea",loc[0].subAdminArea)
                startActivity(intent)

            }
        }
    }

    private fun validCity(cityName: String): Boolean {
        var city=cityName.toLowerCase()
        for(char in city)
        {
            if(!char.isLetter())
                return false

        }
        return true

    }

    fun checkPermissions()
    {
        //min SDK 23
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1
            )
        }
        else
        {
            // Toast.makeText(this, "Location permission Granted", Toast.LENGTH_SHORT).show()
        }

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(grantResults.isNotEmpty())
        {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED &&
                grantResults[1]== PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show()
            }
            else
            {
                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



}