package com.example.projetomobileads5a.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetomobileads5a.R
import com.example.projetomobileads5a.ui.home.HomeViewModel
import com.example.projetomobileads5a.ui.home.RecipeAdapter

class RecipeListFragment : Fragment() {

    private lateinit var viewModel: RecipeListViewModel
    private lateinit var adapter: RecipeAdapter
    private lateinit var recipeListFragment: RecyclerView
    private var categoryFilter: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryFilter = arguments?.getString("filter")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.recipe_list_fragment, container, false)
        recipeListFragment = view.findViewById(R.id.recipeListFragment)

        adapter = RecipeAdapter()
        recipeListFragment.layoutManager = LinearLayoutManager(requireContext())
        recipeListFragment.adapter = adapter

        viewModel = ViewModelProvider(this).get(RecipeListViewModel::class.java)

        viewModel.recipes.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        fetchRecipesFromApi()

        return view
    }

    private fun fetchRecipesFromApi() {
        val query = categoryFilter ?: ""
        viewModel.searchRecipes(query)
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
