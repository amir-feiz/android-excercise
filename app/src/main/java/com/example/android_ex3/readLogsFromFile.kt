package com.example.android_ex3

import android.content.Context
import java.io.File

fun readLogsFromFile(context: Context): List<String> {
    val logFile = File(context.filesDir, "status_log.txt")
    if (!logFile.exists()) return emptyList()
    return logFile.readLines().reversed()
}