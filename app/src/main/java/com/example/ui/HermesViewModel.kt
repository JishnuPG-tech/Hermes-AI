package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.*
import com.example.data.repository.HermesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HermesUiState(
    val currentConversationId: String? = null,
    val selectedModel: String = "Sonnet 5",
    val webSearchEnabled: Boolean = true,
    val memoryEnabled: Boolean = true,
    val selectedProject: String = "None",
    val isThinking: Boolean = false,
    val voiceChatEndedDuration: String? = null,
    val activeReticleCoordinates: Pair<Float, Float>? = null, // Visual reticle for automated device actions
    val planTimelineTitle: String? = null,
    val attachments: List<String> = emptyList(),
    val fontStyle: String = "Default (Editorial)",
    val colorMode: String = "Dark (editorial)",
    val hapticFeedbackEnabled: Boolean = true,
    val backendUrl: String = "http://127.0.0.1:8080",
    val isProUser: Boolean = false
)

class HermesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HermesRepository
    val conversations: StateFlow<List<ConversationEntity>>
    val pinnedConversations: StateFlow<List<ConversationEntity>>
    val tasks: StateFlow<List<TaskEntity>>
    val memories: StateFlow<List<MemoryEntity>>
    val projects: StateFlow<List<ProjectEntity>>

    private val _uiState = MutableStateFlow(HermesUiState())
    val uiState: StateFlow<HermesUiState> = _uiState.asStateFlow()

    private val _currentMessages = MutableStateFlow<List<MessageEntity>>(emptyList())
    val currentMessages: StateFlow<List<MessageEntity>> = _currentMessages.asStateFlow()

    init {
        val db = HermesDatabase.getDatabase(application)
        repository = HermesRepository(db.hermesDao())
        repository.seedDefaultsIfEmpty(viewModelScope)

        conversations = repository.allConversations
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        pinnedConversations = repository.pinnedConversations
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        tasks = repository.allTasks
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        memories = repository.allMemories
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        projects = repository.allProjects
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        // Auto-select latest conversation or start fresh
        viewModelScope.launch {
            conversations.collect { convList ->
                if (_uiState.value.currentConversationId == null && convList.isNotEmpty()) {
                    selectConversation(convList.first().id)
                }
            }
        }
    }

    fun selectConversation(conversationId: String) {
        _uiState.update { it.copy(currentConversationId = conversationId) }
        viewModelScope.launch {
            repository.getMessages(conversationId).collect { msgList ->
                _currentMessages.value = msgList
            }
        }
    }

    fun createNewChat() {
        viewModelScope.launch {
            val newId = repository.createConversation(
                title = "New Chat",
                model = _uiState.value.selectedModel
            )
            _uiState.update { it.copy(currentConversationId = newId) }
            _currentMessages.value = emptyList()
        }
    }

    fun updateSelectedModel(model: String) {
        _uiState.update { it.copy(selectedModel = model) }
    }

    fun toggleWebSearch(enabled: Boolean) {
        _uiState.update { it.copy(webSearchEnabled = enabled) }
    }

    fun toggleMemory(enabled: Boolean) {
        _uiState.update { it.copy(memoryEnabled = enabled) }
    }

    fun setSelectedProject(project: String) {
        _uiState.update { it.copy(selectedProject = project) }
    }

    fun setVoiceChatEnded(duration: String) {
        _uiState.update { it.copy(voiceChatEndedDuration = duration) }
    }

    fun dismissVoiceChatEnded() {
        _uiState.update { it.copy(voiceChatEndedDuration = null) }
    }

    fun showPlanTimeline(title: String) {
        _uiState.update { it.copy(planTimelineTitle = title) }
    }

    fun dismissPlanTimeline() {
        _uiState.update { it.copy(planTimelineTitle = null) }
    }

    fun clearAllMemories() {
        viewModelScope.launch {
            repository.clearMemories()
        }
    }

    fun addAttachment(fileName: String) {
        _uiState.update { current ->
            if (current.attachments.contains(fileName)) current
            else current.copy(attachments = current.attachments + fileName)
        }
    }

    fun removeAttachment(fileName: String) {
        _uiState.update { current ->
            current.copy(attachments = current.attachments - fileName)
        }
    }

    fun clearAttachments() {
        _uiState.update { it.copy(attachments = emptyList()) }
    }

    fun updateFontStyle(font: String) {
        _uiState.update { it.copy(fontStyle = font) }
    }

    fun updateColorMode(mode: String) {
        _uiState.update { it.copy(colorMode = mode) }
    }

    fun toggleHapticFeedback(enabled: Boolean) {
        _uiState.update { it.copy(hapticFeedbackEnabled = enabled) }
    }

    fun updateBackendUrl(url: String) {
        _uiState.update { it.copy(backendUrl = url) }
    }

    fun upgradeToPro() {
        _uiState.update { it.copy(isProUser = true) }
    }

    fun recordPermissionDecision(decision: String, always: Boolean) {
        val currentConvId = _uiState.value.currentConversationId ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val decisionText = if (always) "Allowed always" else if (decision == "allow") "Allowed once" else "Denied"
            repository.insertMessage(
                conversationId = currentConvId,
                role = "assistant",
                content = "Permission was $decisionText. Resuming device actuation..."
            )
        }
    }

    fun addProject(name: String, description: String, taskCount: Int = 0) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertProject(
                name = name,
                description = description,
                status = "Active",
                taskCount = taskCount
            )
        }
    }

    fun addTask(title: String, stepsJson: String = "") {
        val currentConvId = _uiState.value.currentConversationId ?: "conv-1"
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTask(
                TaskEntity(
                    id = java.util.UUID.randomUUID().toString(),
                    conversationId = currentConvId,
                    title = title,
                    status = "running",
                    currentStep = 1,
                    totalSteps = 4,
                    stepsJson = stepsJson
                )
            )
        }
    }

    fun setProUser(isPro: Boolean) {
        _uiState.update { it.copy(isProUser = isPro) }
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank() && _uiState.value.attachments.isEmpty()) return
        val currentConvId = _uiState.value.currentConversationId ?: return

        val attachmentsList = _uiState.value.attachments
        val finalUserMessage = if (attachmentsList.isNotEmpty()) {
            val attachmentTag = attachmentsList.joinToString(", ") { "📎 $it" }
            if (userText.isNotBlank()) "$userText\n\n[$attachmentTag]" else "[$attachmentTag]"
        } else {
            userText
        }

        clearAttachments()

        viewModelScope.launch(Dispatchers.IO) {
            // Insert user message
            repository.insertMessage(
                conversationId = currentConvId,
                role = "user",
                content = finalUserMessage
            )

            _uiState.update { it.copy(isThinking = true) }

            // Agent thinking simulation & realistic action execution
            delay(1200)

            val query = userText.lowercase()
            when {
                query.contains("read screen") || query.contains("screen") -> {
                    // Screen reading response
                    repository.insertMessage(
                        conversationId = currentConvId,
                        role = "assistant",
                        content = "I read the current screen. Here is what is visible:\n\n- Active Window: Settings > Accessibility\n- Elements: 14 interactive components found\n- Target recommended: Hermes Accessibility Service\n\nWould you like me to proceed with enabling automated navigation?",
                        screenContextJson = "Current screen: Accessibility Settings (14 elements)",
                        toolActivityJson = "Read screen completed"
                    )
                }
                query.contains("automate") || query.contains("build") || query.contains("task") -> {
                    // Task execution response with Task Card & checklist
                    repository.insertMessage(
                        conversationId = currentConvId,
                        role = "assistant",
                        content = "I have initiated the autonomous project plan for you. The task pipeline is active.",
                        taskCardJson = "Building expense tracker",
                        toolActivityJson = "Working · 4 actions"
                    )
                }
                query.contains("memory") -> {
                    repository.insertMessage(
                        conversationId = currentConvId,
                        role = "assistant",
                        content = "I have checked your long-term memory across devices:\n\n- **Preference**: Concise editorial communication\n- **Project**: Hermes Android autonomous agent APK\n- **Backend**: Self-hosted endpoint with secure local session sync\n\nAll memories are synchronized and active."
                    )
                }
                query.contains("skill") -> {
                    repository.insertMessage(
                        conversationId = currentConvId,
                        role = "assistant",
                        content = "Installed Hermes Skills:\n\n1. `accessibility-actuator`: Device UI navigation and gestures\n2. `screen-reader`: Vision and accessibility tree parser\n3. `code-executor`: Sandboxed Kotlin and Python execution\n4. `web-search`: Real-time query retrieval\n\nAll skills are operational."
                    )
                }
                attachmentsList.isNotEmpty() -> {
                    repository.insertMessage(
                        conversationId = currentConvId,
                        role = "assistant",
                        content = "I have received and analyzed the attached files (${attachmentsList.joinToString(", ")}).\n\nExtracted structure and insights:\n- Format validated: OK\n- Semantic parsing: Completed\n- Ready for automated refactor or synthesis."
                    )
                }
                else -> {
                    // Standard intelligent editorial response
                    repository.insertMessage(
                        conversationId = currentConvId,
                        role = "assistant",
                        content = "Understood. I am processing this with ${_uiState.value.selectedModel}.\n\n```kotlin\n// Hermes autonomous execution payload\nval session = HermesAgentSession(\n    model = \"${_uiState.value.selectedModel}\",\n    capabilities = listOf(\"web_search\", \"artifacts\", \"actuator\")\n)\nsession.execute()\n```\n\nI can execute this on device or keep it in sandbox. Let me know how you'd like to proceed."
                    )
                }
            }

            _uiState.update { it.copy(isThinking = false) }
        }
    }
}
