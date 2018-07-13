package com.example.anna.mysendbird.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import android.content.Context
import com.example.anna.mysendbird.db.Channel
import com.example.anna.mysendbird.db.ChannelDao

class ChannelRepository(application: Application) {

    private var mChannelDao: ChannelDao? = null

    var channels: List<Channel>? = null

    init {
        val db = ChannelRoomDatabase.getInstance(application)
        mChannelDao = db?.channelDao()
        channels = loadChannelsFromDb()
    }

    companion object {
        private var INSTANCE: ChannelRepository? = null

        fun getInstance(application: Application): ChannelRepository? {

            if (INSTANCE == null) {
                synchronized(ChannelRepository::class) {
                    INSTANCE = ChannelRepository(application)
                }
            }

            return INSTANCE
        }
    }


    fun loadChannelsFromDb(): List<Channel>? {
        var list: List<Channel>? = null
        Thread(object : Runnable {
            override fun run() {
                list = mChannelDao?.getAllChannels()
            }
        }).start()

        return list
    }


    fun insertChannel(channel: Channel) {

        Thread(object : Runnable {
            override fun run() {
                mChannelDao?.insert(channel)
            }
        }).start()
    }
}