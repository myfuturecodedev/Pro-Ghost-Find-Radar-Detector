package com.futurecode.ghostfinderradardetector.fragment.prelogin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.activity.HomeActivity
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentSplashBinding
import com.futurecode.ghostfinderradardetector.utils.JsonReadUtils
import com.futurecode.ghostfinderradardetector.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private var progressStatus = 0
    private var isApiDone = false
    private var progressJob: Job? = null
    private var isNavigated = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val skipSplash = requireActivity().intent.getBooleanExtra("skip_splash", false)

        if (skipSplash) {
            JsonReadUtils.fetchJsonData(requireContext()) {
                navigateToNext()
            }
        } else {
            startProgressLoading()
            JsonReadUtils.fetchJsonData(requireContext()) {
                isApiDone = true
            }
        }
    }

    private fun startProgressLoading() {
        progressJob = viewLifecycleOwner.lifecycleScope.launch {
            // Phase 1: Jab tak API response nahi aata, dheere-dheere 90% tak le jao
            while (progressStatus < 90) {
                delay(30) 
                progressStatus += 1
                updateUI(progressStatus)
                if (isApiDone) break
            }

            // Phase 2: Agar API response aa gaya hai toh 100% tak fast le jao
            while (progressStatus < 100) {
                if (isApiDone) {
                    delay(10)
                    progressStatus += 1
                } else {
                    // API slow hai, toh 90% par thoda wait karo
                    delay(100)
                }
                updateUI(progressStatus)
            }

            navigateToNext()
        }
    }

    private fun updateUI(progress: Int) {
        bindingOrNull?.let { b ->
            b.progressBar.progress = progress
          //  b.tvProgress.text = "$progress%"
        }
    }

    private fun navigateToNext() {
        if (isNavigated || !isAdded) return
        isNavigated = true

        Utils.showInterstitialAd(requireActivity(), true) {
            if (isAdded) {
                lifecycleScope.launch {
                    when {
                        prefManager.isOnboardingDone -> {
                            startActivity(Intent(requireContext(), HomeActivity::class.java))
                            requireActivity().finish()
                        }
                        prefManager.isLanguageChangedFromSplash -> {
                            findNavController().navigate(R.id.action_splashFragment_to_onBoardingFragment)
                        }
                        else -> {
                            findNavController().navigate(R.id.action_splashFragment_to_languageFragment)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        progressJob?.cancel()
        super.onDestroyView()
    }
}
