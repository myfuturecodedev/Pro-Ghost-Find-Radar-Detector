package com.futurecode.ghostfinderradardetector.fragment.afterlogin.scan

import android.app.AlertDialog
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
import com.futurecode.ghostfinderradardetector.ads.reward.RewardAdsHelper
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentScanRoomBinding
import com.futurecode.ghostfinderradardetector.model.GhostModel
import com.futurecode.ghostfinderradardetector.utils.GhostDetectorHelper
import com.futurecode.ghostfinderradardetector.utils.Utils.setAdClickListener
import com.google.android.gms.ads.rewarded.RewardItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class ScanRoomFragment : BaseFragment<FragmentScanRoomBinding>(FragmentScanRoomBinding::inflate),
    RewardAdsHelper.RewardAdInterface {

    private lateinit var ghostDetectorHelper: GhostDetectorHelper
    private var lastDetectedGhost: GhostModel? = null

    // CameraX Control for Flashlight
    private var cameraControl: CameraControl? = null
    private var isFlashOn = false
    private lateinit var nativeAdsHelper: NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper
    private var rewardsHelper: RewardAdsHelper? = null

    // Tracking states for Reward execution flow
    private var isRewardEarned = false
    private var isAdFailedToLoadOrShow = false
    private var scanJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nativeAdsHelper = NativeAdsHelper(requireActivity())
        fullScreenAdsHelper = FullScreenAdsHelper(requireActivity())
        rewardsHelper = RewardAdsHelper(requireActivity())

        ghostDetectorHelper = GhostDetectorHelper(
            context = requireContext(),
            detectedGhostView = binding.ivDetectedGhost,
            lottieAnimation = binding.lottieAnimation,
            ghostIndicatorView = binding.ivGhostIndicator,
            scope = viewLifecycleOwner.lifecycleScope
        )

        startCamera()
        setupListeners()
        loadNativeAds()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val currentBinding = bindingOrNull ?: return@addListener
            try {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(currentBinding.previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                cameraProvider.unbindAll()

                if (isAdded) {
                    val camera = cameraProvider.bindToLifecycle(
                        viewLifecycleOwner, cameraSelector, preview
                    )
                    cameraControl = camera.cameraControl
                }
            } catch (exc: Exception) {
                Log.e("ScanRoomFragment", "CameraX use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun setupListeners() {
        binding.ivClose.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.ivSound.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            findNavController().navigate(R.id.action_scanRoomFragment_to_scarySoundFragment)
        }

        binding.btnCollection.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            findNavController().navigate(R.id.action_scanRoomFragment_to_collectionFragment)
        }

        binding.btnGhostType.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            showGhostTypeBottomSheet(true)
        }

        binding.vOverlay.setOnClickListener {
            showGhostTypeBottomSheet(false)
            binding.clFoundGhostPopup.visibility = View.GONE
            binding.clNotFoundPopup.visibility = View.GONE
            binding.vOverlay.visibility = View.GONE
        }

        binding.btnNoHarm.setAdClickListener(requireActivity(), fullScreenAdsHelper) { selectGhostType(true) }
        binding.btnHorror.setAdClickListener(requireActivity(), fullScreenAdsHelper) { selectGhostType(false) }
        binding.btnContinue.setAdClickListener(requireActivity(), fullScreenAdsHelper) { showGhostTypeBottomSheet(false) }

        binding.ivFlash.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            toggleFlash()
        }

        // Trigger Reward Ad Setup
        binding.flCaptureContainer.setOnClickListener {
            // Reset flags before showing new ad
//            isRewardEarned = false
//            isAdFailedToLoadOrShow = false
//            rewardsHelper?.showRewardAds(this)
            showPremiumUnlockDialog()
        }

        binding.btnViewNow.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            lastDetectedGhost?.let { ghost ->
                val updatedGhost = ghost.copy(sounds = ghost.sounds)
                val bundle = Bundle().apply {
                    putParcelable("ghost", updatedGhost)
                }
                findNavController().navigate(R.id.action_scanRoomFragment_to_ghostDetailFragment, bundle)
                binding.clFoundGhostPopup.visibility = View.GONE
                binding.vOverlay.visibility = View.GONE
            }
        }

        binding.btnTryAgain.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            binding.clNotFoundPopup.visibility = View.GONE
            binding.vOverlay.visibility = View.GONE
        }
    }

    // ✅ FIXED ACTION WORKFLOW FUNCTION
    private fun startScanningWorkflow() {
        val safeBinding = bindingOrNull ?: return
        val isNoHarm = safeBinding.rbNoHarm.isChecked

        // 1. UI Control - Radar View explicit visible setup
        safeBinding.clRadar.visibility = View.VISIBLE
        safeBinding.clTopBar.visibility = View.GONE
        safeBinding.clBottomControls.visibility = View.GONE

        try {
            safeBinding.lottieAnimation.setAnimation(R.raw.animation) // Ensure correct lottie file
            safeBinding.lottieAnimation.visibility = View.VISIBLE
            safeBinding.lottieAnimation.playAnimation()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Cancel previous running job to avoid overlap
        scanJob?.cancel()

        // 2. Continuous Execution on UI Thread Main dispatch
        scanJob = viewLifecycleOwner.lifecycleScope.launch {
            try {
                delay(3500) // 3.5 Seconds clear runtime scanning hold

                ghostDetectorHelper.startDetection(isNoHarm) { ghost ->
                    val activeBinding = bindingOrNull ?: return@startDetection

                    // Restore full UI elements
                    activeBinding.clRadar.visibility = View.GONE
                    activeBinding.clTopBar.visibility = View.VISIBLE
                    activeBinding.clBottomControls.visibility = View.VISIBLE

                    try {
                        activeBinding.lottieAnimation.cancelAnimation()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    // Results popup visibility logic
                    if (ghost != null) {
                        lastDetectedGhost = ghost
                        showFoundPopup(ghost)
                    } else {
                        showNotFoundPopup()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun toggleFlash() {
        isFlashOn = !isFlashOn
        cameraControl?.enableTorch(isFlashOn)
        if (isFlashOn) {
            binding.ivFlash.setImageResource(R.drawable.flash_light)
        } else {
            binding.ivFlash.setImageResource(R.drawable.flash_off)
        }
    }

    private fun showFoundPopup(ghost: GhostModel) {
        binding.vOverlay.visibility = View.VISIBLE
        binding.clFoundGhostPopup.visibility = View.VISIBLE
        binding.clNotFoundPopup.visibility = View.GONE
        binding.ivFoundGhost.setImageResource(ghost.image)
    }

    private fun showNotFoundPopup() {
        binding.vOverlay.visibility = View.VISIBLE
        binding.clNotFoundPopup.visibility = View.VISIBLE
        binding.clFoundGhostPopup.visibility = View.GONE
    }

    private fun showGhostTypeBottomSheet(show: Boolean) {
        if (show) {
            //binding.vOverlay.visibility = View.copy(View.VISIBLE).getInteger(View.VISIBLE)
            binding.clGhostTypeBottomSheet.visibility = View.VISIBLE
        } else {
            binding.vOverlay.visibility = View.GONE
            binding.clGhostTypeBottomSheet.visibility = View.GONE
        }
    }

    private fun selectGhostType(isNoHarm: Boolean) {
        if (isNoHarm) {
            binding.rbNoHarm.isChecked = true
            binding.rbHorror.isChecked = false
            binding.btnNoHarm.setBackgroundResource(R.drawable.bg_ghost_card_selected)
            binding.btnHorror.setBackgroundResource(R.drawable.bg_ghost_card_unselected)
        } else {
            binding.rbNoHarm.isChecked = false
            binding.rbHorror.isChecked = true
            binding.btnNoHarm.setBackgroundResource(R.drawable.bg_ghost_card_unselected)
            binding.btnHorror.setBackgroundResource(R.drawable.bg_ghost_card_selected)
        }
    }

    fun loadNativeAds() {}


    private fun showPremiumUnlockDialog() {
        val currentContext = context ?: return

        // Custom binding implementation for safe view accesses inside Dialog
        val dialogBinding = FragmentScanRoomBinding.inflate(layoutInflater)
        // Iski jagah direct View use karenge context inflate ke sath:
        val dialogView = layoutInflater.inflate(R.layout.dialog_premium_ad, null)

        val alertDialog = AlertDialog.Builder(currentContext, R.style.CustomDialogTheme).create()
        alertDialog.setView(dialogView)
        alertDialog.setCanceledOnTouchOutside(false)

        val btnYes = dialogView.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnYes)
        val btnNo = dialogView.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnNo)

        // YES Button Clicked -> Show Ad directly
        btnYes.setOnClickListener {
            alertDialog.dismiss()
            isRewardEarned = false
            isAdFailedToLoadOrShow = false
            rewardsHelper?.showRewardAds(this) // Trigger your reward ad helper
        }

        // NO Button Clicked -> Close dialog
        btnNo.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()

        // Transparent background stretch fallback handler
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    // =========================================================
    // ✅ RESTRUCTURED REWARD ADS CALLBACK LIFE CYCLE (ROBUST)
    // =========================================================

    override fun onAdShown() {
        Log.d("TAG_REWARD", "Ad overlay active.")
    }

    override fun onUserEarnedReward(rewardItem: RewardItem) {
        // Step 1: User completes ad view -> Set only variable true
        isRewardEarned = true
    }


    override fun onAdClosed() {
        // Step 2: Main Ad container shifts out -> Execute UI logic inside fragment context safely
        if (isAdded) {
            if (isRewardEarned || !isAdFailedToLoadOrShow) {
                // Agar reward mila ho ya fail fallback ho, workflow trigger karein
                startScanningWorkflow()
            }
            // Reset flags state
            isRewardEarned = false
        }
    }

    override fun onAdFailed(error: String) {
        // Step 3: Server/Network failure -> Bypass ad and start scanning directly
        if (isAdded) {
            isAdFailedToLoadOrShow = true
            startScanningWorkflow()
        }
    }

    override fun onDestroyView() {
        scanJob?.cancel()
        cameraControl?.enableTorch(false)
        cameraControl = null
        rewardsHelper = null
        super.onDestroyView()
    }
}