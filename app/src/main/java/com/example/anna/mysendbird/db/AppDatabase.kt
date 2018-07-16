package com.example.anna.mysendbird.db

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.util.Log

@Database(entities = [Channel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(application: Application): AppDatabase? {
            if (INSTANCE == null) {

                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(application,
                            AppDatabase::class.java,
                            "channels")
                            .build()
                }
            }

            return INSTANCE
        }
    }


    abstract fun channelDao(): ChannelDao

}