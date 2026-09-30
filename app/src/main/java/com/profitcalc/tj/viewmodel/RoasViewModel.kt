package com.profitcalc.tj.viewmodel

import androidx.lifecycle.ViewModel
import com.profitcalc.tj.engine.CalculatorEngine
import com.profitcalc.tj.engine.RoasResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RoasViewModel : ViewModel() {
    private val _spend = MutableStateFlow(0.0)
    val spend: StateFlow<Double> = _spend.asStateFlow()

    private val _revenue = MutableStateFlow(0.0)
    val revenue: StateFlow<Double> = _revenue.asStateFlow()

    fun setSpend(v: Double) { _spend.value = v }
    fun setRevenue(v: Double) { _revenue.value = v }

    fun result(): RoasResult = CalculatorEngine.roas(_spend.value, _revenue.value)
}
