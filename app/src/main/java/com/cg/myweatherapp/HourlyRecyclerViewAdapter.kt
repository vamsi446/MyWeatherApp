package com.cg.myweatherapp

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
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.cg.myweatherapp.R


class HourlyRecyclerViewAdapter(val fragmentManager: FragmentManager,private val offset:Long,
        private val values: List<HourlyProperties>)
    : RecyclerView.Adapter<HourlyRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_hourly, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=values[position]
        Log.d("HourlyFragment","onBindViewHolder")
        val date= formatDate(item.dt*1000+offset)
        val icon=item.weather[0].icon
        val time= formatTime(item.dt*1000+offset)
        holder.tempT.setText("${item.temp} °C")
        holder.descT.setText(item.weather[0].description)
        holder.dateT.setText(date)
        holder.timeT.setText(time)

        val iconUrl="https://openweathermap.org/img/wn/$icon@2x.png"

        Glide.with(holder.itemView.context).load(Uri.parse(iconUrl)).into(holder.iconIV)
        holder.itemView.setOnClickListener{
//            val bundle=Bundle()
//            val currentFragment=CurrentFragment()
//            fragmentManager.beginTransaction()
//                    .addToBackStack(null)
//                    .replace(R.id.nav_host_fragment,currentFragment)
//                    .commit()
            val intent= Intent(it.context,DetailedActivity::class.java)
            intent.putExtra("date",date)
            intent.putExtra("temp", "${item.temp}")
            intent.putExtra("desc",item.weather[0].description)
            intent.putExtra("windSpeed","${item.wind_speed}")
            intent.putExtra("pressure","${item.pressure}")
            intent.putExtra("humidity","${item.humidity}")
            intent.putExtra("icon",icon)
            it.context.startActivity(intent)


        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateT: TextView = view.findViewById(R.id.hourlyDateT)
        val timeT: TextView = view.findViewById(R.id.hourlyTimeT)
        val descT: TextView = view.findViewById(R.id.hourlyDescT)
        val iconIV:ImageView=view.findViewById(R.id.hourlyIconIV)
        val tempT:TextView=view.findViewById(R.id.hourlyTempT)



    }
}