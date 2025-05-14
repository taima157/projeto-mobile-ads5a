package com.example.projetomobileads5a.ui.shared

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetomobileads5a.data.api.RetrofitInstance
import com.example.projetomobileads5a.data.model.Recipe
import kotlinx.coroutines.launch

class RecipeSharedViewModel : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    fun searchRecipes(query: String? = null) {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.getRecipes(query)
                _recipes.value = result.results
            } catch (e: Exception) {
                Log.e(e.message, e.toString())
                _recipes.value = emptyList()
            }
        }
    }

    fun getRandomRecipes() {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.getRandomRecipes()
                _recipes.value = result.recipes
            } catch (e: Exception) {
                Log.e(e.message, e.toString())
                _recipes.value = emptyList()
            }
        }
    }

    fun getRandomRecipesWithTags(tags: String) {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.getRandomRecipesWithTags(tags)
                _recipes.value = result.recipes
            } catch (e: Exception) {
                Log.e(e.message, e.toString())
                _recipes.value = emptyList()
            }
        }
    }
}