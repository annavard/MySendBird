package com.example.anna.mysendbird.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Insert
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull


@Entity(tableName = "channel_table")
class Channel(hostUserId: String, otherUserId: String, customType: String, url: String?) {


    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "url")
    var url = url

    @ColumnInfo(name = "host_user")
    var hostUserId = hostUserId

    @ColumnInfo(name = "other_user")
    var otherUserId = otherUserId

    @ColumnInfo(name = "custom_type")
    var customType = customType


//    override fun equals(other: Any?): Boolean {
//        if (other is Channel) {
//            if (other.hostUserId == hostUserId
//                    && other.otherUserId == other.otherUserId
//                    && other.customType == customType) {
//                return true
//            }
//        }
//        return false
//    }


}