package com.cg.myweatherapp

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.cg.myweatherapp.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.time.Month
import java.util.*


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
val MONTH= listOf<String>("January","February","March","April","May","June","July","August","September","November","December")
fun formatDate(millisTime: Long): String {
    val dateObj=Date(millisTime)
    val date=dateObj.date
    val month=MONTH.get(dateObj.month)
    var year=dateObj.year
    year=year+1900
    return "$date $month $year"

}
fun formatTime(time: Long): String {
    val dateObj=Date(time)
    val hours=dateObj.hours
    val min=dateObj.minutes
    return "$hours:$min"

}

class DailyRecyclerViewAdapter( val fragMan:FragmentManager,private val timezone_offset:Long,
                               private val values: List<Properties>
                               ) : RecyclerView.Adapter<DailyRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_daily, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        var millisTime=item.dt*1000
        val offset=timezone_offset
        val icon=item.weather[0].icon
        val desc=item.weather[0].description
        val temp=item.temp.day

        var sunriseTime=(item.sunrise*1000)+offset
        var sunsetTime=(item.sunset*1000)+offset

        val minTemp=item.temp.min
        val maxTemp=item.temp.max
        val formattedDate=formatDate(millisTime)
        val riseTime=formatTime(sunriseTime)
        val setTime=formatTime(sunsetTime)
        holder.itemView.setOnClickListener{

            val intent= Intent(it.context,DetailedActivity::class.java)
            intent.putExtra("date",formattedDate)
            intent.putExtra("temp","$temp")
            intent.putExtra("desc",desc)
            intent.putExtra("windSpeed","${item.wind_speed}")
            intent.putExtra("pressure","${item.pressure}")
            intent.putExtra("humidity","${item.humidity}")
            intent.putExtra("sunrise",riseTime)
            intent.putExtra("sunset",setTime)
            intent.putExtra("icon",icon)
            it.context.startActivity(intent)
           /* val bundle= Bundle()
            bundle.putString("from","daily")
            bundle.putString("date",formattedDate)
            bundle.putString("temp","$temp")
            bundle.putString("desc",desc)
            bundle.putString("windSpeed", "${item.wind_speed}")
            bundle.putString("pressure","${item.pressure}")
            bundle.putString("humidity","${item.humidity}")
            bundle.putString("sunrise",riseTime)
            bundle.putString("sunset",setTime)
            bundle.putString("icon",icon)*/

            //issue with onbackpress
            /*val currentFragment=CurrentFragment()
            currentFragment.arguments=bundle
                    fragMan?.beginTransaction()
                    ?.add(R.id.nav_host_fragment,currentFragment)

                    ?.commit()*/
        }

        holder.dateTV.setText("$formattedDate")
        holder.tempTV.setText("$temp °C")
        holder.maxTempTV.setText("$maxTemp °C")
        holder.minTempTV.setText("$minTemp °C")

        val iconUrl="https://openweathermap.org/img/wn/$icon@2x.png"
        Log.d("DailyFragment","$iconUrl")
       Glide.with(holder.itemView.context).load(Uri.parse(iconUrl)).into(holder.iconIV)

        //Picasso.with(holder.itemView.context).load(Uri.parse(iconUrl)).into(holder.iconIV);



    }




    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTV: TextView = view.findViewById(R.id.dateT)

        val iconIV:ImageView=view.findViewById(R.id.imageView)
        val tempTV:TextView=view.findViewById(R.id.tempT)

        val minTempTV:TextView=view.findViewById(R.id.minT)
        val maxTempTV:TextView=view.findViewById(R.id.maxT)


    }
}
