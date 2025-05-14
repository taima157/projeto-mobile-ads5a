package com.example.projetomobileads5a.ui.home

import android.content.Intent
import android.os.Bundle
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
import com.example.projetomobileads5a.ui.recipe_detail.RecipeDetailActivity
import com.example.projetomobileads5a.ui.shared.RecipeSharedViewModel

class HomeFragment : Fragment() {

    private lateinit var recipeViewModel: RecipeSharedViewModel
    private lateinit var adapter: RecipeAdapter
    private lateinit var recipeListView: RecyclerView
    private lateinit var searchInput: EditText
    private lateinit var searchIcon: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recipeListView = view.findViewById(R.id.recipeListView)
        searchInput = view.findViewById(R.id.searchInput)
        searchIcon = view.findViewById(R.id.searchIcon)

        adapter = RecipeAdapter{ recipe ->
            val intent = Intent(requireContext(), RecipeDetailActivity::class.java)
            intent.putExtra("recipe", recipe)
            startActivity(intent)
        }
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

        setupSwipeRefresh(view)

        return view
    }

    private fun setupSwipeRefresh(view: View) {
        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshHome)
        swipeRefresh?.setOnRefreshListener {
            recipeViewModel.getRandomRecipes()
            swipeRefresh.isRefreshing = false
        }
    }

}