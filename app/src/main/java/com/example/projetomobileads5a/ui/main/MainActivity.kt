package com.example.projetomobileads5a.ui.main

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.projetomobileads5a.R
import com.example.projetomobileads5a.ui.home.HomeFragment
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

    private lateinit var auth: FirebaseAuth

    private lateinit var navigation: TabLayout
    private lateinit var contentFrame: FrameLayout

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()

        auth = Firebase.auth

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }

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