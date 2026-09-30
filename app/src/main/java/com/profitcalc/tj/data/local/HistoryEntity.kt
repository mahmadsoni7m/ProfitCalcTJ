package com.profitcalc.tj.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One completed calculation, stored entirely on-device. Stores both the raw
 * inputs (so "Edit" / "Recalculate" can restore the form) and the computed
 * headline results (so History list rendering never needs to re-run math).
 */
@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productName: String,
    val timestamp: Long,

    // Inputs
    val purchasePrice: Double,
    val salePrice: Double,
    val quantity: Int,
    val commissionPercent: Double,
    val logistics: Double,
    val packaging: Double,
    val advertisingModeIsTotal: Boolean,
    val advertisingValue: Double,
    val discountPercent: Double,
    val taxPercent: Double,
    val otherCosts: Double,

    // Headline results (denormalized for fast list display)
    val profitPerItem: Double,
    val totalProfit: Double,
    val marginPercent: Double,
)
