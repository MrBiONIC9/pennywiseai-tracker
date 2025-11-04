package com.pennywiseai.tracker.util

import android.app.ActivityManager
import android.content.Context
import javax.inject.Inject

class DeviceCapabilities @Inject constructor(
    private val context: Context
) {
    fun getAvailableProcessors(): Int = 
        Runtime.getRuntime().availableProcessors()

    fun getAvailableMemoryMb(): Long {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.availMem / (1024 * 1024) // Convert to MB
    }

    fun calculateOptimalBatchSize(): Int {
        val processors = getAvailableProcessors()
        val availableMemoryMb = getAvailableMemoryMb()

        return when {
            availableMemoryMb > 1024 && processors >= 4 -> 1000
            availableMemoryMb > 512 && processors >= 2 -> 500
            else -> 250
        }
    }
}