package com.supremehyo.awiki.model

import android.content.Context
import com.supremehyo.awiki.repository.WikiContract
import com.supremehyo.awiki.repository.WikiDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // 싱글톤으로 어디서 여러번 호출해도 같은 객체로 인식하도록 집어넣어진다.
class WikiModelImple @Inject constructor ( //Inject는 힐트가 생성자를 알아볼 수 있도록 명시
    @ApplicationContext private val context : Context) : WikiModel{ // context를 hilt 인자로 넘길때 ApplicationContext 가 필요
    val wikidao = WikiDatabase.getInstance(context)!!.contactsDao()

    override fun insertWiki(dto: WikiContract) {
        wikidao.insertWiki(dto)
    }

    override fun getOneWiki(title: String): WikiContract {
       return wikidao.getOneWiki(title)
    }

    override fun editWiki(dto: WikiContract) {
        wikidao.editWiki(dto)
    }

    override fun deleteWiki(long: Long) {
        wikidao.deleteWiki(long)
    }
}