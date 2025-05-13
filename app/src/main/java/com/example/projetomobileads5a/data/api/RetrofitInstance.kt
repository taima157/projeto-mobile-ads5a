package com.example.projetomobileads5a.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: SpoonacularApi by lazy {
        retrofit.create(SpoonacularApi::class.java)
    }

}