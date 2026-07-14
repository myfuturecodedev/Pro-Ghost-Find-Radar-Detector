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
    private var nativeAdsHelper: NativeAdsHelper? = null
    private var fullScreenAdsHelper: FullScreenAdsHelper? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let { currentActivity ->
            nativeAdsHelper = NativeAdsHelper(currentActivity)
            fullScreenAdsHelper = FullScreenAdsHelper(currentActivity)
            setupUI(currentActivity)
        }

        loadNativeAds()
    }

    private fun setupUI(currentActivity: androidx.fragment.app.FragmentActivity) {
        binding.ivBack.setOnClickListener {
            if (isAdded) {
                findNavController().navigateUp()
            }
        }

        fullScreenAdsHelper?.let { helper ->

            binding.btnHowToUse.setAdClickListener(currentActivity, helper) {
                if (isAdded) {
                    findNavController().navigate(R.id.action_settingFragment_to_howToUseFragment)
                }
            }

            binding.btnLanguage.setAdClickListener(currentActivity, helper) {
                if (isAdded) {
                    val bundle = Bundle().apply {
                        putBoolean("isFromSettings", true)
                    }
                    findNavController().navigate(R.id.action_settingFragment_to_languageFragment, bundle)
                }
            }

            binding.btnShare.setAdClickListener(currentActivity, helper) {
                shareApp()
            }

            binding.btnRate.setAdClickListener(currentActivity, helper) {
                rateApp()
            }

            binding.btnPrivacy.setAdClickListener(currentActivity, helper) {
                openPrivacyPolicy()
            }
        }
    }

    fun loadNativeAds() {
        activity?.let { currentActivity ->
            if (nativeAdsHelper == null) {
                nativeAdsHelper = NativeAdsHelper(currentActivity)
            }
            nativeAdsHelper?.showNativeAd(
                nativeBannerAdView = binding.nativeAds3.frame,
                mainLayout = binding.nativeAds3.mainLayout,
                placeholder = binding.nativeAds3.placeholder
            )
        }
    }

    private fun shareApp() {
        val currentContext = context ?: return
        try {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Check out this Ghost Finder app: https://play.google.com/store/apps/details?id=${currentContext.packageName}")
                type = "text/plain"
            }
            startActivity(Intent.createChooser(sendIntent, null))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun rateApp() {
        val currentContext = context ?: return
        val uri = Uri.parse("market://details?id=${currentContext.packageName}")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(goToMarket)
        } catch (e: Exception) {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${currentContext.packageName}")))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun openPrivacyPolicy() {
        val urlString = prefManager.privacyPolicy

        if (urlString.trim().isEmpty()) {
            return
        }

        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
            startActivity(browserIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
