package com.futurecode.ghostfinderradardetector.ads.interstitial_ad


import android.app.Activity
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.ads.AdInterface
import com.futurecode.ghostfinderradardetector.databinding.CustomFullscreenAdBinding
import com.futurecode.ghostfinderradardetector.utils.Utils

class CustomFullScreenAdsHelper(activity: Activity) {
    private val alertDialog: AlertDialog?
    val dialogView: View = LayoutInflater.from(activity).inflate(R.layout.custom_fullscreen_ad, null, false)
    val bind = CustomFullscreenAdBinding.bind(dialogView)
    lateinit var adInterface: AdInterface
    val countdownTimer = object : CountDownTimer(3000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val secondsRemaining = millisUntilFinished / 1000
            bind.countdownTextView.text = secondsRemaining.toString()
            bind.progressBar.progress = secondsRemaining.toInt()
        }

        override fun onFinish() {
            bind.closeImageView.visibility = View.VISIBLE
            bind.progressBar.visibility = View.GONE
            bind.countdownTextView.visibility = View.GONE
        }
    }
    init {
        val builder = AlertDialog.Builder(activity, android.R.style.Theme_Material_Wallpaper_NoTitleBar)
        builder.setView(bind.root)
        alertDialog = builder.create()
        alertDialog.setCancelable(false)

        loadWebVIewSettings(bind.webView)




        // Set up close button
        bind.closeImageView.setOnClickListener {
            bind.webView.clearCache(true)
            bind.webView.clearFormData()
            bind.webView.stopLoading()
            bind.webView.destroy()
            alertDialog.dismiss()
            adInterface.finished()
        }

        // Load the URL into the WebView
        bind.webView.loadUrl(Utils.getRandomUrls(activity))



    }
    fun show( adInterface: AdInterface) {
        this.adInterface = adInterface
        Handler(Looper.getMainLooper()).postDelayed({
            // Start the countdown timer
            countdownTimer.start()
            if (alertDialog != null && !alertDialog.isShowing) alertDialog.show()
        }, 500)
    }

    private fun loadWebVIewSettings(webView: WebView) {

        val settings = webView.settings

        // Enable java script in web view
        settings.javaScriptEnabled = true

        // Enable and setup web view cache
        settings.cacheMode = WebSettings.LOAD_DEFAULT


        // Enable zooming in web view
        settings.setSupportZoom(false)
        settings.builtInZoomControls = false
        settings.displayZoomControls = false

        // Zoom web view text
        settings.textZoom = 100

        // Enable disable images in web view
        settings.blockNetworkImage = false
        // Whether the WebView should load image resources
        settings.loadsImagesAutomatically = true


        // More web view settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.safeBrowsingEnabled = true  // api 26
        }
        //settings.pluginState = WebSettings.PluginState.ON
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.mediaPlaybackRequiresUserGesture = false


        // More optional settings, you can enable it by yourself
        settings.domStorageEnabled = true
        settings.setSupportMultipleWindows(true)
        settings.loadWithOverviewMode = true
        settings.allowContentAccess = true
        settings.setGeolocationEnabled(true)
        settings.allowUniversalAccessFromFileURLs = true
        settings.allowFileAccess = true

        // WebView settings
        webView.fitsSystemWindows = true

        /*
           if SDK version is greater of 19 then activate hardware acceleration
           otherwise activate software acceleration
       */
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        // Set web view client
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }
        }

    }
}