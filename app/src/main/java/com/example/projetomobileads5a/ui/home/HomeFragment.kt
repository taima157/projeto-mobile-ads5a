package com.example.projetomobileads5a.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.projetomobileads5a.R
import com.example.projetomobileads5a.data.model.Recipe
import com.example.projetomobileads5a.data.repository.RecipeDBRepository
import com.example.projetomobileads5a.ui.auth.LoginActivity
import com.example.projetomobileads5a.ui.favorites.FavoritesActivity
import com.example.projetomobileads5a.ui.recipe_detail.RecipeDetailActivity
import com.example.projetomobileads5a.ui.shared.RecipeSharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var recipeViewModel: RecipeSharedViewModel
    private lateinit var adapter: RecipeAdapter
    private lateinit var recipeListView: RecyclerView
    private lateinit var searchInput: EditText
    private lateinit var searchIcon: ImageView
    private lateinit var auth: FirebaseAuth

    private var favoriteRecipes: MutableList<Recipe> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recipeListView = view.findViewById(R.id.recipeListView)
        searchInput = view.findViewById(R.id.searchInput)
        searchIcon = view.findViewById(R.id.searchIcon)

        auth = Firebase.auth

        adapter = RecipeAdapter(
            { recipe -> goToRecipeDetail(recipe)},
            { recipe -> saveRecipe(recipe) }
        )
        recipeListView.layoutManager = LinearLayoutManager(requireContext())
        recipeListView.adapter = adapter

        recipeViewModel = ViewModelProvider(this)[RecipeSharedViewModel::class.java]

        recipeViewModel.recipes.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        recipeViewModel.getRandomRecipes()

        searchIcon.setOnClickListener {
            val query = searchInput.text.toString()
            recipeViewModel.searchRecipes(query)
        }

        updateFavoriteList()
        setupSwipeRefresh(view)

        return view
    }

    override fun onResume() {
        super.onResume()
        updateFavoriteList()
    }

    private fun goToRecipeDetail(recipe: Recipe) {
        val intent = Intent(requireContext(), RecipeDetailActivity::class.java)
        intent.putExtra("recipe", recipe)
        startActivity(intent)
    }

    private fun saveRecipe(recipe: Recipe) {
        if (auth.currentUser == null) {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            return
        }

        val existRecipe = favoriteRecipes.find { it.id == recipe.id }

        if (existRecipe == null) {
            favoriteRecipes.add(recipe)
        } else {
            favoriteRecipes.remove(existRecipe)
        }

        adapter.setFavoriteRecipes(favoriteRecipes)
        RecipeDBRepository.saveFavoriteRecipes(favoriteRecipes)
    }

    private fun updateFavoriteList() {
        RecipeDBRepository.searchFavoriteRecipes { recipeList ->
            Log.d("recipeList", recipeList.toString())
            favoriteRecipes.clear()
            favoriteRecipes.addAll(recipeList)
            adapter.setFavoriteRecipes(favoriteRecipes)
        }
    }

    private fun setupSwipeRefresh(view: View) {
        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshHome)
        swipeRefresh?.setOnRefreshListener {
            recipeViewModel.getRandomRecipes()
            swipeRefresh.isRefreshing = false
        }
    }

}