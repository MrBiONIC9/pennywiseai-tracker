package com.pennywiseai.tracker.util

import kotlinx.coroutines.delay
import kotlin.math.min
import kotlin.math.pow

class RetryManager {
    companion object {
        private const val MAX_RETRIES = 3
        private const val BASE_DELAY_MS = 1000L
        private const val MAX_DELAY_MS = 30000L
    }

    suspend fun <T> retryWithBackoff(
        maxAttempts: Int = MAX_RETRIES,
        block: suspend () -> T
    ): T {
        var currentDelay = BASE_DELAY_MS
        repeat(maxAttempts) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                if (attempt == maxAttempts - 1) throw e
                
                delay(currentDelay)
                currentDelay = calculateNextDelay(currentDelay)
            }
        }
        throw IllegalStateException("Should never reach here")
    }

    private fun calculateNextDelay(currentDelay: Long): Long {
        return min(currentDelay * 2, MAX_DELAY_MS)
    }
}