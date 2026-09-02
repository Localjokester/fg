package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.CommentEntity
import com.example.data.local.entity.DirectMessageEntity
import com.example.data.local.entity.PostEntity
import com.example.data.local.entity.SoundTrackEntity
import com.example.data.local.entity.StoryEntity
import com.example.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VibeDao {

    // --- Posts / Reels ---
    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE postType = 'REEL' ORDER BY timestamp DESC")
    fun getReels(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE isSaved = 1 ORDER BY timestamp DESC")
    fun getSavedPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE isLiked = 1 ORDER BY timestamp DESC")
    fun getLikedPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE authorHandle = :handle ORDER BY timestamp DESC")
    fun getPostsByAuthor(handle: String): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :postId LIMIT 1")
    suspend fun getPostById(postId: Long): PostEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Update
    suspend fun updatePost(post: PostEntity)

    @Query("UPDATE posts SET isLiked = :isLiked, likesCount = likesCount + :delta WHERE id = :postId")
    suspend fun togglePostLike(postId: Long, isLiked: Boolean, delta: Int)

    @Query("UPDATE posts SET isSaved = :isSaved WHERE id = :postId")
    suspend fun togglePostSave(postId: Long, isSaved: Boolean)

    @Query("UPDATE posts SET commentsCount = commentsCount + 1 WHERE id = :postId")
    suspend fun incrementPostCommentCount(postId: Long)

    @Query("DELETE FROM posts WHERE id = :postId")
    suspend fun deletePost(postId: Long)

    // --- Stories ---
    @Query("SELECT * FROM stories ORDER BY timestamp DESC")
    fun getAllStories(): Flow<List<StoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<StoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: StoryEntity): Long

    @Query("UPDATE stories SET isSeen = 1 WHERE id = :storyId")
    suspend fun markStorySeen(storyId: Long)

    // --- Comments ---
    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY timestamp ASC")
    fun getCommentsForPost(postId: Long): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity): Long

    @Query("UPDATE comments SET isLiked = :isLiked, likesCount = likesCount + :delta WHERE id = :commentId")
    suspend fun toggleCommentLike(commentId: Long, isLiked: Boolean, delta: Int)

    // --- User Profile ---
    @Query("SELECT * FROM user_profile WHERE userId = 'current_user' LIMIT 1")
    fun getCurrentUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    // --- Sound Tracks ---
    @Query("SELECT * FROM sound_tracks ORDER BY vibesCount DESC")
    fun getAllSounds(): Flow<List<SoundTrackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSounds(sounds: List<SoundTrackEntity>)

    // --- Direct Messages ---
    @Query("SELECT * FROM direct_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<DirectMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: DirectMessageEntity): Long
}
