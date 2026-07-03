package com.futurecode.ghostfinderradardetector.fragment.afterlogin.indoor

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.ghostfinderradardetector.ads.native_ad.NativeAdsHelper
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentInDoorBinding

class InDoorFragment : BaseFragment<FragmentInDoorBinding>(FragmentInDoorBinding::inflate) {

    private lateinit var nativeAdsHelper: NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nativeAdsHelper = NativeAdsHelper(requireActivity())
        fullScreenAdsHelper = FullScreenAdsHelper(requireActivity())

        loadNative()

        binding.btnKitchen.setOnClickListener {
            navigateToNextPage()
        }

        binding.btnRoom.setOnClickListener {
            navigateToNextPage()
        }

        binding.btnHall.setOnClickListener {
            navigateToNextPage()
        }

        binding.btnWashroom.setOnClickListener {
            navigateToNextPage()
        }

    }


    fun navigateToNextPage() {
        if (isCameraPermissionGranted()) {
            //findNavController().navigate(R.id.action_homeFragment_to_scanRoomFragment)
            findNavController().navigate(R.id.action_inDoorFragment_to_permissionFragment)
        } else {
            //findNavController().navigate(R.id.action_homeFragment_to_permissionFragment)
            findNavController().navigate(R.id.action_inDoorFragment_to_scanRoomFragment)
        }

    }

    private fun isCameraPermissionGranted(): Boolean {
        val currentContext = context ?: return false
        return ContextCompat.checkSelfPermission(
            currentContext,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun loadNative() {
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