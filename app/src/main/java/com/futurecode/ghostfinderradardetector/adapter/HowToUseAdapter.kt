package com.futurecode.ghostfinderradardetector.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.futurecode.ghostfinderradardetector.databinding.ItemHowToUseBinding

class HowToUseAdapter(private val pageCount: Int) : RecyclerView.Adapter<HowToUseAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemHowToUseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHowToUseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            clPage1.visibility = if (position == 0) View.VISIBLE else View.GONE
            clPage2.visibility = if (position == 1) View.VISIBLE else View.GONE
            clPage3.visibility = if (position == 2) View.VISIBLE else View.GONE
        }
    }

    override fun getItemCount(): Int = pageCount
}