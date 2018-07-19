package com.example.anna.mysendbird.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anna.mysendbird.R
import com.example.anna.mysendbird.view.activity.MessageActivity
import com.example.anna.mysendbird.view.view_holder.BasicViewHolder
import com.example.anna.mysendbird.view.view_holder.ReceiveMessageViewHolder

class MessageListAdapter(context: Context, messages: MutableList<String>) : RecyclerView.Adapter<BasicViewHolder>() {

    companion object {
        const val VIEW_TYPE_MESSAGE_SENT = 1
        const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }

    private var mContext: Context = context
    private var mMessages: MutableList<String> = messages


//    override fun getItemViewType(position: Int): Int {
//        val message: UserMessage = mMessages[position] as UserMessage
//        return if (message.sender.userId == SendBird.getCurrentUser().userId) {
//            VIEW_TYPE_MESSAGE_SENT
//        } else {
//            VIEW_TYPE_MESSAGE_RECEIVED
//        }
//
//        return super.getItemViewType(position)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicViewHolder {
        Log.d(MessageActivity.TAG, "onCreateViewHolder")
        var view: View
//        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
//            view = LayoutInflater.from(mContext).inflate(R.layout.item_message_sent, parent, false)
//            return SentMessageViewHolder(view)
//        }
        view = LayoutInflater.from(mContext).inflate(R.layout.item_message_received, parent, false)
        return ReceiveMessageViewHolder(view)

    }


    override fun onBindViewHolder(holder: BasicViewHolder, position: Int) {
        Log.d(MessageActivity.TAG, "onBindViewHolder")
        holder.bindData(mMessages[position])
    }

    override fun getItemCount(): Int {
        Log.d(MessageActivity.TAG, "getItemCount - ${mMessages.size}")
        return mMessages.size
    }


}