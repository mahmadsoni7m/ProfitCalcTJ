package com.profitcalc.tj.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardType.Companion.Decimal
import androidx.compose.foundation.text.KeyboardOptions
import com.profitcalc.tj.util.Formatters

@Composable
fun NumberField(
    label: String,
    value: Double,
    onValueChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
    suffix: String? = null,
    isError: Boolean = false,
    supportingText: String? = null,
    resetSignal: Any = Unit,
) {
    var text by remember(resetSignal) {
        mutableStateOf(if (value == 0.0) "" else Formatters.money(value, 2).replace(",", ""))
    }

    OutlinedTextField(
        value = text,
        onValueChange = { new ->
            text = new
            onValueChange(Formatters.parseInput(new))
        },
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        isError = isError,
        supportingText = supportingText?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
        suffix = suffix?.let { { Text(it) } },
        keyboardOptions = KeyboardOptions(keyboardType = Decimal),
    )
}

/** Whole-number field for quantity-style inputs. Same live-typing fix as [NumberField]. */
@Composable
fun IntField(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: String? = null,
    resetSignal: Any = Unit,
) {
    var text by remember(resetSignal) { mutableStateOf(if (value == 0) "" else value.toString()) }

    OutlinedTextField(
        value = text,
        onValueChange = { new ->
            text = new
            onValueChange(new.filter { it.isDigit() }.toIntOrNull() ?: 0)
        },
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        isError = isError,
        supportingText = supportingText?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}
