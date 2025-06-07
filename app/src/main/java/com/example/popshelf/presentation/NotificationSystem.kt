package com.example.popshelf.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Class implementing the notification system for Popshelf application, instance creation automatically
 * sets up the notification channel/s and schedule periodicNotification events.
 * @property appContext application context
 */
class NotificationSystem(private val appContext: Context) {

    init {
        createNotificationChannel()
        schedulePeriodicNotification()
    }

    private fun createNotificationChannel() {
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val channel = NotificationChannel("popshelf_notifications", "Popshelf Notification", NotificationManager.IMPORTANCE_HIGH).apply {
            //zvuk sa nastavi do kategoria notifikacii, čiže hlasitost podla notifikaci, do not disturb atd.., zvuk je typicke systemove pipnutie
            val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
            setSound(soundUri, audioAttributes)
        }
        val manager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    /**
     * Method which helps to run and test notifications
     */
    fun runTestNotif() {
        val request = OneTimeWorkRequestBuilder<NotificationWorker>().build()
        WorkManager.getInstance(appContext).enqueueUniqueWork("test_notification", ExistingWorkPolicy.KEEP, request)
    }

    private fun schedulePeriodicNotification() {
        val request = PeriodicWorkRequestBuilder<NotificationWorker>(20, TimeUnit.MINUTES).build()

        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
            "periodic_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}