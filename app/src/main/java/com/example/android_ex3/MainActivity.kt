// MainActivity.kt
package com.example.networkmonitor

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: NetworkViewModel
    private lateinit var textViewStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ایجاد رابط کاربری به صورت برنامه‌نویسی
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }

        textViewStatus = TextView(this).apply {
            text = "Checking network status..."
            textSize = 18f
        }

        layout.addView(textViewStatus)
        setContentView(layout)

        viewModel = ViewModelProvider(this).get(NetworkViewModel::class.java)
        viewModel.networkStatus.observe(this, Observer { status ->
            textViewStatus.text = status
        })

        Intent(this, NetworkMonitoringService::class.java).also { intent ->
            startService(intent)
        }
    }
}
