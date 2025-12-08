package com.example.leafai.weather

import okhttp3.OkHttpClient
import okhttp3.Request
import com.google.gson.Gson
import java.io.IOException

object WeatherService {
    private val client = OkHttpClient()
    private val gson = Gson()

    fun fetchWeather(city: String, apiKey: String, callback: (WeatherResponse?) -> Unit) {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey&units=metric"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.body?.string()?.let { body ->
                    val weatherResponse = gson.fromJson(body, WeatherResponse::class.java)
                    callback(weatherResponse)
                } ?: callback(null)
            }
        })
    }
}
