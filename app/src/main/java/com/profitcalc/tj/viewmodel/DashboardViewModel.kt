package com.profitcalc.tj.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.profitcalc.tj.data.repository.HistoryRepository
import com.profitcalc.tj.data.repository.SavedProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class DashboardState(
    val todayProfit: Double = 0.0,
    val todayCalculations: Int = 0,
    val savedProductsCount: Int = 0,
    val averageMargin: Double = 0.0,
    val hasData: Boolean = false,
)

class DashboardViewModel(
    historyRepository: HistoryRepository,
    savedProductRepository: SavedProductRepository,
) : ViewModel() {

    val state: StateFlow<DashboardState> = combine(
        historyRepository.observeToday(),
        savedProductRepository.observeAll(),
    ) { todayHistory, products ->
        DashboardState(
            todayProfit = todayHistory.sumOf { it.totalProfit },
            todayCalculations = todayHistory.size,
            savedProductsCount = products.size,
            averageMargin = if (todayHistory.isEmpty()) 0.0 else todayHistory.map { it.marginPercent }.average(),
            hasData = todayHistory.isNotEmpty() || products.isNotEmpty(),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardState())
}
