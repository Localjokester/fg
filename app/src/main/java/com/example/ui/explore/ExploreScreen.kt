package com.example.ui.explore

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.PostEntity
import com.example.data.local.entity.SoundTrackEntity
import com.example.ui.components.EqualizerWave
import com.example.ui.components.ShimmerExploreSkeleton
import com.example.ui.components.VibeCircularProgressIndicator
import com.example.ui.components.VibeMediaImage
import com.example.ui.components.VibeScoreBadge
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
import com.example.ui.theme.VibeSurfaceVariant

@Composable
fun ExploreScreen(
    posts: List<PostEntity>,
    trendingSounds: List<SoundTrackEntity>,
    searchQuery: String,
    selectedTag: String,
    onSearchQueryChange: (String) -> Unit,
    onTagSelect: (String) -> Unit,
    onPostClick: (PostEntity) -> Unit,
    onSoundClick: (SoundTrackEntity) -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    val topicTags = listOf("All", "#Cyberpunk", "#DanceReels", "#SunsetPhotography", "#EDMFestival", "#CozyCore", "#TokyoNights", "#Aesthetic")

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxSize()
            .testTag("explore_grid"),
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 90.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Search Header (Span 3 columns)
        item(span = { GridItemSpan(3) }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Frosted Search Input Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Search vibes, tags, creators, sounds...", color = TextMuted, fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = FrostedLavender
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = TextSecondary
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("explore_search_bar"),
                    shape = RoundedCornerShape(26.dp),
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

                // Topic Tags Row (Frosted Chips)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(topicTags) { tag ->
                        val isSelected = tag == selectedTag
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected) FrostedLavender
                                    else Color(0x26FFFFFF)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) FrostedLavender else Color(0x33FFFFFF),
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { onTagSelect(tag) }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = tag,
                                color = if (isSelected) Color.Black else TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Trending Audio Banner (Span 3 columns)
        item(span = { GridItemSpan(3) }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Whatshot, contentDescription = "Trending", tint = FrostedLavender, modifier = Modifier.size(18.dp))
                        Text(
                            text = "Trending Sounds on Vibesphere",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(trendingSounds, key = { it.id }) { sound ->
                        Box(
                            modifier = Modifier
                                .width(220.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0x331E1B4B))
                                .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(16.dp))
                                .clickable { onSoundClick(sound) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                ) {
                                    VibeMediaImage(
                                        drawableName = sound.coverDrawable,
                                        contentDescription = sound.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize(),
                                        showProgressSpinner = false
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color(0x33000000)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White, modifier = Modifier.size(18.dp))
                                    }
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = sound.title,
                                        color = TextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = sound.artist,
                                        color = FrostedLavender,
                                        fontSize = 11.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "🔥 ${(sound.vibesCount / 1000)}k vibes",
                                        color = TextMuted,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (isLoading) {
            // Shimmer Explore Grid Skeleton during filtering / network fetching
            item(span = { GridItemSpan(3) }) {
                ShimmerExploreSkeleton(modifier = Modifier.fillMaxWidth())
            }
        } else {
            // Frosted Grid of Posts & Reels
            items(posts, key = { it.id }) { post ->
                val isReel = post.postType == "REEL"
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .border(0.5.dp, Color(0x26FFFFFF), RoundedCornerShape(14.dp))
                        .clickable { onPostClick(post) }
                        .testTag("explore_item_${post.id}")
                ) {
                    VibeMediaImage(
                        drawableName = post.mediaDrawableName,
                        contentDescription = post.caption,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Bottom Gradient info
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color(0x99000000))
                                )
                            )
                    )

                    // Reel indicator badge
                    if (isReel) {
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
                                contentDescription = "Reel",
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    // Likes count on bottom
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Likes",
                            tint = HeartRed,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = if (post.likesCount > 1000) "${post.likesCount / 1000}k" else "${post.likesCount}",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
