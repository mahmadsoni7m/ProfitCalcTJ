package com.profitcalc.tj.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.profitcalc.tj.data.local.SavedProductEntity
import com.profitcalc.tj.data.repository.SavedProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SavedProductsViewModel(private val repository: SavedProductRepository) : ViewModel() {

    val products: StateFlow<List<SavedProductEntity>> = repository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun delete(entity: SavedProductEntity) = viewModelScope.launch { repository.delete(entity) }
}
