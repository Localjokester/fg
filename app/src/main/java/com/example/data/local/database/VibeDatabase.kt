package com.example.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.VibeDao
import com.example.data.local.entity.CommentEntity
import com.example.data.local.entity.DirectMessageEntity
import com.example.data.local.entity.PostEntity
import com.example.data.local.entity.SoundTrackEntity
import com.example.data.local.entity.StoryEntity
import com.example.data.local.entity.UserProfileEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        PostEntity::class,
        StoryEntity::class,
        CommentEntity::class,
        UserProfileEntity::class,
        SoundTrackEntity::class,
        DirectMessageEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class VibeDatabase : RoomDatabase() {

    abstract fun vibeDao(): VibeDao

    companion object {
        @Volatile
        private var INSTANCE: VibeDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): VibeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VibeDatabase::class.java,
                    "vibesphere_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.vibeDao())
                    }
                }
            }
        }

        suspend fun populateInitialData(dao: VibeDao) {
            // Seed Current Profile
            dao.insertOrUpdateProfile(
                UserProfileEntity(
                    userId = "current_user",
                    name = "Aria Nova",
                    handle = "arianova.vibe",
                    bio = "Visual artist & sound designer 🎧 | Capturing neon sunsets, lo-fi moments & festival energy ✨ #VibesphereOriginal",
                    avatarUrl = "",
                    avatarDrawable = "img_app_icon",
                    followersCount = 42800,
                    followingCount = 384,
                    totalVibesCount = 129400,
                    anthemTitle = "Midnight Echoes (Vibe Edit)",
                    anthemArtist = "Aria Nova x Synthwave",
                    badge = "Verified Creator"
                )
            )

            // Seed Stories
            val stories = listOf(
                StoryEntity(
                    id = 1,
                    username = "Your Vibe",
                    userHandle = "arianova.vibe",
                    storyMediaDrawable = "img_vibe_sunset",
                    caption = "Golden hour vibes in the city skyline 🌆",
                    feelingEmoji = "✨",
                    feelingMood = "Euphoric",
                    isSeen = false,
                    isLive = false
                ),
                StoryEntity(
                    id = 2,
                    username = "Kira Volt",
                    userHandle = "kira.volt",
                    storyMediaDrawable = "img_reel_cyber_dance",
                    caption = "Tokyo midnight street choreo dropping soon! ⚡💃",
                    feelingEmoji = "⚡",
                    feelingMood = "Chaotic",
                    isSeen = false,
                    isLive = true
                ),
                StoryEntity(
                    id = 3,
                    username = "Leo Sun",
                    userHandle = "leo_aesthetic",
                    storyMediaDrawable = "img_reel_cafe_aesthetic",
                    caption = "Matcha mornings & good tunes 🍵📖",
                    feelingEmoji = "☕",
                    feelingMood = "Caffeinated",
                    isSeen = false,
                    isLive = false
                ),
                StoryEntity(
                    id = 4,
                    username = "Maya Echo",
                    userHandle = "maya.beats",
                    storyMediaDrawable = "img_vibe_concert",
                    caption = "Bass was shaking the whole venue! 🔮🎶",
                    feelingEmoji = "🎧",
                    feelingMood = "In The Zone",
                    isSeen = false,
                    isLive = false
                ),
                StoryEntity(
                    id = 5,
                    username = "Pixel Drift",
                    userHandle = "pixel.drift",
                    storyMediaDrawable = "img_vibe_sunset",
                    caption = "Retro synthwave wave riding 🚀",
                    feelingEmoji = "🚀",
                    feelingMood = "Hyped",
                    isSeen = true,
                    isLive = false
                )
            )
            dao.insertStories(stories)

            // Seed Posts & Reels (TikTok & Instagram Hybrid)
            val posts = listOf(
                PostEntity(
                    id = 1,
                    authorName = "Kira Volt",
                    authorHandle = "kira.volt",
                    postType = "REEL",
                    mediaDrawableName = "img_reel_cyber_dance",
                    caption = "Cyber rhythm under the neon lights ✨ Track dropped by @cyberbeats! Would you try this dance routine? 💃🔥",
                    hashtags = "#DanceReels #Cyberpunk #NeonVibes #TikTokDance #TokyoNights",
                    soundTitle = "Cybernetic Pulse (Bass Boost)",
                    soundArtist = "Kira Volt x GlitchMob",
                    likesCount = 28400,
                    commentsCount = 1420,
                    sharesCount = 520,
                    vibeScore = 99,
                    isLiked = true,
                    isSaved = true,
                    location = "Akihabara, Tokyo",
                    filterName = "Neon Glitch"
                ),
                PostEntity(
                    id = 2,
                    authorName = "Leo Sun",
                    authorHandle = "leo_aesthetic",
                    postType = "PHOTO",
                    mediaDrawableName = "img_vibe_sunset",
                    caption = "Dusk in the metropolis. The way the light reflects on rainy streets will never get old. Where's your favorite sunset spot? 🌆✨",
                    hashtags = "#SunsetPhotography #InstaGood #CityGram #AestheticFeed #GoldenHour",
                    soundTitle = "Lo-Fi Dusk Reverie",
                    soundArtist = "ChilledCow Vibes",
                    likesCount = 14820,
                    commentsCount = 612,
                    sharesCount = 189,
                    vibeScore = 95,
                    isLiked = false,
                    isSaved = false,
                    location = "Shibuya Sky, Tokyo",
                    filterName = "Golden Pastel"
                ),
                PostEntity(
                    id = 3,
                    authorName = "Maya Echo",
                    authorHandle = "maya.beats",
                    postType = "REEL",
                    mediaDrawableName = "img_vibe_concert",
                    caption = "When the beat drops at 2 AM and 50,000 people become one frequency ⚡🔊 Energy was unmatched! Who's going to Electric Sphere 2026? 🌌",
                    hashtags = "#EDMFestival #LaserShow #RaveCulture #ReelsTrending #MusicVibes",
                    soundTitle = "Hyperdrive Frequency (Live)",
                    soundArtist = "Maya Echo ft. LaserCore",
                    likesCount = 45900,
                    commentsCount = 2890,
                    sharesCount = 1420,
                    vibeScore = 100,
                    isLiked = true,
                    isSaved = false,
                    location = "Sphere Arena, Las Vegas",
                    filterName = "Prism Glow"
                ),
                PostEntity(
                    id = 4,
                    authorName = "Sora Nomad",
                    authorHandle = "sora.nomad",
                    postType = "REEL",
                    mediaDrawableName = "img_reel_cafe_aesthetic",
                    caption = "Pouring calm into a chaotic week. Oat milk latte + vintage thoughts ☕ What's your daily recharge ritual? ✨",
                    hashtags = "#CafeVibes #CozyCore #AestheticReels #CoffeeTime #Mindfulness",
                    soundTitle = "Warm Cinnamon Morning",
                    soundArtist = "Acoustic Sunsets",
                    likesCount = 19200,
                    commentsCount = 415,
                    sharesCount = 310,
                    vibeScore = 92,
                    isLiked = false,
                    isSaved = true,
                    location = "Cafe Kitsune, Paris",
                    filterName = "Warm Film"
                ),
                PostEntity(
                    id = 5,
                    authorName = "Aria Nova",
                    authorHandle = "arianova.vibe",
                    postType = "PHOTO",
                    mediaDrawableName = "img_app_icon",
                    caption = "Welcome to Vibesphere 🔮 The new home for high-energy reels, aesthetic photo drops, and endless good frequency. Drop a 💜 in the comments if you're vibing!",
                    hashtags = "#Vibesphere #CreativeStudio #NeonCommunity #VibeSphereLaunch",
                    soundTitle = "Vibesphere Theme Anthem",
                    soundArtist = "Aria Nova",
                    likesCount = 58200,
                    commentsCount = 3450,
                    sharesCount = 2100,
                    vibeScore = 100,
                    isLiked = true,
                    isSaved = true,
                    location = "Global Frequency",
                    filterName = "Cosmic Glass"
                )
            )
            dao.insertPosts(posts)

            // Seed Comments
            val comments = listOf(
                CommentEntity(
                    postId = 1,
                    authorName = "Maya Echo",
                    authorHandle = "maya.beats",
                    commentText = "The footwork at 0:15 was absolutely insane!! 🔥🔥🔥",
                    likesCount = 324,
                    isLiked = true
                ),
                CommentEntity(
                    postId = 1,
                    authorName = "Sora Nomad",
                    authorHandle = "sora.nomad",
                    commentText = "The lighting matched the beat drop perfectly 💜",
                    likesCount = 98,
                    isLiked = false
                ),
                CommentEntity(
                    postId = 1,
                    authorName = "Aria Nova",
                    authorHandle = "arianova.vibe",
                    commentText = "Need this full audio track on repeat ASAP! 🎧✨",
                    likesCount = 145,
                    isLiked = true
                ),
                CommentEntity(
                    postId = 2,
                    authorName = "Kira Volt",
                    authorHandle = "kira.volt",
                    commentText = "That color grading is pure magic 🌅",
                    likesCount = 89,
                    isLiked = true
                ),
                CommentEntity(
                    postId = 3,
                    authorName = "Leo Sun",
                    authorHandle = "leo_aesthetic",
                    commentText = "Take me back to this night!! The lasers were wild ⚡",
                    likesCount = 210,
                    isLiked = false
                )
            )
            comments.forEach { dao.insertComment(it) }

            // Seed Sound Tracks
            val sounds = listOf(
                SoundTrackEntity(
                    id = 1,
                    title = "Cybernetic Pulse (Bass Boost)",
                    artist = "Kira Volt x GlitchMob",
                    coverDrawable = "img_reel_cyber_dance",
                    vibesCount = 142000,
                    isTrending = true
                ),
                SoundTrackEntity(
                    id = 2,
                    title = "Hyperdrive Frequency (Live)",
                    artist = "Maya Echo ft. LaserCore",
                    coverDrawable = "img_vibe_concert",
                    vibesCount = 98000,
                    isTrending = true
                ),
                SoundTrackEntity(
                    id = 3,
                    title = "Lo-Fi Dusk Reverie",
                    artist = "ChilledCow Vibes",
                    coverDrawable = "img_vibe_sunset",
                    vibesCount = 64000,
                    isTrending = true
                ),
                SoundTrackEntity(
                    id = 4,
                    title = "Warm Cinnamon Morning",
                    artist = "Acoustic Sunsets",
                    coverDrawable = "img_reel_cafe_aesthetic",
                    vibesCount = 43000,
                    isTrending = false
                ),
                SoundTrackEntity(
                    id = 5,
                    title = "Vibesphere Theme Anthem",
                    artist = "Aria Nova",
                    coverDrawable = "img_app_icon",
                    vibesCount = 215000,
                    isTrending = true
                )
            )
            dao.insertSounds(sounds)

            // Seed Direct Messages
            val messages = listOf(
                DirectMessageEntity(
                    senderHandle = "kira.volt",
                    senderName = "Kira Volt",
                    messageText = "Hey Aria! Loved your latest post remix! Are we duetting this Friday? 💃✨",
                    isFromMe = false,
                    unread = true
                ),
                DirectMessageEntity(
                    senderHandle = "maya.beats",
                    senderName = "Maya Echo",
                    messageText = "Sent you the sound stem files for the new track 🎵 Check them out!",
                    isFromMe = false,
                    unread = false
                ),
                DirectMessageEntity(
                    senderHandle = "leo_aesthetic",
                    senderName = "Leo Sun",
                    messageText = "That golden hour filter preset is fire 🔥 Thanks for sharing!",
                    isFromMe = false,
                    unread = false
                )
            )
            messages.forEach { dao.insertMessage(it) }
        }
    }
}
