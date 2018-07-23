package com.example.anna.mysendbird.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anna.mysendbird.R
import com.example.anna.mysendbird.models.Message
import com.example.anna.mysendbird.view.activity.MessageActivity
import com.example.anna.mysendbird.view.view_holder.BasicViewHolder
import com.example.anna.mysendbird.view.view_holder.ReceiveMessageViewHolder
import com.example.anna.mysendbird.view.view_holder.SentMessageViewHolder
import com.sendbird.android.SendBird
import com.sendbird.android.UserMessage

class MessageListAdapter(context: Context, messages: MutableList<Message>) : RecyclerView.Adapter<BasicViewHolder>() {

    companion object {
        const val VIEW_TYPE_MESSAGE_SENT = 1
        const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }

    private var mContext: Context = context
    private var mMessages: MutableList<Message> = messages


    override fun getItemViewType(position: Int): Int {
        val message = mMessages[position]
        return if (message.sender.nickname == SendBird.getCurrentUser().userId) {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
//        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicViewHolder {
//        Log.d(MessageActivity.TAG, "onCreateViewHolder")
        var view: View
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_message_sent, parent, false)
            return SentMessageViewHolder(view)
        }
        view = LayoutInflater.from(mContext).inflate(R.layout.item_message_received, parent, false)
        return ReceiveMessageViewHolder(view)

    }


    override fun onBindViewHolder(holder: BasicViewHolder, position: Int) {
//        Log.d(MessageActivity.TAG, "onBindViewHolder")
        holder.bindData(mMessages[position])
    }

    override fun getItemCount(): Int {
//        Log.d(MessageActivity.TAG, "getItemCount - ${mMessages.size}")
        return mMessages.size
    }

    fun update(message :  Message) {
//        Log.d(MessageActivity.TAG, "MessageListAdapter - update - ${messages.size}")
        mMessages.add(message)

//        for (m in mMessages){
//            Log.d(MessageActivity.TAG, "MessageListAdapter - ${m.message}")
//        }
        notifyItemInserted(mMessages.size)
    }


}