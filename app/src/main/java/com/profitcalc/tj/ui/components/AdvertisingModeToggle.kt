package com.profitcalc.tj.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.profitcalc.tj.engine.AdvertisingMode

@Composable
fun AdvertisingModeToggle(
    mode: AdvertisingMode,
    onModeChange: (AdvertisingMode) -> Unit,
    totalLabel: String,
    perItemLabel: String,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
        SegmentedButton(
            selected = mode == AdvertisingMode.TOTAL_BUDGET,
            onClick = { onModeChange(AdvertisingMode.TOTAL_BUDGET) },
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
        ) { Text(totalLabel) }
        SegmentedButton(
            selected = mode == AdvertisingMode.PER_ITEM,
            onClick = { onModeChange(AdvertisingMode.PER_ITEM) },
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
        ) { Text(perItemLabel) }
    }
}
