package com.adriyo.github.feature.user

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.adriyo.github.data.model.User
import com.adriyo.github.ui.theme.GithubTheme

/**
 * Created by adriyo on 07/01/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

enum class UserListType {
    GRID, LIST
}

@Composable
fun UserListWithDetailScreen(
    uiState: UiState,
    onRefreshUsers: () -> Unit,
    onUserClick: (User) -> Unit,
    onSearchInputChange: (String) -> Unit,
    searchInput: String = "",
    onOpenWebView: (Boolean) -> Unit,
) {
    Row {
        UserList(
            uiState = uiState,
            onRefreshUsers = onRefreshUsers,
            modifier = Modifier
                .width(330.dp),
            listType = UserListType.LIST,
            onUserClick = onUserClick,
            onSearchInputChange = onSearchInputChange,
            searchInput = searchInput,
        )
        VerticalDivider()
        Crossfade(uiState.selectedUser, label = "detail crossfade") { selectedUser ->
            DetailScreen(
                showTopAppBar = false,
                user = selectedUser,
                onOpenWebView = onOpenWebView,
                showWebView = uiState.openWebView,
            )
        }
    }
}

@Composable
fun UserList(
    uiState: UiState,
    onRefreshUsers: () -> Unit,
    onUserClick: (User) -> Unit,
    modifier: Modifier,
    searchInput: String = "",
    onSearchInputChange: (String) -> Unit,
    listType: UserListType = UserListType.GRID,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = onRefreshUsers,
    )
    Column(
        modifier = modifier
            .padding(top = 16.dp)
            .pullRefresh(pullRefreshState)
    ) {
        OutlinedTextField(
            value = searchInput,
            onValueChange = onSearchInputChange,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = ""
                )
            },
            trailingIcon = {
                if (searchInput.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onSearchInputChange("")
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "clear search input"
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
        if (!uiState.message.isNullOrEmpty()) {
            ErrorScreen(
                error = uiState.message,
                onRefreshClick = onRefreshUsers
            )
        } else {
            val users = if (searchInput.isEmpty()) {
                uiState.users
            } else {
                uiState.users.filter { "${it.login}".contains(searchInput, ignoreCase = true) }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                when (listType) {
                    UserListType.GRID -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(
                                start = 12.dp,
                                top = 16.dp,
                                end = 12.dp,
                                bottom = 16.dp
                            ),
                        ) {
                            items(users) {
                                RowUser(
                                    user = it,
                                    selectedUser = uiState.selectedUser,
                                    onUserClick = onUserClick
                                )
                            }
                        }
                    }

                    UserListType.LIST -> {
                        LazyColumn(
                            contentPadding = PaddingValues(
                                start = 12.dp,
                                top = 16.dp,
                                end = 12.dp,
                                bottom = 16.dp
                            ),
                        ) {
                            items(users) {
                                RowUser(
                                    user = it,
                                    selectedUser = uiState.selectedUser,
                                    onUserClick = onUserClick
                                )
                            }
                        }
                    }
                }

                PullRefreshIndicator(
                    refreshing = uiState.isLoading,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            }
        }
    }
}

@Composable
fun ErrorScreen(error: String, onRefreshClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Text(text = error)
        Button(onClick = onRefreshClick) {
            Text("Refresh")
        }
    }
}

@Composable
internal fun RowUser(user: User, selectedUser: User? = null, onUserClick: (User) -> Unit) {
    val borderColor = if (user.login?.equals(selectedUser?.login) == true) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.primary
    }
    val backgroundColor = if (user.login?.equals(selectedUser?.login) == true) {
        MaterialTheme.colorScheme.onBackground
    } else {
        MaterialTheme.colorScheme.background
    }
    val avatarBorderColor = if (user.login?.equals(selectedUser?.login) == true) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    Row(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onUserClick(user) }
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = avatarBorderColor,
                        shape = CircleShape
                    )
                    .height(40.dp)
                    .width(40.dp)
                    .padding(1.dp),
                model = user.avatarUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Text(
                text = user.login ?: " - ",
                modifier = Modifier.padding(horizontal = 10.dp),
                style = MaterialTheme.typography.labelSmall,
                color = avatarBorderColor
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ListUserScreenPreview() {
    GithubTheme(darkTheme = true) {
        repeat(10) {
            RowUser(
                user = User(),
                onUserClick = {

                },
            )
        }
    }
}