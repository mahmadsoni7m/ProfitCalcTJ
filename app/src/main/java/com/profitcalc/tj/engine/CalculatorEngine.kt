package com.profitcalc.tj.engine

import kotlin.math.max

/**
 * Central, pure calculation engine for ProfitCalc TJ.
 *
 * Every function here is a deterministic pure function of its inputs — no
 * Android framework classes, no I/O, no mutable state. This keeps it trivially
 * unit-testable (see CalculatorEngineTest) and keeps UI/ViewModel code free of
 * business-math duplication, per the required layering:
 *
 *   UI -> ViewModel -> CalculatorEngine -> Repository -> Local Storage
 */
object CalculatorEngine {

    /** Guards every division in the engine so a zero denominator never throws. */
    private fun safeDivide(numerator: Double, denominator: Double): Double =
        if (denominator == 0.0) 0.0 else numerator / denominator

    /**
     * Validates raw input and returns the list of problems found. Errors block
     * calculation (the caller should show the calculation as unavailable);
     * warnings do not block it, they only inform the user.
     */
    fun validate(input: CalculationInput): List<ValidationIssue> {
        val issues = mutableListOf<ValidationIssue>()

        if (input.purchasePrice < 0) issues += ValidationIssue(ValidationField.PURCHASE_PRICE, ValidationSeverity.ERROR)
        if (input.salePrice < 0) issues += ValidationIssue(ValidationField.SALE_PRICE, ValidationSeverity.ERROR)
        if (input.logistics < 0) issues += ValidationIssue(ValidationField.LOGISTICS, ValidationSeverity.ERROR)
        if (input.packaging < 0) issues += ValidationIssue(ValidationField.PACKAGING, ValidationSeverity.ERROR)
        if (input.advertisingValue < 0) issues += ValidationIssue(ValidationField.ADVERTISING, ValidationSeverity.ERROR)
        if (input.otherCosts < 0) issues += ValidationIssue(ValidationField.OTHER_COSTS, ValidationSeverity.ERROR)

        if (input.quantity < 1) issues += ValidationIssue(ValidationField.QUANTITY, ValidationSeverity.ERROR)

        if (input.commissionPercent < 0) {
            issues += ValidationIssue(ValidationField.COMMISSION, ValidationSeverity.ERROR)
        } else if (input.commissionPercent > 100) {
            issues += ValidationIssue(ValidationField.COMMISSION, ValidationSeverity.WARNING)
        }

        if (input.discountPercent < 0) {
            issues += ValidationIssue(ValidationField.DISCOUNT, ValidationSeverity.ERROR)
        } else if (input.discountPercent > 100) {
            issues += ValidationIssue(ValidationField.DISCOUNT, ValidationSeverity.WARNING)
        }

        if (input.taxPercent < 0) issues += ValidationIssue(ValidationField.TAX, ValidationSeverity.ERROR)

        return issues
    }

    fun hasBlockingErrors(issues: List<ValidationIssue>): Boolean =
        issues.any { it.severity == ValidationSeverity.ERROR }

    /** Advertising cost attributed to a single unit, for either input mode. */
    fun advertisingPerItem(input: CalculationInput): Double = when (input.advertisingMode) {
        AdvertisingMode.PER_ITEM -> input.advertisingValue
        AdvertisingMode.TOTAL_BUDGET -> safeDivide(input.advertisingValue, input.quantity.toDouble())
    }

    fun calculate(input: CalculationInput): CalculationResult {
        val discountAmount = input.salePrice * (input.discountPercent / 100.0)
        val finalSalePrice = input.salePrice - discountAmount

        val commissionAmount = finalSalePrice * (input.commissionPercent / 100.0)
        val taxAmount = finalSalePrice * (input.taxPercent / 100.0)
        val adPerItem = advertisingPerItem(input)

        val totalCostPerItem = input.purchasePrice + input.logistics + input.packaging +
            adPerItem + input.otherCosts + taxAmount

        val profitPerItem = finalSalePrice - commissionAmount - input.purchasePrice -
            input.logistics - input.packaging - adPerItem - input.otherCosts - taxAmount

        val quantity = input.quantity.toDouble()
        val totalProfit = profitPerItem * quantity
        val revenue = finalSalePrice * quantity
        val totalCommission = commissionAmount * quantity
        val totalCost = totalCostPerItem * quantity

        val marginPercent = safeDivide(profitPerItem, finalSalePrice) * 100.0
        val roiPercent = safeDivide(profitPerItem, totalCostPerItem) * 100.0

        return CalculationResult(
            discountAmount = discountAmount,
            finalSalePrice = finalSalePrice,
            commissionAmount = commissionAmount,
            taxAmount = taxAmount,
            advertisingPerItem = adPerItem,
            totalCostPerItem = totalCostPerItem,
            profitPerItem = profitPerItem,
            totalProfit = totalProfit,
            revenue = revenue,
            totalCommission = totalCommission,
            totalCost = totalCost,
            marginPercent = marginPercent,
            roiPercent = roiPercent,
        )
    }

    /**
     * Required original sale price (before discount) to hit [targetProfitPerItem].
     * Passing 0.0 computes the break-even price. Shared by [breakEven] and
     * [targetProfit].
     *
     * Derivation (F = final sale price after discount d, c = commission fraction,
     * t = tax fraction, Fixed = purchase+logistics+packaging+ad+other):
     *   profit = F*(1-c-t) - Fixed = target
     *   F = (target + Fixed) / (1-c-t)
     *   S = F / (1-d)
     */
    private fun requiredSalePriceFor(input: CalculationInput, targetProfitPerItem: Double): TargetProfitResult {
        val c = input.commissionPercent / 100.0
        val t = input.taxPercent / 100.0
        val d = input.discountPercent / 100.0
        val adPerItem = advertisingPerItem(input)
        val fixed = input.purchasePrice + input.logistics + input.packaging + adPerItem + input.otherCosts

        val marginFactor = 1.0 - c - t
        val discountFactor = 1.0 - d

        if (marginFactor <= 0.0 || discountFactor <= 0.0) {
            return TargetProfitResult(achievable = false, requiredSalePrice = 0.0)
        }

        val finalPriceNeeded = (targetProfitPerItem + fixed) / marginFactor
        val saleNeeded = finalPriceNeeded / discountFactor

        return TargetProfitResult(achievable = true, requiredSalePrice = max(0.0, saleNeeded))
    }

    fun breakEven(input: CalculationInput): BreakEvenResult {
        val required = requiredSalePriceFor(input, targetProfitPerItem = 0.0)
        val currentProfit = calculate(input).profitPerItem
        return BreakEvenResult(
            achievable = required.achievable,
            minSalePrice = required.requiredSalePrice,
            profitAtCurrentPrice = currentProfit,
        )
    }

    /** [totalTargetProfit] is the desired TOTAL profit across the whole quantity. */
    fun targetProfit(input: CalculationInput, totalTargetProfit: Double): TargetProfitResult {
        val perItemTarget = safeDivide(totalTargetProfit, input.quantity.toDouble())
        return requiredSalePriceFor(input, perItemTarget)
    }

    /**
     * Maximum discount percentage that can be applied to [salePrice] while still
     * keeping per-item profit at or above [minDesiredProfit], given commission
     * and fixed per-item costs.
     *
     *   F*(1-c) = minProfit + Fixed  =>  F = (minProfit+Fixed)/(1-c)
     *   discount = 1 - F/S
     */
    fun maxDiscount(
        salePrice: Double,
        purchasePrice: Double,
        logistics: Double,
        packaging: Double,
        commissionPercent: Double,
        minDesiredProfit: Double,
    ): MaxDiscountResult {
        val c = commissionPercent / 100.0
        val marginFactor = 1.0 - c
        if (marginFactor <= 0.0 || salePrice <= 0.0) {
            return MaxDiscountResult(achievable = false, maxDiscountPercent = 0.0)
        }

        val fixed = purchasePrice + logistics + packaging
        val requiredFinalPrice = (minDesiredProfit + fixed) / marginFactor
        val discountFraction = 1.0 - (requiredFinalPrice / salePrice)

        return if (discountFraction < 0.0) {
            MaxDiscountResult(achievable = false, maxDiscountPercent = 0.0)
        } else {
            MaxDiscountResult(achievable = true, maxDiscountPercent = discountFraction * 100.0)
        }
    }

    fun roas(advertisingSpend: Double, revenue: Double): RoasResult {
        if (advertisingSpend <= 0.0) return RoasResult(achievable = false, roas = 0.0)
        return RoasResult(achievable = true, roas = revenue / advertisingSpend)
    }

    fun discountedPrice(originalPrice: Double, discountPercent: Double): Double =
        originalPrice - (originalPrice * (discountPercent / 100.0))
}
