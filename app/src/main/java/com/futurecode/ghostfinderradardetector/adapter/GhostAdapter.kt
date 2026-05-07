package com.futurecode.ghostfinderradardetector.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.databinding.ItemGhostCollectionBinding
import com.futurecode.ghostfinderradardetector.model.GhostModel
import android.app.Activity
import com.futurecode.ghostfinderradardetector.ads.native_ad.NativeAdsHelper
import com.futurecode.ghostfinderradardetector.databinding.ItemNativeAdsAdapterBinding
import com.futurecode.ghostfinderradardetector.utils.AdViewTypeManager


//class GhostAdapter(
//    private var list: List<GhostModel>,
//    private val onItemClick: (Int) -> Unit
//) : RecyclerView.Adapter<GhostAdapter.ViewHolder>() {
//
//    init {
//        // Step 1: Initialize the default selection (First item)
//        resetAndSetDefaultSelection()
//    }
//
//    private fun resetAndSetDefaultSelection() {
//        if (list.isNotEmpty()) {
//            // Unselect everything first to prevent multiple selections
//            list.forEach { it.isSelected = false }
//            // Force select the first item
//            list[0].isSelected = true
//        }
//    }
//
//    inner class ViewHolder(val binding: ItemGhostCollectionBinding) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = ItemGhostCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = list[position]
//
//        holder.binding.apply {
//            ivGhost.setImageResource(item.image)
//
//            // Step 2: Preserve padding (setBackgroundResource resets it)
//            val pL = root.paddingLeft
//            val pT = root.paddingTop
//            val pR = root.paddingRight
//            val pB = root.paddingBottom
//
//            // Step 3: Update background based on isSelected value
//            if (item.isSelected) {
//                root.setBackgroundResource(R.drawable.bg_ghost_card_selected)
//            } else {
//                root.setBackgroundResource(R.drawable.bg_ghost_card_unselected)
//            }
//
//            // Re-apply original padding
//            root.setPadding(pL, pT, pR, pB)
//
//            // Step 4: Handle Click - Update values for all items
//            root.setOnClickListener {
//                val currentPos = holder.adapterPosition
//                if (currentPos != RecyclerView.NO_POSITION) {
//                    // Update selection state for the entire list (Single selection mode)
//                    list.forEachIndexed { index, ghost ->
//                        ghost.isSelected = (index == currentPos)
//                    }
//
//                    // Notify adapter to refresh all items' background
//                    notifyDataSetChanged()
//
//                    // Pass the click event to the Fragment
//                    onItemClick(currentPos)
//                }
//            }
//        }
//    }
//
//    override fun getItemCount(): Int = list.size
//
//    fun updateList(newList: List<GhostModel>) {
//        this.list = newList
//        // Re-initialize default selection when the list changes (e.g., tab switch)
//        resetAndSetDefaultSelection()
//        notifyDataSetChanged()
//    }
//}



class GhostAdapter(
    private val activity: Activity,
    private var list: List<Any>, // Support for GhostModel and "AD_UNIT"
    private val onItemClick: (GhostModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class GhostViewHolder(val binding: ItemGhostCollectionBinding) : RecyclerView.ViewHolder(binding.root)
    inner class AdViewHolder(val binding: ItemNativeAdsAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (list[position] is String && list[position] == "AD_UNIT") {
            AdViewTypeManager.TYPE_AD
        } else {
            AdViewTypeManager.TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == AdViewTypeManager.TYPE_AD) {
            AdViewHolder(ItemNativeAdsAdapterBinding.inflate(inflater, parent, false))
        } else {
            GhostViewHolder(ItemGhostCollectionBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == AdViewTypeManager.TYPE_AD) {
            val adHolder = holder as AdViewHolder
            NativeAdsHelper(activity).showNativeAd(
                adHolder.binding.frameLayout,
                adHolder.binding.relativeLayout,
                adHolder.binding.placeholder
            )
        } else {
            val item = list[position] as GhostModel
            val ghostHolder = holder as GhostViewHolder
            bindGhost(ghostHolder, item)
        }
    }

    private fun bindGhost(holder: GhostViewHolder, item: GhostModel) {
        holder.binding.apply {
            ivGhost.setImageResource(item.image)

            // Step 1: Handle Background Selection
            val pL = root.paddingLeft
            val pT = root.paddingTop
            val pR = root.paddingRight
            val pB = root.paddingBottom

            if (item.isSelected) {
                root.setBackgroundResource(R.drawable.bg_ghost_card_selected)
            } else {
                root.setBackgroundResource(R.drawable.bg_ghost_card_unselected)
            }
            root.setPadding(pL, pT, pR, pB)

            // Step 2: Handle Click Logic
            root.setOnClickListener {
                // Single Selection logic: loop through list and update GhostModels only
                list.forEach {
                    if (it is GhostModel) {
                        it.isSelected = (it.id == item.id)
                    }
                }

                notifyDataSetChanged()
                onItemClick(item)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<Any>) {
        this.list = newList
        notifyDataSetChanged()
    }
}