package com.futurecode.ghostfinderradardetector.fragment.afterlogin.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.ghostfinderradardetector.ads.native_ad.NativeAdsHelper
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentSettingBinding
import com.futurecode.ghostfinderradardetector.utils.Utils.setAdClickListener

class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {
    private lateinit var nativeAdsHelper:NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nativeAdsHelper= NativeAdsHelper(requireActivity())
        fullScreenAdsHelper= FullScreenAdsHelper(requireActivity())

        loadNativeAds()
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnHowToUse.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            // Navigate to How to use or show a dialog
            findNavController().navigate(R.id.action_settingFragment_to_howToUseFragment)
        }

        binding.btnLanguage.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            findNavController().navigate(R.id.action_settingFragment_to_languageFragment)
        }

        binding.btnShare.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            shareApp()
        }

        binding.btnRate.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
        }

        binding.btnPrivacy.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            openPrivacyPolicy()
        }
    }

    fun loadNativeAds(){
        nativeAdsHelper = NativeAdsHelper(requireActivity())
        nativeAdsHelper?.showNativeAd(
            nativeBannerAdView = binding.nativeAds3.frame,
            mainLayout = binding.nativeAds3.mainLayout,
            placeholder = binding.nativeAds3.placeholder
        )
    }

    private fun shareApp() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Check out this Ghost Finder app: https://play.google.com/store/apps/details?id=${requireContext().packageName}")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, null))
    }

    private fun rateApp() {
        val uri = Uri.parse("market://details?id=${requireContext().packageName}")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(goToMarket)
        } catch (e: Exception) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${requireContext().packageName}")))
        }
    }

    private fun openPrivacyPolicy() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com")) // Replace with actual URL
        startActivity(browserIntent)
    }
}