package com.example.projetomobileads5a.ui.recipe

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.projetomobileads5a.R
import com.example.projetomobileads5a.ui.home.RecipeAdapter
import com.example.projetomobileads5a.ui.recipe_detail.RecipeDetailActivity
import com.example.projetomobileads5a.ui.shared.RecipeSharedViewModel

class RecipeListFragment : Fragment() {

    private lateinit var recipeViewModel: RecipeSharedViewModel
    private lateinit var adapter: RecipeAdapter
    private lateinit var recipeListFragment: RecyclerView
    private var categoryFilter: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryFilter = arguments?.getString("filter")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.recipe_list_fragment, container, false)
        recipeListFragment = view.findViewById(R.id.recipeListFragment)

        adapter = RecipeAdapter { recipe ->
            val intent = Intent(requireContext(), RecipeDetailActivity::class.java)
            intent.putExtra("recipe", recipe)
            startActivity(intent)
        }
        recipeListFragment.layoutManager = LinearLayoutManager(requireContext())
        recipeListFragment.adapter = adapter

        recipeViewModel = ViewModelProvider(this)[RecipeSharedViewModel::class.java]

        recipeViewModel.recipes.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        fetchRecipesFromApi()
        setupSwipeRefresh(view)

        return view
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
