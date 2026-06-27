package com.futurecode.ghostfinderradardetector.fragment.prelogin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.activity.MainActivity
import com.futurecode.ghostfinderradardetector.adapter.OnBoardingAdapter
import com.futurecode.ghostfinderradardetector.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentOnBoardingBinding
import com.futurecode.ghostfinderradardetector.model.OnBoardingModel
import com.futurecode.ghostfinderradardetector.utils.Utils.setAdClickListener
import com.google.android.material.tabs.TabLayoutMediator

class OnBoardingFragment : BaseFragment<FragmentOnBoardingBinding>(FragmentOnBoardingBinding::inflate) {

    private lateinit var onBoardingAdapter: OnBoardingAdapter
    private val onBoardingList = mutableListOf<OnBoardingModel>()

    lateinit var fullScreenAdsHelper: FullScreenAdsHelper


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fullScreenAdsHelper= FullScreenAdsHelper(requireActivity())

        initData()
        setupViewPager()
        setupListeners()
    }

    private fun initData() {
        onBoardingList.clear()
        onBoardingList.add(
            OnBoardingModel(
                getString(R.string.onboarding_title_1),
                getString(R.string.onboarding_desc),
                R.drawable.onboard1
            )
        )
        onBoardingList.add(
            OnBoardingModel(
                getString(R.string.onboarding_title_2),
                getString(R.string.onboarding_desc),
                R.drawable.onboard2
            )
        )
        onBoardingList.add(
            OnBoardingModel(
                getString(R.string.onboarding_title_3),
                getString(R.string.onboarding_desc),
                R.drawable.onboard3
            )
        )
    }

    private fun setupViewPager() {
        onBoardingAdapter = OnBoardingAdapter(onBoardingList)
        binding.viewPager.adapter = onBoardingAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == onBoardingList.size - 1) {
                    binding.tvBtnText.text = getString(R.string.get_started)
                } else {
                    binding.tvBtnText.text = getString(R.string.next)
                }
            }
        })
    }

    private fun setupListeners() {
        binding.btnNext.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            val currentItem = binding.viewPager.currentItem
            if (currentItem < onBoardingList.size - 1) {
                binding.viewPager.currentItem = currentItem + 1
            } else {
                finishOnboarding()
            }
        }
    }

    private fun finishOnboarding() {
        prefManager.isOnboardingDone = true
        // Switch to the main application graph (Home) directly without restarting the activity.
        // This prevents the SplashFragment from showing again immediately after onboarding.
       // findNavController().setGraph(R.navigation.nav_afterlogin)
        // Switch to the main application graph safely
//        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
//            findNavController().setGraph(R.navigation.nav_afterlogin)
//        }


        Handler(Looper.getMainLooper()).postDelayed({
            if (isAdded) {
                // findNavController().navigate(R.id.action_splashFragment_to_languageFragment)
                (activity as? MainActivity)?.goToMain()

            }
        }, 4000)
    }
}