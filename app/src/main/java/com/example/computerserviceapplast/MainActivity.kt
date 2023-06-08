package com.example.computerserviceapplast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.computerserviceapplast.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    /////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val bottomNavView = binding.bottomNavigation
        NavigationUI.setupWithNavController(bottomNavView, navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { controller, destination, arguments ->

            bottomNavView.isVisible = arguments?.getBoolean("bottomNavShow") ?: true


        }

        if(auth.currentUser == null){
            navHostFragment.navController
                .navigate(R.id.action_global_auth)
        }
        else{
            navHostFragment.navController
                .navigate(R.id.action_global_home)
        }


    }


    override fun onDestroy() {
        super.onDestroy()

    }
}