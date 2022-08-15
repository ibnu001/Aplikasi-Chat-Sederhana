package com.ibnu.chatapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_user_conversation.*


class UserConversationActivity : AppCompatActivity() {

    lateinit var rvUcListPerson: RecyclerView
    lateinit var userCList: ArrayList<UserCModel>

    lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_conversation)

        rvUcListPerson = findViewById(R.id.rv_uc_listPerson)

        rvUcListPerson.layoutManager = LinearLayoutManager(this)
        rvUcListPerson.setHasFixedSize(true)

        userCList = ArrayList()
        getuserCList()

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_tab_logout -> {
                    // Handle logout icon press
                    logout()
                    true
                }
                else -> false
            }
        }

    }

    private fun getuserCList() {
        // read data from realtime database

        mDbRef = FirebaseDatabase.getInstance().getReference("user")

        mDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userCList.clear()

                    for (listOfUser in snapshot.children) {
                        val listUser = listOfUser.getValue(UserCModel::class.java)
                        if (Firebase.auth.uid != listUser!!.uid){
                            userCList.add(listUser)
                        }
                    }
                    rvUcListPerson.adapter = UserCAdapter(userCList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun logout() {
        if (true) {
            Firebase.auth.signOut()

            Intent(this, SignInActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
    }
}