package com.example.projetomobileads5a.data.model.recipe_detail

data class RecipeDetail (
    val id: Int,
    val title: String,
    val dishTypes: List<String>,
    val extendedIngredients: List<Ingredient>,
    val analyzedInstructions: List<Instruction>,
    val instructions: String
)
