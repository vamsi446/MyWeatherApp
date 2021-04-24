package com.cg.myweatherapp


import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_current.*
import kotlinx.android.synthetic.main.fragment_current.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CurrentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CurrentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var latitude: Double? = null
    private var longitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref=activity?.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        latitude=pref?.getFloat("lat",0.0f)?.toDouble()
        longitude=pref?.getFloat("long",0.0f)?.toDouble()
        Log.d("CurrentFragment","$latitude,$longitude")

    }

    inner class CurrentDataCallback:Callback<CurrentData>
    {
        override fun onResponse(call: Call<CurrentData>, response: Response<CurrentData>) {
            if(response.isSuccessful)
            {
                val data=response.body()!!
                Log.d("CurrentFragment","success : $data")
                val currentOffset=data.timezone_offset
                val currentTemp=data.current.temp
                val currentDate=data.current.dt*1000+currentOffset
                val currentDesc=data.current.weather[0].description
                val currentIcon=data.current.weather[0].icon
                val currentHumidity=data.current.humidity
                val currentPressure=data.current.pressure
                val currentWindSpeed=data.current.wind_speed
                val currentSunrise=data.current.sunrise*1000+currentOffset
                val currentSunset=data.current.sunset*1000+currentOffset
                val formattedDate= formatDate(currentDate)
                val iconUrl="https://openweathermap.org/img/wn/$currentIcon@2x.png"
                Glide.with(context!!).load(Uri.parse(iconUrl)).into(currentIconIV)
                currentDateT.setText(formattedDate)
                currentTempT.setText("$currentTemp °C")
                currentDescT.setText("$currentDesc")
                currentWindSpeedT.setText("$currentWindSpeed m/s")
                currentHumidityT.setText("$currentHumidity %")
                currentPressureT.setText("$currentPressure hPa")
                val sunriseTimeObj= formatTime(currentSunrise)
                val sunsetTimeObj= formatTime(currentSunset)
                currentSunriseT.setText("$sunriseTimeObj")
                currentSunsetT.setText("$sunsetTimeObj")


            }
        }

        override fun onFailure(call: Call<CurrentData>, t: Throwable) {
            Log.d("CurrentFragment","Failure : ${t.message}")
        }

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_current, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val exclude = "daily,alerts,minutely,hourly"
        val apiKey = resources.getString(R.string.apiKey)
        val units = "metric"
        CoroutineScope(Dispatchers.Default).launch {
            val request = DailyInterface.getInstance()
                .getCurrentData("$latitude", "$longitude", exclude, apiKey, units)
            request.enqueue(CurrentDataCallback())
        }
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CurrentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(latitude:Double, longitude:Double) =
                CurrentFragment().apply {
                    arguments = Bundle().apply {
                        putDouble(ARG_PARAM1, latitude)
                        putDouble(ARG_PARAM2, longitude)
                    }
                }
    }
}