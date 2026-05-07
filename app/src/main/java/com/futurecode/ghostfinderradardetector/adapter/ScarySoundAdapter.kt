package com.futurecode.ghostfinderradardetector.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.ads.native_ad.NativeAdsHelper
import com.futurecode.ghostfinderradardetector.databinding.ItemNativeAdsAdapterBinding
import com.futurecode.ghostfinderradardetector.databinding.ItemScarySoundBinding
import com.futurecode.ghostfinderradardetector.model.ScarySoundModel
import com.futurecode.ghostfinderradardetector.utils.AdViewTypeManager

//class ScarySoundAdapter(
//    private val activity: Activity,
//    private var list: List<Any>, // Changed to Any to support Ads
//    private val onPlayClick: (Int) -> Unit
//) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    // 1. Define ViewHolders
//    inner class ItemViewHolder(val binding: ItemScarySoundBinding) : RecyclerView.ViewHolder(binding.root)
//    inner class AdViewHolder(val binding: ItemNativeAdBinding) : RecyclerView.ViewHolder(binding.root)
//
//    // 2. Handle View Types
//    override fun getItemViewType(position: Int): Int {
//        return if (list[position] is String && list[position] == "AD_UNIT") {
//            AdViewTypeManager.TYPE_AD
//        } else {
//            AdViewTypeManager.TYPE_ITEM
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return if (viewType == AdViewTypeManager.TYPE_AD) {
//            val binding = ItemNativeAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            AdViewHolder(binding)
//        } else {
//            val binding = ItemScarySoundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            ItemViewHolder(binding)
//        }
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (getItemViewType(position) == AdViewTypeManager.TYPE_AD) {
//            // Bind Native Ad
//            val adHolder = holder as AdViewHolder
//            NativeAdsHelper(activity).showNativeAd(
//                adHolder.binding.frameLayout,
//                adHolder.binding.relativeLayout,
//                adHolder.binding.placeholder
//            )
//        } else {
//            // Bind Scary Sound Content
//            val item = list[position] as ScarySoundModel
//            val itemHolder = holder as ItemViewHolder
//            bindScarySound(itemHolder, item, position)
//        }
//    }
//
//    private fun bindScarySound(holder: ItemViewHolder, item: ScarySoundModel, position: Int) {
//        holder.binding.tvTitle.text = item.title
//        holder.binding.ivIcon.setImageResource(item.icon)
//        val context = holder.itemView.context
//
//        if (item.isPlaying) {
//            holder.binding.clMain.setBackgroundResource(R.drawable.bg_ghost_card_selected)
//            holder.binding.llPlaying.visibility = View.VISIBLE
//            holder.binding.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.neon_green))
//            holder.binding.ivAction.apply {
//                setImageResource(android.R.drawable.ic_media_pause)
//                setBackgroundResource(R.drawable.bg_play_pause_active)
//                setColorFilter(ContextCompat.getColor(context, R.color.white))
//                setPadding(0, 0, 0, 0)
//            }
//        } else {
//            holder.binding.clMain.setBackgroundResource(R.drawable.bg_ghost_card_unselected)
//            holder.binding.llPlaying.visibility = View.GONE
//            holder.binding.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.white))
//            holder.binding.ivAction.apply {
//                setImageResource(android.R.drawable.ic_media_play)
//                setBackgroundResource(R.drawable.bg_circle_border)
//                setColorFilter(ContextCompat.getColor(context, R.color.white))
//                val padding = context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._8sdp)
//                setPadding(padding, padding, padding, padding)
//            }
//        }
//
//        holder.binding.ivAction.setOnClickListener {
//            onPlayClick(position)
//        }
//    }
//
//    override fun getItemCount(): Int = list.size
//
//    fun updateList(newList: List<Any>) {
//        list = newList
//        notifyDataSetChanged()
//    }
//}


class ScarySoundAdapter(
    private val activity: Activity,
    private var list: List<Any>,
    private val onPlayClick: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 1. Define separate ViewHolders
    inner class ItemViewHolder(val binding: ItemScarySoundBinding) : RecyclerView.ViewHolder(binding.root)
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
            // FIX: Inflate the Ad layout
            val binding = ItemNativeAdsAdapterBinding.inflate(inflater, parent, false)
            AdViewHolder(binding)
        } else {
            // FIX: Inflate the Scary Sound layout
            val binding = ItemScarySoundBinding.inflate(inflater, parent, false)
            ItemViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == AdViewTypeManager.TYPE_AD) {
            val adHolder = holder as AdViewHolder
            // Bind using your helper
            NativeAdsHelper(activity).showNativeAd(
                adHolder.binding.frameLayout,
                adHolder.binding.relativeLayout,
                adHolder.binding.placeholder
            )
        } else {
            val item = list[position] as ScarySoundModel
            val itemHolder = holder as ItemViewHolder
            bindScarySound(itemHolder, item, position)
        }
    }

    private fun bindScarySound(holder: ItemViewHolder, item: ScarySoundModel, position: Int) {
        holder.binding.tvTitle.text = item.title
        holder.binding.ivIcon.setImageResource(item.icon)
        val context = holder.itemView.context

        if (item.isPlaying) {
            holder.binding.clMain.setBackgroundResource(R.drawable.bg_ghost_card_selected)
            holder.binding.llPlaying.visibility = View.VISIBLE
            holder.binding.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.neon_green))
            holder.binding.ivAction.apply {
                setImageResource(android.R.drawable.ic_media_pause)
                setBackgroundResource(R.drawable.bg_play_pause_active)
                setColorFilter(ContextCompat.getColor(context, R.color.white))
                setPadding(0, 0, 0, 0)
            }
        } else {
            holder.binding.clMain.setBackgroundResource(R.drawable.bg_ghost_card_unselected)
            holder.binding.llPlaying.visibility = View.GONE
            holder.binding.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.white))
            holder.binding.ivAction.apply {
                setImageResource(android.R.drawable.ic_media_play)
                setBackgroundResource(R.drawable.bg_circle_border)
                setColorFilter(ContextCompat.getColor(context, R.color.white))
                val padding = context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._8sdp)
                setPadding(padding, padding, padding, padding)
            }
        }

        holder.binding.ivAction.setOnClickListener {
            onPlayClick(position)
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<Any>) {
        list = newList
        notifyDataSetChanged()
    }
}