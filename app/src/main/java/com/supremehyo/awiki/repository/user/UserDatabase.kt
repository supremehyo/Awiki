package com.supremehyo.awiki.repository.user

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.supremehyo.awiki.repository.wiki.WikiContract
import com.supremehyo.awiki.repository.wiki.WikiDAO


@Database(entities = [WikiContract::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase(){

    abstract fun contactsDao(): WikiDAO// DAO로 접근 할수 있도록
    //추상 함수로 선언해둠.

    //싱글톤으로 만들어서 메모리 절약
    companion object {
        private var instance: UserDatabase? = null

        @Synchronized
        fun getInstance(context: Context): UserDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "database-contacts-user"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }
    }

}
