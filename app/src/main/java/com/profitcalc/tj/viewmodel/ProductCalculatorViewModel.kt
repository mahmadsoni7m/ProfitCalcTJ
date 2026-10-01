package com.profitcalc.tj.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.profitcalc.tj.data.local.SavedProductEntity
import com.profitcalc.tj.data.repository.HistoryRepository
import com.profitcalc.tj.data.repository.SavedProductRepository
import com.profitcalc.tj.engine.AdvertisingMode
import com.profitcalc.tj.engine.BreakEvenResult
import com.profitcalc.tj.engine.CalculationInput
import com.profitcalc.tj.engine.CalculationResult
import com.profitcalc.tj.engine.CalculatorEngine
import com.profitcalc.tj.engine.MaxDiscountResult
import com.profitcalc.tj.engine.TargetProfitResult
import com.profitcalc.tj.engine.ValidationIssue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductCalculatorViewModel(
    private val historyRepository: HistoryRepository,
    private val savedProductRepository: SavedProductRepository,
) : ViewModel() {

    private val _input = MutableStateFlow(CalculationInput())
    val input: StateFlow<CalculationInput> = _input.asStateFlow()

    private val _targetProfitTotal = MutableStateFlow(0.0)
    val targetProfitTotal: StateFlow<Double> = _targetProfitTotal.asStateFlow()

    private val _minDesiredProfitForDiscount = MutableStateFlow(0.0)
    val minDesiredProfitForDiscount: StateFlow<Double> = _minDesiredProfitForDiscount.asStateFlow()

    private val _lastSavedMessage = MutableStateFlow<String?>(null)
    val lastSavedMessage: StateFlow<String?> = _lastSavedMessage.asStateFlow()

    private val _formVersion = MutableStateFlow(0)
    val formVersion: StateFlow<Int> = _formVersion.asStateFlow()

    fun currentValidation(): List<ValidationIssue> = CalculatorEngine.validate(_input.value)

    fun currentResult(): CalculationResult = CalculatorEngine.calculate(_input.value)

    fun currentBreakEven(): BreakEvenResult = CalculatorEngine.breakEven(_input.value)

    fun currentTargetProfit(): TargetProfitResult =
        CalculatorEngine.targetProfit(_input.value, _targetProfitTotal.value)

    fun currentMaxDiscount(): MaxDiscountResult = CalculatorEngine.maxDiscount(
        salePrice = _input.value.salePrice,
        purchasePrice = _input.value.purchasePrice,
        logistics = _input.value.logistics,
        packaging = _input.value.packaging,
        commissionPercent = _input.value.commissionPercent,
        minDesiredProfit = _minDesiredProfitForDiscount.value,
    )

    fun update(transform: (CalculationInput) -> CalculationInput) {
        _input.value = transform(_input.value)
    }

    fun setProductName(v: String) = update { it.copy(productName = v) }
    fun setPurchasePrice(v: Double) = update { it.copy(purchasePrice = v) }
    fun setSalePrice(v: Double) = update { it.copy(salePrice = v) }
    fun setQuantity(v: Int) = update { it.copy(quantity = v) }
    fun setCommissionPercent(v: Double) = update { it.copy(commissionPercent = v) }
    fun setLogistics(v: Double) = update { it.copy(logistics = v) }
    fun setPackaging(v: Double) = update { it.copy(packaging = v) }
    fun setAdvertisingMode(v: AdvertisingMode) = update { it.copy(advertisingMode = v) }
    fun setAdvertisingValue(v: Double) = update { it.copy(advertisingValue = v) }
    fun setDiscountPercent(v: Double) = update { it.copy(discountPercent = v) }
    fun setTaxPercent(v: Double) = update { it.copy(taxPercent = v) }
    fun setOtherCosts(v: Double) = update { it.copy(otherCosts = v) }
    fun setTargetProfitTotal(v: Double) { _targetProfitTotal.value = v }
    fun setMinDesiredProfitForDiscount(v: Double) { _minDesiredProfitForDiscount.value = v }

    fun loadFromSavedProduct(entity: SavedProductEntity) {
        _input.value = savedProductRepository.toCalculationInput(entity)
        _formVersion.value += 1
    }

    fun saveToHistory() {
        viewModelScope.launch {
            historyRepository.save(_input.value, currentResult())
            _lastSavedMessage.value = "history"
        }
    }

    fun saveAsProduct() {
        viewModelScope.launch {
            savedProductRepository.save(_input.value)
            _lastSavedMessage.value = "product"
        }
    }

    fun consumeSavedMessage() {
        _lastSavedMessage.value = null
    }

    fun reset() {
        _input.value = CalculationInput()
        _formVersion.value += 1
    }
}
