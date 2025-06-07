package com.example.popshelf.presentation

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.popshelf.PopshelfApplication
import com.example.popshelf.R
import com.example.popshelf.domain.MediaItem
import kotlin.random.Random


/**
 * Custom wrapper class around [CoroutineWorker] from WorkManager. Class implements basic algorithms
 * to create notifications based on random chance every 12 hours.
 *
 * @author David Bolko
 * @param appContext application context
 * @param workerParams paramaters for couroutine worker
 */
class NotificationWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {
    private val app = applicationContext as PopshelfApplication
    private val shelfItemRepositary = app.appContainer.shelfItemRepo


    private suspend fun randomTip(): Notification? {
        val items = shelfItemRepositary.getShelfItems(null)

        val ongoing = items.distinctBy{ it.status == "Ongoing"}
        val planned = items.distinctBy { it.status == "Planned"}

        lateinit var randomTipNotificationItem: MediaItem

        if(ongoing.isNotEmpty() || planned.isNotEmpty()){
            val random = Random(System.currentTimeMillis()).nextInt(1, 3)
            when(random){
                1 -> randomTipNotificationItem = ongoing.random()
                2 -> randomTipNotificationItem = planned.random()
            }

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("popshelf://detail/${randomTipNotificationItem.id}/${randomTipNotificationItem.mediaType.name}/${false}")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            return NotificationCompat.Builder(applicationContext, "popshelf_notifications")
                .setSmallIcon(R.drawable.notif_ico)
                .setContentTitle(applicationContext.getString(R.string.app_name))
                .setContentText(applicationContext.getString(R.string.notification_tip, randomTipNotificationItem.title))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
        }
        return null
    }

    private suspend fun rateTip(): Notification? {
        val items = shelfItemRepositary.getShelfItems(null)

        val item = items.filter { it.status == "Ongoing"}.randomOrNull()

        if(item == null){
           return null
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("popshelf://add/${item.id}/${item.mediaType.name}/${item.shelfId}/${true}")).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)


        return NotificationCompat.Builder(applicationContext, "popshelf_notifications")
            .setSmallIcon(R.drawable.notif_ico)
            .setContentTitle(applicationContext.getString(R.string.app_name))
            .setContentText(applicationContext.getString(R.string.notification_rate_tip, item.title))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    override suspend fun doWork(): Result {
        val notification: Notification? = when (Random.nextBoolean()) {
            true -> randomTip()
            false -> rateTip()
        }

        if(notification != null){
            try {
                NotificationManagerCompat.from(applicationContext).notify(1, notification)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }

        return Result.success()
    }
}