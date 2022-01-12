package com.supremehyo.awiki.model

import android.content.Context
import com.supremehyo.awiki.repository.WikiContract
import dagger.Provides


interface WikiModel {
    fun insertWiki(dto : WikiContract)
    fun getOneWiki (title : String) : WikiContract// 날짜로 데이터 가져오는 작업
    fun editWiki(dto : WikiContract)
    fun deleteWiki (long: Long)
}