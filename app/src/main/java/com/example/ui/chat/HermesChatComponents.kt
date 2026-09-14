package com.example.ui.chat

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.HermesMark
import com.example.ui.components.HermesMarkState
import com.example.ui.theme.*
import java.util.Calendar

@Composable
fun HermesHomeGreeting(
    userName: String = "Jishnu",
    onQuickActionClick: (String) -> Unit
) {
    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 5..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            else -> "Good evening"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 100.dp, bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Radiant Hermes Mark
        HermesMark(
            size = 56.dp,
            state = HermesMarkState.IDLE
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Serif display greeting matching screenshot 1
        Text(
            text = "$greeting, $userName",
            style = MaterialTheme.typography.displayMedium.copy(
                fontSize = 28.sp,
                fontWeight = FontWeight.Normal
            ),
            color = HermesTextPrimary
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Quick action chips (spec: Read screen, Automate, Check memory, New skill)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            QuickActionPill(text = "Read screen") { onQuickActionClick("Read screen") }
            QuickActionPill(text = "Automate") { onQuickActionClick("Automate task") }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            QuickActionPill(text = "Check memory") { onQuickActionClick("Check memory") }
            QuickActionPill(text = "New skill") { onQuickActionClick("New skill") }
        }
    }
}

@Composable
fun QuickActionPill(text: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = HermesSurfaceCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, HermesBorder)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            ),
            color = HermesTextSecondary,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun MessageItem(
    role: String,
    content: String,
    toolActivityJson: String? = null,
    taskCardJson: String? = null,
    screenContextJson: String? = null,
    onViewPlan: (String) -> Unit = {},
    onPermissionDecision: (String, Boolean) -> Unit = { _, _ -> }
) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(320, easing = LinearOutSlowInEasing)) +
                slideInVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    ),
                    initialOffsetY = { 35 }
                )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if (role == "user") {
                // User message (right-aligned, Claude-style warm elevated bubble with asymmetrical rounding)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Surface(
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 6.dp),
                        color = HermesSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, HermesBorder.copy(alpha = 0.5f)),
                        modifier = Modifier.widthIn(max = 310.dp)
                    ) {
                        Text(
                            text = content,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                lineHeight = 23.sp
                            ),
                            color = HermesTextPrimary,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
                        )
                    }
                }
            } else {
                // Assistant message (editorial, spacious, Claude typography and low-chrome design)
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Screen Context preview if present
                    if (screenContextJson != null) {
                        ScreenContextCard(contextSummary = screenContextJson)
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Collapsible Tool Activity if present
                    if (toolActivityJson != null) {
                        ToolActivityCard(activitySummary = toolActivityJson)
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Task Card if present
                    if (taskCardJson != null) {
                        HermesTaskCard(
                            taskJson = taskCardJson,
                            onViewPlan = onViewPlan
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Text Content with Markdown formatting support
                    MarkdownTextDisplay(content = content)

                    // Assistant quick action toolbar with Claude spring micro-interactions
                    val clipboardManager = LocalClipboardManager.current
                    var copiedMessage by remember { mutableStateOf(false) }
                    var thumbsUp by remember { mutableStateOf(false) }
                    var thumbsDown by remember { mutableStateOf(false) }

                    val thumbsUpScale by animateFloatAsState(
                        targetValue = if (thumbsUp) 1.25f else 1.0f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "thumbsUpScale"
                    )
                    val thumbsDownScale by animateFloatAsState(
                        targetValue = if (thumbsDown) 1.25f else 1.0f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "thumbsDownScale"
                    )
                    val copyScale by animateFloatAsState(
                        targetValue = if (copiedMessage) 1.2f else 1.0f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "copyScale"
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(content))
                                copiedMessage = true
                            },
                            modifier = Modifier
                                .size(34.dp)
                                .scale(copyScale)
                        ) {
                            Crossfade(targetState = copiedMessage, label = "copyIcon") { isCopied ->
                                if (isCopied) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Copied",
                                        tint = HermesGreen,
                                        modifier = Modifier.size(17.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy message",
                                        tint = HermesTextSecondary,
                                        modifier = Modifier.size(17.dp)
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = {
                                thumbsUp = !thumbsUp
                                if (thumbsUp) thumbsDown = false
                            },
                            modifier = Modifier
                                .size(34.dp)
                                .scale(thumbsUpScale)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ThumbUp,
                                contentDescription = "Thumbs up",
                                tint = if (thumbsUp) HermesCoral else HermesTextSecondary,
                                modifier = Modifier.size(17.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                thumbsDown = !thumbsDown
                                if (thumbsDown) thumbsUp = false
                            },
                            modifier = Modifier
                                .size(34.dp)
                                .scale(thumbsDownScale)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ThumbDown,
                                contentDescription = "Thumbs down",
                                tint = if (thumbsDown) HermesRed else HermesTextSecondary,
                                modifier = Modifier.size(17.dp)
                            )
                        }

                        AnimatedVisibility(
                            visible = copiedMessage,
                            enter = fadeIn() + expandHorizontally(),
                            exit = fadeOut() + shrinkHorizontally()
                        ) {
                            Text(
                                text = "Copied",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                color = HermesGreen,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }

                    // Inline Permission Card if permission requested
                    if (content.contains("wants to") || content.contains("permission")) {
                        Spacer(modifier = Modifier.height(12.dp))
                        InlinePermissionCard(
                            actionDesc = "Hermes wants to send this message",
                            onDecision = { allowed, always ->
                                onPermissionDecision(if (allowed) "allow" else "deny", always)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MarkdownTextDisplay(content: String) {
    // Splits code blocks from regular text
    val parts = content.split("```")
    Column(modifier = Modifier.fillMaxWidth()) {
        parts.forEachIndexed { index, part ->
            if (index % 2 == 1) {
                // Code block
                val lines = part.trim().lines()
                val lang = if (lines.isNotEmpty() && !lines.first().contains(" ")) lines.first() else ""
                val code = if (lang.isNotEmpty()) lines.drop(1).joinToString("\n") else part.trim()
                CodeSnippetCard(language = lang, code = code)
                Spacer(modifier = Modifier.height(8.dp))
            } else {
                if (part.isNotBlank()) {
                    Text(
                        text = part.trim(),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        ),
                        color = HermesTextPrimary,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CodeSnippetCard(language: String, code: String) {
    val clipboardManager = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(HermesSurfaceCard)
            .border(1.dp, HermesBorder, RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(HermesSurfaceElevated)
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = language.ifEmpty { "code" },
                style = MaterialTheme.typography.labelSmall,
                color = HermesTextSecondary
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = if (copied) "Copied!" else "Copy",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                color = if (copied) HermesGreen else HermesBlue,
                modifier = Modifier.clickable {
                    clipboardManager.setText(AnnotatedString(code))
                    copied = true
                }
            )
        }
        Text(
            text = code,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                lineHeight = 18.sp
            ),
            color = HermesTextPrimary,
            modifier = Modifier.padding(14.dp)
        )
    }
}

@Composable
fun ToolActivityCard(activitySummary: String) {
    var isExpanded by remember { mutableStateOf(false) }
    val arrowRotation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
        label = "arrowRotation"
    )

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = HermesSurfaceCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, HermesBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = HermesAmber,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Working · 4 actions",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
                    color = HermesTextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = HermesTextSecondary,
                    modifier = Modifier
                        .size(18.dp)
                        .rotate(arrowRotation)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)) + fadeIn(),
                exit = shrinkVertically(animationSpec = tween(200)) + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ActionStepRow(label = "Read screen", status = StepStatus.COMPLETE)
                    ActionStepRow(label = "Opened Settings", status = StepStatus.COMPLETE)
                    ActionStepRow(label = "Found Accessibility", status = StepStatus.COMPLETE)
                    ActionStepRow(label = "Tapping Accessibility", status = StepStatus.RUNNING)
                }
            }
        }
    }
}

enum class StepStatus { COMPLETE, RUNNING, PENDING }

@Composable
fun ActionStepRow(label: String, status: StepStatus) {
    val infiniteTransition = rememberInfiniteTransition(label = "step_row")
    val runningPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(tween(600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "runningPulse"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        when (status) {
            StepStatus.COMPLETE -> {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = HermesGreen,
                    modifier = Modifier.size(16.dp)
                )
            }
            StepStatus.RUNNING -> {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .scale(runningPulse),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(HermesAmber)
                    )
                }
            }
            StepStatus.PENDING -> {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, HermesTextMuted, CircleShape)
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
            color = if (status == StepStatus.RUNNING) HermesAmber else HermesTextSecondary
        )
    }
}

@Composable
fun HermesTaskCard(
    taskJson: String,
    onViewPlan: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = HermesSurfaceCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, HermesBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Building expense tracker",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                ),
                color = HermesTextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Steps Checklist matching spec
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                TaskPlanStep(name = "Planning", status = StepStatus.COMPLETE)
                TaskPlanStep(name = "Repository", status = StepStatus.COMPLETE)
                TaskPlanStep(name = "Backend", status = StepStatus.RUNNING)
                TaskPlanStep(name = "Frontend", status = StepStatus.PENDING)
                TaskPlanStep(name = "Tests", status = StepStatus.PENDING)
                TaskPlanStep(name = "Deployment", status = StepStatus.PENDING)
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = HermesDivider, thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "View plan",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    ),
                    color = HermesBlue,
                    modifier = Modifier.clickable { onViewPlan("Building expense tracker") }
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Activity >",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                    color = HermesTextSecondary
                )
            }
        }
    }
}

@Composable
fun TaskPlanStep(name: String, status: StepStatus) {
    val infiniteTransition = rememberInfiniteTransition(label = "task_step")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(700, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulseAlpha"
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        when (status) {
            StepStatus.COMPLETE -> {
                Text(text = "✓", color = HermesGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            StepStatus.RUNNING -> {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(HermesAmber.copy(alpha = pulseAlpha))
                )
            }
            StepStatus.PENDING -> {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .border(1.dp, HermesTextMuted, CircleShape)
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
            color = when (status) {
                StepStatus.COMPLETE -> HermesTextPrimary
                StepStatus.RUNNING -> HermesAmber
                StepStatus.PENDING -> HermesTextSecondary
            }
        )
    }
}

@Composable
fun InlinePermissionCard(
    actionDesc: String,
    onDecision: (allowed: Boolean, always: Boolean) -> Unit
) {
    var decided by remember { mutableStateOf(false) }
    var decisionText by remember { mutableStateOf("") }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = HermesSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, HermesAmber.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = actionDesc,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = HermesTextPrimary
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (!decided) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            decided = true
                            decisionText = "Allowed once"
                            onDecision(true, false)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = HermesSurfaceCard),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Allow once", fontSize = 13.sp, color = HermesTextPrimary)
                    }

                    Button(
                        onClick = {
                            decided = true
                            decisionText = "Allowed always"
                            onDecision(true, true)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = HermesBlue),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Allow always", fontSize = 13.sp, color = Color.White)
                    }

                    OutlinedButton(
                        onClick = {
                            decided = true
                            decisionText = "Denied"
                            onDecision(false, false)
                        },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Deny", fontSize = 13.sp, color = HermesRed)
                    }
                }
            } else {
                Text(
                    text = "Decision: $decisionText",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (decisionText.contains("Allow")) HermesGreen else HermesRed
                )
            }
        }
    }
}

@Composable
fun ScreenContextCard(contextSummary: String) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = HermesSurfaceCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, HermesBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 36.dp, height = 24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(HermesBorder),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🔒",
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Screen context",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
                    color = HermesTextPrimary
                )
                Text(
                    text = contextSummary,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = HermesTextSecondary
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = HermesTextSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun ClaudeThinkingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "thinking_shimmer")
    val dotAlpha1 by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(700, delayMillis = 0, easing = LinearEasing), RepeatMode.Reverse),
        label = "dot1"
    )
    val dotAlpha2 by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(700, delayMillis = 220, easing = LinearEasing), RepeatMode.Reverse),
        label = "dot2"
    )
    val dotAlpha3 by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(700, delayMillis = 440, easing = LinearEasing), RepeatMode.Reverse),
        label = "dot3"
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = HermesSurfaceCard.copy(alpha = 0.6f),
        border = androidx.compose.foundation.BorderStroke(1.dp, HermesBorder.copy(alpha = 0.4f)),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HermesMark(
                size = 24.dp,
                state = HermesMarkState.THINKING
            )
            Spacer(modifier = Modifier.width(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Thinking",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = HermesTextSecondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "•", color = HermesCoral.copy(alpha = dotAlpha1), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(2.dp))
                Text(text = "•", color = HermesCoral.copy(alpha = dotAlpha2), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(2.dp))
                Text(text = "•", color = HermesCoral.copy(alpha = dotAlpha3), fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

