package com.futurecode.ghostfinderradardetector.fragment.afterlogin.howtouse

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.adapter.HowToUseAdapter
import com.futurecode.ghostfinderradardetector.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.ghostfinderradardetector.ads.native_ad.NativeAdsHelper
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentHowToUseBinding
import com.futurecode.ghostfinderradardetector.utils.Utils.setAdClickListener
import com.google.android.material.tabs.TabLayoutMediator

class HowToUseFragment : BaseFragment<FragmentHowToUseBinding>(FragmentHowToUseBinding::inflate) {

    private lateinit var adapter: HowToUseAdapter
    private val pageCount = 3
    private lateinit var nativeAdsHelper:NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper
    

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nativeAdsHelper= NativeAdsHelper(requireActivity())
        fullScreenAdsHelper= FullScreenAdsHelper(requireActivity())
        setupViewPager()
        setupListeners()
    }

    private fun setupViewPager() {
        adapter = HowToUseAdapter(pageCount)
        binding.viewPager.adapter = adapter

        // Link TabLayout (indicator) with ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == pageCount - 1) {
                    binding.tvBtnText.text = getString(R.string.done)
                } else {
                    binding.tvBtnText.text = getString(R.string.next)
                }
            }
        })
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnNext.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            val current = binding.viewPager.currentItem
            if (current < pageCount - 1) {
                binding.viewPager.currentItem = current + 1
            } else {
                findNavController().navigateUp()
            }
        }
    }


}