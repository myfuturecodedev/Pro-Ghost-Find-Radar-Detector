package com.futurecode.ghostfinderradardetector.fragment.afterlogin.outdoorindoor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.ghostfinderradardetector.ads.native_ad.NativeAdsHelper
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentSelectOutDoorInDoorBinding

class SelectOutDoorInDoorFragment :
    BaseFragment<FragmentSelectOutDoorInDoorBinding>(FragmentSelectOutDoorInDoorBinding::inflate) {

    private lateinit var nativeAdsHelper: NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nativeAdsHelper= NativeAdsHelper(requireActivity())
        fullScreenAdsHelper= FullScreenAdsHelper(requireActivity())

        loadNative()
        // Handle Day Button Click
        binding.btnIndoor.setOnClickListener {
           // navigateToNextPage(isDayMode = true)
            findNavController().navigate(R.id.action_selectOutDoorInDoorFragment_to_inDoorFragment)
        }

        // Handle Night Button Click
        binding.btnOutDoor.setOnClickListener {
           // navigateToNextPage(isDayMode = false)
            findNavController().navigate(R.id.action_selectOutDoorInDoorFragment_to_outDoorFragment)

        }

    }

    private fun navigateToNextPage(isDayMode: Boolean) {
        findNavController().navigate(R.id.action_selectOutDoorInDoorFragment_to_inDoorFragment)
    }

    fun loadNative(){
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