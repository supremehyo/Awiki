package com.supremehyo.awiki.model

import android.content.Context
import com.supremehyo.awiki.repository.WikiContract
import com.supremehyo.awiki.repository.WikiDatabase

class WikiModelImple(private val context : Context) : WikiModel{
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