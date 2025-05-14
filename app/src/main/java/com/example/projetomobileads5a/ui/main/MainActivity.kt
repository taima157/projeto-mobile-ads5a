package com.example.projetomobileads5a.ui.main

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.projetomobileads5a.R
import com.example.projetomobileads5a.ui.home.HomeFragment
import com.example.projetomobileads5a.ui.recipe.RecipeListFragment
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var homeFragment: HomeFragment
    private lateinit var dessertsFragment: RecipeListFragment
    private lateinit var pastaFragment: RecipeListFragment
    private lateinit var savoryFragment: RecipeListFragment

    private lateinit var navigation: TabLayout
    private lateinit var contentFrame: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()

        navigation = findViewById(R.id.navigation)
        contentFrame = findViewById(R.id.contentFrame)

        homeFragment = HomeFragment()
        dessertsFragment = RecipeListFragment.newInstance("desserts")
        pastaFragment = RecipeListFragment.newInstance("pasta")
        savoryFragment = RecipeListFragment.newInstance("savory")

        supportFragmentManager.beginTransaction().add(R.id.contentFrame, homeFragment, "HOME")
            .commit()

        navigation.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                showTabContent(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun showTabContent(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()

        supportFragmentManager.fragments.forEach {
            transaction.hide(it)
        }

        val fragment = when (position) {
            0 -> homeFragment
            1 -> dessertsFragment
            2 -> pastaFragment
            3 -> savoryFragment
            else -> homeFragment
        }

        if (!fragment.isAdded) {
            transaction.add(R.id.contentFrame, fragment)
        } else {
            transaction.show(fragment)
        }

        transaction.commit()
    }
}