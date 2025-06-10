package com.example.projetomobileads5a.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.projetomobileads5a.R
import com.example.projetomobileads5a.ui.favorites.FavoritesActivity
import com.example.projetomobileads5a.ui.home.HomeFragment
import com.example.projetomobileads5a.ui.auth.LoginActivity
import com.example.projetomobileads5a.ui.recipe.RecipeListFragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var homeFragment: HomeFragment
    private lateinit var dessertsFragment: RecipeListFragment
    private lateinit var pastaFragment: RecipeListFragment
    private lateinit var savoryFragment: RecipeListFragment

    private lateinit var navigation: TabLayout
    private lateinit var contentFrame: FrameLayout

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()

        auth = Firebase.auth;

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
                if (tab.position < 4) {
                    showTabContent(tab.position)
                } else {
                    isAuthenticated()
                    navigation.getTabAt(0)?.select()
                }
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

    private fun isAuthenticated() {
        val currentUser = auth.currentUser

        Log.d("currentUser", currentUser.toString())

        val intent: Intent

        intent = if (currentUser == null) {
            Intent(this, LoginActivity::class.java)
        } else {
            Intent(this, FavoritesActivity::class.java)
        }

        startActivity(intent)
    }

}