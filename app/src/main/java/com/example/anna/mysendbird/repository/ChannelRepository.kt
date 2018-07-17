package com.example.anna.mysendbird.repository

import android.app.Application

import android.util.Log
import com.example.anna.mysendbird.db.AppDatabase
import com.example.anna.mysendbird.db.Channel
import com.example.anna.mysendbird.db.ChannelDao
import com.example.anna.mysendbird.view.MainActivity

class ChannelRepository(application: Application) {

    private var mChannelDao: ChannelDao? = null

    var channels: List<Channel>? = null

    init {
        val db = AppDatabase.getInstance(application)
        mChannelDao = db?.channelDao()
//        channels = loadChannelsFromDb()
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


    fun loadChannelsFromDb(listener: DbListener, isInit: Boolean): List<Channel>? {
        Log.d(MainActivity.TAG, "loadChannelsFromDb")
        var list: List<Channel>? = null
        Thread(object : Runnable {
            override fun run() {
                list = mChannelDao?.getAllChannels()
                listener.onLoaded(list, isInit)
            }
        }).start()

        return list
    }


    fun insertChannel(channel: Channel, listener: DbListener) {

        Thread(object : Runnable {
            override fun run() {
                mChannelDao?.insert(channel)
                listener.onInserted()
            }
        }).start()
    }


    public interface DbListener {
        fun onInserted()

        fun onLoaded(list: List<Channel>?, isInit : Boolean)

        fun onDeleted()
    }
}