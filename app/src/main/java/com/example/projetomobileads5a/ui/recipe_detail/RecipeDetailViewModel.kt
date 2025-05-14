package com.example.projetomobileads5a.ui.recipe_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetomobileads5a.data.api.RetrofitInstance
import com.example.projetomobileads5a.data.model.recipe_detail.RecipeDetail
import kotlinx.coroutines.launch

class RecipeDetailViewModel : ViewModel() {

    private val _recipeDetail = MutableLiveData<RecipeDetail?>()
    val recipeDetail: LiveData<RecipeDetail?> = _recipeDetail

    fun getRecipeDetail(recipeId: Int) {
        viewModelScope.launch {
            try {
                val recipeDetail = RetrofitInstance.api.getRecipeDetail(recipeId)
                _recipeDetail.value = recipeDetail
            } catch (e: Exception) {
                Log.e(e.message, e.toString())
                _recipeDetail.value = null
            }
        }
    }

}