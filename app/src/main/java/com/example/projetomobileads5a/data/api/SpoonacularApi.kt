package com.example.projetomobileads5a.data.api

import com.example.projetomobileads5a.data.model.ApiResponse
import com.example.projetomobileads5a.data.model.ApiResponseRandom
import com.example.projetomobileads5a.data.model.recipe_detail.RecipeDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApi {

    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @Query("query") query: String? = null,
        @Query("number") number: Int = 10,
        @Query("apiKey") apiKey: String = API_KEY
    ) : ApiResponse

    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int = 10,
        @Query("apiKey") apiKey: String = API_KEY
    ) : ApiResponseRandom

    @GET("recipes/random")
    suspend fun getRandomRecipesWithTags(
        @Query("include-tags") includeTags: String? = null,
        @Query("number") number: Int = 10,
        @Query("apiKey") apiKey: String = API_KEY
    ) : ApiResponseRandom

    @GET("recipes/{idRecipe}/information")
    suspend fun getRecipeDetail(
        @Path("idRecipe") idRecipe: Int,
        @Query("apiKey") apiKey: String = API_KEY
    ) : RecipeDetail

    companion object {
        const val API_KEY = "4089987db1c44f1a88ace3c27cc29813"
    }

}