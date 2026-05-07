package com.futurecode.ghostfinderradardetector.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.ads.native_ad.NativeAdsHelper
import com.futurecode.ghostfinderradardetector.utils.AdViewTypeManager

abstract class UniversalAdAdapter(
    val activity: Activity,
    var displayList: List<Any>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (displayList[position] is String && displayList[position] == "AD_UNIT") {
            AdViewTypeManager.TYPE_AD
        } else {
            AdViewTypeManager.TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == AdViewTypeManager.TYPE_AD) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.native_ads_layout, parent, false)
            AdViewHolder(view)
        } else {
            onCreateItemHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == AdViewTypeManager.TYPE_AD) {
            NativeAdsHelper(activity).showNativeAd(
                holder.itemView.findViewById(R.id.frame_layout),
                holder.itemView.findViewById(R.id.relative_layout),
                holder.itemView.findViewById(R.id.placeholder)
            )
        } else {
            onBindItem(holder, displayList[position], position)
        }
    }

    override fun getItemCount(): Int = displayList.size

    abstract fun onCreateItemHolder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun onBindItem(holder: RecyclerView.ViewHolder, item: Any, position: Int)

    class AdViewHolder(view: View) : RecyclerView.ViewHolder(view)
}