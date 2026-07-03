package com.futurecode.ghostfinderradardetector.fragment.afterlogin.outdoor

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.ghostfinderradardetector.ads.native_ad.NativeAdsHelper
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentOutDoorBinding

class OutDoorFragment : BaseFragment<FragmentOutDoorBinding>(FragmentOutDoorBinding::inflate) {
    private lateinit var nativeAdsHelper: NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nativeAdsHelper= NativeAdsHelper(requireActivity())
        fullScreenAdsHelper= FullScreenAdsHelper(requireActivity())


        loadNative()

        binding.btnForest.setOnClickListener {
            navigateToNextPage()

        }

        binding.btnWaterBodies.setOnClickListener {
            navigateToNextPage()

        }

        binding.btnBridge.setOnClickListener {
            navigateToNextPage()

        }

        binding.btnGarden.setOnClickListener {
            navigateToNextPage()

        }

    }


    fun navigateToNextPage() {
        if (isCameraPermissionGranted()) {
            //findNavController().navigate(R.id.action_homeFragment_to_scanRoomFragment)
            findNavController().navigate(R.id.action_outDoorFragment_to_permissionFragment)
        } else {
            //findNavController().navigate(R.id.action_homeFragment_to_permissionFragment)
            findNavController().navigate(R.id.action_outDoorFragment_to_scanRoomFragment)
        }

    }


    private fun isCameraPermissionGranted(): Boolean {
        val currentContext = context ?: return false
        return ContextCompat.checkSelfPermission(
            currentContext,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
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