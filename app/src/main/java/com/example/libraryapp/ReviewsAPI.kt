import com.example.libraryapp.PostReviews
import com.example.libraryapp.Reviews
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReviewsAPI {
    @GET("reviews")
    suspend fun getMyReviews(): Response<Reviews>

    @GET("reviews/{isbnNo}")
    suspend fun getBookReviews(@Path(value = "isbnNo") isbnNo : String): Response<Reviews>

    @FormUrlEncoded
    @POST("reviews")
    suspend fun postReviews(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("isbnNo") isbnNo: String,
        @Field("score") score: String
    ): Response<PostReviews>

    @DELETE("reviews/{isbnNo}")
    suspend fun deleteReview(@Path(value = "isbnNo") isbnNo : String): Response<Unit>

    @FormUrlEncoded
    @PUT("reviews/{id}")
    suspend fun modifyReviews(
        @Path(value = "id") id : Int,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("isbnNo") isbnNo: String,
        @Field("score") score: String
    ): Response<Reviews>
}
