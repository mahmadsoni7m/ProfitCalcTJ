package com.profitcalc.tj.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.profitcalc.tj.i18n.LocalAppStrings
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
    var showKeypad by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = text,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = isError,
            supportingText = supportingText?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
            suffix = suffix?.let { { Text(it) } },
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) { showKeypad = true },
        )
    }

    if (showKeypad) {
        NumericKeypadSheet(
            title = label,
            initialValue = text,
            allowDecimal = true,
            onValueChange = { new ->
                text = new
                onValueChange(Formatters.parseInput(new))
            },
            onDismiss = { showKeypad = false },
        )
    }
}

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
    var showKeypad by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = text,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = isError,
            supportingText = supportingText?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) { showKeypad = true },
        )
    }

    if (showKeypad) {
        NumericKeypadSheet(
            title = label,
            initialValue = text,
            allowDecimal = false,
            onValueChange = { new ->
                text = new
                onValueChange(new.filter { it.isDigit() }.toIntOrNull() ?: 0)
            },
            onDismiss = { showKeypad = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NumericKeypadSheet(
    title: String,
    initialValue: String,
    allowDecimal: Boolean,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val strings = LocalAppStrings.current
    var current by remember { mutableStateOf(initialValue) }
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(
                current.ifEmpty { "0" },
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
            )

            val rows = if (allowDecimal) {
                listOf(
                    listOf("7", "8", "9"),
                    listOf("4", "5", "6"),
                    listOf("1", "2", "3"),
                    listOf(".", "0", "⌫"),
                )
            } else {
                listOf(
                    listOf("7", "8", "9"),
                    listOf("4", "5", "6"),
                    listOf("1", "2", "3"),
                    listOf("", "0", "⌫"),
                )
            }

            rows.forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { key ->
                        if (key.isEmpty()) {
                            Box(modifier = Modifier.weight(1f))
                        } else {
                            OutlinedButton(
                                onClick = {
                                    current = when (key) {
                                        "⌫" -> current.dropLast(1)
                                        "." -> if (current.contains(".")) current else current.ifEmpty { "0" } + "."
                                        else -> if (current == "0") key else current + key
                                    }
                                    onValueChange(current)
                                },
                                modifier = Modifier.weight(1f).aspectRatio(1.6f),
                            ) {
                                Text(key, style = MaterialTheme.typography.titleLarge)
                            }
                        }
                    }
                }
            }

            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text(strings.actionConfirm)
            }
        }
    }
}
