package com.example.anna.mysendbird.repository

import android.app.Application

import android.util.Log
import com.example.anna.mysendbird.db.AppDatabase
import com.example.anna.mysendbird.db.Channel
import com.example.anna.mysendbird.db.ChannelDao
import com.example.anna.mysendbird.view.activity.MainActivity
import com.sendbird.android.GroupChannel
import com.sendbird.android.UserMessage

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

    fun updateChannel(channel: GroupChannel) {
        Thread(Runnable {
            mChannelDao?.updateChannel(channel.url!!, (channel.lastMessage as UserMessage).message, channel.lastMessage.createdAt.toString())
        }).start()

    }


    public interface DbListener {
        fun onInserted(isInit: Boolean, runnable: Runnable)

        fun onLoaded(list: MutableList<Channel>, isInit: Boolean, runnable: Runnable)

        fun onDeleted()
    }
}