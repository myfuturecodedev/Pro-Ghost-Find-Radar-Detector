package com.futurecode.ghostfinderradardetector.utils


import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import android.content.Context
import com.google.android.gms.ads.MobileAds


object AdMobManager {
    private const val TAG = "AdMobManager"
    private var mInterstitialAd: InterstitialAd? = null
    private var isAdLoading = false

    /**
     * 1. Reusable Ad Loading Function
     * Isse Mintegral, Pangle, aur Liftoff bidding ke zariye AdMob backup me load ho jayenge.
     */
    fun loadInterstitialAd(activity: Activity, adUnitId: String) {
        // Agar ad pehle se load ho raha hai ya load ho chuka hai, toh dubara request na bhejein
        if (isAdLoading || mInterstitialAd != null) return

        isAdLoading = true
        val adRequest = AdRequest.Builder().build()

        // AdMob official standard loading request
        InterstitialAd.load(activity, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e(TAG, "Bidding Networks se ad load nahi ho paya: ${adError.message}")
                mInterstitialAd = null
                isAdLoading = false
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad successfully load ho gaya! Network: ${interstitialAd.responseInfo?.mediationAdapterClassName}")
                mInterstitialAd = interstitialAd
                isAdLoading = false
            }
        })
    }

    /**
     * 2. Reusable Ad Showing Function
     * 'onAdClosedCallback' se hum user ko ad khatam hone par game ya next screen par bhejenge.
     */
    fun showInterstitialAd(activity: Activity, adUnitId: String, onAdClosedCallback: () -> Unit) {
        if (mInterstitialAd != null) {

            // FullScreenContentCallback se hume ad ke screen par aane aur jaane ka pata chalta hai
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "User ne ad close kar diya.")
                    mInterstitialAd = null

                    // Agla ad background me preload karlo taaki agli baar jaldi dikhe
                    loadInterstitialAd(activity, adUnitId)

                    // UI/Game flow ko aage badhayein
                    onAdClosedCallback()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e(TAG, "Ad screen par show nahi ho paya: ${adError.message}")
                    mInterstitialAd = null

                    // Fail hone par bhi preload karein aur user ko aage badha dein
                    loadInterstitialAd(activity, adUnitId)
                    onAdClosedCallback()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad screen par poori tarah dikh raha hai.")
                }
            }

            // Asli ad show karne ki command
            mInterstitialAd?.show(activity)

        } else {
            Log.w(TAG, "Ad abhi taiyar nahi tha, direct next screen par bhej rahe hain.")
            // Agar ad ready nahi hai toh user ko atkaye nahi, callback chalayein
            loadInterstitialAd(activity, adUnitId)
            onAdClosedCallback()
        }
    }
}