package com.futurecode.ghostfinderradardetector.fragment.afterlogin.collection

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.futurecode.ghostfinderradardetector.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.ghostfinderradardetector.ads.native_ad.NativeAdsHelper
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentGhostDetailBinding
import com.futurecode.ghostfinderradardetector.utils.AudioPlayer
import com.futurecode.ghostfinderradardetector.utils.Utils.setAdClickListener

//class GhostDetailFragment : BaseFragment<FragmentGhostDetailBinding>(FragmentGhostDetailBinding::inflate) {
//
//    private val args: GhostDetailFragmentArgs by navArgs()
//    private var isPlaying = false
//
//    private lateinit var nativeAdsHelper:NativeAdsHelper
//    lateinit var fullScreenAdsHelper: FullScreenAdsHelper
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        nativeAdsHelper= NativeAdsHelper(requireActivity())
//        fullScreenAdsHelper= FullScreenAdsHelper(requireActivity())
//
//
//        setupUI()
//        setupListeners()
//        loadNativeAds()
//    }
//
//    private fun setupUI() {
//        val ghost = args.ghost
//        binding.tvTitle.text = "Ghost Detail"
//        binding.ivGhost.setImageResource(ghost.image)
//        binding.tvGhostName.text = ghost.name
//        binding.tvAge.text = ghost.age
//        binding.tvDanger.text = ghost.dangerLevel
//        binding.tvDescription.text = ghost.description
//    }
//
//
//    private fun setupListeners() {
//        binding.ivBack.setOnClickListener {
//            findNavController().navigateUp()
//        }
//
//
//        binding.ivPlay.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
//            val ghost = args.ghost
//
//            if (isPlaying) {
//                AudioPlayer.stop()
//                binding.ivPlay.setImageResource(android.R.drawable.ic_media_play)
//            } else {
//                AudioPlayer.play(requireContext(), ghost.sounds)
//                binding.ivPlay.setImageResource(android.R.drawable.ic_media_pause)
//            }
//
//            isPlaying = !isPlaying
//        }
//        }
//
//
//    fun loadNativeAds(){
//        nativeAdsHelper = NativeAdsHelper(requireActivity())
//        nativeAdsHelper?.showNativeAd(
//            nativeBannerAdView = binding.nativeAds3.frame,
//            mainLayout = binding.nativeAds3.mainLayout,
//            placeholder = binding.nativeAds3.placeholder
//        )
//    }
//
//    override fun onStop() {
//        super.onStop()
//        AudioPlayer.stop() // 👈 prevent leak / background sound
//    }
//}



class GhostDetailFragment : BaseFragment<FragmentGhostDetailBinding>(FragmentGhostDetailBinding::inflate) {

    private val args: GhostDetailFragmentArgs by navArgs()
    private var isPlaying = false

    private var nativeAdsHelper: NativeAdsHelper? = null
    private var fullScreenAdsHelper: FullScreenAdsHelper? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Activity context ko safely fetch karke helpers initialize karein aur setupListeners call karein
        activity?.let { currentActivity ->
            nativeAdsHelper = NativeAdsHelper(currentActivity)
            fullScreenAdsHelper = FullScreenAdsHelper(currentActivity)
            setupListeners(currentActivity)
        }

        setupUI()
        loadNativeAds()
    }

    private fun setupUI() {
        val ghost = args.ghost
        binding.tvTitle.text = "Ghost Detail"
        binding.ivGhost.setImageResource(ghost.image)
        binding.tvGhostName.text = ghost.name
        binding.tvAge.text = ghost.age
        binding.tvDanger.text = ghost.dangerLevel
        binding.tvDescription.text = ghost.description
    }

    private fun setupListeners(currentActivity: androidx.fragment.app.FragmentActivity) {
        binding.ivBack.setOnClickListener {
            if (isAdded) {
                findNavController().navigateUp()
            }
        }

        // Safe call using ?.let to completely avoid NullPointerException or Detach state crash
        fullScreenAdsHelper?.let { helper ->
            binding.ivPlay.setAdClickListener(currentActivity, helper) {

                // 1. Double check fragment presence before touching Context or UI
                val currentContext = context
                if (!isAdded || currentContext == null) return@setAdClickListener

                val ghost = args.ghost

                if (isPlaying) {
                    AudioPlayer.stop()
                    binding.ivPlay.setImageResource(android.R.drawable.ic_media_play)
                } else {
                    // 2. FIXED: Using the verified safe local context instead of requireContext()
                    AudioPlayer.play(currentContext, ghost.sounds)
                    binding.ivPlay.setImageResource(android.R.drawable.ic_media_pause)
                }

                isPlaying = !isPlaying
            }
        }
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

    override fun onStop() {
        super.onStop()
        try {
            AudioPlayer.stop() // prevent leak / background sound safely
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}