package com.cg.myweatherapp

data class DailyData(val timezone_offset:Long,
                     val daily:List<Properties>)


data class Weather(val description:String,
                   val icon:String)

data class Temperature(val day:Double,
                       val min:Double,
                       val max:Double)

data class Properties(val dt:Long,
                      val sunrise:Long,
                      val sunset:Long,
                      val temp:Temperature,
                      val pressure: Int,
                      val humidity: Int,
                      val wind_speed: Double,
                      val weather: List<Weather>)

data class CurrentProperties(val dt:Long,
                       val sunrise: Long,
                       val sunset: Long,
                       val temp:Double,
                       val pressure:Int,
                       val humidity:Int,
                       val wind_speed:Double,
                       val weather: List<Weather>)

data class CurrentData(val timezone_offset:Long,
                       val current:CurrentProperties)


data class HourlyData(val timezone_offset: Long,
                      val hourly: List<HourlyProperties> )

data class HourlyProperties(val dt:Long,
                            val temp:Double,
                            val pressure: Int,
                            val humidity: Int,
                            val wind_speed: Double,
                            val weather: List<Weather>)