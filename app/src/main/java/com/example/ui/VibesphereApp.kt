package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.data.local.entity.PostEntity
import com.example.ui.components.VibeRingAvatar
import com.example.ui.create.CreateVibeScreen
import com.example.ui.explore.ExploreScreen
import com.example.ui.feed.FeedScreen
import com.example.ui.messages.MessagesScreen
import com.example.ui.profile.ProfileScreen
import com.example.ui.sheets.CommentsBottomSheet
import com.example.ui.sheets.EditProfileBottomSheet
import com.example.ui.sheets.ShareBottomSheet
import com.example.ui.sheets.SoundDetailBottomSheet
import com.example.ui.story.StoryViewerDialog
import com.example.ui.theme.BorderGlass
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.FrostedBackdropGradient
import com.example.ui.theme.FrostedLavender
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VibeBackground
import com.example.ui.theme.VibeSurface
import com.example.ui.theme.VibeSurfaceVariant
import com.example.ui.vibe_reels.VibeReelsScreen
import com.example.ui.viewmodel.VibeViewModel
import kotlinx.coroutines.launch

enum class VibeNavTab(val title: String, val iconFilled: androidx.compose.ui.graphics.vector.ImageVector, val iconOutlined: androidx.compose.ui.graphics.vector.ImageVector) {
    FEED("Feed", Icons.Filled.Home, Icons.Outlined.Home),
    EXPLORE("Explore", Icons.Filled.Explore, Icons.Outlined.Explore),
    CREATE("Create", Icons.Filled.Add, Icons.Filled.Add),
    REELS("Vibes", Icons.Filled.PlayCircleOutline, Icons.Outlined.PlayCircle),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun VibesphereApp(
    viewModel: VibeViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(VibeNavTab.FEED) }
    var showMessagesScreen by remember { mutableStateOf(false) }
    var showEditProfileSheet by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val allPosts by viewModel.allPosts.collectAsState()
    val reels by viewModel.reels.collectAsState()
    val savedPosts by viewModel.savedPosts.collectAsState()
    val likedPosts by viewModel.likedPosts.collectAsState()
    val stories by viewModel.stories.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val trendingSounds by viewModel.trendingSounds.collectAsState()
    val directMessages by viewModel.directMessages.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedTopicTag by viewModel.selectedTopicTag.collectAsState()
    val filteredExplorePosts by viewModel.filteredExplorePosts.collectAsState()

    val activeCommentsPost by viewModel.activeCommentsPost.collectAsState()
    val postComments by viewModel.postComments.collectAsState()
    val activeStory by viewModel.activeStory.collectAsState()
    val activeSoundSheet by viewModel.activeSoundSheet.collectAsState()
    val shareTargetPost by viewModel.shareTargetPost.collectAsState()

    val unreadCount = directMessages.count { it.unread }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VibeBackground)
    ) {
        // Frosted Glass Ambient Gradient Backdrop
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FrostedBackdropGradient)
        )

        if (showMessagesScreen) {
            MessagesScreen(
                messages = directMessages,
                onSendMessage = { handle, name, text ->
                    viewModel.sendDirectMessage(handle, name, text)
                },
                onBack = { showMessagesScreen = false }
            )
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = Color.Transparent,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    if (selectedTab != VibeNavTab.REELS) {
                        VibesphereTopBar(
                            unreadCount = unreadCount,
                            onCameraClick = { selectedTab = VibeNavTab.CREATE },
                            onMessagesClick = { showMessagesScreen = true }
                        )
                    }
                },
                bottomBar = {
                    VibesphereBottomNav(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it },
                        modifier = Modifier.navigationBarsPadding()
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = if (selectedTab == VibeNavTab.REELS) 0.dp else innerPadding.calculateTopPadding(),
                            bottom = 0.dp
                        )
                ) {
                    Crossfade(targetState = selectedTab, label = "tab_crossfade") { tab ->
                        when (tab) {
                            VibeNavTab.FEED -> {
                                FeedScreen(
                                    posts = allPosts,
                                    stories = stories,
                                    onStoryClick = { viewModel.openStory(it) },
                                    onAddStoryClick = { selectedTab = VibeNavTab.CREATE },
                                    onToggleLike = { id, liked -> viewModel.toggleLike(id, liked) },
                                    onToggleSave = { id, saved -> viewModel.toggleSave(id, saved) },
                                    onOpenComments = { viewModel.openComments(it) },
                                    onOpenShare = { viewModel.openShare(it) },
                                    onSoundClick = { viewModel.openSoundSheet(it) },
                                    onDirectComment = { post, comment ->
                                        viewModel.openComments(post)
                                        viewModel.sendComment(comment)
                                    }
                                )
                            }
                            VibeNavTab.REELS -> {
                                VibeReelsScreen(
                                    reels = reels,
                                    onToggleLike = { id, liked -> viewModel.toggleLike(id, liked) },
                                    onToggleSave = { id, saved -> viewModel.toggleSave(id, saved) },
                                    onOpenComments = { viewModel.openComments(it) },
                                    onOpenShare = { viewModel.openShare(it) },
                                    onSoundClick = { viewModel.openSoundSheet(it) }
                                )
                            }
                            VibeNavTab.CREATE -> {
                                CreateVibeScreen(
                                    trendingSounds = trendingSounds,
                                    onPublishPost = { caption, hashtags, postType, mediaDrawable, soundTitle, soundArtist, location, filterName ->
                                        viewModel.createNewPost(caption, hashtags, postType, mediaDrawable, soundTitle, soundArtist, location, filterName)
                                        scope.launch {
                                            snackbarHostState.showSnackbar("✨ Your Vibe was published successfully!")
                                        }
                                        selectedTab = if (postType == "REEL") VibeNavTab.REELS else VibeNavTab.FEED
                                    },
                                    onPublishStory = { caption, mediaDrawable ->
                                        viewModel.createNewStory(caption, mediaDrawable)
                                        scope.launch {
                                            snackbarHostState.showSnackbar("✨ Your Story is now live!")
                                        }
                                        selectedTab = VibeNavTab.FEED
                                    }
                                )
                            }
                            VibeNavTab.EXPLORE -> {
                                ExploreScreen(
                                    posts = filteredExplorePosts,
                                    trendingSounds = trendingSounds,
                                    searchQuery = searchQuery,
                                    selectedTag = selectedTopicTag,
                                    onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                    onTagSelect = { viewModel.selectTopicTag(it) },
                                    onPostClick = { viewModel.openComments(it) },
                                    onSoundClick = { viewModel.openSoundSheet(it) }
                                )
                            }
                            VibeNavTab.PROFILE -> {
                                ProfileScreen(
                                    profile = userProfile,
                                    allPosts = allPosts,
                                    savedPosts = savedPosts,
                                    likedPosts = likedPosts,
                                    onEditProfileClick = { showEditProfileSheet = true },
                                    onPostClick = { viewModel.openComments(it) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Story Viewer Fullscreen
        activeStory?.let { story ->
            StoryViewerDialog(
                story = story,
                onDismiss = { viewModel.closeStory() },
                onNextStory = { viewModel.closeStory() }
            )
        }

        // Comments Bottom Sheet
        activeCommentsPost?.let { post ->
            CommentsBottomSheet(
                post = post,
                comments = postComments,
                onDismiss = { viewModel.closeComments() },
                onSendComment = { viewModel.sendComment(it) },
                onToggleCommentLike = { id, liked -> viewModel.toggleCommentLike(id, liked) }
            )
        }

        // Sound Detail Bottom Sheet
        activeSoundSheet?.let { sound ->
            SoundDetailBottomSheet(
                sound = sound,
                onDismiss = { viewModel.closeSoundSheet() },
                onUseSound = {
                    viewModel.closeSoundSheet()
                    selectedTab = VibeNavTab.CREATE
                }
            )
        }

        // Share Bottom Sheet
        shareTargetPost?.let { post ->
            ShareBottomSheet(
                post = post,
                onDismiss = { viewModel.closeShare() }
            )
        }

        // Edit Profile Sheet
        if (showEditProfileSheet && userProfile != null) {
            EditProfileBottomSheet(
                profile = userProfile!!,
                onDismiss = { showEditProfileSheet = false },
                onSave = { name, handle, bio, anthem ->
                    viewModel.updateBio(bio, name, handle, anthem)
                    scope.launch {
                        snackbarHostState.showSnackbar("Profile updated!")
                    }
                }
            )
        }
    }
}

/**
 * Frosted Glass Top App Bar with Translucent Pill Action Buttons
 */
@Composable
fun VibesphereTopBar(
    unreadCount: Int,
    onCameraClick: () -> Unit,
    onMessagesClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0x660F0F0F))
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Brand Title with Gradient
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(FrostedLavender, NeonPurple, NeonCyan)
                        )
                    )
                    .border(1.dp, Color(0x66FFFFFF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_app_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
            }

            Text(
                text = "Vibesphere",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp,
                color = TextPrimary
            )
        }

        // Frosted Glass Action Buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Camera / Search shortcut
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0x26FFFFFF))
                    .border(1.dp, Color(0x33FFFFFF), CircleShape)
                    .clickable(onClick = onCameraClick)
                    .testTag("top_bar_camera_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            // DM / Notification Capsule with Lavender Pip
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0x26FFFFFF))
                    .border(1.dp, Color(0x33FFFFFF), CircleShape)
                    .clickable(onClick = onMessagesClick)
                    .testTag("top_bar_dm_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.NearMe,
                    contentDescription = "Direct Messages",
                    tint = TextPrimary,
                    modifier = Modifier.size(20.dp)
                )
                if (unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 8.dp, end = 8.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(FrostedLavender)
                    )
                }
            }
        }
    }
}

/**
 * Frosted Glass Floating Pill Bottom Navigation Bar
 */
@Composable
fun VibesphereBottomNav(
    selectedTab: VibeNavTab,
    onTabSelected: (VibeNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        // Floating Frosted Pill
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xCC2B2930))
                .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(32.dp))
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            VibeNavTab.entries.forEach { tab ->
                val isSelected = selectedTab == tab
                if (tab == VibeNavTab.CREATE) {
                    // Floating Elevated Center White Create Button (as in Design HTML)
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .offset(y = (-8).dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(4.dp, Color(0xFF1C1B1F), CircleShape)
                            .clickable { onTabSelected(tab) }
                            .testTag("nav_create_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create",
                            tint = Color.Black,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onTabSelected(tab) }
                            .padding(vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = if (isSelected) tab.iconFilled else tab.iconOutlined,
                            contentDescription = tab.title,
                            tint = if (isSelected) FrostedLavender else Color(0x80FFFFFF),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .clip(CircleShape)
                                    .background(FrostedLavender)
                            )
                        } else {
                            Spacer(modifier = Modifier.size(4.dp))
                        }
                    }
                }
            }
        }
    }
}
