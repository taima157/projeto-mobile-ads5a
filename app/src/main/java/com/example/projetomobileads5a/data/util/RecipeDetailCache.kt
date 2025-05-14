package com.example.projetomobileads5a.data.util

import com.example.projetomobileads5a.data.model.recipe_detail.RecipeDetail

object RecipeDetailCache {

    private val cache = mutableMapOf<Int, RecipeDetail>()

    fun get(id: Int): RecipeDetail? = cache[id]
    fun put(id: Int, detail: RecipeDetail) {
        cache[id] = detail
    }

}