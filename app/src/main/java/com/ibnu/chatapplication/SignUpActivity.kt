package com.ibnu.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlin.math.log

class SignUpActivity : AppCompatActivity() {

    private lateinit var etSuNama: EditText
    private lateinit var etSuEmail: EditText
    private lateinit var etSuPassword: EditText

    private lateinit var btnSuSignUp: Button
    private lateinit var btnSuSignIn: Button

    lateinit var auth: FirebaseAuth
    lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        etSuNama = findViewById(R.id.et_su_nama)
        etSuEmail = findViewById(R.id.et_su_email)
        etSuPassword = findViewById(R.id.et_su_password)

        btnSuSignUp = findViewById(R.id.btn_su_signUp)
        btnSuSignIn = findViewById(R.id.btn_su_signIn)

        auth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        etSuNama.doOnTextChanged { text, start, before, count ->
            etLayoutNama(null)
        }

        etSuEmail.doOnTextChanged { text, start, before, count ->
            etLayoutEmail(null)
        }

        etSuPassword.doOnTextChanged { text, start, before, count ->
            etLayoutPassword(null)
        }

        btnSuSignUp.setOnClickListener {
            var nama = etSuNama.text.toString()
            var email = etSuEmail.text.toString()
            var password = etSuPassword.text.toString()

            when {
                nama.isEmpty() && email.isEmpty() && password.isEmpty() -> {
                    etLayoutNama("Tolong isi nama anda!!")
                    etLayoutEmail("Tolong isi email anda!!")
                    etLayoutPassword("Tolong isi password anda!!")
                    etSuNama.requestFocus()
                }
                nama.isEmpty() -> {
                    etLayoutNama("Tolong isi nama anda!!")
                    etSuNama.requestFocus()
                }

                email.isEmpty() -> {
                    etLayoutEmail("Tolong isi email anda!!")
                    etSuEmail.requestFocus()
                }

                password.isEmpty() -> {
                    etLayoutPassword("Tolong isi password anda!!")
                    etSuPassword.requestFocus()
                }

                else -> {
                    signUp(email, password)
                }
            }
        }

        btnSuSignIn.setOnClickListener {
            pindahKeSignIn()
        }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Auth()

                    showToast("Berhasil yeay")
                } else {
                    showToast("Yah Gagal")
                }

            }
    }

    private fun Auth() {
        var nama = etSuNama.text.toString()
        var email = etSuEmail.text.toString()
        var password = etSuPassword.text.toString()

        var uid = auth.uid.toString()

        mDbRef.child("user").child(uid)
            .setValue(SignUpAuthModel(uid, nama, email, password))

        logout()

        etSuNama.text.clear()
        etSuEmail.text.clear()
        etSuPassword.text.clear()
    }

    private fun pindahKeSignIn() {
        Intent(this, SignInActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    private fun logout() {
        if (true) {
            Firebase.auth.signOut()
        }
    }

    private fun showToast(note: String) {
        Toast.makeText(this, note, Toast.LENGTH_SHORT).show()
    }

    private fun etLayoutNama(note: String?) {
        et_layout_su_nama.error = note
    }

    private fun etLayoutEmail(note: String?) {
        et_layout_su_email.error = note
    }

    private fun etLayoutPassword(note: String?) {
        et_layout_su_password.error = note
    }
}