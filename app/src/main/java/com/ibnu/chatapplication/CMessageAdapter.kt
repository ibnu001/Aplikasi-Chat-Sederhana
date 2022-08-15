package com.ibnu.chatapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class CMessageAdapter(
    val cmMessageList: ArrayList<CMessageModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.tv_fr_cm_sendMessage)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage = itemView.findViewById<TextView>(R.id.tv_fr_cm_receiveMessage)
    }

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_cm_receive_message, parent, false)
            return ReceiveViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_cm_send_message, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = cmMessageList[position]

        if (holder.javaClass == SentViewHolder::class.java) {
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
            Log.e("TAG","${currentMessage.message} ${currentMessage.senderUid}")
        } else {
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text = currentMessage.message
        }
    }

    override fun getItemCount(): Int {
        return cmMessageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = cmMessageList[position]

        if (FirebaseAuth.getInstance().currentUser!!.uid.equals(currentMessage.senderUid)) {
            return ITEM_SENT
        } else {
            return ITEM_RECEIVE
        }
    }

}