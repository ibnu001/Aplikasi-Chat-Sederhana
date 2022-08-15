package com.ibnu.chatapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_user_conversation.view.*

class UserCAdapter(
    val userCList: ArrayList<UserCModel>
) : RecyclerView.Adapter<UserCAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_user_conversation, parent, false)

        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currenctUser = userCList[position]

        holder.itemView.tv_fr_uc_namaPerson.text = currenctUser.nama
        holder.itemView.tv_fr_uc_emailPerson.text = currenctUser.email

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ConversationMessageActivity::class.java)

            intent.putExtra("uid", currenctUser.uid)
            intent.putExtra("nama", currenctUser.nama)
            intent.putExtra("email", currenctUser.email)

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userCList.size
    }
}