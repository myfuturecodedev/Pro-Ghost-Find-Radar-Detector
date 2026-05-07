package com.futurecode.ghostfinderradardetector.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.utils.NativeAdManager

abstract class BaseAdAdapter(
    val activity: Activity,
    private var displayList: List<Any>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (displayList[position] is String && displayList[position] == "AD_UNIT") {
            NativeAdManager.TYPE_AD
        } else {
            NativeAdManager.TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == NativeAdManager.TYPE_AD) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.native_ads_layout, parent, false)
            AdViewHolder(view)
        } else {
            onCreateContentHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == NativeAdManager.TYPE_AD) {
            NativeAdManager.bindNativeAd(activity, holder)
        } else {
            onBindContent(holder, displayList[position], position)
        }
    }

    override fun getItemCount(): Int = displayList.size

    // Abstract methods to be implemented by ScarySoundAdapter, etc.
    abstract fun onCreateContentHolder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun onBindContent(holder: RecyclerView.ViewHolder, item: Any, position: Int)

    class AdViewHolder(view: View) : RecyclerView.ViewHolder(view)
}