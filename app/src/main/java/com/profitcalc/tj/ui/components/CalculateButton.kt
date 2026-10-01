package com.profitcalc.tj.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.profitcalc.tj.i18n.LocalAppStrings

@Composable
fun CalculateButton(onClick: (() -> Unit)? = null, modifier: Modifier = Modifier) {
    val strings = LocalAppStrings.current
    val focusManager = LocalFocusManager.current
    Button(
        onClick = {
            focusManager.clearFocus(force = true)
            onClick?.invoke()
        },
        modifier = modifier.fillMaxWidth(),
    ) {
        Icon(Icons.Filled.Calculate, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
        Text(strings.actionCalculate)
    }
}
