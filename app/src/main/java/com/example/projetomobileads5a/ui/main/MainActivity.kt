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

    private lateinit var navigation: TabLayout
    private lateinit var contentFrame: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()

        navigation = findViewById(R.id.navigation)
        contentFrame = findViewById(R.id.contentFrame)

        showTabContent(0)

        navigation.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                showTabContent(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun showTabContent(position: Int) {

        val fragment = when (position) {
            0 -> HomeFragment()
            1 -> RecipeListFragment.newInstance("desserts")
            2 -> RecipeListFragment.newInstance("pasta")
            3 -> RecipeListFragment.newInstance("savory")
            else -> HomeFragment()
        }

        supportFragmentManager.beginTransaction().replace(R.id.contentFrame, fragment).commit()
    }
}