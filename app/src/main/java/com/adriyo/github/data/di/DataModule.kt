package com.adriyo.github.data.di

import com.adriyo.github.data.DefaultDispatcherProvider
import com.adriyo.github.data.DispatcherProvider
import com.adriyo.github.data.remote.ApiService
import com.adriyo.github.data.RemoteDataSource
import com.adriyo.github.data.repo.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by adriyo on 07/01/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        apiService: ApiService,
    ): RemoteDataSource {
        return RemoteDataSource(apiService = apiService)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        remoteDataSource: RemoteDataSource,
    ): UserRepository {
        return UserRepository(remoteDataSource = remoteDataSource)
    }

}