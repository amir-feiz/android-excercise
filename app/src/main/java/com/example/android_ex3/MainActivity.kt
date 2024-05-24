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
import com.example.android_ex3.readLogsFromFile
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: NetworkViewModel
    private lateinit var textViewStatus: TextView
    private lateinit var logTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the UI programmatically
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }

        textViewStatus = TextView(this).apply {
            text = "Checking network status..."
            textSize = 18f
        }

        layout.addView(textViewStatus)

        logTextView = TextView(this).apply {
            text = "Logs:\n"
            textSize = 16f
        }

        layout.addView(logTextView)

        setContentView(layout)

        // Initialize ViewModel and observe network status
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NetworkViewModel::class.java)
        viewModel.networkStatus.observe(this, Observer { status ->
            textViewStatus.text = status
        })

        // Start NetworkMonitoringService
        Intent(this, NetworkMonitoringService::class.java).also { intent ->
            startService(intent)
        }

        // Schedule periodic work request
        val checkStatusWork = PeriodicWorkRequestBuilder<CheckStatusWorker>(2, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "CheckStatusWork",
            ExistingPeriodicWorkPolicy.KEEP,
            checkStatusWork
        )

        // Display logs
        displayLogs()
    }

    private fun displayLogs() {
        val logs = readLogsFromFile(this)
        val formattedLogs = logs.joinToString("\n")
        logTextView.text = "Logs:\n$formattedLogs"
    }
}
