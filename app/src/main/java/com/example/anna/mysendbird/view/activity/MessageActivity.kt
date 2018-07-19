package com.example.anna.mysendbird.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.anna.mysendbird.R
import com.example.anna.mysendbird.models.Message
import com.example.anna.mysendbird.models.User
import com.example.anna.mysendbird.view.adapter.MessageListAdapter
import kotlinx.android.synthetic.main.activity_message_list.*

class MessageActivity : AppCompatActivity(){

    companion object {
        const val TAG = "MessageActivity"
    }


    private lateinit var mAdapter : MessageListAdapter
    private lateinit var mMessages : MutableList<Message>
    private lateinit var mUser : User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)

//        mUser = User("Valodik")
//        mMessages = mutableListOf(Message(mUser, "hey!", "14:00"),
//                Message(mUser, "how are you doing?", "24:00"),
//                Message(mUser, "Call me", "02:00"))


        mAdapter = MessageListAdapter(this, mutableListOf("How it is going?", "Shut up!"))
        val layoutManager = LinearLayoutManager(this)

        reycler_message_list.adapter = mAdapter
        reycler_message_list.layoutManager = layoutManager

    }
}