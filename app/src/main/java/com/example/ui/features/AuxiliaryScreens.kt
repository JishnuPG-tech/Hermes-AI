package com.example.ui.features

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.MemoryEntity
import com.example.data.local.ProjectEntity
import com.example.data.local.TaskEntity
import com.example.ui.chat.StepStatus
import com.example.ui.chat.TaskPlanStep
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(
    projects: List<ProjectEntity>,
    onBack: () -> Unit,
    onNewProject: () -> Unit = {}
) {
    Scaffold(
        containerColor = HermesBackground,
        topBar = {
            TopAppBar(
                title = { Text("Projects", color = HermesTextPrimary, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = HermesTextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = HermesBackground)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(projects) { project ->
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = HermesSurfaceElevated,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = project.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = HermesTextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(HermesSurfaceCard)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = project.status,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (project.status.contains("Building")) HermesAmber else HermesGreen
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = project.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = HermesTextSecondary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "${project.taskCount} tasks linked",
                            style = MaterialTheme.typography.bodySmall,
                            color = HermesTextMuted
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    tasks: List<TaskEntity>,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = HermesBackground,
        topBar = {
            TopAppBar(
                title = { Text("Autonomous Tasks", color = HermesTextPrimary, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = HermesTextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = HermesBackground)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(tasks) { task ->
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = HermesSurfaceElevated,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = HermesTextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            val statusColor = when (task.status) {
                                "completed" -> HermesGreen
                                "running" -> HermesAmber
                                "waiting_approval" -> HermesBlue
                                else -> HermesRed
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(statusColor.copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = task.status.replace("_", " ").uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = statusColor
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Progress: Step ${task.currentStep} of ${task.totalSteps}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = HermesTextSecondary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { (task.currentStep.toFloat() / task.totalSteps.coerceAtLeast(1).toFloat()) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = HermesCoral,
                            trackColor = HermesBorder
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryScreen(
    memories: List<MemoryEntity>,
    onClearAll: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = HermesBackground,
        topBar = {
            TopAppBar(
                title = { Text("Memory", color = HermesTextPrimary, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = HermesTextPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = onClearAll) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = "Wipe Memory", tint = HermesRed)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = HermesBackground)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "Hermes remembers your preferences, project context, and patterns across devices.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HermesTextSecondary
                )
            }

            items(memories) { mem ->
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = HermesSurfaceElevated,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = mem.title,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = HermesTextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(HermesSurfaceCard)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = mem.type.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (mem.type == "semantic") HermesBlue else HermesAmber
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = mem.content,
                            style = MaterialTheme.typography.bodyMedium,
                            color = HermesTextSecondary
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanTimelineSheet(
    title: String,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = HermesSurfaceElevated,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = HermesTextPrimary,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = HermesTextSecondary)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TaskPlanStep(name = "1. Architecture specification & schema", status = StepStatus.COMPLETE)
                TaskPlanStep(name = "2. Initialize repository & dependencies", status = StepStatus.COMPLETE)
                TaskPlanStep(name = "3. Implement backend API routes & database", status = StepStatus.RUNNING)
                TaskPlanStep(name = "4. Assemble Compose UI and design components", status = StepStatus.PENDING)
                TaskPlanStep(name = "5. Automated verification and tests", status = StepStatus.PENDING)
                TaskPlanStep(name = "6. Build & deployment to container runtime", status = StepStatus.PENDING)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillsScreen(
    onBack: () -> Unit
) {
    var showAddSkillDialog by remember { mutableStateOf(false) }
    var skillNameInput by remember { mutableStateOf("") }
    var skillDescInput by remember { mutableStateOf("") }

    val skillsList = remember {
        mutableStateListOf(
            SkillItem("accessibility-actuator", "v2.4.0", "Full device navigation, gesture input, and cross-app automation.", true),
            SkillItem("screen-reader", "v3.1.0", "Semantic UI tree parsing, element layout bounding, and OCR detection.", true),
            SkillItem("code-executor", "v1.8.2", "Secure local Kotlin and Python execution sandbox.", true),
            SkillItem("web-search", "v2.0.0", "Real-time web query retrieval and citation summarizer.", true),
            SkillItem("notion-sync", "v1.2.0", "Two-way document syncing with Notion databases.", false)
        )
    }

    Scaffold(
        containerColor = HermesBackground,
        topBar = {
            TopAppBar(
                title = { Text("Installed Skills", color = HermesTextPrimary, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = HermesTextPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = { showAddSkillDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add skill", tint = HermesCoral)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = HermesBackground)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "Skills empower Hermes to orchestrate tasks across your device and cloud APIs.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HermesTextSecondary
                )
            }

            items(skillsList) { skill ->
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = HermesSurfaceElevated,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = skill.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = HermesTextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(HermesSurfaceCard)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = skill.version,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = HermesTextSecondary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = skill.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = HermesTextSecondary
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (skill.isEnabled) "Status: Operational" else "Status: Disabled",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (skill.isEnabled) HermesGreen else HermesTextMuted,
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                checked = skill.isEnabled,
                                onCheckedChange = { isChecked ->
                                    val index = skillsList.indexOf(skill)
                                    if (index != -1) {
                                        skillsList[index] = skill.copy(isEnabled = isChecked)
                                    }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = HermesTextPrimary,
                                    checkedTrackColor = HermesBlue,
                                    uncheckedThumbColor = HermesTextMuted,
                                    uncheckedTrackColor = HermesBorder
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddSkillDialog) {
        AlertDialog(
            onDismissRequest = { showAddSkillDialog = false },
            containerColor = HermesSurfaceElevated,
            title = { Text("Add Custom Skill", color = HermesTextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Enter skill identifier and prompt specification:", color = HermesTextSecondary, style = MaterialTheme.typography.bodySmall)
                    OutlinedTextField(
                        value = skillNameInput,
                        onValueChange = { skillNameInput = it },
                        label = { Text("Skill name (e.g. spotify-controller)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = skillDescInput,
                        onValueChange = { skillDescInput = it },
                        label = { Text("Description & trigger intent") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (skillNameInput.isNotBlank()) {
                            skillsList.add(SkillItem(skillNameInput, "v1.0.0", skillDescInput.ifBlank { "Custom autonomous skill" }, true))
                            skillNameInput = ""
                            skillDescInput = ""
                            showAddSkillDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HermesBlue)
                ) {
                    Text("Install Skill", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddSkillDialog = false }) {
                    Text("Cancel", color = HermesTextSecondary)
                }
            }
        )
    }
}

data class SkillItem(val name: String, val version: String, val description: String, val isEnabled: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeScreen(
    onBack: () -> Unit
) {
    val sampleCode = """
package com.example.agent

class HermesActuator(private val context: Context) {
    fun executeStep(step: AutonomousStep) {
        val targetNode = findAccessibilityNode(step.elementId)
        targetNode?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
    }
}
""".trimIndent()

    Scaffold(
        containerColor = HermesBackground,
        topBar = {
            TopAppBar(
                title = { Text("Code Repository", color = HermesTextPrimary, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = HermesTextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = HermesBackground)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(HermesSurfaceElevated)
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.FolderZip, contentDescription = null, tint = HermesAmber, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("hermes-android-agent", style = MaterialTheme.typography.titleMedium, color = HermesTextPrimary)
                        Text("branch: main · 12 commits", style = MaterialTheme.typography.bodySmall, color = HermesTextSecondary)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(HermesSurfaceCard)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Git synced", color = HermesGreen, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            item {
                Text("Project Files", style = MaterialTheme.typography.titleSmall, color = HermesTextSecondary)
            }

            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = HermesSurfaceElevated,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("HermesActuator.kt", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold), color = HermesCoral)
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(HermesSurfaceCard)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = sampleCode,
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, fontSize = 12.sp),
                                color = HermesTextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtifactsScreen(
    onBack: () -> Unit
) {
    val artifacts = listOf(
        "Architecture System Diagram" to "SVG vector architecture flowchart for multi-agent execution pipeline.",
        "Monthly Spend Analytics Chart" to "Interactive data visualization generated from user financial inputs.",
        "Editorial Theme Color Tokens" to "Design system specification for dark and twilight layouts."
    )

    Scaffold(
        containerColor = HermesBackground,
        topBar = {
            TopAppBar(
                title = { Text("Artifacts Gallery", color = HermesTextPrimary, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = HermesTextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = HermesBackground)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "Visual and interactive artifacts produced during autonomous chat sessions.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HermesTextSecondary
                )
            }

            items(artifacts) { (name, desc) ->
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = HermesSurfaceElevated,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = HermesBlue, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = HermesTextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodyMedium,
                            color = HermesTextSecondary
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = HermesSurfaceCard)
                            ) {
                                Text("Preview", color = HermesTextPrimary, fontSize = 13.sp)
                            }
                            Button(
                                onClick = { },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = HermesBlue)
                            ) {
                                Text("Export", color = Color.White, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
