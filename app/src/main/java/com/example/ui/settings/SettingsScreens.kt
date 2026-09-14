package com.example.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMainScreen(
    userEmail: String = "jishnupg2005@gmail.com",
    onBack: () -> Unit,
    onNavigateToCapabilities: () -> Unit,
    onNavigateToConnectors: () -> Unit,
    onNavigateToVoiceSettings: () -> Unit,
    onNavigateToPermissions: () -> Unit,
    onNavigateToBackend: () -> Unit,
    onUpgrade: () -> Unit
) {
    var hapticFeedbackEnabled by remember { mutableStateOf(true) }
    var fontStyle by remember { mutableStateOf("Default (Editorial)") }
    var colorMode by remember { mutableStateOf("Dark (editorial)") }
    var showFontDialog by remember { mutableStateOf(false) }
    var showColorDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = HermesBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                        color = HermesTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = HermesTextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Info */ }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "About",
                            tint = HermesTextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = HermesBackground)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Account Info Card
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(HermesSurfaceElevated)
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(HermesCoral),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("J", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userEmail,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = HermesTextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(HermesSurfaceCard)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("Free", style = MaterialTheme.typography.labelSmall, color = HermesTextSecondary)
                        }
                    }
                }
            }

            // Upgrade Banner Card (matching screenshot 6 & 8)
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = HermesSurfaceElevated,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Want more Hermes?",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = HermesTextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Upgrade for more usage and capabilities.",
                                style = MaterialTheme.typography.bodySmall,
                                color = HermesTextSecondary
                            )
                        }
                        Button(
                            onClick = onUpgrade,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = HermesBlue),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text("Upgrade", fontSize = 13.sp, color = Color.White)
                        }
                    }
                }
            }

            // Section: Configuration
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(HermesSurfaceElevated)
                ) {
                    SettingsNavigationItem(
                        icon = Icons.Default.Tune,
                        title = "Capabilities",
                        subtitle = "5 enabled",
                        onClick = onNavigateToCapabilities
                    )
                    HorizontalDivider(color = HermesDivider, thickness = 1.dp)
                    SettingsNavigationItem(
                        icon = Icons.Default.Link,
                        title = "Connectors",
                        subtitle = "1 connected",
                        onClick = onNavigateToConnectors
                    )
                    HorizontalDivider(color = HermesDivider, thickness = 1.dp)
                    SettingsNavigationItem(
                        icon = Icons.Default.Security,
                        title = "Permissions",
                        subtitle = "Accessibility & skills",
                        onClick = onNavigateToPermissions
                    )
                    HorizontalDivider(color = HermesDivider, thickness = 1.dp)
                    SettingsNavigationItem(
                        icon = Icons.Default.VolumeUp,
                        title = "Voice",
                        subtitle = "Rounded · English (UK)",
                        onClick = onNavigateToVoiceSettings
                    )
                    HorizontalDivider(color = HermesDivider, thickness = 1.dp)
                    SettingsNavigationItem(
                        icon = Icons.Default.Dns,
                        title = "Backend & Telemetry",
                        subtitle = "Self-hosted · Online",
                        onClick = onNavigateToBackend
                    )
                }
            }

            // Section: Preferences
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(HermesSurfaceElevated)
                ) {
                    // Haptic feedback switch
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Vibration,
                            contentDescription = null,
                            tint = HermesTextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Haptic feedback",
                            style = MaterialTheme.typography.bodyLarge,
                            color = HermesTextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = hapticFeedbackEnabled,
                            onCheckedChange = { hapticFeedbackEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = HermesTextPrimary,
                                checkedTrackColor = HermesBlue,
                                uncheckedThumbColor = HermesTextMuted,
                                uncheckedTrackColor = HermesBorder
                            )
                        )
                    }

                    HorizontalDivider(color = HermesDivider, thickness = 1.dp)

                    SettingsNavigationItem(
                        icon = Icons.Default.FormatSize,
                        title = "Font style",
                        subtitle = fontStyle,
                        onClick = { showFontDialog = true }
                    )
                    HorizontalDivider(color = HermesDivider, thickness = 1.dp)
                    SettingsNavigationItem(
                        icon = Icons.Default.Palette,
                        title = "Color mode",
                        subtitle = colorMode,
                        onClick = { showColorDialog = true }
                    )
                }
            }

            // Log out
            item {
                Text(
                    text = "Log out",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = HermesRed,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showLogoutDialog = true }
                        .padding(vertical = 16.dp, horizontal = 4.dp)
                )
            }
        }
    }

    if (showFontDialog) {
        AlertDialog(
            onDismissRequest = { showFontDialog = false },
            containerColor = HermesSurfaceElevated,
            title = { Text("Choose font style", color = HermesTextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Default (Editorial)", "Modern Sans", "Monospace Tech").forEach { font ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    fontStyle = font
                                    showFontDialog = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = fontStyle == font,
                                onClick = {
                                    fontStyle = font
                                    showFontDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = HermesBlue)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(font, color = HermesTextPrimary)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFontDialog = false }) {
                    Text("Done", color = HermesBlue)
                }
            }
        )
    }

    if (showColorDialog) {
        AlertDialog(
            onDismissRequest = { showColorDialog = false },
            containerColor = HermesSurfaceElevated,
            title = { Text("Choose color mode", color = HermesTextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Dark (editorial)", "Pitch Black (OLED)", "Warm Twilight").forEach { mode ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    colorMode = mode
                                    showColorDialog = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = colorMode == mode,
                                onClick = {
                                    colorMode = mode
                                    showColorDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = HermesBlue)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(mode, color = HermesTextPrimary)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showColorDialog = false }) {
                    Text("Done", color = HermesBlue)
                }
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = HermesSurfaceElevated,
            title = { Text("Log out", color = HermesTextPrimary) },
            text = { Text("Are you sure you want to log out of Hermes on this device?", color = HermesTextSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HermesRed)
                ) {
                    Text("Log out", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = HermesTextSecondary)
                }
            }
        )
    }
}

@Composable
fun SettingsNavigationItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = HermesTextSecondary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = HermesTextPrimary,
            modifier = Modifier.weight(1f)
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = HermesTextSecondary
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = HermesTextSecondary,
            modifier = Modifier.size(18.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CapabilitiesScreen(
    onBack: () -> Unit
) {
    var webSearch by remember { mutableStateOf(true) }
    var artifacts by remember { mutableStateOf(true) }
    var inlineViz by remember { mutableStateOf(true) }
    var codeExec by remember { mutableStateOf(true) }
    var automation by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = HermesBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Capabilities",
                        style = MaterialTheme.typography.titleLarge,
                        color = HermesTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = HermesTextPrimary
                        )
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Capabilities allow Hermes to use web search and access tools.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HermesTextSecondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(HermesSurfaceElevated)
                ) {
                    CapabilityToggleRow(
                        title = "Web search",
                        desc = "Hermes can search the web to answer questions.",
                        checked = webSearch,
                        onCheckedChange = { webSearch = it }
                    )
                    HorizontalDivider(color = HermesDivider)
                    CapabilityToggleRow(
                        title = "Artifacts",
                        desc = "Hermes can generate visual artifacts like code and diagrams.",
                        checked = artifacts,
                        onCheckedChange = { artifacts = it }
                    )
                    HorizontalDivider(color = HermesDivider)
                    CapabilityToggleRow(
                        title = "Inline visualizations [BETA]",
                        desc = "Interactive charts and cards rendered inside responses.",
                        checked = inlineViz,
                        onCheckedChange = { inlineViz = it }
                    )
                    HorizontalDivider(color = HermesDivider)
                    CapabilityToggleRow(
                        title = "Code execution and file creation",
                        desc = "Run sandboxed code and export files directly.",
                        checked = codeExec,
                        onCheckedChange = { codeExec = it }
                    )
                    HorizontalDivider(color = HermesDivider)
                    CapabilityToggleRow(
                        title = "Automation & Screen Reading",
                        desc = "Allows autonomous device control via accessibility actuator.",
                        checked = automation,
                        onCheckedChange = { automation = it }
                    )
                }
            }
        }
    }
}

@Composable
fun CapabilityToggleRow(
    title: String,
    desc: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = HermesTextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = HermesTextSecondary
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = HermesTextPrimary,
                checkedTrackColor = HermesBlue,
                uncheckedThumbColor = HermesTextMuted,
                uncheckedTrackColor = HermesBorder
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectorsScreen(
    onBack: () -> Unit
) {
    var connectorDiscovery by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = HermesBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Connectors",
                        style = MaterialTheme.typography.titleLarge,
                        color = HermesTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = HermesTextPrimary
                        )
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(HermesSurfaceElevated)
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Connector discovery",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = HermesTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Discover integrations automatically based on active apps.",
                            style = MaterialTheme.typography.bodySmall,
                            color = HermesTextSecondary
                        )
                    }
                    Switch(
                        checked = connectorDiscovery,
                        onCheckedChange = { connectorDiscovery = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = HermesTextPrimary,
                            checkedTrackColor = HermesBlue,
                            uncheckedThumbColor = HermesTextMuted,
                            uncheckedTrackColor = HermesBorder
                        )
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(HermesSurfaceElevated)
                ) {
                    ConnectorItem(name = "Hugging Face", status = "Connected", isConnected = true)
                    HorizontalDivider(color = HermesDivider)
                    ConnectorItem(name = "Google Drive", status = "Connect >", isConnected = false)
                    HorizontalDivider(color = HermesDivider)
                    ConnectorItem(name = "GitHub", status = "Connect >", isConnected = false)
                    HorizontalDivider(color = HermesDivider)
                    ConnectorItem(name = "Notion", status = "Connect >", isConnected = false)
                }
            }
        }
    }
}

@Composable
fun ConnectorItem(name: String, status: String, isConnected: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            color = HermesTextPrimary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = status,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isConnected) HermesGreen else HermesBlue
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceSettingsScreen(
    onBack: () -> Unit
) {
    val voices = listOf(
        "Rounded" to "Calm, warm, and balanced tone",
        "Classic" to "Clear, authoritative, and concise",
        "Warm" to "Friendly and conversational delivery",
        "Editorial" to "Refined, deliberate, and paced",
        "Breeze" to "Light, swift, and natural"
    )
    var selectedVoice by remember { mutableStateOf("Rounded") }

    Scaffold(
        containerColor = HermesBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Voice settings",
                        style = MaterialTheme.typography.titleLarge,
                        color = HermesTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = HermesTextPrimary
                        )
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Select your preferred voice for real-time conversation.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HermesTextSecondary
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(HermesSurfaceElevated)
                ) {
                    voices.forEachIndexed { index, (vName, vDesc) ->
                        val isSelected = selectedVoice == vName
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedVoice = vName }
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = vName,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSelected) HermesCoral else HermesTextPrimary
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = vDesc,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = HermesTextSecondary
                                )
                            }
                            IconButton(
                                onClick = { /* Play voice preview audio */ },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(HermesSurfaceCard)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play preview",
                                    tint = HermesTextPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        if (index < voices.size - 1) {
                            HorizontalDivider(color = HermesDivider)
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(HermesSurfaceElevated)
                ) {
                    SettingsNavigationItem(
                        icon = Icons.Default.Language,
                        title = "Language",
                        subtitle = "English (United Kingdom)",
                        onClick = { }
                    )
                    HorizontalDivider(color = HermesDivider)
                    SettingsNavigationItem(
                        icon = Icons.Default.Speed,
                        title = "Pace",
                        subtitle = "Normal",
                        onClick = { }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsScreen(
    onBack: () -> Unit
) {
    var accessibilityEnabled by remember { mutableStateOf(true) }
    var screenCaptureEnabled by remember { mutableStateOf(true) }
    var micEnabled by remember { mutableStateOf(true) }
    var backgroundTaskEnabled by remember { mutableStateOf(true) }
    var sandboxStorageEnabled by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = HermesBackground,
        topBar = {
            TopAppBar(
                title = { Text("Permissions & Actuator", color = HermesTextPrimary, style = MaterialTheme.typography.titleLarge) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Hermes requires system access to read screen elements and autonomously execute workflows on your device.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HermesTextSecondary
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(HermesSurfaceElevated)
                ) {
                    CapabilityToggleRow(
                        title = "Accessibility Actuator",
                        desc = "Allows Hermes to perform taps, swipes, and text inputs across apps.",
                        checked = accessibilityEnabled,
                        onCheckedChange = { accessibilityEnabled = it }
                    )
                    HorizontalDivider(color = HermesDivider)
                    CapabilityToggleRow(
                        title = "Screen Reader & OCR",
                        desc = "Parses UI trees and visual screen elements in real time.",
                        checked = screenCaptureEnabled,
                        onCheckedChange = { screenCaptureEnabled = it }
                    )
                    HorizontalDivider(color = HermesDivider)
                    CapabilityToggleRow(
                        title = "Microphone Stream",
                        desc = "Required for conversational real-time voice mode.",
                        checked = micEnabled,
                        onCheckedChange = { micEnabled = it }
                    )
                    HorizontalDivider(color = HermesDivider)
                    CapabilityToggleRow(
                        title = "Background Autonomous Tasks",
                        desc = "Continues task execution when app is backgrounded.",
                        checked = backgroundTaskEnabled,
                        onCheckedChange = { backgroundTaskEnabled = it }
                    )
                    HorizontalDivider(color = HermesDivider)
                    CapabilityToggleRow(
                        title = "Sandbox File System",
                        desc = "Allows code execution and saving generated artifacts.",
                        checked = sandboxStorageEnabled,
                        onCheckedChange = { sandboxStorageEnabled = it }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackendTelemetryScreen(
    currentUrl: String,
    onUpdateUrl: (String) -> Unit,
    onBack: () -> Unit
) {
    var endpoint by remember { mutableStateOf(currentUrl) }
    var apiKey by remember { mutableStateOf("sk_live_hermes_prod_882914") }
    var pingResult by remember { mutableStateOf<String?>("24 ms · Latency optimal") }
    var telemetryEnabled by remember { mutableStateOf(true) }
    var offlineQueueEnabled by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = HermesBackground,
        topBar = {
            TopAppBar(
                title = { Text("Backend & Telemetry", color = HermesTextPrimary, style = MaterialTheme.typography.titleLarge) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Configure self-hosted API orchestration, local model gateways, and diagnostic telemetry.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HermesTextSecondary
                )
            }

            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = HermesSurfaceElevated,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Server Endpoint",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = HermesTextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = endpoint,
                            onValueChange = {
                                endpoint = it
                                onUpdateUrl(it)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HermesBlue,
                                unfocusedBorderColor = HermesBorder,
                                focusedTextColor = HermesTextPrimary,
                                unfocusedTextColor = HermesTextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Bearer Auth Token",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = HermesTextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = apiKey,
                            onValueChange = { apiKey = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HermesBlue,
                                unfocusedBorderColor = HermesBorder,
                                focusedTextColor = HermesTextPrimary,
                                unfocusedTextColor = HermesTextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    pingResult = "18 ms · Connected to hermes-edge"
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = HermesSurfaceCard)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, tint = HermesTextPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Ping Server", color = HermesTextPrimary)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            pingResult?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = HermesGreen
                                )
                            }
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(HermesSurfaceElevated)
                ) {
                    CapabilityToggleRow(
                        title = "Local Device Telemetry",
                        desc = "Keep anonymous error traces and crash logs locally.",
                        checked = telemetryEnabled,
                        onCheckedChange = { telemetryEnabled = it }
                    )
                    HorizontalDivider(color = HermesDivider)
                    CapabilityToggleRow(
                        title = "Offline Action Queue",
                        desc = "Queue autonomous tasks and messages when disconnected.",
                        checked = offlineQueueEnabled,
                        onCheckedChange = { offlineQueueEnabled = it }
                    )
                }
            }
        }
    }
}

@Composable
fun UpgradeProDialog(
    onUpgrade: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = HermesSurfaceElevated,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(HermesCoral),
                    contentAlignment = Alignment.Center
                ) {
                    Text("H", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Hermes Pro",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = HermesTextPrimary
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Unlock the ultimate autonomous agent experience:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HermesTextSecondary
                )
                ProBenefitRow(text = "Sonnet 5 and Opus 5 unlimited inference")
                ProBenefitRow(text = "Real-time autonomous screen actuation")
                ProBenefitRow(text = "200k token context window & fast cache")
                ProBenefitRow(text = "Zero-latency real-time voice mode")
                ProBenefitRow(text = "Dedicated self-hosted cluster connectors")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onUpgrade()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = HermesBlue),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Upgrade for $20 / month", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Maybe later", color = HermesTextSecondary)
            }
        }
    )
}

@Composable
fun ProBenefitRow(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = HermesGreen,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
            color = HermesTextPrimary
        )
    }
}
