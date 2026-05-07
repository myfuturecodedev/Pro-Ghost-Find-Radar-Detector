package com.futurecode.ghostfinderradardetector.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    // Use lazy or initialize in onCreate
    private lateinit var binding: ActivityHomeBinding
    private var navController: NavController? = null


    // ADD THIS to BOTH activities
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(MyApplication.setLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Initialize View Binding
        binding = ActivityHomeBinding.inflate(layoutInflater)

        // 2. Set content view using binding.root
        setContentView(binding.root)

        // 3. Properly find the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment

    }



    // Optional: Handle the Up button (Back button) if you have an app bar
    override fun onSupportNavigateUp(): Boolean {
        return navController?.navigateUp() ?: super.onSupportNavigateUp()
    }
}