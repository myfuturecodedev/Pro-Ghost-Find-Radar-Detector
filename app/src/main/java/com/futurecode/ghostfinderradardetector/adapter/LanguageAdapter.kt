package com.futurecode.ghostfinderradardetector.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.databinding.ItemLanguageBinding
import com.futurecode.ghostfinderradardetector.model.LanguageModel
import android.app.Activity
import com.futurecode.ghostfinderradardetector.ads.native_ad.NativeAdsHelper
import com.futurecode.ghostfinderradardetector.databinding.ItemNativeAdsAdapterBinding
import com.futurecode.ghostfinderradardetector.utils.AdViewTypeManager

//class LanguageAdapter(
//    private val list: List<LanguageModel>,
//    private val onItemClick: (Int) -> Unit
//) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {
//
//    inner class LanguageViewHolder(val binding: ItemLanguageBinding) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
//        val binding = ItemLanguageBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false
//        )
//        return LanguageViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
//        val item = list[position]
//        holder.binding.apply {
//            tvLanguageName.text = item.name
//            ivFlag.setImageResource(item.flag)
//
//            if (item.isSelected) {
//                clMain.setBackgroundResource(R.drawable.bg_language_item_selected)
//                ivRadio.setImageResource(R.drawable.ic_radio_selected)
//            } else {
//                clMain.setBackgroundResource(R.drawable.bg_language_item_unselected)
//                ivRadio.setImageResource(R.drawable.ic_radio_unselected)
//            }
//
//            root.setOnClickListener {
//                onItemClick(position)
//            }
//        }
//    }
//
//    override fun getItemCount(): Int = list.size
//}



class LanguageAdapter(
    private val activity: Activity,
    private var list: List<Any>, // Changed to Any for Ad support
    private val onItemClick: (LanguageModel) -> Unit // Better to pass Model than position
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class LanguageViewHolder(val binding: ItemLanguageBinding) : RecyclerView.ViewHolder(binding.root)
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
            LanguageViewHolder(ItemLanguageBinding.inflate(inflater, parent, false))
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
            val item = list[position] as LanguageModel
            val langHolder = holder as LanguageViewHolder

            langHolder.binding.apply {
                tvLanguageName.text = item.name
                ivFlag.setImageResource(item.flag)

                if (item.isSelected) {
                    clMain.setBackgroundResource(R.drawable.bg_language_item_selected)
                    ivRadio.setImageResource(R.drawable.ic_radio_selected)
                } else {
                    clMain.setBackgroundResource(R.drawable.bg_language_item_unselected)
                    ivRadio.setImageResource(R.drawable.ic_radio_unselected)
                }

                root.setOnClickListener {
                    onItemClick(item) // Return the model safely
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size
}