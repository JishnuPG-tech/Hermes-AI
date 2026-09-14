package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToChatSheet(
    webSearchEnabled: Boolean,
    onToggleWebSearch: (Boolean) -> Unit,
    memoryEnabled: Boolean,
    onToggleMemory: (Boolean) -> Unit,
    selectedProject: String,
    onSelectProjectClick: () -> Unit,
    onActionClick: (String) -> Unit,
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
                .padding(bottom = 36.dp)
        ) {
            // Header with Close and Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
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
                    text = "Add to chat",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    ),
                    color = HermesTextPrimary
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier.size(36.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3 Square Action Buttons: Camera, Photos, Files
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Camera
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1.15f)
                        .clip(RoundedCornerShape(18.dp))
                        .background(HermesSurfaceCard)
                        .clickable { onActionClick("Camera") }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = "Camera",
                            tint = HermesTextPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Camera",
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp),
                            color = HermesTextPrimary
                        )
                    }
                }

                // Photos
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1.15f)
                        .clip(RoundedCornerShape(18.dp))
                        .background(HermesSurfaceCard)
                        .clickable { onActionClick("Photos") }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Photos",
                            tint = HermesTextPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Photos",
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp),
                            color = HermesTextPrimary
                        )
                    }
                }

                // Files
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1.15f)
                        .clip(RoundedCornerShape(18.dp))
                        .background(HermesSurfaceCard)
                        .clickable { onActionClick("Files") }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.UploadFile,
                            contentDescription = "Files",
                            tint = HermesTextPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Files",
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp),
                            color = HermesTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Toggles container: Web search & Memory
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(HermesSurfaceCard)
            ) {
                // Web Search
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = "Web search",
                        tint = HermesTextSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Web search",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                        color = HermesTextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = webSearchEnabled,
                        onCheckedChange = onToggleWebSearch,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = HermesTextPrimary,
                            checkedTrackColor = HermesBlue,
                            uncheckedThumbColor = HermesTextMuted,
                            uncheckedTrackColor = HermesBorder
                        )
                    )
                }

                HorizontalDivider(color = HermesDivider, thickness = 1.dp)

                // Memory
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "Memory",
                        tint = HermesTextSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Memory",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                        color = HermesTextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = memoryEnabled,
                        onCheckedChange = onToggleMemory,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = HermesTextPrimary,
                            checkedTrackColor = HermesBlue,
                            uncheckedThumbColor = HermesTextMuted,
                            uncheckedTrackColor = HermesBorder
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Add to project row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(HermesSurfaceCard)
                    .clickable { onSelectProjectClick() }
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = "Add to project",
                    tint = HermesTextSecondary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Add to project",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                    color = HermesTextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = selectedProject,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    color = HermesTextSecondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = HermesTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
