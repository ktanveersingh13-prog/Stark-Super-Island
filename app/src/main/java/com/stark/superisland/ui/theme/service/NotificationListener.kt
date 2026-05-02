package com.stark.superisland.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.LruCache
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap

class NotificationListener : NotificationListenerService() {

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val activeNotifications = ConcurrentHashMap<String, NotificationData>()
    private val appLabelCache = LruCache<String, String>(100)

    companion object {
        const val LIVE_UPDATE_CHANNEL_ID = "stark_live_updates"
        const val LIVE_UPDATE_CHANNEL_NAME = "System Live Updates"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName == packageName || sbn.isAppGroupSummary) return

        serviceScope.launch {
            val key = sbn.key
            val extras = sbn.notification.extras

            val title = resolveTitle(sbn)
            val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""
            val progress = extras.getInt(Notification.EXTRA_PROGRESS)
            val progressMax = extras.getInt(Notification.EXTRA_PROGRESS_MAX)

            val data = NotificationData(
                id = sbn.id,
                packageName = sbn.packageName,
                title = title,
                text = text,
                progress = if (progressMax > 0) (progress * 100 / progressMax) else -1,
                isOngoing = (sbn.notification.flags and Notification.FLAG_ONGOING_EVENT) != 0
            )

            activeNotifications[key] = data
            if (data.isOngoing) { updateNativeLiveUpdate(data) }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        activeNotifications.remove(sbn.key)
        if ((sbn.notification.flags and Notification.FLAG_ONGOING_EVENT) != 0) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.cancel(sbn.id)
        }
    }

    private fun resolveTitle(sbn: StatusBarNotification): String {
        val extras = sbn.notification.extras
        var title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()?.trim() ?: ""
        if (title.isEmpty()) title = extras.getCharSequence(Notification.EXTRA_TITLE_BIG)?.toString()?.trim() ?: ""
        return title.ifEmpty { getCachedAppLabel(sbn.packageName) }
    }

    private fun getCachedAppLabel(packageName: String): String {
        appLabelCache.get(packageName)?.let { return it }
        return try {
            val pm = packageManager
            val label = pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0)).toString()
            appLabelCache.put(packageName, label)
            label
        } catch (e: Exception) { packageName }
    }

    private fun updateNativeLiveUpdate(data: NotificationData) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this, LIVE_UPDATE_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) 
            .setContentTitle(data.title)
            .setContentText(data.text)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        if (data.progress >= 0) builder.setProgress(100, data.progress, false)
        manager.notify(data.id, builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(LIVE_UPDATE_CHANNEL_ID, LIVE_UPDATE_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    data class NotificationData(val id: Int, val packageName: String, val title: String, val text: String, val progress: Int, val isOngoing: Boolean)
}
