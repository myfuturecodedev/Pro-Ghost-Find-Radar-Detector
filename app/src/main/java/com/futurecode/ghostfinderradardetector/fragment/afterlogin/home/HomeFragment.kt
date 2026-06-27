package com.futurecode.ghostfinderradardetector.fragment.afterlogin.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.navigation.fragment.findNavController
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.activity.MyApplication
import com.futurecode.ghostfinderradardetector.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.ghostfinderradardetector.ads.native_ad.NativeAdsHelper
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentHomeBinding
import com.futurecode.ghostfinderradardetector.notification.NotificationScheduler
import com.futurecode.ghostfinderradardetector.utils.NotificationPermissionHelper
import com.futurecode.ghostfinderradardetector.utils.Utils.setAdClickListener

//class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
//    private lateinit var nativeAdsHelper: NativeAdsHelper
//    lateinit var fullScreenAdsHelper: FullScreenAdsHelper
//
//    // SAFE: Passing 'this' is perfectly valid during initialization!
//    private val notificationPermissionHelper = NotificationPermissionHelper(this)
//
//    // Your lazy margin calculation variable below...
//    private val paddingInDp = 50
//    private val marginInPx by lazy {
//        val scale = resources.displayMetrics.density
//        (paddingInDp * scale + 0.5f).toInt()
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        nativeAdsHelper = NativeAdsHelper(requireActivity())
//        fullScreenAdsHelper = FullScreenAdsHelper(requireActivity())
//
//        Log.d("TAG", "selectedLanguage: ${prefManager.selectedLanguage}")
//        Log.d("TAG", "selectedLanguage: ${prefManager.clickCount}")
//
//
//        loadNativeAds()
//
//
//        val name = BuildConfig.APPLICATION_ID
//        Log.d("TAG", "onViewCreated: $name")
//
//        // Call the permission check whenever the screen opens
//        notificationPermissionHelper.checkAndRequestPermission()
//
//        binding.btnSettings.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
//            findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
//        }
//
//        binding.btnScanRoom.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
//            if (isCameraPermissionGranted()) {
//                findNavController().navigate(R.id.action_homeFragment_to_scanRoomFragment)
//            } else {
//                findNavController().navigate(R.id.action_homeFragment_to_permissionFragment)
//            }
//        }
//
//        binding.btnCollection.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
//            findNavController().navigate(R.id.action_homeFragment_to_collectionFragment)
//        }
//
//        binding.btnHowToUse.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
//            findNavController().navigate(R.id.action_homeFragment_to_howToUseFragment)
//        }
//    }
//
//    private fun isCameraPermissionGranted(): Boolean {
//        return ContextCompat.checkSelfPermission(
//            requireContext(),
//            Manifest.permission.CAMERA
//        ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    fun loadNativeAds() {
//        // Safe access call style fixed for lateinit property validation
//        nativeAdsHelper.showNativeAd(
//            nativeBannerAdView = binding.nativeAds3.frame,
//            mainLayout = binding.nativeAds3.mainLayout,
//            placeholder = binding.nativeAds3.placeholder
//        )
//    }
//}

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private var nativeAdsHelper: NativeAdsHelper? = null
    private var fullScreenAdsHelper: FullScreenAdsHelper? = null

    private val notificationPermissionHelper = NotificationPermissionHelper(this)

    private val paddingInDp = 50
    private val marginInPx by lazy {
        val scale = resources.displayMetrics.density
        (paddingInDp * scale + 0.5f).toInt()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Activity context safely handle karke initialize karein aur setupUI call karein
        activity?.let { currentActivity ->
            nativeAdsHelper = NativeAdsHelper(currentActivity)
            fullScreenAdsHelper = FullScreenAdsHelper(currentActivity)
            setupUI(currentActivity)
        }

        notificationPermissionHelper.checkAndRequestPermission()
        loadNativeAds()
    }

    private fun setupUI(currentActivity: androidx.fragment.app.FragmentActivity) {
        // 2. Safe call using ?.let to completely avoid NullPointerException
        fullScreenAdsHelper?.let { helper ->

            binding.btnSettings.setAdClickListener(currentActivity, helper) {
                if (isCurrentDestination(R.id.homeFragment)) {
                    findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
                }
            }

            binding.btnScanRoom.setAdClickListener(currentActivity, helper) {
                if (isCurrentDestination(R.id.homeFragment)) {
                    if (isCameraPermissionGranted()) {
                        findNavController().navigate(R.id.action_homeFragment_to_scanRoomFragment)
                    } else {
                        findNavController().navigate(R.id.action_homeFragment_to_permissionFragment)
                    }
                }
            }

            binding.btnCollection.setAdClickListener(currentActivity, helper) {
                if (isCurrentDestination(R.id.homeFragment)) {
                    findNavController().navigate(R.id.action_homeFragment_to_collectionFragment)
                }
            }

            binding.btnHowToUse.setAdClickListener(currentActivity, helper) {
                if (isCurrentDestination(R.id.homeFragment)) {
                    findNavController().navigate(R.id.action_homeFragment_to_howToUseFragment)
                }
            }
        }
    }

    // Helper Function: Safe Destination check
    private fun isCurrentDestination(fragmentId: Int): Boolean {
        return isAdded && findNavController().currentDestination?.id == fragmentId
    }

    private fun isCameraPermissionGranted(): Boolean {
        val currentContext = context ?: return false
        return ContextCompat.checkSelfPermission(
            currentContext,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun loadNativeAds() {
        activity?.let { currentActivity ->
            if (nativeAdsHelper == null) {
                nativeAdsHelper = NativeAdsHelper(currentActivity)
            }
            nativeAdsHelper?.showNativeAd(
                nativeBannerAdView = binding.nativeAds3.frame,
                mainLayout = binding.nativeAds3.mainLayout,
                placeholder = binding.nativeAds3.placeholder
            )
        }
    }
}