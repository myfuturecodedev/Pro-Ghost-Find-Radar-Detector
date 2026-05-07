package com.futurecode.ghostfinderradardetector.fragment.afterlogin.scarysound

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.adapter.ScarySoundAdapter
import com.futurecode.ghostfinderradardetector.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.ghostfinderradardetector.ads.native_ad.NativeAdsHelper
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentScarySoundBinding
import com.futurecode.ghostfinderradardetector.model.ScarySoundModel
import com.futurecode.ghostfinderradardetector.utils.AdViewTypeManager
import com.futurecode.ghostfinderradardetector.utils.AudioPlayer
import com.futurecode.ghostfinderradardetector.utils.Utils.setAdClickListener

class ScarySoundFragment : BaseFragment<FragmentScarySoundBinding>(FragmentScarySoundBinding::inflate) {
    private lateinit var scarySoundAdapter: ScarySoundAdapter
    private var scarySounds = mutableListOf<ScarySoundModel>()
    private lateinit var nativeAdsHelper:NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nativeAdsHelper= NativeAdsHelper(requireActivity())
        fullScreenAdsHelper= FullScreenAdsHelper(requireActivity())

        initData()
        setupListeners()
        setupRecyclerView()
       // loadNativeAds()
    }

    private fun initData() {
        scarySounds.clear()
        scarySounds = mutableListOf(
            ScarySoundModel(1, getString(R.string.female_horror_ghost), R.drawable.femal_horrer_ghost, sounds = R.raw.evil_girl_laughing),
            ScarySoundModel(2, getString(R.string.ghost_whispers), R.drawable.ghost_wishper_one, sounds = R.raw.ghost_sound1),
            ScarySoundModel(3, getString(R.string.horror_suspense), R.drawable.horror_suspense, sounds = R.raw.ghost_sound2),
            ScarySoundModel(4, getString(R.string.laughing), R.drawable.laughing, sounds = R.raw.ghost_sound3),
            ScarySoundModel(5, getString(R.string.horror_lightning_strike), R.drawable.settings, sounds = R.raw.ghost_sounds4)
        )

    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnDone.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        // 1. Wrap your original data list with Ad markers
        val listWithAds = AdViewTypeManager.wrapList(scarySounds, interval = 3)

// 2. Initialize the adapter (passing requireActivity() for Ad support)
        scarySoundAdapter = ScarySoundAdapter(requireActivity(), listWithAds) { position ->
            // Handle the play click safely
            val item = listWithAds[position]
            if (item is ScarySoundModel) {
               // handlePlayClick(item) // Pass the model instead of position to avoid index confusion

                handlePlayClick(item)
            }
        }

// 3. Setup RecyclerView
        binding.rvScarySounds.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scarySoundAdapter
        }


    }

//    private fun handlePlayClick(position: Int) {
//        scarySounds.forEachIndexed { index, model ->
//            if (index == position) {
//                if (model.isPlaying) {
//                    AudioPlayer.stop()
//                    model.isPlaying = false
//                } else {
//                    AudioPlayer.play(requireContext(), model.sounds)
//                    model.isPlaying = true
//                }
//            } else {
//                model.isPlaying = false
//            }
//        }
//
//        scarySoundAdapter.notifyDataSetChanged()
//    }


    private fun handlePlayClick(clickedModel: ScarySoundModel) {
        scarySounds.forEach { model ->
            if (model.id == clickedModel.id) { // Use ID to identify the unique sound
                if (model.isPlaying) {
                    AudioPlayer.stop()
                    model.isPlaying = false
                } else {
                    AudioPlayer.play(requireContext(), model.sounds)
                    model.isPlaying = true
                }
            } else {
                // Stop and uncheck all other sounds
                model.isPlaying = false
            }
        }

        // Refresh the adapter to show the green "Playing" state
        scarySoundAdapter.notifyDataSetChanged()
    }
}