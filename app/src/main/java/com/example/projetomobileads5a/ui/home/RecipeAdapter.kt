package com.example.projetomobileads5a.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projetomobileads5a.R
import com.example.projetomobileads5a.data.model.Recipe

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    private var recipes: List<Recipe> = listOf()

    fun setData(data: List<Recipe>) {
        recipes = data
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.recipeTitle)
        val image: ImageView = view.findViewById(R.id.recipeImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = recipes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.title.text = recipe.title
        // Use Coil, Glide ou Picasso para carregar a imagem:
        Glide.with(holder.itemView).load(recipe.image).into(holder.image)
    }
}