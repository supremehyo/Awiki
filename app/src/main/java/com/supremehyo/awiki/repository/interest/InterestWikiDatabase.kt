package com.supremehyo.awiki.repository.interest

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [InterestWikiContract::class], version = 1, exportSchema = false)
abstract class InterestWikiDatabase : RoomDatabase(){
    abstract fun contactsDao(): InterestWikiDAO// DAO로 접근 할수 있도록
    //추상 함수로 선언해둠.
    //싱글톤으로 만들어서 메모리 절약
    companion object {
        private var instance: InterestWikiDatabase? = null
        @Synchronized
        fun getInstance(context: Context): InterestWikiDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    InterestWikiDatabase::class.java,
                    "database-contacts"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }
    }
}