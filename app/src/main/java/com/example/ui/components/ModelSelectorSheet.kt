package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class ModelOption(
    val id: String,
    val name: String,
    val tag: String? = null,
    val description: String
)

val AvailableModels = listOf(
    ModelOption(
        id = "fable-5.1",
        name = "Fable 5.1",
        tag = "Pro or Max",
        description = "For your toughest challenges"
    ),
    ModelOption(
        id = "opus-5",
        name = "Opus 5",
        tag = "Pro",
        description = "For complex tasks"
    ),
    ModelOption(
        id = "sonnet-5",
        name = "Sonnet 5",
        tag = null,
        description = "Most efficient for everyday tasks"
    ),
    ModelOption(
        id = "haiku-4.5",
        name = "Haiku 4.5",
        tag = null,
        description = "Fastest for quick answers"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelSelectorSheet(
    selectedModel: String,
    onModelSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = HermesSurfaceElevated,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 4.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(HermesBorder)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header with Close button and title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = HermesTextSecondary
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Select model",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    ),
                    color = HermesTextPrimary
                )
                Spacer(modifier = Modifier.weight(1f))
                // Placeholder to balance the row
                Box(modifier = Modifier.size(36.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Card list of models
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(HermesSurfaceCard)
            ) {
                AvailableModels.forEachIndexed { index, model ->
                    val isSelected = selectedModel.startsWith(model.name)
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onModelSelected(model.name)
                                onDismiss()
                            }
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = model.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 17.sp
                                    ),
                                    color = if (isSelected) HermesBlue else HermesTextPrimary
                                )
                                if (model.tag != null) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(HermesBadgePro)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = model.tag,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium
                                            ),
                                            color = HermesBadgeProText
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = model.description,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                                color = if (isSelected) HermesBlue.copy(alpha = 0.85f) else HermesTextSecondary
                            )
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = HermesBlue,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    if (index < AvailableModels.size - 1) {
                        HorizontalDivider(
                            color = HermesDivider,
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}
