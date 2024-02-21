package com.adriyo.github.data.repo

import com.adriyo.github.data.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Created by adriyo on 14/01/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */
interface IUserRepository {
    fun getUsers(): Flow<List<User>>
}