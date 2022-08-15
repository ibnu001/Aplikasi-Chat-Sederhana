package com.ibnu.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_conversation_message.*
import kotlinx.android.synthetic.main.activity_conversation_message.topAppBar
import kotlinx.android.synthetic.main.activity_user_conversation.*

class ConversationMessageActivity : AppCompatActivity() {

    private lateinit var etCmTextMessage: EditText
    private lateinit var fabCmSendMessage: FloatingActionButton

    private lateinit var rvCmCMessage: RecyclerView
    private lateinit var cMessageList: ArrayList<CMessageModel>
    private lateinit var cMessageAdapter: CMessageAdapter

    private lateinit var mDbRef: DatabaseReference

    var senderRoom: String? = null
    var receiverRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation_message)

        rvCmCMessage = findViewById(R.id.rv_cm_conversationMessage)

        etCmTextMessage = findViewById(R.id.et_cm_textMessage)
        fabCmSendMessage = findViewById(R.id.fab_cm_sendMessage)

        // Untuk mendapatkan name dan User UID dari lawan bicara
        // yang dikirimkan dari user adapter
        val nama = intent.getStringExtra("nama")
        val receiverUid = intent.getStringExtra("uid")

        // Untuk mendapatkan User UID dari user yang menggunakan
        val senderUidD = FirebaseAuth.getInstance().currentUser?.uid

        topAppBar.setOnClickListener {
            startActivity(Intent(this, UserConversationActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            })
        }

        topAppBar!!.title = intent.getStringExtra("nama")

        senderRoom = receiverUid + senderUidD
        receiverRoom = senderUidD + receiverUid

        cMessageList = ArrayList()
        cMessageAdapter = CMessageAdapter(cMessageList)

        rvCmCMessage.adapter = cMessageAdapter
        rvCmCMessage.layoutManager = LinearLayoutManager(this)

        rvCmCMessage.setHasFixedSize(true)

        getCMessageList()


        fabCmSendMessage.setOnClickListener {
            val messageS = etCmTextMessage.text.toString()
            val messageObject = CMessageModel(messageS, senderUidD)

            if (messageS.isNotEmpty()) {
                mDbRef.child("chats").child("personal_chat")
                    .child(senderRoom!!)
                    .child("messages")
                    .push()
                    .setValue(messageObject)
                    .addOnSuccessListener {
                        mDbRef.child("chats").child("personal_chat")
                            .child(receiverRoom!!)
                            .child("messages")
                            .push()
                            .setValue(messageObject)
                    }
            }
            etCmTextMessage.text.clear()
        }
    }

    private fun getCMessageList() {
        mDbRef = FirebaseDatabase.getInstance().reference

        mDbRef.child("chats").child("personal_chat").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    cMessageList.clear()

                    for (listOfMessage in snapshot.children) {
                        val listMessage = listOfMessage.getValue(CMessageModel::class.java)
                        cMessageList.add(listMessage!!)
                    }
                    cMessageAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}