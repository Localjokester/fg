package com.example.ui.create

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.SoundTrackEntity
import com.example.ui.components.EqualizerWave
import com.example.ui.components.getDrawableResByName
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

data class FilterPreset(
    val name: String,
    val overlayColor: Color,
    val description: String
)

@Composable
fun CreateVibeScreen(
    trendingSounds: List<SoundTrackEntity>,
    onPublishPost: (
        caption: String,
        hashtags: String,
        postType: String,
        mediaDrawable: String,
        soundTitle: String,
        soundArtist: String,
        location: String,
        filterName: String
    ) -> Unit,
    onPublishStory: (caption: String, mediaDrawable: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Reel, 1: Post, 2: Story
    val tabs = listOf("🎬 Vibe Reel", "📸 Photo Post", "✨ Story")

    val availableMedia = listOf(
        "img_reel_cyber_dance",
        "img_vibe_sunset",
        "img_reel_cafe_aesthetic",
        "img_vibe_concert",
        "img_app_icon"
    )
    var selectedMedia by remember { mutableStateOf(availableMedia[0]) }

    val filters = listOf(
        FilterPreset("Cyber Neon", Color(0x33D0BCFF), "Electric Lavender"),
        FilterPreset("Golden Glow", Color(0x33FFAA00), "Warm Sunlight"),
        FilterPreset("Tokyo Noir", Color(0x3300E5FF), "Moody Contrast"),
        FilterPreset("VHS Glitch", Color(0x338F00FF), "Retro Synthwave"),
        FilterPreset("Pure Normal", Color.Transparent, "Natural Tone")
    )
    var selectedFilter by remember { mutableStateOf(filters[0]) }

    var caption by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Tokyo, Shibuya") }
    var selectedSound by remember { mutableStateOf(if (trendingSounds.isNotEmpty()) trendingSounds[0] else SoundTrackEntity(title = "Cybernetic Pulse", artist = "Kira Volt")) }
    var showSoundPicker by remember { mutableStateOf(false) }

    val quickTags = listOf("#Vibesphere", "#Cyberpunk", "#DanceReels", "#Aesthetic", "#GoldenHour", "#TokyoNights", "#EDMFestival")

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .navigationBarsPadding()
            .padding(bottom = 90.dp)
            .testTag("create_vibe_screen")
    ) {
        // Creator Mode Tabs (Frosted Container)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
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
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == index) FrostedLavender else TextSecondary,
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Live Visual Studio Canvas Preview (Frosted Border)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0x331E1B4B))
                .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(24.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(if (selectedTab == 0) 9f / 12f else if (selectedTab == 2) 9f / 14f else 4f / 3f)
            ) {
                // Base selected image
                Image(
                    painter = painterResource(id = getDrawableResByName(selectedMedia)),
                    contentDescription = "Studio Preview",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Live Filter Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(selectedFilter.overlayColor)
                )

                // Bottom Tag & Music Pill (Frosted)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color(0xCC0F0F0F))
                            )
                        )
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x33000000))
                            .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "✨ ${selectedFilter.name}",
                            color = FrostedLavender,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (selectedTab != 2) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x33000000))
                                .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(12.dp))
                                .clickable { showSoundPicker = !showSoundPicker }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Icon(imageVector = Icons.Default.MusicNote, contentDescription = null, tint = FrostedLavender, modifier = Modifier.size(14.dp))
                            Text(text = selectedSound.title, color = TextPrimary, fontSize = 11.sp, maxLines = 1)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Select Media Strip
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Select Media Asset",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(availableMedia) { mediaName ->
                    val isSelected = mediaName == selectedMedia
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(
                                2.dp,
                                if (isSelected) FrostedLavender else Color(0x26FFFFFF),
                                RoundedCornerShape(14.dp)
                            )
                            .clickable { selectedMedia = mediaName }
                    ) {
                        Image(
                            painter = painterResource(id = getDrawableResByName(mediaName)),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0x44D0BCFF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filter Preset Strip (Frosted)
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Live Vibe Filters",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filters) { filter ->
                    val isSelected = filter.name == selectedFilter.name
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isSelected) FrostedLavender
                                else Color(0x26FFFFFF)
                            )
                            .border(1.dp, if (isSelected) FrostedLavender else Color(0x33FFFFFF), RoundedCornerShape(16.dp))
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = filter.name,
                            color = if (isSelected) Color.Black else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Soundtrack Selection Area (for Reels and Posts)
        if (selectedTab != 2) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Audio Soundtrack",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (showSoundPicker) "Hide Sounds" else "Change Sound",
                        color = FrostedLavender,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { showSoundPicker = !showSoundPicker }
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                AnimatedVisibility(visible = showSoundPicker) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        trendingSounds.forEach { sound ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (selectedSound.id == sound.id) Color(0x33D0BCFF) else Color(0x26FFFFFF))
                                    .border(1.dp, if (selectedSound.id == sound.id) FrostedLavender else Color(0x26FFFFFF), RoundedCornerShape(14.dp))
                                    .clickable {
                                        selectedSound = sound
                                        showSoundPicker = false
                                    }
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(imageVector = Icons.Default.MusicNote, contentDescription = null, tint = FrostedLavender, modifier = Modifier.size(18.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = sound.title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    Text(text = sound.artist, color = TextSecondary, fontSize = 11.sp)
                                }
                                if (selectedSound.id == sound.id) {
                                    EqualizerWave(color = FrostedLavender, modifier = Modifier.height(12.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Caption & Details (Frosted Fields)
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = caption,
                onValueChange = { caption = it },
                label = { Text("Caption & Vibe Story") },
                placeholder = { Text("What's on your frequency today? ✨") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("create_caption_input"),
                minLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0x26FFFFFF),
                    unfocusedContainerColor = Color(0x1AFFFFFF),
                    focusedBorderColor = FrostedLavender,
                    unfocusedBorderColor = Color(0x26FFFFFF),
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(16.dp)
            )

            // Quick Hashtag helper chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(quickTags) { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x26FFFFFF))
                            .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(14.dp))
                            .clickable { caption = if (caption.isBlank()) tag else "$caption $tag" }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(text = tag, color = FrostedLavender, fontSize = 11.sp)
                    }
                }
            }

            if (selectedTab != 2) {
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Add Location") },
                    leadingIcon = { Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = FrostedLavender) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0x26FFFFFF),
                        unfocusedContainerColor = Color(0x1AFFFFFF),
                        focusedBorderColor = FrostedLavender,
                        unfocusedBorderColor = Color(0x26FFFFFF),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Publish Button (Frosted Lavender Accent)
        Button(
            onClick = {
                if (selectedTab == 2) {
                    // Story
                    onPublishStory(
                        if (caption.isNotBlank()) caption else "Vibesphere Moments ✨",
                        selectedMedia
                    )
                } else {
                    // Reel or Photo Post
                    val postType = if (selectedTab == 0) "REEL" else "PHOTO"
                    onPublishPost(
                        if (caption.isNotBlank()) caption else "New frequency dropped on Vibesphere 🔮✨",
                        "#Vibesphere #CreativeStudio #TrendingVibes",
                        postType,
                        selectedMedia,
                        selectedSound.title,
                        selectedSound.artist,
                        location,
                        selectedFilter.name
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(54.dp)
                .testTag("publish_vibe_button"),
            colors = ButtonDefaults.buttonColors(containerColor = FrostedLavender),
            shape = RoundedCornerShape(27.dp)
        ) {
            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = Color.Black)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (selectedTab == 2) "Publish to Stories" else "Publish to Vibesphere",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
