package com.example.libraryapp

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {
    @GET("Users")
    fun getUsers(): Response<UserClass>

    @GET("check/id/{studentNumber}")
    suspend fun checkStudentNumber(@Path(value = "studentNumber") studentNumber: String): Boolean

    @GET("check/nickname/{nickname}")
    suspend fun checkNickname(@Path(value = "nickname") nickname: String): Boolean

    @POST("signup")
    suspend fun signup(@Body user: UserClass): User

    @POST("login")
    suspend fun login(@Body loginData: LoginData): User

    @GET("logout")
    fun logout(): Response<UserClass>
}

