package com.profitcalc.tj.i18n

import androidx.compose.runtime.staticCompositionLocalOf

enum class AppLanguage {
    TAJIK,
    RUSSIAN;

    companion object {
        fun fromCode(code: String): AppLanguage = when (code) {
            "ru" -> RUSSIAN
            else -> TAJIK
        }
    }

    val code: String
        get() = when (this) {
            TAJIK -> "tj"
            RUSSIAN -> "ru"
        }
}

fun stringsFor(language: AppLanguage): AppStrings = when (language) {
    AppLanguage.TAJIK -> TjStrings
    AppLanguage.RUSSIAN -> RuStrings
}

val LocalAppStrings = staticCompositionLocalOf { TjStrings }
