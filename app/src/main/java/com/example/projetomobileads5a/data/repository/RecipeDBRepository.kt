package com.example.projetomobileads5a.data.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.LongDef
import com.example.projetomobileads5a.data.model.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object RecipeDBRepository {

    private val auth = FirebaseAuth.getInstance()

    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    fun saveFavoriteRecipes(
        recipes: List<Recipe>,
        onComplete: (() -> Unit)? = null
    ) {
        val user = auth.currentUser

        if (user != null) {
            val favoritesRef = db.collection("users")
                .document(user.uid)
                .collection("favorites")

            favoritesRef.get()
                .addOnSuccessListener { snapshot ->
                    val existingIds = snapshot.documents.map { it.id }
                    val newIds = recipes.map { "recipeId_${it.id}" }
                    val toDelete = existingIds.filter { it !in newIds }

                    for (id in toDelete) {
                        favoritesRef.document(id).delete()
                            .addOnSuccessListener {
                                Log.d("RecipeDBRepository", "Receita removida: $id")
                            }
                            .addOnFailureListener {
                                Log.e("RecipeDBRepository", "Erro ao remover receita $id: ${it.message}")
                            }
                    }

                    for (recipe in recipes) {
                        favoritesRef.document("recipeId_${recipe.id}")
                            .set(recipe)
                            .addOnSuccessListener {
                                Log.d("RecipeDBRepository", "Receita ${recipe.title} salva com sucesso")
                            }
                            .addOnFailureListener {
                                Log.e("RecipeDBRepository", "Erro ao salvar receita: ${it.message}")
                            }
                    }

                    onComplete?.invoke()
                }
                .addOnFailureListener {
                    Log.e("RecipeDBRepository", "Erro ao buscar receitas: ${it.message}")
                    onComplete?.invoke()
                }
        } else {
            Log.w("RecipeDBRepository", "Usuário não autenticado")
            onComplete?.invoke()
        }
    }


    fun searchFavoriteRecipes(onResult: (List<Recipe>) -> Unit) {
        val user = auth.currentUser

        if (user != null) {
            val favoritesRef = db.collection("users").document(user.uid).collection("favorites")

            favoritesRef.get()
                .addOnSuccessListener { querySnapshot ->
                    val recipeList = querySnapshot.documents.mapNotNull { doc ->
                        doc.toObject(Recipe::class.java)
                    }
                    onResult(recipeList)
                }
                .addOnFailureListener {
                    Log.d("RecipeDBRepository", "Erro ao buscar receitas: ${it.message}")
                    onResult(emptyList())
                }
        } else {
            Log.d("RecipeDBRepository", "User não autenticado")
            onResult(emptyList())
        }
    }
}
