package com.adriyo.github.feature

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.adriyo.github.feature.user.UserRoute

@Composable
fun GithubNavGraph(navController: NavHostController, isExpandedScreen: Boolean) {
    NavHost(navController = navController, startDestination = "users") {
        composable("users") { UserRoute(isExpandedScreen = isExpandedScreen) }
    }
}