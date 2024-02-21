package com.adriyo.github.data.di

import com.adriyo.github.data.DefaultDispatcherProvider
import com.adriyo.github.data.DispatcherProvider
import com.adriyo.github.data.repo.UserRepository
import com.adriyo.github.data.repo.IUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by adriyo on 13/01/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */
@Module
@InstallIn(SingletonComponent::class)
interface BindModule {

    @Binds
    abstract fun bindsDefaultDispatcherProvider(
        defaultDispatcherProvider: DefaultDispatcherProvider,
    ): DispatcherProvider


    @Binds
    fun bindsUserRepository(userRepository: UserRepository): IUserRepository

}