package com.example.anna.mysendbird.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.anna.mysendbird.R
import com.example.anna.mysendbird.db.Channel
import com.example.anna.mysendbird.repository.ChannelRepository
import com.sendbird.android.*
import kotlinx.android.synthetic.main.activity_main.*
import com.sendbird.android.SendBird


class MainActivity : AppCompatActivity(), ChannelRepository.DbListener {

    private lateinit var mHostUserId: String

    private var mGroupChannel: GroupChannel? = null

    private lateinit var mOtherUserId: String

    private var mCustomType: String = ""

    private var mDistinctChannels: List<Channel>? = null


    companion object {
        const val TAG = "MainActivity"
        const val CHANNEL_UNIQUE_HANDLER_ID = "group_channel_handler"
        const val CONNECTION_UNIQUE_HANDLER_ID = "group_channel_handler"
    }


    override fun onResume() {
        SendBird.addChannelHandler(CHANNEL_UNIQUE_HANDLER_ID, object : SendBird.ChannelHandler() {
            override fun onMessageReceived(p0: BaseChannel?, p1: BaseMessage?) {
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

                    text_other_user.text = mOtherUserId

                    val channel = Channel(mHostUserId, mOtherUserId, p0.customType, p0.url)
                    ChannelRepository.getInstance(application)?.insertChannel(channel, this@MainActivity)
                }
//                if (p0?.getUrl() == groupChannel?.url) {
                if (p1 is UserMessage) {
                    Log.d(TAG, "onMessageReceived - message -  ${p1.message}")
                }
//                }
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

        setOnclickListeners(img_flower, img_home, img_sofa, text_host_user)

        mHostUserId = intent.getStringExtra("user_id")
        text_host_user.text = mHostUserId

        //Get the list through ViewModel later
        ChannelRepository.getInstance(application)?.loadChannelsFromDb(this, true)
    }


    private fun setOnclickListeners(vararg views: View) {
        for (view in views) {

            view.setOnClickListener { p0 ->
                when (p0?.id) {
                    img_flower.id -> {
                        mCustomType = "flower"
                        connectOtherUser(text_other_user.text.toString())
                    }
                    img_home.id -> {
                        mCustomType = "home"
                        connectOtherUser(text_other_user.text.toString())
                    }
                    img_sofa.id -> {
                        mCustomType = "sofa"
                        connectOtherUser(text_other_user.text.toString())
                    }
                }
            }
        }
    }


    private fun connectOtherUser(userId: String) {
        Log.d(TAG, "connectOtherUser - $userId")


        //TODO; Check if connected

        SendBird.connect(userId, object : SendBird.ConnectHandler {
            override fun onConnected(p0: User?, p1: SendBirdException?) {
                if (p1 != null) {
                    p1.stackTrace
                    return
                }
                Log.d(TAG, "onConnected - ${p0?.userId}")

                mOtherUserId = text_other_user.text.toString()
                mDistinctChannels = ChannelRepository.getInstance(application)?.loadChannelsFromDb(this@MainActivity, false)
            }
        })
    }


    private fun getChannel() {

        val channel = Channel(mHostUserId, mOtherUserId, mCustomType)

        if (mDistinctChannels == null || !mDistinctChannels!!.contains(channel)) {
            Log.d(TAG, "null or doesn't contain - ${channel.url}")
            createChannel()
            return
        }

        val index = mDistinctChannels!!.indexOf(channel)

        GroupChannel.getChannel(mDistinctChannels!![index].url, object : GroupChannel.GroupChannelGetHandler {
            override fun onResult(p0: GroupChannel?, p1: SendBirdException?) {
                if (p1 != null) {
                    p1.stackTrace
                    return
                }
                Log.d(TAG, "getChannel - url - ${p0?.url}")
                Log.d(TAG, "getChannel - customType - ${p0?.customType}")
                for (member in p0?.members!!) {
                    Log.d(TAG, "getChannel - member - ${member.userId}")
                }
                Log.d(TAG, "getChannel - mHostUserId - $mHostUserId")
                Log.d(TAG, "getChannel - mOtherUserId - $mOtherUserId")

                mGroupChannel = p0
                sendMessage("heylooo")
            }
        })

    }


    private fun createChannel() {
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

                        ChannelRepository.getInstance(application)?.insertChannel(Channel(mHostUserId, mOtherUserId, p0!!.customType, p0.url), this@MainActivity)
                        sendMessage("holaaa")
//                inviteOtherUser(userId)
                    }
                })
    }


    private fun sendMessage(message: String) {
        mGroupChannel?.sendUserMessage(message, object : BaseChannel.SendUserMessageHandler {
            override fun onSent(p0: UserMessage?, p1: SendBirdException?) {
                Log.d(TAG, "sendMessage - onSent - ${p0?.message}")
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


    override fun onInserted() {
//        Log.d(TAG, "onInserted")
    }

    override fun onLoaded(list: List<Channel>?, isInit: Boolean) {
//        Log.d(TAG, "onLoaded")

        mDistinctChannels = list
        if (!isInit) {
            getChannel()
        }
    }

    override fun onDeleted() {
//        Log.d(TAG, "onDeleted")
    }
}
