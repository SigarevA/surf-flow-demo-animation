/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.debug

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

private val DEBUG_NOTIFICATION_ID = "DEBUG_NOTIFICATION_ID".hashCode()

object DebugSettings {

    fun showNotification(context: Context) {
        val channelId = context.getString(R.string.debug_notification_channel_id)
        val notificationTitle = context.getString(R.string.debug_screen_title)
        val notificationBody = context.getString(R.string.debug_screen_body)

        val pendingIntent = PendingIntent.getActivity(
            context,
            DEBUG_NOTIFICATION_ID,
            Intent(context, DebugActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(notificationTitle)
            .setContentIntent(pendingIntent)

        notificationBuilder.setSmallIcon(R.drawable.debug_ic_debug)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    notificationTitle,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = notificationBody
                }
            )
        }

        notificationManager.notify(DEBUG_NOTIFICATION_ID, notificationBuilder.build())
    }
}