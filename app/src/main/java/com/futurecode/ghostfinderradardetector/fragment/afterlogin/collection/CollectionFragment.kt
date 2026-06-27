package com.futurecode.ghostfinderradardetector.fragment.afterlogin.collection

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.adapter.GhostAdapter
import com.futurecode.ghostfinderradardetector.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.ghostfinderradardetector.ads.native_ad.NativeAdsHelper
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentCollectionBinding
import com.futurecode.ghostfinderradardetector.model.GhostModel
import com.futurecode.ghostfinderradardetector.utils.AdViewTypeManager
import com.futurecode.ghostfinderradardetector.utils.GhostData
import com.futurecode.ghostfinderradardetector.utils.Utils.setAdClickListener

class CollectionFragment : BaseFragment<FragmentCollectionBinding>(FragmentCollectionBinding::inflate) {

    private lateinit var ghostAdapter: GhostAdapter
    private val noHarmGhosts = mutableListOf<GhostModel>()
    private val horrorGhosts = mutableListOf<GhostModel>()

    // This variable stays preserved in memory when navigating back from details
    private var isNoHarmTabSelected = true

    private lateinit var nativeAdsHelper: NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nativeAdsHelper = NativeAdsHelper(requireActivity())
        fullScreenAdsHelper = FullScreenAdsHelper(requireActivity())

        initData()
        setupUI()
        setupListeners()
        setupRecyclerView()
    }

    private fun initData() {
        noHarmGhosts.clear()
        noHarmGhosts.addAll(GhostData.ghosts.take(4))

        horrorGhosts.clear()
        horrorGhosts.addAll(GhostData.ghosts.drop(4))
    }

    private fun setupUI() {
        // FIX: Dynamic selection based on memory state when returning to the fragment view

        selectTab(isNoHarmTabSelected)
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvNoHarmGhost.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            if (!isNoHarmTabSelected) selectTab(true)
        }

        binding.tvHorrorGhost.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            if (isNoHarmTabSelected) selectTab(false)
        }
    }

    private fun selectTab(isNoHarm: Boolean) {
        isNoHarmTabSelected = isNoHarm

        val rawList = if (isNoHarm) noHarmGhosts else horrorGhosts
        val wrappedList = AdViewTypeManager.wrapList(rawList, interval = 2, isSingleAd = true)

        if (isNoHarm) {
            binding.tvNoHarmGhost.apply {
                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_collection_tab_selected)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.neon_green))
            }
            binding.tvHorrorGhost.apply {
                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_collection_tab_unselected)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
        } else {
            binding.tvNoHarmGhost.apply {
                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_collection_tab_unselected)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
            binding.tvHorrorGhost.apply {
                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_collection_tab_selected)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.neon_green))
            }
        }

        if (::ghostAdapter.isInitialized) {
            ghostAdapter.updateList(wrappedList)
            AdViewTypeManager.setGridSpanSize(binding.rvGhosts, ghostAdapter, 2)
        }
    }

    private fun setupRecyclerView() {
        val currentList = if (isNoHarmTabSelected) noHarmGhosts else horrorGhosts
        val listWithAds = AdViewTypeManager.wrapList(currentList, interval = 2, isSingleAd = true)

        ghostAdapter = GhostAdapter(requireActivity(), listWithAds) { selectedGhost ->
            handleItemClick(selectedGhost)
        }

        binding.rvGhosts.apply {
            val gridManager = GridLayoutManager(requireContext(), 2)
            layoutManager = gridManager
            adapter = ghostAdapter
            AdViewTypeManager.setGridSpanSize(this, ghostAdapter, 2)
        }
    }

    private fun handleItemClick(selectedGhost: GhostModel) {
        val bundle = Bundle().apply {
            putParcelable("ghost", selectedGhost)
        }

        if (isAdded) {
            findNavController().navigate(R.id.action_collectionFragment_to_ghostDetailFragment, bundle)
        }
    }
}