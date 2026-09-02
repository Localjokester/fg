package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.HeartRed
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.FrostedLavender
import com.example.ui.theme.NeonGold
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonPeach
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VibeBackground
import com.example.ui.theme.VibeCardBg
import com.example.ui.theme.VibeStoryGradient
import com.example.ui.theme.VibeSurface
import com.example.ui.theme.VibeSurfaceVariant
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun getDrawableResByName(name: String): Int {
    return when (name) {
        "img_reel_cyber_dance" -> R.drawable.img_reel_cyber_dance
        "img_reel_cafe_aesthetic" -> R.drawable.img_reel_cafe_aesthetic
        "img_vibe_sunset" -> R.drawable.img_vibe_sunset
        "img_vibe_concert" -> R.drawable.img_vibe_concert
        "img_app_icon" -> R.drawable.img_app_icon
        else -> R.drawable.img_vibe_sunset
    }
}

/**
 * Story / User Avatar with vibrant gradient ring and optional LIVE / unseen indicator.
 */
@Composable
fun VibeRingAvatar(
    drawableName: String,
    size: Dp = 64.dp,
    hasStory: Boolean = true,
    isSeen: Boolean = false,
    isLive: Boolean = false,
    onClick: () -> Unit = {}
) {
    val ringPadding = if (hasStory) 3.dp else 0.dp
    val borderBrush = when {
        isLive -> Brush.linearGradient(listOf(Color(0xFFFF0055), Color(0xFFFF5500)))
        hasStory && !isSeen -> Brush.linearGradient(listOf(FrostedLavender, NeonPurple, NeonCyan))
        hasStory && isSeen -> Brush.linearGradient(listOf(Color(0x40FFFFFF), Color(0x20FFFFFF)))
        else -> Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
    }

    Box(
        modifier = Modifier
            .size(size)
            .clickable(onClick = onClick)
            .testTag("avatar_ring"),
        contentAlignment = Alignment.Center
    ) {
        // Outer Gradient Ring
        if (hasStory) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(borderBrush)
            )
        }

        // Inner Avatar Frame
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(ringPadding)
                .clip(CircleShape)
                .background(VibeBackground),
            contentAlignment = Alignment.Center
        ) {
            val isUri = drawableName.startsWith("content://") || drawableName.startsWith("file://") || drawableName.startsWith("http://") || drawableName.startsWith("https://")
            if (isUri) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(drawableName)
                        .crossfade(true)
                        .build(),
                    contentDescription = "User Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp)
                        .clip(CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(id = getDrawableResByName(drawableName)),
                    contentDescription = "User Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp)
                        .clip(CircleShape)
                )
            }
        }

        // LIVE Pill Badge
        if (isLive) {
            Surface(
                color = Color(0xFFFF0055),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 4.dp)
            ) {
                Text(
                    text = "LIVE",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
            }
        }
    }
}

/**
 * Double Tap Heart Burst Animation (Instagram/TikTok style)
 */
@Composable
fun DoubleTapLikeArea(
    onDoubleTap: () -> Unit,
    onSingleTap: () -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var showHeart by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = {
                    showHeart = true
                    onDoubleTap()
                    scope.launch {
                        delay(900)
                        showHeart = false
                    }
                },
                onTap = {
                    onSingleTap()
                }
            )
        },
        contentAlignment = Alignment.Center
    ) {
        content()

        AnimatedVisibility(
            visible = showHeart,
            enter = scaleIn(spring(dampingRatio = 0.4f, stiffness = 400f)) + fadeIn(),
            exit = scaleOut(tween(300)) + fadeOut(tween(300))
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Liked Heart Animation",
                tint = HeartRed,
                modifier = Modifier
                    .size(110.dp)
                    .testTag("double_tap_heart")
            )
        }
    }
}

/**
 * Rotating Vinyl Disc for Reel Audio (TikTok style)
 */
@Composable
fun RotatingSoundDisc(
    coverDrawable: String,
    isPlaying: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "disc_spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "disc_angle"
    )

    Box(
        modifier = modifier
            .size(52.dp)
            .clickable(onClick = onClick)
            .testTag("sound_disc"),
        contentAlignment = Alignment.Center
    ) {
        // Vinyl Outer Ring with Frosted Border
        Box(
            modifier = Modifier
                .fillMaxSize()
                .rotate(if (isPlaying) rotation else 0f)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xCC2B2930),
                            Color(0xDD1E1B4B),
                            Color(0xFF0F0F0F)
                        )
                    )
                )
                .border(1.5.dp, Color(0x40FFFFFF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Center album artwork
            Image(
                painter = painterResource(id = getDrawableResByName(coverDrawable)),
                contentDescription = "Sound Cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
            )
        }
    }
}

/**
 * Frosted Glass Music Ticker Bar with pulsing equalizer waveform
 */
@Composable
fun MusicTicker(
    title: String,
    artist: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x33000000))
            .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.MusicNote,
            contentDescription = "Music",
            tint = FrostedLavender,
            modifier = Modifier.size(13.dp)
        )

        Text(
            text = "$title • $artist",
            color = TextPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Mini Audio Wave Bars
        EqualizerWave(color = FrostedLavender, modifier = Modifier.height(11.dp))
    }
}

@Composable
fun EqualizerWave(color: Color, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "eq_bars")
    val bar1 by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(450, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b1"
    )
    val bar2 by infiniteTransition.animateFloat(
        initialValue = 0.9f, targetValue = 0.2f,
        animationSpec = infiniteRepeatable(tween(350, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b2"
    )
    val bar3 by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 0.95f,
        animationSpec = infiniteRepeatable(tween(550, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b3"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .height((12 * bar1).dp)
                .clip(RoundedCornerShape(1.dp))
                .background(color)
        )
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .height((12 * bar2).dp)
                .clip(RoundedCornerShape(1.dp))
                .background(color)
        )
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .height((12 * bar3).dp)
                .clip(RoundedCornerShape(1.dp))
                .background(color)
        )
    }
}

/**
 * Frosted Glass Vibe Score Pill (e.g. ⚡ 99% VIBE)
 */
@Composable
fun VibeScoreBadge(score: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x33000000))
            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "⚡",
            fontSize = 11.sp
        )
        Text(
            text = "$score% VIBE",
            color = FrostedLavender,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

/**
 * Animated Shimmer Gradient Brush for loading placeholders and perceived performance enhancement.
 */
@Composable
fun shimmerBrush(
    showShimmer: Boolean = true,
    targetValue: Float = 1400f
): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            Color(0x14FFFFFF),
            Color(0x338B5CF6),
            Color(0x66FFFFFF),
            Color(0x338B5CF6),
            Color(0x14FFFFFF)
        )
        val transition = rememberInfiniteTransition(label = "shimmer_transition")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1100, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "shimmer_translation"
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnimation.value - 450f, translateAnimation.value - 450f),
            end = Offset(translateAnimation.value, translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent)
        )
    }
}

/**
 * Shimmer effect modifier to apply to any composable placeholder shape.
 */
fun Modifier.shimmerLoading(
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp)
): Modifier = composed {
    if (enabled) {
        val brush = shimmerBrush(showShimmer = true)
        this
            .clip(shape)
            .background(Color(0x1F1E1B4B))
            .background(brush)
            .border(1.dp, Color(0x22FFFFFF), shape)
    } else {
        this
    }
}

/**
 * Polished Circular Progress Indicator with glowing neon lavender / cyan styling.
 */
@Composable
fun VibeCircularProgressIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    strokeWidth: Dp = 3.dp,
    color: Color = FrostedLavender,
    trackColor: Color = Color(0x338B5CF6)
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            color = color,
            trackColor = trackColor,
            strokeWidth = strokeWidth
        )
    }
}

/**
 * Smart Media Image Loader with animated Shimmer Placeholder and Circular Progress Indicator.
 * Improves perceived loading performance during feed scrolling and image rendering.
 */
@Composable
fun VibeMediaImage(
    drawableName: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = RoundedCornerShape(0.dp),
    showProgressSpinner: Boolean = true,
    simulatedDelayMs: Long = 180L
) {
    var isLoaded by remember(drawableName) { mutableStateOf(false) }

    LaunchedEffect(drawableName) {
        isLoaded = false
        if (simulatedDelayMs > 0) {
            delay(simulatedDelayMs)
        }
        isLoaded = true
    }

    Box(
        modifier = modifier
            .clip(shape)
            .testTag("vibe_media_image_container"),
        contentAlignment = Alignment.Center
    ) {
        if (!isLoaded) {
            // Animated Shimmer Placeholder Box
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmerLoading(enabled = true, shape = shape),
                contentAlignment = Alignment.Center
            ) {
                if (showProgressSpinner) {
                    VibeCircularProgressIndicator(
                        size = 28.dp,
                        strokeWidth = 2.5.dp,
                        color = FrostedLavender
                    )
                }
            }
        }

        // Image with smooth fade-in
        AnimatedVisibility(
            visible = isLoaded,
            enter = fadeIn(tween(250)),
            exit = fadeOut(tween(150))
        ) {
            val isUri = drawableName.startsWith("content://") || drawableName.startsWith("file://") || drawableName.startsWith("http://") || drawableName.startsWith("https://")
            if (isUri) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(drawableName)
                        .crossfade(true)
                        .build(),
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape)
                )
            } else {
                Image(
                    painter = painterResource(id = getDrawableResByName(drawableName)),
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape)
                )
            }
        }
    }
}

/**
 * Shimmer Story Avatar Item Placeholder
 */
@Composable
fun ShimmerStoryItemSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.width(72.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .shimmerLoading(enabled = true, shape = CircleShape)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(10.dp)
                .shimmerLoading(enabled = true, shape = RoundedCornerShape(4.dp))
        )
    }
}

/**
 * Shimmer Post Card Skeleton for Feed initial fetch & refresh states
 */
@Composable
fun ShimmerPostSkeleton(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x1F1E1B4B))
            .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
            .padding(14.dp)
            .testTag("shimmer_post_skeleton")
    ) {
        Column {
            // Header Row Skeleton
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .shimmerLoading(enabled = true, shape = CircleShape)
                )
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .width(110.dp)
                            .height(12.dp)
                            .shimmerLoading(enabled = true, shape = RoundedCornerShape(4.dp))
                    )
                    Box(
                        modifier = Modifier
                            .width(70.dp)
                            .height(10.dp)
                            .shimmerLoading(enabled = true, shape = RoundedCornerShape(4.dp))
                    )
                }
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .shimmerLoading(enabled = true, shape = CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Media Shimmer Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .shimmerLoading(enabled = true, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                VibeCircularProgressIndicator(
                    size = 36.dp,
                    color = FrostedLavender
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Icons Row Skeleton
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.size(26.dp).shimmerLoading(enabled = true, shape = CircleShape))
                Box(modifier = Modifier.size(26.dp).shimmerLoading(enabled = true, shape = CircleShape))
                Box(modifier = Modifier.size(26.dp).shimmerLoading(enabled = true, shape = CircleShape))
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier.size(26.dp).shimmerLoading(enabled = true, shape = CircleShape))
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Caption Skeleton Lines
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(12.dp)
                    .shimmerLoading(enabled = true, shape = RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(10.dp)
                    .shimmerLoading(enabled = true, shape = RoundedCornerShape(4.dp))
            )
        }
    }
}

/**
 * Full Feed Skeleton when fetching or refreshing data
 */
@Composable
fun ShimmerFeedSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Shimmer Stories Row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(5) {
                ShimmerStoryItemSkeleton()
            }
        }

        // Shimmer Post Cards
        Column(
            modifier = Modifier.padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(3) {
                ShimmerPostSkeleton()
            }
        }
    }
}

/**
 * Shimmer Explore Grid Skeleton when searching or filtering topics
 */
@Composable
fun ShimmerExploreSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(4) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .shimmerLoading(enabled = true, shape = RoundedCornerShape(12.dp))
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .shimmerLoading(enabled = true, shape = RoundedCornerShape(12.dp))
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .shimmerLoading(enabled = true, shape = RoundedCornerShape(12.dp))
                )
            }
        }
    }
}

/**
 * Sync / Data Fetch Banner with rotating indicator and frosted shimmer backdrop
 */
@Composable
fun VibeSyncBanner(
    isSyncing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isSyncing,
        enter = fadeIn(tween(200)),
        exit = fadeOut(tween(200))
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0x558B5CF6),
                            Color(0x33EC4899),
                            Color(0x5506B6D4)
                        )
                    )
                )
                .border(1.dp, FrostedLavender, RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            VibeCircularProgressIndicator(
                size = 18.dp,
                strokeWidth = 2.dp,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Fetching newest vibes from network...",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
