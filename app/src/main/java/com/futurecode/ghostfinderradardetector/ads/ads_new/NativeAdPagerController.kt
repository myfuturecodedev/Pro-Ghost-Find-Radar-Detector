package com.futurecode.ghostfinderradardetector.ads.ads_new

import com.futurecode.ghostfinderradardetector.databinding.ItemOnboardingNativeAdBinding


class NativeAdPagerController(
    private val nativeAdPageLoader: NativeAdPageLoader
) {

    fun bind(pageKey: String, binding: ItemOnboardingNativeAdBinding) {
        if (binding.root.tag == pageKey) return
        binding.root.tag = pageKey
        nativeAdPageLoader.load(binding)
    }
}