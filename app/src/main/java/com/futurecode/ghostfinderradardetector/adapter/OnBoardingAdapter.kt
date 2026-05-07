package com.futurecode.ghostfinderradardetector.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.futurecode.ghostfinderradardetector.databinding.ItemOnboardingBinding
import com.futurecode.ghostfinderradardetector.model.OnBoardingModel

class OnBoardingAdapter(private val list: List<OnBoardingModel>) :
    RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {
    inner class OnBoardingViewHolder(val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        val binding = ItemOnboardingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OnBoardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        val item = list[position]
        holder.binding.apply {
            ivOnboarding.setImageResource(item.image)
            tvTitle.text = item.title
            tvDescription.text = item.description
        }
    }

    override fun getItemCount(): Int = list.size
}