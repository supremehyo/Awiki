package com.supremehyo.awiki.repository.wiki

import android.content.Context
import androidx.room.*


@Database(entities = [WikiContract::class], version = 1, exportSchema = false)

    abstract class WikiDatabase : RoomDatabase(){
    abstract fun contactsDao(): WikiDAO// DAO로 접근 할수 있도록
    //추상 함수로 선언해둠.
    //싱글톤으로 만들어서 메모리 절약
    companion object {
        private var instance: WikiDatabase? = null
        @Synchronized
        fun getInstance(context: Context): WikiDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    WikiDatabase::class.java,
                    "database-contacts"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }
    }
}