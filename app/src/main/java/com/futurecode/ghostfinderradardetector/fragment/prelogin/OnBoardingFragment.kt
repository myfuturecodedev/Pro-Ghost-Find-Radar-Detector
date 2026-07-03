package com.futurecode.ghostfinderradardetector.fragment.prelogin

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.activity.MainActivity
import com.futurecode.ghostfinderradardetector.adapter.AdEnabledPagerMapper
import com.futurecode.ghostfinderradardetector.adapter.AdPagerItem
import com.futurecode.ghostfinderradardetector.adapter.AdPagerPlacementConfig
import com.futurecode.ghostfinderradardetector.adapter.AdPagerTimerController
import com.futurecode.ghostfinderradardetector.adapter.OnBoardingAdapter
import com.futurecode.ghostfinderradardetector.ads.ads_new.ExistingNativeAdPageLoader
import com.futurecode.ghostfinderradardetector.ads.ads_new.NativeAdPagerController
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentOnBoardingBinding
import com.futurecode.ghostfinderradardetector.model.OnBoardingModel
import com.google.android.material.tabs.TabLayoutMediator

class OnBoardingFragment :
    BaseFragment<FragmentOnBoardingBinding>(FragmentOnBoardingBinding::inflate) {

    private lateinit var adapter: OnBoardingAdapter
    private var timerController: AdPagerTimerController? = null

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (::adapter.isInitialized) {
                adapter.onPageSelected(position)
                updateUIForPosition(position)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val onboardingList = listOf(
            OnBoardingModel(
                getString(R.string.onboarding_title_1),
                getString(R.string.onboarding_desc),
                R.drawable.onboard1,
                getString(R.string.next)
            ),
            OnBoardingModel(
                getString(R.string.onboarding_title_2),
                getString(R.string.onboarding_desc),
                R.drawable.onboard2,
                getString(R.string.next)
            ),
            OnBoardingModel(
                getString(R.string.onboarding_title_3),
                getString(R.string.onboarding_desc),
                R.drawable.onboard3,
                getString(R.string.get_started)
            )
        )

        val adConfig = AdPagerPlacementConfig(
            adsEnabled = true,
            timerAdPagerPositions = setOf(1, 3),
            timerUnlockDurationMs = 3_000L
        )

        val pagerItems = AdEnabledPagerMapper.build(onboardingList, adConfig)
        val nativeAdPagerController = NativeAdPagerController(
            ExistingNativeAdPageLoader(requireActivity())
        )

        timerController = AdPagerTimerController()

        adapter = OnBoardingAdapter(
            list = pagerItems,
            nativeAdPagerController = nativeAdPagerController,
            timerController = timerController!!,
            onAdAdvanceRequested = { position ->
                adapter.nextPositionAfter(position)?.let { nextPosition ->
                    binding.viewPager.setCurrentItem(nextPosition, true)
                }
            }
        )

        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback)

        // Setup indicators
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()

        // Handle fragment continue button click
        binding.btnContinue.setOnClickListener {
            handleContinue(binding.viewPager.currentItem)
        }

        binding.viewPager.post {
            if (::adapter.isInitialized) {
                val currentPos = binding.viewPager.currentItem
                adapter.onPageSelected(currentPos)
                updateUIForPosition(currentPos)
            }
        }
    }

    private fun handleContinue(position: Int) {
        if (adapter.isLastContentPage(position)) {
            prefManager.isOnboardingDone = true
            (activity as? MainActivity)?.goToMain()
        } else {
            adapter.nextPositionAfter(position)?.let { nextPosition ->
                binding.viewPager.setCurrentItem(nextPosition, true)
            }
        }
    }

    private fun updateUIForPosition(position: Int) {
        val item = adapter.list.getOrNull(position)
        if (item is AdPagerItem.Content) {
            // Show fragment button for onboarding content
            binding.tabLayout.visibility = View.VISIBLE
            binding.btnContinue.visibility = View.VISIBLE
            binding.tvBtnText.text = item.data.smallButtonText
        } else {
            // Hide fragment button for ads (ads have their own controls)
            binding.tabLayout.visibility = View.GONE
            binding.btnContinue.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        binding.viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        if (::adapter.isInitialized) {
            adapter.release()
        }
        timerController?.clear()
        timerController = null
        super.onDestroyView()
    }
}
