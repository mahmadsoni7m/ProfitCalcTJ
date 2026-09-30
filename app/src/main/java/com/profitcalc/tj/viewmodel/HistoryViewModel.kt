package com.profitcalc.tj.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.profitcalc.tj.data.local.HistoryEntity
import com.profitcalc.tj.data.repository.HistoryRepository
import com.profitcalc.tj.engine.CalculationResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {

    val history: StateFlow<List<HistoryEntity>> = repository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun delete(entity: HistoryEntity) = viewModelScope.launch { repository.delete(entity) }

    fun duplicate(entity: HistoryEntity) = viewModelScope.launch { repository.duplicate(entity) }

    fun clearAll() = viewModelScope.launch { repository.clearAll() }

    fun recalculate(entity: HistoryEntity): CalculationResult = repository.recalculate(entity)
}
