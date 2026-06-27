package com.futurecode.ghostfinderradardetector.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.futurecode.ghostfinderradardetector.databinding.ItemHowToUseBinding
import com.bumptech.glide.Glide
import com.futurecode.ghostfinderradardetector.R


//class HowToUseAdapter(private val pageCount: Int) : RecyclerView.Adapter<HowToUseAdapter.ViewHolder>() {
//    inner class ViewHolder(val binding: ItemHowToUseBinding) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = ItemHowToUseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        with(holder.binding) {
//            clPage1.visibility = if (position == 0) View.VISIBLE else View.GONE
//            clPage2.visibility = if (position == 1) View.VISIBLE else View.GONE
//            clPage3.visibility = if (position == 2) View.VISIBLE else View.GONE
//        }
//    }
//
//    override fun getItemCount(): Int = pageCount
//}




class HowToUseAdapter(private val pageCount: Int) : RecyclerView.Adapter<HowToUseAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemHowToUseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHowToUseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context

        with(holder.binding) {
            // 1. SAFE IMAGE LOADING USING GLIDE (RAM Blast Prevention)
            // Heavy backgrounds aur overlay images ko Glide runtime par automatic compress karega
            Glide.with(context)
                .load(R.drawable.how_to_use_two)
                .into(object : com.bumptech.glide.request.target.CustomTarget<android.graphics.drawable.Drawable>() {
                    override fun onResourceReady(
                        resource: android.graphics.drawable.Drawable,
                        transition: com.bumptech.glide.request.transition.Transition<in android.graphics.drawable.Drawable>?
                    ) {
                        clPhoneFrame.background = resource
                    }
                    override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {
                        clPhoneFrame.background = null
                    }
                })

            // Page 1 memory logic
            if (position == 0) {
                clPage1.visibility = View.VISIBLE
                Glide.with(context).load(R.drawable.how_to_use_im).into(ivRadar)
            } else {
                clPage1.visibility = View.GONE
                Glide.with(context).clear(ivRadar) // Clear bitmap memory
            }

            // Page 2 memory logic
            clPage2.visibility = if (position == 1) View.VISIBLE else View.GONE

            // Page 3 memory logic
            if (position == 2) {
                clPage3.visibility = View.VISIBLE
                Glide.with(context).load(R.drawable.onboard3).into(ivGhostFound)
            } else {
                clPage3.visibility = View.GONE
                Glide.with(context).clear(ivGhostFound) // Clear bitmap memory
            }
        }
    }

    override fun getItemCount(): Int = pageCount
}