package com.example.projetomobileads5a.ui.home

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
import com.example.projetomobileads5a.R

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
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

        adapter = RecipeAdapter()
        recipeListView.layoutManager = LinearLayoutManager(requireContext())
        recipeListView.adapter = adapter

        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        recipeListView = view.findViewById(R.id.recipeListView)
        searchInput = view.findViewById(R.id.searchInput)
        searchIcon = view.findViewById(R.id.searchIcon)

        adapter = RecipeAdapter()
        recipeListView.layoutManager = LinearLayoutManager(requireContext())
        recipeListView.adapter = adapter

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        viewModel.recipes.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        viewModel.searchRecipes(null)

        searchIcon.setOnClickListener {
            val query = searchInput.text.toString()
            viewModel.searchRecipes(query)
        }

        return view
    }

}