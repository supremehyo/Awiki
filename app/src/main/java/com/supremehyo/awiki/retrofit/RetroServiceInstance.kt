package com.supremehyo.awiki.retrofit

import com.supremehyo.awiki.repository.wiki.WikiContract
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RetroServiceInstance {
    @Headers("Content-Type: application/json")

    @GET("getget")
    fun getData() : Call<WikiContract>

    @POST("test2")
    fun postData(@Body wikiContract : WikiContract) : Call<WikiContract>

}