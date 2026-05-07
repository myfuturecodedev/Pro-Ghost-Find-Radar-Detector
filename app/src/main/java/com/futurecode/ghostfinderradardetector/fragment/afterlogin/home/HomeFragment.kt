package com.futurecode.ghostfinderradardetector.fragment.afterlogin.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.futurecode.ghostfinderradardetector.BuildConfig
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.ghostfinderradardetector.ads.native_ad.NativeAdsHelper
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentHomeBinding
import com.futurecode.ghostfinderradardetector.utils.PrefManager
import com.futurecode.ghostfinderradardetector.utils.Utils.setAdClickListener

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private lateinit var nativeAdsHelper:NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nativeAdsHelper= NativeAdsHelper(requireActivity())
        fullScreenAdsHelper= FullScreenAdsHelper(requireActivity())
        Log.d("TAG", "selectedLanguage: ${prefManager.selectedLanguage}")

        loadNativeAds()

//        binding.btnSettings.setOnClickListener {
//            findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
//        }

        val name = BuildConfig.APPLICATION_ID
        Log.d("TAG", "onViewCreated: $name")

        binding.btnSettings.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
        }

        binding.btnScanRoom.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            if (isCameraPermissionGranted()) {
                findNavController().navigate(R.id.action_homeFragment_to_scanRoomFragment)
            } else {
                findNavController().navigate(R.id.action_homeFragment_to_permissionFragment)
            }
        }

        binding.btnCollection.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            findNavController().navigate(R.id.action_homeFragment_to_collectionFragment)
        }


        binding.btnHowToUse.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            findNavController().navigate(R.id.action_homeFragment_to_howToUseFragment)
        }

//        binding.cvFeatured.setOnClickListener {
//
//        }
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }



    fun loadNativeAds(){
        nativeAdsHelper = NativeAdsHelper(requireActivity())
        nativeAdsHelper?.showNativeAd(
            nativeBannerAdView = binding.nativeAds3.frame,
            mainLayout = binding.nativeAds3.mainLayout,
            placeholder = binding.nativeAds3.placeholder
        )
    }
}