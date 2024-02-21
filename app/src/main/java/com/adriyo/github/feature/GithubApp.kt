package com.adriyo.github.feature

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.adriyo.github.ui.theme.GithubTheme

/**
 * Created by adriyo on 20/02/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */


@Composable
fun GithubApp(widthSizeClass: WindowWidthSizeClass) {
    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded
    val navController = rememberNavController()
    GithubTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            GithubNavGraph(
                navController = navController,
                isExpandedScreen = isExpandedScreen
            )
        }
    }
}

