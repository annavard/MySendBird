package com.example.anna.mysendbird.view.view_holder


import android.util.Log
import android.view.View
import com.example.anna.mysendbird.models.Message
import com.example.anna.mysendbird.view.activity.MessageActivity
import kotlinx.android.synthetic.main.item_message_sent.view.*

class SentMessageViewHolder(itemView: View) : BasicViewHolder(itemView){

    var messageBodyText  = itemView.txt_message_body_sent
    var messageTimeText  = itemView.txt_message_time_sent



//
//    override fun bindData(userMessage: Message) {
//        messageBodyText.text = userMessage.message
//        messageTimeText.text = userMessage.createdAt
//    }


    override fun bindData(userMessage: Message) {
        Log.d(MessageActivity.TAG, "SentMessageViewHolder - bindData userMessage - $userMessage")
//        userProfileImage.setImageResource(userMessage.sender.profileUrl)
        messageBodyText.text = userMessage.message

    }

}