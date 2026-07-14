package com.futurecode.ghostfinderradardetector.fragment.prelogin

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.activity.MainActivity
import com.futurecode.ghostfinderradardetector.activity.MyApplication
import com.futurecode.ghostfinderradardetector.adapter.AdEnabledPagerMapper
import com.futurecode.ghostfinderradardetector.adapter.AdPagerItem
import com.futurecode.ghostfinderradardetector.adapter.AdPagerPlacementConfig
import com.futurecode.ghostfinderradardetector.adapter.AdPagerTimerController
import com.futurecode.ghostfinderradardetector.adapter.OnBoardingAdapter
import com.futurecode.ghostfinderradardetector.ads.ads_new.ExistingNativeAdPageLoader
import com.futurecode.ghostfinderradardetector.ads.ads_new.NativeAdPagerController
import com.futurecode.ghostfinderradardetector.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentOnBoardingBinding
import com.futurecode.ghostfinderradardetector.model.OnBoardingModel
import com.google.android.material.tabs.TabLayoutMediator
import com.futurecode.ghostfinderradardetector.utils.Utils.setAdClickListener

class OnBoardingFragment :
    BaseFragment<FragmentOnBoardingBinding>(FragmentOnBoardingBinding::inflate) {

    private lateinit var adapter: OnBoardingAdapter
    private var timerController: AdPagerTimerController? = null
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper

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
        fullScreenAdsHelper = FullScreenAdsHelper(requireActivity())
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

        var isAdsEnabled = !MyApplication.app.prefManager.adsOff

        val adConfig = AdPagerPlacementConfig(
            adsEnabled = isAdsEnabled,
            timerAdPagerPositions = setOf(1, 3),
            timerUnlockDurationMs = 3_000L
        )

        val pagerItems = AdEnabledPagerMapper.build(onboardingList, adConfig)
        val nativeAdPagerController = NativeAdPagerController(
            ExistingNativeAdPageLoader(requireActivity())
        )

        val pagerTimerController = AdPagerTimerController()
        timerController = pagerTimerController

        adapter = OnBoardingAdapter(
            activity = requireActivity(),
            list = pagerItems,
            nativeAdPagerController = nativeAdPagerController,
            timerController = pagerTimerController,
            onAdAdvanceRequested = { position ->
                adapter.nextPositionAfter(position)?.let { nextPosition ->
                    binding.viewPager.setCurrentItem(nextPosition, true)
                }
            },
            onContentContinueRequested = { /* No-op */ }
        )

        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()

        // ✅ CRITICAL FIX: Normal Click listener ko ad click listener se replace kiya
        binding.btnContinue.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            if (isAdded) {
                handleContinue(binding.viewPager.currentItem)
            }
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
            binding.tabLayout.visibility = View.VISIBLE
            binding.btnContinue.visibility = View.VISIBLE
            binding.tvBtnText.text = item.data.smallButtonText
        } else {
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