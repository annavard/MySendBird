package com.example.anna.mysendbird.db

import android.arch.persistence.room.*
import android.support.annotation.NonNull


@Entity(tableName = "channel_table")
class Channel(hostUserId: String?, otherUserId: String?, customType: String) {


    @Ignore
    constructor(hostUserId: String?,
                otherUserId: String?,
                customType: String,
                url: String) : this(hostUserId, otherUserId, customType) {
        this.url = url
    }


    @NonNull
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    var uId: Long = 0


    @ColumnInfo(name = "url")
     var url: String? = null

    @ColumnInfo(name = "host_user")
    var hostUserId = hostUserId

    @ColumnInfo(name = "other_user")
    var otherUserId = otherUserId

    @ColumnInfo(name = "custom_type")
    var customType = customType


    override fun equals(other: Any?): Boolean {
        if (other is Channel) {
            if (other.hostUserId == hostUserId
                    && other.otherUserId == other.otherUserId
                    && other.customType == customType) {
                return true
            }
        }
        return false
    }


}