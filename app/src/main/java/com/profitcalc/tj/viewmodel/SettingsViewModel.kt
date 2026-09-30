package com.profitcalc.tj.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.profitcalc.tj.data.prefs.AppSettings
import com.profitcalc.tj.data.prefs.SettingsPrefs
import com.profitcalc.tj.data.prefs.ThemeMode
import com.profitcalc.tj.i18n.AppLanguage
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val prefs: SettingsPrefs) : ViewModel() {

    val settings: StateFlow<AppSettings> = prefs.settingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettings())

    fun setLanguage(language: AppLanguage) = viewModelScope.launch { prefs.setLanguage(language) }
    fun setThemeMode(mode: ThemeMode) = viewModelScope.launch { prefs.setThemeMode(mode) }
    fun setCurrencySymbol(symbol: String) = viewModelScope.launch { prefs.setCurrencySymbol(symbol) }
    fun setDecimalPlaces(places: Int) = viewModelScope.launch { prefs.setDecimalPlaces(places) }
}
