package daimao.nekomimi.notifycalender

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Resources
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.ConfigurationCompat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val CHANNEL_ID: String = "CHANNEL_ID_DATE";
private const val notificationId = 0

fun createNotificationChannel(context: Context): Unit {
    val name = context.getString(R.string.channel_name)
    val descriptionText = context.getString(R.string.channel_description)
    val importance = NotificationManager.IMPORTANCE_LOW
    val channel = NotificationChannel(CHANNEL_ID, name, importance)
    channel.description = descriptionText
    NotificationManagerCompat.from(context).createNotificationChannel(channel)
}

@SuppressLint("MissingPermission")
fun postNotification(context: Context) {
    if (!PermissionHelper.hasNotifyPermission(context)) {
        return
    }
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(formatDate())
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(false)
        .setOngoing(true)
        .setAllowSystemGeneratedContextualActions(false)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setDeleteIntent(DateChangeReceiver.createPendingIntent(context))
    NotificationManagerCompat.from(context).notify(notificationId, builder.build())

    DateChangeReceiver.setAlarm(context)
}

fun cancelNotification(context: Context): Unit {
    if (!PermissionHelper.hasNotifyPermission(context)) {
        return
    }
    NotificationManagerCompat.from(context).cancel(notificationId)
}

fun formatDate(): String {
    val locales = ConfigurationCompat.getLocales(Resources.getSystem().configuration)
    val locale = locales.get(0)
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd : E", locale)
    return LocalDate.now().format(formatter)
}




