package com.example.data.repository

import com.example.data.local.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

class HermesRepository(private val dao: HermesDao) {

    val allConversations: Flow<List<ConversationEntity>> = dao.getAllConversations()
    val pinnedConversations: Flow<List<ConversationEntity>> = dao.getPinnedConversations()
    val allTasks: Flow<List<TaskEntity>> = dao.getAllTasks()
    val allMemories: Flow<List<MemoryEntity>> = dao.getAllMemories()
    val allProjects: Flow<List<ProjectEntity>> = dao.getAllProjects()

    fun getMessages(conversationId: String): Flow<List<MessageEntity>> {
        return dao.getMessagesForConversation(conversationId)
    }

    suspend fun getConversation(id: String): ConversationEntity? {
        return dao.getConversationById(id)
    }

    suspend fun createConversation(title: String, model: String = "Sonnet 5"): String {
        val id = UUID.randomUUID().toString()
        val conv = ConversationEntity(
            id = id,
            title = title,
            selectedModel = model
        )
        dao.insertConversation(conv)
        return id
    }

    suspend fun insertMessage(
        conversationId: String,
        role: String,
        content: String,
        toolActivityJson: String? = null,
        taskCardJson: String? = null,
        screenContextJson: String? = null
    ) {
        val message = MessageEntity(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            role = role,
            content = content,
            toolActivityJson = toolActivityJson,
            taskCardJson = taskCardJson,
            screenContextJson = screenContextJson
        )
        dao.insertMessage(message)
        val conv = dao.getConversationById(conversationId)
        if (conv != null) {
            dao.updateConversation(conv.copy(updatedAt = System.currentTimeMillis()))
        }
    }

    suspend fun insertTask(task: TaskEntity) {
        dao.insertTask(task)
    }

    suspend fun updateTask(task: TaskEntity) {
        dao.updateTask(task)
    }

    suspend fun insertMemory(type: String, title: String, content: String) {
        dao.insertMemory(
            MemoryEntity(
                id = UUID.randomUUID().toString(),
                type = type,
                title = title,
                content = content
            )
        )
    }

    suspend fun clearMemories() {
        dao.clearAllMemories()
    }

    suspend fun deleteMemory(id: String) {
        dao.deleteMemory(id)
    }

    suspend fun insertProject(name: String, description: String, status: String, taskCount: Int) {
        dao.insertProject(
            ProjectEntity(
                id = UUID.randomUUID().toString(),
                name = name,
                description = description,
                status = status,
                taskCount = taskCount
            )
        )
    }

    // Seed default starter data if empty
    fun seedDefaultsIfEmpty(scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            val convs = dao.getAllConversations().first()
            if (convs.isEmpty()) {
                // Seed initial conversations matching screenshot 5
                val conv1 = ConversationEntity(
                    id = "conv-1",
                    title = "Resume optimization for ATS score i...",
                    isPinned = true,
                    selectedModel = "Sonnet 5"
                )
                val conv2 = ConversationEntity(
                    id = "conv-2",
                    title = "Free Docker hosting platforms 24/7",
                    isPinned = false,
                    selectedModel = "Sonnet 5"
                )
                val conv3 = ConversationEntity(
                    id = "conv-3",
                    title = "Untitled",
                    isPinned = false,
                    selectedModel = "Sonnet 5"
                )
                val conv4 = ConversationEntity(
                    id = "conv-4",
                    title = "Plan execution",
                    isPinned = false,
                    selectedModel = "Sonnet 5"
                )
                val conv5 = ConversationEntity(
                    id = "conv-5",
                    title = "Greeting exchange",
                    isPinned = false,
                    selectedModel = "Sonnet 5"
                )

                dao.insertConversation(conv1)
                dao.insertConversation(conv2)
                dao.insertConversation(conv3)
                dao.insertConversation(conv4)
                dao.insertConversation(conv5)

                // Seed starter memories matching spec & screenshot 7
                dao.insertMemory(
                    MemoryEntity(
                        id = "mem-1",
                        type = "semantic",
                        title = "User Preference",
                        content = "Jishnu prefers concise explanations and calm editorial typography."
                    )
                )
                dao.insertMemory(
                    MemoryEntity(
                        id = "mem-2",
                        type = "semantic",
                        title = "Architecture",
                        content = "Hermes Android uses self-hosted backend with authoritative session persistence."
                    )
                )
                dao.insertMemory(
                    MemoryEntity(
                        id = "mem-3",
                        type = "episodic",
                        title = "Learned preference",
                        content = "Updated server context and project configuration preferences."
                    )
                )

                // Seed sample projects
                dao.insertProject(
                    ProjectEntity(
                        id = "proj-1",
                        name = "Expense Tracker",
                        description = "Full-stack mobile app with automated receipt parsing",
                        status = "Building...",
                        taskCount = 6
                    )
                )
                dao.insertProject(
                    ProjectEntity(
                        id = "proj-2",
                        name = "Hermes Android",
                        description = "Next generation autonomous assistant APK",
                        status = "Active",
                        taskCount = 12
                    )
                )
                dao.insertProject(
                    ProjectEntity(
                        id = "proj-3",
                        name = "Server Deployment",
                        description = "Production CI/CD container stack",
                        status = "Healthy",
                        taskCount = 4
                    )
                )

                // Seed starter tasks matching spec
                dao.insertTask(
                    TaskEntity(
                        id = "task-1",
                        conversationId = "conv-4",
                        title = "Build expense tracker",
                        status = "running",
                        currentStep = 3,
                        totalSteps = 6,
                        stepsJson = "Planning:done|Repository:done|Backend:running|Frontend:pending|Tests:pending|Deployment:pending"
                    )
                )
                dao.insertTask(
                    TaskEntity(
                        id = "task-2",
                        conversationId = "conv-2",
                        title = "Deploy server",
                        status = "waiting_approval",
                        currentStep = 2,
                        totalSteps = 4,
                        stepsJson = "Setup docker:done|Configure ports:done|Provision domain:running|SSL certs:pending"
                    )
                )
                dao.insertTask(
                    TaskEntity(
                        id = "task-3",
                        conversationId = "conv-1",
                        title = "Fix Android build",
                        status = "completed",
                        currentStep = 4,
                        totalSteps = 4,
                        stepsJson = "Audit dependencies:done|Update gradle:done|Verify proguard:done|Compile APK:done"
                    )
                )
            }
        }
    }
}
