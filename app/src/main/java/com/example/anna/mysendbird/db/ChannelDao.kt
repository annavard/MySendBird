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
    fun getAllChannels(): MutableList<Channel>

    @Delete
    fun delete(channel: Channel)

    @Query("UPDATE channel_table SET last_message = :lastMessage, created_at = :messageCreatedAt WHERE url = :url" )
    fun updateChannel(url : String, lastMessage : String, messageCreatedAt : String) {

    }


}