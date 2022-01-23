package com.supremehyo.awiki.model

import android.content.Context
import com.supremehyo.awiki.repository.wiki.WikiContract
import okhttp3.ResponseBody
import retrofit2.Call


interface WikiModel {
     suspend fun insertWiki(dto : WikiContract , localOrApi : String) : Long
     suspend fun getOneWiki (title : String , context: Context , localOrApi : String) : WikiContract// 날짜로 데이터 가져오는 작업
     suspend fun editWiki(dto : WikiContract , localOrApi : String)
     suspend fun deleteWiki (long: Long , localOrApi : String)
     suspend fun randomGetOneWiki(long: Long) :WikiContract //홈에서 랜덤 위키를 id를 기준으로 가져오는 메소드
     suspend fun getWikiListBySearch(title : String) : List<WikiContract>//제목을 기준으로 like 쿼리로 비슷한거 리스트 가져오는 함수
     suspend fun gettest() : String
     suspend fun posttest(wikiContract : WikiContract)

}