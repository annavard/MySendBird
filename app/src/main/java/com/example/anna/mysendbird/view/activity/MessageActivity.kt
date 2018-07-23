package com.example.anna.mysendbird.view.activity

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.anna.mysendbird.R
import com.example.anna.mysendbird.db.Channel
import com.example.anna.mysendbird.models.Message
import com.example.anna.mysendbird.models.User
import com.example.anna.mysendbird.repository.ChannelRepository
import com.example.anna.mysendbird.view.adapter.MessageListAdapter
import com.sendbird.android.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_message_list.*
import kotlinx.android.synthetic.main.item_product.*


class MessageActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MessageActivity"
    }


    private lateinit var mAdapter: MessageListAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mMessages: MutableList<Message> = mutableListOf()
    private lateinit var mUser: User
    private lateinit var mUrl: String
    private lateinit var mGroupChannel: GroupChannel


    override fun onResume() {
        SendBird.addChannelHandler(MainActivity.CHANNEL_UNIQUE_HANDLER_ID, object : SendBird.ChannelHandler() {
            override fun onMessageReceived(p0: BaseChannel?, p1: BaseMessage?) {
                if (p1 is UserMessage) {
                    Log.d(MainActivity.TAG, "onMessageReceived - message -  ${p1.message}")
                }
                Log.d(MainActivity.TAG, "onMessageReceived BaseChannel - url - ${p0?.url}")
                loadLastMessage()
            }
        })
        super.onResume()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)

        mUrl = intent.getStringExtra("url")

        GroupChannel.getChannel(mUrl, object : GroupChannel.GroupChannelGetHandler {
            override fun onResult(p0: GroupChannel?, p1: SendBirdException?) {
                if (p1 != null) {
                    p1.stackTrace
                    return
                }
                Log.d(TAG, "getChannel - ${p0?.url}")
                mGroupChannel = p0!!

                loadMessages(true)

            }
        })


        button_chatbox_send.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d(TAG, "button_chatbox_send - onClick")
                var message = edittext_chatbox.text.toString()
                sendMessage(message)
            }
        })

    }


    private fun loadMessages(isInit: Boolean) {
        var messageQuery = mGroupChannel.createPreviousMessageListQuery()
        messageQuery?.load(20, true, object : PreviousMessageListQuery.MessageListQueryResult {
            override fun onResult(p0: MutableList<BaseMessage>?, p1: SendBirdException?) {
                if (p1 != null) {
                    p1.stackTrace
                    return
                }
                Log.d(TAG, "PreviousMessageListQuery - onResult")
                if (p0 != null) {
                    mMessages.clear()
                    for (message in p0) {
                        Log.d(TAG, "PreviousMessageListQuery - onResult - list - ${(message as UserMessage).message}")
                        mMessages.add(Message(User((message as UserMessage).sender.userId), (message as UserMessage).message, (message as UserMessage).createdAt.toString()))
                    }

                    for (message in mMessages) {
                        Log.d(TAG, "PreviousMessageListQuery - onResult - isinit - list - ${message.message}")
                    }
                    mAdapter = MessageListAdapter(this@MessageActivity, mMessages)
                    reycler_message_list.adapter = mAdapter
                    mLayoutManager = LinearLayoutManager(this@MessageActivity)
                    reycler_message_list.layoutManager = mLayoutManager
                }
            }
        })
    }

    private fun loadLastMessage() {

        for (message in mMessages) {
//            Log.d(TAG, "PreviousMessageListQuery - onResult - update - list - ${message.message}")
        }
        val message = Message(User((mGroupChannel.lastMessage as UserMessage).sender.userId), (mGroupChannel.lastMessage as UserMessage).message, mGroupChannel.lastMessage.createdAt.toString())
        mAdapter.update(message)
        scrollToLastPosition()

    }

    private fun sendMessage(message: String) {
        if (message.isEmpty()) return
        mGroupChannel?.sendUserMessage(message, object : BaseChannel.SendUserMessageHandler {
            override fun onSent(p0: UserMessage?, p1: SendBirdException?) {
                Log.d(MessageActivity.TAG, "sendMessage - onSent - ${p0?.message}")

                edittext_chatbox.setText("")
                loadLastMessage()

            }
        })
    }

    private fun scrollToLastPosition() {
        if (mLayoutManager.findLastCompletelyVisibleItemPosition() < mLayoutManager.itemCount - 1) {
            reycler_message_list.smoothScrollToPosition(mLayoutManager.getItemCount() - 1)
        }
    }

}