package com.profitcalc.tj.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.profitcalc.tj.i18n.AppLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

enum class ThemeMode { LIGHT, DARK, SYSTEM }

data class AppSettings(
    val language: AppLanguage = AppLanguage.TAJIK,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val currencySymbol: String = "TJS",
    val decimalPlaces: Int = 2,
)

/**
 * Wraps Jetpack DataStore (local file, no network involved) for app settings.
 * All reads are exposed as Flow so Compose screens recompose instantly on change.
 */
class SettingsPrefs(private val context: Context) {

    private object Keys {
        val LANGUAGE = stringPreferencesKey("language")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val CURRENCY = stringPreferencesKey("currency")
        val DECIMALS = intPreferencesKey("decimal_places")
        val ONBOARDED = booleanPreferencesKey("onboarded")
    }

    val settingsFlow: Flow<AppSettings> = context.dataStore.data.map { prefs ->
        AppSettings(
            language = AppLanguage.fromCode(prefs[Keys.LANGUAGE] ?: "tj"),
            themeMode = runCatching { ThemeMode.valueOf(prefs[Keys.THEME_MODE] ?: ThemeMode.SYSTEM.name) }
                .getOrDefault(ThemeMode.SYSTEM),
            currencySymbol = prefs[Keys.CURRENCY] ?: "TJS",
            decimalPlaces = prefs[Keys.DECIMALS] ?: 2,
        )
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { it[Keys.LANGUAGE] = language.code }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun setCurrencySymbol(symbol: String) {
        context.dataStore.edit { it[Keys.CURRENCY] = symbol }
    }

    suspend fun setDecimalPlaces(places: Int) {
        context.dataStore.edit { it[Keys.DECIMALS] = places.coerceIn(0, 4) }
    }
}
