package com.futurecode.ghostfinderradardetector.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.databinding.ActivityMainBinding
import com.futurecode.ghostfinderradardetector.utils.PrefManager
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null

    private lateinit var binding: ActivityMainBinding

    // ADD THIS to BOTH activities
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(MyApplication.setLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // MyApplication.app.checkInternetConnection()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        navController = navHostFragment!!.navController

        navController?.addOnDestinationChangedListener { controller, destination, bundle ->
            binding.flBanner.reload()
        }
    }

    fun goToMain() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}

