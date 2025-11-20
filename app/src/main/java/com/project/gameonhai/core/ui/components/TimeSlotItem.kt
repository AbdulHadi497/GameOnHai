package com.project.gameonhai.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.gameonhai.core.model.TimeSlot
import com.project.gameonhai.core.model.TimeSlotStatus

/**
 * Reusable component for displaying time slots
 * Shows different states: Available, Booked, Unavailable, Past
 */
@Composable
fun TimeSlotItem(
    timeSlot: TimeSlot,
    status: TimeSlotStatus,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isClickable = status == TimeSlotStatus.AVAILABLE

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp)
                    )
                } else {
                    Modifier
                }
            )
            .clip(RoundedCornerShape(12.dp))
            .let {
                if (isClickable) it.clickable { onClick() } else it
            },

        shape = RoundedCornerShape(12.dp),

        colors = CardDefaults.elevatedCardColors(
            containerColor = when {
                isSelected -> MaterialTheme.colorScheme.primaryContainer
                status == TimeSlotStatus.AVAILABLE -> MaterialTheme.colorScheme.surface
                status == TimeSlotStatus.BOOKED -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            }
        ),

        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (isSelected) 6.dp else 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${timeSlot.startTime} - ${timeSlot.endTime}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
                        isClickable -> MaterialTheme.colorScheme.onSurface
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                StatusBadge(status = status)
            }

            if (isClickable || isSelected) {
                Text(
                    text = "Rs ${timeSlot.price.toInt()}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun StatusBadge(status: TimeSlotStatus) {
    val (text, color) = when (status) {
        TimeSlotStatus.AVAILABLE -> "Available" to MaterialTheme.colorScheme.primary
        TimeSlotStatus.BOOKED -> "Booked" to MaterialTheme.colorScheme.error
        TimeSlotStatus.UNAVAILABLE -> "Unavailable" to MaterialTheme.colorScheme.outline
        TimeSlotStatus.PAST -> "Past" to MaterialTheme.colorScheme.outline
    }

    Surface(
        color = color.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.padding(0.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}