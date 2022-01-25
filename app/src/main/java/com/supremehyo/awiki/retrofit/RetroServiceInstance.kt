package com.supremehyo.awiki.retrofit

import androidx.room.Delete
import com.supremehyo.awiki.DTO.DebateDTO
import com.supremehyo.awiki.DTO.DebateListDTO
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



    ///////// 토론 관련 api

    @GET("wikilist/{title}")
    fun getDebateTitleList(@Path("id") id : Long) : Call<DebateListDTO>

    @GET("wikilist/{title}")
    fun getDebateListByTitle(@Path("title") title : String) : List<DebateDTO>

    //wiki insert
    @POST("debate/{id}/{title}")
    fun postDebate(@Path("id") id : Long ,@Path("title") title : String , @Body dto : DebateDTO) : Call<ResponseBody>

    //토론 생성, 문서 id와 문서에서 생성될 토론의 제목을 전달
    @POST("debate/{id}/{title}")
    fun createDebate(@Path("id") id : Long ,@Path("title") title : String) : Call<Long>



}