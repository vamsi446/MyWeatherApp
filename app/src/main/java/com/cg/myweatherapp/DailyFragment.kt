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
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A fragment representing a list of Items.
 */
class DailyFragment : Fragment() {
    lateinit var rView:RecyclerView
    private var columnCount = 1
    private var latitude:Double?=0.0
    private var longitude:Double?=0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
        val pref=activity?.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        latitude=pref?.getFloat("lat",0.0f)?.toDouble()
        longitude=pref?.getFloat("long",0.0f)?.toDouble()
        Log.d("DailyFragment","$latitude,$longitude")
        val exclude="alerts,minutely,hourly"

        val apiKey=resources.getString(R.string.apiKey)
        val units="metric"
        CoroutineScope(Dispatchers.Default).launch {
            val request=DailyInterface.getInstance().getDailyData("$latitude","$longitude",exclude,apiKey,units)
            request.enqueue(DailyDataCallback())

        }

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rView = inflater.inflate(R.layout.fragment_daily_list, container, false) as RecyclerView

        rView.layoutManager= LinearLayoutManager(context)


        return rView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            DailyFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
    inner class DailyDataCallback:Callback<DailyData>
    {
        override fun onResponse(call: Call<DailyData>, response: Response<DailyData>) {
            if(response.isSuccessful)
            {
                val data=response.body()!!

                Log.d("DailyFragment","success : $data")

                rView.adapter=DailyRecyclerViewAdapter(activity?.supportFragmentManager!!,data.timezone_offset,data?.daily!!)

            }
        }

        override fun onFailure(call: Call<DailyData>, t: Throwable) {
            Log.d("DailyFragment","Failure : ${t.message}")
        }

    }

}