package com.futurecode.ghostfinderradardetector.utils

import android.app.Activity
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.ads.native_ad.NativeAdsHelper
import com.google.android.material.imageview.ShapeableImageView

object NativeAdManager {
    const val TYPE_ITEM = 1
    const val TYPE_AD = 2

    // 1. Reusable method to inject "Ad Markers" into any list
    fun <T> wrapListWithAds(originalList: List<T>, interval: Int = 3): List<Any> {
        val newList = mutableListOf<Any>()
        originalList.forEachIndexed { index, item ->
            newList.add(item as Any)
            if ((index + 1) % interval == 0) {
                newList.add("AD_UNIT") // Marker for Ad
            }
        }
        return newList
    }

    // 2. Reusable binding logic for any Adapter
    fun bindNativeAd(activity: Activity, holder: RecyclerView.ViewHolder) {
        // We use your existing Helper
        val adContainer = holder.itemView.findViewById<FrameLayout>(R.id.frame_layout)
        val mainLayout = holder.itemView.findViewById<RelativeLayout>(R.id.relative_layout)
        val placeholder = holder.itemView.findViewById<ShapeableImageView>(R.id.placeholder)

        NativeAdsHelper(activity).showNativeAd(adContainer, mainLayout, placeholder)
    }
}