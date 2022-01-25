package com.supremehyo.awiki.repository.interest

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface InterestWikiDAO {

    @Query("SELECT * FROM tb_intersetWiki where title =:title")
    fun getOneWiki(title: String): InterestWikiContract

    @Query("SELECT * FROM tb_intersetWiki where title LIKE '%' || :title ||'%'")
    fun getListWikiBySearch(title: String): List<InterestWikiContract>

    @Query("SELECT * FROM tb_intersetWiki")
    fun getListWikiDefault(): List<InterestWikiContract>

    @Insert
    fun insertWiki(contacts: InterestWikiContract) : Long

    @Query("DELETE FROM tb_intersetWiki where id =:long ")
    fun deleteWiki(long: Long)



}