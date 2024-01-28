package com.example.promoadmin.ui

import android.os.Bundle
import android.view.WindowManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.setupWithNavController
import com.example.api.user.model.User
import com.example.promoadmin.R
import com.example.promoadmin.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val args: HomeActivityArgs by navArgs()
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        window.statusBarColor = resources.getColor(R.color.white)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUser = args.userObject

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        if (currentUser.role != "admin"){
            navView.menu.findItem(R.id.navigation_admin).isVisible = false
        }

        navView.setupWithNavController(navController)

    }
}