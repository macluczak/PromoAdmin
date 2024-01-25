package com.example.promoadmin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.promoadmin.databinding.ActivityAuthenticationBinding
import com.example.promoadmin.databinding.ActivitySplashBinding
import com.example.promoadmin.feature.authentication.AuthActivity
import com.example.promoadmin.feature.authentication.AuthViewModel
import com.example.promoadmin.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.wait
@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_splash) as NavHostFragment
        val navController = navHostFragment.navController
        navController
            .setGraph(R.navigation.entry_nav)
    }
}