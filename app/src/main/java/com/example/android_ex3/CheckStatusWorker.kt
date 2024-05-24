package com.example.android_ex3

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class CheckStatusWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val logFileName = "status_log.txt"

    override fun doWork(): Result {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        val isBluetoothEnabled = bluetoothAdapter?.isEnabled == true

        val isAirplaneModeOn = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.System.getInt(
                applicationContext.contentResolver,
                Settings.System.AIRPLANE_MODE_ON, 0
            ) != 0
        } else {
            Settings.Global.getInt(
                applicationContext.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON, 0
            ) != 0
        }

        val logMessage = "Bluetooth Enabled: $isBluetoothEnabled, Airplane Mode: $isAirplaneModeOn"
        Log.i("airplane_worker", logMessage)
        writeLogToFile(logMessage)

        return Result.success()
    }

    private fun writeLogToFile(logMessage: String) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val message = "$timestamp - $logMessage\n"
        val file = File(applicationContext.filesDir, logFileName)
        FileOutputStream(file, true).use {
            it.write(message.toByteArray())
        }
    }
}
