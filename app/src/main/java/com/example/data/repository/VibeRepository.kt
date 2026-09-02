package com.example.data.repository

import com.example.data.local.dao.VibeDao
import com.example.data.local.entity.CommentEntity
import com.example.data.local.entity.DirectMessageEntity
import com.example.data.local.entity.PostEntity
import com.example.data.local.entity.SoundTrackEntity
import com.example.data.local.entity.StoryEntity
import com.example.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

class VibeRepository(private val dao: VibeDao) {

    val allPosts: Flow<List<PostEntity>> = dao.getAllPosts()
    val reels: Flow<List<PostEntity>> = dao.getReels()
    val savedPosts: Flow<List<PostEntity>> = dao.getSavedPosts()
    val likedPosts: Flow<List<PostEntity>> = dao.getLikedPosts()
    val stories: Flow<List<StoryEntity>> = dao.getAllStories()
    val userProfile: Flow<UserProfileEntity?> = dao.getCurrentUserProfile()
    val trendingSounds: Flow<List<SoundTrackEntity>> = dao.getAllSounds()
    val directMessages: Flow<List<DirectMessageEntity>> = dao.getAllMessages()

    fun getCommentsForPost(postId: Long): Flow<List<CommentEntity>> = dao.getCommentsForPost(postId)

    fun getPostsByAuthor(handle: String): Flow<List<PostEntity>> = dao.getPostsByAuthor(handle)

    suspend fun toggleLikePost(postId: Long, currentLiked: Boolean) {
        val newLiked = !currentLiked
        val delta = if (newLiked) 1 else -1
        dao.togglePostLike(postId, newLiked, delta)
    }

    suspend fun toggleSavePost(postId: Long, currentSaved: Boolean) {
        dao.togglePostSave(postId, !currentSaved)
    }

    suspend fun addComment(postId: Long, text: String, authorName: String, authorHandle: String) {
        val comment = CommentEntity(
            postId = postId,
            authorName = authorName,
            authorHandle = authorHandle,
            commentText = text,
            timestamp = System.currentTimeMillis(),
            likesCount = 0,
            isLiked = false
        )
        dao.insertComment(comment)
        dao.incrementPostCommentCount(postId)
    }

    suspend fun toggleCommentLike(commentId: Long, currentLiked: Boolean) {
        val newLiked = !currentLiked
        val delta = if (newLiked) 1 else -1
        dao.toggleCommentLike(commentId, newLiked, delta)
    }

    suspend fun markStorySeen(storyId: Long) {
        dao.markStorySeen(storyId)
    }

    suspend fun createPost(
        caption: String,
        hashtags: String,
        postType: String,
        mediaDrawable: String,
        soundTitle: String,
        soundArtist: String,
        location: String,
        filterName: String,
        authorName: String,
        authorHandle: String,
        spotifyPlaylistUrl: String = "",
        spotifyPlaylistName: String = ""
    ): Long {
        val post = PostEntity(
            authorName = authorName,
            authorHandle = authorHandle,
            postType = postType,
            mediaDrawableName = mediaDrawable,
            caption = caption,
            hashtags = hashtags,
            soundTitle = soundTitle,
            soundArtist = soundArtist,
            spotifyPlaylistUrl = spotifyPlaylistUrl,
            spotifyPlaylistName = spotifyPlaylistName,
            likesCount = 1,
            commentsCount = 0,
            sharesCount = 0,
            vibeScore = 95,
            isLiked = true,
            isSaved = false,
            location = location,
            filterName = filterName,
            timestamp = System.currentTimeMillis()
        )
        return dao.insertPost(post)
    }

    suspend fun createStory(
        caption: String,
        mediaDrawable: String,
        username: String,
        userHandle: String,
        feelingEmoji: String = "✨",
        feelingMood: String = "Euphoric"
    ): Long {
        val story = StoryEntity(
            username = username,
            userHandle = userHandle,
            storyMediaDrawable = mediaDrawable,
            caption = caption,
            feelingEmoji = feelingEmoji,
            feelingMood = feelingMood,
            isSeen = false,
            isLive = false,
            timestamp = System.currentTimeMillis()
        )
        return dao.insertStory(story)
    }

    suspend fun updateProfile(profile: UserProfileEntity) {
        dao.insertOrUpdateProfile(profile)
    }

    suspend fun sendMessage(
        recipientHandle: String,
        recipientName: String,
        text: String,
        isFromMe: Boolean = true,
        isDisappearing: Boolean = false,
        expireMinutes: Int = 0
    ) {
        val msg = DirectMessageEntity(
            senderHandle = recipientHandle,
            senderName = recipientName,
            messageText = text,
            isFromMe = isFromMe,
            unread = !isFromMe,
            isDisappearing = isDisappearing,
            expireMinutes = expireMinutes,
            timestamp = System.currentTimeMillis()
        )
        dao.insertMessage(msg)
    }

    suspend fun setMessageReaction(messageId: Long, reaction: String) {
        dao.setMessageReaction(messageId, reaction)
    }

    suspend fun deleteMessage(messageId: Long) {
        dao.deleteMessage(messageId)
    }

    suspend fun purgeExpiredDisappearingMessages() {
        dao.deleteExpiredMessages(System.currentTimeMillis())
    }

    suspend fun clearConversation(handle: String, name: String) {
        dao.clearConversation(handle, name)
    }
}
