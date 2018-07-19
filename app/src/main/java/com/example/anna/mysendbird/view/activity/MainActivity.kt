package com.example.anna.mysendbird.view.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.anna.mysendbird.R
import com.example.anna.mysendbird.db.Channel
import com.example.anna.mysendbird.repository.ChannelRepository
import com.example.anna.mysendbird.view.adapter.ChatAdapter
import com.example.anna.mysendbird.view.view_holder.ChatViewHolder
import com.sendbird.android.*
import kotlinx.android.synthetic.main.activity_main.*
import com.sendbird.android.SendBird
import kotlinx.android.synthetic.main.item_product.*


class MainActivity : AppCompatActivity(), ChannelRepository.DbListener, ChatViewHolder.ProductItemClickListener {


    private lateinit var mHostUserId: String

    private var mGroupChannel: GroupChannel? = null

    private lateinit var mOtherUserId: String

    private var mCustomType: String = ""

    private lateinit var mAdapter: ChatAdapter

    private var mChannels: MutableList<Channel>? = null


    companion object {
        const val TAG = "MainActivity"
        const val CHANNEL_UNIQUE_HANDLER_ID = "group_channel_handler"
        const val CONNECTION_UNIQUE_HANDLER_ID = "group_channel_handler"
    }


    override fun onResume() {
        SendBird.addChannelHandler(CHANNEL_UNIQUE_HANDLER_ID, object : SendBird.ChannelHandler() {
            override fun onMessageReceived(p0: BaseChannel?, p1: BaseMessage?) {
                //                if (p0?.getUrl() == groupChannel?.url) {
                if (p1 is UserMessage) {
                    Log.d(TAG, "onMessageReceived - message -  ${p1.message}")
                }
//                }
                Log.d(TAG, "onMessageReceived BaseChannel - url - ${p0?.url}")
//                Log.d(TAG, "onMessageReceived mGroupChannel - url- ${mGroupChannel?.url}")


                if (p0 is GroupChannel) {
                    Log.d(TAG, "onMessageReceived BaseChannel - customType - ${p0.customType}")
                    for (member in p0.members) {
                        Log.d(TAG, member.userId)
                    }

                    mHostUserId = p0.members[0].userId
                    mOtherUserId = p0.members[1].userId

                    Log.d(TAG, "mHostUserId - $mHostUserId")
                    Log.d(TAG, "mOtherUserId - $mOtherUserId")

                    txt_other_user.text = mOtherUserId

                    val channel = Channel(mHostUserId, mOtherUserId, p0.customType, p0.url, "", "")
                    ChannelRepository.getInstance(application)?.insertChannel(channel, this@MainActivity, false, Runnable { })
                }
            }
        })

        SendBird.addConnectionHandler(CONNECTION_UNIQUE_HANDLER_ID, object : SendBird.ConnectionHandler {
            override fun onReconnectStarted() {
                Log.d(TAG, "onReconnectStarted")
            }

            override fun onReconnectSucceeded() {
                Log.d(TAG, "onReconnectSucceeded")
            }

            override fun onReconnectFailed() {
                Log.d(TAG, "onReconnectFailed")
            }
        })
        super.onResume()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHostUserId = intent.getStringExtra("user_id")
        text_host_user.text = mHostUserId


        //Get the list through ViewModel later
        ChannelRepository.getInstance(application)?.loadChannelsFromDb(this, true, Runnable { })


    }


    private fun initiateMessage(userId: String, customType: String, isInit: Boolean, runnable: Runnable) {
        Log.d(TAG, "initiateMessage - userId - $userId - customType - $customType - isInit - $isInit")
        mCustomType = customType
        mOtherUserId = userId
        getChannel(isInit, runnable)
    }


    private fun getChannel(isInit: Boolean, runnable: Runnable) {

        val channel = Channel(mHostUserId, mOtherUserId, mCustomType, "", "")

        if (mChannels == null || !mChannels!!.contains(channel)) {
            Log.d(TAG, "null or doesn't contain - ${channel.url}")
            createChannel(isInit, runnable)
            return
        }

        val index = mChannels!!.indexOf(channel)

        GroupChannel.getChannel(mChannels!![index].url, object : GroupChannel.GroupChannelGetHandler {
            override fun onResult(p0: GroupChannel?, p1: SendBirdException?) {
                if (p1 != null) {
                    p1.stackTrace
                    return
                }
                Log.d(TAG, "getChannel - url - ${p0?.url}")
                Log.d(TAG, "getChannel - customType - ${p0?.customType}")
//                for (member in p0?.members!!) {
//                    Log.d(TAG, "getChannel - member - ${member.userId}")
//                }
//                Log.d(TAG, "getChannel - mHostUserId - $mHostUserId")
//                Log.d(TAG, "getChannel - mOtherUserId - $mOtherUserId")

                mGroupChannel = p0
                sendMessage("heylooo")
            }
        })
    }


    private fun createChannel(isInit: Boolean, runnable: Runnable) {
        var members = mutableListOf(mHostUserId, mOtherUserId)
        GroupChannel.createChannelWithUserIds(members,
                false,
                null,
                null,
                null,
                mCustomType,
                object : GroupChannel.GroupChannelCreateHandler {
                    override fun onResult(p0: GroupChannel?, p1: SendBirdException?) {
                        if (p1 != null) {
                            p1.stackTrace
                            return
                        }
                        Log.d(TAG, "createChannelWithUserIds - URL - ${p0?.url}")
                        Log.d(TAG, "createChannelWithUserIds - customType - ${p0?.customType}")

                        mGroupChannel = p0

                        val channel = Channel(p0!!.url, mHostUserId, mOtherUserId, p0.customType, "", "")
                        ChannelRepository.getInstance(application)?.insertChannel(channel, this@MainActivity, isInit, runnable)
                        sendMessage("holaaa")
//                inviteOtherUser(userId)
                    }
                })
    }


    private fun sendMessage(message: String) {
        mGroupChannel?.sendUserMessage(message, object : BaseChannel.SendUserMessageHandler {
            override fun onSent(p0: UserMessage?, p1: SendBirdException?) {
                Log.d(TAG, "sendMessage - onSent - ${p0?.message}")
                ChannelRepository.getInstance(application)?.updateChannel(mGroupChannel!!)
            }
        })
    }


    private fun inviteOtherUser(userId: String) {
        SendBird.setChannelInvitationPreference(true, object : SendBird.SetChannelInvitationPreferenceHandler {
            override fun onResult(p0: SendBirdException?) {
                if (p0 != null) {
                    p0.stackTrace
                    return
                }
                Log.d(TAG, "setChannelInvitationPreference - onResult")

                mGroupChannel?.inviteWithUserId(userId, object : GroupChannel.GroupChannelInviteHandler {
                    override fun onResult(p0: SendBirdException?) {
                        if (p0 != null) {
                            p0.stackTrace
                            return
                        }
                        Log.d(TAG, "inviteWithUserId - onResult")
                        sendMessage("gdhahsgdshgdhjahsdjlkaDJSGLKsghdjashjfgsdgf")
                    }
                })
            }
        })
    }

    override fun onPause() {
        SendBird.removeChannelHandler(CHANNEL_UNIQUE_HANDLER_ID)
        SendBird.removeConnectionHandler(CONNECTION_UNIQUE_HANDLER_ID)
        super.onPause()
    }


    override fun onInserted(isInit: Boolean, runnable: Runnable) {
        Log.d(TAG, "onInserted")
        ChannelRepository.getInstance(application)?.loadChannelsFromDb(this, isInit, runnable)

    }

    override fun onLoaded(list: MutableList<Channel>, isInit: Boolean, runnable: Runnable) {
        Log.d(TAG, "onLoaded - isInit")
        Log.d(TAG, "onLoaded - list size - ${list?.size}")

        if (list.isEmpty()) {

            initiateMessage("Rosie", "flower", true, Runnable {
                initiateMessage("Nelson", "electricity", false, Runnable {
                    initiateMessage("Harry", "Lamp", false, Runnable {

                    })
                })
            })

            return
        }
        if (isInit) {
            runOnUiThread {
                mChannels = list
                mAdapter = ChatAdapter(this, mChannels, this)
                recycler_product_list.adapter = mAdapter
                recycler_product_list.layoutManager = LinearLayoutManager(this)
                runnable.run()

            }
            return
        }

        runOnUiThread {
            mAdapter.update(list)
            runnable.run()
        }

    }

    override fun onDeleted() {
//        Log.d(TAG, "onDeleted")
    }


    override fun onProductItemClicked(channel: Channel) {

        //TODO; pass channel
        intent = Intent(this, MessageActivity::class.java)
        startActivity(intent)
    }
}
