package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun HermesComposer(
    text: String,
    onTextChange: (String) -> Unit,
    selectedModel: String,
    onOpenModelSelector: () -> Unit,
    onOpenAddToChat: () -> Unit,
    onSend: () -> Unit,
    onStartVoiceMode: () -> Unit,
    attachments: List<String> = emptyList(),
    onRemoveAttachment: (String) -> Unit = {},
    showUpgradeBanner: Boolean = true,
    onUpgradeClick: () -> Unit = {},
    voiceEndedDuration: String? = null,
    onDismissVoiceEnded: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("hermes_composer_card"),
        shape = RoundedCornerShape(26.dp),
        color = HermesSurfaceElevated,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Voice Ended Banner (if voice mode recently finished, matching screenshot 4)
            if (voiceEndedDuration != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(HermesSurfaceCard)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = null,
                        tint = HermesTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Voice chat ended",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            ),
                            color = HermesTextPrimary
                        )
                        Text(
                            text = voiceEndedDuration,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = HermesTextSecondary
                        )
                    }
                    IconButton(
                        onClick = { /* Feedback recorded */ },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Thumbs up",
                            tint = HermesTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = { /* Feedback recorded */ },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbDown,
                            contentDescription = "Thumbs down",
                            tint = HermesTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = onDismissVoiceEnded,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dismiss",
                            tint = HermesTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            } else if (showUpgradeBanner) {
                // Upgrade to Pro top banner matching Screenshot 1
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(HermesSurfaceCard.copy(alpha = 0.6f))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Get more with Hermes Pro",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                        color = HermesTextSecondary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Upgrade to Pro",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        ),
                        color = HermesBlue,
                        modifier = Modifier.clickable { onUpgradeClick() }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Attached Media / File Chips
            if (attachments.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    attachments.forEach { item ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = HermesSurfaceCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, HermesBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (item.endsWith(".jpg") || item.endsWith(".png")) Icons.Default.Image else Icons.Default.Description,
                                    contentDescription = null,
                                    tint = HermesCoral,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = item,
                                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
                                    color = HermesTextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove",
                                    tint = HermesTextSecondary,
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clickable { onRemoveAttachment(item) }
                                )
                            }
                        }
                    }
                }
            }

            // Input Text Field
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 6.dp)
            ) {
                if (text.isEmpty()) {
                    Text(
                        text = "Ask Hermes...",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 17.sp,
                            color = HermesTextSecondary.copy(alpha = 0.6f)
                        )
                    )
                }
                BasicTextField(
                    value = text,
                    onValueChange = onTextChange,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 17.sp,
                        color = HermesTextPrimary
                    ),
                    cursorBrush = SolidColor(HermesCoral),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("hermes_input_field"),
                    maxLines = 6
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom controls row: [+] [Sonnet 5 Low] ... [Mic] [Waveform] or [Send]
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // '+' Add to chat button
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(HermesSurfaceCard)
                        .clickable { onOpenAddToChat() }
                        .testTag("add_to_chat_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add to chat",
                        tint = HermesTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Model Selector Pill (e.g., "Sonnet 5 Low")
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(HermesSurfaceCard)
                        .clickable { onOpenModelSelector() }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("model_selector_pill"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedModel.contains("Sonnet")) "Sonnet 5" else selectedModel,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        ),
                        color = HermesTextPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Low",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                        color = HermesTextSecondary
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                val hasQuery = text.isNotBlank() || attachments.isNotEmpty()

                AnimatedContent(
                    targetState = hasQuery,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(220)) + scaleIn(initialScale = 0.8f))
                            .togetherWith(fadeOut(animationSpec = tween(180)) + scaleOut(targetScale = 0.8f))
                    },
                    label = "composerActionButtons"
                ) { isReadyToSend ->
                    if (isReadyToSend) {
                        // Send button with spring pop
                        var isPressed by remember { mutableStateOf(false) }
                        val sendScale by animateFloatAsState(
                            targetValue = if (isPressed) 0.88f else 1.0f,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                            label = "sendScale"
                        )

                        IconButton(
                            onClick = {
                                isPressed = true
                                onSend()
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .scale(sendScale)
                                .clip(CircleShape)
                                .background(HermesCoral)
                                .testTag("send_button")
                        ) {
                            LaunchedEffect(isPressed) {
                                if (isPressed) {
                                    kotlinx.coroutines.delay(120)
                                    isPressed = false
                                }
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = "Send",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Mic button
                            IconButton(
                                onClick = onStartVoiceMode,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(HermesSurfaceCard)
                                    .testTag("mic_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Voice input",
                                    tint = HermesTextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            // Voice Mode launcher button with waveform icon
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(HermesTextPrimary)
                                    .clickable { onStartVoiceMode() }
                                    .testTag("voice_mode_launch_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GraphicEq,
                                    contentDescription = "Full Voice Mode",
                                    tint = HermesBackground,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
