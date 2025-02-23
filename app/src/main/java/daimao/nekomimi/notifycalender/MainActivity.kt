package daimao.nekomimi.notifycalender

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import daimao.nekomimi.notifycalender.ui.theme.NotifyCalenderTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App(this)
        }

        createNotificationChannel(applicationContext)
        PermissionHelper.register(this)
    }
}

@Composable
fun App(activity: ComponentActivity) {
    return NotifyCalenderTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Permissions(activity, innerPadding)
                ButtonPostNotification()
            }
        }
    }
}

@Preview
@Composable
fun ButtonPostNotification() {
    val context = LocalContext.current
    Button(onClick = {
        postNotification(context)
    }) {
        Text("Post Notification")
    }
}

@Composable
fun Permissions(activity: ComponentActivity, innerPadding: PaddingValues) {
    val permissionState = remember { mutableStateOf(PermissionHelper.hasNotifyPermission(activity)) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val lifecycleObserver = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                permissionState.value = PermissionHelper.hasNotifyPermission(activity)
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = permissionState.value.toString(),
            Modifier.padding(innerPadding)
        )
        if (permissionState.value) {
            Text("permission verified")
        } else {
            Button(
                onClick = {
                    if (PermissionHelper.willShowPermissionRequest(activity)) {
                        PermissionHelper.openAppNotificationSettings(activity)
                        return@Button
                    }
                    scope.launch {
                        permissionState.value = PermissionHelper.requestPermission()
                    }
                }, modifier = Modifier.padding(innerPadding)
            ) { Text("Request Permission") }
        }
    }

}

