package com.example.ui.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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

@Composable
fun AuthScreen(
    onLogin: (emailOrHandle: String, pass: String, (Boolean, String) -> Unit) -> Unit,
    onSignUp: (
        name: String,
        handle: String,
        email: String,
        pass: String,
        bio: String,
        avatarDrawable: String,
        (Boolean, String) -> Unit
    ) -> Unit,
    onGuestLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 0 = Sign In, 1 = Create Account / Sign Up
    var authMode by remember { mutableIntStateOf(0) }

    // Sign In Fields
    var signInIdentifier by remember { mutableStateOf("arianova.vibe") }
    var signInPassword by remember { mutableStateOf("vibepass123") }
    var showSignInPassword by remember { mutableStateOf(false) }

    // Sign Up Fields
    var signUpName by remember { mutableStateOf("") }
    var signUpHandle by remember { mutableStateOf("") }
    var signUpEmail by remember { mutableStateOf("") }
    var signUpPassword by remember { mutableStateOf("") }
    var signUpBio by remember { mutableStateOf("") }
    var showSignUpPassword by remember { mutableStateOf(false) }
    var selectedAvatar by remember { mutableStateOf("img_reel_cyber_dance") }
    var selectedAesthetic by remember { mutableStateOf("🔮 Cyber Synthwave") }

    val avatarChoices = listOf(
        "img_reel_cyber_dance",
        "img_vibe_sunset",
        "img_vibe_concert",
        "img_reel_cafe_aesthetic",
        "img_app_icon"
    )

    val aestheticChoices = listOf(
        "🔮 Cyber Synthwave",
        "🌸 Pastel Dream",
        "⚡ Electric Neon",
        "🌙 Midnight Obsidian",
        "☀️ Golden Glow"
    )

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VibeBackground)
            .testTag("auth_screen")
    ) {
        // Ambient background glow
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
                .imePadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // App Brand Logo & Pulse
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.sweepGradient(
                                listOf(FrostedLavender, NeonMagenta, NeonCyan, FrostedLavender)
                            )
                        )
                        .padding(2.5.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E1B4B)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon),
                        contentDescription = "Vibesphere Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape)
                    )
                }

                Text(
                    text = "VIBESPHERE",
                    color = TextPrimary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 3.sp
                )

                Text(
                    text = if (authMode == 0) "Welcome back to your vibe frequency ✨" else "Join the next-gen social frequency 🚀",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }

            // Auth Mode Tab Switcher (Frosted Pill)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x26FFFFFF))
                    .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(20.dp))
                    .padding(4.dp)
            ) {
                TabRow(
                    selectedTabIndex = authMode,
                    containerColor = Color.Transparent,
                    contentColor = TextPrimary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[authMode]),
                            color = FrostedLavender,
                            height = 3.dp
                        )
                    },
                    divider = {}
                ) {
                    Tab(
                        selected = authMode == 0,
                        onClick = {
                            authMode = 0
                            errorMessage = null
                            successMessage = null
                        },
                        text = {
                            Text(
                                "Sign In",
                                fontWeight = if (authMode == 0) FontWeight.Bold else FontWeight.Medium,
                                color = if (authMode == 0) FrostedLavender else TextSecondary,
                                fontSize = 14.sp
                            )
                        }
                    )
                    Tab(
                        selected = authMode == 1,
                        onClick = {
                            authMode = 1
                            errorMessage = null
                            successMessage = null
                        },
                        text = {
                            Text(
                                "Create Account",
                                fontWeight = if (authMode == 1) FontWeight.Bold else FontWeight.Medium,
                                color = if (authMode == 1) FrostedLavender else TextSecondary,
                                fontSize = 14.sp
                            )
                        }
                    )
                }
            }

            // Status message (Error or Success)
            errorMessage?.let { err ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x33FF0055))
                        .border(1.dp, Color(0x66FF0055), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "⚠️ $err",
                        color = Color(0xFFFFB4C8),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            successMessage?.let { msg ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x3300E5FF))
                        .border(1.dp, Color(0x6600E5FF), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "✨ $msg",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Main Form Container (Frosted Card)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0x331E1B4B))
                    .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(24.dp))
                    .padding(18.dp)
            ) {
                AnimatedContent(
                    targetState = authMode,
                    transitionSpec = {
                        if (targetState > initialState) {
                            slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                        } else {
                            slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
                        }
                    },
                    label = "AuthFormAnimation"
                ) { mode ->
                    if (mode == 0) {
                        // SIGN IN FORM
                        Column(
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "Sign In to Your Account",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            // Username / Email
                            OutlinedTextField(
                                value = signInIdentifier,
                                onValueChange = { signInIdentifier = it },
                                label = { Text("Email or Vibe Handle (@handle)") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.AlternateEmail,
                                        contentDescription = null,
                                        tint = FrostedLavender,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signin_identifier_input"),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0x26FFFFFF),
                                    unfocusedContainerColor = Color(0x1AFFFFFF),
                                    focusedBorderColor = FrostedLavender,
                                    unfocusedBorderColor = Color(0x26FFFFFF),
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                )
                            )

                            // Password
                            OutlinedTextField(
                                value = signInPassword,
                                onValueChange = { signInPassword = it },
                                label = { Text("Password") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = FrostedLavender,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { showSignInPassword = !showSignInPassword }) {
                                        Icon(
                                            imageVector = if (showSignInPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = "Toggle password",
                                            tint = TextMuted,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                },
                                visualTransformation = if (showSignInPassword) VisualTransformation.None else PasswordVisualTransformation(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signin_password_input"),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0x26FFFFFF),
                                    unfocusedContainerColor = Color(0x1AFFFFFF),
                                    focusedBorderColor = FrostedLavender,
                                    unfocusedBorderColor = Color(0x26FFFFFF),
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Submit Sign In Button
                            Button(
                                onClick = {
                                    if (signInIdentifier.isBlank() || signInPassword.isBlank()) {
                                        errorMessage = "Please provide your email/handle and password."
                                    } else {
                                        errorMessage = null
                                        onLogin(signInIdentifier, signInPassword) { success, msg ->
                                            if (!success) {
                                                errorMessage = msg
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("signin_submit_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = FrostedLavender),
                                shape = RoundedCornerShape(24.dp)
                            ) {
                                Text(
                                    text = "Sign In",
                                    color = Color.Black,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Quick Demo Switcher
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0x1AFFFFFF))
                                    .clickable {
                                        signInIdentifier = "arianova.vibe"
                                        signInPassword = "vibepass123"
                                        onLogin("arianova.vibe", "vibepass123") { _, _ -> }
                                    }
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = FrostedLavender,
                                    modifier = Modifier.size(16.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "One-Tap Demo: Sign in as @arianova.vibe",
                                        color = TextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Verified Creator profile pre-loaded",
                                        color = TextSecondary,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    } else {
                        // SIGN UP / CREATE ACCOUNT FORM
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Create Your Creator Profile",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            // Avatar Picker Strip
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "Select Vibe Avatar",
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    items(avatarChoices) { avatar ->
                                        val isSelected = avatar == selectedAvatar
                                        Box(
                                            modifier = Modifier
                                                .size(52.dp)
                                                .clip(CircleShape)
                                                .border(
                                                    2.dp,
                                                    if (isSelected) FrostedLavender else Color(0x33FFFFFF),
                                                    CircleShape
                                                )
                                                .clickable { selectedAvatar = avatar }
                                        ) {
                                            Image(
                                                painter = painterResource(id = getDrawableResByName(avatar)),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                            if (isSelected) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .background(Color(0x44D0BCFF)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // Full Name
                            OutlinedTextField(
                                value = signUpName,
                                onValueChange = { signUpName = it },
                                label = { Text("Display Name (e.g. Kai Sterling)") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = FrostedLavender,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_name_input"),
                                shape = RoundedCornerShape(16.dp),
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

                            // Handle
                            OutlinedTextField(
                                value = signUpHandle,
                                onValueChange = {
                                    signUpHandle = it.lowercase().replace(" ", "").replace("@", "")
                                },
                                label = { Text("Username Handle (@handle)") },
                                prefix = { Text("@", color = FrostedLavender) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.AlternateEmail,
                                        contentDescription = null,
                                        tint = FrostedLavender,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_handle_input"),
                                shape = RoundedCornerShape(16.dp),
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

                            // Email
                            OutlinedTextField(
                                value = signUpEmail,
                                onValueChange = { signUpEmail = it },
                                label = { Text("Email Address") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = null,
                                        tint = FrostedLavender,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_email_input"),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0x26FFFFFF),
                                    unfocusedContainerColor = Color(0x1AFFFFFF),
                                    focusedBorderColor = FrostedLavender,
                                    unfocusedBorderColor = Color(0x26FFFFFF),
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                            )

                            // Password
                            OutlinedTextField(
                                value = signUpPassword,
                                onValueChange = { signUpPassword = it },
                                label = { Text("Create Password") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = FrostedLavender,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { showSignUpPassword = !showSignUpPassword }) {
                                        Icon(
                                            imageVector = if (showSignUpPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = "Toggle password",
                                            tint = TextMuted,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                },
                                visualTransformation = if (showSignUpPassword) VisualTransformation.None else PasswordVisualTransformation(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_password_input"),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0x26FFFFFF),
                                    unfocusedContainerColor = Color(0x1AFFFFFF),
                                    focusedBorderColor = FrostedLavender,
                                    unfocusedBorderColor = Color(0x26FFFFFF),
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                            )

                            // Bio
                            OutlinedTextField(
                                value = signUpBio,
                                onValueChange = { signUpBio = it },
                                label = { Text("Creator Bio & Vibes") },
                                placeholder = { Text("e.g. Visual art, cyber aesthetics & lo-fi beats ✨", color = TextMuted) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_bio_input"),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0x26FFFFFF),
                                    unfocusedContainerColor = Color(0x1AFFFFFF),
                                    focusedBorderColor = FrostedLavender,
                                    unfocusedBorderColor = Color(0x26FFFFFF),
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                maxLines = 2
                            )

                            // Aesthetic Choice
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "Primary Vibe Aesthetic",
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(aestheticChoices) { aesthetic ->
                                        val isSelected = aesthetic == selectedAesthetic
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(if (isSelected) FrostedLavender else Color(0x26FFFFFF))
                                                .border(1.dp, if (isSelected) FrostedLavender else Color(0x33FFFFFF), RoundedCornerShape(12.dp))
                                                .clickable { selectedAesthetic = aesthetic }
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = aesthetic,
                                                color = if (isSelected) Color.Black else TextPrimary,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Submit Sign Up Button
                            Button(
                                onClick = {
                                    if (signUpName.isBlank() || signUpHandle.isBlank() || signUpEmail.isBlank() || signUpPassword.isBlank()) {
                                        errorMessage = "Please fill in all required fields (Name, Handle, Email, Password)."
                                    } else {
                                        errorMessage = null
                                        val finalBio = if (signUpBio.isNotBlank()) "$signUpBio | $selectedAesthetic" else selectedAesthetic
                                        onSignUp(
                                            signUpName,
                                            signUpHandle,
                                            signUpEmail,
                                            signUpPassword,
                                            finalBio,
                                            selectedAvatar
                                        ) { success, msg ->
                                            if (!success) {
                                                errorMessage = msg
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("signup_submit_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = FrostedLavender),
                                shape = RoundedCornerShape(24.dp)
                            ) {
                                Text(
                                    text = "Create Account & Enter Vibesphere",
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Quick Guest Explore Button
            OutlinedButton(
                onClick = onGuestLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("guest_explore_button"),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(listOf(FrostedLavender, NeonCyan))
                )
            ) {
                Text(
                    text = "🚀 Explore as Guest without Account",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Footer info
            Text(
                text = "By continuing, you agree to Vibesphere's Community Guidelines & Frequency Terms.",
                color = TextMuted,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
