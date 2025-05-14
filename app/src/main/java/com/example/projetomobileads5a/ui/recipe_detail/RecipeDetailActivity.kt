package com.example.projetomobileads5a.ui.recipe_detail

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.projetomobileads5a.R
import com.example.projetomobileads5a.data.model.Recipe
import com.example.projetomobileads5a.data.model.recipe_detail.Ingredient
import com.example.projetomobileads5a.data.model.recipe_detail.Step

class RecipeDetailActivity : AppCompatActivity() {

    private val recipeDetailModel: RecipeDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dishTypes = findViewById<TextView>(R.id.dishTypes)

        val ingredientsContainer = findViewById<LinearLayout>(R.id.ingredientsContainer)
        val stepsContainer = findViewById<LinearLayout>(R.id.stepsContainer)

        val recipe = intent.getParcelableExtra<Recipe>("recipe")

        recipeDetailModel.recipeDetail.observe(this) { recipeDetail ->
            if (recipeDetail != null) {
                dishTypes.text = recipeDetail.dishTypes.joinToString(" | ")
                setIngredientsList(recipeDetail.extendedIngredients, ingredientsContainer)

                val steps = recipeDetail.analyzedInstructions.firstOrNull()?.steps.orEmpty()
                setStepsList(steps, stepsContainer)
            }
        }

        if (recipe != null) {
            loadRecipeInfos(recipe)
            recipeDetailModel.getRecipeDetail(recipe.id)
        }

        setupBackButton()
    }

    private fun setupBackButton() {
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun loadRecipeInfos(recipe: Recipe) {
        val recipeTitle = findViewById<TextView>(R.id.recipeDetailTitle)
        val recipeImage = findViewById<ImageView>(R.id.recipeDetailImage)

        recipeTitle.text = recipe.title
        Glide.with(this).load(recipe.image).into(recipeImage)
    }

    private fun setIngredientsList(ingredients: List<Ingredient>, container: LinearLayout) {
        container.removeAllViews()

        ingredients.forEach { ingredient ->
            val view = layoutInflater.inflate(R.layout.item_ingredient, container, false)
            view.findViewById<TextView>(R.id.itemIngredientText).text = ingredient.original
            container.addView(view)
        }
    }

    private fun setStepsList(steps: List<Step>, container: LinearLayout) {
        container.removeAllViews()

        steps.forEach { step ->
            val view = layoutInflater.inflate(R.layout.item_step, container, false)
            view.findViewById<TextView>(R.id.stepNumber).text = step.number.toString()
            view.findViewById<TextView>(R.id.stepText).text = step.step
            container.addView(view)
        }
    }

}