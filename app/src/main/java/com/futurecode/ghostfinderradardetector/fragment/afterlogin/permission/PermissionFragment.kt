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
import android.content.Intent
import android.net.Uri
import android.provider.Settings

import androidx.appcompat.app.AlertDialog

//class PermissionFragment : BaseFragment<FragmentPermissionBinding>(FragmentPermissionBinding::inflate) {
//
//    private lateinit var nativeAdsHelper: NativeAdsHelper
//    lateinit var fullScreenAdsHelper: FullScreenAdsHelper
//
//    // 1. Updated Permission Launcher to handle permanent denials
//    private val cameraPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        binding.switchCamera.isChecked = isGranted
//
//        if (isGranted) {
//            findNavController().navigate(R.id.action_permissionFragment_to_scanRoomFragment)
//        } else {
//            // Detect if they checked "Don't ask again"
//            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
//                showSettingsDialog()
//            } else {
//                Toast.makeText(requireContext(), "Camera permission is required to find ghosts!", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        nativeAdsHelper = NativeAdsHelper(requireActivity())
//        fullScreenAdsHelper = FullScreenAdsHelper(requireActivity())
//
//
//        setupUI()
//        checkPermissions()
//        loadNativeAds()
//    }
//
//    private fun setupUI() {
//        // 2. Used ClickListener to prevent programmatic toggle loops
//        binding.switchCamera.setOnClickListener {
//            val isChecked = binding.switchCamera.isChecked
//            if (isChecked && !isCameraPermissionGranted()) {
//                // Revert visual state temporarily until actually granted
//                binding.switchCamera.isChecked = false
//                requestCameraPermission()
//            }
//        }
//
//        binding.btnContinue.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
//            if (isCameraPermissionGranted()) {
//                findNavController().navigate(R.id.action_permissionFragment_to_scanRoomFragment)
//            } else {
//                requestCameraPermission()
//            }
//        }
//    }
//
//    private fun requestCameraPermission() {
//        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
//            Toast.makeText(requireContext(), "Camera is required to scan the room for ghosts!", Toast.LENGTH_LONG).show()
//            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
//        } else if (!isCameraPermissionGranted()) {
//            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
//        }
//    }
//
//    // 3. Added Dialog to send user to App Settings if permanently denied
//    private fun showSettingsDialog() {
//        AlertDialog.Builder(requireContext())
//            .setTitle("Permission Required")
//            .setMessage("Camera permission is permanently denied. Please enable it in App Settings to use the Ghost Radar.")
//            .setPositiveButton("Go to Settings") { _, _ ->
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                val uri = Uri.fromParts("package", requireActivity().packageName, null)
//                intent.data = uri
//                startActivity(intent)
//            }
//            .setNegativeButton("Cancel", null)
//            .show()
//    }
//
//    private fun isCameraPermissionGranted(): Boolean {
//        return ContextCompat.checkSelfPermission(
//            requireContext(),
//            Manifest.permission.CAMERA
//        ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun checkPermissions() {
//        binding.switchCamera.isChecked = isCameraPermissionGranted()
//    }
//
//    private fun loadNativeAds() {
//        nativeAdsHelper = NativeAdsHelper(requireActivity())
//        nativeAdsHelper.showNativeAd(
//            nativeBannerAdView = binding.nativeAds3.frame,
//            mainLayout = binding.nativeAds3.mainLayout,
//            placeholder = binding.nativeAds3.placeholder
//        )
//    }
//}


class PermissionFragment : BaseFragment<FragmentPermissionBinding>(FragmentPermissionBinding::inflate) {

    private var nativeAdsHelper: NativeAdsHelper? = null
    private var fullScreenAdsHelper: FullScreenAdsHelper? = null

    // Permission Launcher - Handled permanently denied and detachment safely
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Agar fragment attached hai, tabhi UI elements ko update karo
        if (isAdded) {
            binding.switchCamera.isChecked = isGranted
        }

        if (isGranted) {
            if (isAdded) {
                findNavController().navigate(R.id.action_permissionFragment_to_scanRoomFragment)
            }
        } else {
            // Detect if they checked "Don't ask again"
            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showSettingsDialog()
            } else {
                context?.let {
                    Toast.makeText(it, "Camera permission is required to find ghosts!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Activity ko safely fetch karke helpers initialize karein
        activity?.let { currentActivity ->
            nativeAdsHelper = NativeAdsHelper(currentActivity)
            fullScreenAdsHelper = FullScreenAdsHelper(currentActivity)
            setupUI(currentActivity)
        }

        checkPermissions()
        loadNativeAds()
    }

    private fun setupUI(currentActivity: androidx.fragment.app.FragmentActivity) {
        binding.switchCamera.setOnClickListener {
            val isChecked = binding.switchCamera.isChecked
            if (isChecked && !isCameraPermissionGranted()) {
                binding.switchCamera.isChecked = false
                requestCameraPermission()
            }
        }

        // Interstitial Ad setup with safe activity context
        fullScreenAdsHelper?.let { helper ->
            binding.btnContinue.setAdClickListener(currentActivity, helper) {
                if (isCameraPermissionGranted()) {
                    if (isAdded) {
                        findNavController().navigate(R.id.action_permissionFragment_to_scanRoomFragment)
                    }
                } else {
                    requestCameraPermission()
                }
            }
        }
    }

    private fun requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            context?.let {
                Toast.makeText(it, "Camera is required to scan the room for ghosts!", Toast.LENGTH_LONG).show()
            }
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else if (!isCameraPermissionGranted()) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // 100% Safe Settings Dialog (Anti-Crash)
    private fun showSettingsDialog() {
        val currentContext = context ?: return
        val currentActivity = activity ?: return

        AlertDialog.Builder(currentContext)
            .setTitle("Permission Required")
            .setMessage("Camera permission is permanently denied. Please enable it in App Settings to use the Ghost Radar.")
            .setPositiveButton("Go to Settings") { dialog, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", currentActivity.packageName, null)
                    }
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    dialog.dismiss()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun isCameraPermissionGranted(): Boolean {
        val currentContext = context ?: return false
        return ContextCompat.checkSelfPermission(
            currentContext,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermissions() {
        binding.switchCamera.isChecked = isCameraPermissionGranted()
    }

    private fun loadNativeAds() {
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