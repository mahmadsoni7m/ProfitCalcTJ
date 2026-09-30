package com.profitcalc.tj.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.profitcalc.tj.ui.theme.BigNumberStyle
import com.profitcalc.tj.ui.theme.ProfitNegative
import com.profitcalc.tj.ui.theme.ProfitPositive

/** The single, large headline result card (e.g. "Profit per item: 85.00 сомонӣ"). */
@Composable
fun HeadlineResultCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    isPositive: Boolean? = null,
) {
    val valueColor = when (isPositive) {
        true -> ProfitPositive
        false -> ProfitNegative
        null -> MaterialTheme.colorScheme.onSurface
    }
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(label, style = MaterialTheme.typography.titleMedium)
            Text(value, style = BigNumberStyle, color = valueColor)
        }
    }
}

/** A small labeled stat, used on the Dashboard and in breakdown lists. */
@Composable
fun StatRow(label: String, value: String, modifier: Modifier = Modifier, valueColor: Color = Color.Unspecified) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyLarge, color = valueColor)
    }
}

/** Card used on the Dashboard for quick stats like "Today's profit". */
@Composable
fun DashboardStatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.titleLarge)
        }
    }
}

/** A generic titled section wrapper card, used to group inputs/results. */
@Composable
fun SectionCard(title: String, modifier: Modifier = Modifier, content: @Composable ColumnScopeAlias.() -> Unit) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            content()
        }
    }
}

typealias ColumnScopeAlias = androidx.compose.foundation.layout.ColumnScope
