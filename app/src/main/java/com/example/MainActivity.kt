package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.HermesViewModel
import com.example.ui.chat.HermesHomeGreeting
import com.example.ui.chat.MessageItem
import com.example.ui.chat.ClaudeThinkingIndicator
import com.example.ui.components.*
import com.example.ui.features.*
import com.example.ui.settings.*
import com.example.ui.theme.*
import com.example.ui.voice.VoiceModeScreen
import kotlinx.coroutines.launch

sealed class ScreenDestination {
    data object Chat : ScreenDestination()
    data object Voice : ScreenDestination()
    data object Settings : ScreenDestination()
    data object Capabilities : ScreenDestination()
    data object Connectors : ScreenDestination()
    data object VoiceSettings : ScreenDestination()
    data object Permissions : ScreenDestination()
    data object BackendTelemetry : ScreenDestination()
    data object Projects : ScreenDestination()
    data object Tasks : ScreenDestination()
    data object Memory : ScreenDestination()
    data object Skills : ScreenDestination()
    data object Code : ScreenDestination()
    data object Artifacts : ScreenDestination()
}

class MainActivity : ComponentActivity() {

    private val viewModel: HermesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                HermesApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun HermesApp(viewModel: HermesViewModel) {
    var currentDestination by remember { mutableStateOf<ScreenDestination>(ScreenDestination.Chat) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val conversations by viewModel.conversations.collectAsStateWithLifecycle()
    val pinnedConversations by viewModel.pinnedConversations.collectAsStateWithLifecycle()
    val currentMessages by viewModel.currentMessages.collectAsStateWithLifecycle()
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val memories by viewModel.memories.collectAsStateWithLifecycle()
    val projects by viewModel.projects.collectAsStateWithLifecycle()

    var showModelSelectorSheet by remember { mutableStateOf(false) }
    var showAddToChatSheet by remember { mutableStateOf(false) }
    var showUpgradeDialog by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    // Auto-scroll to bottom on new message
    LaunchedEffect(currentMessages.size) {
        if (currentMessages.isNotEmpty()) {
            listState.animateScrollToItem(currentMessages.size - 1)
        }
    }

    when (val dest = currentDestination) {
        is ScreenDestination.Voice -> {
            VoiceModeScreen(
                selectedModel = uiState.selectedModel,
                onOpenVoiceSettings = { currentDestination = ScreenDestination.VoiceSettings },
                onOpenModelSelector = { showModelSelectorSheet = true },
                onCloseVoiceMode = { duration ->
                    viewModel.setVoiceChatEnded(duration)
                    currentDestination = ScreenDestination.Chat
                },
                onExecuteActionFromVoice = { /* Handled in voice */ }
            )
        }

        is ScreenDestination.Settings -> {
            SettingsMainScreen(
                onBack = { currentDestination = ScreenDestination.Chat },
                onNavigateToCapabilities = { currentDestination = ScreenDestination.Capabilities },
                onNavigateToConnectors = { currentDestination = ScreenDestination.Connectors },
                onNavigateToVoiceSettings = { currentDestination = ScreenDestination.VoiceSettings },
                onNavigateToPermissions = { currentDestination = ScreenDestination.Permissions },
                onNavigateToBackend = { currentDestination = ScreenDestination.BackendTelemetry },
                onUpgrade = { showUpgradeDialog = true }
            )
        }

        is ScreenDestination.Capabilities -> {
            CapabilitiesScreen(onBack = { currentDestination = ScreenDestination.Settings })
        }

        is ScreenDestination.Connectors -> {
            ConnectorsScreen(onBack = { currentDestination = ScreenDestination.Settings })
        }

        is ScreenDestination.VoiceSettings -> {
            VoiceSettingsScreen(onBack = {
                currentDestination = ScreenDestination.Settings
            })
        }

        is ScreenDestination.Permissions -> {
            PermissionsScreen(onBack = { currentDestination = ScreenDestination.Settings })
        }

        is ScreenDestination.BackendTelemetry -> {
            BackendTelemetryScreen(
                currentUrl = uiState.backendUrl,
                onUpdateUrl = { viewModel.updateBackendUrl(it) },
                onBack = { currentDestination = ScreenDestination.Settings }
            )
        }

        is ScreenDestination.Projects -> {
            ProjectsScreen(
                projects = projects,
                onBack = { currentDestination = ScreenDestination.Chat }
            )
        }

        is ScreenDestination.Tasks -> {
            TasksScreen(
                tasks = tasks,
                onBack = { currentDestination = ScreenDestination.Chat }
            )
        }

        is ScreenDestination.Memory -> {
            MemoryScreen(
                memories = memories,
                onClearAll = { viewModel.clearAllMemories() },
                onBack = { currentDestination = ScreenDestination.Chat }
            )
        }

        is ScreenDestination.Skills -> {
            SkillsScreen(onBack = { currentDestination = ScreenDestination.Chat })
        }

        is ScreenDestination.Code -> {
            CodeScreen(onBack = { currentDestination = ScreenDestination.Chat })
        }

        is ScreenDestination.Artifacts -> {
            ArtifactsScreen(onBack = { currentDestination = ScreenDestination.Chat })
        }

        is ScreenDestination.Chat -> {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        drawerContainerColor = HermesBackground,
                        drawerContentColor = HermesTextPrimary
                    ) {
                        HermesDrawerContent(
                            conversations = conversations,
                            pinnedConversations = pinnedConversations,
                            currentConversationId = uiState.currentConversationId,
                            onSelectConversation = { id ->
                                viewModel.selectConversation(id)
                                scope.launch { drawerState.close() }
                            },
                            onNewChat = {
                                viewModel.createNewChat()
                                scope.launch { drawerState.close() }
                            },
                            onNavigateToProjects = {
                                currentDestination = ScreenDestination.Projects
                                scope.launch { drawerState.close() }
                            },
                            onNavigateToTasks = {
                                currentDestination = ScreenDestination.Tasks
                                scope.launch { drawerState.close() }
                            },
                            onNavigateToSkills = {
                                currentDestination = ScreenDestination.Skills
                                scope.launch { drawerState.close() }
                            },
                            onNavigateToMemory = {
                                currentDestination = ScreenDestination.Memory
                                scope.launch { drawerState.close() }
                            },
                            onNavigateToCode = {
                                currentDestination = ScreenDestination.Code
                                scope.launch { drawerState.close() }
                            },
                            onNavigateToArtifacts = {
                                currentDestination = ScreenDestination.Artifacts
                                scope.launch { drawerState.close() }
                            },
                            onOpenProfileSettings = {
                                currentDestination = ScreenDestination.Settings
                                scope.launch { drawerState.close() }
                            }
                        )
                    }
                }
            ) {
                Scaffold(
                    containerColor = HermesBackground,
                    topBar = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .statusBarsPadding()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Hamburger Drawer Toggle
                            IconButton(
                                onClick = { scope.launch { drawerState.open() } },
                                modifier = Modifier
                                    .size(40.dp)
                                    .testTag("drawer_menu_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Open Menu",
                                    tint = HermesTextPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            // Ghost/Bot Agent Indicator Icon matching Screenshot 1 & 4
                            IconButton(
                                onClick = { currentDestination = ScreenDestination.Tasks },
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(HermesSurfaceElevated)
                                    .testTag("agent_status_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SmartToy,
                                    contentDescription = "Active Agent",
                                    tint = HermesCoral,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    },
                    bottomBar = {
                        HermesComposer(
                            text = inputText,
                            onTextChange = { inputText = it },
                            selectedModel = uiState.selectedModel,
                            onOpenModelSelector = { showModelSelectorSheet = true },
                            onOpenAddToChat = { showAddToChatSheet = true },
                            onSend = {
                                val text = inputText
                                inputText = ""
                                viewModel.sendMessage(text)
                            },
                            onStartVoiceMode = { currentDestination = ScreenDestination.Voice },
                            attachments = uiState.attachments,
                            onRemoveAttachment = { viewModel.removeAttachment(it) },
                            showUpgradeBanner = !uiState.isProUser,
                            onUpgradeClick = { showUpgradeDialog = true },
                            voiceEndedDuration = uiState.voiceChatEndedDuration,
                            onDismissVoiceEnded = { viewModel.dismissVoiceChatEnded() }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (currentMessages.isEmpty()) {
                            // Empty State / Home Greeting matching Screenshot 1
                            HermesHomeGreeting(
                                userName = "Jishnu",
                                onQuickActionClick = { action ->
                                    viewModel.sendMessage(action)
                                }
                            )
                        } else {
                            // Message List with auto-scroll animation
                            LaunchedEffect(currentMessages.size, uiState.isThinking) {
                                if (currentMessages.isNotEmpty() || uiState.isThinking) {
                                    val targetIndex = if (uiState.isThinking) currentMessages.size else currentMessages.size - 1
                                    if (targetIndex >= 0) {
                                        listState.animateScrollToItem(targetIndex)
                                    }
                                }
                            }

                            LazyColumn(
                                state = listState,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 8.dp),
                                contentPadding = PaddingValues(top = 12.dp, bottom = 16.dp)
                            ) {
                                items(currentMessages, key = { it.id }) { message ->
                                    MessageItem(
                                        role = message.role,
                                        content = message.content,
                                        toolActivityJson = message.toolActivityJson,
                                        taskCardJson = message.taskCardJson,
                                        screenContextJson = message.screenContextJson,
                                        onViewPlan = { planTitle ->
                                            viewModel.showPlanTimeline(planTitle)
                                        }
                                    )
                                }

                                if (uiState.isThinking) {
                                    item {
                                        ClaudeThinkingIndicator()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Bottom Sheets
    if (showModelSelectorSheet) {
        ModelSelectorSheet(
            selectedModel = uiState.selectedModel,
            onModelSelected = { model ->
                viewModel.updateSelectedModel(model)
            },
            onDismiss = { showModelSelectorSheet = false }
        )
    }

    if (showAddToChatSheet) {
        AddToChatSheet(
            webSearchEnabled = uiState.webSearchEnabled,
            onToggleWebSearch = { viewModel.toggleWebSearch(it) },
            memoryEnabled = uiState.memoryEnabled,
            onToggleMemory = { viewModel.toggleMemory(it) },
            selectedProject = uiState.selectedProject,
            onSelectProjectClick = {
                currentDestination = ScreenDestination.Projects
                showAddToChatSheet = false
            },
            onActionClick = { action ->
                when (action) {
                    "Camera" -> viewModel.addAttachment("camera_snapshot.jpg")
                    "Photos" -> viewModel.addAttachment("image_selected.png")
                    "Files" -> viewModel.addAttachment("workspace_doc.pdf")
                    else -> viewModel.addAttachment(action)
                }
                showAddToChatSheet = false
            },
            onDismiss = { showAddToChatSheet = false }
        )
    }

    if (showUpgradeDialog) {
        UpgradeProDialog(
            onUpgrade = { viewModel.setProUser(true) },
            onDismiss = { showUpgradeDialog = false }
        )
    }

    uiState.planTimelineTitle?.let { planTitle ->
        PlanTimelineSheet(
            title = planTitle,
            onDismiss = { viewModel.dismissPlanTimeline() }
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
