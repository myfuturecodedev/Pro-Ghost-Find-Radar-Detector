package com.futurecode.ghostfinderradardetector.activity

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import com.facebook.ads.AudienceNetworkAds
import com.futurecode.ghostfinderradardetector.ads.app_open_ad.AppOpenHelperNew
import com.futurecode.ghostfinderradardetector.utils.JsonReadUtils
import com.futurecode.ghostfinderradardetector.utils.NetworkMonitor
import com.futurecode.ghostfinderradardetector.utils.PrefManager
import com.google.android.gms.ads.MobileAds
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import java.util.Locale

class MyApplication : Application() {

    lateinit var prefManager: PrefManager
    var appOpenHelper: AppOpenHelperNew? = null
    private lateinit var networkMonitor: NetworkMonitor
    private var currentActivity: Activity? = null

    private lateinit var analytics: FirebaseAnalytics

    override fun attachBaseContext(base: Context) {
        // Safe locale initialization for Application context
        val lang = PrefManager.get(base).selectedLanguage
        val locale = Locale(lang)
        Locale.setDefault(locale)

        val configuration = Configuration(base.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        // Using createConfigurationContext is the safe way for attachBaseContext
        super.attachBaseContext(base.createConfigurationContext(configuration))
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        prefManager = PrefManager.get(this)
        analytics = Firebase.analytics
        
        setupActivityTracker()
        networkMonitor = NetworkMonitor(this)
        networkMonitor.startMonitoring()
        
        JsonReadUtils.fetchJsonData(this)
        initializeADS()
    }

    private fun setupActivityTracker() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityStarted(activity: Activity) { currentActivity = activity }
            override fun onActivityStopped(activity: Activity) { if (currentActivity == activity) currentActivity = null }
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {}
            override fun onActivityResumed(p0: Activity) {}
            override fun onActivityPaused(p0: Activity) {}
            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
            override fun onActivityDestroyed(p0: Activity) {}
        })
    }

    fun getCurrentActivity(): Activity? = currentActivity

    fun checkInternetConnection() { /* Handled by NetworkMonitor */ }

    private fun initializeADS() {
        AudienceNetworkAds.initialize(this)
        MobileAds.initialize(this) {}
        if (!prefManager.adsOff) {
            appOpenHelper = AppOpenHelperNew(this)
        }
    }

    companion object {
        lateinit var app: MyApplication

        /**
         * Centralized method to localize a context.
         * Used in BaseActivity.attachBaseContext
         */
        fun setLocale(context: Context): Context {
            val pref = PrefManager.get(context)
            val lang = pref.selectedLanguage
            val locale = Locale(lang)
            Locale.setDefault(locale)

            val configuration = Configuration(context.resources.configuration)
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)

            // We do NOT call updateConfiguration here to avoid deadlock/black screens
            return context.createConfigurationContext(configuration)
        }
    }
}
