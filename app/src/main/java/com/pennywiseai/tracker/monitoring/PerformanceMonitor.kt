package com.pennywiseai.tracker.monitoring

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PerformanceMonitor @Inject constructor() {
    private val _metrics = MutableStateFlow(PerformanceMetrics())
    val metrics: StateFlow<PerformanceMetrics> = _metrics.asStateFlow()

    fun recordOperation(
        operationType: OperationType,
        durationMs: Long,
        success: Boolean
    ) {
        _metrics.value = _metrics.value.copy(
            operations = _metrics.value.operations + Operation(
                type = operationType,
                durationMs = durationMs,
                success = success,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    fun getAverageOperationTime(type: OperationType): Double {
        return _metrics.value.operations
            .filter { it.type == type }
            .map { it.durationMs }
            .average()
    }

    fun getSuccessRate(type: OperationType): Double {
        val operations = _metrics.value.operations.filter { it.type == type }
        if (operations.isEmpty()) return 0.0
        
        return operations.count { it.success }.toDouble() / operations.size
    }
}

data class PerformanceMetrics(
    val operations: List<Operation> = emptyList()
)

data class Operation(
    val type: OperationType,
    val durationMs: Long,
    val success: Boolean,
    val timestamp: Long
)

enum class OperationType {
    SMS_PARSING,
    TRANSACTION_SAVING,
    ENCRYPTION,
    DECRYPTION
}