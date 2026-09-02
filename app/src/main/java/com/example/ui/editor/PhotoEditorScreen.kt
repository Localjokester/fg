package com.example.ui.editor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.getDrawableResByName
import com.example.ui.theme.FrostedBackdropGradient
import com.example.ui.theme.FrostedLavender
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGold
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VibeBackground

data class EditorFilter(
    val id: String,
    val name: String,
    val tagLine: String,
    val tintOverlay: Color = Color.Transparent,
    val saturation: Float = 1f,
    val contrast: Float = 1f,
    val brightness: Float = 0f,
    val warmthR: Float = 1f,
    val warmthB: Float = 1f
)

data class EditorSticker(
    val id: String,
    val text: String,
    val bgColor: Color,
    val textColor: Color
)

@Composable
fun PhotoEditorScreen(
    initialMediaDrawable: String = "img_vibe_sunset",
    onDismiss: () -> Unit,
    onSaveAndPublish: (editedDrawable: String, filterName: String, caption: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val mediaLibrary = remember {
        mutableStateListOf(
            "img_vibe_sunset",
            "img_reel_cyber_dance",
            "img_reel_cafe_aesthetic",
            "img_vibe_concert",
            "img_app_icon"
        )
    }

    var currentMedia by remember { mutableStateOf(initialMediaDrawable) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val uriString = uri.toString()
            if (!mediaLibrary.contains(uriString)) {
                mediaLibrary.add(0, uriString)
            }
            currentMedia = uriString
        }
    }

    val filterPresets = listOf(
        EditorFilter("normal", "Natural", "Original Tone", Color.Transparent, 1f, 1f, 0f),
        EditorFilter("cyber", "Cyber Vibe", "Lavender & Magenta Aura", Color(0x33D0BCFF), 1.3f, 1.2f, 10f, 1.1f, 1.3f),
        EditorFilter("golden", "Golden Glow", "Warm Sunbeam", Color(0x33FFAA00), 1.2f, 1.1f, 15f, 1.3f, 0.8f),
        EditorFilter("noir", "Tokyo Noir", "Moody Contrast", Color(0x2200E5FF), 0f, 1.4f, -10f, 1f, 1f),
        EditorFilter("synthwave", "Synth Glitch", "Retro Violet", Color(0x338F00FF), 1.4f, 1.3f, 5f, 1.2f, 1.4f),
        EditorFilter("frost", "Arctic Cyan", "Cool Mint Breeze", Color(0x3300E5FF), 1.1f, 1.15f, 5f, 0.8f, 1.3f),
        EditorFilter("emerald", "Emerald Neon", "Futuristic Matrix", Color(0x3300FF88), 1.2f, 1.2f, 0f, 0.9f, 1.1f),
        EditorFilter("candy", "Pastel Dream", "Soft Rose Aura", Color(0x33FF2A85), 1.1f, 1.05f, 20f, 1.2f, 1.1f)
    )

    var selectedFilter by remember { mutableStateOf(filterPresets[0]) }

    // Adjustment slider states
    var brightness by remember { mutableFloatStateOf(0f) } // -50 to +50
    var contrast by remember { mutableFloatStateOf(1f) }   // 0.5 to 1.5
    var saturation by remember { mutableFloatStateOf(1f) } // 0 to 2
    var warmth by remember { mutableFloatStateOf(0f) }     // -50 to +50
    var vignetteAlpha by remember { mutableFloatStateOf(0f) } // 0 to 1

    // Transform states
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    var isFlippedHorizontally by remember { mutableStateOf(false) }
    var selectedAspectRatio by remember { mutableFloatStateOf(4f / 3f) } // 1f, 4f/3f, 4f/5f, 9f/16f

    // Stickers / Badges
    val availableStickers = listOf(
        EditorSticker("vibe", "⚡ VIBESPHERE", Color(0xCC1E1B4B), FrostedLavender),
        EditorSticker("aura", "✨ 100% AURA", Color(0xCC312E81), NeonCyan),
        EditorSticker("golden", "☀️ GOLDEN HOUR", Color(0xCC7C2D12), NeonGold),
        EditorSticker("tokyo", "🌃 TOKYO NIGHTS", Color(0xCC000000), Color.White),
        EditorSticker("cyber", "🔮 FREQUENCY", Color(0xCC831843), NeonMagenta)
    )
    var activeSticker by remember { mutableStateOf<EditorSticker?>(null) }

    // Editor Tab: 0 = Filters, 1 = Adjustments, 2 = Crop & Transform, 3 = Stickers
    var editorTab by remember { mutableIntStateOf(0) }
    var isHoldingOriginal by remember { mutableStateOf(false) }

    // Build composed ColorMatrix based on filter + manual sliders
    val colorMatrix = remember(selectedFilter, brightness, contrast, saturation, warmth, isHoldingOriginal) {
        if (isHoldingOriginal) {
            ColorMatrix()
        } else {
            val cm = ColorMatrix()
            val finalSat = (selectedFilter.saturation * saturation).coerceIn(0f, 2f)
            cm.setToSaturation(finalSat)

            val totalBrightness = selectedFilter.brightness + brightness
            val totalContrast = selectedFilter.contrast * contrast
            val warmFactor = warmth / 100f

            val rScale = totalContrast * (selectedFilter.warmthR + warmFactor).coerceAtLeast(0.1f)
            val gScale = totalContrast
            val bScale = totalContrast * (selectedFilter.warmthB - warmFactor).coerceAtLeast(0.1f)

            val array = floatArrayOf(
                rScale, 0f, 0f, 0f, totalBrightness,
                0f, gScale, 0f, 0f, totalBrightness,
                0f, 0f, bScale, 0f, totalBrightness,
                0f, 0f, 0f, 1f, 0f
            )
            ColorMatrix(array)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VibeBackground)
            .testTag("photo_editor_screen")
    ) {
        // Ambient background gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FrostedBackdropGradient)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Top Action Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Cancel / Close
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0x26FFFFFF))
                        .border(1.dp, Color(0x33FFFFFF), CircleShape)
                        .testTag("editor_close_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel",
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Title and Compare Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Vibe Studio",
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Hold for Original button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isHoldingOriginal) FrostedLavender else Color(0x26FFFFFF))
                            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(12.dp))
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        isHoldingOriginal = true
                                        tryAwaitRelease()
                                        isHoldingOriginal = false
                                    }
                                )
                            }
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                            .testTag("compare_original_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                tint = if (isHoldingOriginal) Color.Black else TextSecondary,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = if (isHoldingOriginal) "Original" else "Hold Compare",
                                color = if (isHoldingOriginal) Color.Black else TextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Done / Next Button
                Button(
                    onClick = {
                        onSaveAndPublish(
                            currentMedia,
                            selectedFilter.name,
                            "Crafted in Vibesphere Studio ✨ Filter: ${selectedFilter.name}"
                        )
                    },
                    modifier = Modifier
                        .height(38.dp)
                        .testTag("editor_done_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = FrostedLavender),
                    shape = RoundedCornerShape(19.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Done",
                        color = Color.Black,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Main Canvas Viewport with Live Filters & Overlays
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(selectedAspectRatio)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0x331E1B4B))
                        .border(1.5.dp, Color(0x33FFFFFF), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Filtered Base Image with transformations
                    val isUri = currentMedia.startsWith("content://") || currentMedia.startsWith("file://") || currentMedia.startsWith("http")
                    val imagePainter = if (isUri) {
                        rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(currentMedia)
                                .crossfade(true)
                                .build()
                        )
                    } else {
                        painterResource(id = getDrawableResByName(currentMedia))
                    }

                    Image(
                        painter = imagePainter,
                        contentDescription = "Edited Image",
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.colorMatrix(colorMatrix),
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(rotationAngle)
                            .scale(scaleX = if (isFlippedHorizontally) -1f else 1f, scaleY = 1f)
                    )

                    // Overlay Filter Tint (if not holding original)
                    if (!isHoldingOriginal && selectedFilter.tintOverlay != Color.Transparent) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(selectedFilter.tintOverlay)
                        )
                    }

                    // Vignette Overlay
                    if (!isHoldingOriginal && vignetteAlpha > 0f) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.radialGradient(
                                        listOf(Color.Transparent, Color(0xEE000000).copy(alpha = vignetteAlpha))
                                    )
                                )
                        )
                    }

                    // Active Sticker Badge Overlay
                    activeSticker?.let { sticker ->
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(sticker.bgColor)
                                .border(1.dp, sticker.textColor.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                .clickable { activeSticker = null }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = sticker.text,
                                color = sticker.textColor,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    // Filter Name Badge (top-left)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x66000000))
                            .border(0.5.dp, Color(0x33FFFFFF), RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "✨ ${if (isHoldingOriginal) "Original" else selectedFilter.name}",
                            color = FrostedLavender,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Quick Asset Switcher Strip with Extract from Device button
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x26FFFFFF))
                            .border(1.dp, FrostedLavender, RoundedCornerShape(10.dp))
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            .testTag("editor_extract_photo_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = "Extract Photo",
                            tint = FrostedLavender,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                items(mediaLibrary) { mediaName ->
                    val isCurrent = mediaName == currentMedia
                    val isUriMedia = mediaName.startsWith("content://") || mediaName.startsWith("file://") || mediaName.startsWith("http")
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                1.5.dp,
                                if (isCurrent) FrostedLavender else Color(0x26FFFFFF),
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { currentMedia = mediaName }
                    ) {
                        if (isUriMedia) {
                            coil.compose.AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(mediaName)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Image(
                                painter = painterResource(id = getDrawableResByName(mediaName)),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

            // Bottom Editing Control Panel (Frosted Card)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0x331E1B4B))
                    .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(24.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Editor Category Tabs
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x26FFFFFF))
                            .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(16.dp))
                    ) {
                        TabRow(
                            selectedTabIndex = editorTab,
                            containerColor = Color.Transparent,
                            contentColor = TextPrimary,
                            indicator = { tabPositions ->
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[editorTab]),
                                    color = FrostedLavender,
                                    height = 2.5.dp
                                )
                            },
                            divider = {}
                        ) {
                            val tabs = listOf("Filters", "Adjust", "Crop/Flip", "Aura Stamps")
                            tabs.forEachIndexed { index, label ->
                                Tab(
                                    selected = editorTab == index,
                                    onClick = { editorTab = index },
                                    text = {
                                        Text(
                                            text = label,
                                            fontSize = 11.sp,
                                            fontWeight = if (editorTab == index) FontWeight.Bold else FontWeight.Medium,
                                            color = if (editorTab == index) FrostedLavender else TextSecondary
                                        )
                                    }
                                )
                            }
                        }
                    }

                    // Content based on selected editor tab
                    when (editorTab) {
                        0 -> {
                            // Filters Preset Row
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(filterPresets) { filter ->
                                    val isSelected = selectedFilter.id == filter.id
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(if (isSelected) Color(0x33D0BCFF) else Color(0x1AFFFFFF))
                                            .border(
                                                1.dp,
                                                if (isSelected) FrostedLavender else Color(0x26FFFFFF),
                                                RoundedCornerShape(14.dp)
                                            )
                                            .clickable { selectedFilter = filter }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                            .testTag("filter_item_${filter.id}")
                                    ) {
                                        // Mini preview swatch
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    if (filter.tintOverlay != Color.Transparent) filter.tintOverlay
                                                    else Color(0x44FFFFFF)
                                                )
                                                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(8.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Image(
                                                painter = painterResource(id = getDrawableResByName(currentMedia)),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                            if (filter.tintOverlay != Color.Transparent) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .background(filter.tintOverlay)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = filter.name,
                                            color = if (isSelected) FrostedLavender else TextPrimary,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }

                        1 -> {
                            // Sliders: Brightness, Contrast, Saturation, Warmth, Vignette
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                // Brightness
                                SliderControlRow(
                                    label = "Brightness",
                                    icon = Icons.Default.Brightness6,
                                    value = brightness,
                                    valueRange = -50f..50f,
                                    displayValue = "${brightness.toInt()}",
                                    onValueChange = { brightness = it },
                                    onReset = { brightness = 0f }
                                )

                                // Contrast
                                SliderControlRow(
                                    label = "Contrast",
                                    icon = Icons.Default.Contrast,
                                    value = contrast,
                                    valueRange = 0.5f..1.5f,
                                    displayValue = "%.1fx".format(contrast),
                                    onValueChange = { contrast = it },
                                    onReset = { contrast = 1f }
                                )

                                // Saturation
                                SliderControlRow(
                                    label = "Saturation",
                                    icon = Icons.Default.Palette,
                                    value = saturation,
                                    valueRange = 0f..2f,
                                    displayValue = "%.1fx".format(saturation),
                                    onValueChange = { saturation = it },
                                    onReset = { saturation = 1f }
                                )

                                // Warmth
                                SliderControlRow(
                                    label = "Warmth",
                                    icon = Icons.Default.WbSunny,
                                    value = warmth,
                                    valueRange = -50f..50f,
                                    displayValue = "${warmth.toInt()}",
                                    onValueChange = { warmth = it },
                                    onReset = { warmth = 0f }
                                )

                                // Vignette
                                SliderControlRow(
                                    label = "Vignette Glow",
                                    icon = Icons.Default.AutoAwesome,
                                    value = vignetteAlpha,
                                    valueRange = 0f..1f,
                                    displayValue = "${(vignetteAlpha * 100).toInt()}%",
                                    onValueChange = { vignetteAlpha = it },
                                    onReset = { vignetteAlpha = 0f }
                                )
                            }
                        }

                        2 -> {
                            // Aspect Ratio & Rotation / Flip
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                // Aspect ratios
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    val ratios = listOf(
                                        Triple("1:1 Square", 1f, "1:1"),
                                        Triple("4:3 Standard", 4f / 3f, "4:3"),
                                        Triple("4:5 Feed", 4f / 5f, "4:5"),
                                        Triple("9:16 Reel", 9f / 16f, "9:16")
                                    )
                                    ratios.forEach { (name, ratio, label) ->
                                        val isSelected = selectedAspectRatio == ratio
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(if (isSelected) FrostedLavender else Color(0x26FFFFFF))
                                                .border(1.dp, if (isSelected) FrostedLavender else Color(0x26FFFFFF), RoundedCornerShape(12.dp))
                                                .clickable { selectedAspectRatio = ratio }
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = label,
                                                color = if (isSelected) Color.Black else TextPrimary,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                // Rotate and Flip Actions
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { rotationAngle = (rotationAngle + 90f) % 360f },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(38.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x26FFFFFF)),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(imageVector = Icons.AutoMirrored.Filled.RotateRight, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Rotate 90°", color = TextPrimary, fontSize = 11.sp)
                                    }

                                    Button(
                                        onClick = { isFlippedHorizontally = !isFlippedHorizontally },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(38.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x26FFFFFF)),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Flip, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Flip Horizontal", color = TextPrimary, fontSize = 11.sp)
                                    }
                                }
                            }
                        }

                        3 -> {
                            // Stickers and Vibe Badges
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "Tap a stamp to attach to your photo:",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )

                                LazyRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(availableStickers) { sticker ->
                                        val isSelected = activeSticker?.id == sticker.id
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(if (isSelected) sticker.bgColor else Color(0x26FFFFFF))
                                                .border(
                                                    1.5.dp,
                                                    if (isSelected) sticker.textColor else Color(0x33FFFFFF),
                                                    RoundedCornerShape(14.dp)
                                                )
                                                .clickable {
                                                    activeSticker = if (isSelected) null else sticker
                                                }
                                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                        ) {
                                            Text(
                                                text = sticker.text,
                                                color = sticker.textColor,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SliderControlRow(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    displayValue: String,
    onValueChange: (Float) -> Unit,
    onReset: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = FrostedLavender,
            modifier = Modifier.size(16.dp)
        )

        Text(
            text = label,
            color = TextPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(75.dp)
        )

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = FrostedLavender,
                activeTrackColor = FrostedLavender,
                inactiveTrackColor = Color(0x33FFFFFF)
            )
        )

        Text(
            text = displayValue,
            color = TextSecondary,
            fontSize = 10.sp,
            modifier = Modifier.width(36.dp)
        )

        IconButton(
            onClick = onReset,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Reset",
                tint = TextMuted,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
