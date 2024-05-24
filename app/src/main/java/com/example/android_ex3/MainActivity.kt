// MainActivity.kt
package com.example.networkmonitor

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.android_ex3.CheckStatusWorker
import com.example.android_ex3.R
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val checkStatusWork = PeriodicWorkRequestBuilder<CheckStatusWorker>(2, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "CheckStatusWork",
            ExistingPeriodicWorkPolicy.KEEP,
            checkStatusWork
        )
    }
}
