package com.example.ui.feed

import com.example.R
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.PostEntity
import com.example.data.local.entity.SoundTrackEntity
import com.example.data.local.entity.StoryEntity
import com.example.ui.components.DoubleTapLikeArea
import com.example.ui.components.MusicTicker
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
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VibeBackground
import com.example.ui.theme.VibeCardBg
import com.example.ui.theme.VibeStoryGradient
import com.example.ui.theme.VibeSurfaceVariant

@Composable
fun FeedScreen(
    posts: List<PostEntity>,
    stories: List<StoryEntity>,
    onStoryClick: (StoryEntity) -> Unit,
    onAddStoryClick: () -> Unit,
    onToggleLike: (Long, Boolean) -> Unit,
    onToggleSave: (Long, Boolean) -> Unit,
    onOpenComments: (PostEntity) -> Unit,
    onOpenShare: (PostEntity) -> Unit,
    onSoundClick: (SoundTrackEntity) -> Unit,
    onDirectComment: (PostEntity, String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("feed_screen_list"),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Stories Carousel Row
        item {
            StoriesCarouselRow(
                stories = stories,
                onStoryClick = onStoryClick,
                onAddStoryClick = onAddStoryClick
            )
        }

        // Feed Posts
        items(posts, key = { it.id }) { post ->
            FeedPostCard(
                post = post,
                onToggleLike = { onToggleLike(post.id, post.isLiked) },
                onToggleSave = { onToggleSave(post.id, post.isSaved) },
                onOpenComments = { onOpenComments(post) },
                onOpenShare = { onOpenShare(post) },
                onSoundClick = {
                    onSoundClick(
                        SoundTrackEntity(
                            title = post.soundTitle,
                            artist = post.soundArtist,
                            coverDrawable = post.mediaDrawableName,
                            vibesCount = post.likesCount * 3
                        )
                    )
                },
                onSendDirectComment = { text -> onDirectComment(post, text) }
            )
        }
    }
}

/**
 * Stories Carousel (Frosted Glass style with lavender gradient halos and Add Story button)
 */
@Composable
fun StoriesCarouselRow(
    stories: List<StoryEntity>,
    onStoryClick: (StoryEntity) -> Unit,
    onAddStoryClick: () -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // "Your Vibe" Story Adder Button
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable(onClick = onAddStoryClick)
                    .testTag("add_story_button")
            ) {
                Box(
                    modifier = Modifier.size(64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, Color(0x40FFFFFF), CircleShape)
                            .background(Color(0x26FFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon),
                            contentDescription = "Your Story",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                        )
                    }

                    // Plus Badge
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(FrostedLavender)
                            .border(1.5.dp, Color(0xFF1E1B4B), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Story",
                            tint = Color.Black,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Your Vibe",
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Friends / Trending Stories
        items(stories, key = { it.id }) { story ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onStoryClick(story) }
            ) {
                VibeRingAvatar(
                    drawableName = story.storyMediaDrawable,
                    size = 64.dp,
                    hasStory = true,
                    isSeen = story.isSeen,
                    isLive = story.isLive,
                    onClick = { onStoryClick(story) }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = story.username,
                    color = if (story.isSeen) TextMuted else TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(66.dp)
                )
            }
        }
    }
}

/**
 * Frosted Glass Post Card with Double-Tap Heart, Music Ticker, Vibe Badge, and Quick Interaction
 */
@Composable
fun FeedPostCard(
    post: PostEntity,
    onToggleLike: () -> Unit,
    onToggleSave: () -> Unit,
    onOpenComments: () -> Unit,
    onOpenShare: () -> Unit,
    onSoundClick: () -> Unit,
    onSendDirectComment: (String) -> Unit
) {
    var quickCommentText by remember { mutableStateOf("") }
    var isExpandedCaption by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0x331E1B4B))
            .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(24.dp))
            .testTag("feed_post_card_${post.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Post Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    VibeRingAvatar(
                        drawableName = post.mediaDrawableName,
                        size = 40.dp,
                        hasStory = true,
                        isSeen = false
                    )

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = post.authorName,
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (post.postType == "REEL") {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0x33D0BCFF))
                                        .border(0.5.dp, Color(0x66D0BCFF), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "REEL",
                                        color = FrostedLavender,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }

                        Text(
                            text = "${post.location} • @${post.authorHandle}",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(onClick = onOpenShare) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = TextSecondary
                    )
                }
            }

            // Post Media Container with Double Tap Heart
            DoubleTapLikeArea(
                onDoubleTap = onToggleLike,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(if (post.postType == "REEL") 4f / 5f else 1f)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = getDrawableResByName(post.mediaDrawableName)),
                        contentDescription = post.caption,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Reel Play Overlay Indicator
                    if (post.postType == "REEL") {
                        Box(
                            modifier = Modifier
                                .padding(12.dp)
                                .align(Alignment.TopEnd)
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0x66000000))
                                .border(1.dp, Color(0x33FFFFFF), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Reel Video",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Frosted Vibe Score & Filter Badge Overlay at bottom of media
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color(0xCC0F0F0F))
                                )
                            )
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        VibeScoreBadge(score = post.vibeScore)

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x33000000))
                                .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "✨ ${post.filterName}",
                                color = TextPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Interactive Actions Bar (Like, Comment, Remix/Share, Save)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Like Action
                    IconButton(
                        onClick = onToggleLike,
                        modifier = Modifier.testTag("post_like_button_${post.id}")
                    ) {
                        Icon(
                            imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (post.isLiked) HeartRed else TextPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    // Comment Action
                    IconButton(
                        onClick = onOpenComments,
                        modifier = Modifier.testTag("post_comment_button_${post.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = "Comment",
                            tint = TextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Remix / Share Action
                    IconButton(onClick = onOpenShare) {
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = "Remix or Share",
                            tint = TextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Bookmark / Save Action
                IconButton(
                    onClick = onToggleSave,
                    modifier = Modifier.testTag("post_save_button_${post.id}")
                ) {
                    Icon(
                        imageVector = if (post.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save",
                        tint = if (post.isSaved) NeonGold else TextPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            // Post Details & Music Ticker
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 2.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Like count & Vibe count
                Text(
                    text = "${post.likesCount} likes • ${post.sharesCount} vibe remixes",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                // Caption with Author Handle
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "${post.authorName}  ",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = post.caption,
                        color = TextSecondary,
                        fontSize = 13.sp,
                        maxLines = if (isExpandedCaption) 10 else 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable { isExpandedCaption = !isExpandedCaption }
                    )
                }

                // Hashtags
                if (post.hashtags.isNotBlank()) {
                    Text(
                        text = post.hashtags,
                        color = FrostedLavender,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Music Ticker
                MusicTicker(
                    title = post.soundTitle,
                    artist = post.soundArtist,
                    onClick = onSoundClick
                )

                // "View all X comments"
                Text(
                    text = "View all ${post.commentsCount} comments",
                    color = TextMuted,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .clickable(onClick = onOpenComments)
                        .padding(vertical = 2.dp)
                )

                // Inline quick comment field with frosted background
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VibeRingAvatar(
                        drawableName = "img_app_icon",
                        size = 28.dp,
                        hasStory = false
                    )

                    OutlinedTextField(
                        value = quickCommentText,
                        onValueChange = { quickCommentText = it },
                        placeholder = { Text("Add a vibe comment...", color = TextMuted, fontSize = 12.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(22.dp),
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

                    if (quickCommentText.isNotBlank()) {
                        IconButton(
                            onClick = {
                                onSendDirectComment(quickCommentText)
                                quickCommentText = ""
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(FrostedLavender)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Post Comment",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
