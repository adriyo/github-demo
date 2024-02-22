@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalTestApi::class)

package com.adriyo.github.user

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adriyo.github.TestDispatcherProvider
import com.adriyo.github.data.model.User
import com.adriyo.github.data.repo.IUserRepository
import com.adriyo.github.feature.user.UserRoute
import com.adriyo.github.feature.user.UserViewModel
import com.adriyo.github.ui.theme.GithubTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by adriyo on 22/02/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

@RunWith(AndroidJUnit4::class)
class UserScreenTest {

    private val testDispatcher = TestDispatcherProvider()

    @get:Rule
    val screenTestRule = createComposeRule()

    @Test
    fun errorScreen() {
        val viewModel = UserViewModel(
            repository = FailedUserRepository(),
            dispatcher = testDispatcher
        )
        screenTestRule.setContent {
            UserRoute(isExpandedScreen = false, viewModel = viewModel)
        }
        screenTestRule.onNodeWithText("Error").assertIsDisplayed()
    }

    @Test
    fun displayUserList() {
        val viewModel = UserViewModel(
            repository = SuccessUserRepository(),
            dispatcher = testDispatcher
        )
        screenTestRule.setContent {
            GithubTheme {
                UserRoute(isExpandedScreen = false, viewModel = viewModel)
            }
        }
        screenTestRule.onNodeWithText("User 1").assertIsDisplayed()
        screenTestRule.onNodeWithText("User 2").assertIsDisplayed()
    }

    @Test
    fun displayUserList_smallScreen_performClickDetail() {
        val viewModel = UserViewModel(
            repository = SuccessUserRepository(),
            dispatcher = testDispatcher
        )
        screenTestRule.setContent {
            GithubTheme {
                UserRoute(isExpandedScreen = false, viewModel = viewModel)
            }
        }
        screenTestRule.onNodeWithText("User 1").assertIsDisplayed().performClick()
        screenTestRule.onNodeWithText("Detail").assertIsDisplayed()
    }

}

class FailedUserRepository : IUserRepository {
    override fun getUsers(): Flow<List<User>> {
        return flow {
            throw Exception("Error")
        }
    }
}

class SuccessUserRepository : IUserRepository {
    override fun getUsers(): Flow<List<User>> {
        return flow {
            emit(
                listOf(
                    User(id = 1, login = "User 1"),
                    User(id = 2, login = "User 2"),
                )
            )
        }
    }
}
