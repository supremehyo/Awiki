package com.supremehyo.awiki.model

import android.content.Context
import android.util.Log
import com.supremehyo.awiki.repository.WikiContract
import com.supremehyo.awiki.repository.WikiDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // 싱글톤으로 어디서 여러번 호출해도 같은 객체로 인식하도록 집어넣어진다.
class WikiModelImple @Inject constructor ( //Inject는 힐트가 생성자를 알아볼 수 있도록 명시
    @ApplicationContext private val context : Context) : WikiModel{ // context를 hilt 인자로 넘길때 ApplicationContext 가 필요


    override suspend fun insertWiki(dto: WikiContract) {
        val wikidao = WikiDatabase.getInstance(context)!!.contactsDao()
      var aaaa =  wikidao.insertWiki(dto)
        Log.v("wegwadb" , aaaa.toString())
    }

    override suspend fun getOneWiki(title: String , context  : Context): WikiContract {
        val wikidao = WikiDatabase.getInstance(context)!!.contactsDao()
       return wikidao.getOneWiki(title)
    }

    override suspend fun editWiki(dto: WikiContract) {
        val wikidao = WikiDatabase.getInstance(context)!!.contactsDao()
        wikidao.editWiki(dto)
    }

    override suspend fun deleteWiki(long: Long) {
        val wikidao = WikiDatabase.getInstance(context)!!.contactsDao()
        wikidao.deleteWiki(long)
    }
}