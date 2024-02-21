package com.adriyo.github.data.repo

import com.adriyo.github.data.RemoteDataSource
import com.adriyo.github.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : IUserRepository {

    override fun getUsers(): Flow<List<User>> = flow {
        emit(remoteDataSource.getUsers())
    }

}
