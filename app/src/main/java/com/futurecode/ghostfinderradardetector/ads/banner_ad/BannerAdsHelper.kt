package com.futurecode.ghostfinderradardetector.ads.banner_ad

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.activity.MyApplication
import com.futurecode.ghostfinderradardetector.utils.PrefManager
import com.futurecode.ghostfinderradardetector.utils.Utils
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
//
//class BannerAdsHelper @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : LinearLayout(context, attrs, defStyleAttr) {
//
//    private val prefManager: PrefManager = PrefManager.get(context)
//    private var admobAdView: com.google.android.gms.ads.AdView? = null
//
//    init {
//        orientation = VERTICAL
//        // Important: Ensure we don't try to show ads if the activity is finishing
//        if (context is Activity && (context.isFinishing || context.isDestroyed)) {
//            // Skip
//        } else {
//            showAds()
//        }
//    }
//
//    fun reload() {
//        showAds()
//    }
//
//    private fun showAds() {
//        Log.d("TAG", "showAds: ${prefManager.adsOff}")
//        if (!prefManager.adsOff) {
//            val adId = prefManager.admobBanner
//            if (adId.isNotEmpty()) {
//                loadAdmobBanner(adId)
//            } else {
//                showCustomFallback()
//            }
//        } else {
//            removeAllViews()
//        }
//    }
//
//    private fun showCustomFallback() {
//        removeAllViews()
//        val imageView = ImageView(context).apply {
//            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
//            setImageResource(R.drawable.banner)
//            scaleType = ImageView.ScaleType.CENTER_CROP
//        }
//        addView(imageView)
//        imageView.setOnClickListener {
//            val activity = context as? Activity
//            activity?.let { act ->
//                Utils.openCustomChrome(act, Utils.getRandomUrls(act))
//            }
//        }
//    }
//
//    private fun loadAdmobBanner(adId: String) {
//        admobAdView?.destroy()
//        admobAdView = com.google.android.gms.ads.AdView(context).apply {
//            adUnitId = adId
//            setAdSize(getAdaptiveSize())
//            adListener = object : AdListener() {
//                override fun onAdFailedToLoad(error: LoadAdError) {
//                    showCustomFallback()
//                }
//                override fun onAdLoaded() {
//                    removeAllViews()
//                    addView(this@apply)
//                }
//            }
//        }
//        admobAdView?.loadAd(AdRequest.Builder().build())
//    }
//
//    private fun getAdaptiveSize(): AdSize {
//        val displayMetrics = resources.displayMetrics
//        val adWidth = (displayMetrics.widthPixels / displayMetrics.density).toInt()
//        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
//    }
//
//    override fun onDetachedFromWindow() {
//        admobAdView?.destroy()
//        super.onDetachedFromWindow()
//    }
//}



class BannerAdsHelper(context: Context, attrs: AttributeSet?) :
    LinearLayout(context, attrs) {

    private var myPreferenceHelper: PrefManager = MyApplication.app.prefManager
    private var admobAdView: com.google.android.gms.ads.AdView? = null
    private var isShowingFallback = false

    init {
        showAds(context)
    }

    fun reload() {
        clearBanner()
        showAds(context)
    }

    private fun clearBanner() {
        isShowingFallback = false
        admobAdView?.destroy()
        admobAdView = null
        removeAllViews()
    }

    fun showAds(context: Context) {
        if (!myPreferenceHelper.adsOff) {
            showAdmobBanner(context)
        } else {
            clearBanner()
        }
    }

    private fun showCustomAd(context: Context) {
        if (isShowingFallback) return

        isShowingFallback = true
        removeAllViews()
        orientation = VERTICAL

        val imageView = ImageView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            setImageResource(R.drawable.banner)
            scaleType = ImageView.ScaleType.CENTER_CROP
            setPadding(5, 5, 5, 5)
            adjustViewBounds = true
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
        clearBanner()

        val localAdView = com.google.android.gms.ads.AdView(context)
        admobAdView = localAdView

        localAdView.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )

        addView(localAdView)

        val displayMetrics = resources.displayMetrics
        val adWidthPixels = displayMetrics.widthPixels.toFloat()
        val density = displayMetrics.density
        val adWidth = (adWidthPixels / density).toInt()

        val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
            context,
            adWidth
        )

        localAdView.setAdSize(adSize)
        localAdView.adUnitId = myPreferenceHelper.admobBanner

        localAdView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, "onAdFailedToLoad: ADMOB ${adError.message}")

                if (admobAdView !== localAdView) return
                showCustomAd(context)
            }
        }

        localAdView.loadAd(AdRequest.Builder().build())
    }

    override fun onDetachedFromWindow() {
        admobAdView?.destroy()
        admobAdView = null
        super.onDetachedFromWindow()
    }

    companion object {
        private const val TAG = "BannerAdManager"
    }
}