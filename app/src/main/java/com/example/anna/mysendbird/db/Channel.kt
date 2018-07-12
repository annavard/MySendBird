package com.example.anna.mysendbird.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Insert
import android.arch.persistence.room.PrimaryKey


@Entity
 class Channel{

    @PrimaryKey
    lateinit var channelId : String

    lateinit var url : String
}