package com.cg.myweatherapp


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

/**
 * A fragment representing a list of Items.
 */
class HourlyFragment : Fragment() {
    lateinit var rView:RecyclerView

    private var columnCount = 1
    private var latitude:Double?=null
    private var longitude:Double?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)

        }
        val pref=activity?.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        latitude=pref?.getFloat("lat",0.0f)?.toDouble()
        longitude=pref?.getFloat("long",0.0f)?.toDouble()
        CoroutineScope(Dispatchers.Default).launch { val exclude="alerts,minutely,current,daily"
            val apiKey=resources.getString(R.string.apiKey)
            val units="metric"
            val request=DailyInterface.getInstance().getHourlyData("$latitude","$longitude",exclude,apiKey,units)
            request.enqueue(HourlyDataCallback()) }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rView = inflater.inflate(R.layout.fragment_hourly_list, container, false) as RecyclerView

        rView.layoutManager=LinearLayoutManager(context)

        return rView
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                HourlyFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
    inner class HourlyDataCallback: Callback<HourlyData>
    {
        override fun onResponse(call: Call<HourlyData>, response: Response<HourlyData>) {
            if(response.isSuccessful)
            {
                val data=response.body()!!

                //Log.d("HourlyFragment","success : $data")
                rView.adapter=HourlyRecyclerViewAdapter(activity!!.supportFragmentManager,data.timezone_offset,data.hourly)
            }
        }

        override fun onFailure(call: Call<HourlyData>, t: Throwable) {
            Log.d("HourlyFragment","Failure : ${t.message}")
        }

    }
}