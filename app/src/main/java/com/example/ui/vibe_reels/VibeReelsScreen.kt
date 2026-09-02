package com.example.ui.vibe_reels

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.PostEntity
import com.example.data.local.entity.SoundTrackEntity
import com.example.ui.components.DoubleTapLikeArea
import com.example.ui.components.MusicTicker
import com.example.ui.components.RotatingSoundDisc
import com.example.ui.components.VibeRingAvatar
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
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VibeBackground

@Composable
fun VibeReelsScreen(
    reels: List<PostEntity>,
    onToggleLike: (Long, Boolean) -> Unit,
    onToggleSave: (Long, Boolean) -> Unit,
    onOpenComments: (PostEntity) -> Unit,
    onOpenShare: (PostEntity) -> Unit,
    onSoundClick: (SoundTrackEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    if (reels.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(VibeBackground),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "🎬", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "No reels in the stream yet.",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Create the first vibe reel!",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }
        }
        return
    }

    val pagerState = rememberPagerState(pageCount = { reels.size })

    VerticalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("vibe_reels_pager")
    ) { page ->
        val reel = reels[page]
        ReelVideoItem(
            reel = reel,
            onToggleLike = { onToggleLike(reel.id, reel.isLiked) },
            onToggleSave = { onToggleSave(reel.id, reel.isSaved) },
            onOpenComments = { onOpenComments(reel) },
            onOpenShare = { onOpenShare(reel) },
            onSoundClick = {
                onSoundClick(
                    SoundTrackEntity(
                        title = reel.soundTitle,
                        artist = reel.soundArtist,
                        coverDrawable = reel.mediaDrawableName,
                        vibesCount = reel.likesCount * 2
                    )
                )
            }
        )
    }
}

@Composable
fun ReelVideoItem(
    reel: PostEntity,
    onToggleLike: () -> Unit,
    onToggleSave: () -> Unit,
    onOpenComments: () -> Unit,
    onOpenShare: () -> Unit,
    onSoundClick: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(true) }
    var isFollowing by remember { mutableStateOf(false) }
    var isCaptionExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Video / Visual Layer with Double Tap Like Area
        DoubleTapLikeArea(
            onDoubleTap = onToggleLike,
            onSingleTap = { isPlaying = !isPlaying },
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = getDrawableResByName(reel.mediaDrawableName)),
                    contentDescription = reel.caption,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Cinematic dark vignette gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0x55000000),
                                    Color.Transparent,
                                    Color(0x88000000),
                                    Color(0xEE090710)
                                )
                            )
                        )
                )

                // Play / Pause Indicator
                AnimatedVisibility(
                    visible = !isPlaying,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut(),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color(0x88000000)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Paused",
                            tint = Color.White,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }
            }
        }

        // Right Floating Action Stack (TikTok style)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Author Avatar with Follow (+) Button
            Box(contentAlignment = Alignment.BottomCenter) {
                VibeRingAvatar(
                    drawableName = reel.mediaDrawableName,
                    size = 48.dp,
                    hasStory = true,
                    isSeen = false
                )

                // Follow toggle badge
                Box(
                    modifier = Modifier
                        .offset(y = 8.dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(if (isFollowing) NeonCyan else NeonMagenta)
                        .clickable { isFollowing = !isFollowing },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFollowing) Icons.Default.Check else Icons.Default.Add,
                        contentDescription = "Follow",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Like Action
            ReelActionIcon(
                icon = Icons.Default.Favorite,
                label = "${reel.likesCount}",
                tint = if (reel.isLiked) HeartRed else Color.White,
                onClick = onToggleLike,
                testTag = "reel_like_button"
            )

            // Comment Action
            ReelActionIcon(
                icon = Icons.Default.Comment,
                label = "${reel.commentsCount}",
                tint = Color.White,
                onClick = onOpenComments,
                testTag = "reel_comment_button"
            )

            // Bookmark / Save Action
            ReelActionIcon(
                icon = if (reel.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                label = "Save",
                tint = if (reel.isSaved) NeonGold else Color.White,
                onClick = onToggleSave,
                testTag = "reel_save_button"
            )

            // Remix / Share Action
            ReelActionIcon(
                icon = Icons.Default.Share,
                label = "${reel.sharesCount}",
                tint = Color.White,
                onClick = onOpenShare,
                testTag = "reel_share_button"
            )

            // Rotating Vinyl Sound Disc
            RotatingSoundDisc(
                coverDrawable = reel.mediaDrawableName,
                isPlaying = isPlaying,
                onClick = onSoundClick
            )
        }

        // Bottom Left Creator Info & Audio Ticker Overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.78f)
                .padding(start = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Vibe Score & Filter Tag
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VibeScoreBadge(score = reel.vibeScore)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x33000000))
                        .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(text = "⚡ ${reel.filterName}", color = FrostedLavender, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Creator Handle & Follow Status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "@${reel.authorHandle}",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = "Verified",
                    tint = FrostedLavender,
                    modifier = Modifier.size(16.dp)
                )

                if (!isFollowing) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x26FFFFFF))
                            .border(1.dp, Color(0x40FFFFFF), RoundedCornerShape(12.dp))
                            .clickable { isFollowing = true }
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Text(text = "Follow", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Caption & Hashtags
            Text(
                text = reel.caption,
                color = Color.White,
                fontSize = 13.sp,
                maxLines = if (isCaptionExpanded) 6 else 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clickable { isCaptionExpanded = !isCaptionExpanded }
            )

            if (reel.hashtags.isNotBlank()) {
                Text(
                    text = reel.hashtags,
                    color = FrostedLavender,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Sound Bar Ticker
            MusicTicker(
                title = reel.soundTitle,
                artist = reel.soundArtist,
                onClick = onSoundClick
            )
        }
    }
}

@Composable
fun ReelActionIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit,
    testTag: String = ""
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(Color(0x33000000))
                .border(1.dp, Color(0x33FFFFFF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
