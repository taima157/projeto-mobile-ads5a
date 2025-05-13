package com.example.projetomobileads5a.data.api

import com.example.projetomobileads5a.data.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SpoonacularApi {

    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @Query("query") query: String? = null,
        @Query("number") number: Int = 10,
        @Query("apiKey") apiKey: String = API_KEY
    ) : ApiResponse

    companion object {
        const val API_KEY = "4089987db1c44f1a88ace3c27cc29813"
    }

}