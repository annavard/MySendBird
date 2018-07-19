package com.example.anna.mysendbird.db

import android.arch.persistence.room.*
import android.support.annotation.NonNull
import com.sendbird.android.UserMessage


@Entity(tableName = "channel_table")
class Channel(hostUserId: String?,
              otherUserId: String?,
              customType: String,
              lastMessage: String,
              messageCreatedAt: String) {


    @Ignore
    constructor(url: String,
                hostUserId: String?,
                otherUserId: String?,
                customType: String,
                lastMessage: String,
                messageCreatedAt: String
    ) : this(hostUserId, otherUserId, customType, lastMessage, messageCreatedAt) {

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

    @ColumnInfo(name = "last_message")
    var lastMessage = lastMessage

    @ColumnInfo(name = "created_at")
    var messageCreatedAt: String = messageCreatedAt


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