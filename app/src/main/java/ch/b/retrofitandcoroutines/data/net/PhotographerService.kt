package ch.b.retrofitandcoroutines.data.net


import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface PhotographerService {
    @GET("posts")
    suspend fun getPhotographers() : ResponseBody

    @FormUrlEncoded
    @POST("posts")
    suspend fun makePost(
        @Field("author") author: String,
        @Field("idPhotographer") idPhotographer: Int,
        @Field("like")like: Int,
        @Field("theme") theme: String,
        @Field("url") url: String,

    ) : Response<PhotographerCloud>

}