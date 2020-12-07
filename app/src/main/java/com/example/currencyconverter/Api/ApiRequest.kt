package com.example.currencyconverter.Api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface ApiRequest {

    @GET("latest?access_key=b1807ad38518144b575da96a8d16defc")
    fun getCurrency(): Call<JsonObject>
}