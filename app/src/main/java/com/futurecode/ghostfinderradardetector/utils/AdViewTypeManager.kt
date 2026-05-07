package com.futurecode.ghostfinderradardetector.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

//object AdViewTypeManager {
//    const val TYPE_ITEM = 1
//    const val TYPE_AD = 2
//
//    // 1. Wraps any list with "AD_UNIT" markers
//    fun <T> wrapList(list: List<T>, interval: Int = 3): List<Any> {
//        val newList = mutableListOf<Any>()
//        list.forEachIndexed { index, item ->
//            newList.add(item as Any)
//            if ((index + 1) % interval == 0) newList.add("AD_UNIT")
//        }
//        return newList
//    }
//
//    // 2. IMPORTANT: Makes Ads full-width in Grids
//    fun setGridSpanSize(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>, spanCount: Int) {
//        val layoutManager = recyclerView.layoutManager
//        if (layoutManager is GridLayoutManager) {
//            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//                override fun getSpanSize(position: Int): Int {
//                    return if (adapter.getItemViewType(position) == TYPE_AD) {
//                        spanCount // Full width
//                    } else {
//                        1 // Single column
//                    }
//                }
//            }
//        }
//    }
//}


object AdViewTypeManager {
    const val TYPE_ITEM = 1
    const val TYPE_AD = 2

    /**
     * @param list The original data list
     * @param interval The frequency of ads (e.g., every 3 items)
     * @param isSingleAd If true, only ONE ad will be placed at the [interval] position
     */
    fun <T> wrapList(list: List<T>, interval: Int = 3, isSingleAd: Boolean = false): List<Any> {
        val newList = mutableListOf<Any>()

        if (isSingleAd) {
            // Logic for a single ad at a specific index
            list.forEachIndexed { index, item ->
                if (index == interval) {
                    newList.add("AD_UNIT")
                }
                newList.add(item as Any)
            }
            // If list is too short, add it at the end
            if (list.size <= interval && !newList.contains("AD_UNIT")) {
                newList.add("AD_UNIT")
            }
        } else {
            // Your original logic for repeating ads
            list.forEachIndexed { index, item ->
                newList.add(item as Any)
                if ((index + 1) % interval == 0) {
                    newList.add("AD_UNIT")
                }
            }
        }
        return newList
    }

    // Makes Ads full-width in Grids (Works for both single or multiple ads)
    fun setGridSpanSize(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>, spanCount: Int) {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapter.getItemViewType(position) == TYPE_AD) {
                        spanCount // Take up all columns
                    } else {
                        1 // Take up only one column
                    }
                }
            }
        }
    }
}