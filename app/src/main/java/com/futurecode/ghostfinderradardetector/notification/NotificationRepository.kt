package com.futurecode.ghostfinderradardetector.notification

import com.futurecode.ghostfinderradardetector.utils.getNotificationListFromPrefs


object NotificationRepository {
    val notifications = getNotificationListFromPrefs()

}