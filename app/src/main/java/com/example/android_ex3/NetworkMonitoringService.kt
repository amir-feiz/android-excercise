package com.example.networkmonitor

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.android_ex3.R

class NetworkMonitoringService : Service() {

    private val networkReceiver = NetworkReceiver()

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filter)
        createNotificationChannel()
        startForeground(1, getNotification("Checking network status..."))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "NETWORK_STATUS_CHANNEL",
                "Network Status Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    private fun getNotification(contentText: String): Notification {
        return NotificationCompat.Builder(this, "NETWORK_STATUS_CHANNEL")
            .setContentTitle("Network Status")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_network_status) // آیکون کوچک معتبر را اینجا تنظیم کنید
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

    inner class NetworkReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

            val isConnected = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
            val notification = getNotification(if (isConnected) "Connected to the internet" else "Disconnected from the internet")
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.notify(1, notification)
        }
    }
}
