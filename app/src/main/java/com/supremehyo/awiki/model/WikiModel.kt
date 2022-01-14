package com.supremehyo.awiki.model

import android.content.Context
import com.supremehyo.awiki.repository.wiki.WikiContract


interface WikiModel {
     suspend fun insertWiki(dto : WikiContract)
     suspend fun getOneWiki (title : String , context: Context) : WikiContract// 날짜로 데이터 가져오는 작업
     suspend fun editWiki(dto : WikiContract)
     suspend fun deleteWiki (long: Long)
}