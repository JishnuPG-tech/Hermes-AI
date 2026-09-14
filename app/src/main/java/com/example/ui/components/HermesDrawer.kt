package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ConversationEntity
import com.example.ui.theme.*

@Composable
fun HermesDrawerContent(
    conversations: List<ConversationEntity>,
    pinnedConversations: List<ConversationEntity>,
    currentConversationId: String?,
    onSelectConversation: (String) -> Unit,
    onNewChat: () -> Unit,
    onNavigateToProjects: () -> Unit,
    onNavigateToTasks: () -> Unit,
    onNavigateToSkills: () -> Unit,
    onNavigateToMemory: () -> Unit,
    onNavigateToCode: () -> Unit,
    onNavigateToArtifacts: () -> Unit,
    onOpenProfileSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .width(320.dp)
            .testTag("hermes_drawer"),
        color = HermesBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            // Header: Serif brand title "Hermes" (matching screenshot 5)
            Text(
                text = "Hermes",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Normal
                ),
                color = HermesTextPrimary,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )

            // Primary Navigation Items
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                DrawerNavigationRow(
                    icon = Icons.AutoMirrored.Filled.Chat,
                    label = "Chats",
                    isSelected = true,
                    onClick = { /* Stay on chats */ }
                )
                DrawerNavigationRow(
                    icon = Icons.Default.Folder,
                    label = "Projects",
                    isSelected = false,
                    onClick = onNavigateToProjects
                )
                DrawerNavigationRow(
                    icon = Icons.Default.Code,
                    label = "Code",
                    isSelected = false,
                    onClick = onNavigateToCode
                )
                DrawerNavigationRow(
                    icon = Icons.Default.GridView,
                    label = "Artifacts",
                    isSelected = false,
                    onClick = onNavigateToArtifacts
                )
                DrawerNavigationRow(
                    icon = Icons.Default.CheckCircleOutline,
                    label = "Tasks",
                    isSelected = false,
                    onClick = onNavigateToTasks
                )
                DrawerNavigationRow(
                    icon = Icons.Default.Extension,
                    label = "Skills",
                    isSelected = false,
                    onClick = onNavigateToSkills
                )
                DrawerNavigationRow(
                    icon = Icons.Default.Psychology,
                    label = "Memory",
                    isSelected = false,
                    onClick = onNavigateToMemory
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = HermesDivider, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // Scrollable Pinned & Recents list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // Pinned Section
                if (pinnedConversations.isNotEmpty()) {
                    item {
                        Text(
                            text = "Pinned",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            color = HermesTextSecondary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    items(pinnedConversations) { conv ->
                        val isSelected = conv.id == currentConversationId
                        Text(
                            text = conv.title,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            ),
                            color = if (isSelected) HermesCoral else HermesTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onSelectConversation(conv.id) }
                                .padding(vertical = 10.dp, horizontal = 4.dp)
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // Recents Section
                item {
                    Text(
                        text = "Recents",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = HermesTextSecondary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(conversations.filter { !it.isPinned }) { conv ->
                    val isSelected = conv.id == currentConversationId
                    Text(
                        text = conv.title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 16.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        ),
                        color = if (isSelected) HermesCoral else HermesTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onSelectConversation(conv.id) }
                            .padding(vertical = 10.dp, horizontal = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Footer matching Screenshot 5: User Avatar ('J') + "+ New chat" Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User Avatar
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(HermesCoral)
                        .clickable { onOpenProfileSettings() }
                        .testTag("drawer_user_avatar"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "J",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // "+ New chat" Pill Button
                Button(
                    onClick = onNewChat,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                    modifier = Modifier.testTag("new_chat_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "New chat",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerNavigationRow(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) HermesSurfaceElevated.copy(alpha = 0.6f) else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) HermesTextPrimary else HermesTextSecondary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(18.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 17.sp,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            ),
            color = if (isSelected) HermesTextPrimary else HermesTextSecondary
        )
    }
}
