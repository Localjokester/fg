package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.database.VibeDatabase
import com.example.data.local.entity.CommentEntity
import com.example.data.local.entity.DirectMessageEntity
import com.example.data.local.entity.PostEntity
import com.example.data.local.entity.SoundTrackEntity
import com.example.data.local.entity.StoryEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.repository.VibeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VibeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VibeRepository

    init {
        val db = VibeDatabase.getDatabase(application, viewModelScope)
        repository = VibeRepository(db.vibeDao())
    }

    // Posts & Reels
    val allPosts: StateFlow<List<PostEntity>> = repository.allPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reels: StateFlow<List<PostEntity>> = repository.reels
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedPosts: StateFlow<List<PostEntity>> = repository.savedPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val likedPosts: StateFlow<List<PostEntity>> = repository.likedPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Stories
    val stories: StateFlow<List<StoryEntity>> = repository.stories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Profile
    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Sounds
    val trendingSounds: StateFlow<List<SoundTrackEntity>> = repository.trendingSounds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Direct Messages
    val directMessages: StateFlow<List<DirectMessageEntity>> = repository.directMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Search & Explore Filter ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedTopicTag = MutableStateFlow("All")
    val selectedTopicTag: StateFlow<String> = _selectedTopicTag.asStateFlow()

    val filteredExplorePosts: StateFlow<List<PostEntity>> = combine(
        allPosts,
        _searchQuery,
        _selectedTopicTag
    ) { posts, query, topic ->
        posts.filter { post ->
            val matchesQuery = query.isBlank() ||
                    post.caption.contains(query, ignoreCase = true) ||
                    post.hashtags.contains(query, ignoreCase = true) ||
                    post.authorName.contains(query, ignoreCase = true) ||
                    post.authorHandle.contains(query, ignoreCase = true) ||
                    post.soundTitle.contains(query, ignoreCase = true)

            val matchesTopic = topic == "All" ||
                    post.hashtags.contains(topic, ignoreCase = true) ||
                    post.filterName.contains(topic, ignoreCase = true)

            matchesQuery && matchesTopic
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- UI Interactions State ---
    private val _activeCommentsPost = MutableStateFlow<PostEntity?>(null)
    val activeCommentsPost: StateFlow<PostEntity?> = _activeCommentsPost.asStateFlow()

    private val _postComments = MutableStateFlow<List<CommentEntity>>(emptyList())
    val postComments: StateFlow<List<CommentEntity>> = _postComments.asStateFlow()

    private val _activeStory = MutableStateFlow<StoryEntity?>(null)
    val activeStory: StateFlow<StoryEntity?> = _activeStory.asStateFlow()

    private val _activeSoundSheet = MutableStateFlow<SoundTrackEntity?>(null)
    val activeSoundSheet: StateFlow<SoundTrackEntity?> = _activeSoundSheet.asStateFlow()

    private val _shareTargetPost = MutableStateFlow<PostEntity?>(null)
    val shareTargetPost: StateFlow<PostEntity?> = _shareTargetPost.asStateFlow()

    private val _isMusicPlaying = MutableStateFlow(true)
    val isMusicPlaying: StateFlow<Boolean> = _isMusicPlaying.asStateFlow()

    // --- Actions ---
    fun toggleLike(postId: Long, currentLiked: Boolean) {
        viewModelScope.launch {
            repository.toggleLikePost(postId, currentLiked)
        }
    }

    fun toggleSave(postId: Long, currentSaved: Boolean) {
        viewModelScope.launch {
            repository.toggleSavePost(postId, currentSaved)
        }
    }

    fun openComments(post: PostEntity) {
        _activeCommentsPost.value = post
        viewModelScope.launch {
            repository.getCommentsForPost(post.id).collect { comments ->
                _postComments.value = comments
            }
        }
    }

    fun closeComments() {
        _activeCommentsPost.value = null
    }

    fun sendComment(text: String) {
        val currentPost = _activeCommentsPost.value ?: return
        if (text.isBlank()) return
        viewModelScope.launch {
            val profile = userProfile.value
            val authorName = profile?.name ?: "Aria Nova"
            val authorHandle = profile?.handle ?: "arianova.vibe"
            repository.addComment(currentPost.id, text, authorName, authorHandle)
        }
    }

    fun toggleCommentLike(commentId: Long, currentLiked: Boolean) {
        viewModelScope.launch {
            repository.toggleCommentLike(commentId, currentLiked)
        }
    }

    fun openStory(story: StoryEntity) {
        _activeStory.value = story
        viewModelScope.launch {
            repository.markStorySeen(story.id)
        }
    }

    fun closeStory() {
        _activeStory.value = null
    }

    fun openSoundSheet(sound: SoundTrackEntity) {
        _activeSoundSheet.value = sound
    }

    fun closeSoundSheet() {
        _activeSoundSheet.value = null
    }

    fun openShare(post: PostEntity) {
        _shareTargetPost.value = post
    }

    fun closeShare() {
        _shareTargetPost.value = null
    }

    fun toggleMusicPlayback() {
        _isMusicPlaying.value = !_isMusicPlaying.value
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectTopicTag(tag: String) {
        _selectedTopicTag.value = tag
    }

    fun createNewPost(
        caption: String,
        hashtags: String,
        postType: String,
        mediaDrawable: String,
        soundTitle: String,
        soundArtist: String,
        location: String,
        filterName: String
    ) {
        viewModelScope.launch {
            val profile = userProfile.value
            val authorName = profile?.name ?: "Aria Nova"
            val authorHandle = profile?.handle ?: "arianova.vibe"
            repository.createPost(
                caption = caption,
                hashtags = hashtags,
                postType = postType,
                mediaDrawable = mediaDrawable,
                soundTitle = soundTitle,
                soundArtist = soundArtist,
                location = location,
                filterName = filterName,
                authorName = authorName,
                authorHandle = authorHandle
            )
        }
    }

    fun createNewStory(caption: String, mediaDrawable: String) {
        viewModelScope.launch {
            val profile = userProfile.value
            val authorName = profile?.name ?: "Aria Nova"
            val authorHandle = profile?.handle ?: "arianova.vibe"
            repository.createStory(
                caption = caption,
                mediaDrawable = mediaDrawable,
                username = authorName,
                userHandle = authorHandle
            )
        }
    }

    fun updateBio(newBio: String, newName: String, newHandle: String, newAnthem: String) {
        viewModelScope.launch {
            val current = userProfile.value ?: UserProfileEntity()
            val updated = current.copy(
                bio = newBio,
                name = newName,
                handle = newHandle,
                anthemTitle = newAnthem
            )
            repository.updateProfile(updated)
        }
    }

    fun sendDirectMessage(recipientHandle: String, recipientName: String, text: String) {
        viewModelScope.launch {
            repository.sendMessage(recipientHandle, recipientName, text)
        }
    }
}
