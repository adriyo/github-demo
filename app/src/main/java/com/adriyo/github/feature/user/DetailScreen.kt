@file:OptIn(ExperimentalMaterial3Api::class)

package com.adriyo.github.feature.user

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.adriyo.github.data.model.User

/**
 * Created by adriyo on 20/02/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailScreen(
    showTopAppBar: Boolean,
    user: User?,
    onBack: () -> Unit = {},
    showWebView: Boolean,
    onOpenWebView: (Boolean) -> Unit,
) {

    Scaffold(
        topBar = {
            if (showTopAppBar) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Detail")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                if (showWebView) {
                                    onOpenWebView(false)
                                } else {
                                    onBack()
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (user == null) {
                ErrorContent()
            } else if (showWebView && !user.htmlUrl.isNullOrEmpty()) {
                WebViewScreen(
                    showTopAppBar = !showTopAppBar,
                    url = user.htmlUrl,
                    onBack = {
                        onOpenWebView(false)
                    },
                )
            } else {
                DetailPage(
                    user = user,
                    onLinkClick = {
                        onOpenWebView(true)
                    },
                )
            }
        }
    }
}

@Composable
private fun ErrorContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = "Empty Icon",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No data to display.",
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "There are currently user to show here.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun DetailPage(user: User, onLinkClick: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        UserAvatar(avatarUrl = user.avatarUrl)
        Spacer(modifier = Modifier.height(16.dp))
        UserDetailsList(user = user, onLinkClick = onLinkClick)
    }
}


@Composable
fun UserAvatar(avatarUrl: String?) {
    AsyncImage(
        modifier = Modifier
            .clip(CircleShape)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = CircleShape
            )
            .size(100.dp)
            .padding(1.dp),
        model = avatarUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun UserDetailsList(user: User, onLinkClick: () -> Unit) {
    Column {
        UserDetailItem("Username", user.login ?: "Not Available")
        UserDetailItem("Type", user.type ?: "Not Available")
        UserDetailItem("ID", user.id?.toString() ?: "Not Available")
        UserDetailClickableItem(
            "URL",
            user.htmlUrl ?: "Not Available",
            onLinkClick = onLinkClick
        )
    }
}

@Composable
fun UserDetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier
                .weight(1f),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            modifier = Modifier
                .weight(2f),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun UserDetailClickableItem(label: String, value: String, onLinkClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier
                .weight(1f),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        ClickableText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                    append(value)
                }
                addStringAnnotation(
                    tag = "",
                    annotation = value,
                    start = 0,
                    end = value.length - 1
                )
            },
            maxLines = 1,
            modifier = Modifier
                .weight(2f),
            overflow = TextOverflow.Ellipsis,
            onClick = { onLinkClick() },
        )
    }
}
