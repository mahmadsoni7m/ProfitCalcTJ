package com.profitcalc.tj.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** A reusable product template (e.g. "iPhone Case") with its default cost parameters. */
@Entity(tableName = "saved_products")
data class SavedProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val purchasePrice: Double,
    val defaultSalePrice: Double,
    val commissionPercent: Double,
    val logistics: Double,
    val packaging: Double,
    val advertisingModeIsTotal: Boolean,
    val advertisingValue: Double,
    val taxPercent: Double,
    val otherCosts: Double,
)
