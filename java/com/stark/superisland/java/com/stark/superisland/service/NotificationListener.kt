package com.stark.superisland.service

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val title = sbn.notification.extras.getString("android.title") ?: ""
        val text = sbn.notification.extras.getString("android.text") ?: ""
        
        // Send data to the Floating Island
        val intent = Intent(this, OverlayService::class.java).apply {
            putExtra("TITLE", title)
            putExtra("TEXT", text)
        }
        startService(intent)
    }
}
