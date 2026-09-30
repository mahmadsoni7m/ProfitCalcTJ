package com.profitcalc.tj.engine

enum class AdvertisingMode {
    TOTAL_BUDGET,
    PER_ITEM,
}

/**
 * All raw inputs for the main product-profit calculation.
 * Every numeric field defaults to 0.0 so a partially-filled form never crashes.
 */
data class CalculationInput(
    val productName: String = "",
    val purchasePrice: Double = 0.0,
    val salePrice: Double = 0.0,
    val quantity: Int = 1,
    val commissionPercent: Double = 0.0,
    val logistics: Double = 0.0,
    val packaging: Double = 0.0,
    val advertisingMode: AdvertisingMode = AdvertisingMode.PER_ITEM,
    val advertisingValue: Double = 0.0,
    val discountPercent: Double = 0.0,
    val taxPercent: Double = 0.0,
    val otherCosts: Double = 0.0,
)

data class CalculationResult(
    val discountAmount: Double,
    val finalSalePrice: Double,
    val commissionAmount: Double,
    val taxAmount: Double,
    val advertisingPerItem: Double,
    val totalCostPerItem: Double,
    val profitPerItem: Double,
    val totalProfit: Double,
    val revenue: Double,
    val totalCommission: Double,
    val totalCost: Double,
    val marginPercent: Double,
    val roiPercent: Double,
)

data class BreakEvenResult(
    val achievable: Boolean,
    val minSalePrice: Double,
    val profitAtCurrentPrice: Double,
)

data class TargetProfitResult(
    val achievable: Boolean,
    val requiredSalePrice: Double,
)

data class MaxDiscountResult(
    val achievable: Boolean,
    val maxDiscountPercent: Double,
)

data class RoasResult(
    val achievable: Boolean,
    val roas: Double,
)

/** Simple validation problem attached to a specific logical field. */
enum class ValidationField {
    PURCHASE_PRICE, SALE_PRICE, QUANTITY, COMMISSION, DISCOUNT, TAX, LOGISTICS,
    PACKAGING, ADVERTISING, OTHER_COSTS,
}

enum class ValidationSeverity { ERROR, WARNING }

data class ValidationIssue(
    val field: ValidationField,
    val severity: ValidationSeverity,
)
