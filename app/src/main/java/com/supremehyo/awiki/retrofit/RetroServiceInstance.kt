package com.supremehyo.awiki.retrofit

import androidx.room.Delete
import com.supremehyo.awiki.repository.wiki.WikiContract
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RetroServiceInstance {
    @Headers("Content-Type: application/json")

    //wiki data get
    @GET("wiki")
    fun getData() : Call<WikiContract>

    //검색 했을때 list 불러오기
    @GET("wikilist/{title}")
    fun getListData(@Path("title") title : String) : Call<List<WikiContract>>

    //wiki insert
    @POST("wiki")
    fun postData(@Body wikiContract : WikiContract) : Call<ResponseBody>

    //편집 , wiki 업데이트
    @PUT("wiki")
    fun updateData(@Body wikiContract : WikiContract) : Call<ResponseBody>

    @DELETE("wiki")
    fun deleteData(@Body wikiContract : WikiContract) : Call<ResponseBody>


}