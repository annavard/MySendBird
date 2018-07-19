package com.example.anna.mysendbird.view.view_holder


import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import com.example.anna.mysendbird.db.Channel
import com.example.anna.mysendbird.models.Product
import com.sendbird.android.UserMessage
import kotlinx.android.synthetic.main.item_product.view.*

class ChatViewHolder(itemView: View, listener: ProductItemClickListener) : RecyclerView.ViewHolder(itemView) {

    var mListener = listener

    lateinit var mChannel: Channel

    init {
        itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mListener.onProductItemClicked(mChannel)
            }
        })
    }

    var image = itemView.img_profile
    var textOtherUser = itemView.txt_other_user
    var textMessage = itemView.txt_message
    var textAbout = itemView.txt_about
    var textDate = itemView.txt_date

    fun bindData(channel: Channel) {

        mChannel = channel
        textOtherUser.text = channel.otherUserId
        textMessage.text = channel.lastMessage
        textAbout.text = channel.customType
        textDate.text = channel.messageCreatedAt

    }


    interface ProductItemClickListener {

        fun onProductItemClicked(channel: Channel)
    }
}