package com.example.projetomobileads5a.ui.recipe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.projetomobileads5a.R
import com.example.projetomobileads5a.data.model.Recipe
import com.example.projetomobileads5a.data.repository.RecipeDBRepository
import com.example.projetomobileads5a.ui.auth.LoginActivity
import com.example.projetomobileads5a.ui.home.RecipeAdapter
import com.example.projetomobileads5a.ui.recipe_detail.RecipeDetailActivity
import com.example.projetomobileads5a.ui.shared.RecipeSharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecipeListFragment : Fragment() {

    private lateinit var recipeViewModel: RecipeSharedViewModel
    private lateinit var adapter: RecipeAdapter
    private lateinit var recipeListFragment: RecyclerView
    private lateinit var auth: FirebaseAuth

    private var categoryFilter: String? = null
    private var favoriteRecipes: MutableList<Recipe> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryFilter = arguments?.getString("filter")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.recipe_list_fragment, container, false)
        recipeListFragment = view.findViewById(R.id.recipeListFragment)

        auth = Firebase.auth

        adapter = RecipeAdapter(
            { recipe -> goToRecipeDetail(recipe)},
            { recipe -> saveRecipe(recipe) }
        )

        recipeListFragment.layoutManager = LinearLayoutManager(requireContext())
        recipeListFragment.adapter = adapter

        recipeViewModel = ViewModelProvider(this)[RecipeSharedViewModel::class.java]

        recipeViewModel.recipes.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        fetchRecipesFromApi()
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

    private fun fetchRecipesFromApi() {
        val query = categoryFilter ?: ""
        recipeViewModel.getRandomRecipesWithTags(query)
    }

    private fun setupSwipeRefresh(view: View) {
        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        swipeRefresh?.setOnRefreshListener {
            fetchRecipesFromApi()
            swipeRefresh.isRefreshing = false
        }
    }

    companion object {
        fun newInstance(filter: String?): RecipeListFragment {
            val fragment = RecipeListFragment()
            val args = Bundle()
            args.putString("filter", filter)
            fragment.arguments = args
            return fragment
        }
    }
}
