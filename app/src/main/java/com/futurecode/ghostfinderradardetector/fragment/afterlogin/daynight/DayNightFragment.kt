package com.futurecode.ghostfinderradardetector.fragment.afterlogin.daynight

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.ghostfinderradardetector.ads.native_ad.NativeAdsHelper
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentDayNightBinding
import com.futurecode.ghostfinderradardetector.utils.Utils.setAdClickListener

class DayNightFragment : BaseFragment<FragmentDayNightBinding>(FragmentDayNightBinding::inflate) {

    private lateinit var nativeAdsHelper: NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nativeAdsHelper= NativeAdsHelper(requireActivity())
        fullScreenAdsHelper= FullScreenAdsHelper(requireActivity())


        loadNative()
        // Handle Day Button Click
        binding.btnDay.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            navigateToNextPage(isDayMode = true)
        }

        // Handle Night Button Click
        binding.btnNight.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            navigateToNextPage(isDayMode = false)
        }

    }

    private fun navigateToNextPage(isDayMode: Boolean) {
        findNavController().navigate(R.id.action_dayNightFragment_to_selectOutDoorInDoorFragment)

    }

    fun  loadNative(){
        activity?.let { currentActivity ->
            nativeAdsHelper = NativeAdsHelper(currentActivity)
            nativeAdsHelper?.showNativeAd(
                nativeBannerAdView = binding.nativeAds3.frame,
                mainLayout = binding.nativeAds3.mainLayout,
                placeholder = binding.nativeAds3.placeholder
            )
        }
    }
}