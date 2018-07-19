package com.example.anna.mysendbird.view.view_holder

import android.util.Log
import android.view.View
import com.example.anna.mysendbird.view.activity.MessageActivity
import kotlinx.android.synthetic.main.item_message_received.view.*

class ReceiveMessageViewHolder(itemView: View) : BasicViewHolder(itemView) {

    var userProfileImage = itemView.img_profile_received
    var userNameText = itemView.txt_message_name_received
    var messageBodyText = itemView.txt_message_body_received
    var messageTimeText = itemView.txt_message_time_received

//    override fun bindData(userMessage: Message) {
////        userProfileImage.setImageResource(userMessage.sender.profileUrl)
//        userNameText.text = userMessage.sender.nickname
//        messageBodyText.text = userMessage.message
//        messageTimeText.text = userMessage.createdAt
//
//    }

    override fun bindData(userMessage: String) {
        Log.d(MessageActivity.TAG, "ReceiveMessageViewHolder - bindData userMessage - $userMessage")
//        userProfileImage.setImageResource(userMessage.sender.profileUrl)
        userNameText.text = userMessage

    }

}