package com.supremehyo.awiki.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// 테이블인 Entity 쿼리로 접근하는 인터페이스
@Dao
interface WikiDAO {
    // 파라미터를 쿼리문안에 쓰고 싶으면 : 를 적어주고 사용
  //  @Query("SELECT * FROM tb_wiki where date =:date")
  //  fun getAll(date: String): List<WikiContract>


    @Query("SELECT * FROM tb_wiki where title =:title")
     fun getOneWiki(title: String): WikiContract

    @Update
     fun editWiki(vararg contacts: WikiContract)

    @Insert
     fun insertWiki(contacts: WikiContract) : Long

    @Query("DELETE FROM tb_wiki where id =:long ")
     fun deleteWiki(long: Long)
}