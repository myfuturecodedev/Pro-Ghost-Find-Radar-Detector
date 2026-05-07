package com.futurecode.ghostfinderradardetector.ads.banner_ad


import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import com.facebook.ads.AdView
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.activity.MyApplication
import com.futurecode.ghostfinderradardetector.utils.PrefManager
import com.futurecode.ghostfinderradardetector.utils.Utils
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError


class BannerAdsHelper(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var myPreferenceHelper: PrefManager = MyApplication.app.prefManager
    private lateinit var fb_adView: AdView
    private lateinit var admobAdView: com.google.android.gms.ads.AdView

    init {
        showAds(context)
    }

    fun reload() {
        removeAllViews()
        showAds(context)
    }

    fun showAds(context: Context) {
        if (!myPreferenceHelper.adsOff) {
            showAdmobBanner(context)
        }
    }

    private fun showCustomAd(context: Context) {
        orientation = VERTICAL

        val imageView = ImageView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            setImageResource(R.drawable.banner)
            scaleType = ImageView.ScaleType.FIT_CENTER
            setPadding(5, 5, 5, 5)
        }

        addView(imageView)

        imageView.setOnClickListener {
            val activity = context as? Activity
            activity?.let { act ->
                Utils.openCustomChrome(act, Utils.getRandomUrls(act))
            }
        }
    }

    private fun showAdmobBanner(context: Context) {
        admobAdView = com.google.android.gms.ads.AdView(context)
        removeAllViews()
        addView(admobAdView)
        admobAdView.setAdSize(AdSize.BANNER)
        admobAdView.adUnitId = myPreferenceHelper.admobBanner

        admobAdView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, "onAdFailedToLoad: ADMOB ${adError.message}")
                showCustomAd(context)
            }
        }

        val adRequest = AdRequest.Builder().build()
        admobAdView.loadAd(adRequest)
    }

    companion object {
        private const val TAG = "BannerAdManager"
    }
}