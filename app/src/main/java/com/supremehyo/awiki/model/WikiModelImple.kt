package com.supremehyo.awiki.model

import android.content.Context
import android.util.Log
import com.supremehyo.awiki.repository.wiki.WikiContract
import com.supremehyo.awiki.repository.wiki.WikiDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

//여기 들어올때 local에 쓰일지 api 통신을 할지 flag 를 context랑 같이 넘겨서
//거기에 맞게 분기해서 필요에 맞게 통신하면 될듯함.

@Singleton // 싱글톤으로 어디서 여러번 호출해도 같은 객체로 인식하도록 집어넣어진다.
class WikiModelImple @Inject constructor ( //Inject는 힐트가 생성자를 알아볼 수 있도록 명시
    @ApplicationContext private val context : Context) : WikiModel{ // context를 hilt 인자로 넘길때 ApplicationContext 가 필요


    override suspend fun insertWiki(dto: WikiContract , localOrApi : String) : Long {
        var aaaa = 0L
        when(localOrApi){
            "local" ->{
                val wikidao = WikiDatabase.getInstance(context)!!.contactsDao()
                aaaa =  wikidao.insertWiki(dto)
                return aaaa
            }"api" ->{
              val wikidao = WikiDatabase.getInstance(context)!!.contactsDao()
                 aaaa =  wikidao.insertWiki(dto)
                return aaaa
            }
        }
        return aaaa
    }
    override suspend fun getOneWiki(title: String , context  : Context , localOrApi : String): WikiContract {
        if(localOrApi == "local"){
            val localWikidao = WikiDatabase.getInstance(context)!!.contactsDao()
            return localWikidao.getOneWiki(title)
        }else{
            val apiWikidao = WikiDatabase.getInstance(context)!!.contactsDao()
            return apiWikidao.getOneWiki(title)
        }
    }
    override suspend fun editWiki(dto: WikiContract , localOrApi : String) {

        when(localOrApi){
                "local" ->{
                    val wikidao = WikiDatabase.getInstance(context)!!.contactsDao()
                    wikidao.editWiki(dto)
                }"api" ->{
                    val wikidao = WikiDatabase.getInstance(context)!!.contactsDao()
                    wikidao.editWiki(dto)
                }
        }
    }
    override suspend fun deleteWiki(long: Long , localOrApi : String) {
        when(localOrApi){
            "local" ->{
                val wikidao = WikiDatabase.getInstance(context)!!.contactsDao()
                wikidao.deleteWiki(long)
            }"api" ->{
                val wikidao = WikiDatabase.getInstance(context)!!.contactsDao()
                wikidao.deleteWiki(long)
        }
        }
    }

    override suspend fun randomGetOneWiki(long: Long): WikiContract {
        val wikidao = WikiDatabase.getInstance(context)!!.contactsDao()
        return wikidao.randomGetOneWiki(long)
    }

    override suspend fun getWikiListBySearch(title: String): List<WikiContract> {
        val wikidao = WikiDatabase.getInstance(context)!!.contactsDao()
        return wikidao.getListWikiBySearch(title)
    }
}