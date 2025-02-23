package daimao.nekomimi.notifycalender

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

object PermissionHelper {

    fun hasNotifyPermission(context: Context): Boolean {
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            return false;
        }
        val checkPermissionResult = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        )
        return checkPermissionResult == PackageManager.PERMISSION_GRANTED
    }

    fun willShowPermissionRequest(activity: Activity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.POST_NOTIFICATIONS)
    }

    fun openAppNotificationSettings(context: Context) {
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            return
        }
        val intent = Intent()
        intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        context.startActivity(intent)
    }

    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null
    private var permissionContinuation: CancellableContinuation<Boolean>? = null

    suspend fun requestPermission(activity: ComponentActivity): Boolean {
        requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            permissionContinuation?.resume(isGranted)
            permissionContinuation = null
        }

        try {
            return suspendCancellableCoroutine { continuation ->
                permissionContinuation = continuation
                run {
                    requestPermissionLauncher?.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                continuation.invokeOnCancellation {
                    permissionContinuation = null
                }
            }

        } finally {
            requestPermissionLauncher?.unregister()
            requestPermissionLauncher = null
        }


    }

}





