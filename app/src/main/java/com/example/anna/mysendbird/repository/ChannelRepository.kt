package com.example.anna.mysendbird.repository

import android.app.Application

import android.util.Log
import com.example.anna.mysendbird.db.AppDatabase
import com.example.anna.mysendbird.db.Channel
import com.example.anna.mysendbird.db.ChannelDao
import com.example.anna.mysendbird.view.activity.MainActivity

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


    fun loadChannelsFromDb(listener: DbListener, isInit: Boolean, runnable: Runnable): MutableList<Channel> {
        Log.d(MainActivity.TAG, "loadChannelsFromDb")
        var list: MutableList<Channel> = mutableListOf()
        Thread(object : Runnable {
            override fun run() {
                list = mChannelDao?.getAllChannels()!!
                listener.onLoaded(list, isInit, runnable)
            }
        }).start()

        return list
    }


    fun insertChannel(channel: Channel, listener: DbListener, isInit: Boolean, runnable: Runnable) {

        Thread(object : Runnable {
            override fun run() {
                mChannelDao?.insert(channel)
                listener.onInserted(isInit, runnable)
            }
        }).start()
    }

    fun updateChannel(channelUrl : String, message : String, time : String, listener: DbListener) {
        Thread(Runnable {
            mChannelDao?.updateChannel(channelUrl, message, time)
            listener.onUpdated(channelUrl, message, time, false, Runnable {  })
        }).start()

    }


    public interface DbListener {
        fun onInserted(isInit: Boolean, runnable: Runnable)

        fun onLoaded(list: MutableList<Channel>, isInit: Boolean, runnable: Runnable)

        fun onUpdated(channelUrl: String, message: String, time : String, isInit: Boolean, runnable: Runnable)

        fun onDeleted()
    }
}