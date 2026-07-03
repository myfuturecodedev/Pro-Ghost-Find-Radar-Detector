package com.futurecode.ghostfinderradardetector.fragment.prelogin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.activity.HomeActivity
import com.futurecode.ghostfinderradardetector.activity.MainActivity
import com.futurecode.ghostfinderradardetector.activity.MyApplication
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentSplashBinding
import com.futurecode.ghostfinderradardetector.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private var progressStatus = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startProgress()
    }

    private fun startProgress() {
        viewLifecycleOwner.lifecycleScope.launch {
            while (progressStatus < 100) {
                delay(50) // 5 seconds total
                progressStatus += 1

                bindingOrNull?.let { b ->
                    b.progressBar.progress = progressStatus
                    b.tvProgress.text = "$progressStatus%"
                }
            }
            navigateToNext()
        }
    }

    private fun navigateToNext() {

        Utils.showInterstitialAd(requireActivity(), true) {
            /*findNavController().navigate(
                if (MyApplication.app.prefManager.isloggedIn) R.id.action_splashFragment_to_permissionFlashlightFragment else R.id.action_splashFragment_to_languageFragment,
                Bundle().apply {
                    putString("from", "splash")
                },
                NavOptions.Builder()
                    .setPopUpTo(R.id.splashFragment, true)
                    .build()
            )*/
//            if (isAdded) {
                if (prefManager.isOnboardingDone) {
                    // If onboarding is already done, go directly to Home (switch graph)
                    // findNavController().setGraph(R.navigation.nav_afterlogin)
//                    (activity as? MainActivity)?.goToMain()
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                    requireActivity().finish()
                } else {
                    // First time user: go to Language Selection
                    findNavController().navigate(R.id.action_splashFragment_to_languageFragment)
                    NavOptions.Builder()
                        .setPopUpTo(R.id.splashFragment, true)
                        .build()
                }
//            }

        }
    }
}