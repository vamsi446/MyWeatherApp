package com.cg.myweatherapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_current.*
import kotlinx.android.synthetic.main.fragment_daily.*

class DetailedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_current)

        val date=intent.getStringExtra("date")
        val temp=intent.getStringExtra("temp")
        val desc=intent.getStringExtra("desc")
        val windSpeed=intent.getStringExtra("windSpeed")
        val pressure=intent.getStringExtra("pressure")
        val humidity=intent.getStringExtra("humidity")
        val sunrise=intent.getStringExtra("sunrise")?:""
        val sunset=intent.getStringExtra("sunset")?:""
        val icon=intent.getStringExtra("icon")
        val iconUrl="https://openweathermap.org/img/wn/$icon@2x.png"
        Log.d("DailyFragment","$iconUrl")
        Glide.with(this).load(Uri.parse(iconUrl)).into(currentIconIV)
        supportActionBar?.setTitle(date)

        if(sunrise.isEmpty() && sunset.isEmpty())
        {
            imageView4.visibility= View.INVISIBLE
            imageView7.visibility= View.INVISIBLE
            currentSunriseT.visibility= View.INVISIBLE
            currentSunsetT.visibility= View.INVISIBLE
        }

        currentDateT.setText(date)
        currentDescT.setText(desc)
        currentHumidityT.setText("$humidity %")
        currentPressureT.setText("$pressure hPa")
        currentSunriseT.setText(sunrise)
        currentSunsetT.setText(sunset)
        currentWindSpeedT.setText("$windSpeed m/s")
        currentTempT.setText("$temp °C")

    }
}