@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adriyo.github.feature.user

import app.cash.turbine.test
import com.adriyo.github.MainDispatcherRule
import com.adriyo.github.TestDispatcherProvider
import com.adriyo.github.data.model.User
import com.adriyo.github.data.repo.IUserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by adriyo on 12/01/2024.
 * [Github](https://github.com/adriyo)
 */
class UserViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: UserViewModel
    private val dispatcherProvider = TestDispatcherProvider()

    @Before
    fun setup() {
    }

    @Test
    fun `loading init state`() = runTest {
        val userRepository = FakeUserRepository()
        viewModel = UserViewModel(repository = userRepository, dispatcher = dispatcherProvider)
        val state = viewModel.uiState.value
        assertThat(state.isLoading).isTrue()
    }

    @Test
    fun `should get success response`() = runTest {
        val userRepository = FakeUserRepository()
        viewModel = UserViewModel(repository = userRepository, dispatcher = dispatcherProvider)
        val expected = UiState(
            isLoading = false,
            users = listOf(User(id = 1, login = "Name 1"), User(id = 2, login = "Name 2"))
        )
        viewModel.refresh()
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(expected)
            cancelAndConsumeRemainingEvents()
        }
    }


    @Test
    fun `should get failed response`() = runTest {
        val userRepository = FailedUserRepository()
        viewModel = UserViewModel(repository = userRepository, dispatcher = dispatcherProvider)
        val expected = UiState(isLoading = false, message = "error")
        viewModel.refresh()
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(expected)
            cancelAndConsumeRemainingEvents()
        }
    }

}

class FailedUserRepository: IUserRepository {
    override fun getUsers(): Flow<List<User>> {
        return flow {
            throw Exception("error")
        }
    }
}

class FakeUserRepository : IUserRepository {
    override fun getUsers(): Flow<List<User>> {
        return flow {
            emit(
                listOf(
                    User(
                        id = 1,
                        login = "Name 1"
                    ),
                    User(
                        id = 2,
                        login = "Name 2"
                    )
                )
            )
        }
    }
}


