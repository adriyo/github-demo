package com.adriyo.github.feature.user

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

enum class ScreenType {
    LIST_WITH_DETAIL, LIST, DETAIL
}

@Composable
fun UserRoute(isExpandedScreen: Boolean, viewModel: UserViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val screenType: ScreenType = getScreenType(uiState, isExpandedScreen)
    when (screenType) {
        ScreenType.LIST_WITH_DETAIL -> {
            UserListWithDetailScreen(
                uiState = uiState,
                onRefreshUsers = viewModel::refresh,
                onUserClick = viewModel::onSelectUser,
                onSearchInputChange = viewModel::onSearchInputChange,
                searchInput = uiState.searchInput,
                onOpenWebView = viewModel::onOpenWebView,
            )
            BackHandler {
                viewModel.onNavigateBack()
            }
        }

        ScreenType.LIST -> {
            UserList(
                uiState = uiState,
                onRefreshUsers = viewModel::refresh,
                modifier = Modifier
                    .fillMaxSize(),
                onUserClick = viewModel::onSelectUser,
                onSearchInputChange = viewModel::onSearchInputChange,
                searchInput = uiState.searchInput,
            )
        }

        ScreenType.DETAIL -> {
            DetailScreen(
                showTopAppBar = true,
                user = uiState.selectedUser,
                onBack = viewModel::clearSelectedUser,
                onOpenWebView = viewModel::onOpenWebView,
                showWebView = uiState.openWebView,
            )
            BackHandler {
                viewModel.onNavigateBack()
            }
        }
    }

    LaunchedEffect(Unit) {
        if (screenType != ScreenType.DETAIL) {
            viewModel.refresh()
        }
    }
}

fun getScreenType(uiState: UiState, isExpandedScreen: Boolean): ScreenType {
    return when (isExpandedScreen) {
        false -> if (uiState.selectedUser != null) {
            ScreenType.DETAIL
        } else {
            ScreenType.LIST
        }

        true -> ScreenType.LIST_WITH_DETAIL
    }
}
