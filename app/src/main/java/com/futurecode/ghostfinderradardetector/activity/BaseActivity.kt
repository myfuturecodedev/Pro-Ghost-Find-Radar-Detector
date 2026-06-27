package com.futurecode.ghostfinderradardetector.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        // Use the centralized localized context from MyApplication
        super.attachBaseContext(MyApplication.setLocale(newBase))
    }
}
