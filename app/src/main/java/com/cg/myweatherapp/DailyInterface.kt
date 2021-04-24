package com.cg.myweatherapp


import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface DailyInterface {
    @GET("data/2.5/onecall")
    fun getDailyData(@Query("lat") lat:String,
                     @Query("lon") lon:String,
                     @Query("exclude") exclude:String,
                     @Query("appid") appid:String,
                     @Query("units") units:String): Call<DailyData>
    @GET("data/2.5/onecall")
    fun getCurrentData(@Query("lat") lat:String,
                     @Query("lon") lon:String,
                     @Query("exclude") exclude:String,
                     @Query("appid") appid:String,
                     @Query("units") units:String): Call<CurrentData>

    @GET("data/2.5/onecall")
    fun getHourlyData(@Query("lat") lat:String,
                      @Query("lon") lon:String,
                      @Query("exclude") exclude:String,
                      @Query("appid") appid:String,
                      @Query("units") units:String):Call<HourlyData>
    companion object{
        val BASE_URL="https://api.openweathermap.org/"
        fun getInstance():DailyInterface{
            val builder=Retrofit.Builder()
            builder.addConverterFactory(GsonConverterFactory.create())
            builder.baseUrl(BASE_URL)
            val retrofit=builder.build()
            return retrofit.create(DailyInterface::class.java)
        }

    }
}