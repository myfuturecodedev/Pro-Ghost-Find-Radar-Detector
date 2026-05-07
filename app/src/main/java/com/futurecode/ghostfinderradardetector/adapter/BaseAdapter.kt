package com.futurecode.ghostfinderradardetector.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.futurecode.ghostfinderradardetector.utils.AdapterUtil

abstract class BaseAdapter<type> : RecyclerView.Adapter<RecyclerView.ViewHolder?>(),
    AdapterUtil<type?> {

    var itemList: ArrayList<type?> = ArrayList()

    object NativeAdItem

    fun buildListWithAds(
        list: List<type?>,
        firstAdIndex: Int = 4,
        adOffset: Int = 4
    ): ArrayList<Any> {
        val result = arrayListOf<Any>()
        val cleanList = list.filterNotNull()

        if (cleanList.isEmpty()) return result

        var nextAdIndex = firstAdIndex

        cleanList.forEachIndexed { index, item ->
            result.add(item)

            if (index + 1 == nextAdIndex && index != cleanList.lastIndex) {
                result.add(NativeAdItem)
                nextAdIndex += adOffset
            }
        }

        return result
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun set(list: MutableList<type?>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    public override fun add(list: MutableList<type?>?) {
        val position = itemList.size
        itemList.addAll(list!!)
        notifyItemInserted(position)
    }

    public override fun add(item: type?) {
        val position = itemList.size
        itemList.add(item)
        notifyItemInserted(position)
    }

    public override fun add(item: type?, position: Int) {
        itemList.add(position, item)
        notifyItemInserted(position)
    }

    fun remove(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeData(position: Int) {
        itemList.removeAt(position)
        notifyDataSetChanged()
    }

    fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }

    fun refreshPosition(position: Int) {
        notifyItemChanged(position)
    }

    override fun get(position: Int): type? {
        return itemList[position]
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}