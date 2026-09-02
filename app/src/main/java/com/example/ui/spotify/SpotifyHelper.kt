package com.example.ui.spotify

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.ui.components.EqualizerWave
import com.example.ui.components.getDrawableResByName
import com.example.ui.theme.FrostedLavender
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VibeBackground

val SpotifyGreen = Color(0xFF1DB954)
val SpotifyDark = Color(0xFF121212)
val SpotifyCardBg = Color(0xFF181818)

data class SpotifyTrack(
    val title: String,
    val artist: String,
    val duration: String
)

data class SpotifyPlaylist(
    val id: String,
    val name: String,
    val curator: String,
    val followers: String,
    val coverDrawable: String,
    val trackCount: Int,
    val spotifyUrl: String,
    val description: String,
    val tracks: List<SpotifyTrack>
)

val CuratedSpotifyPlaylists = listOf(
    SpotifyPlaylist(
        id = "37i9dQZF1DXdLEN7aqioXM",
        name = "Cyber Chill & Synthwave Beats",
        curator = "Spotify • Official",
        followers = "1.4M followers",
        coverDrawable = "img_vibe_sunset",
        trackCount = 58,
        spotifyUrl = "https://open.spotify.com/playlist/37i9dQZF1DXdLEN7aqioXM",
        description = "Neon night drives, glowing synthesizers, and futuristic retro dreamscapes.",
        tracks = listOf(
            SpotifyTrack("Midnight City Lights", "Kavinsky & VibeSphere", "3:42"),
            SpotifyTrack("Neon Horizon (Extended Mix)", "CyberAura", "4:15"),
            SpotifyTrack("Chasing Solar Echoes", "Kira Volt", "3:18"),
            SpotifyTrack("Synthetic Dreams", "Tokyo Nightfall", "2:55"),
            SpotifyTrack("Retrowave Sunset", "Aria Nova", "3:30")
        )
    ),
    SpotifyPlaylist(
        id = "37i9dQZF1DX4t95PaoR1To",
        name = "Neon Tokyo Lo-Fi & Chill",
        curator = "Lo-Fi Central",
        followers = "890K followers",
        coverDrawable = "img_reel_cafe_aesthetic",
        trackCount = 84,
        spotifyUrl = "https://open.spotify.com/playlist/37i9dQZF1DX4t95PaoR1To",
        description = "Smooth rainy afternoon beats, coffee shop ambience, and warm analog vinyl crackles.",
        tracks = listOf(
            SpotifyTrack("Shibuya Rainy Walk", "Lofi Fruits Music", "2:20"),
            SpotifyTrack("Matcha Latte Beats", "Chillhop Music", "2:45"),
            SpotifyTrack("Midnight Study Session", "Kudasaibeats", "2:10"),
            SpotifyTrack("Sakura Blossom Breeze", "Nujabes Tribute", "3:05")
        )
    ),
    SpotifyPlaylist(
        id = "37i9dQZF1DX6J5t3hSDrXh",
        name = "Deep Midnight Techno & Darkwave",
        curator = "ClubVibe",
        followers = "620K followers",
        coverDrawable = "img_vibe_concert",
        trackCount = 42,
        spotifyUrl = "https://open.spotify.com/playlist/37i9dQZF1DX6J5t3hSDrXh",
        description = "Hypnotic 130+ BPM basslines, underground club euphoria, and industrial lasers.",
        tracks = listOf(
            SpotifyTrack("Industrial Pulse 01", "Charlotte de Beat", "5:12"),
            SpotifyTrack("Rave Paradigm", "Enrico Sangiuliano", "4:50"),
            SpotifyTrack("Dark Dimension", "Amelie Lens Tribute", "4:32"),
            SpotifyTrack("Acid Frequency", "Tale Of Us Edit", "5:00")
        )
    ),
    SpotifyPlaylist(
        id = "37i9dQZF1DX2Nc3B70tvx0",
        name = "Golden Hour Indie Vibes",
        curator = "Indie Pulse",
        followers = "1.1M followers",
        coverDrawable = "img_reel_cyber_dance",
        trackCount = 65,
        spotifyUrl = "https://open.spotify.com/playlist/37i9dQZF1DX2Nc3B70tvx0",
        description = "Sun-drenched melodies, indie pop, and breezy acoustic vibes for good feelings.",
        tracks = listOf(
            SpotifyTrack("Sunsets in Malibu", "Boy Pablo & Friends", "3:10"),
            SpotifyTrack("Coastal Highway", "Dayglow", "3:25"),
            SpotifyTrack("Vintage Love Letter", "Rex Orange Vibe", "2:50"),
            SpotifyTrack("Golden Glow", "Dominic Fike Tribute", "3:00")
        )
    )
)

/**
 * Open Spotify URL in Spotify App or Web Browser
 */
fun openSpotifyUrl(context: Context, url: String) {
    try {
        val finalUrl = if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("spotify:")) {
            url
        } else {
            "https://open.spotify.com/playlist/$url"
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Opening Spotify Playlist: $url", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Parse or retrieve Spotify playlist object by URL or Name
 */
fun findSpotifyPlaylist(urlOrName: String): SpotifyPlaylist {
    val matched = CuratedSpotifyPlaylists.firstOrNull {
        it.spotifyUrl.equals(urlOrName, ignoreCase = true) ||
                it.id.equals(urlOrName, ignoreCase = true) ||
                it.name.contains(urlOrName, ignoreCase = true)
    }
    if (matched != null) return matched

    // Fallback dynamic playlist for custom user-pasted links
    val cleanedName = when {
        urlOrName.contains("synth", ignoreCase = true) -> "Custom Synthwave Mix"
        urlOrName.contains("lofi", ignoreCase = true) -> "Custom Lo-Fi Beat Tape"
        urlOrName.contains("techno", ignoreCase = true) -> "Custom Techno Club Mix"
        else -> "My Spotify Playlist"
    }

    return SpotifyPlaylist(
        id = "custom_user_playlist",
        name = cleanedName,
        curator = "Spotify User",
        followers = "Linked by Creator",
        coverDrawable = "img_vibe_sunset",
        trackCount = 24,
        spotifyUrl = if (urlOrName.isNotBlank()) urlOrName else "https://open.spotify.com/playlist/37i9dQZF1DXdLEN7aqioXM",
        description = "Custom Spotify playlist linked to this post / profile on Vibesphere.",
        tracks = listOf(
            SpotifyTrack("Featured Vibe Track", "Spotify Artist", "3:30"),
            SpotifyTrack("Echoes in the Dark", "Electronic Soundscape", "4:12"),
            SpotifyTrack("Ambient Resonance", "Soundwave Studio", "3:05")
        )
    )
}

/**
 * Glowing Spotify Playlist Badge Pill
 */
@Composable
fun SpotifyBadgePill(
    playlistName: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x331DB954))
            .border(1.dp, SpotifyGreen.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .testTag("spotify_playlist_badge"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Spotify Icon representation
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(SpotifyGreen),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.GraphicEq,
                contentDescription = "Spotify",
                tint = Color.Black,
                modifier = Modifier.size(10.dp)
            )
        }

        Text(
            text = playlistName.ifBlank { "Spotify Playlist" },
            color = SpotifyGreen,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Icon(
            imageVector = Icons.Default.OpenInNew,
            contentDescription = "Open Spotify",
            tint = SpotifyGreen,
            modifier = Modifier.size(11.dp)
        )
    }
}

/**
 * Spotify Playlist Bottom Sheet with Track Previews and Direct Launch
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotifyPlaylistBottomSheet(
    playlistUrl: String,
    playlistName: String = "",
    onDismiss: () -> Unit,
    onUseAsPostSoundtrack: ((SpotifyPlaylist) -> Unit)? = null
) {
    val context = LocalContext.current
    val playlist = remember(playlistUrl, playlistName) {
        if (playlistName.isNotBlank()) findSpotifyPlaylist(playlistName)
        else findSpotifyPlaylist(playlistUrl)
    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var playingTrackIndex by remember { mutableIntStateOf(-1) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFA121212),
        contentColor = TextPrimary,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color(0x40FFFFFF))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .testTag("spotify_playlist_bottom_sheet"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Bar: Spotify Header & Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(SpotifyGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Spotify",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "Spotify Playlist Link",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpotifyGreen
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary
                    )
                }
            }

            // Playlist Hero Card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x331DB954))
                    .border(1.dp, Color(0x4D1DB954), RoundedCornerShape(18.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Album Art
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.5.dp, SpotifyGreen, RoundedCornerShape(14.dp))
                ) {
                    Image(
                        painter = painterResource(id = getDrawableResByName(playlist.coverDrawable)),
                        contentDescription = playlist.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = playlist.name,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "${playlist.curator} • ${playlist.trackCount} tracks",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Text(
                        text = playlist.followers,
                        color = SpotifyGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Playlist Description
            Text(
                text = playlist.description,
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            // Direct Open in Spotify Action Button
            Button(
                onClick = { openSpotifyUrl(context, playlist.spotifyUrl) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("open_in_spotify_button"),
                colors = ButtonDefaults.buttonColors(containerColor = SpotifyGreen),
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Open Playlist in Spotify",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Optional: Use this Playlist as Audio in Post/Reel
            if (onUseAsPostSoundtrack != null) {
                Button(
                    onClick = {
                        onUseAsPostSoundtrack(playlist)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x33D0BCFF)),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = FrostedLavender,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Attach to Current Post / Story",
                        color = FrostedLavender,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Tracks List Section
            Text(
                text = "Playlist Tracks & Audio Previews",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(playlist.tracks.size) { index ->
                    val track = playlist.tracks[index]
                    val isPlaying = playingTrackIndex == index

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isPlaying) Color(0x261DB954) else Color(0x1AFFFFFF))
                            .border(
                                1.dp,
                                if (isPlaying) SpotifyGreen else Color(0x1FFFFFFF),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                playingTrackIndex = if (isPlaying) -1 else index
                            }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(if (isPlaying) SpotifyGreen else Color(0x26FFFFFF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = if (isPlaying) Color.Black else TextPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = track.title,
                                    color = if (isPlaying) SpotifyGreen else TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = track.artist,
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        if (isPlaying) {
                            EqualizerWave(color = SpotifyGreen, modifier = Modifier.height(14.dp))
                        } else {
                            Text(text = track.duration, color = TextMuted, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Spotify Playlist Linker Dialog - Paste URL or choose from curated playlists
 */
@Composable
fun SpotifyPlaylistLinkerDialog(
    initialUrl: String = "",
    onDismiss: () -> Unit,
    onPlaylistLinked: (url: String, name: String) -> Unit
) {
    var urlInput by remember { mutableStateOf(initialUrl) }
    var selectedCurated by remember { mutableStateOf<SpotifyPlaylist?>(null) }
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("spotify_playlist_linker_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SpotifyDark),
            border = androidx.compose.foundation.BorderStroke(1.dp, SpotifyGreen.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(SpotifyGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = "Spotify",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "Link Spotify Playlist",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Text(
                    text = "Attach your favorite Spotify playlist to this post or your creator profile so listeners can play along.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                // Paste Input Field
                OutlinedTextField(
                    value = urlInput,
                    onValueChange = {
                        urlInput = it
                        selectedCurated = null
                    },
                    label = { Text("Spotify Playlist URL or URI") },
                    placeholder = { Text("https://open.spotify.com/playlist/...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Link, contentDescription = null, tint = SpotifyGreen)
                    },
                    trailingIcon = {
                        if (urlInput.isNotBlank()) {
                            IconButton(onClick = { urlInput = "" }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = TextMuted)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("spotify_url_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SpotifyGreen,
                        unfocusedBorderColor = Color(0x33FFFFFF),
                        focusedContainerColor = Color(0x261DB954),
                        unfocusedContainerColor = Color(0x1AFFFFFF),
                        focusedLabelColor = SpotifyGreen
                    )
                )

                // Quick Pick Curated Playlists Strip
                Text(
                    text = "Or Choose Trending Curated Vibe Playlists:",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(CuratedSpotifyPlaylists) { item ->
                        val isSelected = selectedCurated?.id == item.id || urlInput == item.spotifyUrl
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0x331DB954) else Color(0x1AFFFFFF))
                                .border(
                                    1.dp,
                                    if (isSelected) SpotifyGreen else Color(0x26FFFFFF),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    selectedCurated = item
                                    urlInput = item.spotifyUrl
                                }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Image(
                                    painter = painterResource(id = getDrawableResByName(item.coverDrawable)),
                                    contentDescription = item.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                )
                                Column {
                                    Text(
                                        text = item.name,
                                        color = if (isSelected) SpotifyGreen else TextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${item.trackCount} tracks • ${item.followers}",
                                        color = TextSecondary,
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = SpotifyGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x26FFFFFF)),
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Text(text = "Cancel", color = TextSecondary, fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            val finalUrl = if (urlInput.isNotBlank()) urlInput.trim() else CuratedSpotifyPlaylists[0].spotifyUrl
                            val finalName = selectedCurated?.name ?: findSpotifyPlaylist(finalUrl).name
                            onPlaylistLinked(finalUrl, finalName)
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("confirm_link_spotify_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = SpotifyGreen),
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Link,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Link Playlist",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
