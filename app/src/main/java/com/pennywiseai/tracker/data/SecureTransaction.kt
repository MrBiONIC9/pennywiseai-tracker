package com.pennywiseai.tracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "secure_transactions")
data class SecureTransaction(
    @PrimaryKey val id: String,
    val encryptedAmount: EncryptedData,
    val encryptedMerchant: EncryptedData,
    val encryptedReference: EncryptedData?,
    val transactionType: TransactionType, // Non-sensitive, used for filtering
    val timestamp: Instant, // Non-sensitive, used for sorting
    val bankName: String, // Non-sensitive, used for filtering
    val createdAt: Instant = Instant.now()
)