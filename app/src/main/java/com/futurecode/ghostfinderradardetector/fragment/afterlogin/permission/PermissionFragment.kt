package com.futurecode.ghostfinderradardetector.fragment.afterlogin.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.ghostfinderradardetector.ads.native_ad.NativeAdsHelper
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentPermissionBinding
import com.futurecode.ghostfinderradardetector.utils.Utils.setAdClickListener

class PermissionFragment : BaseFragment<FragmentPermissionBinding>(FragmentPermissionBinding::inflate) {

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        binding.switchCamera.isChecked = isGranted
        if (isGranted) {
            findNavController().navigate(R.id.action_permissionFragment_to_scanRoomFragment)
        }
    }

    private lateinit var nativeAdsHelper:NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nativeAdsHelper= NativeAdsHelper(requireActivity())
        fullScreenAdsHelper= FullScreenAdsHelper(requireActivity())

        setupUI()
        checkPermissions()
        loadNativeAds()
    }

    private fun setupUI() {
        binding.switchCamera.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && !isCameraPermissionGranted()) {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        binding.btnContinue.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            if (isCameraPermissionGranted()) {
                findNavController().navigate(R.id.action_permissionFragment_to_scanRoomFragment)
            } else {
                Toast.makeText(requireContext(), "Camera permission is required to find ghosts!", Toast.LENGTH_SHORT).show()
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermissions() {
        binding.switchCamera.isChecked = isCameraPermissionGranted()
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