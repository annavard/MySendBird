package com.example.anna.mysendbird.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query


@Dao
interface ChannelDao {

    @Insert
    fun insert(channel: Channel)


    @Query("SELECT * FROM channel_table")
    fun getAllChannels(): List<Channel>

    @Delete
    fun delete(channel: Channel)


}