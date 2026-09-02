package com.example.ui.messages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.DirectMessageEntity
import com.example.ui.components.VibeRingAvatar
import com.example.ui.components.getDrawableResByName
import com.example.ui.theme.FrostedLavender
import com.example.ui.theme.HeartRed
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGold
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VibeBackground

data class ChatThemePreset(
    val id: String,
    val name: String,
    val accentColor: Color,
    val bubbleGradient: Brush,
    val backgroundGradient: Brush,
    val iconEmoji: String
)

val ChatThemePresets = listOf(
    ChatThemePreset(
        id = "cyber_lavender",
        name = "Cyber Lavender",
        accentColor = FrostedLavender,
        bubbleGradient = Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFFD0BCFF))),
        backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF0F0C20), Color(0xFF1E1B4B))),
        iconEmoji = "🔮"
    ),
    ChatThemePreset(
        id = "neon_magenta",
        name = "Neon Magenta",
        accentColor = NeonMagenta,
        bubbleGradient = Brush.horizontalGradient(listOf(Color(0xFFFF007F), Color(0xFFFF77A9))),
        backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF1A051B), Color(0xFF2C0A2D))),
        iconEmoji = "🌸"
    ),
    ChatThemePreset(
        id = "arctic_cyan",
        name = "Arctic Cyan",
        accentColor = NeonCyan,
        bubbleGradient = Brush.horizontalGradient(listOf(Color(0xFF00B4D8), Color(0xFF00E5FF))),
        backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF031926), Color(0xFF0A2E44))),
        iconEmoji = "🌊"
    ),
    ChatThemePreset(
        id = "golden_sunset",
        name = "Golden Sunset",
        accentColor = NeonGold,
        bubbleGradient = Brush.horizontalGradient(listOf(Color(0xFFFF8C00), Color(0xFFFFD700))),
        backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF201205), Color(0xFF38200A))),
        iconEmoji = "☀️"
    ),
    ChatThemePreset(
        id = "midnight_obsidian",
        name = "Midnight Obsidian",
        accentColor = Color(0xFFE2E8F0),
        bubbleGradient = Brush.horizontalGradient(listOf(Color(0xFF334155), Color(0xFF475569))),
        backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF0B0F17), Color(0xFF141A24))),
        iconEmoji = "🖤"
    ),
    ChatThemePreset(
        id = "emerald_matrix",
        name = "Emerald Matrix",
        accentColor = Color(0xFF00FF88),
        bubbleGradient = Brush.horizontalGradient(listOf(Color(0xFF00B862), Color(0xFF00FF88))),
        backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF041C12), Color(0xFF082D1D))),
        iconEmoji = "🌿"
    )
)

data class VibeFriendItem(
    val name: String,
    val handle: String,
    val avatarDrawable: String,
    val statusText: String = "Online",
    val hasStory: Boolean = true
)

data class ActivityItem(
    val username: String,
    val actionText: String,
    val timeAgo: String,
    val type: String,
    val avatarDrawable: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    messages: List<DirectMessageEntity>,
    onSendMessage: (recipientHandle: String, recipientName: String, text: String, isDisappearing: Boolean, expireMinutes: Int) -> Unit,
    onSetReaction: (messageId: Long, reaction: String) -> Unit = { _, _ -> },
    onDeleteMessage: (messageId: Long) -> Unit = { _ -> },
    onClearChat: (handle: String, name: String) -> Unit = { _, _ -> },
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Chats, 1: Activity
    var activeChatUser by remember { mutableStateOf<VibeFriendItem?>(null) }
    var chatInput by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    // Chat Settings State (Disappearing Messages & Theme)
    var isDisappearingOn by remember { mutableStateOf(false) }
    var selectedExpireMinutes by remember { mutableIntStateOf(5) } // 5m default
    var selectedTheme by remember { mutableStateOf(ChatThemePresets[0]) }

    var showThemeSheet by remember { mutableStateOf(false) }
    var showDisappearingSheet by remember { mutableStateOf(false) }
    var showOptionsMenu by remember { mutableStateOf(false) }
    var reactingMessageId by remember { mutableStateOf<Long?>(null) }

    val friendsList = remember {
        listOf(
            VibeFriendItem("Kira Volt", "kira.volt", "img_reel_cyber_dance", "Streaming Live ⚡"),
            VibeFriendItem("Maya Echo", "maya.beats", "img_vibe_concert", "Listening to Synthwave 🎧"),
            VibeFriendItem("Leo Sun", "leo_aesthetic", "img_reel_cafe_aesthetic", "Coffee & Chill ☕"),
            VibeFriendItem("Sora Nomad", "sora.nomad", "img_vibe_sunset", "Exploring Tokyo 🌆"),
            VibeFriendItem("Pixel Drift", "pixel.drift", "img_app_icon", "In Creative Flow 🎨")
        )
    }

    val notifications = remember {
        listOf(
            ActivityItem("Kira Volt", "liked your vibe reel", "2m ago", "LIKE", "img_reel_cyber_dance"),
            ActivityItem("Maya Echo", "remixed your sound with a new rhythm", "15m ago", "REMIX", "img_vibe_concert"),
            ActivityItem("Leo Sun", "started following your frequency", "45m ago", "FOLLOW", "img_vibe_sunset"),
            ActivityItem("Sora Nomad", "dropped a comment on your sunset drop", "2h ago", "COMMENT", "img_reel_cafe_aesthetic")
        )
    }

    val filteredFriends = remember(searchQuery, friendsList) {
        if (searchQuery.isBlank()) friendsList
        else friendsList.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.handle.contains(searchQuery, ignoreCase = true)
        }
    }

    // Main Layout Container
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (activeChatUser != null) Color.Transparent else VibeBackground)
            .testTag("messages_screen")
    ) {
        if (activeChatUser != null) {
            // Apply Selected Chat Theme Background Gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(selectedTheme.backgroundGradient)
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // Top Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x401E1B4B))
                    .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
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

                if (activeChatUser != null) {
                    // Chat User Info
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { /* Profile preview */ },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box {
                            VibeRingAvatar(
                                drawableName = activeChatUser!!.avatarDrawable,
                                size = 38.dp,
                                hasStory = activeChatUser!!.hasStory,
                                isSeen = false
                            )
                            // Online Green Dot
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .align(Alignment.BottomEnd)
                                    .clip(CircleShape)
                                    .background(Color(0xFF00FF88))
                                    .border(1.5.dp, Color(0xFF1E1B4B), CircleShape)
                            )
                        }

                        Column {
                            Text(
                                text = activeChatUser!!.name,
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = activeChatUser!!.statusText,
                                color = selectedTheme.accentColor,
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        }
                    }

                    // Disappearing Messages Quick Toggle Button
                    IconButton(
                        onClick = { showDisappearingSheet = true },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (isDisappearingOn) selectedTheme.accentColor.copy(alpha = 0.25f)
                                else Color(0x1AFFFFFF)
                            )
                            .testTag("disappearing_messages_toggle")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Disappearing Messages",
                            tint = if (isDisappearingOn) selectedTheme.accentColor else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Change Chat Theme Button
                    IconButton(
                        onClick = { showThemeSheet = true },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0x1AFFFFFF))
                            .testTag("chat_theme_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Change Theme",
                            tint = selectedTheme.accentColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // More Options Dropdown
                    Box {
                        IconButton(onClick = { showOptionsMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Options",
                                tint = TextSecondary
                            )
                        }

                        DropdownMenu(
                            expanded = showOptionsMenu,
                            onDismissRequest = { showOptionsMenu = false },
                            modifier = Modifier
                                .background(Color(0xFF1E1B4B))
                                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(12.dp))
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Default.Palette, contentDescription = null, tint = selectedTheme.accentColor, modifier = Modifier.size(16.dp))
                                        Text("Change Theme (${selectedTheme.name})", color = TextPrimary, fontSize = 13.sp)
                                    }
                                },
                                onClick = {
                                    showOptionsMenu = false
                                    showThemeSheet = true
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Default.Timer, contentDescription = null, tint = selectedTheme.accentColor, modifier = Modifier.size(16.dp))
                                        Text(
                                            if (isDisappearingOn) "Disappearing Messages ($selectedExpireMinutes min)" else "Enable Disappearing Messages",
                                            color = TextPrimary,
                                            fontSize = 13.sp
                                        )
                                    }
                                },
                                onClick = {
                                    showOptionsMenu = false
                                    showDisappearingSheet = true
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Default.ClearAll, contentDescription = null, tint = HeartRed, modifier = Modifier.size(16.dp))
                                        Text("Clear Chat History", color = HeartRed, fontSize = 13.sp)
                                    }
                                },
                                onClick = {
                                    showOptionsMenu = false
                                    activeChatUser?.let { onClearChat(it.handle, it.name) }
                                }
                            )
                        }
                    }
                } else {
                    // Messages Hub Header
                    Text(
                        text = "Vibe Lounge & Chats",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    )
                }
            }

            // Screen Content
            if (activeChatUser == null) {
                // MESSAGES / ACTIVITY OVERVIEW
                Column(modifier = Modifier.fillMaxSize()) {
                    // Search Field
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search friends or messages...", color = TextMuted, fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("messages_search_bar"),
                        shape = RoundedCornerShape(20.dp),
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

                    // Tabs: Direct Messages vs Activity Alerts
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
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
                        // CHATS LIST
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(top = 8.dp, bottom = 90.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Active Vibe Friends Top Row
                            item {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = "Active Vibe Frequency",
                                        color = TextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                                        contentPadding = PaddingValues(vertical = 4.dp)
                                    ) {
                                        items(friendsList) { friend ->
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier
                                                    .clickable { activeChatUser = friend }
                                                    .testTag("friend_avatar_${friend.handle}")
                                            ) {
                                                Box {
                                                    VibeRingAvatar(
                                                        drawableName = friend.avatarDrawable,
                                                        size = 54.dp,
                                                        hasStory = friend.hasStory,
                                                        isSeen = false
                                                    )
                                                    Box(
                                                        modifier = Modifier
                                                            .size(12.dp)
                                                            .align(Alignment.BottomEnd)
                                                            .clip(CircleShape)
                                                            .background(Color(0xFF00FF88))
                                                            .border(1.5.dp, Color(0xFF1E1B4B), CircleShape)
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = friend.name,
                                                    color = TextPrimary,
                                                    fontSize = 11.sp,
                                                    maxLines = 1,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Recent Conversations",
                                        color = TextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Conversation Cards
                            items(filteredFriends) { friend ->
                                val friendMessages = messages.filter {
                                    it.senderHandle == friend.handle || it.senderName == friend.name
                                }
                                val lastMessage = friendMessages.lastOrNull()?.messageText
                                    ?: "Tapped into your vibe frequency ✨"

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(Color(0x331E1B4B))
                                        .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(18.dp))
                                        .clickable { activeChatUser = friend }
                                        .padding(12.dp)
                                        .testTag("chat_card_${friend.handle}")
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Box {
                                            VibeRingAvatar(
                                                drawableName = friend.avatarDrawable,
                                                size = 48.dp,
                                                hasStory = friend.hasStory,
                                                isSeen = true
                                            )
                                        }

                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = friend.name,
                                                    color = TextPrimary,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = "Just now",
                                                    color = TextMuted,
                                                    fontSize = 11.sp
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = lastMessage,
                                                color = TextSecondary,
                                                fontSize = 12.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        // Status or Unread Indicator
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
                    } else {
                        // ACTIVITY & NOTIFICATIONS TAB
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(top = 8.dp, bottom = 90.dp),
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
                                            Text(
                                                text = item.username,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary,
                                                fontSize = 13.sp
                                            )
                                            Text(
                                                text = " ${item.actionText}",
                                                color = TextSecondary,
                                                fontSize = 13.sp
                                            )
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
                }
            } else {
                // ACTIVE 1-ON-1 CHAT SCREEN
                val currentFriend = activeChatUser!!
                val chatMessages = messages.filter {
                    it.senderHandle == currentFriend.handle ||
                    it.senderName == currentFriend.name ||
                    it.isFromMe
                }
                val listState = rememberLazyListState()

                LaunchedEffect(chatMessages.size) {
                    if (chatMessages.isNotEmpty()) {
                        listState.animateScrollToItem(chatMessages.size)
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                ) {
                    // Disappearing Messages Banner (if enabled)
                    AnimatedVisibility(visible = isDisappearingOn) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(selectedTheme.accentColor.copy(alpha = 0.18f))
                                .border(1.dp, selectedTheme.accentColor.copy(alpha = 0.3f))
                                .padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = selectedTheme.accentColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "🔥 Disappearing Messages ON: Vanishes in $selectedExpireMinutes min",
                                color = selectedTheme.accentColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Message Timeline
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Encryption / Privacy Header
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0x26000000))
                                        .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(12.dp))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "🔒 Vibe Encrypted Lounge • Theme: ${selectedTheme.iconEmoji} ${selectedTheme.name}",
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        // Messages Bubbles
                        items(chatMessages, key = { it.id }) { msg ->
                            val isMe = msg.isFromMe

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
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
                                            if (isMe) selectedTheme.bubbleGradient
                                            else Brush.linearGradient(listOf(Color(0x40FFFFFF), Color(0x2AFFFFFF)))
                                        )
                                        .border(
                                            1.dp,
                                            if (isMe) selectedTheme.accentColor.copy(alpha = 0.5f)
                                            else Color(0x33FFFFFF),
                                            RoundedCornerShape(
                                                topStart = 18.dp,
                                                topEnd = 18.dp,
                                                bottomStart = if (isMe) 18.dp else 4.dp,
                                                bottomEnd = if (isMe) 4.dp else 18.dp
                                            )
                                        )
                                        .clickable {
                                            reactingMessageId = if (reactingMessageId == msg.id) null else msg.id
                                        }
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            text = msg.messageText,
                                            color = if (isMe) Color.White else TextPrimary,
                                            fontSize = 14.sp
                                        )

                                        // Disappearing Timer or Reaction Badges
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            if (msg.isDisappearing) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(Color(0x33000000))
                                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Timer,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(10.dp)
                                                    )
                                                    Text(
                                                        text = "${msg.expireMinutes}m",
                                                        color = Color.White,
                                                        fontSize = 9.sp
                                                    )
                                                }
                                            }

                                            if (msg.reactionEmoji.isNotBlank()) {
                                                Text(
                                                    text = msg.reactionEmoji,
                                                    fontSize = 12.sp,
                                                    modifier = Modifier
                                                        .clip(CircleShape)
                                                        .background(Color(0x40000000))
                                                        .padding(2.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                // Quick Reaction bar when tapped
                                AnimatedVisibility(visible = reactingMessageId == msg.id) {
                                    Row(
                                        modifier = Modifier
                                            .padding(top = 4.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Color(0x66000000))
                                            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(16.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        listOf("❤️", "🔥", "😂", "😮", "👏", "⚡").forEach { emoji ->
                                            Text(
                                                text = emoji,
                                                fontSize = 16.sp,
                                                modifier = Modifier
                                                    .clickable {
                                                        onSetReaction(msg.id, emoji)
                                                        reactingMessageId = null
                                                    }
                                                    .padding(2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Quick Vibe Phrase Bar
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(listOf("That's so fire! 🔥", "Sending good vibes ✨", "Let's collab! 🚀", "Obsessed with that sound 🎧", "On my way! 🏃💨")) { phrase ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0x26FFFFFF))
                                    .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(14.dp))
                                    .clickable {
                                        onSendMessage(
                                            currentFriend.handle,
                                            currentFriend.name,
                                            phrase,
                                            isDisappearingOn,
                                            if (isDisappearingOn) selectedExpireMinutes else 0
                                        )
                                    }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(text = phrase, color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                    }

                    // Bottom Chat Input Bar (Frosted)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0x401E1B4B))
                            .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                            .navigationBarsPadding()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = chatInput,
                            onValueChange = { chatInput = it },
                            placeholder = {
                                Text(
                                    if (isDisappearingOn) "Disappearing message ($selectedExpireMinutes min)..." else "Message ${currentFriend.name}...",
                                    color = TextMuted,
                                    fontSize = 13.sp
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("chat_input_field"),
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0x26FFFFFF),
                                unfocusedContainerColor = Color(0x1AFFFFFF),
                                focusedBorderColor = selectedTheme.accentColor,
                                unfocusedBorderColor = Color(0x26FFFFFF),
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            singleLine = true
                        )

                        // Voice Aura note simulator
                        IconButton(
                            onClick = {
                                onSendMessage(
                                    currentFriend.handle,
                                    currentFriend.name,
                                    "🎙️ [Voice Aura Note • 0:14 🎧]",
                                    isDisappearingOn,
                                    if (isDisappearingOn) selectedExpireMinutes else 0
                                )
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0x26FFFFFF))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Voice Note",
                                tint = selectedTheme.accentColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Send Button
                        IconButton(
                            onClick = {
                                if (chatInput.isNotBlank()) {
                                    onSendMessage(
                                        currentFriend.handle,
                                        currentFriend.name,
                                        chatInput,
                                        isDisappearingOn,
                                        if (isDisappearingOn) selectedExpireMinutes else 0
                                    )
                                    chatInput = ""
                                }
                            },
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(selectedTheme.bubbleGradient)
                                .testTag("send_chat_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // DISAPPEARING MESSAGES MODAL SHEET
        if (showDisappearingSheet) {
            ModalBottomSheet(
                onDismissRequest = { showDisappearingSheet = false },
                containerColor = Color(0xFF1E1B4B),
                scrimColor = Color.Black.copy(alpha = 0.6f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = selectedTheme.accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "Disappearing Messages",
                                color = TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Messages automatically vanish after the chosen timer expires.",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    val timerOptions = listOf(
                        Triple("Off", 0, "Messages stay permanent"),
                        Triple("10 Seconds (Fast Burn)", 0, "Disappears instantly for quick secret drops 🔥"),
                        Triple("5 Minutes", 5, "Great for casual vibe sessions ⚡"),
                        Triple("1 Hour", 60, "Temporary session privacy ⏳"),
                        Triple("24 Hours", 1440, "Standard daily vanish 🌙")
                    )

                    timerOptions.forEach { (label, minutes, desc) ->
                        val isSelected = if (label == "Off") !isDisappearingOn else (isDisappearingOn && selectedExpireMinutes == minutes)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) selectedTheme.accentColor.copy(alpha = 0.25f) else Color(0x1AFFFFFF))
                                .border(1.dp, if (isSelected) selectedTheme.accentColor else Color(0x26FFFFFF), RoundedCornerShape(16.dp))
                                .clickable {
                                    if (label == "Off") {
                                        isDisappearingOn = false
                                    } else {
                                        isDisappearingOn = true
                                        selectedExpireMinutes = if (minutes == 0) 1 else minutes
                                    }
                                    showDisappearingSheet = false
                                }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = label,
                                    color = if (isSelected) selectedTheme.accentColor else TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = desc,
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = selectedTheme.accentColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }

        // CHAT THEME SELECTOR MODAL SHEET
        if (showThemeSheet) {
            ModalBottomSheet(
                onDismissRequest = { showThemeSheet = false },
                containerColor = Color(0xFF1E1B4B),
                scrimColor = Color.Black.copy(alpha = 0.6f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = null,
                            tint = selectedTheme.accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "Customize Chat Theme",
                                color = TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Change colors, glows, and bubble gradients for this chat.",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(ChatThemePresets) { theme ->
                            val isSelected = theme.id == selectedTheme.id

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) theme.accentColor.copy(alpha = 0.25f) else Color(0x1AFFFFFF))
                                    .border(1.dp, if (isSelected) theme.accentColor else Color(0x26FFFFFF), RoundedCornerShape(16.dp))
                                    .clickable {
                                        selectedTheme = theme
                                        showThemeSheet = false
                                    }
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(theme.bubbleGradient)
                                        .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = theme.iconEmoji, fontSize = 16.sp)
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = theme.name,
                                        color = if (isSelected) theme.accentColor else TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Aesthetic Vibe Frequency",
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = theme.accentColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
