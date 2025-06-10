package com.example.projetomobileads5a.ui.favorites

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetomobileads5a.R
import com.example.projetomobileads5a.data.model.Recipe
import com.example.projetomobileads5a.data.repository.RecipeDBRepository
import com.example.projetomobileads5a.ui.auth.LoginActivity
import com.example.projetomobileads5a.ui.home.RecipeAdapter
import com.example.projetomobileads5a.ui.main.MainActivity
import com.example.projetomobileads5a.ui.recipe_detail.RecipeDetailActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class FavoritesActivity : AppCompatActivity() {
    private lateinit var favoriteRecyclerView: RecyclerView
    private lateinit var adapter: RecipeAdapter
    private var favoriteRecipes: MutableList<Recipe> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorites)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupBackButton()
        setupLogoutButton()

        adapter = RecipeAdapter(
            onItemClick = { recipe -> goToRecipeDetail(recipe) },
            saveRecipe = { recipe -> removeFromFavorites(recipe) }
        )

        favoriteRecyclerView = findViewById(R.id.favoriteRecyclerView)

        favoriteRecyclerView.layoutManager = LinearLayoutManager(this)
        favoriteRecyclerView.adapter = adapter

        loadFavorites()
    }

    private fun loadFavorites() {
        RecipeDBRepository.searchFavoriteRecipes { recipeList ->
            favoriteRecipes.clear()
            favoriteRecipes.addAll(recipeList)
            adapter.setData(favoriteRecipes)
            adapter.setFavoriteRecipes(favoriteRecipes)
        }
    }

    private fun goToRecipeDetail(recipe: Recipe) {
        val intent = Intent(this, RecipeDetailActivity::class.java)
        intent.putExtra("recipe", recipe)
        startActivity(intent)
    }

    private fun removeFromFavorites(recipe: Recipe) {
        favoriteRecipes.removeAll { it.id == recipe.id }
        adapter.setData(favoriteRecipes)
        adapter.setFavoriteRecipes(favoriteRecipes)
        RecipeDBRepository.saveFavoriteRecipes(favoriteRecipes)
    }

    private fun setupBackButton() {
        val backButton = findViewById<ImageButton>(R.id.backButtonFavorite)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun setupLogoutButton() {
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            Firebase.auth.signOut()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


}