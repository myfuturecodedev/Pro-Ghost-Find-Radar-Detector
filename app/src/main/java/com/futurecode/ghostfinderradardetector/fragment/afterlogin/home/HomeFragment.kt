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
//                if (isCurrentDestination(R.id.homeFragment)) {
//                    if (isCameraPermissionGranted()) {
//                        //findNavController().navigate(R.id.action_homeFragment_to_scanRoomFragment)
//                        findNavController().navigate(R.id.action_homeFragment_to_dayNightFragment)
//                    } else {
//                        //findNavController().navigate(R.id.action_homeFragment_to_permissionFragment)
//                        findNavController().navigate(R.id.action_homeFragment_to_dayNightFragment)
//                    }
//                }

                findNavController().navigate(R.id.action_homeFragment_to_dayNightFragment)

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