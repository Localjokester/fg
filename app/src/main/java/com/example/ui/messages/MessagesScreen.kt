package com.example.ui.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.DirectMessageEntity
import com.example.ui.components.VibeRingAvatar
import com.example.ui.theme.BorderGlass
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.FrostedLavender
import com.example.ui.theme.HeartRed
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VibeBackground
import com.example.ui.theme.VibeCardBg
import com.example.ui.theme.VibeSurface
import com.example.ui.theme.VibeSurfaceVariant

data class ActivityItem(
    val username: String,
    val actionText: String,
    val timeAgo: String,
    val type: String, // "LIKE", "REMIX", "FOLLOW"
    val avatarDrawable: String
)

@Composable
fun MessagesScreen(
    messages: List<DirectMessageEntity>,
    onSendMessage: (recipientHandle: String, recipientName: String, text: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: DMs, 1: Activity Notifications
    var activeChatUser by remember { mutableStateOf<String?>(null) }
    var chatInput by remember { mutableStateOf("") }

    val notifications = listOf(
        ActivityItem("Kira Volt", "liked your vibe reel", "5m ago", "LIKE", "img_reel_cyber_dance"),
        ActivityItem("Maya Echo", "remixed your sound with a new dance", "22m ago", "REMIX", "img_vibe_concert"),
        ActivityItem("Leo Sun", "started following you", "1h ago", "FOLLOW", "img_vibe_sunset"),
        ActivityItem("Sora Nomad", "commented on your photo drop", "3h ago", "LIKE", "img_reel_cafe_aesthetic")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("messages_screen")
    ) {
        // Frosted Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x331E1B4B))
                .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (activeChatUser != null) {
                    activeChatUser = null
                } else {
                    onBack()
                }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }

            Text(
                text = if (activeChatUser != null) activeChatUser!! else "Vibe Lounge & Activity",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        if (activeChatUser == null) {
            // Tabs: Messages vs Activity (Frosted Container)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x26FFFFFF))
                    .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(20.dp))
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = TextPrimary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = FrostedLavender,
                            height = 3.dp
                        )
                    },
                    divider = {}
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                "Direct Messages",
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == 0) FrostedLavender else TextSecondary,
                                fontSize = 13.sp
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                "Activity & Alerts",
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == 1) FrostedLavender else TextSecondary,
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }

            if (selectedTab == 0) {
                // Messages List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            text = "Active Vibe Friends",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(listOf("Kira Volt", "Maya Echo", "Leo Sun", "Sora Nomad")) { name ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable { activeChatUser = name }
                                ) {
                                    VibeRingAvatar(
                                        drawableName = "img_app_icon",
                                        size = 52.dp,
                                        hasStory = true,
                                        isSeen = false
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = name, color = TextPrimary, fontSize = 10.sp, maxLines = 1)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Recent Chats",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(messages, key = { it.id }) { msg ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0x331E1B4B))
                                .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(18.dp))
                                .clickable { activeChatUser = msg.senderName }
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                VibeRingAvatar(
                                    drawableName = "img_app_icon",
                                    size = 46.dp,
                                    hasStory = false
                                )

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = msg.senderName,
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = msg.messageText,
                                        color = TextSecondary,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                if (msg.unread) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(FrostedLavender)
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Activity Notifications (Frosted)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(notifications) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0x331E1B4B))
                                .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(16.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            VibeRingAvatar(
                                drawableName = item.avatarDrawable,
                                size = 44.dp,
                                hasStory = false
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Row {
                                    Text(text = item.username, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp)
                                    Text(text = " ${item.actionText}", color = TextSecondary, fontSize = 13.sp)
                                }
                                Text(text = item.timeAgo, color = TextMuted, fontSize = 11.sp)
                            }

                            Icon(
                                imageVector = when (item.type) {
                                    "LIKE" -> Icons.Default.Favorite
                                    "REMIX" -> Icons.Default.Repeat
                                    else -> Icons.Default.Notifications
                                },
                                contentDescription = null,
                                tint = if (item.type == "LIKE") HeartRed else FrostedLavender,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        } else {
            // Live Chat Bubble Screen
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🔒 End-to-End Vibe Encrypted Chat with $activeChatUser",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    items(messages.filter { it.senderName == activeChatUser || it.isFromMe }) { msg ->
                        val isMe = msg.isFromMe
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 18.dp,
                                            topEnd = 18.dp,
                                            bottomStart = if (isMe) 18.dp else 4.dp,
                                            bottomEnd = if (isMe) 4.dp else 18.dp
                                        )
                                    )
                                    .background(
                                        if (isMe) Color(0x66D0BCFF)
                                        else Color(0x33FFFFFF)
                                    )
                                    .border(
                                        1.dp,
                                        if (isMe) FrostedLavender else Color(0x26FFFFFF),
                                        RoundedCornerShape(
                                            topStart = 18.dp,
                                            topEnd = 18.dp,
                                            bottomStart = if (isMe) 18.dp else 4.dp,
                                            bottomEnd = if (isMe) 4.dp else 18.dp
                                        )
                                    )
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = msg.messageText,
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                // Chat Input Bar (Frosted)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x331E1B4B))
                        .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .navigationBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = chatInput,
                        onValueChange = { chatInput = it },
                        placeholder = { Text("Message $activeChatUser...", color = TextMuted, fontSize = 13.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field"),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0x26FFFFFF),
                            unfocusedContainerColor = Color(0x1AFFFFFF),
                            focusedBorderColor = FrostedLavender,
                            unfocusedBorderColor = Color(0x26FFFFFF),
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )

                    IconButton(
                        onClick = {
                            if (chatInput.isNotBlank()) {
                                onSendMessage(
                                    activeChatUser?.lowercase()?.replace(" ", "") ?: "friend",
                                    activeChatUser ?: "Friend",
                                    chatInput
                                )
                                chatInput = ""
                            }
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(FrostedLavender)
                            .testTag("send_chat_button")
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = Color.Black, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}
