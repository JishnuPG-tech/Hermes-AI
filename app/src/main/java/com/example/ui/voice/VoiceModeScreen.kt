package com.example.ui.voice

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.HermesMark
import com.example.ui.components.HermesMarkState
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class VoiceState {
    CONNECTING,
    LISTENING,
    THINKING,
    SPEAKING,
    ACTING
}

@Composable
fun VoiceModeScreen(
    selectedModel: String,
    onOpenVoiceSettings: () -> Unit,
    onOpenModelSelector: () -> Unit,
    onCloseVoiceMode: (durationText: String) -> Unit,
    onExecuteActionFromVoice: (String) -> Unit
) {
    var voiceState by remember { mutableStateOf(VoiceState.CONNECTING) }
    var isMicMuted by remember { mutableStateOf(false) }
    var speechTranscript by remember { mutableStateOf("") }
    var actingStep by remember { mutableStateOf("Step 2 / 5\nOpening the application…") }
    var sessionSeconds by remember { mutableIntStateOf(0) }

    // Simulation lifecycle to feel truly alive and responsive
    LaunchedEffect(Unit) {
        // Track session duration
        launch {
            while (true) {
                delay(1000)
                sessionSeconds++
            }
        }

        // Connecting phase
        delay(1400)
        voiceState = VoiceState.LISTENING
        speechTranscript = "\"Build my website\""

        delay(3200)
        voiceState = VoiceState.THINKING

        delay(2200)
        voiceState = VoiceState.SPEAKING

        delay(3800)
        voiceState = VoiceState.ACTING
        actingStep = "Step 2 / 5\nOpening the application…"

        delay(4000)
        voiceState = VoiceState.LISTENING
        speechTranscript = "I am ready for the next instruction."
    }

    val infiniteBeacon = rememberInfiniteTransition(label = "beacon")
    val beaconAlpha by infiniteBeacon.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(750, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "beaconAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HermesBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("voice_mode_screen")
    ) {
        // Top status row: Live indicator & Settings Gear
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Live indicator with animated pulsing dot and real-time clock
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(HermesSurfaceElevated.copy(alpha = 0.6f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            if (voiceState == VoiceState.CONNECTING) HermesAmber.copy(alpha = beaconAlpha)
                            else HermesGreen.copy(alpha = beaconAlpha)
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (voiceState == VoiceState.CONNECTING) "Connecting…" else String.format("%02d:%02d · Live", sessionSeconds / 60, sessionSeconds % 60),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.3.sp
                    ),
                    color = HermesTextPrimary
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Settings gear
            IconButton(
                onClick = onOpenVoiceSettings,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(HermesSurfaceElevated)
                    .testTag("voice_settings_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Voice Settings",
                    tint = HermesTextPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // Center Content: Mark & Status Copy
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Large Animated Hermes Sunburst Mark
            val markState = when (voiceState) {
                VoiceState.CONNECTING -> HermesMarkState.IDLE
                VoiceState.LISTENING -> HermesMarkState.LISTENING
                VoiceState.THINKING -> HermesMarkState.THINKING
                VoiceState.SPEAKING -> HermesMarkState.SPEAKING
                VoiceState.ACTING -> HermesMarkState.ACTING
            }

            HermesMark(
                size = 80.dp,
                state = markState
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Status Headline with smooth slide and fade transitions
            val statusText = when (voiceState) {
                VoiceState.CONNECTING -> "Connecting…"
                VoiceState.LISTENING -> "Listening"
                VoiceState.THINKING -> "Thinking…"
                VoiceState.SPEAKING -> "Speaking"
                VoiceState.ACTING -> "Hermes is acting"
            }

            AnimatedContent(
                targetState = statusText,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(280)) + slideInVertically { it / 3 })
                        .togetherWith(fadeOut(animationSpec = tween(200)) + slideOutVertically { -it / 3 })
                },
                label = "statusTextTransition"
            ) { targetTitle ->
                Text(
                    text = targetTitle,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = HermesTextPrimary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Subtitle / transcript / acting steps / audio visualizer
            when (voiceState) {
                VoiceState.SPEAKING -> {
                    // Animated multi-band audio waveform visualizer
                    VoiceWaveformVisualizer()
                }
                VoiceState.LISTENING -> {
                    AnimatedVisibility(
                        visible = speechTranscript.isNotEmpty(),
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Text(
                            text = "“$speechTranscript”",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 17.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                lineHeight = 24.sp
                            ),
                            color = HermesTextPrimary.copy(alpha = 0.9f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
                VoiceState.ACTING -> {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = HermesAmber.copy(alpha = 0.12f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, HermesAmber.copy(alpha = 0.3f)),
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        Text(
                            text = actingStep,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = HermesAmber,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
                else -> {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // Bottom Controls Row: [+] [Sonnet ⌄] [Mic Toggle] [✕ End]
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // '+' Add Content Button
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(HermesSurfaceElevated)
                    .clickable { /* Quick add content to voice */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add content",
                    tint = HermesTextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Model pill selector (e.g. "Sonnet ⌄")
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(HermesSurfaceElevated)
                    .clickable { onOpenModelSelector() }
                    .padding(horizontal = 18.dp, vertical = 12.dp)
                    .testTag("voice_model_pill"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedModel.contains("Sonnet")) "Sonnet" else selectedModel,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    ),
                    color = HermesTextPrimary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.UnfoldMore,
                    contentDescription = "Change model",
                    tint = HermesTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Mic Mute / Unmute Button
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(if (isMicMuted) HermesRed.copy(alpha = 0.2f) else HermesSurfaceElevated)
                    .clickable { isMicMuted = !isMicMuted },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                    contentDescription = if (isMicMuted) "Unmute" else "Mute",
                    tint = if (isMicMuted) HermesRed else HermesTextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            // End Voice Chat Button (✕)
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable {
                        val durationStr = "${maxOf(sessionSeconds, 2)}s"
                        onCloseVoiceMode(durationStr)
                    }
                    .testTag("voice_end_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "End voice chat",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun VoiceWaveformVisualizer() {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")
    val barAnimations = listOf(
        infiniteTransition.animateFloat(initialValue = 10f, targetValue = 30f, animationSpec = infiniteRepeatable(tween(420, delayMillis = 0, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "h1"),
        infiniteTransition.animateFloat(initialValue = 14f, targetValue = 46f, animationSpec = infiniteRepeatable(tween(380, delayMillis = 40, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "h2"),
        infiniteTransition.animateFloat(initialValue = 18f, targetValue = 56f, animationSpec = infiniteRepeatable(tween(460, delayMillis = 90, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "h3"),
        infiniteTransition.animateFloat(initialValue = 22f, targetValue = 64f, animationSpec = infiniteRepeatable(tween(340, delayMillis = 20, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "h4"),
        infiniteTransition.animateFloat(initialValue = 26f, targetValue = 72f, animationSpec = infiniteRepeatable(tween(500, delayMillis = 70, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "h5"),
        infiniteTransition.animateFloat(initialValue = 20f, targetValue = 60f, animationSpec = infiniteRepeatable(tween(370, delayMillis = 50, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "h6"),
        infiniteTransition.animateFloat(initialValue = 16f, targetValue = 50f, animationSpec = infiniteRepeatable(tween(440, delayMillis = 100, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "h7"),
        infiniteTransition.animateFloat(initialValue = 12f, targetValue = 38f, animationSpec = infiniteRepeatable(tween(390, delayMillis = 30, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "h8"),
        infiniteTransition.animateFloat(initialValue = 8f, targetValue = 24f, animationSpec = infiniteRepeatable(tween(450, delayMillis = 80, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "h9")
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(76.dp)
    ) {
        barAnimations.forEach { barState ->
            Box(
                modifier = Modifier
                    .width(4.5.dp)
                    .height(barState.value.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            listOf(
                                Color(0xFFF09575),
                                HermesCoral
                            )
                        )
                    )
            )
        }
    }
}
