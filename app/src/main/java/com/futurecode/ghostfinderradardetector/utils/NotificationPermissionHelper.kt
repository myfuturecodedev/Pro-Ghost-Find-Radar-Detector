package com.futurecode.ghostfinderradardetector.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.futurecode.ghostfinderradardetector.activity.MyApplication
import com.futurecode.ghostfinderradardetector.notification.NotificationScheduler

//class NotificationPermissionHelper(private val fragment: Fragment,private val context: Context) {
//
//    // 1. Register the launcher immediately when the class is created.
//    // This MUST happen before the Fragment reaches the Resumed state.
//    private val requestPermissionLauncher =
//        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//            if (isGranted) {
//                //
//
//                NotificationScheduler.startNotificationWorker(context)
//                MyApplication.app.prefManager.isNotificationStarts = true
//
//                Log.d("NotificationHelper", "Notification permission GRANTED by user.")
//            } else {
//                Log.d("NotificationHelper", "Notification permission DENIED by user.")
//                // Optional: You can show a Toast or Dialog here explaining why you need it
//            }
//        }
//
//    // 2. The single function to call from your Fragment
//    fun checkAndRequestPermission() {
//        // Android 13 (API 33) and above require runtime permission
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            when {
//                ContextCompat.checkSelfPermission(
//                    fragment.requireContext(),
//                    Manifest.permission.POST_NOTIFICATIONS
//                ) == PackageManager.PERMISSION_GRANTED -> {
//                    // Permission is already granted, do nothing
//                    Log.d("NotificationHelper", "Permission already granted.")
//                    NotificationScheduler.startNotificationWorker(context)
//                    MyApplication.app.prefManager.isNotificationStarts = true
//
//                }
//                fragment.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
//                    // The user denied it previously but didn't check "Don't ask again"
//                    // We can ask again.
//                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
//                }
//                else -> {
//                    // First time asking
//                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
//                }
//            }
//        } else {
//            // Android 12 and below: Notifications are enabled by default at install time
//            Log.d("NotificationHelper", "Android < 13 detected. No runtime permission required.")
//        }
//    }
//}


class NotificationPermissionHelper(private val fragment: Fragment) {

    // 1. Register the launcher immediately using only the fragment instance
    private val requestPermissionLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Safely grab the context now that the fragment is fully attached and operational
                val safeContext = fragment.requireContext()
                NotificationScheduler.startNotificationWorker(safeContext)
                MyApplication.app.prefManager.isNotificationStarts = true

                Log.d("NotificationHelper", "Notification permission GRANTED by user.")
            } else {
                Log.d("NotificationHelper", "Notification permission DENIED by user.")
            }
        }

    // 2. Main check and request execution block
    fun checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val safeContext = fragment.requireContext()

            when {
                ContextCompat.checkSelfPermission(
                    safeContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Log.d("NotificationHelper", "Permission already granted.")
                    NotificationScheduler.startNotificationWorker(safeContext)
                    MyApplication.app.prefManager.isNotificationStarts = true
                }
                fragment.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            Log.d("NotificationHelper", "Android < 13 detected. No runtime permission required.")
        }
    }
}