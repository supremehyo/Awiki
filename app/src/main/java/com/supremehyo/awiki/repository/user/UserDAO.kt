package com.supremehyo.awiki.repository.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.supremehyo.awiki.repository.wiki.WikiContract

@Dao
interface UserDAO {


    @Query("SELECT * FROM tb_wiki where title =:title")
    fun getUserData(title: String): WikiContract

    @Update
    fun getUserInterest(vararg contacts: WikiContract)

    @Insert
    fun insertUserInterest(contacts: WikiContract) : Long

}