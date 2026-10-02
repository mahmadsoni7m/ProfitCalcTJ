package com.profitcalc.tj.viewmodel

import androidx.lifecycle.ViewModel
import com.profitcalc.tj.engine.CalculatorEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DiscountToolViewModel : ViewModel() {
    private val _originalPrice = MutableStateFlow(0.0)
    val originalPrice: StateFlow<Double> = _originalPrice.asStateFlow()

    private val _discountPercent = MutableStateFlow(0.0)
    val discountPercent: StateFlow<Double> = _discountPercent.asStateFlow()

    fun setOriginalPrice(v: Double) { _originalPrice.value = v }
    fun setDiscountPercent(v: Double) { _discountPercent.value = v }

    fun finalPrice(originalPrice: Double, discountPercent: Double): Double =
        CalculatorEngine.discountedPrice(originalPrice, discountPercent)
}
