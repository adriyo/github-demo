package com.adriyo.github.data

import com.adriyo.github.data.model.User
import com.adriyo.github.data.remote.ApiService
import javax.inject.Inject

/**
 * Created by adriyo on 07/01/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getUsers(): List<User> {
        return apiService.getUsers()
    }
}