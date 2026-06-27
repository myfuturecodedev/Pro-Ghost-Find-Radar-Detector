package com.futurecode.ghostfinderradardetector.fragment.afterlogin.scan

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.ghostfinderradardetector.ads.native_ad.NativeAdsHelper
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentScanRoomBinding
import com.futurecode.ghostfinderradardetector.model.GhostModel
import com.futurecode.ghostfinderradardetector.utils.GhostDetectorHelper
import com.futurecode.ghostfinderradardetector.utils.Utils.setAdClickListener


//class ScanRoomFragment : BaseFragment<FragmentScanRoomBinding>(FragmentScanRoomBinding::inflate) {
//    private lateinit var ghostDetectorHelper: GhostDetectorHelper
//    private var lastDetectedGhost: GhostModel? = null
//
//    // CameraX Control for Flashlight
//    private var cameraControl: CameraControl? = null
//    private var isFlashOn = false
//    private lateinit var nativeAdsHelper:NativeAdsHelper
//    lateinit var fullScreenAdsHelper: FullScreenAdsHelper
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        nativeAdsHelper= NativeAdsHelper(requireActivity())
//        fullScreenAdsHelper= FullScreenAdsHelper(requireActivity())
//
//        ghostDetectorHelper = GhostDetectorHelper(
//            context = requireContext(),
//            detectedGhostView = binding.ivDetectedGhost,
//            lottieAnimation = binding.lottieAnimation,
//            ghostIndicatorView = binding.ivGhostIndicator,
//            scope = viewLifecycleOwner.lifecycleScope
//        )
//
//        startCamera()
//        setupListeners()
//        loadNativeAds()
//    }
//
//    private fun startCamera() {
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
//
//        cameraProviderFuture.addListener({
//            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//
//            val preview = Preview.Builder()
//                .build()
//                .also {
//                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
//                }
//
//            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//            try {
//                cameraProvider.unbindAll()
//
//                // Bind and capture the camera instance
//                val camera = cameraProvider.bindToLifecycle(
//                    viewLifecycleOwner, cameraSelector, preview
//                )
//
//                // Initialize cameraControl to manage the flash
//                cameraControl = camera.cameraControl
//
//            } catch (exc: Exception) {
//                Log.e("ScanRoomFragment", "CameraX use case binding failed", exc)
//            }
//
//        }, ContextCompat.getMainExecutor(requireContext()))
//    }
//
//    private fun setupListeners() {
//        binding.ivClose.setOnClickListener {
//            findNavController().navigateUp()
//        }
//
//        binding.ivSound.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
//            findNavController().navigate(R.id.action_scanRoomFragment_to_scarySoundFragment)
//        }
//
//        binding.btnCollection.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
//            findNavController().navigate(R.id.action_scanRoomFragment_to_collectionFragment)
//        }
//
//        binding.btnGhostType.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
//            showGhostTypeBottomSheet(true)
//        }
//
//        binding.vOverlay.setOnClickListener {
//            showGhostTypeBottomSheet(false)
//            binding.clFoundGhostPopup.visibility = View.GONE
//            binding.clNotFoundPopup.visibility = View.GONE
//            binding.vOverlay.visibility = View.GONE
//        }
//
//        binding.btnNoHarm.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
//            selectGhostType(true)
//        }
//
//        binding.btnHorror.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
//            selectGhostType(false)
//        }
//
//        binding.btnContinue.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
//            showGhostTypeBottomSheet(false)
//        }
//
//        // FLASH CLICK LISTENER
//        binding.ivFlash.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
//            toggleFlash()
//        }
//
//        binding.flCaptureContainer.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
//            val isNoHarm = binding.rbNoHarm.isChecked
//            binding.clRadar.visibility= View.VISIBLE
//            binding.clTopBar.visibility= View.GONE
//            binding.clBottomControls.visibility= View.GONE
//
//            ghostDetectorHelper.startDetection(isNoHarm) { ghost ->
//                if (ghost != null) {
//                    binding.clRadar.visibility= View.GONE
//                    binding.clTopBar.visibility= View.VISIBLE
//                    binding.clBottomControls.visibility= View.VISIBLE
//                    lastDetectedGhost = ghost
//                    showFoundPopup(ghost)
//                } else {
//                    binding.clRadar.visibility= View.GONE
//                    binding.clTopBar.visibility= View.VISIBLE
//                    binding.clBottomControls.visibility= View.VISIBLE
//                    showNotFoundPopup()
//                }
//            }
//        }
//
//        binding.btnViewNow.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
//            lastDetectedGhost?.let { ghost ->
//
//                val updatedGhost = ghost.copy(
//                    sounds = ghost.sounds   // 👈 add sound here
//                )
//
//                val bundle = Bundle().apply {
//                    putParcelable("ghost", updatedGhost)
//                }
//
//                findNavController().navigate(R.id.action_scanRoomFragment_to_ghostDetailFragment, bundle)
//                binding.clFoundGhostPopup.visibility = View.GONE
//                binding.vOverlay.visibility = View.GONE
//            }
//        }
//
//        binding.btnTryAgain.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
//            binding.clNotFoundPopup.visibility = View.GONE
//            binding.vOverlay.visibility = View.GONE
//        }
//    }
//
//    private fun toggleFlash() {
//        isFlashOn = !isFlashOn
//
//        cameraControl?.enableTorch(isFlashOn)
//
//        if (isFlashOn) {
//            binding.ivFlash.setImageResource(R.drawable.flash_light)
//        } else {
//            binding.ivFlash.setImageResource(R.drawable.flash_off)
//        }
//    }
//
//    private fun showFoundPopup(ghost: GhostModel) {
//        binding.vOverlay.visibility = View.VISIBLE
//        binding.clFoundGhostPopup.visibility = View.VISIBLE
//        binding.clNotFoundPopup.visibility = View.GONE
//        binding.ivFoundGhost.setImageResource(ghost.image)
//    }
//
//    private fun showNotFoundPopup() {
//        binding.vOverlay.visibility = View.VISIBLE
//        binding.clNotFoundPopup.visibility = View.VISIBLE
//        binding.clFoundGhostPopup.visibility = View.GONE
//    }
//
//    private fun showGhostTypeBottomSheet(show: Boolean) {
//        if (show) {
//            binding.vOverlay.visibility = View.VISIBLE
//            binding.clGhostTypeBottomSheet.visibility = View.VISIBLE
//        } else {
//            binding.vOverlay.visibility = View.GONE
//            binding.clGhostTypeBottomSheet.visibility = View.GONE
//        }
//    }
//
//    private fun selectGhostType(isNoHarm: Boolean) {
//        if (isNoHarm) {
//            binding.rbNoHarm.isChecked = true
//            binding.rbHorror.isChecked = false
//            binding.btnNoHarm.setBackgroundResource(R.drawable.bg_ghost_card_selected)
//            binding.btnHorror.setBackgroundResource(R.drawable.bg_ghost_card_unselected)
//        } else {
//            binding.rbNoHarm.isChecked = false
//            binding.rbHorror.isChecked = true
//            binding.btnNoHarm.setBackgroundResource(R.drawable.bg_ghost_card_unselected)
//            binding.btnHorror.setBackgroundResource(R.drawable.bg_ghost_card_selected)
//        }
//    }
//
//    fun loadNativeAds(){
////        nativeAdsHelper = NativeAdsHelper(requireActivity())
////        nativeAdsHelper?.showNativeAd(
////            nativeBannerAdView = binding.nativeAds3.frameLayout,
////            mainLayout = binding.nativeAds3.relativeLayout,
////            placeholder = binding.nativeAds3.placeholder
////        )
//    }
//    override fun onDestroyView() {
//        // Turn off flash explicitly when exiting
//        cameraControl?.enableTorch(false)
//        super.onDestroyView()
//    }
//}



class ScanRoomFragment : BaseFragment<FragmentScanRoomBinding>(FragmentScanRoomBinding::inflate) {
    private var ghostDetectorHelper: GhostDetectorHelper? = null
    private var lastDetectedGhost: GhostModel? = null

    // CameraX Control for Flashlight
    private var cameraControl: CameraControl? = null
    private var isFlashOn = false
    private var nativeAdsHelper: NativeAdsHelper? = null
    private var fullScreenAdsHelper: FullScreenAdsHelper? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Activity context ko safely fetch karke helpers initialize karein
        activity?.let { currentActivity ->
            nativeAdsHelper = NativeAdsHelper(currentActivity)
            fullScreenAdsHelper = FullScreenAdsHelper(currentActivity)

            // GhostDetectorHelper initialisation with safe binding objects
            ghostDetectorHelper = GhostDetectorHelper(
                context = currentActivity,
                detectedGhostView = binding.ivDetectedGhost,
                lottieAnimation = binding.lottieAnimation,
                ghostIndicatorView = binding.ivGhostIndicator,
                scope = viewLifecycleOwner.lifecycleScope
            )

            setupListeners(currentActivity)
        }

        startCamera()
        loadNativeAds()
    }

    private fun startCamera() {
        val currentContext = context ?: return
        val cameraProviderFuture = ProcessCameraProvider.getInstance(currentContext)

        cameraProviderFuture.addListener({
            // 1. Critical Safe Check: Agar listener chalne tak fragment destroy ho gaya ho
            val currentBinding = bindingOrNull ?: return@addListener

            try {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build()
                    .also {
                        // Using safe local binding reference
                        it.setSurfaceProvider(currentBinding.previewView.surfaceProvider)
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                cameraProvider.unbindAll()

                // Bind and capture the camera instance safely
                if (isAdded) {
                    val camera = cameraProvider.bindToLifecycle(
                        viewLifecycleOwner, cameraSelector, preview
                    )
                    // Initialize cameraControl to manage the flash
                    cameraControl = camera.cameraControl
                }

            } catch (exc: Exception) {
                Log.e("ScanRoomFragment", "CameraX use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(currentContext))
    }

    private fun setupListeners(currentActivity: androidx.fragment.app.FragmentActivity) {
        binding.ivClose.setOnClickListener {
            if (isAdded) {
                findNavController().navigateUp()
            }
        }

        // Safe helpers implementation avoiding NullPointer/Double-Bang crashes
        fullScreenAdsHelper?.let { helper ->

            // FIXED: Switched all core buttons back to setAdClickListener safely using the local helper
            binding.ivSound.setAdClickListener(currentActivity, helper) {
                if (isAdded) findNavController().navigate(R.id.action_scanRoomFragment_to_scarySoundFragment)
            }

            binding.btnCollection.setAdClickListener(currentActivity, helper) {
                if (isAdded) findNavController().navigate(R.id.action_scanRoomFragment_to_collectionFragment)
            }

            binding.btnGhostType.setAdClickListener(currentActivity, helper) {
                showGhostTypeBottomSheet(true)
            }

            binding.vOverlay.setOnClickListener {
                showGhostTypeBottomSheet(false)
                binding.clFoundGhostPopup.visibility = View.GONE
                binding.clNotFoundPopup.visibility = View.GONE
                binding.vOverlay.visibility = View.GONE
            }

            binding.btnNoHarm.setOnClickListener {
                selectGhostType(true)
            }

            binding.btnHorror.setOnClickListener {
                selectGhostType(false)
            }

            binding.btnContinue.setOnClickListener {
                showGhostTypeBottomSheet(false)
            }

            binding.ivFlash.setAdClickListener(currentActivity, helper) {
                toggleFlash()
            }

            // FIXED: Removed the dangerous fullScreenAdsHelper!! and replaced it with safe 'helper'
            binding.flCaptureContainer.setAdClickListener(currentActivity, helper) {

                // Double check fragment presence before touching UI
                if (!isAdded) return@setAdClickListener

                val isNoHarm = binding.rbNoHarm.isChecked
                binding.clRadar.visibility = View.VISIBLE
                binding.clTopBar.visibility = View.GONE
                binding.clBottomControls.visibility = View.GONE

                ghostDetectorHelper?.startDetection(isNoHarm) { ghost ->
                    // Callback safe check: Agar detection khatam hone tak user baahar chala gaya ho
                    val activeBinding = bindingOrNull ?: return@startDetection

                    if (ghost != null) {
                        activeBinding.clRadar.visibility = View.GONE
                        activeBinding.clTopBar.visibility = View.VISIBLE
                        activeBinding.clBottomControls.visibility = View.VISIBLE
                        lastDetectedGhost = ghost
                        showFoundPopup(ghost)
                    } else {
                        activeBinding.clRadar.visibility = View.GONE
                        activeBinding.clTopBar.visibility = View.VISIBLE
                        activeBinding.clBottomControls.visibility = View.VISIBLE
                        showNotFoundPopup()
                    }
                }
            }

            binding.btnViewNow.setAdClickListener(currentActivity, helper) {
                lastDetectedGhost?.let { ghost ->
                    if (isAdded) {
                        val updatedGhost = ghost.copy(sounds = ghost.sounds)
                        val bundle = Bundle().apply {
                            putParcelable("ghost", updatedGhost)
                        }
                        findNavController().navigate(R.id.action_scanRoomFragment_to_ghostDetailFragment, bundle)
                        binding.clFoundGhostPopup.visibility = View.GONE
                        binding.vOverlay.visibility = View.GONE
                    }
                }
            }

            binding.btnTryAgain.setOnClickListener {
                binding.clNotFoundPopup.visibility = View.GONE
                binding.vOverlay.visibility = View.GONE
            }
        }
    }

    private fun toggleFlash() {
        isFlashOn = !isFlashOn
        try {
            cameraControl?.enableTorch(isFlashOn)
            if (isFlashOn) {
                bindingOrNull?.ivFlash?.setImageResource(R.drawable.flash_light)
            } else {
                bindingOrNull?.ivFlash?.setImageResource(R.drawable.flash_off)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showFoundPopup(ghost: GhostModel) {
        bindingOrNull?.apply {
            vOverlay.visibility = View.VISIBLE
            clFoundGhostPopup.visibility = View.VISIBLE
            clNotFoundPopup.visibility = View.GONE
            ivFoundGhost.setImageResource(ghost.image)
        }
    }

//    private fun showNotFoundPopup() {
//        bindingOrNull?.apply {
//            vOverlay.visibility = View.getVisibleOrFallback() rescue View.VISIBLE
//
//            vOverlay.visibility = View.VISIBLE
//            clNotFoundPopup.visibility = View.VISIBLE
//            clFoundGhostPopup.visibility = View.GONE
//        }
//    }


    private fun showNotFoundPopup() {
        bindingOrNull?.apply {
            vOverlay.visibility = View.VISIBLE // ✅ Sirf yeh single line rakho
            clNotFoundPopup.visibility = View.VISIBLE
            clFoundGhostPopup.visibility = View.GONE
        }
    }

    private fun showGhostTypeBottomSheet(show: Boolean) {
        bindingOrNull?.apply {
            if (show) {
                vOverlay.visibility = View.VISIBLE
                clGhostTypeBottomSheet.visibility = View.VISIBLE
            } else {
                vOverlay.visibility = View.GONE
                clGhostTypeBottomSheet.visibility = View.GONE
            }
        }
    }

    private fun selectGhostType(isNoHarm: Boolean) {
        bindingOrNull?.apply {
            if (isNoHarm) {
                rbNoHarm.isChecked = true
                rbHorror.isChecked = false
                btnNoHarm.setBackgroundResource(R.drawable.bg_ghost_card_selected)
                btnHorror.setBackgroundResource(R.drawable.bg_ghost_card_unselected)
            } else {
                rbNoHarm.isChecked = false
                rbHorror.isChecked = true
                btnNoHarm.setBackgroundResource(R.drawable.bg_ghost_card_unselected)
                btnHorror.setBackgroundResource(R.drawable.bg_ghost_card_selected)
            }
        }
    }

    fun loadNativeAds() {
        activity?.let { currentActivity ->
            if (nativeAdsHelper == null) {
                nativeAdsHelper = NativeAdsHelper(currentActivity)
            }
//            nativeAdsHelper?.showNativeAd(
//                nativeBannerAdView = binding.nativeAds3.frame,
//                mainLayout = binding.nativeAds3.mainLayout,
//                placeholder = binding.nativeAds3.placeholder
//            )
        }
    }

    override fun onDestroyView() {
        try {
            cameraControl?.enableTorch(false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        ghostDetectorHelper = null // Prevent context memory leaks
        super.onDestroyView()
    }
}