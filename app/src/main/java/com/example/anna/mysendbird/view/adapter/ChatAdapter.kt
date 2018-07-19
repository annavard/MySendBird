package com.example.anna.mysendbird.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.anna.mysendbird.R
import com.example.anna.mysendbird.db.Channel
import com.example.anna.mysendbird.view.view_holder.ChatViewHolder

class ChatAdapter(context: Context,
                  channels: MutableList<Channel>?,
                  listener : ChatViewHolder.ProductItemClickListener) : RecyclerView.Adapter<ChatViewHolder>() {


        var mContext =  context
        var mChannels = channels
        var mListener = listener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.item_product, parent, false)
        return ChatViewHolder(view, mListener)
    }

    override fun getItemCount(): Int {
        return mChannels?.size!!
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bindData(mChannels!![position])
    }

    fun update(list: List<Channel>) {
        mChannels?.clear()
        mChannels?.addAll(list)
        notifyDataSetChanged()
    }

}