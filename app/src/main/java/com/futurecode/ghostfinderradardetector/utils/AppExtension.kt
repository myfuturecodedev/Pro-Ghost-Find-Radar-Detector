package com.futurecode.ghostfinderradardetector.utils

import android.util.Log
import com.futurecode.ghostfinderradardetector.activity.MyApplication
import com.futurecode.ghostfinderradardetector.notification.NotificationModel
import com.google.gson.Gson

fun getNotificationListFromPrefs(): List<NotificationModel> {
    return try {
        val jsonString = MyApplication.app.prefManager.notificationList

        Log.d("TAG", "getNotificationListFromPrefs: $jsonString")
        if (jsonString.isNullOrEmpty()) return emptyList()

        Gson().fromJson(
            jsonString,
            Array<NotificationModel>::class.java
        )?.toList() ?: emptyList()

    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}
