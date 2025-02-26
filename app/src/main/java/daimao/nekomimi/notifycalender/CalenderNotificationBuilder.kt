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
    channel.setShowBadge(false)
    NotificationManagerCompat.from(context).createNotificationChannel(channel)
}

@SuppressLint("MissingPermission")
fun postNotification(context: Context) {
    if (!PermissionHelper.hasNotifyPermission(context)) {
        return
    }

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(getNotificationIcon(LocalDate.now().dayOfMonth))
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

fun getNotificationIcon(day: Int): Int {
    return when (day) {
        1 -> R.drawable.notification_icon_01
        2 -> R.drawable.notification_icon_02
        3 -> R.drawable.notification_icon_03
        4 -> R.drawable.notification_icon_04
        5 -> R.drawable.notification_icon_05
        6 -> R.drawable.notification_icon_06
        7 -> R.drawable.notification_icon_07
        8 -> R.drawable.notification_icon_08
        9 -> R.drawable.notification_icon_09
        10 -> R.drawable.notification_icon_10
        11 -> R.drawable.notification_icon_11
        12 -> R.drawable.notification_icon_12
        13 -> R.drawable.notification_icon_13
        14 -> R.drawable.notification_icon_14
        15 -> R.drawable.notification_icon_15
        16 -> R.drawable.notification_icon_16
        17 -> R.drawable.notification_icon_17
        18 -> R.drawable.notification_icon_18
        19 -> R.drawable.notification_icon_19
        20 -> R.drawable.notification_icon_20
        21 -> R.drawable.notification_icon_21
        22 -> R.drawable.notification_icon_22
        23 -> R.drawable.notification_icon_23
        24 -> R.drawable.notification_icon_24
        25 -> R.drawable.notification_icon_25
        26 -> R.drawable.notification_icon_26
        27 -> R.drawable.notification_icon_27
        28 -> R.drawable.notification_icon_28
        29 -> R.drawable.notification_icon_29
        30 -> R.drawable.notification_icon_30
        31 -> R.drawable.notification_icon_31
        else -> R.drawable.notification_icon_01 // Default icon for invalid days
    }
}



