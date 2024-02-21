package com.adriyo.github.data.remote

import com.adriyo.github.data.model.User
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by adriyo on 07/01/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */
interface ApiService {

    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("users/{user}")
    fun getUser(@Path("user") user: String): List<User>

}