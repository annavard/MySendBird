package com.example.anna.mysendbird.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.anna.mysendbird.models.Message
import com.sendbird.android.BaseMessage
import com.sendbird.android.UserMessage

open class BasicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


    open fun bindData(userMessage: UserMessage) {}
    open fun bindData(userMessage: Message) {}
    open fun bindData(userMessage: String) {}
}