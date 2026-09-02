package com.example.ui.profile

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.PostEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.ui.components.EqualizerWave
import com.example.ui.components.VibeRingAvatar
import com.example.ui.components.getDrawableResByName
import com.example.ui.spotify.SpotifyBadgePill
import com.example.ui.spotify.SpotifyGreen
import com.example.ui.spotify.SpotifyPlaylistBottomSheet
import com.example.ui.spotify.SpotifyPlaylistLinkerDialog
import com.example.ui.theme.BorderGlass
import com.example.ui.theme.BorderSubtle
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
import com.example.ui.theme.VibeCardBg
import com.example.ui.theme.VibeSurface
import com.example.ui.theme.VibeSurfaceVariant

data class HighlightItem(val title: String, val drawable: String)

@Composable
fun ProfileScreen(
    profile: UserProfileEntity?,
    allPosts: List<PostEntity>,
    savedPosts: List<PostEntity>,
    likedPosts: List<PostEntity>,
    onEditProfileClick: () -> Unit,
    onPostClick: (PostEntity) -> Unit,
    onLogoutClick: () -> Unit = {},
    onLinkSpotifyPlaylist: (url: String, name: String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val user = profile ?: UserProfileEntity()
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Posts, 1: Reels, 2: Saved, 3: Liked
    var showSpotifyLinker by remember { mutableStateOf(false) }
    var showSpotifyBottomSheet by remember { mutableStateOf(false) }

    val myPosts = allPosts.filter { it.authorHandle == user.handle || it.postType == "PHOTO" }
    val myReels = allPosts.filter { it.postType == "REEL" }

    val displayedGrid = when (selectedTab) {
        0 -> myPosts
        1 -> myReels
        2 -> savedPosts
        3 -> likedPosts
        else -> myPosts
    }

    val highlights = listOf(
        HighlightItem("Tokyo 🗼", "img_vibe_sunset"),
        HighlightItem("Cyber ⚡", "img_reel_cyber_dance"),
        HighlightItem("Vibes 🎧", "img_vibe_concert"),
        HighlightItem("Coffee ☕", "img_reel_cafe_aesthetic"),
        HighlightItem("App 🔮", "img_app_icon")
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(bottom = 90.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Profile Info Header (Span 3 columns)
        item(span = { GridItemSpan(3) }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0x331E1B4B))
                    .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Top Row: Avatar + Stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        VibeRingAvatar(
                            drawableName = user.avatarDrawable,
                            size = 76.dp,
                            hasStory = true,
                            isSeen = false
                        )

                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ProfileStatColumn(count = "${allPosts.size}", label = "Posts")
                            ProfileStatColumn(count = "${user.followersCount / 1000}k", label = "Followers")
                            ProfileStatColumn(count = "${user.followingCount}", label = "Following")
                            ProfileStatColumn(count = "${user.totalVibesCount / 1000}k", label = "Vibes ⚡")
                        }
                    }

                    // Name & Bio
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = user.name,
                                color = TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified Creator",
                                tint = FrostedLavender,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Text(
                            text = "@${user.handle}",
                            color = FrostedLavender,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = user.bio,
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }

                    // Profile Anthem Ticker (Frosted)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x26FFFFFF))
                            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(14.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = "Anthem",
                            tint = FrostedLavender,
                            modifier = Modifier.size(18.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Profile Anthem", color = TextMuted, fontSize = 10.sp)
                            Text(
                                text = "${user.anthemTitle} • ${user.anthemArtist}",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                        EqualizerWave(color = FrostedLavender, modifier = Modifier.height(14.dp))
                    }

                    // Linked Spotify Playlist Card (Frosted)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (user.spotifyPlaylistUrl.isNotBlank()) Color(0x261DB954) else Color(0x1AFFFFFF)
                            )
                            .border(
                                1.dp,
                                if (user.spotifyPlaylistUrl.isNotBlank()) SpotifyGreen.copy(alpha = 0.5f) else Color(0x26FFFFFF),
                                RoundedCornerShape(14.dp)
                            )
                            .clickable {
                                if (user.spotifyPlaylistUrl.isNotBlank()) {
                                    showSpotifyBottomSheet = true
                                } else {
                                    showSpotifyLinker = true
                                }
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "🟢", fontSize = 14.sp)
                            Column {
                                Text(
                                    text = if (user.spotifyPlaylistUrl.isNotBlank()) "Linked Spotify Playlist" else "Spotify Playlist",
                                    color = if (user.spotifyPlaylistUrl.isNotBlank()) SpotifyGreen else TextMuted,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (user.spotifyPlaylistName.isNotBlank()) user.spotifyPlaylistName else "Tap to link a Spotify playlist...",
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1
                                )
                            }
                        }

                        Text(
                            text = if (user.spotifyPlaylistUrl.isNotBlank()) "Preview" else "+ Link",
                            color = if (user.spotifyPlaylistUrl.isNotBlank()) SpotifyGreen else FrostedLavender,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x33000000))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Action Buttons (Frosted)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onEditProfileClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(14.dp))
                                .testTag("edit_profile_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0x26FFFFFF)),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Edit", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = onLogoutClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(14.dp))
                                .testTag("switch_account_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0x1AFFFFFF))
                        ) {
                            Text("🔑 Switch / Log Out", color = FrostedLavender, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    // Story Highlights
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "Story Highlights", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(highlights) { hl ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .border(1.5.dp, Color(0x40FFFFFF), CircleShape)
                                            .background(Color(0x26FFFFFF))
                                            .padding(3.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = getDrawableResByName(hl.drawable)),
                                            contentDescription = hl.title,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(CircleShape)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = hl.title, color = TextSecondary, fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Profile Tab Switcher (Frosted)
        item(span = { GridItemSpan(3) }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x26FFFFFF))
                    .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(16.dp))
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = TextPrimary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = FrostedLavender,
                            height = 2.5.dp
                        )
                    },
                    divider = {}
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(imageVector = Icons.Default.GridOn, contentDescription = "Posts", tint = if (selectedTab == 0) FrostedLavender else TextMuted, modifier = Modifier.size(20.dp)) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(imageVector = Icons.Default.Videocam, contentDescription = "Reels", tint = if (selectedTab == 1) FrostedLavender else TextMuted, modifier = Modifier.size(20.dp)) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = { Icon(imageVector = Icons.Default.BookmarkBorder, contentDescription = "Saved", tint = if (selectedTab == 2) FrostedLavender else TextMuted, modifier = Modifier.size(20.dp)) }
                    )
                    Tab(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = { Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "Liked", tint = if (selectedTab == 3) FrostedLavender else TextMuted, modifier = Modifier.size(20.dp)) }
                    )
                }
            }
        }

        // Grid Content
        if (displayedGrid.isEmpty()) {
            item(span = { GridItemSpan(3) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "✨", fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "No items in this tab yet", color = TextSecondary, fontSize = 13.sp)
                    }
                }
            }
        } else {
            items(displayedGrid, key = { it.id }) { post ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .border(0.5.dp, Color(0x26FFFFFF), RoundedCornerShape(12.dp))
                        .clickable { onPostClick(post) }
                ) {
                    Image(
                        painter = painterResource(id = getDrawableResByName(post.mediaDrawableName)),
                        contentDescription = post.caption,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    if (post.postType == "REEL") {
                        Box(
                            modifier = Modifier
                                .padding(6.dp)
                                .align(Alignment.TopEnd)
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color(0x80000000))
                                .border(0.5.dp, Color(0x40FFFFFF), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showSpotifyLinker) {
        SpotifyPlaylistLinkerDialog(
            initialUrl = user.spotifyPlaylistUrl,
            onDismiss = { showSpotifyLinker = false },
            onPlaylistLinked = { url, name ->
                onLinkSpotifyPlaylist(url, name)
                showSpotifyLinker = false
            }
        )
    }

    if (showSpotifyBottomSheet && user.spotifyPlaylistUrl.isNotBlank()) {
        SpotifyPlaylistBottomSheet(
            playlistUrl = user.spotifyPlaylistUrl,
            playlistName = user.spotifyPlaylistName,
            onDismiss = { showSpotifyBottomSheet = false }
        )
    }
}

@Composable
fun ProfileStatColumn(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 11.sp
        )
    }
}
