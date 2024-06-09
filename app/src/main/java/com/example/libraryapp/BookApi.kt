package com.example.libraryapp

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BookApi {
    @GET("books")
    fun getBooks(): Response<Books>

    @GET("books/{isbnNo}")
    fun getBook(@Path(value = "isbnNo") isbnNo: String): Response<Books>

    @GET("search/{key_word}")
    fun searchBook(@Path(value = "key_word") key_word: String): Response<Books>

    @GET("popularbooks")
    fun getPopularBooks(): Response<Books>

    @GET("recommendbook")
    fun getRecommendBook(): Response<Books>

    @POST("books")
    suspend fun postBooks(@Body book: Book): Response<PostBooks>

    @POST("keywords")
    suspend fun postKeywords(@Body keywords: List<String>): Response<Books>

    @DELETE("books/{isbnNo}")
    suspend fun deleteBooks(@Path(value = "isbnNo") isbnNo: String): Response<Void>

    @PUT("books/{isbnNo}")
    suspend fun modifyBooks(@Path(value = "isbnNo") isbnNo: String, @Body book: Book): Response<Books>
}


// Data classes for request bodies
data class Book(
    val isbnNo: String,
    val title: String,
    val author: String,
    val publish: String,
    val pubyear: String,
    val num: String,
    val location: String,
    val imageSrc: String,
    val detailSrc: String,
    val tags: String
)

data class Keywords(
    val tag1: String,
    val tag2: String
)