package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val authorName: String,
    val authorHandle: String,
    val authorAvatarUrl: String = "",
    val postType: String = "PHOTO", // "PHOTO", "REEL", "CAROUSEL"
    val mediaDrawableName: String = "img_vibe_sunset",
    val caption: String,
    val hashtags: String, // comma or space separated
    val soundTitle: String = "Midnight City Lights",
    val soundArtist: String = "Kavinsky & VibeSphere",
    val soundCoverDrawable: String = "img_app_icon",
    val likesCount: Int = 1240,
    val commentsCount: Int = 89,
    val sharesCount: Int = 34,
    val vibeScore: Int = 98,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val location: String = "Tokyo, Shibuya",
    val filterName: String = "Cyber Neon"
)

@Entity(tableName = "stories")
data class StoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val userHandle: String,
    val userAvatarUrl: String = "",
    val storyMediaDrawable: String = "img_reel_cyber_dance",
    val caption: String = "Living in the moment ✨",
    val isSeen: Boolean = false,
    val isLive: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val postId: Long,
    val authorName: String,
    val authorHandle: String,
    val authorAvatarUrl: String = "",
    val commentText: String,
    val timestamp: Long = System.currentTimeMillis(),
    val likesCount: Int = 0,
    val isLiked: Boolean = false
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val userId: String = "current_user",
    val name: String = "Aria Nova",
    val handle: String = "arianova.vibe",
    val bio: String = "Visual artist & sound designer 🎧 | Living between neon dreams and lo-fi beats ✨ #VibesphereCreator",
    val avatarUrl: String = "",
    val avatarDrawable: String = "img_app_icon",
    val followersCount: Int = 42800,
    val followingCount: Int = 384,
    val totalVibesCount: Int = 129000,
    val anthemTitle: String = "Neon Horizon (Extended Mix)",
    val anthemArtist: String = "CyberAura",
    val badge: String = "Verified Creator"
)

@Entity(tableName = "sound_tracks")
data class SoundTrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val artist: String,
    val coverDrawable: String = "img_app_icon",
    val durationSeconds: Int = 30,
    val vibesCount: Int = 15200,
    val isTrending: Boolean = true
)

@Entity(tableName = "direct_messages")
data class DirectMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val senderHandle: String,
    val senderName: String,
    val messageText: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFromMe: Boolean = false,
    val unread: Boolean = false
)
