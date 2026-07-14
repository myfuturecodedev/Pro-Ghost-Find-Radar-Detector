package com.futurecode.ghostfinderradardetector.activity

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.databinding.ActivityMainBinding
import com.futurecode.ghostfinderradardetector.utils.PrefManager

class MainActivity : BaseActivity() {

    private var navController: NavController? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefManager = PrefManager.get(this)
        
        // Agar is session mein onboarding ho chuki hai, toh direct HomeActivity par jayein
        if (prefManager.isOnboardingDone) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            super.onCreate(savedInstanceState)
            return
        }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        navController = navHostFragment?.navController

        navController?.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.splashFragment) {
                binding.flBanner.reload()
            }
        }
    }

    fun goToMain() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
