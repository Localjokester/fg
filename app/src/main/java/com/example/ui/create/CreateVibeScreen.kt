package com.example.ui.create

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.local.entity.SoundTrackEntity
import com.example.ui.components.VibeCircularProgressIndicator
import com.example.ui.components.getDrawableResByName
import com.example.ui.spotify.SpotifyBadgePill
import com.example.ui.spotify.SpotifyGreen
import com.example.ui.spotify.SpotifyPlaylistBottomSheet
import com.example.ui.spotify.SpotifyPlaylistLinkerDialog
import com.example.ui.theme.FrostedLavender
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        filterName: String,
        spotifyPlaylistUrl: String,
        spotifyPlaylistName: String
    ) -> Unit,
    onPublishStory: (caption: String, mediaDrawable: String, feelingEmoji: String, feelingMood: String) -> Unit,
    onOpenPhotoEditor: (selectedMedia: String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Reel, 1: Post, 2: Story
    val tabs = listOf("🎬 Vibe Reel", "📸 Photo Post", "✨ Story")

    // Default presets & extracted user media list
    val extractedMediaUris = remember { mutableStateListOf<String>() }
    val defaultMediaList = listOf(
        "img_reel_cyber_dance",
        "img_vibe_sunset",
        "img_reel_cafe_aesthetic",
        "img_vibe_concert",
        "img_app_icon"
    )
    var selectedMedia by remember { mutableStateOf(defaultMediaList[0]) }

    // Android Zero-Permission Photo & Video Pickers (ActivityResultContracts)
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val uriString = uri.toString()
            if (!extractedMediaUris.contains(uriString)) {
                extractedMediaUris.add(0, uriString)
            }
            selectedMedia = uriString
            Toast.makeText(context, "Media extracted from device successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    // Spotify Playlist Linking State
    var linkedSpotifyUrl by remember { mutableStateOf("") }
    var linkedSpotifyName by remember { mutableStateOf("") }
    var showSpotifyLinkerDialog by remember { mutableStateOf(false) }
    var showSpotifyPreviewSheet by remember { mutableStateOf(false) }

    val filters = listOf(
        FilterPreset("Cyber Neon", Color(0x33D0BCFF), "Electric Lavender"),
        FilterPreset("Golden Glow", Color(0x33FFAA00), "Warm Sunlight"),
        FilterPreset("Tokyo Noir", Color(0x3300E5FF), "Moody Contrast"),
        FilterPreset("VHS Glitch", Color(0x338F00FF), "Retro Synthwave"),
        FilterPreset("Pure Normal", Color.Transparent, "Natural Tone")
    )
    var selectedFilter by remember { mutableStateOf(filters[0]) }

    val feelings = listOf(
        Pair("🔥", "On Fire"),
        Pair("✨", "Euphoric"),
        Pair("🎧", "In The Zone"),
        Pair("☕", "Caffeinated"),
        Pair("😴", "Low Battery"),
        Pair("💫", "Dreamy"),
        Pair("⚡", "Chaotic"),
        Pair("🧘", "Zen"),
        Pair("🚀", "Hyped"),
        Pair("🎨", "Creative Flow")
    )
    var selectedFeeling by remember { mutableStateOf(feelings[1]) } // Default: ✨ Euphoric

    var caption by remember { mutableStateOf("") }
    var isGeneratingAI by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var location by remember { mutableStateOf("Tokyo, Shibuya") }
    var selectedSound by remember {
        mutableStateOf(
            if (trendingSounds.isNotEmpty()) trendingSounds[0]
            else SoundTrackEntity(title = "Cybernetic Pulse", artist = "Kira Volt")
        )
    }
    var showSoundPicker by remember { mutableStateOf(false) }

    val quickTags = listOf("#Vibesphere", "#Cyberpunk", "#DanceReels", "#Aesthetic", "#GoldenHour", "#TokyoNights", "#EDMFestival")
    val scrollState = rememberScrollState()

    val isExtractedMedia = selectedMedia.startsWith("content://") || selectedMedia.startsWith("file://") || selectedMedia.startsWith("http")

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
                // Render Media (Device URI or Drawable)
                if (isExtractedMedia) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(selectedMedia)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Studio Preview",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(id = getDrawableResByName(selectedMedia)),
                        contentDescription = "Studio Preview",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Live Filter Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(selectedFilter.overlayColor)
                )

                // Top Media Extraction Badge
                if (isExtractedMedia) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x80000000))
                            .border(1.dp, NeonCyan, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Extracted from Device",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

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

                    if (selectedTab == 2) {
                        // Story Feeling Mood Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x551E1B4B))
                                .border(1.dp, FrostedLavender, RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "Feeling ${selectedFeeling.first} ${selectedFeeling.second}",
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        // Sound or Spotify Linked
                        if (linkedSpotifyName.isNotBlank()) {
                            SpotifyBadgePill(
                                playlistName = linkedSpotifyName,
                                onClick = { showSpotifyPreviewSheet = true }
                            )
                        } else {
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
                                Icon(
                                    imageVector = Icons.Default.MusicNote,
                                    contentDescription = null,
                                    tint = FrostedLavender,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = selectedSound.title,
                                    color = TextPrimary,
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Open Advanced Photo Editor shortcut
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x26FFFFFF))
                    .clickable { onOpenPhotoEditor(selectedMedia) }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    tint = FrostedLavender,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Open in Pro Photo Editor",
                    color = FrostedLavender,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ==========================================
        // 1. EXTRACT PHOTOS & VIDEOS FROM DEVICE
        // ==========================================
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Media & Device Extraction",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Zero-Permission Picker",
                    color = NeonCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Extraction Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Extract Photo Button
                Button(
                    onClick = {
                        mediaPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .testTag("extract_photo_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x3300E5FF)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Extract Photo",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Extract Video Button
                Button(
                    onClick = {
                        mediaPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .testTag("extract_video_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x33D0BCFF)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FrostedLavender.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = null,
                        tint = FrostedLavender,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Extract Video",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Media Selection Strip (Extracted URIs + Default Presets)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // "Extract New" Card
                item {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x26FFFFFF))
                            .border(1.dp, Color(0x40FFFFFF), RoundedCornerShape(14.dp))
                            .clickable {
                                mediaPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                                )
                            }
                            .testTag("picker_extract_media_tile"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = "Extract Media",
                                tint = FrostedLavender,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = "Extract", color = FrostedLavender, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Extracted User Media
                items(extractedMediaUris) { uriString ->
                    val isSelected = uriString == selectedMedia
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(
                                2.dp,
                                if (isSelected) NeonCyan else Color(0x3300E5FF),
                                RoundedCornerShape(14.dp)
                            )
                            .clickable { selectedMedia = uriString }
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(uriString)
                                .crossfade(true)
                                .build(),
                            contentDescription = "User extracted media",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0x4400E5FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // Built-in presets
                items(defaultMediaList) { mediaName ->
                    val isSelected = mediaName == selectedMedia
                    Box(
                        modifier = Modifier
                            .size(64.dp)
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
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ==========================================
        // 2. LINK SPOTIFY PLAYLIST INTEGRATION
        // ==========================================
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(SpotifyGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Spotify",
                            tint = Color.Black,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                    Text(
                        text = "Link Spotify Playlist",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (linkedSpotifyName.isNotBlank()) {
                    Text(
                        text = "Change",
                        color = SpotifyGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { showSpotifyLinkerDialog = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (linkedSpotifyName.isBlank()) {
                // Prompt to link Spotify
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { showSpotifyLinkerDialog = true }
                        .testTag("attach_spotify_playlist_button"),
                    colors = CardDefaults.cardColors(containerColor = Color(0x261DB954)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SpotifyGreen.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Link,
                                contentDescription = null,
                                tint = SpotifyGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    text = "Attach Spotify Playlist",
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Link your soundtrack so listeners can open it on Spotify",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = SpotifyGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            } else {
                // Display linked Spotify pill with preview and remove buttons
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0x331DB954)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SpotifyGreen),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { showSpotifyPreviewSheet = true }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(SpotifyGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GraphicEq,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = linkedSpotifyName,
                                    color = SpotifyGreen,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Text(
                                    text = "Tap to preview tracklist & launch",
                                    color = TextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                linkedSpotifyUrl = ""
                                linkedSpotifyName = ""
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove Spotify Link",
                                tint = TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

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
                                    Text(text = sound.title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                    Text(text = "${sound.artist} • ${sound.durationSeconds}s", color = TextSecondary, fontSize = 11.sp)
                                }
                                if (selectedSound.id == sound.id) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = FrostedLavender, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Caption & AI Caption Generator
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedTab == 2) "Story Text & Mood" else "Caption & Vibes",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                // AI Caption Prompt Generator
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x3300E5FF))
                        .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .clickable(enabled = !isGeneratingAI) {
                            coroutineScope.launch {
                                isGeneratingAI = true
                                delay(900)
                                val pool = listOf(
                                    "Chasing high-frequency echoes in the neon metropolis ⚡🔮 #CyberVibe #FutureAesthetic",
                                    "Pure golden hour bliss radiating through every frame ✨🎧 #VibeAesthetic #Atmosphere",
                                    "Unlocking dimensional creativity with ${selectedFilter.name} vibes 🌌💫 #Vibesphere #CreatorMode",
                                    "Deep sonic rhythms meet frosted lavender dreams 🔮✨ #InTheZone #CreativeEnergy"
                                )
                                caption = pool.random()
                                isGeneratingAI = false
                            }
                        }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .testTag("ai_caption_btn"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (isGeneratingAI) {
                        VibeCircularProgressIndicator(size = 12.dp, strokeWidth = 1.5.dp, color = NeonCyan)
                    } else {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
                    }
                    Text(
                        text = if (isGeneratingAI) "Synthesizing..." else "AI Vibe Caption",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            OutlinedTextField(
                value = caption,
                onValueChange = { caption = it },
                placeholder = {
                    Text(
                        if (selectedTab == 2) "Write what's on your mind..."
                        else "Write a vibe caption, drop hashtags..."
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .testTag("create_caption_input"),
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

            // Feeling Emoji & Mood Selector (Specially for Stories & Status)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "How are you feeling right now? (Story Mood)",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(feelings) { feeling ->
                        val isSelected = feeling == selectedFeeling
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) FrostedLavender else Color(0x26FFFFFF))
                                .border(1.dp, if (isSelected) FrostedLavender else Color(0x26FFFFFF), RoundedCornerShape(14.dp))
                            .clickable { selectedFeeling = feeling }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${feeling.first} ${feeling.second}",
                                color = if (isSelected) Color.Black else TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
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
                    // Story with Feeling
                    onPublishStory(
                        if (caption.isNotBlank()) caption else "Vibesphere Moments ✨",
                        selectedMedia,
                        selectedFeeling.first,
                        selectedFeeling.second
                    )
                } else {
                    // Reel or Photo Post
                    val postType = if (selectedTab == 0) "REEL" else "PHOTO"
                    onPublishPost(
                        if (caption.isNotBlank()) caption else "New frequency dropped on Vibesphere 🔮✨",
                        "#Vibesphere #CreativeStudio #TrendingVibes",
                        postType,
                        selectedMedia,
                        if (linkedSpotifyName.isNotBlank()) linkedSpotifyName else selectedSound.title,
                        if (linkedSpotifyName.isNotBlank()) "Spotify Playlist" else selectedSound.artist,
                        location,
                        selectedFilter.name,
                        linkedSpotifyUrl,
                        linkedSpotifyName
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

    // Spotify Linker Dialog
    if (showSpotifyLinkerDialog) {
        SpotifyPlaylistLinkerDialog(
            initialUrl = linkedSpotifyUrl,
            onDismiss = { showSpotifyLinkerDialog = false },
            onPlaylistLinked = { url, name ->
                linkedSpotifyUrl = url
                linkedSpotifyName = name
            }
        )
    }

    // Spotify Preview Bottom Sheet
    if (showSpotifyPreviewSheet && linkedSpotifyUrl.isNotBlank()) {
        SpotifyPlaylistBottomSheet(
            playlistUrl = linkedSpotifyUrl,
            playlistName = linkedSpotifyName,
            onDismiss = { showSpotifyPreviewSheet = false }
        )
    }
}
